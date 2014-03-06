package com.mofiler;

import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.media.control.MetaDataControl;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mofiler.api.ApiListener;
import com.mofiler.api.RESTApi;

//this class will hold the actual client / server logic, at the uppermost level, and will hold
//data in persistence until it deserves to be pushed to the server, in chunks, in a single push.
public class MofilerClient implements ApiListener {

	private RESTApi restApi;
	private JSONArray jsonUserValues;
	private static int K_MOFILER_STACK_LENGTH = 10;
	private MofilerDeferredObject deferredObj;
	private boolean bUseDeferredSend = false;
	private ApiListener listener = null;

	public MofilerClient(boolean a_bUseDeferredSend) {
		restApi = new RESTApi();
		restApi.addMethodListener(RESTApi.K_MOFILER_API_METHOD_NAME_inject, this);
		restApi.addMethodListener(RESTApi.K_MOFILER_API_METHOD_NAME_get, this);
		this.bUseDeferredSend = a_bUseDeferredSend;
	}
	
	public void addHeaderKeyValue(String header, String value)
	{
		restApi.addPropertyKeyValuePair(header, value);		
	}
	
	private void internal_populateVector(String key, String value) throws JSONException
	{
		JSONObject tmpObj = new JSONObject();
		tmpObj.put(key, value);
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
			if (jsonUserValues.length() < K_MOFILER_STACK_LENGTH){
				internal_populateVector(key, value);
			}
			else
			{
				//send stack data and then push the new data into the stack
				deferredObj = new MofilerDeferredObject(key, value);
				restApi.pushKeyValueStack(jsonUserValues);
			}
		}
		catch(JSONException ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void pushValue_array(String key, String value, long expireAfterMs) {
		try{
			if (jsonUserValues.length() < K_MOFILER_STACK_LENGTH){
				internal_populateVector(key, value);
			}
			else
			{
				//send stack data and then push the new data into the stack
				deferredObj = new MofilerDeferredObject(key, value, expireAfterMs);
				restApi.pushKeyValueStack(jsonUserValues);
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
        System.err.println("methodResponded " + a_methodCalled);

        if (a_vectBusinessObject.size() > 1)
        {
            String methodname = (String)a_vectBusinessObject.elementAt(0);
            if (a_methodCalled.startsWith("error"))
            {
                JSONObject jsonErr = (JSONObject)a_vectBusinessObject.elementAt(1);

                String strOriginalMethod = restApi.getMethodForError(a_methodCalled);
                
                if (strOriginalMethod.startsWith(RESTApi.K_MOFILER_API_METHOD_NAME_inject))
                {
                	//TODO: in case deferred sending is enabled, then if an error happened, try resending stack later
                }
                else
                if (strOriginalMethod.startsWith(RESTApi.K_MOFILER_API_METHOD_NAME_get))
                {
                    //TODO: get the value we asked for, and also 
                }
                else
                {
                    methodResponded_ErrorHandler(strOriginalMethod, jsonErr);

                } /* end if */

            } /* end if */
            else
            if (a_methodCalled.startsWith(RESTApi.K_MOFILER_API_METHOD_NAME_inject))
            {
            	System.err.println("EN MOFILER CLIENT, A LLAMAR A LISTENER INJECT!");
            	
        		if (listener != null)
        			listener.methodResponded(a_methodCalled, a_vectBusinessObject);

            }
            else
        	if (a_methodCalled.startsWith(RESTApi.K_MOFILER_API_METHOD_NAME_get))
        	{
            	System.err.println("EN MOFILER CLIENT, A LLAMAR A LISTENER GET!");
        		if (listener != null)
        			listener.methodResponded(a_methodCalled, a_vectBusinessObject);
        	} /* end if */
        } 

    }   
}
