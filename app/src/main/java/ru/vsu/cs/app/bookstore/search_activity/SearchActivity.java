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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import ru.vsu.cs.app.bookstore.R;
import ru.vsu.cs.app.bookstore.detailed_info_activity.FullInfoActivity;


public class SearchActivity extends Activity {

    private EditText text_search;
    private ImageButton btn_search;
    private TextView statusAndInfo;
    private ListView recordsList;
    private ProgressBar isLoading;

    private GoogleBooksAPIRequest booksAPIRequest;
    RecordsAdapter adapter;

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
        recordsList = (ListView) findViewById(R.id.list_data);
        isLoading = (ProgressBar) findViewById(R.id.progress_bar);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut(text_search.getText().toString());
                if (!(text_search.getText().toString().toUpperCase().startsWith("ВВЕДИТЕ")
                        || text_search.getText().toString().isEmpty())){
                    logOut("Кнопка нажата");
                    booksAPIRequest = new GoogleBooksAPIRequest();
                    booksAPIRequest.execute(text_search.getText().toString().replace("\\s+", "\\s"));
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

        String logs;
        ArrayList<BookObject> records;

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
                    logs = "Impossible: The only two URLs used in the app are taken from string resources.";
                    // Impossible: The only two URLs used in the app are taken from string resources.
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    logs = "Impossible: GET is a perfectly valid request method.";
                    // Impossible: "GET" is a perfectly valid request method.
                    e.printStackTrace();
                }

                int responseCode = connection.getResponseCode();
                if(responseCode != 200){//если ошибка
                    logs ="GoogleBooksAPI request failed. Response Code: " + responseCode;
                    //Log.w(getClass().getName(), "GoogleBooksAPI request failed. Response Code: " + responseCode);
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
//                InputStream inputStream = connection.getInputStream();
                String responseString = builder.toString();
//                //TODO
                records = new ArrayList<BookObject>();
                records = new BookObjectParser().parse(responseString);


                // Close connection and return response code.
               // inputStream.close();
                connection.disconnect();

                return responseString;
            } catch (SocketTimeoutException e) {
                logs = "Connection timed out. Return null";
                Log.w(getClass().getName(), "Connection timed out. Return null");
                return null;
            } catch(IOException e){
                logs = "IOException when connecting to Google Books API.";
                Log.w(getClass().getName(), "IOException when connecting to Google Books API.");
                e.printStackTrace();
                return null;
              } catch (JSONException e) {
                logs = "JSONException when parse response.";
                Log.w(getClass().getName(), "JSONException when parse response.");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonObject){//имеет доступ, для вывода результатов
            isLoading.setVisibility(View.INVISIBLE);

            //должно быть верно
            if (!records.isEmpty()) {
                adapter = new RecordsAdapter(SearchActivity.this, R.layout.item_list, records);
                recordsList.setAdapter(adapter);
                recordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        Intent intent = new Intent(SearchActivity.this, FullInfoActivity.class);
                        intent.putExtra(FullInfoActivity.EXTRA_BOOK, adapter.getItem(pos));
                        startActivity(intent);
                    }
                });
            } else {
                logs = "Соответствий не найдено";
            }

            //if (logs.isEmpty()){
               statusAndInfo.setMaxLines(10);
               statusAndInfo.setText(jsonObject);
           // } else {
                logOut(logs);
            //}

        }

        private String readResponse(InputStream inputStream) throws IOException{
            StringBuilder builder = new StringBuilder();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = responseReader.readLine();
            while (line != null){
                builder.append(line);
                line = responseReader.readLine();
            }
            return builder.toString();
        }


        private class BookObjectParser implements Parser<BookObject> {

            public ArrayList<BookObject> records = new ArrayList<BookObject>();

            @Override
            public ArrayList<BookObject> parse(String json) throws JSONException{
                JSONObject response = new JSONObject(json);
                JSONArray dataArray = response.getJSONArray("items");

                for (int i = 0; i < dataArray.length(); i++){
                    BookObject bookObject = new BookObject();

                    StringBuilder data = new StringBuilder();

                    JSONObject volumeInfo = dataArray.getJSONObject(i).getJSONObject("volumeInfo");
                    bookObject.setTitle(volumeInfo.getString("title"));//название

                    JSONArray authors = volumeInfo.getJSONArray("authors");//авторы
                    for (int j = 0; j < authors.length(); j++ ){
                        data.append(authors.get(i))
                                .append(", ");
                    }
                    bookObject.setAuthors(data.toString());
                    data.setLength(0);

                    bookObject.setDescription(volumeInfo.optString("description"));//описание
                    if (bookObject.getDescription().isEmpty()){
                        bookObject.setDescription("Нет краткого описания");

                    }

                    bookObject.setPageCount(volumeInfo.optInt("pageCount"));//кол-во страниц

                    JSONArray category = volumeInfo.optJSONArray("categories");//жанр
                    if (category!= null) {
                        for (int j = 0; j < category.length(); j++) {
                            data.append(category.optString(i))
                                    .append(", ");
                        }
                        bookObject.setCategory(data.toString());
                        data.setLength(0);
                    } else {
                        bookObject.setCategory(null);
                    }

                    //TODO images
                    JSONObject covers = volumeInfo.getJSONObject("imageLinks");//
                    bookObject.setSmallCover(covers.optString("smallThumbnail"),
                                            getApplicationContext(),
                                            R.id.image_book_cover);
                    if (bookObject.getSmallCover().toString().isEmpty()) {
                        logs = "Не удалось найти smallThumbnail.";
                    }

                    bookObject.setBigCover(covers.optString("thumbnail"),
                                            getApplicationContext(),
                                            R.id.image_cover);
                    if (bookObject.getBigCover().toString().isEmpty()) {
                        logs = "Не удалось найти thumbnail.";
                    }

                    bookObject.setLanguage(volumeInfo.optString("language"));//язык

                    JSONObject saleInfo = dataArray.getJSONObject(i).getJSONObject("saleInfo");
                    bookObject.setEBook(saleInfo.optBoolean("isEbook"));//isEBook

                    if (saleInfo.optString("saleability").isEmpty()//со скидкой
                            || saleInfo.optString("saleability").toUpperCase().equals("NOT_FOR_SALE")){
                        bookObject.setForSale(false);
                    } else {
                        bookObject.setForSale(true);
                    }

                    records.add(bookObject);
                }

                return records;
            }

        }

    }
}
