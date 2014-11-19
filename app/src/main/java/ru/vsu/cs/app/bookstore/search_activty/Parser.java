package ru.vsu.cs.app.bookstore.search_activty;

import org.json.JSONException;

import java.io.Serializable;

public interface Parser<T extends Serializable> {

	public T parse(String json) throws JSONException;

}