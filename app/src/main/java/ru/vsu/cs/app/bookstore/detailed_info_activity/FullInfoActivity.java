package ru.vsu.cs.app.bookstore.detailed_info_activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import ru.vsu.cs.app.bookstore.R;
import ru.vsu.cs.app.bookstore.search_activity.BookObject;

public class FullInfoActivity extends Activity {

    public static final String EXTRA_BOOK = "book";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info);

        BookObject extraBook = (BookObject) getIntent().getSerializableExtra(EXTRA_BOOK);
        //имя
//        TextView name = (TextView) findViewById(R.id.textName);
//        name.setText(extraBook.getName());
//        //автор
//        TextView author =(TextView) findViewById(R.id.textAuthor);
//        author.setText(extraBook.getAuthor());
//        //цена
//        TextView cost = (TextView) findViewById(R.id.textCost);
//        cost.setText(getString(R.string.price, (int) extraBook.getCost()));
//        //краткое описание
//        TextView description = (TextView) findViewById(R.id.textOfDescription);
//        description.setText(extraBook.getDiscription());
//        //обложка
//        ImageView userImage = (ImageView) findViewById(R.id.imageCover);
//        userImage.setImageResource(extraBook.getCover());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.full_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
