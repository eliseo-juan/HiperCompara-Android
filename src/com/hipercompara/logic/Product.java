package com.hipercompara.logic;

import java.text.DecimalFormat;

import com.hipercompara.dao.HCdbManager;

/**
 * Clase Product, con ella representamos un producto generico
 * @author Luis Miguel
 */
public class Product implements Comparable<Product>{
	
	/**
	 * Constructor por defecto
	 */
	public Product() {
		super();
		timesInList = 1;
	}
	
	/**
	 * Obtiene el identificador del producto
	 * @return identificador del producto
	 */
	public int getId() {
		return id;
	}

	/**
	 * Inicializa el identificador del producto
	 * @param id identificador del producto
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Obtiene la marca asociada al producto
	 * @return marca
	 */
	public Brand getBrand() {
		if(brand == null)
			brand = HCdbManager.getDb().getBrandbyId(brandId);
		return brand;
	}
	
	/**
	 * Inicializa el id de la marca del producto, esto es importante para 
	 * que al pedir la marca se obtenga la marca correcta
	 * @param brandId identificador de la marca
	 */
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	
	/**
	 * Obtiene el identificador de la marca del producto
	 * @return identificador de la marca
	 */
	public int getBrandId() {
		return brandId;
	}

	/**
	 * Obtiene el precio medio del producto
	 * @return precio medio
	 */
	public float getAveragePrice() {
		return averagePrice;
	}

	/**
	 * Inicializa el precio medio del producto
	 * @param averagePrice precio medio
	 */
	public void setAveragePrice(float averagePrice) {
		this.averagePrice = averagePrice;
	}

	/**
	 * Obtiene el nombre del producto
	 * @return nombre
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Inicializa el nombre del producto
	 * @param name nombre
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Obtiene la descripcion del producto
	 * @return descripcion
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Inicializa la descripcion del producto
	 * @param description descripcion
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Obtiene la cantidad de producto que tiene el producto
	 * esta cantidad se mide en la unidad indicada por unitType
	 * @return cantidad
	 */
	public float getQuantity() {
		return quantity;
	}

	/**
	 * Inicializa la cantidad del producto
	 * @param quantity cantidad
	 */
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	/**
	 * Obtiene el numero de veces que aparece el producto en la lista 
	 * de la compra
	 * @return numero de veces que hemos añadido el producto
	 */
	public int getTimesInList() {
		return timesInList;
	}

	/**
	 * Actualiza el numero de veces que hemos añadido el producto
	 * @param timesInList numero de veces
	 */
	public void setTimesInList(int timesInList) {
		this.timesInList = timesInList;
	}

	/**
	 * Obtiene el tipo de unidad en que se mide la cantidad del producto
	 * @return tipo de unidad
	 */
	public UnitType getUnitType() {
		if(unitType == null)
			unitType = HCdbManager.getDb().getUnitTypebyId(unitTypeId);
		return unitType;
	}
	
	/**
	 * Inicializa el identificador del tipo de unidad, es importante
	 * para que getUnitType funcione correctamente
	 * @param unitTypeId identificador del tipo de unidad
	 */
	public void setUnitTypeId(int unitTypeId) {
		this.unitTypeId = unitTypeId;
	}
	
	/**
	 * Obtiene el identificador del tipo de unidad
	 * @return identificador del tipo de unidad
	 */
	public int getUnitTypeId() {
		return unitTypeId;
	}
	
	/**
	 * Funcion para que se puedan ordenar los productos,
	 * se ordenan por orden alfetico del nombre 
	 */
	@Override
	public int compareTo(Product other) {
		return name.compareTo(other.getName());
	}

	/**
	 * Obtiene la categoria del producto
	 * @return categoria
	 */
	public Category getCategory() {
		if(category == null)
			category = HCdbManager.getDb().getCategoryById(categoryId);
		return category;
	}

	/**
	 * Inicializa el identificador de la categoria del producto,
	 * muy importante inicializarlo ya que si no, no se podra 
	 * obtener correctamente la categoria del producto
	 * @param categoryId identificador de la categoria
	 */
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	/**
	 * Obtiene el identificador de la categoria del producto 
	 * @return identificador de la categoria
	 */
	public int getCategoryId() {
		return categoryId;
	}

	/**
	 * Metodo toString en primer lugar aparece el nombre del producto,
	 * si tiene una marca aparecera despues entre parentesis, por ultimo 
	 * aparece el precio medio del producto.
	 * Esta cadena es la que se debe mostrar al buscar un producto por nombre
	 */
	@Override
	public String toString() {
		String result = name;
		Brand b = getBrand();
		// Si hay marca y no es marca blanca muestra el nombre
		if(b != null && brandId != 100 )
			result = result + " (" + b.getName() +")";
		result = result + " " + String.valueOf(new DecimalFormat("##.##").format(averagePrice)) + " €";
		return result;
	}

	private int id;
	private String name;
	private Brand brand;
	private int brandId;
	private float averagePrice; // Precio medio de todos los productos concretos de ese tipo
	private String description;
	private float quantity;     // Cantidad del producto en su unidad correspondiente (Ej 500.0 ml)
	private int timesInList;    // Cantidad de veces que el producto se pone en la lista
	private UnitType unitType;  // Unidad en la que se expresa el valor quantity
	private int unitTypeId;
	private Category category;
	private int categoryId;
}
