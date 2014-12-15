package ru.vsu.cs.app.bookstore.search_activity;

import org.json.JSONException;
import java.io.Serializable;
import java.util.ArrayList;

public interface Parser<T extends Serializable> {

	public ArrayList<BookObject> parse(String json) throws JSONException;

}