package com.hipercompara.gui;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.hipercompara.R;
import com.hipercompara.logic.Supermarket;
import com.hipercompara.logic.SupermarketManager;

/**
 * Activity en el que puedes desmarcar los supermercados que no te interesan
 * 
 * @author Eliseo Juan
 */
public class SettingsActivity extends SherlockActivity {

	ArrayList<Supermarket> supermarkets;
	ListView supermarketListView;
	MarketListAdapter adapter;
	SharedPreferences pref;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @author Eliseo Juan
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		pref = getSharedPreferences("default", Context.MODE_PRIVATE);

		ActionBar actionBar = getSupportActionBar();
		if (pref.getBoolean("isFirst", true))
			actionBar.setDisplayHomeAsUpEnabled(false);
		else
			actionBar.setDisplayHomeAsUpEnabled(true);

		supermarketListView = (ListView) findViewById(R.id.listViewSettingsSupermarket);

		supermarkets = SupermarketManager.instance().getSupermarketsInCircle();
		adapter = new MarketListAdapter(this, supermarkets);
		supermarketListView.setAdapter(adapter);

	}

	/**
	 * Cargamos el menu de la activity.
	 * 
	 * @author vicdoz
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.settingslist, menu);
		return true;
	}

	/**
	 * @see SherlockActivity#onOptionsItemSelected(MenuItem)
	 * @author Eliseo Juan
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.finishSettings:
			SharedPreferences pref = getSharedPreferences("default",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor e = pref.edit();
			e.putBoolean("isFirst", false);
			e.commit();
			Intent i = new Intent(this, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
			break;

		case R.id.backToMap:
			startActivity(new Intent(this, NearbyMarket.class));
			finish();
			break;
		case android.R.id.home:
			finish();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * @see android.app.Activity#onBackPressed()
	 * @author Eliseo Juan
	 */
	@Override
	public void onBackPressed() {
		if (pref.getBoolean("isFirst", true)) {
			Intent i = new Intent(this, NearbyMarket.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
		} else
			super.onBackPressed();
	}
}
