package ru.vsu.cs.app.bookstore.search_activty;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by V on 11.11.2014.
 */
public class GoogleBooksAPIRequest extends AsyncTask<String, Object, String> {

    private InternetConnection internetConnection = new InternetConnection();

    //выполняются в таком же порядке
    @Override
    protected void onPreExecute() {//имеет доступ к UI, по сути для сбора нужной инф-ии
       // super.onPreExecute();
        // Check network connection.

        if(!internetConnection.checkNetworkConnection()){
            // Cancel request.
            Log.i(getClass().getName(), "Not connected to the internet");
            cancel(true);
            return;
        }
        //connectionInternet.checkNetworkConnection();
    }

    @Override
    protected String doInBackground(String... params) {//не имеет доступ к UI, вся логика и обработка данных
        //return null;
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
        }
//        } catch (JSONException e) {
//            Log.d(getClass().getName(), "JSONException when connecting to Google Books API.");
//            e.printStackTrace();
//            return null;
//        }
    }

    @Override
    protected void onPostExecute(String jsonObject) {//имеет доступ, для вывода результатов
        super.onPostExecute(jsonObject);
    }

    private class InternetConnection extends Activity{

        private static final String DEBUG_TAG = "debugTag=";

        public boolean checkNetworkConnection() {
            ConnectivityManager connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMngr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                return true;
            } else {
                return false;
            }

        }
        /*private String readIt(InputStream stream, int len)
            throws IOException {
            Reader reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

        private String downloadUrl(String myUrl)
            throws IOException {

            InputStream is = null;
            int len = 500;//показывает только первые 500 символов страницы

            try {
                URL url = new URL(myUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);//миллисекунды
                conn.setConnectTimeout(15000);//миллисекунды
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                int response = conn.getResponseCode();
                Log.d(DEBUG_TAG, "The response is " + response);
                is = conn.getInputStream();

                //инпутстрим в строку
                String convertasString = readIt(is, len);
                return convertasString;
            } finally {
                if (is != null){
                    is.close();
                }
            }
        }*/
    }

}
