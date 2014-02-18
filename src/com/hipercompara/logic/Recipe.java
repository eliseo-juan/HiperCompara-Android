package com.hipercompara.logic;

import java.util.ArrayList;

/**
 * Clase que representa una receta de cocina
 * @author Eliseo
 *
 */
public class Recipe {
	
	/**
	 * Constructor por defecto
	 */
	public Recipe() {
		id = 0;
		name = "";
		description = "";
		products = null;
	}

	/**
	 * Constructor
	 * @param name nombre de la receta
	 * @param description descripcion
	 * @param products lista de productos que tiene la receta
	 */
	public Recipe(String name, String description, ArrayList<Product> products) {
		super();
		this.name = name;
		this.description = description;
		this.products = products;
	}
	
	/**
	 * Obtiene el nombre de la receta
	 * @return nombre de la receta
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Inicializa el nombre de la receta
	 * @param name nombre
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Obtiene la descripcion
	 * @return descripcion
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Inicializa la descripcion de la receta
	 * @param description descripcion
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Obtiene la lista de productos que contiene la receta
	 * @return lista de productos
	 */
	public ArrayList<Product> getProducts() {
		return products;
	}
	
	/**
	 * Inicializa los productos de la receta
	 * @param products lista de productos
	 */
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}
	
	/**
	 * Obtiene el identificador de la receta
	 * @return identificador de la receta
	 */
	public int getId() {
		return id;
	}

	/**
	 * Inicializa el identificador de la receta
	 * @param id identificador
	 */
	public void setId(int id) {
		this.id = id;
	}

	private int id;
	private String name;
	private String description;
	private ArrayList<Product> products;
}
