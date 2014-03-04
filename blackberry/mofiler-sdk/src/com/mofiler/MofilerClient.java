package com.mofiler;

import java.util.Hashtable;

import org.json.me.JSONException;

import com.mofiler.api.RESTApi;

//this class will hold the actual client / server logic, at the uppermost level, and will hold
//data in persistence until it deserves to be pushed to the server, in chunks, in a single push.
public class MofilerClient {

	private RESTApi restApi;

	public MofilerClient() {
		restApi = new RESTApi();
	}
	
	public void addHeaderKeyValue(String header, String value)
	{
		restApi.addPropertyKeyValuePair(header, value);		
	}
	
	public void pushValue(String key, String value){
		try{
			restApi.pushKeyValue(key, value);
		}
		catch(JSONException ex)
		{
			ex.printStackTrace();
		}
	}

	public void pushValue(String key, String value, long expireAfterMs){
		try{
			restApi.pushKeyValue(key, value, expireAfterMs);
		}
		catch(JSONException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void setIdentity(Hashtable hashIds){
		restApi.setIdentity(hashIds);
	}
}
