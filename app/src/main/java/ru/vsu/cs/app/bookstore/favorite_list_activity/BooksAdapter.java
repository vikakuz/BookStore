package ru.vsu.cs.app.bookstore.favorite_list_activity;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ru.vsu.cs.app.bookstore.R;

/**
 * Created by V on 16.12.2014.
 */
public class BooksAdapter extends SimpleCursorAdapter {

    public BooksAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

}
