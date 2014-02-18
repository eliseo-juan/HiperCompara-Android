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
import com.hipercompara.logic.Offer;

/**
 * Adaptador que muestra las ofertas
 * 
 * @author Eliseo Juan
 */
public class OffersAdapter extends BaseAdapter {

	ArrayList<Offer> offersArrayList;
	LayoutInflater lInflater;
	Context context;

	/**
	 * Constructor del adaptador
	 * 
	 * @see com.hipercompara.gui.OffersAdapter
	 * @author Eliseo Juan
	 * @param context
	 *            Contexto del activity que lo invoca
	 * @param offers
	 *            Conjunto de Ofertas a mostrar
	 */
	OffersAdapter(Context context, ArrayList<Offer> offers) {
		this.context = context;
		offersArrayList = offers;
		lInflater = LayoutInflater.from(context);
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return offersArrayList.size();
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int index) {
		return offersArrayList.get(index);
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
		view = lInflater.inflate(R.layout.offer_list_row, null);

		TextView product = (TextView) view
				.findViewById(R.id.textViewOfferProductName);
		TextView productBrand = (TextView) view
				.findViewById(R.id.textViewOfferProductBrand);

		ImageView supermarket = (ImageView) view
				.findViewById(R.id.imageViewOfferSupermarket);
		TextView price = (TextView) view.findViewById(R.id.textViewOffer);
		TextView newPrice = (TextView) view.findViewById(R.id.textViewOfferNew);
		view.setTag(item);

		product.setText(offersArrayList.get(item).getProduct().getName());
		productBrand.setText(offersArrayList.get(item).getProduct().getBrand()
				.getName());
		int id = context.getResources().getIdentifier(
				offersArrayList.get(item).getSupermarket().getIconName(),
				"drawable", context.getPackageName());
		supermarket.setImageResource(id);

		price.setText(String.format("%.2f",
				Float.valueOf(offersArrayList.get(item).getPrice())));
		newPrice.setText(String.format("%.2f",
				Float.valueOf(offersArrayList.get(item).getNewPrice())));
		return view;
	}
}
