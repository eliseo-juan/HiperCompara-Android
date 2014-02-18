package com.hipercompara.gui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.hipercompara.R;
import com.hipercompara.logic.Product;
import com.hipercompara.logic.ProductSearchEngine;
import com.hipercompara.logic.ShoppingList;

/**
 * Activity que muestra los productos comprados recientemente.
 * 
 * @author Eliseo Juan
 */
public class RecentActivity extends SherlockActivity implements
		OnItemClickListener {

	ProductFilteredAdapter productsAdapter;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @author Eliseo Juan
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recent);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		ListView ls = (ListView) findViewById(R.id.listViewRecentProducts);

		ArrayList<Product> productsList = ProductSearchEngine.instance()
				.getMostPurchasedProducts();
		productsAdapter = new ProductFilteredAdapter(this, productsList);
		ls.setOnItemClickListener(this);
		ls.setAdapter(productsAdapter);
	}

	/**
	 * @see SherlockActivity#onCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 * @author Eliseo Juan
	 */
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.recent, menu);
		return true;
	}

	/**
	 * @see SherlockActivity#onOptionsItemSelected(MenuItem)
	 * @author Eliseo Juan
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_finished:
			startActivity(new Intent(this, MainActivity.class)
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			finish();
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * @see 
	 *      android.widget.AdapterView.OnItemClickListener#onItemClick(AdapterView
	 *      <?> arg0, View arg1, int arg2, long arg3)
	 * @author Eliseo Juan
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Product p = (Product) productsAdapter.getItem(arg2);
		ShoppingList.instance().addProduct(p);
		Toast.makeText(this, p.getName() + " añadido, ¿alguno más? ",
				Toast.LENGTH_SHORT).show();
	}
}
