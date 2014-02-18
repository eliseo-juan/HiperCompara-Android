package com.hipercompara.gui;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hipercompara.R;
import com.hipercompara.logic.SupermarketSuggestion;
import com.hipercompara.logic.SupermarketSuggestionManager;

/**
 * Activity que muestra los supermercados mas cercanos, el mapa y dónde están
 * situados
 * 
 * @author Eliseo Juan and Victor Ahuir
 */
public class MarketActivity extends SherlockFragmentActivity {

	ImageView[] imageSuggestion = new ImageView[3];
	TextView[] priceSuggestion = new TextView[3];
	LinearLayout[] linearMarket = new LinearLayout[3];
	private GoogleMap map = null;
	ArrayList<LatLng> markerPoints; // Chinchetas de supermercados.
	private int view = 0; // view son las diferentes vistas del mapa:satelite,
							// hibrido o mapa normal de carreteras.
	double latitudeValencia = Double.parseDouble("39.480825");
	double longitudeValencia = Double.parseDouble("-0.364094");
	ArrayList<SupermarketSuggestion> suggestions;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @author Eliseo Juan
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_market);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		imageSuggestion[0] = (ImageView) findViewById(R.id.imageMarketFirst);
		imageSuggestion[1] = (ImageView) findViewById(R.id.imageMarketSecond);
		imageSuggestion[2] = (ImageView) findViewById(R.id.imageMarketThird);

		priceSuggestion[0] = (TextView) findViewById(R.id.textViewMarketFirst);
		priceSuggestion[1] = (TextView) findViewById(R.id.textViewMarketSecond);
		priceSuggestion[2] = (TextView) findViewById(R.id.textViewMarketThird);

		linearMarket[0] = (LinearLayout) findViewById(R.id.linearLayoutMarketFirst);
		linearMarket[1] = (LinearLayout) findViewById(R.id.linearLayoutMarketFirst);
		linearMarket[2] = (LinearLayout) findViewById(R.id.linearLayoutMarketFirst);
		loadSuggestions();

		markerPoints = new ArrayList<LatLng>();
		// Cogemos la referencia para SuportFragment del mapa
		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mapMarket);
		map = fm.getMap();// Cogemos el mapa
		map.setMyLocationEnabled(true); // Activamos la localizacion GPS en el
										// mapa.
		setSupermarketLocation(); // Inicializamos chinchetas supermercados
	}

	/**
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 * @author Eliseo Juan
	 */
	@Override
	protected void onResume() {
		super.onResume();
		initializeMap();

	}

	/**
	 * Funcion que obtiene los resultados y genera la interfaz
	 * 
	 * @author Eliseo Juan
	 */
	private void loadSuggestions() {
		suggestions = SupermarketSuggestionManager.instance().getSuggestions();
		for (int i = 0; i < 3; i++) {
			if (suggestions.size() > i) {
				String market = suggestions.get(i).getSuperMarket().getName();
				int id = getResources().getIdentifier(
						"market_" + market.toLowerCase(), "drawable",
						getPackageName());
				imageSuggestion[i].setImageResource(id);
				priceSuggestion[i].setText(suggestions.get(i).getTotalPrice()
						+ getString(R.string.euro_symbol));
			} else {
				imageSuggestion[i].setVisibility(View.GONE);
				priceSuggestion[i].setVisibility(View.GONE);
			}
		}
	}

	/**
	 * Cargamos el menu de la activity
	 * 
	 * @author vicdoz
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.market, menu);
		return true;
	}

	/**
	 * Opciones del menu
	 * 
	 * @author vicdoz
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuView:
			changeView();
			break;
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Funcion para inicializar el mapa en Valencia con opciones que queramos
	 * 
	 * @author vicdoz
	 */
	private void initializeMap() {
		CameraUpdate camUpd1 = CameraUpdateFactory.newLatLng(new LatLng(
				latitudeValencia, longitudeValencia));
		camUpd1 = CameraUpdateFactory.zoomTo(19);
		map.moveCamera(camUpd1);
		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mapMarket)).getMap();

		LatLng valencia = new LatLng(latitudeValencia, longitudeValencia);
		CameraPosition camPos = new CameraPosition.Builder().target(valencia) // Centramos
																				// el
																				// mapa
																				// en
																				// Valencia
				.zoom(13) // Establecemos el zoom en 13
				.bearing(45) // Establecemos la orientación con el noreste
								// arriba
				.tilt(0) // Bajamos el punto de vista de la cámara 0 grados
				.build();

		CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
		map.animateCamera(camUpd3);
	}

	/**
	 * Función para poner las chinchetas que queramos en el mapa.
	 * 
	 * @author vicdoz
	 */
	private void setSupermarketLocation() {

		switch (suggestions.size()) {
		default:
		case 3:
			// Supermercado 3
			map.addMarker(new MarkerOptions().position(
					new LatLng(suggestions.get(2).getSuperMarket()
							.getLatitude(), suggestions.get(2).getSuperMarket()
							.getLongitude())).title(
					suggestions.get(2).getSuperMarket().getName()));
		case 2:
			// Supermercado 2
			map.addMarker(new MarkerOptions().position(
					new LatLng(suggestions.get(1).getSuperMarket()
							.getLatitude(), suggestions.get(1).getSuperMarket()
							.getLongitude())).title(
					suggestions.get(1).getSuperMarket().getName()));
		case 1:
			// Supermercado 1
			map.addMarker(new MarkerOptions().position(
					new LatLng(suggestions.get(0).getSuperMarket()
							.getLatitude(), suggestions.get(0).getSuperMarket()
							.getLongitude())).title(
					suggestions.get(0).getSuperMarket().getName()));
		}
	}

	/**
	 * Función para definir el tipo de vista del mapa:Normal,hibrida,satellite
	 * 
	 * @author vicdoz
	 */
	private void changeView() {
		view = (view + 1) % 4;
		switch (view) {
		case 0:
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
		case 1:
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		case 2:
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;
		case 3:
			map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;
		}
	}
}
