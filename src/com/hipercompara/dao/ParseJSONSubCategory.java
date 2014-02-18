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
import com.hipercompara.logic.SubCategory;

public class ParseJSONSubCategory {
	ArrayList<SubCategory> subcategories=null;

	public ArrayList<SubCategory> getAllSubCategories(){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://hipercompara.shenko.me/json/subcategory.php");
		subcategories = new ArrayList<SubCategory>();
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
				SubCategory sc = new SubCategory();
				sc.setId(jsonData.getInt("id"));
				sc.setName(jsonData.getString("name"));
				sc.setCategoryId(jsonData.getInt("category_id"));
				subcategories.add(sc);
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

		return subcategories;
	}


}
