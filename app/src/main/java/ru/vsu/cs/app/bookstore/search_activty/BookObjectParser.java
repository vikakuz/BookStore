package ru.vsu.cs.app.bookstore.search_activty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;


/*
id;
title, authors, pageCount, category,language
URL smallCover, bigCover,readOnline
boolean isEBook, isForSale // in "saleInfo"
*/

public class BookObjectParser implements Parser<BookObject> {

    @Override
    public BookObject parse(String json) throws JSONException {
        BookObject bookObject = new BookObject();
        JSONObject response = new JSONObject(json);
        JSONArray dataArray = response.getJSONArray("items");

        for (int i = 0; i < dataArray.length(); i++){
            StringBuilder data = new StringBuilder();

            JSONObject volumeInfo = dataArray.getJSONObject(i).getJSONObject("volumeInfo");
            bookObject.setTitle(volumeInfo.getString("title"));//название

            JSONArray authors = volumeInfo.getJSONArray("authors");//авторы
            for (int j = 0; j < authors.length(); j++ ){
                data.append(authors.getJSONObject(i).toString())
                        .append("\n");
            }
            bookObject.setAuthors(data.toString());
            data.setLength(0);

            bookObject.setPageCount(volumeInfo.optInt("pageCount"));//кол-во страниц

            JSONArray category = volumeInfo.getJSONArray("categories");//жанр
            for (int j = 0; j < category.length(); j++ ){
                data.append(category.getJSONObject(i).toString())
                        .append("\n");
            }
            bookObject.setCategory(data.toString());
            data.setLength(0);

            //TODO images
            JSONObject covers = volumeInfo.getJSONObject("imageLinks");
            try{
                bookObject.setSmallCover(new URL(covers.getString("smallThumbnail")));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                bookObject.setBigCover(new URL(covers.getString("thumbnail")));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            bookObject.setLanguage(volumeInfo.optString("language"));//язык



            JSONObject saleInfo = dataArray.getJSONObject(i).getJSONObject("saleInfo");
            bookObject.setEBook(saleInfo.getBoolean("isEbook"));//isEBook

            if (saleInfo.optString("saleability").isEmpty()//со скидкой
                    || saleInfo.optString("saleability").toUpperCase().equals("NOT_FOR_SALE")){
                bookObject.setForSale(false);
            } else {
                bookObject.setForSale(true);
            }
        }

        return bookObject;
    }

}
