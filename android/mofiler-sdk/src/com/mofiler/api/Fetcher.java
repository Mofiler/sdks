package com.mofiler.api;
/*
 * @(#)Fetcher	1.0 24-02-14

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

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;

import com.mofiler.device.MO_Device;

public class Fetcher
{
    /* class members here */
    private static final Object MO_FETCH_THREAD_LOCK = new Object();
    private String strHTTPLastException = null;
    private String strLastPayloadReceived = null;
    private String strURLToHit = null;
    private String strCurrentAPIMethodName = null;
    private boolean bCurrMethodIsPOST = false;
    private FetcherListener fetchListener = null;
    public int iLastretCode = 0;
    boolean bActionFinished = false;

    private String strSessionIDValue = null;
    private String strInstallIDValue = null;
    private Hashtable hashSessionHeaders = null;
    private Hashtable hashApplicationHeaders = null;

    private String strPayload = null;
    //private JSONObject jsonPayload = null;
    private JSONArray jsonPayload = null;
    
    private Hashtable identity = null;
    
    private Context context;
    
    private boolean bUseVerboseExtras;

	public Fetcher()
	{
        //generic constructor
	}

    public void setContext(Context context){
    	this.context = context;
    }
	
    public String getLastHTTPResponse()
    {
        return strHTTPLastException;
    }

    private void resetConnState()
    {
        strLastPayloadReceived = null;
        strHTTPLastException = null;
        strURLToHit = null;
        //MOConnThread = null;
        iLastretCode = 0;
    }

    public void setPayload(String a_strPayload) throws JSONException 
    {
        strPayload = a_strPayload;
        //clarify object
        JSONObject tmpObj = new JSONObject(strPayload);
        JSONArray tmpArr = new JSONArray();
        tmpArr.put(tmpObj);
        jsonPayload = tmpArr;
    }

    public void setPayload(JSONObject a_jsonPayload) throws JSONException 
    {
    	strPayload = a_jsonPayload.toString();
        JSONArray tmpArr = new JSONArray();
        tmpArr.put(a_jsonPayload);
        jsonPayload = tmpArr;
    }

    public void setPayload(JSONArray a_jsonPayload) throws JSONException 
    {
    	strPayload = a_jsonPayload.toString();
        jsonPayload = a_jsonPayload;
    }
    
    public void setIdentity(Hashtable hashIds)
    {
    	identity = hashIds;
    }
    
    public Hashtable getIdentity()
    {
    	return this.identity;
    }
    
    public void setUseVerboseDeviceContext(boolean bVerbose){
    	this.bUseVerboseExtras = bVerbose;
    }
    
    private void connPlainHitURL_UnThreaded()
    {
        try {
            bActionFinished = false;

            iLastretCode = connPlainHitURL(strURLToHit, bCurrMethodIsPOST);
            if (iLastretCode >= 0)
            {
                //ToDo: here call the callback fx.
                if (strLastPayloadReceived != null)
                {
                    //fetchListener.dataReceived("", strLastPayloadReceived);
                    fetchListener.dataReceived(strCurrentAPIMethodName, strLastPayloadReceived, strPayload, bCurrMethodIsPOST);
                } /* end if */
                else
                {
                    fetchListener.dataReceived(strCurrentAPIMethodName, "null", strPayload, bCurrMethodIsPOST);
                }
            } /* end if */
            else
            {
                if (strHTTPLastException != null)
                {
                    fetchListener.dataReceived("error" + "_" + strCurrentAPIMethodName, "{\"error\": \""+ "MOConnectionError - " + strHTTPLastException +"\", \"errcode\": " +iLastretCode*(-1)  + " }", strPayload, bCurrMethodIsPOST);
                }
                else
                {
                    fetchListener.dataReceived("error" + "_" + strCurrentAPIMethodName, "{\"error\": \""+ "MOConnectionError" +"\", \"errcode\": " +iLastretCode*(-1)  + " }", strPayload, bCurrMethodIsPOST);
                } /* end if */

            }

        } 
        catch(Exception ex)
        {
            try
            {
                fetchListener.dataReceived("error" + "_" + strCurrentAPIMethodName, "{\"error\": \""+ ex.getMessage() +"\"}", strPayload, bCurrMethodIsPOST);

            } catch ( Exception ex2 )
            {
            	ex2.printStackTrace();
            }
        }
        finally
        {
            bActionFinished = true;
        }
    }

    public void connPlainHitURL_UnThreaded(String a_strAPIName, String a_strURL, FetcherListener a_fetchListener, boolean a_bHTTPMethodIsPost)
    {
        strURLToHit = a_strURL;// + MO_Device.getConnectionString();
        strCurrentAPIMethodName = a_strAPIName;
        bCurrMethodIsPOST = a_bHTTPMethodIsPost;
        fetchListener = a_fetchListener;

        connPlainHitURL_UnThreaded();
    }

    public void setSessionID(String a_strValue)
    {
        strSessionIDValue = a_strValue;
    }

    public void setSessionID(long a_lValue)
    {
        strSessionIDValue = a_lValue + "";
    }

    public void setInstallID(String a_strValue)
    {
    	//System.err.println("EN FETCHER: installid: " + a_strValue);
    	strInstallIDValue = a_strValue;
    }

    
    public void connPlainHitURL_Threaded(String a_strAPIName, String a_strURL, FetcherListener a_fetchListener, boolean a_bBlock, boolean a_bHTTPMethodIsPost, String a_strThreadID)
    {
        /* we need this in order to avoid 2 or more UI threads calling the fetcher at the same time */
        synchronized (MO_FETCH_THREAD_LOCK)
        {
            strURLToHit = a_strURL;// + MO_Device.getConnectionString();
            strCurrentAPIMethodName = a_strAPIName;
            bCurrMethodIsPOST = a_bHTTPMethodIsPost;
            fetchListener = a_fetchListener;
            //System.err.println("aca estoy1");

            Thread MOConnThread = new Thread(new Runnable() {
                public void run()
                {
                    connPlainHitURL_UnThreaded();
                }
                
              },((a_strThreadID != null) ?  a_strThreadID : "fakename"));

            bActionFinished = false;
            MOConnThread.start();

            if (a_bBlock == true)
            {
            	//TODO: some kind of yelding here
//                while (bActionFinished == false)
//                {
//                    //iLoopFaker++;
//                    //sleep(10);
//                } /* end while */
            } /* end if */

        } /* end block */

    }
    
    
    /**
     * Initializator. Sets a property key/value pair.
     * 
     * @param a_propertyKey
     *                 holds ad-hoc http header name
     * @param a_propertyValue
     *                 holds ad-hoc header value for propertyKey
     */
    public void addApplicationHeaders(Hashtable a_adhocHeaders)
    {
        /* set local vars */
        //headerHashtable. = a_adhocHeaders;
        if (a_adhocHeaders != null)
        {
        	if (hashApplicationHeaders == null)
        		hashApplicationHeaders = new Hashtable();
        	
        	for (Enumeration e = a_adhocHeaders.keys(); e.hasMoreElements() ;) {
        		String oneKey = (String) e.nextElement();
        		String oneValue = (String) a_adhocHeaders.get(oneKey);
        		hashApplicationHeaders.put(oneKey, oneValue);
            }
        }
    }
    
    /* this class is responsible for the following headers
     * 
     * -Fetcher: maneja el seteo de sesion, api version, devicecontextstring. Provee threading a las conexiones.
		X-Mofiler-SessionID
		X-Mofiler-ApiVersion
		X-Mofiler-DeviceContextString --> tendra si se conecta por wifi, 3g, tipo de dispositivo... esto tiene que ir en el CUERPO 
		del PUT!!!!
     * */
    private void constructHeaders()
    {
    	if (hashSessionHeaders == null)
    	{
    		hashSessionHeaders = new Hashtable();
    		
    		//check session id value
    		if (strSessionIDValue == null)
    		{
    			Random rndNbr = new Random();
    			strSessionIDValue = rndNbr.nextLong() + "";
    		}
    		hashSessionHeaders.put(Constants.K_MOFILER_API_HEADER_INSTALLID, strInstallIDValue);
			hashSessionHeaders.put(Constants.K_MOFILER_API_HEADER_SESSIONID, strSessionIDValue);
			hashSessionHeaders.put(Constants.K_MOFILER_API_HEADER_API_VERSION, Constants.K_MOFILER_API_VERSION);
			
			//OJOOOO no tiene que ir en el header, esto tiene que ser parte del payload que se envia!!
			//OJOOOO no tiene que ir en el header, esto tiene que ser parte del payload que se envia!!
			//OJOOOO no tiene que ir en el header, esto tiene que ser parte del payload que se envia!!
			//hashSessionHeaders.put(Constants.K_MOFILER_API_HEADER_DEVICE_CONTEXT, buildDeviceContextJSONObject());
    	}
    	
    }

    private JSONObject buildDeviceContextJSONObject()
    {
    	//JSONObject jsonobj = new JSONObject();
    	JSONObject jsonobjInner = new JSONObject();
    	
    	try {
    		
        	jsonobjInner.put(Constants.K_MOFILER_API_DEVICE_CONTEXT_MANUFACTURER, MO_Device.getDeviceManufacturer());
        	jsonobjInner.put(Constants.K_MOFILER_API_DEVICE_CONTEXT_MODELNAME, MO_Device.getDeviceModelName());
        	jsonobjInner.put(Constants.K_MOFILER_API_DEVICE_CONTEXT_DISPLAYSIZE, MO_Device.getDisplaySize(((Activity)context).getWindowManager().getDefaultDisplay()));
        	jsonobjInner.put(Constants.K_MOFILER_API_DEVICE_CONTEXT_LOCALE, MO_Device.getLocale());
        	jsonobjInner.put(Constants.K_MOFILER_API_DEVICE_CONTEXT_NETWORK, MO_Device.getConnectionString(context));

        	/* 2014-03-24 added into the body */
        	jsonobjInner.put(Constants.K_MOFILER_API_HEADER_SESSIONID, strSessionIDValue);
        	jsonobjInner.put(Constants.K_MOFILER_API_HEADER_INSTALLID, strInstallIDValue);

        	/* 2014-11-16 added extras */
        	jsonobjInner.put(Constants.K_MOFILER_API_DEVICE_CONTEXT_EXTRAS, MO_Device.getExtras(context, bUseVerboseExtras));
        	
        	//jsonobj.put(Constants.K_MOFILER_API_DEVICE_CONTEXT, jsonobjInner);
    	}
    	catch (JSONException ex)
    	{
    		//System.err.println("FECK, something went wrong");
    		ex.printStackTrace();
    	}
    	
    	//System.err.println("BUILD CONTEXT cadena es esto: " + jsonobjInner.toString());
		//return jsonobj; //jsonobj.toString();
		return jsonobjInner;
    }
    
    private JSONArray buildIdentityVector()
    {
    	Enumeration e = identity.keys();
    	JSONArray jsonToReturn = new JSONArray();
    	
    	try {
        	while ( e.hasMoreElements() ) { 
        	    String sKey = (String)e.nextElement();
        	    JSONObject tmpObj = new JSONObject();
        	    tmpObj.put(sKey, identity.get(sKey));
        	    jsonToReturn.put(tmpObj);
        	}     	
    	}
    	catch(JSONException ex)
    	{
    		//System.err.println("FECK, something went wrong with identity vector building");
    		ex.printStackTrace();
    	}
    	
    	return jsonToReturn;
    }

    public int connPlainHitURL(String a_strURL, boolean a_bHTTPMethodIsPost)
    {
        int retCode = 0;
        int     rc = 0;
        MO_Connection conn = null;
        bCurrMethodIsPOST = a_bHTTPMethodIsPost;

        resetConnState();
    	conn = new MO_Connection();
        constructHeaders();
        conn.MO_Msg_AddPropertyKeyValuePair(hashSessionHeaders);
        conn.MO_Msg_AddPropertyKeyValuePair(hashApplicationHeaders);
        

        try
        {
            if (a_bHTTPMethodIsPost)
            {
                if (strPayload != null)
                {
//#ifdef USEENCRYPT
//@
//@                    if (bUSE_ENC_TO_SERVER)
//@                    {
//@                        //String strURLEncoded = URLEncoder.rawUrlEncode(strPayload);
//@                        com.mofiler.forms.UILogger.logToScreen("going to connect URL: " + a_strURL);
//@                        com.mofiler.forms.UILogger.logToScreen("encrypt -> original string: " + strPayload);
//@                        String strURLEncoded = URLEncoder.encode(strPayload, "UTF-8");
//@                        //System.err.println("URL_ENCODED: " + strURLEncoded);
//@                        com.mofiler.forms.UILogger.logToScreen("encrypt -> URL_ENCODED: " + strURLEncoded);
//@                        String strEncryptedString = conn.MO_Msg_EncryptString(strURLEncoded);
//@                        //String strEncryptedString = conn.MO_Msg_EncryptString(URLEncoder.encode(strPayload,"UTF-8"));
//@                        //String strEncryptedString = conn.MO_Msg_EncryptString(strPayload);
//@                        //rc = conn.MO_Msg_Connect(a_strURL, strPayload, HttpConnection.POST);
//@                        rc = conn.MO_Msg_Connect(a_strURL, "payload=" + strEncryptedString, HttpConnection.POST);
//@                        //com.mofiler.forms.UILogger.logToScreen("connPlainHitURL 3");
//@                    }
//@                    else
//@                    {
//@                        //String strURLEncoded = URLEncoder.rawUrlEncode(strPayload);
//@                        String strURLEncoded = URLEncoder.encode(strPayload, "UTF-8");
//@                        rc = conn.MO_Msg_Connect(a_strURL, "payload=" + strURLEncoded, HttpConnection.POST);
//@//                         //String strURLEncoded = URLEncoder.rawUrlEncode(strPayload);
//@//                         rc = conn.MO_Msg_Connect(a_strURL, "payload=" + strPayload, HttpConnection.POST);
//@                    } /* end if */
//#else
                	JSONObject jsonPayloadToSend = new JSONObject();
                	jsonPayloadToSend.put(Constants.K_MOFILER_API_USER_VALUES, jsonPayload);
                	jsonPayloadToSend.put(Constants.K_MOFILER_API_IDENTITY, buildIdentityVector());
                	jsonPayloadToSend.put(Constants.K_MOFILER_API_DEVICE_CONTEXT, buildDeviceContextJSONObject());

                	/*jsonPayload.put(Constants.K_MOFILER_API_USER_VALUES, buildIdentityVector());
                	jsonPayload.put(Constants.K_MOFILER_API_IDENTITY, buildIdentityVector());
                	jsonPayload.put(Constants.K_MOFILER_API_DEVICE_CONTEXT, buildDeviceContextJSONObject());
                	strPayload = jsonPayload.toString();*/
                	String strPayloadLocal = jsonPayloadToSend.toString();
                    //rc = conn.MO_Msg_Connect(a_strURL, "payload=" + strPayload, HttpConnection.POST);
                    //rc = conn.MO_Msg_Connect(a_strURL, strPayload, HttpConnection.POST);
                    rc = conn.MO_Msg_Connect(a_strURL, strPayloadLocal, MO_Connection.K_MOFILER_METHOD_HTTP_POST);
//#endif

                }
                else
                {
                    rc = conn.MO_Msg_Connect(a_strURL, "", MO_Connection.K_MOFILER_METHOD_HTTP_POST);
                } /* end if */
            }
            else
            {
                rc = conn.MO_Msg_Connect(a_strURL, "", MO_Connection.K_MOFILER_METHOD_HTTP_GET);
            } /* end if */


            //if -1, then its an exception. get it.
            if (rc == -1)
            {
                strHTTPLastException = conn.MO_Msg_GetLastException();
            }
            else
            {
                strHTTPLastException = null;
            } /* end if */

            if ( rc == 200 )
            {
            	
                if (retCode < 0)
                {
                    //System.err.println("ERROR");
                }
                else
                {
                    strLastPayloadReceived = conn.MO_Msg_GetLastReceivedContentString();
                    
//#ifdef USEENCRYPT
//@                    if (strLastPayloadReceived != null)
//@                    {
//@                        strLastPayloadReceived = conn.MO_Msg_DecryptString(strLastPayloadReceived);
//@                        
//@                        //System.out.println("strLastPayloadReceived_2: "+strLastPayloadReceived);
//@                        
//@                        if (strLastPayloadReceived != null)
//@                        {
//@                            strLastPayloadReceived = URLDecoder.decode(strLastPayloadReceived, "UTF-8");
//@                        } /* end if */
//@
//@                    } /* end if */
//@                    //System.out.println("strLastPayloadReceived_3: "+strLastPayloadReceived);
//#endif
                    //System.out.println("____________________________________________");
                    
                }
            }
            else
            {
                retCode = rc;

                /*2011-07-30: according to the MO API spec, a 409 error will contain further information in the payload */
                if (rc == -409)
                {
                    strLastPayloadReceived = conn.MO_Msg_GetLastReceivedContentString();
//#ifdef USEENCRYPT
//@                    if (strLastPayloadReceived != null)
//@                    {
//@                        strLastPayloadReceived = conn.MO_Msg_DecryptString(strLastPayloadReceived);
//@                        if (strLastPayloadReceived != null)
//@                        {
//@                            strLastPayloadReceived = URLDecoder.decode(strLastPayloadReceived, "UTF-8");
//@                        } /* end if */
//@                    } /* end if */
//#endif
                } /* end if */
            }
        } catch (IOException ex)
        {
            retCode = -1;
            ex.printStackTrace();

        } catch (Exception ex2)
        {
            retCode = -1;
            ex2.printStackTrace();

        } finally {
            conn = null;
        }

        conn = null;
        return retCode;
    }


}

