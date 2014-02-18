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
import com.hipercompara.logic.Category;

public class ParseJSONCategory extends AsyncTask<String, Integer, Boolean> {
	ArrayList<Category> categories=null;
	
	public ParseJSONCategory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
				DefaultHttpClient httpClient = new DefaultHttpClient();
		        HttpGet httpGet = new HttpGet("http://hipercompara.shenko.me/json/category.php");
		        categories = new ArrayList<Category>();
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
			        	Category c = new Category();
			        	c.setId(jsonData.getInt("id"));
			        	c.setName(jsonData.getString("name"));
			        	c.setIconName(jsonData.getString("icon_name"));
			        	categories.add(c);
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
	
	public ArrayList<Category> getAllCategories(){
		return categories;
	}
	
}
