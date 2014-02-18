package com.hipercompara.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.hipercompara.R;
import com.hipercompara.logic.Category;
import com.hipercompara.logic.Product;
import com.hipercompara.logic.ProductSearchEngine;
import com.hipercompara.logic.ShoppingList;
import com.hipercompara.logic.SubCategory;

/**
 * Activity precedido de AddActivity en la que puedes seleccionar la
 * subCategoria y la marca para mostrar los productos filtrados.
 * 
 * @author Eliseo Juan
 */
public class NextFilterActivity extends SherlockActivity implements
		OnItemClickListener, OnItemSelectedListener {

	Spinner subCategorySpinner, brandSpinner;
	Category category;
	ListView productListView;
	SubCategory subCategory;
	HashMap<Integer, Integer> subCategoryMap;
	HashMap<Integer, String> brandMap;
	ProductFilteredAdapter productsAdapter;
	ArrayList<Product> productsListBySubCategory;
	ArrayList<Product> productsListBySubCategoryAndBrand;
	Bundle bundle;
	ActionBar actionBar;
	int categoryId;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @author Eliseo Juan
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_next_filter);

		bundle = getIntent().getExtras();
		categoryId = bundle.getInt("categoryId");

		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);

		subCategorySpinner = (Spinner) findViewById(R.id.spinnerFilterSubCategory);
		brandSpinner = (Spinner) findViewById(R.id.spinnerFilterBrand);
		productListView = (ListView) findViewById(R.id.listViewFilterProducts);
		productListView.setOnItemClickListener(this);
		subCategorySpinner.setOnItemSelectedListener(this);
		brandSpinner.setOnItemSelectedListener(this);
		category = ProductSearchEngine.instance().getCategoryById(categoryId);
		refreshSubCategorySpinner();
		actionBar.setTitle("Productos en " + category.getName());
	}

	/**
	 * Funcion que actualiza los elementos del Spinner de subCategoría
	 * 
	 * @author Eliseo Juan
	 */
	private void refreshSubCategorySpinner() {

		List<String> spinnerAdapter = new ArrayList<String>();
		subCategoryMap = new HashMap<Integer, Integer>();
		for (int j = 0; category != null && category.getSubcategories() != null
				&& j < category.getSubcategories().size(); j++) {
			spinnerAdapter.add(category.getSubcategories().get(j).getName());
			subCategoryMap.put(j, category.getSubcategories().get(j).getId());
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinnerAdapter);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		subCategorySpinner.setAdapter(dataAdapter);
		subCategory = category.getSubcategories().get(0);
		refreshProductList("Todas");
		refreshBrandSpinner();
	}

	/**
	 * Funcion que actualiza la lista de productos una vez elegido subtipo o
	 * marca
	 * 
	 * @author Eliseo Juan
	 * @param brand
	 *            Nombre de la marca seleccionada.
	 */
	private void refreshProductList(String brand) {
		productsListBySubCategory = subCategory.getProducts();
		productsListBySubCategoryAndBrand = new ArrayList<Product>();
		if (!brand.equals("Todas")) {
			int brandId = Integer.parseInt(brand);
			for (int i = 0; productsListBySubCategory != null
					&& i < productsListBySubCategory.size(); i++) {
				if (brandId == productsListBySubCategory.get(i).getBrandId())
					productsListBySubCategoryAndBrand
							.add(productsListBySubCategory.get(i));
			}
		} else
			productsListBySubCategoryAndBrand = productsListBySubCategory;
		productsAdapter = new ProductFilteredAdapter(this,
				productsListBySubCategoryAndBrand);
		productListView.setAdapter(productsAdapter);

	}

	/**
	 * Funcion que actualiza los elementos del Spinner de marca
	 * 
	 * @author Eliseo Juan
	 */
	private void refreshBrandSpinner() {
		List<String> brandSpinnerAdapter = new ArrayList<String>();
		brandSpinnerAdapter.add("Todas");
		brandMap = new HashMap<Integer, String>();
		brandMap.put(0, "Todas");
		int counter = 1;
		for (int j = 0; productsListBySubCategory != null
				&& j < productsListBySubCategory.size(); j++) {
			if (!brandMap.containsValue(String
					.valueOf(productsListBySubCategory.get(j).getBrandId()))) {
				brandSpinnerAdapter.add(productsListBySubCategory.get(j)
						.getBrand().getName());
				brandMap.put(counter, String.valueOf(productsListBySubCategory
						.get(j).getBrandId()));
				++counter;
			}
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, brandSpinnerAdapter);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		brandSpinner.setAdapter(dataAdapter);

	}

	/**
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(
	 *      AdapterView<?> arg0, View arg1, int arg2, long arg3)
	 * @author Eliseo Juan
	 */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (arg0.getId() == R.id.spinnerFilterSubCategory) {
			subCategory = category.getSubcategories().get(arg2);
			refreshProductList("Todas");
			refreshBrandSpinner();
		} else if (arg0.getId() == R.id.spinnerFilterBrand) {
			refreshProductList(brandMap.get(arg2));
		}
	}

	/**
	 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(
	 *      AdapterView<?> arg0)
	 * @author Eliseo Juan
	 */
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
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
