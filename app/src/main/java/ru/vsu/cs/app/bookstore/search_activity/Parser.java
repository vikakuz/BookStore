package ru.vsu.cs.app.bookstore.search_activity;

import org.json.JSONException;

import java.io.Serializable;

public interface Parser<T extends Serializable> {

	public java.util.ArrayList<BookObject> parse(String json) throws JSONException;

}