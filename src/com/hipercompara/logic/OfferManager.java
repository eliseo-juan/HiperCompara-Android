package com.hipercompara.logic;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import com.hipercompara.dao.ParseJSONOffer;
import android.content.SharedPreferences;

/**
 * OfferManager
 * Clase que gestiona que ofertas se ven en la aplicacion
 * por ejemplo al poner un producto en oferta a la lista de la compra
 * esta clase ya no mostrara las ofertas que sean de otros supermercados
 * distintos al de la oferta escogida 
 * @author Luis Miguel
 *
 */

public class OfferManager {
	
	/**
	 * Constructor
	 */
	private OfferManager() {
		super();
		conditionsChanged = true;
	}

	/**
	 * Devuelve la unica instancia de la clase
	 * @return instancia de la clase
	 */
	public static OfferManager instance() {
		if(manager == null)
			manager = new OfferManager();
		return manager;
	}
	
	/**
	 * Devuelve las ofertas pertenecientes a una categoria
	 * @param c categoria
	 * @return lista de ofertas de la categoria
	 */
	public ArrayList<Offer> getOffersByCategory(Category c) {
		ArrayList<Offer> offersByCategory = new ArrayList<Offer>();
		for(Offer o: getAllOffers())
			if(o.getProduct().getCategoryId() == c.getId())
				offersByCategory.add(o);
		return offersByCategory;
	}
	
	/**
	 * Obtiene la lista de ofertas completa
	 * @return lista de ofertas
	 */
	public ArrayList<Offer> getAllOffers() {
		ArrayList<Offer> aux = null;
		
		if(offers == null || conditionsChanged) {
			try {
				ParseJSONOffer pjo = new ParseJSONOffer();
				pjo.execute();
				pjo.get();
				aux = pjo.getOffers();
				conditionsChanged = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			aux = offers;
		}
		offers = new ArrayList<Offer>();
		SharedPreferences preferences = SharedPreferencesManager.getSharedPreferences();
		
		// Selecciona solo las ofertas de los supermercados que tenemos seleccionados en preferencias
		// Ademas si tenemos una oferta en la lista de la compra solo nos mostrara las ofertas de 
		// ese supermercado
		for(Offer o: aux) {
			int superId = o.getSupermarket().getId();
			if( preferences.getBoolean(Integer.valueOf(superId).toString(), true) && isSupermarketCompatible(superId))
				offers.add(o);
		}
		return offers;
	}
	
	/**
	 * Se debe llamar a esta funcion cuando cambien las preferencias
	 * o se borre una oferta de la lista para que el filtrado se haga
	 * correctamente
	 */
	public void offerConditionsChanged() {
		conditionsChanged = true;
		System.out.println("Actualizar ofertas");
	}
	
	/**
	 * Funcion que nos ayuda a filtrar los productos, si la lista de la compra no
	 * tiene ofertas devuelve true si tenemos ese supermercado dentro del radio de busqueda,
	 * en caso contrario si hay ofertas y no son de este supermercado devuelve false
	 * @param supermarketId id del supermercado a comprobar
	 * @return Nos dice si una oferta de ese supermercado puede aparecer en el filtrado
	 */
	private boolean isSupermarketCompatible(int supermarketId) {
		ArrayList<Offer> offers = ShoppingList.instance().getOffers();
		
		if(offers.size() == 0) {
			ArrayList<Supermarket> supersInCircle = SupermarketManager.instance().getSupermarketsInCircle();
			for(Supermarket s: supersInCircle) {
				if(s.getId() == supermarketId)
					return true;
			}
		} else {
			for(Offer o: offers) {
				if(o.getSupermarket().getId() == supermarketId)
					return true;
			}
		}
		
		return false;
	}
	
	private static OfferManager manager;
	private ArrayList<Offer> offers;
	private boolean conditionsChanged; // true si han cambiado las preferencias
}
