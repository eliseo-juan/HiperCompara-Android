package com.hipercompara.gui;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.hipercompara.R;
import com.hipercompara.logic.Category;
import com.hipercompara.logic.Offer;
import com.hipercompara.logic.OfferManager;
import com.hipercompara.logic.ProductSearchEngine;
import com.hipercompara.logic.ShoppingList;

/**
 * Activity que muestra las ofertas ordenadas por categorías
 * 
 * @author Eliseo Juan
 */
public class OffersActivity extends SherlockActivity implements TabListener,
		OnItemClickListener {

	private ListView offersListView;
	private OffersAdapter offersAdapter;
	private ArrayList<Offer> offerList;
	private ArrayList<Category> categoryList;
	private Boolean[] showCategory;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @author Eliseo Juan
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offers);

		getSupportActionBar().setTitle("Ofertas");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// getSupportActionBar().setDisplayShowHomeEnabled(false);
		// getSupportActionBar().setDisplayShowTitleEnabled(false);

		setContentView(R.layout.tab_navigation);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		offersListView = (ListView) findViewById(R.id.listViewOffers);
		offersListView.setOnItemClickListener(this);

		ActionBar.Tab tab = getSupportActionBar().newTab();
		tab.setText("Todas");
		tab.setTabListener(this);
		getSupportActionBar().addTab(tab);
	}

	/**
	 * Crea las tabs de las ofertas
	 * 
	 * @author Eliseo Juan
	 */
	private void createTabs() {

		ArrayList<Category> categoryList = ProductSearchEngine.instance()
				.getCategories();
		for (int i = 0; categoryList != null & i < categoryList.size(); i++) {
			if (showCategory[i]) {
				ActionBar.Tab tab1 = getSupportActionBar().newTab();
				tab1.setText(categoryList.get(i).getName());
				tab1.setTabListener(this);
				getSupportActionBar().addTab(tab1);
			}
		}
	}

	/**
	 * Obtiene todas las ofertas y selecciona las categorías que se deben
	 * mostrar
	 * 
	 * @author Eliseo Juan
	 */
	private void startup() {
		offerList = OfferManager.instance().getAllOffers();
		categoryList = ProductSearchEngine.instance().getCategories();
		showCategory = new Boolean[categoryList.size()];
		Arrays.fill(showCategory, false);

		for (int i = 0; offerList != null && i < offerList.size(); i++) {
			showCategory[offerList.get(i).getProduct().getCategoryId() - 1] = true;
		}
		createTabs();
	}

	/**
	 * @see android.app.Activity#onStart()
	 * @author Eliseo Juan
	 */
	@Override
	protected void onStart() {
		super.onStart();
		startup();
	}

	/**
	 * @see com.actionbarsherlock.app.ActionBar.TabListener#onTabSelected(Tab,
	 *      FragmentTransaction)
	 * @author Eliseo Juan
	 */
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// Preparado para que funcione
		if (tab.getText().toString().equals("Todas")) {
			offerList = OfferManager.instance().getAllOffers();
		} else {
			Category c = ProductSearchEngine.instance().getCategoryByName(
					tab.getText().toString());
			offerList = OfferManager.instance().getOffersByCategory(c);
		}
		offersAdapter = new OffersAdapter(this, offerList);
		offersListView.setAdapter(offersAdapter);

	}

	/**
	 * @see com.actionbarsherlock.app.ActionBar.TabListener#onTabUnselected(Tab,
	 *      FragmentTransaction)
	 * @author Eliseo Juan
	 */
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see com.actionbarsherlock.app.ActionBar.TabListener#onTabReselected(Tab
	 *      tab, FragmentTransaction ft)
	 * @author Eliseo Juan
	 */
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see 
	 *      android.widget.AdapterView.OnItemClickListener.onItemClick(AdapterView
	 *      <?> arg0, View arg1, int arg2, long arg3)
	 * @author Eliseo Juan
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Offer o = (Offer) arg0.getItemAtPosition(arg2);
		ShoppingList.instance().addOffer(o);
		finish();
	}

	/**
	 * @see SherlockActivity#onOptionsItemSelected(MenuItem)
	 * @author Eliseo Juan
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
