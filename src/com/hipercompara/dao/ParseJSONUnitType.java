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
import com.hipercompara.logic.UnitType;

public class ParseJSONUnitType {
	ArrayList<UnitType> unitTypes=null;

	public ArrayList<UnitType> getAllUnitTypes(){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://hipercompara.shenko.me/json/unit_type.php");
		unitTypes= new ArrayList<UnitType>();
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
				UnitType type = new UnitType();
				type.setId(json_data.getInt("id"));
				type.setName(json_data.getString("name"));
				unitTypes.add(type);
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
		return unitTypes;
	}

}


