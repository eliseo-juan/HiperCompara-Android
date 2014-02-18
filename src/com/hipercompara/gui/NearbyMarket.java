package com.hipercompara.gui;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hipercompara.R;
import com.hipercompara.logic.AppConstants;
import com.hipercompara.logic.SharedPreferencesManager;
import com.hipercompara.logic.Supermarket;
import com.hipercompara.logic.SupermarketManager;

/**
 * La clase NearByMarket representa una actividad que muestra un mapa con para
 * que el usuario seleccione la distancia máxima a la que acudir al
 * supermercado.
 * 
 * @author vicdoz
 */
public class NearbyMarket extends SherlockFragmentActivity implements
		LocationListener, OnEditorActionListener {
	private double myLatitude = AppConstants.latitudeValencia;
	private double myLongitude = AppConstants.longitudeValencia;
	private float radius_in_KM = AppConstants.defaultPreferencesRadius;
	private static final float cte_radius_planet = AppConstants.earthRadius;
	private transient GoogleMap mMap;
	private transient EditText radiusEditText;
	ArrayList<Supermarket> supermarkets;
	ArrayList<Supermarket> circleSupermakets;
	ArrayList<Integer> superIdsList = new ArrayList<Integer>();// Ids de los
																// supermercados
																// del circulo
	MarkerOptions markerPoints;
	View view;
	SharedPreferences pref;
	LocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearbymarket);

		pref = getSharedPreferences("default", Context.MODE_PRIVATE);

		ActionBar actionBar = getSupportActionBar();
		if (pref.getBoolean("isFirst", true))
			actionBar.setDisplayHomeAsUpEnabled(false);
		else
			actionBar.setDisplayHomeAsUpEnabled(true);

		radiusEditText = (EditText) this.findViewById(R.id.EditRadiusCircle);
		supermarkets = SupermarketManager.instance().getSupermarkets();
		radiusEditText.setOnEditorActionListener(this);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		loadPreferences();
		View v = view;
		Location l = locationManager
				.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		myLatitude = l.getLatitude();
		myLongitude = l.getLongitude();
		cargaGoogleMap();
		circleInMap(v);
	}

	@Override
	protected void onPause() {
		super.onPause();
		savePreferences();
		locationManager.removeUpdates(this);
	}

	/**
	 * El metodo circleInMap realiza la funcionalidad de dibujar un circulo en
	 * el mapa.
	 * 
	 * @author vicdoz
	 * @param view
	 *            - View
	 */
	public void circleInMap(View view) {
		CameraUpdate camUpd2 = CameraUpdateFactory.newLatLngZoom(new LatLng(
				myLatitude, myLongitude), 14F);
		mMap.animateCamera(camUpd2);

		circleSupermakets = SupermarketManager.instance()
				.getSupermermarketsFromMyPosition(myLatitude, myLongitude,
						radius_in_KM, cte_radius_planet);

		CircleOptions opcionesCirculo = new CircleOptions().center(
				new LatLng(myLatitude, myLongitude)).radius(radius_in_KM);

		mMap.clear();
		Circle circle = mMap.addCircle(opcionesCirculo);
		circle.setStrokeColor(Color.argb(255, 10, 71, 255));// bordes
		circle.setCenter(new LatLng(myLatitude, myLongitude));
		circle.setStrokeWidth(2f);
		circle.setFillColor(Color.argb(50, 133, 224, 255));// interior circulo
															// color
		supermarketsInMap(view, circleSupermakets);
	}

	/**
	 * @author vicdoz
	 * @param view
	 *            Carga los supermercados en el mapa(chinchetas)
	 */
	public void supermarketsInMap(View view, ArrayList<Supermarket> supermarkets) {
		Iterator<Supermarket> iterator = supermarkets.iterator();
		String nombre;

		while (iterator.hasNext()) {
			Supermarket s = iterator.next();
			nombre = "mini_market_" + s.getName().toLowerCase();
			int resID = getResources().getIdentifier(nombre, "drawable",
					getPackageName());
			BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(resID);

			mMap.addMarker(new MarkerOptions()
					.position(new LatLng(s.getLatitude(), s.getLongitude()))
					.icon(icon)

					.title(s.getName() + " " + s.getAddress()));

		}
	}

	/**
	 * 
	 * El método cargaGoogleMap realiza la carga de la referencia a GoogleMap.
	 * 
	 * @author vicdoz
	 */
	private void cargaGoogleMap() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.mapa)).getMap();
			if (mMap != null) {
				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				mMap.setMyLocationEnabled(true);

			}
		}
	}

	/**
	 * Método que se llama cada vez que cambia la localizacion
	 * 
	 * @author vicdoz
	 */
	@Override
	public void onLocationChanged(Location location) {
		myLatitude = location.getLatitude();
		myLongitude = location.getLongitude();
		Log.d("LOCATION", "Latitud = " + myLatitude + " Longitude = "
				+ myLongitude);
		circleInMap(view);
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		switch (v.getId()) {
		case R.id.EditRadiusCircle:
			try {
				radius_in_KM = Float
						.parseFloat(radiusEditText.getText().toString()) * 1000;
			} catch(NumberFormatException e) {
				radius_in_KM = AppConstants.defaultPreferencesRadius * 1000;
				SharedPreferences preferences = SharedPreferencesManager.getNearbyMarketPreferences();
				radiusEditText.setText(Float.valueOf(preferences.getFloat("radius", AppConstants.defaultPreferencesRadius)).toString());
			} finally {
				// Oculta el teclado
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(radiusEditText.getWindowToken(), 0);
			}
			circleInMap(v);
			view = v;
			break;
		}
		return true;
	}

	/**
	 * Cargamos el menu de la activity
	 * 
	 * @author vicdoz
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.settingsmap, menu);
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
		case R.id.forwardtoList:
			startActivity(new Intent(this, SettingsActivity.class));
			savePreferences();
			finish();
			break;
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void savePreferences() {
		SharedPreferences preferences = SharedPreferencesManager
				.getNearbyMarketPreferences();
		SharedPreferences.Editor editor = preferences.edit();
		try {
			editor.putFloat("radius",
				Float.parseFloat(radiusEditText.getText().toString()));
		} catch (NumberFormatException e) {
			editor.putFloat("radius", AppConstants.defaultPreferencesRadius);
		}
		editor.putFloat("latitude", (float) myLatitude);
		editor.putFloat("longitude", (float) myLongitude);
		editor.commit();
	}

	private void loadPreferences() {
		SharedPreferences preferences = SharedPreferencesManager
				.getNearbyMarketPreferences();
		float radius = preferences.getFloat("radius",
				AppConstants.defaultPreferencesRadius);
		radius_in_KM = radius * 1000;
		radiusEditText.setText(Float.valueOf(radius).toString());
		myLatitude = preferences.getFloat("latitude",
				AppConstants.latitudeValencia);
		myLongitude = preferences.getFloat("longitude",
				AppConstants.longitudeValencia);
	}

	@Override
	public void onBackPressed() {
		if (pref.getBoolean("isFirst", true))
			finish();
		else
			super.onBackPressed();
	}
}
