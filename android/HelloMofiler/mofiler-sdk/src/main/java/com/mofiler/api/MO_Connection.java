package com.mofiler.api;
/*
 * @(#)MSG_Connection	1.0 24-02-14

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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;


/**
 * This class provides all http connection capability handling
 *
 * @author Mario Zorz
 * @version 1.0
 * @see HttpConnection
 */
public final class MO_Connection {

    public static String K_MOFILER_METHOD_HTTP_GET = "HttpConnection.GET";
    public static String K_MOFILER_METHOD_HTTP_POST = "HttpConnection.POST";

    InputStream inputStream = null;
    String result = "";

    /**
     * HTTP connection method: GET, POST, etc.
     */
    private String method;
    /**
     * holds ad-hoc http header name and values for header names
     */
    private Hashtable headerHashtable;

    /**
     * url to which connection takes place
     */
    private String url;
    /**
     * hold the last response message as received from server
     */
    private String lastRespMessage;
    /**
     * holds the last response code as received from server
     */
    private int lastRC;
    /**
     * holds the last exception message in case anything went wrong
     */
    private String lastException = null;
    /**
     * holds the last content type string as received from server
     */
    private String lastContentType;
    /**
     * holds the last content encoding string as received from
     * server
     */
    private String lastContentEncoding;
    /**
     * holds the last content lenght as received from server
     */
    private int lastContentLength;
    /**
     * holds the accumulated content lenght as received from server
     */
    private int accumContentLength;
    /**
     * holds the accumulated content lenght as sent to the server
     * (approx)
     */
    private int accumSentContentLength;
    /**
     * the HttpConnection instance
     */
    private HttpConnection conn;
    /**
     * holds the last received data
     */
    public byte[] rcvdData;
    /**
     * holds the last received data, uncompressed
     */
    public byte[] rcvdUncompressedData;
    /**
     * internal value for HTTP 30x responses (relocation)
     */
    private static final int MO_CONN_RELOCATION = -301; //302;
    private static final int MO_CONN_RELOCATION_TEMP = -302; //302;
    /**
     * internal value for HTTP 100 responses (continue)
     */
    private static final int MO_CONN_CONTINUE = -100;
    /**
     * internal value for any unhandled HTTP responses
     */
    private static final int MO_CONN_SOMEOTHERHTTPRESP = -42;
/**     private static final int  MO_CONN_MAXRECEIVEBUFFER        = 4096; */
    /**
     * noise level is measured by the exceptioncounter
     */
    private int iExceptionCounter;

    /**
     */
    //InputStream  	is;
    DataInputStream is;
    /**
     * last return code
     */
    int rc;
    /**
     */
    //byte[]       	data1;
    int len;
    int ch;

    /**
     */
    OutputStream os = null;

    /**
     * instance for GZIP library
     */
    private com.mofiler.util.GZIP myGzip;

    private String strEncoding;

    /**
     * creator method
     */
    public MO_Connection() {
        /* set local vars */
        method = K_MOFILER_METHOD_HTTP_GET; //"HttpConnection.GET";
        headerHashtable = null; //a_adhocHeaders;
        conn = null;
        url = null;
        lastRespMessage = null;
        lastRC = 0;

        MO_Connection_Initialize();

    }

    /**
     * creator method
     *
     * @param a_strAppVersionString    holds the Application Version String which is sent over to the server on every connection
     * @param a_strDeviceContextString holds the Context String. Any valuable data describing the execution context (device capabilites, etc.) is sent in this chunk to be observde by the server
     * @param a_method                 HTTP connection method: GET, POST, etc.
     * @param a_propertyKey            holds ad-hoc http header name
     * @param a_propertyValue          holds ad-hoc header value for propertyKey
     */
    public MO_Connection(String a_method, Hashtable a_adhocHeaders) {
        /* set local vars */
        method = a_method;
        headerHashtable = a_adhocHeaders;
        conn = null;
        url = null;
        lastRespMessage = null;
        lastRC = 0;

        MO_Connection_Initialize();

    }


    /**
     * initializator proc
     */
    private void MO_Connection_Initialize() {
        is = null;
        rc = 0;
        len = 0;
        rcvdData = null; //new StringBuffer();

        iExceptionCounter = 0;
    }


    /**
     * Initializator. Sets a property key/value pair.
     *
     * @param a_propertyKey   holds ad-hoc http header name
     * @param a_propertyValue holds ad-hoc header value for propertyKey
     */
    public void MO_Msg_SetPropertyKeyValuePair(Hashtable a_adhocHeaders) {
        /* set local vars */
        headerHashtable = a_adhocHeaders;
    }

    /**
     * Initializator. Sets a property key/value pair.
     *
     * @param a_propertyKey   holds ad-hoc http header name
     * @param a_propertyValue holds ad-hoc header value for propertyKey
     */
    public void MO_Msg_AddPropertyKeyValuePair(Hashtable a_adhocHeaders) {
        /* set local vars */
        //headerHashtable. = a_adhocHeaders;
        if (a_adhocHeaders != null) {
            if (headerHashtable == null)
                headerHashtable = new Hashtable();

            for (Enumeration e = a_adhocHeaders.keys(); e.hasMoreElements(); ) {
                String oneKey = (String) e.nextElement();
                String oneValue = (String) a_adhocHeaders.get(oneKey);
                headerHashtable.put(oneKey, oneValue);
            }
        }
    }


    private void addNoiseLevelToHeaders() {
        if (headerHashtable == null)
            headerHashtable = new Hashtable();

        headerHashtable.put(Constants.K_MOFILER_API_HEADER_NOISELEVEL, iExceptionCounter + "");

    }


    /**
     * gets the actual noise level. Any exception thrown inside this class makes the noise level higher.
     *
     * @return the noise level
     */
    public int MO_Msg_GetNoiseLevel() {
        return iExceptionCounter;
    }

    /**
     * resets the actual noise level to zero. Any exception thrown inside this class makes the noise level higher.
     */
    public void MO_Msg_ResetNoiseLevel() {
        iExceptionCounter = 0;
    }


    public void MO_Msg_SetEncoding(String a_strEncoding) {
        if (a_strEncoding != null) {
            if (a_strEncoding.length() > 0) {
                strEncoding = a_strEncoding;
            } /* end if */
        } /* end if */
    }


    /**
     * connects to a URL and sends the message as body
     *
     * @param a_url     url to connect to
     * @param a_message message to send to server
     * @param a_method  the HTTP connection method: GET, POST, etc.
     * @return 0 if OK
     * -1 if NOT OK (exception thrown)
     * HTTP return code whenever not equal to 200 OK
     * @throws IOException
     */
    public int MO_Msg_Connect(String a_url, String a_message, String a_method) throws IOException {

        rc = 0;

        if (
                (a_url != null)
                        &&
                        (a_message != null)
                ) {

            method = a_method;

            try {
                // Set up HTTP post

                // HttpClient is more then less deprecated. Need to change to URLConnection
                HttpClient httpClient = new DefaultHttpClient();

                HttpResponse httpResponse;

                if (method.compareTo(K_MOFILER_METHOD_HTTP_GET) == 0) {
                    HttpGet get = new HttpGet(a_url);
                    get.setHeader("Content-type", "application/json");
                    get.setHeader("Accept-Encoding", "application/gzipped");
                    get.setHeader("Pragma", "no-cache");
                    addNoiseLevelToHeaders();
                    if (headerHashtable != null) {
                        for (Enumeration e = headerHashtable.keys(); e.hasMoreElements(); ) {
                            String oneKey = (String) e.nextElement();
                            String oneValue = (String) headerHashtable.get(oneKey);
                            get.setHeader(oneKey, oneValue);
                        }
                    }
                    httpResponse = httpClient.execute(get);
                } else {
                    HttpPost httpPost = new HttpPost(a_url);
                    httpPost.setHeader("Content-type", "application/json");
                    httpPost.setHeader("Accept-Encoding", "application/gzipped");
                    httpPost.setHeader("Pragma", "no-cache");
                    addNoiseLevelToHeaders();
                    if (headerHashtable != null) {
                        for (Enumeration e = headerHashtable.keys(); e.hasMoreElements(); ) {
                            String oneKey = (String) e.nextElement();
                            String oneValue = (String) headerHashtable.get(oneKey);
                            httpPost.setHeader(oneKey, oneValue);
                        }
                    }

                    httpPost.setEntity(new StringEntity(a_message, "UTF8"));
                    httpResponse = httpClient.execute(httpPost);
                }

                HttpEntity httpEntity = httpResponse.getEntity();
                rc = httpResponse.getStatusLine().getStatusCode();

                // Read content & Log
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsupportedEncodingException", e1.toString());
                e1.printStackTrace();
                iExceptionCounter++;
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
                iExceptionCounter++;
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
                iExceptionCounter++;
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
                iExceptionCounter++;
            }
            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();
                rcvdData = result.getBytes();

            } catch (Exception e) {
                iExceptionCounter++;
                Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
            }


        } else {
            iExceptionCounter++;
            throw new IllegalArgumentException("URL or message invalid");
        }

        return rc;

    }


    /**
     * closes any open connection and input/output streams
     *
     * @throws IOException
     */
    private void MO_Int_Msg_Disconnect() throws IOException {
        try {
            if (is != null) {
                is.close();
                is = null;
            }
            if (os != null) {
                os.close();
                os = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }

        } catch (Exception e) {
            iExceptionCounter++;
            throw new IllegalArgumentException("Error al cerrar la conexion");
        }

    }


    /**
     * Getter method: returns the last HTTP response code
     *
     * @return returns the last HTTP response code
     */
    public int MO_Msg_GetLastResponseCode() {
        return lastRC;
    }

    /**
     * Getter method: returns the last exception message
     *
     * @return returns the last exception message
     */
    public String MO_Msg_GetLastException() {
        return lastException;
    }

    /**
     * Getter method: returns the last response message
     *
     * @return returns the last response message
     */
    public String MO_Msg_GetLastResponseMessage() {
        return lastRespMessage;
    }

    /**
     * Getter method: returns the last content type string
     *
     * @return returns the last content type string
     */
    public String MO_Msg_GetLastContentType() {
        return lastContentType;
    }

    /**
     * Getter method: returns the last content encoding string
     *
     * @return returns the last content encoding string
     */
    public String MO_Msg_GetLastContentEncoding() {
        return lastContentEncoding;
    }

    /**
     * Getter method: returns the last content length
     *
     * @return returns the last content length
     */
    public int MO_Msg_GetLastContentLength() {
        return lastContentLength;
    }

    /**
     * Getter method: returns the accumulated content length
     *
     * @return returns the accum content length
     */
    public int MO_Msg_GetAccumReceivedContentLength() {
        return accumContentLength;
    }

    /**
     * Getter method: returns the accumulated content length for
     * sent data
     *
     * @return returns the accum content length sent
     */
    public int MO_Msg_GetAccumSentContentLength() {
        return accumSentContentLength;
    }


    /**
     * Getter method: returns the received content from the server in a string object
     *
     * @return returns the received content from the server in a string object
     */
    public String MO_Msg_GetLastReceivedContentString() {
        String strToReturn = null;
        if (lastContentType == null) {
            lastContentType = "";
        } /* end if */

        if (lastContentEncoding == null) {
            lastContentEncoding = "";
        } /* end if */

        if (
                (lastContentType.startsWith("application/gzipped"))
                        ||
                        (lastContentEncoding.indexOf("gzip") >= 0)
                ) {
            Runtime runtime = Runtime.getRuntime();
            rcvdUncompressedData = MO_Msg_GetLastReceivedContentUnGzip();
            if (rcvdUncompressedData != null) {
                try {
                    strToReturn = new String(rcvdUncompressedData, "UTF-8");

                } catch (java.io.UnsupportedEncodingException ex) {
                    //System.err.println("Excepcion: " + ex);
                    strToReturn = new String(rcvdUncompressedData);
                }
                return strToReturn;
                //return new String(rcvdUncompressedData, "UTF-8");
            } else {
                return null;
            } /* end if */

        } else {
            try {
                if (rcvdData != null) {
                    strToReturn = new String(rcvdData, "UTF-8");
                } /* end if */

            } catch (java.io.UnsupportedEncodingException ex) {
                //System.err.println("Excepcion: " + ex);
                strToReturn = new String(rcvdData);
            }
            return strToReturn;
        } /* end if */

    }

    /**
     * Getter method: returns the received content from the server in a byte[] object
     *
     * @return returns the received content from the server in a byte[] object
     */
    public byte[] MO_Msg_GetLastReceivedContentByte() {
        return rcvdData;
        //return rcvdData.toString();

    }

    /**
     * empties the last received data object
     */
    public void MO_Msg_EmptyLastReceivedContent() {
        rcvdData = null;
        rcvdUncompressedData = null;
    }

    /**
     * unzips the last received data if content type was gzipped
     *
     * @return returns a string represntation of the recived data
     */
    private byte[] MO_Msg_GetLastReceivedContentUnGzip()
    //private String MO_Msg_GetLastReceivedContentUnGzip()
    {
        String strToReturn = null;
        rcvdUncompressedData = null;
        try {
            if (rcvdData != null) {
                rcvdUncompressedData = myGzip.inflate(rcvdData);
                //strToReturn = MO_Msg_Int_UnGzip(rcvdData);
            } /* end if */
        } catch (Exception ex) {
            //no pude leer la data!
            int i = 0;
            //System.err.println("Exception at decompressing: " + ex.getMessage());
            //System.err.println("Exception at decompressing: " + ex);
        }
        return rcvdUncompressedData;
        //return strToReturn;
    }


}

