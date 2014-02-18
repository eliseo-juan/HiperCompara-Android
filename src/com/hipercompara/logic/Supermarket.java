package com.hipercompara.logic;

/**
 * Supermarket
 * Clase para representar un supermercado
 * @author Luis Miguel
 *
 */
public class Supermarket {
	
	/**
	 * Constructor por defecto
	 */
	public Supermarket() {
		super();
		id = 0;
		name = "";
		country = "";
		city = "";
		postalCode = 0;
		address = "";
	}
	
	/**
	 * Obtiene el identificador del supermercado
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Inicializa el identificador del supermercado
	 * @param id identificador
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Obtiene el nombre del supermercado
	 * @return nombre
	 */
	public String getName() {
		return name;
	}

	/**
	 * Inicializa el nombre del supermercado
	 * @param name nombre
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Obtiene el pais del supermercado
	 * @return pais
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Inicializa el pais del supermercado
	 * @param country nombre del pais
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * Obtiene la ciudad del supermercado
	 * @return ciudad
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Inicializa la ciudad
	 * @param city ciudad
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Obtiene el codigo postal
	 * @return codigo postal
	 */
	public int getPostalCode() {
		return postalCode;
	}

	/**
	 * Inicializa el codigo postal
	 * @param postalCode codigo postal
	 */
	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * Obtiene la direccion
	 * @return direccion
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Inicializa la direccion
	 * @param address direccion
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Obtiene la latitud
	 * @return latitud
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Inicializa la latitud
	 * @param latitude latitud
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Obtiene la longitud
	 * @return longitud
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Inicializa la longitud
	 * @param longitude longitud
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Obtiene el nombre del icono del supermercado,
	 * este nombre se buscara en los recursos para ponerle
	 * el icono adecuado
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

	/**
	 * Metodo toString
	 */
	@Override
	public String toString() {
		return name;
	}

	int id;
	String name;
	String country;
	String city;
	int postalCode;
	String address;
	double latitude;
	double longitude;
	String iconName;
}
