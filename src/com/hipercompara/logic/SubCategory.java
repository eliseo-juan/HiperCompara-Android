package com.hipercompara.logic;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.hipercompara.dao.ParseJSONProductBySubC;

/**
 * Subcategory 
 * Clase que define una subcategoria
 * @author Luis Miguel
 *
 */
public class SubCategory {

	/**
	 * Obtiene el identificador de la subcategoria
	 * @return identificador
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Inicializa el identificador de la subcategoria
	 * @param id identificador
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Obtiene el nombre de la subcategoria
	 * @return nombre
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Inicializa el nombre de la subcategoria
	 * @param name nombre
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Obtiene el idenetificador de la categoria a la pertenece la 
	 * subcategoria
	 * @return identificador de la categoria
	 */
	public int getCategoryId() {
		return categoryId;
	}

	/**
	 * Inicializa el identificador de la categoria
	 * @param categoryId identificador de la categoria
	 */
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * Obtiene todos los productos correspondientes a la subcategoria
	 * @return productos de la subcategoria
	 * */
	public ArrayList<Product> getProducts() {
		if(products == null) {
			try {
				ParseJSONProductBySubC pjsc = new ParseJSONProductBySubC();
				pjsc.execute(id);
				pjsc.get();
				products = pjsc.getProducts();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return products;
	}

	private int id;
	private String name;
	private int categoryId;
	private ArrayList<Product> products;
}
