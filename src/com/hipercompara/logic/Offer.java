package com.hipercompara.logic;
import java.util.Date;

/**
 * Clase que representa a una oferta
 * @author Luis Miguel
 *
 */
public class Offer {
	
	/**
	 * Calcula y devuelve el precio anterior del producto
	 * @return precio anterior
	 */
	public float getPrice() {
		return (newPrice*100.0f)/(100.0f-discountPercentage);
	}
	
	/**
	 * Obtiene el precio actual del producto
	 * @return precio actual
	 */
	public float getNewPrice() {
		return newPrice;
	}

	/**
	 * Inicializa el valor del precio nuevo
	 * @param newPrice nuevo precio
	 */
	public void setNewPrice(float newPrice) {
		this.newPrice = newPrice;
	}
	
	/**
	 * Obtiene el porcentaje de descuento
	 * @return porcentaje de descuento
	 */
	public float getDiscountPercentage() {
		return discountPercentage;
	}

	/**
	 * Inicializa el valor del porcentaje de descuento
	 * @param discountPercentage porcentaje de descuento (sobre 100)
	 */
	public void setDiscountPercentage(float discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	/**
	 * Obtiene la fecha de inicio de la oferta
	 * @return fecha de inicio
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Inicializa el valor de la fecha de inicio 
	 * @param startDate fecha de inicio
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Obtiene la fecha de finalizacion de la oferta
	 * @return fecha de finalizacion
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Inicializa la fecha de fin de la oferta
	 * @param endDate fecha de fin
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Obtiene el producto asociado con la oferta
	 * @return producto
	 */
	public Product getProduct() {
		return product;
	}
	
	/**
	 * Cambia el producto asociado con la oferta
	 * @param product Producto
	 */
	public void setProduct(Product product) {
		this.product = product;
	}
	
	/**
	 * Obtiene el supermercado que realiza la oferta
	 * @return supermercado
	 */
	public Supermarket getSupermarket() {
		return supermarket;
	}
	
	/**
	 * Cambia el supermercado de la oferta
	 * @param supermarket supermercado
	 */
	public void setSupermarket(Supermarket supermarket) {
		this.supermarket = supermarket;
	}
	
	private float newPrice;
	private float discountPercentage;
	private Date startDate;
	private Date endDate;
	private Product product;
	private Supermarket supermarket;
}
