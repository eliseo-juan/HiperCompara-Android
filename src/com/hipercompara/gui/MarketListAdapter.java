package com.hipercompara.gui;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

import com.hipercompara.R;
import com.hipercompara.logic.OfferManager;
import com.hipercompara.logic.Supermarket;

/**
 * Adaptador que se usa en los ajustes, y muestra los supermercados
 * 
 * @author Eliseo Juan
 */
public class MarketListAdapter extends BaseAdapter implements
		OnCheckedChangeListener {

	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	ArrayList<Supermarket> supermarketArrayList;
	ArrayList<Supermarket> supermarketArrayListFull;
	LayoutInflater lInflater;
	Context context;
	ArrayList<String> supermarketNames;

	/**
	 * Constructor del adaptador
	 * 
	 * @see com.hipercompara.gui.MarketListAdapter
	 * @author Eliseo Juan
	 * @param context
	 *            Contexto del activity que lo invoca
	 * @param supermarkets
	 *            Conjunto de SuperMercados a mostrar
	 */
	MarketListAdapter(Context context, ArrayList<Supermarket> supermarkets) {
		this.context = context;
		supermarketArrayList = new ArrayList<Supermarket>();
		supermarketNames = new ArrayList<String>();
		supermarketArrayListFull = supermarkets;
		lInflater = LayoutInflater.from(context);
		prefs = context.getSharedPreferences("prefs_supermarket",
				Context.MODE_PRIVATE);
		editor = prefs.edit();
		for (int i = 0; i < supermarketArrayListFull.size(); i++) {
			if (!supermarketNames.contains(supermarketArrayListFull.get(i)
					.getName())) {
				supermarketArrayList.add(supermarketArrayListFull.get(i));
				supermarketNames.add(supermarketArrayListFull.get(i).getName());
			}
		}

	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return supermarketArrayList.size();
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int index) {
		return supermarketArrayList.get(index);
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
		view = lInflater.inflate(R.layout.supermarket_list_row, null);
		CheckBox check = (CheckBox) view
				.findViewById(R.id.checkBoxSuperMarketList);
		ImageView logo = (ImageView) view
				.findViewById(R.id.imageViewSuperMarketList);
		check.setTag(supermarketArrayList.get(item).getName());
		check.setOnCheckedChangeListener(this);

		check.setChecked(prefs.getBoolean(
				String.valueOf(supermarketArrayList.get(item).getId()), true));

		int id = context.getResources().getIdentifier(
				"market_"
						+ supermarketArrayList.get(item).getName()
								.toLowerCase(), "drawable",
				context.getPackageName());
		logo.setImageResource(id);
		return view;
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(CompoundButton
	 *      arg0, boolean arg1)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		for (int i = 0; i < supermarketArrayListFull.size(); i++) {
			if (supermarketArrayListFull.get(i).getName()
					.equals(buttonView.getTag()))
				editor.putBoolean(
						String.valueOf(supermarketArrayListFull.get(i).getId()),
						isChecked);
		}
		editor.commit();
		// Avisa a Offer manager de que las preferencias han cambiado
		OfferManager.instance().offerConditionsChanged();
	}
}
