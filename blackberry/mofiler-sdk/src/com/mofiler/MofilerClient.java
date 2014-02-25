package com.mofiler;

import java.io.IOException;

import org.json.me.JSONException;

import com.mofiler.api.RESTApi;


public final class MofilerClient{
	static private MofilerClient instance = null;
	static private String getUrl = "http://";
	private HttpClient conection;
	private RESTApi restApi;

	private MofilerClient() {
		conection = new HttpClient();
		restApi = new RESTApi();
	}
	
	static public MofilerClient getInstance() {
		if(instance == null)
			instance = new MofilerClient();
		return instance;
	}

	public String get(String name) throws IOException{
		String c = getUrl + name;
		return conection.get(c);
	}
	
	public boolean put(String url){
		boolean res = false;
		try {
			res = conection.postViaHttpConnection(url);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}
}
