package com.hipercompara.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.hipercompara.logic.Offer;
import com.hipercompara.logic.Product;
import com.hipercompara.logic.Supermarket;

public class ParseJSONOffer extends AsyncTask<Void, Integer, Boolean> {
	ArrayList<Offer> offers=null;
	HashMap<Integer,Supermarket> supermarketCache = null;

	public ParseJSONOffer() {
		supermarketCache = new HashMap<Integer, Supermarket>();
	}

	/**
	 * Funcion que no debes pasar  parametros de la que quieres obtener todos las Offers
	 * del servidor
	 */

	@Override
	protected Boolean doInBackground(Void... params) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://hipercompara.shenko.me/json/offer.php");
		offers= new ArrayList<Offer>();
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
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				for(int i=0;i<JSA.length();i++){
					JSONObject json_data = JSA.getJSONObject(i);
					Offer of=new Offer();
					Product p=getProductOfferFromServer(json_data.getInt("product_id"));
					int superMarketId = json_data.getInt("super_id");
					Supermarket s;
					if(supermarketCache.containsKey(Integer.valueOf(superMarketId))) {
						s = supermarketCache.get(Integer.valueOf(superMarketId));
						System.out.println("CACHE");
					} else {
						s = getSupermarketOfferFromServer(superMarketId);
						supermarketCache.put(Integer.valueOf(superMarketId),s);
					} 
					of.setDiscountPercentage(Float.parseFloat(json_data.getString("percentage")));
					of.setNewPrice(Float.parseFloat(json_data.getString("new_price")));
					of.setStartDate(df.parse(json_data.getString("start_date")));
					of.setEndDate(df.parse(json_data.getString("end_date")));
					of.setProduct(p);
					of.setSupermarket(s);
					offers.add(of);
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
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
	public ArrayList<Offer> getOffers(){
		return offers;
	}

	private Product getProductOfferFromServer(int id){
		Product p=new Product();
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://hipercompara.shenko.me/json/product.php?id="+id);
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
					p.setId(json_data.getInt("id"));
					p.setName(json_data.getString("name"));
					p.setAveragePrice(Float.parseFloat(json_data.getString("av_price")));
					p.setDescription(json_data.getString("description"));
					p.setQuantity(Float.parseFloat(json_data.getString("units")));
					int brandId = json_data.getInt("brand_id");
					p.setBrandId(brandId);
					int categoryId = json_data.getInt("category_id");
					p.setCategoryId(categoryId);
					int unitTypeId = json_data.getInt("unit_type_id");
					p.setUnitTypeId(unitTypeId);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return p;
	}

	private Supermarket getSupermarketOfferFromServer(int id){
		Supermarket s=new Supermarket();
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://hipercompara.shenko.me/json/super.php?id="+id);
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
					s.setId(json_data.getInt("id"));
					s.setName(json_data.getString("name"));
					s.setCountry(json_data.getString("country"));
					s.setCity(json_data.getString("city"));
					s.setPostalCode(json_data.getInt("postcode"));
					s.setAddress(json_data.getString("address"));
					s.setLatitude(Float.parseFloat(json_data.getString("latitude")));
					s.setLongitude(Float.parseFloat(json_data.getString("longitude")));
					s.setIconName(json_data.getString("icon_name"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return s;
	}

}
