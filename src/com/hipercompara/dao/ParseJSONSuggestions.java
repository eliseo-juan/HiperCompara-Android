package com.hipercompara.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.util.Pair;

import com.hipercompara.logic.Supermarket;
import com.hipercompara.logic.SupermarketSuggestion;
/**
 * Se obtienen los calculos de las sugerencias en el servidor, de manera que si offerIds contiene alguna oferta
 * lee el php especifico para ello, de otra manera lee el otro especifico para sugerir supermercados
 * @author Javier
 *
 */
public class ParseJSONSuggestions {
	public ArrayList<SupermarketSuggestion> executeCalculation(HashMap<Integer,Integer> listIdsWithTimesInList, ArrayList<Integer> supermarketIds, ArrayList<Pair<Integer,Integer>> offerIds) {
		ArrayList<SupermarketSuggestion> suggestions = new ArrayList<SupermarketSuggestion>();
		if(listIdsWithTimesInList.size()<=0) 
			return suggestions;
		DefaultHttpClient httpClient = new DefaultHttpClient();
        String URL = "";
        if (offerIds.size()>0){
        	URL="http://hipercompara.shenko.me/js/totalpricewithoffers.php?ids=";
        	Iterator<Entry<Integer, Integer>> it = listIdsWithTimesInList.entrySet().iterator();
            while (it.hasNext()) {
            	Map.Entry<Integer, Integer> e = (Map.Entry <Integer, Integer>)it.next();
            	for(int i=0;i<e.getValue();i++){
            		URL=URL+e.getKey();
                	if(i<e.getValue()-1) URL=URL+"+";
            	}
            	if (it.hasNext()) URL=URL+"+";
            }
            URL=URL+"&offers=";
            for(int i=0;i<offerIds.size();i++){
            	URL=URL+offerIds.get(i).first;
            	if(i<offerIds.size()-1) URL=URL+"+";
            }
            URL=URL+"&super="+offerIds.get(0).second;
        }
        else{
        	URL="http://hipercompara.shenko.me/js/totalpricewithsupers.php?ids=";
        	Iterator<Entry<Integer, Integer>> it = listIdsWithTimesInList.entrySet().iterator();
            while (it.hasNext()) {
            	Map.Entry<Integer, Integer> e = (Map.Entry <Integer, Integer>)it.next();
            	for(int i=0;i<e.getValue();i++){
            		URL=URL+e.getKey();
                	if(i<e.getValue()-1) URL=URL+"+";
            	}
            	if (it.hasNext()) URL=URL+"+";
            }
            URL=URL+"&supers=";
            for(int i=0;i<supermarketIds.size();i++){
            	URL=URL+supermarketIds.get(i);
            	if(i<supermarketIds.size()-1) URL=URL+"+";
            }
        } 
		HttpGet httpGet = new HttpGet(URL);
        Log.i("List", URL);
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
	        ArrayList<JSONObject> jsonData = new ArrayList<JSONObject>();
	        for(int i=0;i<JSA.length();i++){
	        	jsonData.add(JSA.getJSONObject(i));
	        }
	        if (Float.parseFloat(jsonData.get(0).getString("0"))<0) return new ArrayList<SupermarketSuggestion>();
	        for (int i=0;i<jsonData.get(0).length();i++){
	        	Supermarket sup= new Supermarket();
		        SupermarketSuggestion sug= new SupermarketSuggestion();
		        if(Float.parseFloat(jsonData.get(0).getString(""+i))>0){
		        	sug.setTotalPrice(Float.parseFloat(jsonData.get(0).getString(""+i)));
		        	sup.setName(jsonData.get(1).getString(""+i));
		        	sup.setLatitude(Double.parseDouble(jsonData.get(2).getString(""+i)));
		        	sup.setLongitude(Double.parseDouble(jsonData.get(3).getString(""+i)));
		        	sug.setSuperMarket(sup);
		        	suggestions.add(sug);
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
		}
		return suggestions;
	}
}
