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
import com.hipercompara.logic.Product;
import android.os.AsyncTask;


/**
 * Clase ParseJSONProduct que se utliza para obtener el JSON
 * de la parte del servidor web donde se almacenan nuestros productos
 * y lo guarda en una lista de la Clase Producto
 * @author Javier
 *
 *
 */

public class ParseJSONProduct extends AsyncTask<String, Integer, Boolean> {
	
	ArrayList<Product> product=null;
	
	public ParseJSONProduct() {
		
	}

/**
 * Funci�n que debes pasar como �nico par�metro una direccion HTTP
 * 
 * 
 */
	
	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(params[0]);
        product= new ArrayList<Product>();
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
	        	JSONObject json_data = JSA.getJSONObject(i);
	        	Product prod=new Product();
	        	prod.setId(json_data.getInt("id"));
	        	prod.setName(json_data.getString("name"));
	        	prod.setAveragePrice(Float.parseFloat(json_data.getString("av_price")));
	        	prod.setDescription(json_data.getString("description"));
	        	prod.setQuantity(Float.parseFloat(json_data.getString("units")));
				int brandId = json_data.getInt("brand_id");
				prod.setBrandId(brandId);
				int categoryId = json_data.getInt("category_id");
				prod.setCategoryId(categoryId);
				int unitTypeId = json_data.getInt("unit_type_id");
				prod.setUnitTypeId(unitTypeId);
	        	product.add(prod);
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
	public ArrayList<Product> getAllProducts(){
		return product;
	}
 


}

