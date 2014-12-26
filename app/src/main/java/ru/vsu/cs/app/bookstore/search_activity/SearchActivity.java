package ru.vsu.cs.app.bookstore.search_activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import ru.vsu.cs.app.bookstore.R;
import ru.vsu.cs.app.bookstore.detailed_info_activity.FullInfoActivity;
import ru.vsu.cs.app.bookstore.favorite_list_activity.DBActivity;


public class SearchActivity extends Activity {

    private EditText text_search;
    private ImageButton btn_search;
    private TextView statusAndInfo;
    private ListView recordsList;
    private ProgressBar isLoading;

    private GoogleBooksAPIRequest booksAPIRequest;
    RecordsAdapter adapter;
    ArrayList<BookObject> records = new ArrayList<BookObject>();

    private void logOut(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        adapter = new RecordsAdapter(SearchActivity.this, R.layout.item_list, records);

        text_search = (EditText) findViewById(R.id.text_search);
        btn_search = (ImageButton) findViewById(R.id.btn_search);btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(text_search.getText().toString().toUpperCase().startsWith("ВВЕДИТЕ")
                        || text_search.getText().toString().isEmpty())){
                    booksAPIRequest = new GoogleBooksAPIRequest();
                    booksAPIRequest.execute(text_search.getText().toString().replace("\\s+", "+"));

                }

            }
        });

        statusAndInfo = (TextView) findViewById(R.id.text_search_result);
        recordsList = (ListView) findViewById(R.id.list_search_data);
        recordsList.setAdapter(adapter);
        recordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent intent = new Intent(SearchActivity.this, FullInfoActivity.class);
                intent.putExtra(FullInfoActivity.EXTRA_BOOK, adapter.getItem(pos));
                startActivity(intent);
            }
        });

        isLoading = (ProgressBar) findViewById(R.id.progress_bar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_favorites:
                Intent intent = new Intent(SearchActivity.this, DBActivity.class);
                //intent.putExtra(FullInfoActivity.EXTRA_BOOK, adapter.getItem(pos));
                startActivity(intent);
                break;
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private class GoogleBooksAPIRequest extends AsyncTask<String, Object, String> {

        String logs;

        //выполняются в таком же порядке
        @Override
        protected void onPreExecute() {//имеет доступ к UI, по сути для сбора нужной инф-ии
            // Check network connection.
            isLoading.setVisibility(View.VISIBLE);

            ConnectivityManager connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMngr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                logOut("Соединение установлено");
            } else {
                logOut("Отсутствует интернет соединение");
                cancel(true);
            }
        }

        @Override
        protected String doInBackground(String... params) {//не имеет доступ к UI, вся логика и обработка данных
            // Stop if cancelled
            if(isCancelled()){
                return null;
            }

            //String apiUrlString = "https://www.googleapis.com/books/v1/volumes?q=" + params[0];//простой поиск
            String apiUrlString = "https://www.googleapis.com/books/v1/volumes?q=" + params[0];
            try{
                HttpURLConnection connection = null;
                // устанавливаем соединение
                try{
                    URL url = new URL(apiUrlString);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000); // 5 seconds
                    connection.setConnectTimeout(5000); // 5 seconds
                } catch (MalformedURLException e) {
                    logs += "Impossible: The only two URLs used in the app are taken from string resources.";
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    logs += "Impossible: GET is a perfectly valid request method.";
                    e.printStackTrace();
                }

                int responseCode = connection.getResponseCode();
                if(responseCode != 200){//если ошибка
                    logs +="GoogleBooksAPI request failed. Response Code: " + responseCode;
                    connection.disconnect();
                    return null;
                }

                //чтение данных от апи
                StringBuilder builder = new StringBuilder();
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = responseReader.readLine();
                while (line != null){
                    builder.append(line);
                    line = responseReader.readLine();
                }
                String responseString = builder.toString();
                records.clear();
                records.addAll(new BookObjectParser().parse(responseString));


                // Close connection and return response code.
                connection.disconnect();
                return responseString;
            } catch (SocketTimeoutException e) {
                logs += "Connection timed out. Return null";
                Log.w(getClass().getName(), "Connection timed out. Return null");
                return null;
            } catch(IOException e){
                logs += "IOException when connecting to Google Books API.";
                Log.w(getClass().getName(), "IOException when connecting to Google Books API.");
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                logs += "JSONException when parse response.";
                Log.w(getClass().getName(), "JSONException when parse response.");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonObject) {//имеет доступ, для вывода результатов
            //должно быть верно
           // adapter.notifyDataSetChanged();
            if (records != null && !records.isEmpty()) {
                statusAndInfo.setText("");
            } else {
                statusAndInfo.setText(R.string.no_records);
                // logs = "Соответствий не найдено";
            }
            adapter.notifyDataSetChanged();
        }

    }
}
