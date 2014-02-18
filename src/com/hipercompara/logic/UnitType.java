package com.hipercompara.logic;

/**
 * Clase UnitType para representar los distintos tipos de unidad
 * kg, l, gr, ud, etc.
 * Quizas con un enum sobre pero asi se pueden ir poniendo tipos en la
 * base de datos segun hagan falta
 * @author Luis Miguel
 *
 */
public class UnitType {

	/**
	 * Obtiene el id
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Inicializa el id
	 * @param id identificador
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Obtiene el nombre
	 * @return nombre
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Inicializa el nombre
	 * @param name nombre
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	int id;
	String name;
}
