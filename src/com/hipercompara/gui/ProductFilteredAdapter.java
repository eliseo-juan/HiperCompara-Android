package com.hipercompara.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hipercompara.R;
import com.hipercompara.logic.Product;

/**
 * Adaptador que se usa al buscar por categoría, y muestra los productos según
 * los filtros
 * 
 * @author Eliseo Juan
 */
public class ProductFilteredAdapter extends BaseAdapter {

	ArrayList<Product> productsArrayList;
	LayoutInflater lInflater;
	Context context;

	/**
	 * Constructor del adaptador
	 * 
	 * @see com.hipercompara.gui.ProductFilteredAdapter
	 * @author Eliseo Juan
	 * @param context
	 *            Contexto del activity que lo invoca
	 * @param products
	 *            Conjunto de Productos a mostrar
	 */
	ProductFilteredAdapter(Context context, ArrayList<Product> products) {
		productsArrayList = products;
		this.context = context;
		lInflater = LayoutInflater.from(context);
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return productsArrayList.size();
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int index) {
		return productsArrayList.get(index);
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int id) {
		return id;
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.Adapter#getView(int arg0, View arg1, ViewGroup arg2)
	 */
	@Override
	public View getView(int item, View view, ViewGroup parent) {
		view = lInflater.inflate(R.layout.product_list_row, null);

		TextView name = (TextView) view.findViewById(R.id.textViewProductName);
		TextView brand = (TextView) view
				.findViewById(R.id.textViewProductBrand);
		TextView avPrice = (TextView) view.findViewById(R.id.textViewAvPrice);
		view.setTag(item);

		if (productsArrayList.get(item).getBrand() == null)
			brand.setVisibility(View.GONE);
		else
			brand.setText(productsArrayList.get(item).getBrand().getName());
		name.setText(productsArrayList.get(item).getName());
		avPrice.setText(String.valueOf(new DecimalFormat("##.##")
				.format(productsArrayList.get(item).getAveragePrice()))
				+ context.getString(R.string.euro_symbol));

		return view;
	}

}
