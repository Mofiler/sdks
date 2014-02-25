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
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;

import javax.microedition.io.HttpConnection;

import org.json.me.JSONException;
import org.json.me.JSONObject;

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
    String strImageName = null;
    boolean bActionFinished = false;
    private boolean bDoBlockThread = false;

    private String strSessionIDValue = null;
    private Hashtable hashSessionHeaders = null;

    private String strPayload = null;

	public Fetcher()
	{
        //generic constructor
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

    public void setPayload(String a_strPayload)
    {
        strPayload = a_strPayload;
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
        strURLToHit = a_strURL + MO_Device.getConnectionString();
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


    public void connPlainHitURL_Threaded(String a_strAPIName, String a_strURL, FetcherListener a_fetchListener, boolean a_bBlock, boolean a_bHTTPMethodIsPost, String a_strThreadID)
    {
        /* we need this in order to avoid 2 or more UI threads calling the fetcher at the same time */
        synchronized (MO_FETCH_THREAD_LOCK)
        {
            strURLToHit = a_strURL + MO_Device.getConnectionString();
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
                while (bActionFinished == false)
                {
                    //iLoopFaker++;
                    //sleep(10);
                } /* end while */
            } /* end if */

        } /* end block */

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
    			strSessionIDValue = rndNbr + "";
    		}
			hashSessionHeaders.put(Constants.K_MOFILER_API_HEADER_SESSIONID, strSessionIDValue);
			hashSessionHeaders.put(Constants.K_MOFILER_API_HEADER_API_VERSION, Constants.K_MOFILER_API_VERSION);
			
			//OJOOOO no tiene que ir en el header, esto tiene que ser parte del payload que se envia!!
			//OJOOOO no tiene que ir en el header, esto tiene que ser parte del payload que se envia!!
			//OJOOOO no tiene que ir en el header, esto tiene que ser parte del payload que se envia!!
			//hashSessionHeaders.put(Constants.K_MOFILER_API_HEADER_DEVICE_CONTEXT, buildDeviceContextJSONObject());
    	}
    	
    }

    private String buildDeviceContextJSONObject()
    {
    	JSONObject jsonobj = new JSONObject();
    	JSONObject jsonobjInner = new JSONObject();
    	
    	try {
    		
        	jsonobjInner.put(Constants.K_MOFILER_API_DEVICE_CONTEXT_MANUFACTURER, MO_Device.getDeviceManufacturer());
        	jsonobjInner.put(Constants.K_MOFILER_API_DEVICE_CONTEXT_MODELNAME, MO_Device.getDeviceModelName());
        	jsonobjInner.put(Constants.K_MOFILER_API_DEVICE_CONTEXT_DISPLAYSIZE, MO_Device.getDisplaySize());
        	jsonobjInner.put(Constants.K_MOFILER_API_DEVICE_CONTEXT_NETWORK, MO_Device.getConnectionString());
        	jsonobj.put(Constants.K_MOFILER_API_DEVICE_CONTEXT, jsonobjInner);
    	}
    	catch (JSONException ex)
    	{
    		System.err.println("FECK, something went quite wrong");
    		ex.printStackTrace();
    	}
    	
		return jsonobj.toString();
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
                    rc = conn.MO_Msg_Connect(a_strURL, "payload=" + strPayload, HttpConnection.POST);
//#endif

                }
                else
                {
                    rc = conn.MO_Msg_Connect(a_strURL, "", HttpConnection.POST);
                } /* end if */
            }
            else
            {
                rc = conn.MO_Msg_Connect(a_strURL, "", HttpConnection.GET);
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

            if ( rc == HttpConnection.HTTP_OK )
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
                    
                    //System.err.println("RECIBIMOS ESTO: " + strLastPayloadReceived);
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


    public String getTime(){
		Calendar c = Calendar.getInstance();		
        Date d = new Date(c.getTime().getTime());
        return d.toString() + " || ";
        //System.out.println("cal2: " + Calendar.getInstance(TimeZone.getTimeZone("GMT+1")).getTime().getTime()); 
	}

    

    
}

