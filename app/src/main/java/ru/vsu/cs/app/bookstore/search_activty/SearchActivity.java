package ru.vsu.cs.app.bookstore.search_activty;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.vsu.cs.app.bookstore.R;


public class SearchActivity extends Activity {

    private EditText text_search;
    private ImageButton btn_search;
    private TextView statusAndInfo;
    private ListView records;
    private ProgressBar isLoading;

    private GoogleBooksAPIRequest booksAPIRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        text_search = (EditText) findViewById(R.id.text_search);
        btn_search = (ImageButton) findViewById(R.id.btn_search);
       // records = (ListView) findViewById(R.id.list_data);

        text_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Log.i("event", "captured");
                    statusAndInfo.setText("Введите слово для поиска");
                    return false;
                }

                return false;
            }
        });

        text_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                statusAndInfo.setText("Тект  не меняется");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                statusAndInfo.setText("Текст вводят...");
            }

            @Override
            public void afterTextChanged(Editable s) {
                statusAndInfo.setText("Текст ввели");
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusAndInfo = (TextView) findViewById(R.id.text_empty);
                if (!text_search.getText().toString().equals("Поиск книги")){
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

}
