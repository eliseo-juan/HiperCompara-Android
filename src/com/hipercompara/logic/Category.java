package com.hipercompara.logic;

import java.util.ArrayList;

import com.hipercompara.dao.HCdbManager;

/**
 * Category
 * Clase para definir una categoria por la que ordenar productos
 * @author Luis Miguel
 *
 */
public class Category {
	/**
	 * Constructor
	 */
	public Category() {
		this.id = -1;
		this.name = "";
		this.iconName = "default";
	}
	
	/**
	 * Obtiene el identificador de la categoria
	 * @return id de la categoría
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Inicializa el identificador de la categoria
	 * @param id Identificador de la categoria
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Obtiene el nombre de la categoria
	 * @return nombre de la categoria
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Inicializa el nombre de la categoria
	 * @param name nuevo nombre
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Obtiene todas las subcategorias asociadas a la categoria
	 * Para poder navegar por categorias esto es necesario
	 * @return lista con las subcategorias
	 */
	public ArrayList<SubCategory> getSubcategories() {
		if(subcategories == null)
			subcategories = HCdbManager.getDb().getSubCategories(id);
		return subcategories;
	}
	
	/**
	 * Inicializa la lista de las subcategorias
	 * @param subcategories lista de subcategorias
	 */
	public void setSubcategories(ArrayList<SubCategory> subcategories) {
		this.subcategories = subcategories;
	}
	
	/**
	 * Obtiene el nombre del icono que se debe usar en la aplicacion 
	 * para representar la categoria
	 * @return nombre del icono
	 */
	public String getIconName() {
		return iconName;
	}

	/**
	 * Inicializa el nombre del icono
	 * @param iconName nombre del icono
	 */
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}


	private int id;
	private String name;
	private String iconName; // Nombre del icono que aparecera en la aplicación
	private ArrayList<SubCategory> subcategories;
}
