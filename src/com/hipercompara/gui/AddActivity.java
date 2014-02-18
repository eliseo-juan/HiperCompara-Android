package com.hipercompara.gui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Spinner;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.hipercompara.R;
import com.hipercompara.logic.Category;
import com.hipercompara.logic.Product;
import com.hipercompara.logic.SubCategory;

/**
 * La clase AddActivity se encarga de mostrar las categorías y al seleccionar
 * una se llama a la siguiente con el id de la categoria
 * 
 * @author Eliseo Juan
 */
public class AddActivity extends SherlockActivity implements OnClickListener {

	Spinner subCategorySpinner, brandSpinner;
	Category category;
	ListView productListView;
	SubCategory subCategory;
	HashMap<Integer, Integer> subCategoryMap;
	HashMap<Integer, String> brandMap;
	ProductFilteredAdapter productsAdapter;
	ArrayList<Product> productsListBySubCategory;
	ArrayList<Product> productsListBySubCategoryAndBrand;
	int viewFlag = 0;
	ActionBar actionBar;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @author Eliseo Juan
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.ic_launcher_bar);
		actionBar.setTitle("Selecciona categoría");

		findViewById(R.id.imageButtonAddCarne).setOnClickListener(this);
		findViewById(R.id.imageButtonAddPesacado).setOnClickListener(this);
		findViewById(R.id.imageButtonAddFruta).setOnClickListener(this);
		findViewById(R.id.imageButtonAddLacteos).setOnClickListener(this);
		findViewById(R.id.imageButtonAddVarios).setOnClickListener(this);
		findViewById(R.id.imageButtonAddPanaderia).setOnClickListener(this);
		findViewById(R.id.imageButtonAddCongelados).setOnClickListener(this);
		findViewById(R.id.imageButtonAddDrogueria).setOnClickListener(this);
		findViewById(R.id.imageButtonAddBebida).setOnClickListener(this);
		findViewById(R.id.imageButtonUltimasCompras).setOnClickListener(this);

	}

	/**
	 * Según la categoria pulsada se lanza nextFilterLayout con un id de
	 * categoría
	 * 
	 * @see android.view.View.OnClickListener#onClick(View v)
	 * @author Eliseo Juan
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imageButtonAddCarne:
			nextFilterLayout(2);
			break;
		case R.id.imageButtonAddPesacado:
			nextFilterLayout(3);
			break;
		case R.id.imageButtonAddFruta:
			nextFilterLayout(5);
			break;
		case R.id.imageButtonAddLacteos:
			nextFilterLayout(6);
			break;
		case R.id.imageButtonAddVarios:
			nextFilterLayout(9);
			break;
		case R.id.imageButtonAddPanaderia:
			nextFilterLayout(7);
			break;
		case R.id.imageButtonAddCongelados:
			nextFilterLayout(3);
			break;
		case R.id.imageButtonAddDrogueria:
			nextFilterLayout(4);
			break;
		case R.id.imageButtonAddBebida:
			nextFilterLayout(1);
			break;
		case R.id.imageButtonUltimasCompras:
			startActivity(new Intent(this, RecentActivity.class));
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * Funcion que llama al siguiente activity por subcategorías
	 * 
	 * @author Eliseo Juan
	 * @param i
	 *            Flag de Categoría
	 */
	private void nextFilterLayout(int i) {
		Intent intent = new Intent(this, NextFilterActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putInt("categoryId", i);
		intent.putExtras(mBundle);
		startActivity(intent);
	}

	/**
	 * @see com.actionbarsherlock.app.SherlockActivity#onOptionsItemSelected(MenuItem)
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
