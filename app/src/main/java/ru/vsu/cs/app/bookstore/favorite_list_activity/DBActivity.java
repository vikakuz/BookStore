package ru.vsu.cs.app.bookstore.favorite_list_activity;

import android.app.Activity;
import android.content.Intent;
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
//android.support.v7.app.ActionBarActivity;
import java.sql.SQLException;
import java.util.List;

import ru.vsu.cs.app.bookstore.R;
import ru.vsu.cs.app.bookstore.detailed_info_activity.FullInfoActivity;
import ru.vsu.cs.app.bookstore.search_activity.BookObject;
import ru.vsu.cs.app.bookstore.search_activity.RecordsAdapter;
import ru.vsu.cs.app.bookstore.search_activity.SearchActivity;

public class DBActivity extends Activity{

    private TextView info;
    private ListView recordsList;
    RecordsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        DBHelper dbHelper = DBHelper.getInstance(this);

        info = (TextView) findViewById(R.id.text_info);
        recordsList = (ListView) findViewById(R.id.list_favorite_data);
        List<BookObject> records = null;
        try {
            records = dbHelper.getBookObjectDao().getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (records != null && !records.isEmpty()) {
            adapter = new RecordsAdapter(DBActivity.this, R.layout.item_list, records);
            recordsList.setAdapter(adapter);
            recordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    Intent intent = new Intent(DBActivity.this, FullInfoActivity.class);
                    intent.putExtra(FullInfoActivity.EXTRA_BOOK, adapter.getItem(pos));
                    startActivity(intent);
                }
            });
            info.setText("");
        } else {
            info.setText(R.string.text_list_is_empty);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorite_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.action_search:
                Intent intent = new Intent(DBActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
