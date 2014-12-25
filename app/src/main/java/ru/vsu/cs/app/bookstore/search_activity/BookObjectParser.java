package ru.vsu.cs.app.bookstore.search_activity;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by V on 23.12.2014.
 */
public class BookObjectParser implements Parser<BookObject> {

    public ArrayList<BookObject> records = new ArrayList<BookObject>();

    @Override
    public ArrayList<BookObject> parse(String json) throws JSONException {
        if (!TextUtils.isEmpty(json)) {
            JSONObject response = new JSONObject(json);
            JSONArray dataArray = response.optJSONArray("items");
            if (dataArray != null && dataArray.length() != 0) {

                for (int i = 0; i < dataArray.length(); i++) {
                    BookObject bookObject = new BookObject();

                    StringBuilder data = new StringBuilder();

                    bookObject.setId(dataArray.getJSONObject(i).getString("id"));

                    JSONObject volumeInfo = dataArray.getJSONObject(i).getJSONObject("volumeInfo");
                    bookObject.setTitle(volumeInfo.getString("title"));//название

                    JSONArray authors = volumeInfo.optJSONArray("authors");//авторы
                    if (authors != null) {
                        data.append(authors.optString(0));
//                      for (int j = 0; j < authors.length(); j++ ){
//                          data.append(authors.optString(i))
//                                  .append(", ");
//                      }
                        bookObject.setAuthors(data.toString());
                        data.setLength(0);
                    }

                    bookObject.setDescription(volumeInfo.optString("description"));//описание

                    JSONArray category = volumeInfo.optJSONArray("categories");//жанр
                    if (category != null) {
                        data.append(category.optString(0));
                        for (int j = 1; j < category.length(); j++) {
                            data.append(", ")
                                    .append(category.optString(i));
                        }
                        bookObject.setCategory(data.toString());
                        data.setLength(0);
                    } else {
                        bookObject.setCategory(null);
                    }

                    JSONObject covers = volumeInfo.optJSONObject("imageLinks");
                    if (covers != null) {
                        bookObject.setSmallCover(covers.optString("smallThumbnail"));
//                    if (TextUtils.isEmpty(bookObject.getSmallCover().toString())) {
//                        logs += "Не удалось найти small cover.";
//                    }

                        bookObject.setBigCover(covers.optString("thumbnail"));
//                    if (TextUtils.isEmpty(bookObject.getBigCover().toString())) {
//                        logs += "Не удалось найти big cover.";
//                    }
                    }

                    bookObject.setLanguage(volumeInfo.optString("language"));//язык

                    bookObject.setDetailedInfo(volumeInfo.optString("infoLink"));//ссылка на подроную информацию
//                if (bookObject.getDetailedInfo().toString().isEmpty()) {
//                    logs += "Не удалось найти infoLink.";
//                }

                    JSONObject saleInfo = dataArray.getJSONObject(i).getJSONObject("saleInfo");

                    bookObject.setEBook(saleInfo.optBoolean("isEbook"));//isEBook

                    if (TextUtils.isEmpty(saleInfo.optString("saleability"))//со скидкой
                            || saleInfo.optString("saleability").toUpperCase().equals("NOT_FOR_SALE")) {
                        bookObject.setForSale(false);
                    } else {
                        bookObject.setForSale(true);

                        JSONObject cost = saleInfo.optJSONObject("listPrice");
                        if (cost != null) {
                            bookObject.setCost(cost.optDouble("amount"), cost.optString("currencyCode"));//полная цена
                        }
                        JSONObject saleCost = saleInfo.optJSONObject("retailPrice");
                        if (saleCost != null) {
                            bookObject.setSaleCost(saleCost.optDouble("amount"), saleCost.optString("currencyCode"));//цена по скидке
                        }
                    }

                    records.add(bookObject);
                }
            }
        }

        return records;
    }
}
