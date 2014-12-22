package ru.vsu.cs.app.bookstore.search_activity;

import android.content.Context;
import android.text.TextUtils;

import com.androidquery.AQuery;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

public class BookObject implements Serializable {

    private int id/*,
            pageCount*/;
    private String title, authors, category, language, description,
            cost, saleCost;
    private URL smallCover, bigCover,
            detailedInfo, readOnline;
    private boolean isEBook, isForSale;// in "saleInfo"

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String authors) {
        if (TextUtils.isEmpty(authors)){
            this.authors = " - ";
        } else {
            this.authors = authors;
        }
    }

    public void setCategory(String category) {
        if (TextUtils.isEmpty(category)){
            this.category = " - ";
        } else {
            this.category = category;
        }
    }

    public void setLanguage(String language) {
        if (!TextUtils.isEmpty(language)) {
            if (language.toUpperCase().equals("RU")) {
                this.language = "русский";
            } else if (language.toUpperCase().equals("EN")){
                this.language = "английский";
            } else {
                this.language = language;
            }
        } else {
            this.language = " - ";
        }
    }

    public void setSmallCover(String smallCover) {
        try {
            this.smallCover = new URL(smallCover);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setBigCover(String bigCover) {
        try {
            this.bigCover = new URL(bigCover);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setDetailedInfo(String detailedInfo) {
        try {
            this.detailedInfo = new URL(detailedInfo);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public URL getDetailedInfo() {
        return detailedInfo;
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
        if (TextUtils.isEmpty(description)){
            this.description = "Нет краткого описания.";
        } else {
            this.description = description;

        }
    }

    public String getCost() {
        return cost;
    }

    public String getSaleCost() {
        return saleCost;
    }

    public void setCost(double amount, String currencyCode) {
        this.cost = Double.toString(amount) + currencyCode;
    }

    public void setSaleCost(double amount, String currencyCode) {
        this.saleCost = Double.toString(amount) + currencyCode;
    }
}
