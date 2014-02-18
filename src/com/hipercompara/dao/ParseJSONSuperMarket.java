package com.hipercompara.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hipercompara.logic.Supermarket;

import android.os.AsyncTask;


/**
 * Clase ParseJSONSupermarket que se utliza para obtener el JSON
 * de la parte del servidor web donde se almacenan nuestros supermercados
 * y lo guarda en una lista de la Clase Supermarket
 * @author Javier
 *
 *
 */

public class ParseJSONSuperMarket extends AsyncTask<String, Integer, Boolean> {

	ArrayList<Supermarket> superArray=null;
	
	public ParseJSONSuperMarket () {
		
	}
	
	/**
	 * Funci�n que debes pasar como �nico par�metro una direccion HTTP
	 * @param params Debe ser un unico parametro de tipo String 
	 * con la direcci�n HTTP donde est� el JSON
	 * 
	 * 
	 */

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://hipercompara.shenko.me/json/super.php");
        superArray= new ArrayList<Supermarket>();
        HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
			is.close();
	        String json = sb.toString();
	        JSONArray JSA= new JSONArray(json);
	        for(int i=0;i<JSA.length();i++){
	        	JSONObject jsonData = JSA.getJSONObject(i);
	        	Supermarket sup=new Supermarket();
	        	sup.setId(jsonData.getInt("id"));
	        	sup.setAddress(jsonData.getString("address"));
	        	sup.setCity(jsonData.getString("city"));
	        	sup.setCountry(jsonData.getString("country"));
	        	sup.setName(jsonData.getString("name"));
	        	sup.setPostalCode(jsonData.getInt("postcode"));
	        	sup.setIconName(jsonData.getString("icon_name"));
	        	sup.setLatitude(jsonData.getDouble("latitude"));
	        	sup.setLongitude(jsonData.getDouble("longitude"));
	        	superArray.add(sup);
	        }
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
		return null;
	}
	
	/**
	 * Para que no de problemas, se debe ejecutar despues de que acabe
	 * doInBackground o execute (Depende lo que se use para lanzar la tarea)
	 * 
	 * 
	 */
	public ArrayList<Supermarket> getAllSupermarkets(){
		return superArray;
	}
 


}
