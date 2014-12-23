package ru.vsu.cs.app.bookstore.favorite_list_activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ru.vsu.cs.app.bookstore.search_activity.BookObject;

/**
 * Created by V on 23.12.2014.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper{

    private static final String DATABASE_NAME = "favorite_books.db";
    private static final int DATABASE_VERSION = 1;

    private BookObjectDao bookObjectDao;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, BookObject.class);
        } catch (SQLException e) {
            Log.d("tag", "Can't create database " + e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int old, int next) {
        try {
            TableUtils.dropTable(connectionSource, BookObject.class, true);
        } catch (SQLException e) {
            Log.d("tag","Can't drop databases "+ e);
            throw new RuntimeException(e);
        }
        onCreate(database, connectionSource);
    }

    public static DBHelper getInstance(Context context) {
        return OpenHelperManager.getHelper(context, DBHelper.class);
    }

    public BookObjectDao getCityObjectDao() throws SQLException {
        if (bookObjectDao == null) {
            bookObjectDao = new BookObjectDao(getConnectionSource());
        }
        return bookObjectDao;
    }

}
