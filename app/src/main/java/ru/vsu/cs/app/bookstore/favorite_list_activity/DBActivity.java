package ru.vsu.cs.app.bookstore.favorite_list_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
//android.support.v7.app.ActionBarActivity;
import java.sql.SQLException;

import ru.vsu.cs.app.bookstore.R;
import ru.vsu.cs.app.bookstore.search_activity.BookObject;
import ru.vsu.cs.app.bookstore.search_activity.SearchActivity;

public class DBActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
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
