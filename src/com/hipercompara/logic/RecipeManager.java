package com.hipercompara.logic;

import java.util.ArrayList;

import com.hipercompara.dao.HCdbManager;


/**
 * RecipeManager
 * Clase que gestiona las recetas 
 * @author Eliseo
 *
 */
public class RecipeManager {
	
	/**
	 * Constructor
	 */
	private RecipeManager() {
		super();
	}

	/**
	 * Devuelve la unica instancia de la clase
	 * @return instancia de la clase
	 */
	public static RecipeManager instance() {
		if(manager == null)
			manager = new RecipeManager();
		return manager;
	}
	
	/**
	 * Devuelve una lista con todas las recetas que hay en la 
	 * base de datos
	 * @return lista de recetas
	 */
	public ArrayList<Recipe> getAllRecipes() {
		if(recipes == null)
			recipes = HCdbManager.getDb().getAllRecipes();
		return recipes;
	}

	
	private static RecipeManager manager; // Instancia de la clase
	private ArrayList<Recipe> recipes;    // Lista de recetas
}
