package com.hipercompara.logic;
/***
 * Clase Brand, representa una marca
 * @author Luis Miguel
 *
 */
public class Brand {
	
	/**
	 * Devuelve el nombre de la marca
	 * @return nombre de la marca
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Inicializa el nombre de la marca
	 * @param name Nombre de la marca
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Obtiene el identificador de la marca
	 * @return id de la marca
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Inicializa el identificador de la marca
	 * @param id de la marca
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	
	String name;
	int id;
}
