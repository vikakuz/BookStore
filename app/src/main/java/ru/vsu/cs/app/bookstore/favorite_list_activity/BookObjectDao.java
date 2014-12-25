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

    public BookObject getBookById(String id) throws SQLException {
        //запрос
        return queryBuilder().where().eq(BookObject.FIELD_ID, id).queryForFirst();
    }

    public boolean objectExists(BookObject book) throws SQLException {
        if (getBookById(book.getId()) != null) {
            return  true;
        } else {
            return  false;
        }
    }

}
