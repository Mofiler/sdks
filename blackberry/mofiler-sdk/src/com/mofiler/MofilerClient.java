package com.mofiler;

import java.util.Hashtable;
import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mofiler.api.ApiListener;
import com.mofiler.api.RESTApi;
import com.sun.lwuit.io.util.Util;

//this class will hold the actual client / server logic, at the uppermost level, and will hold
//data in persistence until it deserves to be pushed to the server, in chunks, in a single push.
public class MofilerClient implements ApiListener {

	private RESTApi restApi;
	private JSONArray jsonUserValues;
	private MofilerValueStack mofilerValues;
	private static int K_MOFILER_STACK_LENGTH = 10;
	private static int K_MOFILER_MAX_STACK_LENGTH = 1000; //if we reach this, just eliminate all data to avoid bloat
	//private MofilerDeferredObject deferredObj;
	private boolean bUseDeferredSend = false;
	private ApiListener listener = null;
	private String strURL;

	public MofilerClient(boolean a_bUseDeferredSend) {
		restApi = new RESTApi();
		restApi.useThreadedConnections(true, false);
		restApi.addMethodListener(RESTApi.K_MOFILER_API_METHOD_NAME_inject, this);
		restApi.addMethodListener(RESTApi.K_MOFILER_API_METHOD_NAME_get, this);
		this.bUseDeferredSend = a_bUseDeferredSend;
	
		//initialization for LWUIT IO
        com.sun.lwuit.io.Storage.init("mofiler");
        Util.register("MofilerValueStack", MofilerValueStack.class);
        
        loadDataFromStorage();        
	}
	
	
	public void addHeaderKeyValue(String header, String value)
	{
		restApi.addPropertyKeyValuePair(header, value);		
	}
	
	private JSONArray discardKeyValueIfExistsInArray(JSONArray a_jsonArrToLookIn, String key, String value)
	{
		JSONArray newJsonArray = new JSONArray();
		
		try {
			for (int i=0; i < a_jsonArrToLookIn.length(); i++){
				JSONObject tmpObj = a_jsonArrToLookIn.getJSONObject(i);
				if (tmpObj.has(key)){
					//discard this object from array and put the new key value instead
				} else {
					newJsonArray.put(tmpObj);
				}
			}
		} catch (JSONException ex){
			ex.printStackTrace();
		}
		
		
		return newJsonArray;
	}
	
	private void internal_populateVector(String key, String value) throws JSONException
	{
		jsonUserValues = discardKeyValueIfExistsInArray(jsonUserValues, key, value);
		JSONObject tmpObj = new JSONObject();
		tmpObj.put(key, value);
		tmpObj.put(RESTApi.K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());
		jsonUserValues.put(tmpObj);
	}

	private void internal_populateVector(String key, String value, long expireAfterMs) throws JSONException
	{
		jsonUserValues = discardKeyValueIfExistsInArray(jsonUserValues, key, value);
		JSONObject tmpObj = new JSONObject();
		tmpObj.put(key, value);
		tmpObj.put("expireAfter", expireAfterMs);
		tmpObj.put(RESTApi.K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());
		jsonUserValues.put(tmpObj);
	}
	
	public void pushValue(String key, String value){
		
		if (!bUseDeferredSend){
			pushValue_single(key, value);
		}
		else
		{
			pushValue_array(key, value);
		}
	}

	private void pushValue_array(String key, String value) {
		try{
			if (((jsonUserValues.length() % K_MOFILER_STACK_LENGTH) != 0) || (jsonUserValues.length() == 0)){
				internal_populateVector(key, value);
			}
			else if(jsonUserValues.length() > K_MOFILER_MAX_STACK_LENGTH){
				//send this and clean all
				restApi.pushKeyValueStack(jsonUserValues);
				jsonUserValues = new JSONArray();
				mofilerValues.setJsonStack(jsonUserValues);
				internal_populateVector(key, value);
				doSaveDataToDisk();
			}
			else
			{
				//send stack data and then push the new data into the stack
				//deferredObj = new MofilerDeferredObject(key, value);
				restApi.pushKeyValueStack(jsonUserValues);
				doSaveDataToDisk();
				jsonUserValues = new JSONArray();
				internal_populateVector(key, value);
			}
		}
		catch(JSONException ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void pushValue_array(String key, String value, long expireAfterMs) {
		try{
			if (((jsonUserValues.length() % K_MOFILER_STACK_LENGTH) != 0) || (jsonUserValues.length() == 0)){
				internal_populateVector(key, value, expireAfterMs);
			}
			else if(jsonUserValues.length() > K_MOFILER_MAX_STACK_LENGTH){
				//clean all
				restApi.pushKeyValueStack(jsonUserValues);
				jsonUserValues = new JSONArray();
				mofilerValues.setJsonStack(jsonUserValues);
				internal_populateVector(key, value, expireAfterMs);
				doSaveDataToDisk();
			}
			else
			{
				//send stack data and then push the new data into the stack
				//deferredObj = new MofilerDeferredObject(key, value, expireAfterMs);
				restApi.pushKeyValueStack(jsonUserValues);
				doSaveDataToDisk();
				jsonUserValues = new JSONArray();
				internal_populateVector(key, value, expireAfterMs);
			}
		}
		catch(JSONException ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	private void pushValue_single(String key, String value){
		try{
			restApi.pushKeyValue(key, value);
		}
		catch(JSONException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void pushValue(String key, String value, long expireAfterMs){
		
		if (!bUseDeferredSend){
			pushValue_single(key, value, expireAfterMs);
		}
		else
		{
			pushValue_array(key, value, expireAfterMs);
		}
	}

	private void pushValue_single(String key, String value, long expireAfterMs){
		try{
			restApi.pushKeyValue(key, value, expireAfterMs);
		}
		catch(JSONException ex)
		{
			ex.printStackTrace();
		}
	}

	public void getValue(String key, String identityKey, String identityValue){
		
		try{
			restApi.getValue(key, identityKey, identityValue);
		}
		catch(JSONException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void setIdentity(Hashtable hashIds){
		restApi.setIdentity(hashIds);
	}

	public void setListener(ApiListener a_listener){
		this.listener = a_listener;
	}
	
	public String getURL() {
		this.strURL = this.restApi.getServerURL();
		return this.strURL;
	}

	public void setURL(String a_URL) {
		this.strURL = a_URL;
		this.restApi.setServerURL(a_URL);
	}
	
	public void flushData(){
		try {
			restApi.pushKeyValueStack(jsonUserValues);
		} catch (JSONException ex){
			ex.printStackTrace();
		}
	}
	
    private int methodResponded_ErrorHandler(String a_methodCalled, final JSONObject a_berr)
    {
        int retCode = 0;
        //System.err.println("AN ERROR OCCURED: " + a_methodCalled);
        //System.err.println("AN ERROR OCCURED: " + a_methodCalled);
        //System.err.println("AN ERROR OCCURED: " + a_methodCalled);
        //System.err.println("AN ERROR OCCURED: " + a_methodCalled);
        //System.err.println("AN ERROR OCCURED: " + a_methodCalled);

        return retCode;
    }
    
    public void methodResponded(String a_methodCalled, Vector a_vectBusinessObject)
    {
        if (a_vectBusinessObject.size() > 1)
        {
            String methodname = (String)a_vectBusinessObject.elementAt(0);
            if (a_methodCalled.startsWith("error"))
            {
                JSONObject jsonErr = (JSONObject)a_vectBusinessObject.elementAt(1);

                String strOriginalMethod = restApi.getMethodForError(a_methodCalled);
                
                if (listener != null)
        			listener.methodResponded(strOriginalMethod, a_vectBusinessObject);
        		
        		if (bUseDeferredSend){
                	//TODO: in case deferred sending is enabled, then if an error happened, try resending stack later
        			try {
        				JSONArray jsonTmpArray = (JSONArray) new JSONArray((String)(a_vectBusinessObject.elementAt(2)));
        				if (jsonTmpArray.length() > K_MOFILER_MAX_STACK_LENGTH){
        						//clean all
        						jsonUserValues = new JSONArray();
        						mofilerValues.setJsonStack(jsonUserValues);
        						doSaveDataToDisk();
        				} else {
            				jsonUserValues = concatArray(jsonUserValues, jsonTmpArray);
        				}
            			
        			} catch(Exception ex){
        				ex.printStackTrace();
        			}
        		}
        		
//                if (strOriginalMethod.startsWith(RESTApi.K_MOFILER_API_METHOD_NAME_inject))
//                {
//                	//TODO: in case deferred sending is enabled, then if an error happened, try resending stack later
//            		if (listener != null)
//            			listener.methodResponded(a_methodCalled, a_vectBusinessObject);
//                }
//                else
//                if (strOriginalMethod.startsWith(RESTApi.K_MOFILER_API_METHOD_NAME_get))
//                {
//                    //TODO: get the value we asked for, and also 
//            		if (listener != null)
//            			listener.methodResponded(a_methodCalled, a_vectBusinessObject);
//                }
//                else
//                {
//                    methodResponded_ErrorHandler(strOriginalMethod, jsonErr);
//
//                } /* end if */

            } /* end if */
            else
            if (a_methodCalled.startsWith(RESTApi.K_MOFILER_API_METHOD_NAME_inject))
            {
        		if (bUseDeferredSend){
        			//all went good, so erase current value stack
					jsonUserValues = new JSONArray();
					mofilerValues.setJsonStack(jsonUserValues);
					doSaveDataToDisk();
        		}
            	
        		if (listener != null)
        			listener.methodResponded(a_methodCalled, a_vectBusinessObject);

            }
            else
        	if (a_methodCalled.startsWith(RESTApi.K_MOFILER_API_METHOD_NAME_get))
        	{
        		if (listener != null)
        			listener.methodResponded(a_methodCalled, a_vectBusinessObject);
        	} /* end if */
        } 

    }
    
    
    
    synchronized public void loadDataFromStorage()
    {
        try
        {
        	mofilerValues = (MofilerValueStack) com.sun.lwuit.io.Storage.getInstance().readObject("vectvaluestack");
        } catch (Exception ex)
        {
            System.err.println("EXCEPCION reading DB: " + ex.getMessage());
        }

        if (mofilerValues == null){
        	jsonUserValues = new JSONArray();
        	mofilerValues = new MofilerValueStack(null);
        } else {
        	try {
            	jsonUserValues = mofilerValues.getJsonStack();
        	} catch(Exception ex){
        		ex.printStackTrace();
            	jsonUserValues = new JSONArray();
            	mofilerValues = new MofilerValueStack(null);
        	}
        }
        
    }

    synchronized public void doSaveDataToDisk()
    {
    	mofilerValues.setJsonStack(jsonUserValues);
        com.sun.lwuit.io.Storage.getInstance().writeObject("vectvaluestack", mofilerValues);
        com.sun.lwuit.io.Storage.getInstance().flushStorageCache();
    }
    
    
    private JSONArray concatArray(JSONArray arr1, JSONArray arr2)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (int i = 0; i < arr1.length(); i++) {
            result.put(arr1.get(i));
        }
        for (int i = 0; i < arr2.length(); i++) {
            result.put(arr2.get(i));
        }
        return result;
    }    
}
