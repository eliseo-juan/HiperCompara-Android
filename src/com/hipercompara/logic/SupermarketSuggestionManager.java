package com.hipercompara.logic;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Pair;
import com.hipercompara.dao.ParseJSONSuggestions;


/**
 * Clase SuggestionManager, esta clase debe de devolver los tres 
 * supermercados mas baratos para hacer la compra,
 * estos los devuelve en una clase llamada SupermarketSuggestion
 * 
 * La llamada al metodo computeCheapestSupermarkets es la que desencadena que se realicen
 * los calculos en el servidor y este devuelva los tres mejores supermercados empaquetados
 * en instancias de la clase SupermarketSuggestion que ademas contendra el precio total.
 * Tras la llamada al metodo anterior, llamar a getSuggestions para obtener las sugerencias
 * 
 * @author Luis Miguel
 * 
 * para calcular los supermercados mas baratos llamar 
 * SupermarketSuggestionManager.instance().computeCheapestSupermarkets();
 * SupermarketSuggestionManager.instance().getSuggestions();
 *
 */
public class SupermarketSuggestionManager {
	
	/**
	 * Constructor
	 */
	private SupermarketSuggestionManager() {
		suggestions = new ArrayList<SupermarketSuggestion>();
	}
	
	/**
	 * Devuelve la unica instancia de la clase
	 * @return instancia de la clase
	 */
	public static SupermarketSuggestionManager instance() {
		if(manager == null)
			manager = new SupermarketSuggestionManager();
		return manager;
	}
	
	/**
	 * Realiza la llamada a la capa de datos para que el servidor calcule los supermercados
	 * mas baratos
	 * @return si hay sugerencias o no
	 */
	@SuppressLint("UseSparseArrays")
	public boolean computeCheapestSupermarkets() {
		ArrayList<Product> products = ShoppingList.instance().getProducts();
		ArrayList<Offer> offers = ShoppingList.instance().getOffers();
		ArrayList<Integer> supermarketIds = new ArrayList<Integer>();
		ArrayList< Pair<Integer,Integer> >    offerIds = new ArrayList< Pair<Integer,Integer> >();
		HashMap<Integer,Integer> productIds = new HashMap<Integer,Integer>();
		
		// Obtiene los ids de los supermercados marcados a true en shared preferences
		ArrayList<Supermarket> supers = SupermarketManager.instance().getSupermarketsInCircle();
		SharedPreferences preferences = SharedPreferencesManager.getSharedPreferences();
		for(Supermarket sm: supers) {
			if(preferences.getBoolean(Integer.valueOf(sm.getId()).toString(), true))
				supermarketIds.add(sm.getId());
		}
		
		// Prepara los ids de las ofertas
		for(Offer of: offers) {
			offerIds.add(new Pair<Integer,Integer>(
					Integer.valueOf(of.getProduct().getId()),
					Integer.valueOf(of.getSupermarket().getId())));
		}
		
		// Prepara los ids de los productos junto con las veces que queremos ese producto
		for(Product p: products)
			productIds.put(Integer.valueOf(p.getId()), Integer.valueOf(p.getTimesInList()));

		// Llamada que reliza el calculo en el servidor
		ParseJSONSuggestions jsu = new ParseJSONSuggestions();
		suggestions = jsu.executeCalculation(productIds, supermarketIds, offerIds);
		Collections.sort(suggestions);
		
		
		// Elimina sugerencias si hay mas de 3 para evitar errores
		for(int i = suggestions.size() - 1; i > 2; --i)
			suggestions.remove(i);
		
		if(suggestions.size() > 0)
			return true;
		else 
			return false;
	}
	
	/**
	 * Obtiene las sugerencias de los supermercados mas baratos
	 * una vez hemos hecho el calculo
	 * @return sugerencias de supermercados
	 */
	public ArrayList<SupermarketSuggestion> getSuggestions() {
		return suggestions;
	}

	private ArrayList<SupermarketSuggestion> suggestions;
	private static SupermarketSuggestionManager manager; // instancia de la clase
}
