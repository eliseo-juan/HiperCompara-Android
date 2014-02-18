package com.hipercompara.gui;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ExpandableListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.hipercompara.R;
import com.hipercompara.logic.Recipe;
import com.hipercompara.logic.RecipeManager;

/**
 * Activity que muestra las recetas disponibles y los artículos que necesita
 * cada receta
 * 
 * @author Eliseo Juan
 */
public class RecipeActivity extends SherlockActivity {

	ArrayList<Recipe> recipes;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @author Eliseo Juan
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		loadData();

		ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandableListRecipes);
		RecipeAdapter adapter = new RecipeAdapter(this, recipes);
		listView.setAdapter(adapter);

	}

	/**
	 * @see SherlockActivity#onCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 * @author Eliseo Juan
	 */
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.recipe, menu);
		return true;
	}

	/**
	 * @see SherlockActivity#onOptionsItemSelected(MenuItem)
	 * @author Eliseo Juan
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_back:
			finish();
		case android.R.id.home:
			finish();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Carga todas las recetas
	 * 
	 * @author Eliseo Juan
	 */
	private void loadData() {
		recipes = new ArrayList<Recipe>();
		recipes = RecipeManager.instance().getAllRecipes();

	}
}
