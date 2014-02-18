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

public class ParseJSONRecipeProduct {
	ArrayList<Product> product;

	Product getProduct(int id) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://hipercompara.shenko.me/json/product.php?id="+id);
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
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return product.get(0);
	}
}
