package com.hipercompara.logic;
import java.util.ArrayList;
import java.util.ListIterator;

import com.hipercompara.dao.HCdbManager;


/**
 * Clase ShoppingList, sera nuestra lista de la compra
 * en la que iremos agregando los productos que busquemos.
 * Esta clase sigue el patron singleton
 * @author Luis Miguel 
 */
public class ShoppingList {

	/**
	 * Constructor
	 */
	private ShoppingList() {
		super();
	}

	/**
	 * Devuelve la unica instancia de clase
	 * @return instancia de la clase
	 */
	public static ShoppingList instance() {
		if(shoppingList == null)
			shoppingList = new ShoppingList();
		return shoppingList;
	}

	/**
	 * Obtiene todos los prodcutos que hemos puesto en la lista,
	 * si hemos puesto ofertas estas apareceran primero, las ofertas 
	 * en principio aparecen como productos normales, de hecho con esta
	 * funcion no hay forma de distinguirlas, solo  se distinguen a
	 * nivel de la logica
	 * @return lista de ofertas y productos
	 */
	public ArrayList<Product> getProducts() {
		if(products == null)
			products = HCdbManager.getDb().getAllProductsList();

		//TODO Falta comprobar que la fecha de la oferta no haya pasado
		// Si ha caducado se debe borrar la oferta y meter el producto
		// normal a la base de datos
		if(offers == null)
			offers = HCdbManager.getDb().getAllOffers();

		ArrayList<Product> aux = new ArrayList<Product>();
		for(Offer o: offers)
			aux.add(o.getProduct());
		for(Product p: products)
			aux.add(p);

		return aux;
	}

	/**
	 * Obtiene las ofertas de la lista, este metodo solo se 
	 * necesita para enviar correctamente los datos al servidor
	 * cuando calcula el precio.
	 * @return lista de ofertas
	 */
	public ArrayList<Offer> getOffers() {
		if(offers == null)
			offers = HCdbManager.getDb().getAllOffers();
		return offers;
	}

	/**
	 * Pone un producto en la lista si no esta ya puesto
	 * como producto o como oferta, si ya esta incrementa
	 * en uno el numero de veces que queremos ese producto
	 * @param p producto
	 */
	public void addProduct(Product p) {
		if(getTimesInList(p.getId()) == 0) {
			HCdbManager.getDb().addProductList(p);
			products.add(p);
		} else {
			updateTimesInListProduct(p.getId(), getTimesInList(p.getId()) + 1);
		}
	}

	/**
	 * Pone una oferta en la lista si no esta puesta ya 
	 * como producto u oferta, si ya esta aumenta en uno 
	 * el numero de veces que queremos ese producto
	 * @param o oferta
	 */
	public void addOffer(Offer o) {
		if(getTimesInList(o.getProduct().getId()) == 0) {
			HCdbManager.getDb().addOffer(o);
			offers.add(o);
		} else {
			updateTimesInListProduct(o.getProduct().getId(), getTimesInList(o.getProduct().getId()) + 1);
		}
	}

	/**
	 * Actualiza el numero de veces que aparece un producto en la
	 * lista, sirve tanto para las ofertas como para los productos 
	 * normales
	 * @param id identificador del producto o producto asociado a la oferta
	 * @param times cantidad de unidades que queremos de ese producto
	 * */
	public void updateTimesInListProduct(int id, int times) {
		HCdbManager.getDb().updateTimesInList(id, times);

		ListIterator<Offer> ito = offers.listIterator();
		while(ito.hasNext()) {
			Offer o = ito.next();
			Product p = o.getProduct();
			if(p.getId() == id) {
				p.setTimesInList(times);
				o.setProduct(p);
				ito.set(o);
				return;
			}
		}

		ListIterator<Product> it = products.listIterator();
		while(it.hasNext()) {
			Product p = it.next();
			if(p.getId() == id) {
				p.setTimesInList(times);
				it.set(p);
			}
		}
	}

	/**
	 * Elimina un producto de la lista de la compra, sirve tanto
	 * para borrar una oferta como para borrar un producto normal
	 * @param id identificador del producto a eliminar
	 */
	public void removeProductById(int id) {
		// Busca el id en las ofertas y si esta en ofertas lo borra y sale
		int position = -1;
		int current = 0;
		System.out.println("BORRANDO");
		for(Offer o: offers) {
			if(o.getProduct().getId() == id) {
				HCdbManager.getDb().deleteOfferbyIds(id, o.getSupermarket().getId(), true, false);
				position = current;
				break;
			}
			++current;
		}
		if( position >= 0) {
			offers.remove(position);
			OfferManager.instance().offerConditionsChanged();
			return;
		}

		// Busca el id en los productos normales y lo borra si esta ahi
		position = -1;
		current = 0;
		for(Product p: products) {
			if(p.getId() == id) {
				HCdbManager.getDb().deleteProductFromList(id);
				position = current;
				break;
			}
			++current;
		}
		if(position >= 0) {
			products.remove(position);
			OfferManager.instance().offerConditionsChanged();
		}
	}

	/**
	 * Elimina todos los productos y ofertas de la lista de la compra
	 * siempre que no esten vacias
	 */
	public void removeAllProducts() {
		if(products.size() > 0 || offers.size() > 0) {
			HCdbManager.getDb().deleteAllList();
			HCdbManager.getDb().deleteAllOffers();
			products.clear();
			offers.clear();
			OfferManager.instance().offerConditionsChanged();
		}
	}


	/**
	 * Nos dice el numero de veces que aparece un producto en la lista o
	 * 0 si no esta
	 * @param id identificador del producto 
	 * @return numero de veces que queremos ese producto o 0 si no esta
	 */
	private int getTimesInList(int id) {
		for(Product p: products) {
			if(p.getId() == id)
				return p.getTimesInList();
		}

		for(Offer o: offers) {
			if(o.getProduct().getId() == id)
				return o.getProduct().getTimesInList();
		}

		return 0;
	}

	private ArrayList<Product> products;  // Productos que vamos poniendo en la lista
	private ArrayList<Offer> offers;      // Ofertas añadidas a la lista de la compra
	private static ShoppingList shoppingList;     // Unica instancia de la clase
}
