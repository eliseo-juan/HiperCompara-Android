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
import com.hipercompara.logic.Brand;


public class ParseJSONBrand {
	ArrayList<Brand> brands=null;

	public ArrayList<Brand> getAllBrands(){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://hipercompara.shenko.me/json/brand.php");
		brands = new ArrayList<Brand>();
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
				Brand b = new Brand();
				b.setId(json_data.getInt("id"));
				b.setName(json_data.getString("name"));
				brands.add(b);
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

		return brands;
	}

}
