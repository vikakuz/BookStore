package ru.vsu.cs.app.bookstore.detailed_info_activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.sql.SQLException;

import ru.vsu.cs.app.bookstore.R;
import ru.vsu.cs.app.bookstore.favorite_list_activity.DBActivity;
import ru.vsu.cs.app.bookstore.favorite_list_activity.DBHelper;
import ru.vsu.cs.app.bookstore.search_activity.BookObject;
import ru.vsu.cs.app.bookstore.search_activity.SearchActivity;

public class FullInfoActivity extends Activity {

    public static final String EXTRA_BOOK = "book";

    public TextView title;
    public TextView author;
    public TextView language;
    public TextView category;
    public TextView cost;
    public TextView saleCost;
    public TextView description;

    public ImageView cover;

    public ImageButton buy;
    public ImageButton favorite;
    public ImageButton full_info;
    private ActionBar supportActionBar;

    BookObject extraBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info);

        AQuery aq = new AQuery(this.getApplicationContext());
        extraBook = (BookObject) getIntent().getSerializableExtra(EXTRA_BOOK);
        //имя
        title = (TextView) findViewById(R.id.text_title);
        title.setText(extraBook.getTitle());
        title.setSelected(true);
        //автор
        author = (TextView) findViewById(R.id.text_author);
        author.setText(extraBook.getAuthors());
        //язык
        language = (TextView) findViewById(R.id.text_language_value);
        language.setText(extraBook.getLanguage());
        //жанр
        category = (TextView) findViewById(R.id.text_category_value);
        category.setText(extraBook.getCategory());

        //цена
        cost = (TextView) findViewById(R.id.text_cost_value);
        //цена со скидкой
        saleCost = (TextView) findViewById(R.id.text_salecost_value);
        if (extraBook.isForSale()) {

            if (TextUtils.isEmpty(extraBook.getCost())) {
                cost.setText("Нет в продаже");
                cost.setTextColor(getResources().getColor(R.color.white));
                cost.setTextSize(14);
                saleCost.setText("");
            } else {
                // Create spannable text and set style.
                Spannable text = new SpannableString(extraBook.getCost());
                text.setSpan(new StrikethroughSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                text.setSpan(new StyleSpan(Typeface.ITALIC), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                text.setSpan(new BackgroundColorSpan(R.color.red),0,text.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                // Set spannable text in TextView.
                cost.setText(text);

                //cost.setText(getResources().getString(R.string.strike_style, extraBook.getCost()));
                //cost.setTextColor(getResources().getColor(R.color.red));

                saleCost.setText(extraBook.getSaleCost());
                saleCost.setTextColor(getResources().getColor(R.color.green));
            }
        } else {
            if (TextUtils.isEmpty(extraBook.getCost())) {
                cost.setText("Нет в продаже");
                cost.setTextColor(getResources().getColor(R.color.white));
                cost.setTextSize(14);
            } else {
                cost.setText(extraBook.getCost());
                cost.setTextColor(getResources().getColor(R.color.green));
            }
            saleCost.setText("");
        }
        //краткое описание
        description = (TextView) findViewById(R.id.text_of_description);
        description.setText(extraBook.getDescription());
        //обложка
        cover = (ImageView) findViewById(R.id.image_cover);
        if (extraBook.getBigCover() == null) {
            cover.setImageResource(R.drawable.ic_default_cover);
        } else {
            aq.id(cover).image(extraBook.getBigCover());
        }

        buy = (ImageButton) findViewById(R.id.btn_buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        favorite = (ImageButton) findViewById(R.id.btn_favorite);
        setBtnResource();
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objectIsInDB()){
                    try {
                        deleteFromDB(extraBook);
                    } catch (SQLException e) {
                        Log.w(getClass().getName(), "Не удалось удалить из бд");
                        e.printStackTrace();
                    }
                } else {
                    try {
                        saveInDB(extraBook);
                    } catch (SQLException e) {
                        Log.w(getClass().getName(), "Не удалось добавить в бд");
                        e.printStackTrace();
                    }
                }
                setBtnResource();
            }
        });

        full_info = (ImageButton) findViewById(R.id.btn_detailed_info);
        full_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void saveInDB(BookObject book) throws SQLException {
        DBHelper dbHelper = DBHelper.getInstance(this);
        dbHelper.getBookObjectDao().createOrUpdate(book);
    }

    private void deleteFromDB(BookObject book) throws SQLException {
        DBHelper dbHelper = DBHelper.getInstance(this);
        dbHelper.getBookObjectDao().delete(book);
    }

    private boolean objectIsInDB() {
        DBHelper helper = DBHelper.getInstance(this);
        try {
            return helper.getBookObjectDao().objectExists(extraBook);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setBtnResource(){
        if (objectIsInDB()){//горящая кнопка
            favorite.setImageResource(android.R.drawable.star_big_on);
        } else {            //негорящая кнопка
            favorite.setImageResource(android.R.drawable.star_big_off);
        }
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
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_favorites:
                intent = new Intent(FullInfoActivity.this, DBActivity.class);
                //intent.putExtra(FullInfoActivity.EXTRA_BOOK, adapter.getItem(pos));
                startActivity(intent);
                break;
            case R.id.action_search:
                intent = new Intent(FullInfoActivity.this, SearchActivity.class);
                //intent.putExtra(FullInfoActivity.EXTRA_BOOK, adapter.getItem(pos));
                startActivity(intent);
                break;
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
