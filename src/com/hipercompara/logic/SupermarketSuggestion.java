package com.hipercompara.logic;

/**
 * Clase SupermarketSuggestion 
 * Contiene el supermercado y el precio de la compra en ese supermercado
 * Esta clase representa una de las sugerencias cuando calculamos el supermercado 
 * mas barato
 * @author Luis Miguel
 *
 */
public class SupermarketSuggestion implements Comparable<SupermarketSuggestion> {
	
	/**
	 * Contructor por defecto
	 */
	public SupermarketSuggestion() {
		super();
	}
	
	/**
	 * Obtiene el precio de la compra en ese supermercado
	 * @return precio de la compra
	 */
	public float getTotalPrice() {
		return totalPrice;
	}
	
	/**
	 * Inicializa el precio de la compra 
	 * @param totalPrice precio de la compra
	 */
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	/**
	 * Obtiene el supermercado 
	 * @return supermercado
	 */
	public Supermarket getSuperMarket() {
		return superMarket;
	}
	
	/**
	 * Inicializa el supermercado 
	 * @param superMarket suppermercado
	 */
	public void setSuperMarket(Supermarket superMarket) {
		this.superMarket = superMarket;
	}
	
	/**
	 * Metodo que sirve para poder ordenar las sugerencias por precio
	 */
	@Override
	public int compareTo(SupermarketSuggestion another) {
		return Float.compare(totalPrice, another.getTotalPrice());
	}
	
	/**
	 * Metodo toString
	 */
	@Override
	public String toString() {
		return superMarket.toString() + " " + Float.toString(totalPrice);
	}

	private float totalPrice;
	private Supermarket superMarket;
}
