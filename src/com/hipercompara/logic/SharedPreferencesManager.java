package com.hipercompara.logic;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Clase que nos permite acceder a las preferencias de la aplicacion desde
 * cualquier parte de la aplicacion sin tener que pasar el contexto
 * @author Luis Miguel
 */
public class SharedPreferencesManager {
	
	/**
	 * Obtiene las preferencias por defecto
	 * @return preferencias por defecto
	 */
	public static SharedPreferences getDefaultPreferences() {
		return context.getSharedPreferences("default", Context.MODE_PRIVATE);
	}
	
	/**
	 * Obtiene las preferencias de los supermercados, es decir las preferencias 
	 * de los supermercados a los que queremos ir
	 * @return preferencias de los supermercados
	 */
	public static SharedPreferences getSharedPreferences() {
		return context.getSharedPreferences("prefs_supermarket",Context.MODE_PRIVATE);
	}
	
	/**
	 * Obtiene las preferencias del activity NearbyMarket, radio de busqueda
	 * y ultima posicion
	 * @return preferencias de NearbyMarket
	 */
	public static SharedPreferences getNearbyMarketPreferences() {
		return context.getSharedPreferences("prefs_nearby_market",Context.MODE_PRIVATE);
	}
	
	/**
	 * Inicializa el contexto, esto es importante hacerlo al principio de la ejecucion
	 * de la aplicacion para que esta clase conozca el contexto y se lo pueda pasar
	 * a las llamadas que obtienen las preferencias
	 * @param context contexto de la aplicacion
	 */
	public static void setContext(Context context) {
		SharedPreferencesManager.context = context;
	}
	
	public static Context context; // Contexto de la aplicacion
}
