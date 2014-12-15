package ru.vsu.cs.app.bookstore.favorite_list_activity;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

/**
 * Created by V on 16.12.2014.
 */
public class BooksAdapter extends SimpleCursorAdapter {

    public BooksAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

}
