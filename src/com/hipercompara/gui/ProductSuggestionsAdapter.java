package com.hipercompara.gui;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Esta clase es necesaria para manejar la barra de busqueda superior
 * 
 * @author Eliseo Juan
 */
class ProductSuggestionsAdapter extends CursorAdapter {

	/**
	 * Constructor del adaptador
	 * 
	 * @see com.hipercompara.gui.ProductSuggestionsAdapter
	 * @author Eliseo Juan
	 * @param context
	 *            Contexto del activity que lo invoca
	 * @param c
	 *            Cursor con los productos
	 */
	public ProductSuggestionsAdapter(Context context, Cursor c) {
		super(context, c, 0);
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(android.R.layout.simple_list_item_1, parent,
				false);
		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView tv = (TextView) view;
		final int textIndex = cursor
				.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
		tv.setText(cursor.getString(textIndex));
	}
}
