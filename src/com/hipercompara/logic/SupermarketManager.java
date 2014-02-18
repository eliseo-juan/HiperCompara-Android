package com.hipercompara.logic;

import java.util.ArrayList;
import java.util.Iterator;
import android.content.SharedPreferences;
import com.hipercompara.dao.ParseJSONSuperMarket;

/**
 * Clase SupermarketManager 
 * Nos devuelve la lista de supermercados existente y
 * la lista de supermercados que se encuentran a menos de cierta
 * distancia de una posicion dada.
 * Sigue el patron singleton
 * @author Luis Miguel
 *
 */
public class SupermarketManager {
	/**
	 * Constructor
	 */
	private SupermarketManager() {

	}

	/**
	 * Devuelve la unica instancia existente de la clase
	 * @return instancia de la clase
	 */
	public static SupermarketManager instance() {
		if(manager == null)
			manager = new SupermarketManager();
		return manager;
	}

	/**
	 * Obtiene todos los supermercados del servidor
	 * @return todos los supermercados
	 */
	public ArrayList<Supermarket> getSupermarkets() {
		if(supermarkets == null) {
			try {
				ParseJSONSuperMarket pjs = new ParseJSONSuperMarket();
				pjs.execute();
				pjs.get();
				supermarkets = pjs.getAllSupermarkets();
			} catch (Exception e) {
				System.out.println("Error al intentar cargar los supers");
				supermarkets = new ArrayList<Supermarket>();
			}
		}
		return supermarkets;
	}
	
	/**
	 * Obtiene los supermercados que se encuentran dentro de un circulo cuyo centro
	 * es nuestra posicion y cuyo radio esta en las preferencias
	 * @return supermercados cercanos
	 */
	public ArrayList<Supermarket> getSupermarketsInCircle() {
		if(supermarketsInCircle == null) {
			SharedPreferences preferences = SharedPreferencesManager.getNearbyMarketPreferences();
			float radius = preferences.getFloat("radius", 3f);
			// Si no hay posicion coge las coordenadas de Valencia
			float latitude = preferences.getFloat("latitude", AppConstants.latitudeValencia);
			float longitude = preferences.getFloat("longitude", AppConstants.longitudeValencia);
			getSupermermarketsFromMyPosition(latitude, longitude, radius*1000, AppConstants.earthRadius);
		}
		return supermarketsInCircle;
	}
	
	/**
	 * Funcion que devuelve una lista de supermercados que estan dentro del circulo de la muerte.
	 * @author vicdoz
	 * @param myLat mi latitud
	 * @param myLong mi longitud
	 * @param radius_in_KM radio de busqueda de los supermercados
	 * @param cte_radius_planet radio de la tierra
	 * @return Lista de supermercados
	 */
	public ArrayList<Supermarket> getSupermermarketsFromMyPosition(double myLat,double myLong,float radius_in_KM,float cte_radius_planet){
		ArrayList<Supermarket> newList=new ArrayList<Supermarket>();
		ArrayList<Supermarket> supers = getSupermarkets(); // Llama a este metodo por si no se habian obtenido
		Iterator<Supermarket> it = supers.iterator();
		while(it.hasNext()){
			Supermarket s = it.next(); 
			double d=getDistance(myLat,myLong,s.getLatitude(),s.getLongitude(),cte_radius_planet);
			if(d<=radius_in_KM) 
				newList.add(s);
		}
		supermarketsInCircle=newList;
		return newList;

	}

	/**
	 * @author vicdoz
	 * Calcula la distancia entre 2 pares de coordenadas cualquiera.
	 * @param lat_a latitud punto a
	 * @param lon_a longitud punto a
	 * @param lat_b latitud punto b
	 * @param lon_b longitud punto b
	 * @param cte_radius_planet radio de la tierra
	 * @return distancia entre dos coordenadas terrestres
	 */
	public static double getDistance(double lat_a,double lon_a, double lat_b, double lon_b,float cte_radius_planet){
		double xlat=Math.toRadians(lat_a);
		double ylat=Math.toRadians(lat_b);
		double xlon=Math.toRadians(lon_a);
		double ylon=Math.toRadians(lon_b);
		return  (cte_radius_planet * Math.acos( Math.cos( xlat ) * Math.cos( ylat) * Math.cos( ylon - xlon ) + Math.sin( xlat ) * Math.sin( ylat ) ))*1000;
	}


	private static SupermarketManager manager;
	ArrayList<Supermarket> supermarkets;
	ArrayList<Supermarket> supermarketsInCircle;

}
