package com.hipercompara.gui;

import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.hipercompara.R;
import com.hipercompara.logic.Product;
import com.hipercompara.logic.Recipe;
import com.hipercompara.logic.ShoppingList;

/**
 * Adaptador que muestra las recetas expandibles
 * 
 * @author Eliseo Juan
 */
public class RecipeAdapter extends BaseExpandableListAdapter implements
		OnCheckedChangeListener {

	private final ArrayList<Recipe> recipes;
	public LayoutInflater inflater;
	public Context context;
	ArrayList<Product> products;
	Vector<Integer> productsId = new Vector<Integer>();

	/**
	 * Constructor del adaptador
	 * 
	 * @see com.hipercompara.gui.RecipeAdapter
	 * @author Eliseo Juan
	 * @param context
	 *            Contexto del activity que lo invoca
	 * @param recipes
	 *            Conjunto de Recetas a mostrar
	 */
	public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
		this.context = context;
		this.recipes = recipes;
		inflater = LayoutInflater.from(context);
		this.products = ShoppingList.instance().getProducts();

		for (int i = 0; i < products.size(); i++)
			productsId.add(products.get(i).getId());
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return recipes.get(groupPosition).getProducts().get(childPosition);
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return recipes.get(groupPosition).getProducts().get(childPosition)
				.getId();
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean,
	 *      View, ViewGroup)
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		convertView = inflater.inflate(R.layout.recipe_product_list_row, null);
		Product p = recipes.get(groupPosition).getProducts().get(childPosition);
		CheckBox productCheckBox = (CheckBox) convertView
				.findViewById(R.id.checkBoxRecipeProduct);
		productCheckBox.setText(p.getName());
		if (productsId.contains(p.getId()))
			productCheckBox.setChecked(true);
		productCheckBox.setOnCheckedChangeListener(this);
		productCheckBox.setTag(getChild(groupPosition, childPosition));
		return convertView;
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		return recipes.get(groupPosition).getProducts().size();
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return recipes.get(groupPosition);
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		return recipes.size();
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return recipes.get(groupPosition).getId();
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean,
	 *      View, ViewGroup)
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		convertView = inflater.inflate(R.layout.recipe_list_row, null);

		TextView name = (TextView) convertView
				.findViewById(R.id.textViewRecipeName);
		TextView description = (TextView) convertView
				.findViewById(R.id.textViewRecipeDescription);
		name.setText(recipes.get(groupPosition).getName());
		description.setText(recipes.get(groupPosition).getDescription());
		return convertView;
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int arg0, int
	 *      arg1)
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @author Eliseo Juan
	 * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(CompoundButton
	 *      arg0, boolean arg1)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Product p = (Product) buttonView.getTag();
		if (isChecked)
			ShoppingList.instance().addProduct(p);
		else
			ShoppingList.instance().removeProductById(p.getId());

	}
}