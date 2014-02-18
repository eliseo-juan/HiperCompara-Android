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
import android.os.AsyncTask;
import com.hipercompara.logic.Product;



public class ParseJSONProductByName extends AsyncTask<String, Integer, Boolean> {
ArrayList<Product> product=null;
	
	public ParseJSONProductByName() {
		
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
		String Url=replaceSpacesByUnderscores(params[0]);
        HttpGet httpGet = new HttpGet("http://hipercompara.shenko.me/json/products.php?name="+Url);
        product= new ArrayList<Product>();
        HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			int cont=0;
			while ((line = reader.readLine()) != null) {
				if (line.equals("null")&&cont==0){
	            	sb=null;
	            }
	            else sb.append(line + "\n");
				cont++;
	        }
			is.close();
			if(sb!=null){
				String json = sb.toString();
				JSONArray JSA= new JSONArray(json);
				for(int i=0;i<JSA.length();i++){
					JSONObject json_data = JSA.getJSONObject(i);
					Product prod=new Product();
					prod.setId(json_data.getInt("id"));
					prod.setName(json_data.getString("name"));
					prod.setAveragePrice(0);
					prod.setDescription(null);
					prod.setQuantity(0);
					int brandId = json_data.getInt("brand_id");
					prod.setBrandId(brandId);
					int categoryId = json_data.getInt("category_id");
					prod.setCategoryId(categoryId);
					int unitTypeId = json_data.getInt("unit_type_id");
					prod.setUnitTypeId(unitTypeId);
					float avPrice = (float)json_data.getDouble("av_price");
					prod.setAveragePrice(avPrice);
					product.add(prod);
				}
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
		}/* catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    */  
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
	
	private String replaceSpacesByUnderscores(String string){
		String answer="";
		for (int x=0; x < string.length(); x++) {
			  if (string.charAt(x) != ' ') answer += string.charAt(x);
			  else answer+="_";
		}
		return answer;
	}
 
}
