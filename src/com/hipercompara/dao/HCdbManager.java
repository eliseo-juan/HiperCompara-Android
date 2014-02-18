package com.hipercompara.dao;

import com.hipercompara.logic.SharedPreferencesManager;

import android.content.Context;

/**
 * Clase que nos devuelve una instancia de HCdb,
 * simplemente mantiene el contexto de la aplicacion 
 * para que se pueda acceder a la base de datos desde
 * cualquier parte de la logica
 * @author Luis Miguel
 *
 */
public class HCdbManager {
	
	/**
	 * Devuelve una instancia de la clase HCdb
	 * @return instancia de HCdb
	 */
	public static HCdb getDb() {
		return new HCdb(context);
	}
	
	/**
	 * Nos dice si la base de datos de la aplicación
	 * se ha creado, esto se usa la primera vez que se inicia la 
	 * aplicacion para para bajar los datos necesarios del servidor
	 * @return si se ha creado la base de datos
	 */
	public static boolean isDbCreated() {
		return SharedPreferencesManager.getDefaultPreferences().getBoolean("dbCreated", false);
	}

	/**
	 * Inicializa el campo contexto, esto es necesario realizarlo cuando se inicia 
	 * la aplicacion para que todas las clases que usan la base de datos puedan
	 * obtenerla a traves de esta clase
	 * @param context contexto de la aplicacion
	 */
	public static void setContext(Context context) {
		HCdbManager.context = context;
	}

	private static Context context;
}
