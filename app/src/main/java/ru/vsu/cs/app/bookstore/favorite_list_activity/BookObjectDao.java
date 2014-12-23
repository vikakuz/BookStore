package ru.vsu.cs.app.bookstore.favorite_list_activity;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

import ru.vsu.cs.app.bookstore.search_activity.BookObject;

/**
 * Created by V on 23.12.2014.
 */
public class BookObjectDao extends BaseDaoImpl<BookObject, Integer> {

    protected BookObjectDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, BookObject.class);
    }

    public BookObject getBookByTitle(String title) throws SQLException {
        //запрос
        BookObject object = queryBuilder().where().eq(BookObject.FIELD_TITLE, title).queryForFirst();
        return object;

    }


}
