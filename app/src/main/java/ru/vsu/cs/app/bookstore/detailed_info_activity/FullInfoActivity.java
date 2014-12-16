package ru.vsu.cs.app.bookstore.detailed_info_activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import ru.vsu.cs.app.bookstore.R;
import ru.vsu.cs.app.bookstore.search_activity.BookObject;

public class FullInfoActivity extends Activity {

    public static final String EXTRA_BOOK = "book";

    public TextView title;
    public TextView author;
    public TextView language;
    public TextView category;
    public TextView cost;
    public TextView description;

    public ImageView cover;

    public ImageButton buy;
    public ImageButton favorite;
    public ImageButton full_info;

    private AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info);

        aq = new AQuery(this.getApplicationContext());
        BookObject extraBook = (BookObject) getIntent().getSerializableExtra(EXTRA_BOOK);
        //имя
        title = (TextView) findViewById(R.id.text_title);
        title.setText(extraBook.getTitle());
        //автор
        author =(TextView) findViewById(R.id.text_author);
        author.setText(extraBook.getAuthors());
        //язык
        language =(TextView) findViewById(R.id.text_language_value);
        language.setText(extraBook.getLanguage());
        //жанр
        category =(TextView) findViewById(R.id.text_category_value);
        category.setText(extraBook.getCategory());
        //цена
        cost = (TextView) findViewById(R.id.text_cost_value);
        if (extraBook.isForSale()){
            cost.setText(extraBook.getCost()    + "\t" +
                    extraBook.getSaleCost());
            cost.setTextColor(getResources().getColor(R.color.orange));
        } else {
            cost.setText(extraBook.getCost());
            cost.setTextColor(getResources().getColor(R.color.green));
        }
        //краткое описание
        description = (TextView) findViewById(R.id.text_of_description);
        description.setText(extraBook.getDescription());
        //обложка
        cover = (ImageView) findViewById(R.id.image_cover);
        aq.id(cover).image(extraBook.getBigCover().toString());

        buy = (ImageButton) findViewById(R.id.btn_buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        favorite = (ImageButton) findViewById(R.id.btn_favorite);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        full_info = (ImageButton) findViewById(R.id.btn_detailed_info);
        full_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
