package com.hipercompara.gui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.InputFilter;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.hipercompara.R;
import com.hipercompara.dao.HCdbManager;
import com.hipercompara.logic.Product;
import com.hipercompara.logic.ProductSearchEngine;
import com.hipercompara.logic.SharedPreferencesManager;
import com.hipercompara.logic.ShoppingList;
import com.hipercompara.logic.Supermarket;
import com.hipercompara.logic.SupermarketSuggestion;
import com.hipercompara.logic.SupermarketSuggestionManager;

/**
 * Activity principal desde el cual se puede acceder a todas las opciones y
 * pantallas
 * 
 * @author Eliseo Juan
 */
public class MainActivity extends SherlockActivity implements
		SearchView.OnQueryTextListener, SearchView.OnSuggestionListener,
		OnClickListener {

	RelativeLayout relative;
	ListView shoppingListView;
	ArrayList<Product> shoppingList;
	Button estimateButton;
	ImageButton nextActivityButton;
	LinearLayout resultLayout, loadingLayout;
	ImageView logoSupermarketImage;
	TextView priceTextView;
	ActionBar actionBar;
	ShoppingListAdapter adapter;
	private static final String[] COLUMNS = { BaseColumns._ID,
			SearchManager.SUGGEST_COLUMN_TEXT_1, };

	private ProductSuggestionsAdapter mSuggestionsAdapter;
	SearchView searchView;
	HashMap<Integer, Integer> searchViewId;
	ArrayList<Product> productList;
	public boolean estimatePriceListener;
	public boolean suggestionsIsOK = false;
	private int contextualIdProduct;
	SharedPreferences pref;
	ProgressDialog pd;
	Context context;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @author Eliseo Juan
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pref = getSharedPreferences("default", Context.MODE_PRIVATE);

		if (pref.getBoolean("isFirst", true)) {
			Intent i = new Intent(this, NearbyMarket.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}

		context = this;

		searchView = new SearchView(getSupportActionBar().getThemedContext());

		actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.ic_launcher_bar);
		actionBar.setTitle("Lista");

		shoppingListView = (ListView) findViewById(R.id.listViewMainShoppingList);
		estimateButton = (Button) findViewById(R.id.buttonMainEstimate);
		nextActivityButton = (ImageButton) findViewById(R.id.buttonMainNextActivity);
		logoSupermarketImage = (ImageView) findViewById(R.id.imageMainLogoSupermmarket);
		priceTextView = (TextView) findViewById(R.id.textViewMainPrice);
		resultLayout = (LinearLayout) findViewById(R.id.linearLayoutMainResult);
		loadingLayout = (LinearLayout) findViewById(R.id.linearLayoutMainLoading);
		relative = (RelativeLayout) findViewById(R.id.addProductLayout);

		nextActivityButton.setOnClickListener(this);
		estimateButton.setOnClickListener(this);

		// Pasa el contexto al controlador de la base de datos
		HCdbManager.setContext(getApplicationContext());
		SharedPreferencesManager.setContext(getApplicationContext());
		registerForContextMenu(shoppingListView);

		if (!pref.getBoolean("showHint", true)) {
			relative.setVisibility(View.GONE);
		}

	}

	/**
	 * @see android.app.Activity#onStart()
	 * @author Eliseo Juan
	 */
	@Override
	protected void onStart() {
		if (pref.getBoolean("isFirst", true))
			finish();
		super.onStart();
	}

	/**
	 * Consulta los elementos de ShoppingList y los pone en la lista
	 * 
	 * @author Eliseo Juan
	 */
	private void refreshShoppingList() {

		shoppingList = ShoppingList.instance().getProducts();
		if (shoppingList.size() != 0) {
			adapter = new ShoppingListAdapter(this, shoppingList);
			relative.setVisibility(View.GONE);
			pref.edit().putBoolean("showHint", false).commit();
			shoppingListView.setAdapter(adapter);

		} else if (shoppingList != null || shoppingList.size() == 0) {
			estimateButton.setVisibility(View.GONE);
		}
	}

	/**
	 * Funcion que se activa cuando se pulsa el boton de borrar un producto de
	 * la lista
	 * 
	 * @author Eliseo Juan
	 */
	public void onRowClicked(View v) {
		int position = (Integer) v.getTag();
		final Product p = (Product) adapter.getItem(position);

		Toast.makeText(getApplicationContext(), "Click en" + p.getName(),
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * Lanza el activity deseado
	 * 
	 * @see android.view.View.OnClickListener#onClick(View v)
	 * @author Eliseo Juan
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonMainNextActivity:
			startActivity(new Intent(this, MarketActivity.class));
			break;
		case R.id.buttonMainEstimate:
			EstimatePrice task = new EstimatePrice();
			task.execute();
			break;

		}
	}

	/**
	 * Funcion precedida por estimatePrice que genera el resultado y lo muestra.
	 * 
	 * @author Eliseo Juan
	 */
	private void loadResult() {
		SupermarketSuggestion suggestion = SupermarketSuggestionManager
				.instance().getSuggestions().get(0);
		Supermarket market = suggestion.getSuperMarket();
		int id = getResources().getIdentifier(
				"market_" + market.getName().toLowerCase(), "drawable",
				getPackageName());
		logoSupermarketImage.setImageResource(id);

		priceTextView.setText(suggestion.getTotalPrice()
				+ getString(R.string.euro_symbol));
		// Guardamos en la BD los productos con los que se ha calculado la lista
		// para sugerencias personalizadas
		HCdbManager.getDb().addPurchasedProducts();

		loadingLayout.setVisibility(View.GONE);
		resultLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * Funcion que restaura la interfaz del cálculo
	 * 
	 * @author Eliseo Juan
	 */
	public void restoreResult() {
		estimateButton.setVisibility(View.VISIBLE);
		resultLayout.setVisibility(View.GONE);
		loadingLayout.setVisibility(View.GONE);
	}

	/**
	 * @see SherlockActivity#onCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 * @author Eliseo Juan
	 */
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

		searchView.setQueryHint("Buscar productos");
		searchView.setOnQueryTextListener(this);
		searchView.setOnSuggestionListener(this);

		menu.add("Buscar")
				.setIcon(android.R.drawable.ic_menu_search)
				.setActionView(searchView)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * @see SherlockActivity#onOptionsItemSelected(MenuItem)
	 * @author Eliseo Juan
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_delete_list:
			ShoppingList.instance().removeAllProducts();
			shoppingList.removeAll(shoppingList);
			restoreResult();
			refreshShoppingList();

			break;
		case R.id.action_add:
			startActivity(new Intent(this, AddActivity.class));
			break;
		case R.id.action_recipes:
			startActivity(new Intent(this, RecipeActivity.class));
			break;
		case R.id.action_offers:
			startActivity(new Intent(this, OffersActivity.class));
			break;
		case R.id.action_settings:
			startActivity(new Intent(this, NearbyMarket.class));
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * @see android.app.Activity#onResume()
	 * @author Eliseo Juan
	 */
	@Override
	public void onResume() {
		if (!HCdbManager.isDbCreated()) {
			FirstTime task = new FirstTime();
			task.execute();
		} else {
			restoreResult();
			refreshShoppingList();

		}
		super.onResume();
	}

	/**
	 * @see com.actionbarsherlock.widget.SearchView.OnQueryTextListener#onQueryTextSubmit
	 * @author Eliseo Juan
	 */
	@Override
	public boolean onQueryTextSubmit(String query) {
		return true;
	}

	/**
	 * @see android.view.View.OnClickListener#onClick(View v)
	 * @author Eliseo Juan
	 */
	@Override
	public boolean onQueryTextChange(String newText) {
		if (newText.length() >= 2) {
			productList = ProductSearchEngine.instance().search(newText);
			mSuggestionsAdapter = null;
			MatrixCursor cursor = new MatrixCursor(COLUMNS);
			searchViewId = new HashMap<Integer, Integer>();
			cursor.addRow(new String[] { "" + 0,
					getString(R.string.category_search_title) });
			int i = 1;
			for (Product p : productList) {
				cursor.addRow(new String[] { "" + i, p.toString() });
				searchViewId.put(i, Integer.valueOf(p.getId()));
				i++;
			}

			mSuggestionsAdapter = new ProductSuggestionsAdapter(
					getSupportActionBar().getThemedContext(), cursor);
			searchView.setSuggestionsAdapter(mSuggestionsAdapter);
		}
		return false;
	}

	/**
	 * @see com.actionbarsherlock.widget.SearchView.OnSuggestionListener#onSuggestionSelect(int)
	 * @author Eliseo Juan
	 */
	@Override
	public boolean onSuggestionSelect(int position) {
		return false;
	}

	/**
	 * @see com.actionbarsherlock.widget.SearchView.OnSuggestionListener#onSuggestionClick(int)
	 * @author Eliseo Juan
	 */
	@Override
	public boolean onSuggestionClick(int position) {
		if (position == 0) {
			startActivity(new Intent(this, AddActivity.class));
		} else {
			Product p = ProductSearchEngine.instance().getProduct(
					searchViewId.get(position));

			ShoppingList.instance().addProduct(p);
			restoreResult();
			refreshShoppingList();

			searchView.setQuery("", true);
		}
		return true;
	}

	/**
	 * Clase que actua como AsynTask y es el hilo que carga los supermercados
	 * mas baratos
	 * 
	 * @author Eliseo Juan
	 */
	private class EstimatePrice extends AsyncTask<Void, Integer, Boolean> {
		@Override
		protected void onPreExecute() {
			estimateButton.setVisibility(View.GONE);
			loadingLayout.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... voids) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			suggestionsIsOK = SupermarketSuggestionManager.instance()
					.computeCheapestSupermarkets();
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (suggestionsIsOK)
				loadResult();
			else
				restoreResult();
			super.onPreExecute();
		}
	}

	private class FirstTime extends AsyncTask<Void, Integer, Boolean> {
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(context);
			pd.setTitle("Inicializando...");
			pd.setMessage("Se está configurando la aplicación por primera vez");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		@Override
		protected Boolean doInBackground(Void... voids) {
			shoppingList = ShoppingList.instance().getProducts();
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			pd.dismiss();
			refreshShoppingList();

		}
	}

	/**
	 * Funcion que muestra todos los errores del Activity
	 * 
	 * @param i
	 *            Código de error
	 * @author Eliseo Juan
	 */
	public void error(int i) {
		AlertDialog.Builder builder = new Builder(this);
		switch (i) {
		case 1:// Connection
			builder.setTitle("Error");
			builder.setMessage("No hay conexiÃƒÆ’Ã‚Â³n a internet");
			builder.setNeutralButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
						}
					});
			break;
		case 2:
			builder.setTitle("Error");
			builder.setMessage("No se ha recibido ninguna sugerencia");
			builder.setNeutralButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
						}
					});
			break;
		default:
			break;
		}
		builder.create();
	}

	/**
	 * @see android.app.Activity#onCreateContextMenu(ContextMenu, View,
	 *      ContextMenuInfo)
	 * @author Eliseo Juan
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		int position = info.position;
		contextualIdProduct = position;
		android.view.MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.shoppinglist, menu);
	}

	/**
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 * @author Eliseo Juan
	 */
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {

		switch (item.getItemId()) {
		case R.id.shoppingListOp1:
			ShoppingList.instance().removeProductById(
					ShoppingList.instance().getProducts()
							.get(contextualIdProduct).getId());
			shoppingList.remove(contextualIdProduct);
			restoreResult();
			refreshShoppingList();

			return true;
		case R.id.shoppingListOp2:

			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Modificar cantidad");
			alert.setIcon(android.R.drawable.ic_dialog_alert);
			final EditText input = new EditText(this);
			int maxLength = 2;
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(maxLength);
			input.setFilters(FilterArray); // por ejemplo maximo 2 cifras
			input.setInputType(InputType.TYPE_CLASS_NUMBER);
			alert.setView(input);
			alert.setPositiveButton("Aceptar",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Product p = shoppingList.get(contextualIdProduct);
							int idProduct = p.getId();
							// Si se intruduce un 0 , se elimina de la lista
							if (Integer.parseInt(input.getText().toString()) == 0) {
								ShoppingList.instance().removeProductById(
										ShoppingList.instance().getProducts()
												.get(contextualIdProduct)
												.getId());
								shoppingList.remove(contextualIdProduct);
							} else {
								ShoppingList.instance()
										.updateTimesInListProduct(
												idProduct,
												Integer.parseInt(input
														.getText().toString()));
								shoppingList.set(contextualIdProduct, p);
							}
							restoreResult();
							refreshShoppingList();

						}

					});
			alert.setNegativeButton("Cancelar", null);
			alert.show();
		default:
			return super.onContextItemSelected(item);

		}

	}
}
