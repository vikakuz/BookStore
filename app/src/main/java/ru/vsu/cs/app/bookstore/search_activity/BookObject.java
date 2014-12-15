package ru.vsu.cs.app.bookstore.search_activity;

import android.content.Context;

import com.androidquery.AQuery;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

public class BookObject implements Serializable {
    private int id,
            pageCount;
    private String title, authors, category,language, description;
    private URL smallCover, bigCover,
            readOnline;
    private boolean isEBook, isForSale;// in "saleInfo"
    //private ArrayList<MenuItem> menuItems;


    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setSmallCover(String smallCover/*, Context context, int id*/) {
       /* AQuery aq = new AQuery(context); //если не ошибаюсь, а параметрах контекст
        aq.id(id).image(smallCover);*/
        try {
            this.smallCover = new URL(smallCover);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setBigCover(String bigCover/*, Context context, int id*/) {
       /* AQuery aq = new AQuery(context); //если не ошибаюсь, а параметрах контекст
        aq.id(id).image(bigCover);*/
        try {
            this.bigCover = new URL(bigCover);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setReadOnline(URL readOnline) {
        this.readOnline = readOnline;
    }

    public void setEBook(boolean isEBook) {
        this.isEBook = isEBook;
    }

    public void setForSale(boolean isForSale) {
        this.isForSale = isForSale;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getCategory() {
        return category;
    }

    public String getLanguage() {
        return language;
    }

    public URL getSmallCover() {
        return smallCover;
    }

    public URL getBigCover() {
        return bigCover;
    }

    public URL getReadOnline() {
        return readOnline;
    }

    public boolean isEBook() {
        return isEBook;
    }

    public boolean isForSale() {
        return isForSale;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
