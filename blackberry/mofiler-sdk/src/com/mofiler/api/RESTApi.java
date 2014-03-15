package com.mofiler.api;
/*
 * @(#)RESTApi	1.0 24-02-14

    Copyright (C) 2006-2014  Mario Zorz email me at mariozorz at gmail dot com
    The Prosciutto Project website: http://www.prosciuttoproject.org
    The Prosciutto Project blog:   http://prosciutto.boutiquestartups.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mofiler.device.MO_Device;
import com.mofiler.exception.IdentityNotSetException;
import com.mofiler.util.URLEncoder;


public class RESTApi implements FetcherListener
{
    /* class members here */
    private Fetcher connFetcher;

    //public static final String K_MOFILER_API_URL_BASE_POSTFIX                    = "www.mofiler.net/";
    //public static final String K_MOFILER_API_URL_BASE_POSTFIX                    = "localhost:3000";
    //public String K_MOFILER_API_URL_BASE_POSTFIX                    = "localhost:3000";
    public String K_MOFILER_API_URL_BASE_POSTFIX                    = null;
    //public static final String K_MOFILER_API_URL_BASE                    = "http://" + K_MOFILER_API_URL_BASE_POSTFIX;
    public String K_MOFILER_API_URL_BASE                    = "http://" + K_MOFILER_API_URL_BASE_POSTFIX;
    public static final String K_MOFILER_API_URL_METHOD_inject           = "/api/values/";
    public static final String K_MOFILER_API_URL_METHOD_get           = "/api/values/";

    public static final String K_MOFILER_API_METHOD_NAME_inject           = "POST_VALUES"; //arbitrary name
    public static final String K_MOFILER_API_METHOD_NAME_get           = "GET_VALUES";
    
    public static final String K_MOFILER_API_HEADER_X_SESSION_ID          = "X-Mofiler-SessionID";
    public static final String K_MOFILER_API_TIMESTAMP_KEY          = "tstamp";
    
    private String strCurrentMethodName = null;
    
    static private Hashtable msgListeners;

    private boolean bThreadedConnections = false;
    private boolean bUseBlockingThreads = true;
    
    private Hashtable hashApplicationHeaders = null;


    public RESTApi()
    {
        //generic constructor
        connFetcher = new Fetcher();
        
		Random rndNbr = new Random();
		connFetcher.setSessionID(rndNbr.nextLong());
        strCurrentMethodName = null;
    }

    static public void addMethodListener(String a_strMethodToListenTo, ApiListener listener)
    {
        if (msgListeners == null)
        {
            msgListeners = new Hashtable();
        }
        addMethodListener(a_strMethodToListenTo, listener, msgListeners);
    }

    static public void unregisterMethodListener(String a_strMethodToListenTo, ApiListener listener)
    {
        if (msgListeners != null)
        {
            Vector vec = (Vector) msgListeners.get(a_strMethodToListenTo);
            if (vec == null)
            {
                return;
            }
            if (vec.contains(listener))
            {
                vec.removeElement(listener);
            }
        } /* end if */
    }

    static public void unregisterAllMethodsListener(ApiListener listener)
    {
        unregisterMethodListener(RESTApi.K_MOFILER_API_METHOD_NAME_get, listener);
        unregisterMethodListener(RESTApi.K_MOFILER_API_METHOD_NAME_inject, listener);
    }

    static private void addMethodListener(String a_code, ApiListener listener, Hashtable msgListeners)
    {
        if (msgListeners == null)
        {
            msgListeners = new Hashtable();
        }
        Vector vec = (Vector) msgListeners.get(a_code);
        if (vec == null)
        {
            vec = new Vector();
            vec.addElement(listener);
            msgListeners.put(a_code, vec);
            return;
        }
        if (!vec.contains(listener))
        {
            vec.addElement(listener);
        }
    }


    public void useThreadedConnections(boolean a_bUseThreadedConns, boolean a_bBlockThem)
    {
        bThreadedConnections = a_bUseThreadedConns;
        bUseBlockingThreads = a_bBlockThem;
    }

    
    public void addPropertyKeyValuePair(String header, String value)
    {
    	if (hashApplicationHeaders == null)
    		hashApplicationHeaders = new Hashtable();
    	hashApplicationHeaders.put(header, value);
    }
    
    private void setHTTPPOSTPayload(String a_strPayload) throws JSONException
    {
        connFetcher.setPayload(a_strPayload);
    }

    private void setHTTPPOSTPayload(JSONObject a_jsonPayload) throws JSONException
    {
        connFetcher.setPayload(a_jsonPayload);
    }

    private void setHTTPPOSTPayload(JSONArray a_jsonPayload) throws JSONException
    {
        connFetcher.setPayload(a_jsonPayload);
    }
    
    public void setIdentity(Hashtable hashIds)
    {
    	connFetcher.setIdentity(hashIds);
    }
    
    private int connWrapper(String a_strMethod, String a_strURL, boolean a_bHTTPMethodIsPost)
    {
        //int retCode = connFetcher.connPlainHitURL_Threaded(a_strURL);
        int retCode  = 0;

        //show some sign here that we are fetching data
        /**/

    	connFetcher.addApplicationHeaders(hashApplicationHeaders);    	
        
        if (bThreadedConnections)
        {
            connFetcher.connPlainHitURL_Threaded(a_strMethod, a_strURL, this, bUseBlockingThreads, a_bHTTPMethodIsPost, null);
        } /* end if */
        else
        {
            connFetcher.connPlainHitURL_UnThreaded(a_strMethod, a_strURL, this, a_bHTTPMethodIsPost);
            retCode = connFetcher.iLastretCode;
        }

        if (retCode < 0)
        {
            //ToDo: treat the error here
        } /* end if */
        else
        {
            //ToDo: ALL GOOD
        }

        return retCode;
    }


    /*

    /pushKeyValue

Method:
    PUT

Request:
    No content

    */
    public int pushKeyValue(String a_strKey, String a_strValue, long expireAfter) throws JSONException
    {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;
        strCurrentMethodName = K_MOFILER_API_METHOD_NAME_inject;

        JSONObject json = new JSONObject();
        json.put(a_strKey, a_strValue);
        json.put("expireAfter", expireAfter);
        json.put(K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());
        setHTTPPOSTPayload(json);
        int retCode = connWrapper(K_MOFILER_API_METHOD_NAME_inject, strURL, true);

        return retCode;
    }

    public int pushKeyValue(String a_strKey, String a_strValue) throws JSONException
    {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;
        strCurrentMethodName = K_MOFILER_API_METHOD_NAME_inject;
        
        JSONObject json = new JSONObject();
        json.put(a_strKey, a_strValue);
        json.put(K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());
        setHTTPPOSTPayload(json);
        int retCode = connWrapper(K_MOFILER_API_METHOD_NAME_inject, strURL, true);

        return retCode;
    }

    public int pushKeyValue(String a_strKey, JSONObject a_jsonValue) throws JSONException
    {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;
        strCurrentMethodName = K_MOFILER_API_METHOD_NAME_inject;
        
        JSONObject json = new JSONObject();
        json.put(a_strKey, a_jsonValue);
        json.put(K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());
        setHTTPPOSTPayload(json);
        int retCode = connWrapper(K_MOFILER_API_METHOD_NAME_inject, strURL, true);

        return retCode;
    }

    public int pushKeyArray(String a_strKey, JSONArray a_jsonArray) throws JSONException
    {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;
        strCurrentMethodName = K_MOFILER_API_METHOD_NAME_inject;
        
        JSONObject json = new JSONObject();
        json.put(a_strKey, a_jsonArray);
        json.put(K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());
        setHTTPPOSTPayload(json);
        int retCode = connWrapper(K_MOFILER_API_METHOD_NAME_inject, strURL, true);

        return retCode;
    }
    
    
    public int pushKeyValueStack(JSONArray jsonData) throws JSONException
    {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;
        strCurrentMethodName = K_MOFILER_API_METHOD_NAME_inject;
        setHTTPPOSTPayload(jsonData);
        int retCode = connWrapper(K_MOFILER_API_METHOD_NAME_inject, strURL, true);

        return retCode;
    }

    
    public int getValue(String a_strKey, String a_identityKey, String a_identityValue) throws JSONException
    {
        //String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_get;
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_get + MO_Device.getDeviceManufacturer() + "/" + a_identityKey + "/" + a_identityValue + "/" + a_strKey + "/";
        strCurrentMethodName = K_MOFILER_API_METHOD_NAME_get;
        
        int retCode = connWrapper(K_MOFILER_API_METHOD_NAME_get, strURL, false);

        return retCode;
    }
    
    
    public void setServerURL(String a_strURL){
    	K_MOFILER_API_URL_BASE_POSTFIX = a_strURL;
        K_MOFILER_API_URL_BASE = "http://" + K_MOFILER_API_URL_BASE_POSTFIX;

    }

    public String getServerURL(){
    	return K_MOFILER_API_URL_BASE_POSTFIX;
    }

    public String getMethodForError(String a_strErrored_Method)
    {
        String strOriginalMethod = "";

        if (a_strErrored_Method != null)
        {
            if (a_strErrored_Method.startsWith("error") )
            {
                int iIdx = a_strErrored_Method.indexOf("_");
                if (
                   (iIdx >= 0)
                   &&
                   (iIdx <= a_strErrored_Method.length() )
                   )
                {
                    strOriginalMethod = a_strErrored_Method.substring(iIdx+1);
                } /* end if */
            } /* end if */
        } /* end if */

        return strOriginalMethod;
    }

    public void dataReceived(String a_methodCalled, String a_strPayload, String a_strOriginalSentPayload, boolean a_bHTTPMethodIsPost) throws Exception
    {
        //ToDo: HERE PARSE OUT the payload and create a proper business object.
        //THEN pass the !!business object!! to the ApiListeners.

        Vector vectBO = new Vector();
        JSONObject json = null;
        
        System.err.println("DATA SENT: " + a_strOriginalSentPayload);
        System.err.println("DATA RECEIVED: " + a_strPayload);
        System.err.println("METHOD CALLED: " + a_methodCalled);

        boolean jsonNull = false;

        //FIX for {} and null.
        if (a_strPayload.equals("{}")                           
            || a_strPayload.equals("null") )
        {

            //a_strPayload = "{\"error\":\"emty json {}\"}";
            a_strPayload = "{\"empty\":\"" +  a_strPayload + "\"}";         
        }

        if ( a_strPayload.equals("{\"error\": \"null\"}") )
        {
            jsonNull = true;
        }

        try
        {
            json = new JSONObject(a_strPayload);

        } catch ( JSONException ex )
        {
            ex.printStackTrace();
        }

        vectBO.addElement(a_methodCalled);
        vectBO.addElement(json);
        vectBO.addElement(a_strOriginalSentPayload);

        Vector listeners = null;

        if (a_methodCalled.startsWith("error"))
        {
            listeners = (Vector) msgListeners.get(getMethodForError(a_methodCalled));
        } else
        {
            listeners = (Vector) msgListeners.get(a_methodCalled);
        } /* end if*/


        if (listeners != null)
        {
            if (!jsonNull)
            {
                for (int iter = 0; iter < listeners.size(); iter++)
                {
                    ((ApiListener) listeners.elementAt(iter)).methodResponded(a_methodCalled + "_" + strCurrentMethodName, vectBO);//a_strPayload);
                } /* end for */
            } /* end if */

        } /* end if */

    }
    
}

