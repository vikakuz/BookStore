package ru.vsu.cs.app.bookstore.search_activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import ru.vsu.cs.app.bookstore.R;


public class SearchActivity extends Activity {

    private EditText text_search;
    private ImageButton btn_search;
    private TextView statusAndInfo;
    private ListView records;
    private ProgressBar isLoading;

    private GoogleBooksAPIRequest booksAPIRequest;

    private void logOut(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        text_search = (EditText) findViewById(R.id.text_search);
        btn_search = (ImageButton) findViewById(R.id.btn_search);
        statusAndInfo = (TextView) findViewById(R.id.text_empty);
        records = (ListView) findViewById(R.id.list_data);
        isLoading = (ProgressBar) findViewById(R.id.progress_bar);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!text_search.getText().toString().toUpperCase().startsWith("ВВЕДИТЕ")
                        || !text_search.getText().toString().isEmpty()){
                    logOut("Кнопка нажата");
                    booksAPIRequest = new GoogleBooksAPIRequest();
                    String[] params = text_search.getText().toString().split("\\s+");
                    booksAPIRequest.execute(params);
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class GoogleBooksAPIRequest extends AsyncTask<String, Object, String> {

        //выполняются в таком же порядке
        @Override
        protected void onPreExecute() {//имеет доступ к UI, по сути для сбора нужной инф-ии
            // super.onPreExecute();
            // Check network connection.

            isLoading.setVisibility(View.VISIBLE);

            ConnectivityManager connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMngr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                logOut("Соединение установлено");
                return;
            } else {
                Log.i(getClass().getName(), "Not connected to the internet");
                logOut("Отсутствует интернет соединение");
                //cancel(true);
                return;
            }
        }

        @Override
        protected String doInBackground(String... params) {//не имеет доступ к UI, вся логика и обработка данных
            //return null;
            // Stop if cancelled

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*if(isCancelled()){
                return null;
            }

            String apiUrlString = "https://www.googleapis.com/books/v1/volumes?q=" + params[0];//простой поиск
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
                    // Impossible: The only two URLs used in the app are taken from string resources.
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    // Impossible: "GET" is a perfectly valid request method.
                    e.printStackTrace();
                }

                int responseCode = connection.getResponseCode();
                if(responseCode != 200){//если ошибка
                    Log.w(getClass().getName(), "GoogleBooksAPI request failed. Response Code: " + responseCode);
                    connection.disconnect();
                    return null;
                }

                //данные от апи
                StringBuilder builder = new StringBuilder();
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = responseReader.readLine();
                while (line != null){
                    builder.append(line);
                    line = responseReader.readLine();
                }
                String responseString = builder.toString();

                Log.d(getClass().getName(), "Response String: " + responseString);
                //JSONObject responseJson = new JSONObject(responseString);
                // Close connection and return response code.
                connection.disconnect();
                return responseString;
                //return responseJson;
            } catch (SocketTimeoutException e) {
                Log.w(getClass().getName(), "Connection timed out. Return null");
                return null;
            } catch(IOException e){
                Log.d(getClass().getName(), "IOException when connecting to Google Books API.");
                e.printStackTrace();
                return null;
            }*/
//        } catch (JSONException e) {
//            Log.d(getClass().getName(), "JSONException when connecting to Google Books API.");
//            e.printStackTrace();
//            return null;
//        }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonObject) {//имеет доступ, для вывода результатов
            isLoading.setVisibility(View.INVISIBLE);
           // super.onPostExecute(jsonObject);
        }
    }
}
