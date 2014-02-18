package com.hipercompara.gui;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hipercompara.R;
import com.hipercompara.logic.Product;

/**
 * Adaptador que se usa en la lista de la compra, y muestra los productos que
 * están en ellas
 * 
 * @author Eliseo Juan
 */
public class ShoppingListAdapter extends BaseAdapter {

	ArrayList<Product> productsArrayList;
	LayoutInflater lInflater;
	Context context;

	/**
	 * Constructor del adaptador
	 * 
	 * @see com.hipercompara.gui.ShoppingListAdapter
	 * @author Eliseo Juan
	 * @param context
	 *            Contexto del activity que lo invoca
	 * @param products
	 *            Conjunto de Ofertas a mostrar
	 */
	ShoppingListAdapter(Context context, ArrayList<Product> products) {
		this.context = context;
		productsArrayList = products;
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
		view = lInflater.inflate(R.layout.shopping_list_row, null);
		ImageView icon = (ImageView) view.findViewById(R.id.imageListRowIcon);
		TextView name = (TextView) view
				.findViewById(R.id.textViewListRowProduct);
		TextView quantity = (TextView) view
				.findViewById(R.id.textViewListRowQuantity);
		view.setTag(item);

		name.setText("" + productsArrayList.get(item).getName());
		quantity.setText("" + productsArrayList.get(item).getTimesInList());

		int id = context.getResources().getIdentifier(
				productsArrayList.get(item).getCategory().getIconName(),
				"drawable", context.getPackageName());
		icon.setImageResource(id);

		return view;
	}
}
