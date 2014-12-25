package ru.vsu.cs.app.bookstore.search_activity;

import android.text.TextUtils;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "mybooks")
public class BookObject implements Serializable {

    public static final String FIELD_ID = "id";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_AUTHOR = "authors";
    private static final String FIELD_CATEGORY = "category";
    private static final String FIELD_LANGUAGE = "language";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_COST = "cost";
    private static final String FIELD_SALECOST = "saleCost";
    private static final String FIELD_SMALLCOVER = "smallCover";
    private static final String FIELD_BIGCOVER = "bigCover";
    private static final String FIELD_DETAILED_INFO = "detailedInfo";
    private static final String FIELD_IS_EBOOK = "isEBook";
    private static final String FIELD_IS_FOR_SALE = "isForSale";


    @DatabaseField(columnName = FIELD_ID, id = true)
    private String id;
    @DatabaseField(columnName = FIELD_TITLE)
    private String title;
    @DatabaseField(columnName = FIELD_AUTHOR)
    private String authors;
    @DatabaseField(columnName = FIELD_CATEGORY)
    private String category;
    @DatabaseField(columnName = FIELD_LANGUAGE)
    private String language;
    @DatabaseField(columnName = FIELD_DESCRIPTION)
    private String description;
    @DatabaseField(columnName = FIELD_COST)
    private String cost;
    @DatabaseField(columnName = FIELD_SALECOST)
    private String saleCost;
    @DatabaseField(columnName = FIELD_SMALLCOVER)
    private String smallCover;
    @DatabaseField(columnName = FIELD_BIGCOVER)
    private String bigCover;
    @DatabaseField(columnName = FIELD_DETAILED_INFO)
    private String detailedInfo;
    @DatabaseField(columnName = FIELD_IS_EBOOK)
    private boolean isEBook;
    @DatabaseField(columnName = FIELD_IS_FOR_SALE)
    private boolean isForSale;

    public BookObject(){
    }

    public void setId(String id) {
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



    public void setEBook(boolean isEBook) {
        this.isEBook = isEBook;
    }

    public void setForSale(boolean isForSale) {
        this.isForSale = isForSale;
    }

    public String getId() {
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

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setSaleCost(String saleCost) {
        this.saleCost = saleCost;
    }

    public String getSmallCover() {
        return smallCover;
    }

    public String getBigCover() {
        return bigCover;
    }

    public String getDetailedInfo() {
        return detailedInfo;
    }

    public void setSmallCover(String smallCover) {
        this.smallCover = smallCover;
    }

    public void setBigCover(String bigCover) {
        this.bigCover = bigCover;
    }

    public void setDetailedInfo(String detailedInfo) {
        this.detailedInfo = detailedInfo;
    }
}
