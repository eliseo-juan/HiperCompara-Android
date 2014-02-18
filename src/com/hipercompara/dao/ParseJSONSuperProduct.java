package com.hipercompara.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Point;
import android.os.AsyncTask;

public class ParseJSONSuperProduct extends AsyncTask<String, Integer, Boolean> {

	HashMap<Point, Float> superMap = null; 
	
	public ParseJSONSuperProduct () {
		
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
        HttpGet httpGet = new HttpGet(params[0]);
        superMap= new HashMap<Point, Float>();
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
	        	Point key = new Point(jsonData.getInt("super_id"),jsonData.getInt("product_id"));
	        	float val = Float.parseFloat(jsonData.getString("price"));
	        	superMap.put(key, val);
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
		return true;
	}
	
	/**
	 * Para que no de problemas, se debe ejecutar despues de que acabe
	 * doInBackground o execute (Depende lo que se use para lanzar la tarea)
	 * 
	 * 
	 */
	public HashMap<Point, Float> getAllSuperProducts(){
		return superMap;
	}
 


}
