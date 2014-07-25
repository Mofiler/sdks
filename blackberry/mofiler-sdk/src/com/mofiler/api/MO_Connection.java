package com.mofiler.api;
/*
 * @(#)MSG_Connection	1.0 24-02-14

	Mofiler
	
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

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;


/**
 * This class provides all http connection capability handling
 * 
 * @author Mario Zorz
 * @version 1.0
 * @see HttpConnection
 */
public final class MO_Connection
{
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
    private int    lastRC;
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
    private static final int MO_CONN_RELOCATION              = -301; //302;
    private static final int MO_CONN_RELOCATION_TEMP         = -302; //302;
    /**
     * internal value for HTTP 100 responses (continue)
     */
    private static final int MO_CONN_CONTINUE                = -100;
    /**
     * internal value for any unhandled HTTP responses
     */
    private static final int MO_CONN_SOMEOTHERHTTPRESP       = -42;
/**     private static final int  MO_CONN_MAXRECEIVEBUFFER        = 4096; */
    /**
     * noise level is measured by the exceptioncounter
     */
    private int  iExceptionCounter;

    /**
     */
    //InputStream  	is;
    DataInputStream is;
    /**
     * last return code
     */
    int             rc;
    /**
     */
    //byte[]       	data1;
    int             len;
    int             ch;

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
     * 
     */
    public MO_Connection()
    {
        /* set local vars */
        method = HttpConnection.GET;
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
     * @param a_strAppVersionString
     *                 holds the Application Version String which is sent over to the server on every connection
     * @param a_strDeviceContextString
     *                 holds the Context String. Any valuable data describing the execution context (device capabilites, etc.) is sent in this chunk to be observde by the server
     * @param a_method HTTP connection method: GET, POST, etc.
     * @param a_propertyKey
     *                 holds ad-hoc http header name
     * @param a_propertyValue
     *                 holds ad-hoc header value for propertyKey
     */
    public MO_Connection(String a_method, Hashtable a_adhocHeaders)
    {
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
    private void MO_Connection_Initialize()
    {
        is = null;
        rc = 0;
        len = 0;
        rcvdData = null; //new StringBuffer();

        iExceptionCounter = 0;
    }



    /**
     * Initializator. Sets a property key/value pair.
     * 
     * @param a_propertyKey
     *                 holds ad-hoc http header name
     * @param a_propertyValue
     *                 holds ad-hoc header value for propertyKey
     */
    public void MO_Msg_SetPropertyKeyValuePair(Hashtable a_adhocHeaders)
    {
        /* set local vars */
        headerHashtable = a_adhocHeaders;
    }

    /**
     * Initializator. Sets a property key/value pair.
     * 
     * @param a_propertyKey
     *                 holds ad-hoc http header name
     * @param a_propertyValue
     *                 holds ad-hoc header value for propertyKey
     */
    public void MO_Msg_AddPropertyKeyValuePair(Hashtable a_adhocHeaders)
    {
        /* set local vars */
        //headerHashtable. = a_adhocHeaders;
        if (a_adhocHeaders != null)
        {
        	if (headerHashtable == null)
        		headerHashtable = new Hashtable();
        	
        	for (Enumeration e = a_adhocHeaders.keys(); e.hasMoreElements() ;) {
        		String oneKey = (String) e.nextElement();
        		String oneValue = (String) a_adhocHeaders.get(oneKey);
        		headerHashtable.put(oneKey, oneValue);
            }
        }
    }

    
    private void addNoiseLevelToHeaders()
    {
    	if (headerHashtable == null)
    		headerHashtable = new Hashtable();
    	
    	headerHashtable.put(Constants.K_MOFILER_API_HEADER_NOISELEVEL, iExceptionCounter + "");
    	
    }

    /**
     * sets everything needed to start a connection to the outside world
     * 
     * @param a_url    the url
     * @param a_method the HTTP connection method: GET, POST, etc.
     * 
     * @return 0 if OK
     *         -1 if NOT OK
     * @exception IOException
     */
    private int MO_Msg_PrepareConnection(String a_url, String a_method) throws IOException
    {
        rc = 0;

        try
        {

            try
            {
                if (conn != null)
                {
                    conn.close();
                    conn = null;
                }

                conn = (HttpConnection)Connector.open(a_url);

            } catch (SecurityException e)
            {
                iExceptionCounter++;
                conn = null;
                rc = -1;
                lastRC = -1;
                lastException = e.toString();
            }

            // Set the request method and headers
            if (conn != null)
            {
                conn.setRequestMethod(a_method);
                method = a_method;
                if (a_method == HttpConnection.POST)
                {
                    conn.setRequestProperty("Content-Type", "application/json");
                    //conn.setRequestProperty("Content-Type", "application/octet-stream");
                }
                
                conn.setRequestProperty("Accept-Encoding", "application/gzipped");
                conn.setRequestProperty("Pragma", "no-cache");

                addNoiseLevelToHeaders();
                
                if (headerHashtable != null)
                {
                	for (Enumeration e = headerHashtable.keys(); e.hasMoreElements() ;) {
                		String oneKey = (String) e.nextElement();
                		String oneValue = (String) headerHashtable.get(oneKey);
                        conn.setRequestProperty(oneKey, oneValue);
                    }
                }
                	
            } /* end if */

            url = a_url;

        } catch (IOException e)
        {
            url = null;
            iExceptionCounter++;
            //System.err.println("EXCEPTION: " + e.getMessage());

            //iExceptionCounter++;
            rc = -1;
            lastRC = -1;
            lastException = e.toString();
            e.printStackTrace();
            
            //throw new IllegalArgumentException("Not an HTTP URL");
            throw e;

        }

        return rc;
    }

    /**
     * gets the actual noise level. Any exception thrown inside this class makes the noise level higher.
     * 
     * @return the noise level
     */
    public int MO_Msg_GetNoiseLevel()
    {
        return iExceptionCounter;
    }

    /**
     * resets the actual noise level to zero. Any exception thrown inside this class makes the noise level higher.
     */
    public void MO_Msg_ResetNoiseLevel()
    {
        iExceptionCounter = 0;
    }


    public void MO_Msg_SetEncoding(String a_strEncoding)
    {
        if (a_strEncoding != null)
        {
            if (a_strEncoding.length() > 0)
            {
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
     * 
     * @return 0 if OK
     *         -1 if NOT OK (exception thrown)
     *         HTTP return code whenever not equal to 200 OK
     * @exception IOException
     */
    public int MO_Msg_Connect(String a_url, String a_message, String a_method) throws IOException
    {

        rc = 0;

        if (
           ( a_url != null )
           &&
           ( a_message != null )
           )
        {
            try
            {

                rc = MO_Msg_PrepareConnection(a_url, a_method);
                if (rc >= 0)
                {

                    try
                    {

                        rc = MO_Msg_SendRequest(a_message);

                        if (rc > 0)
                        {
                            if (rc == HttpConnection.HTTP_OK)
                            {
                                MO_Msg_ReceiveResponse();
                            }
                            MO_Int_Msg_Disconnect();
                        } else
                        {
                            String strNewURL = null;

                            switch (rc)
                            {
                            case MO_CONN_RELOCATION:
                            case MO_CONN_RELOCATION_TEMP:
                                strNewURL = conn.getHeaderField("Location").trim();
                                MO_Int_Msg_Disconnect();
                                rc = MO_Msg_Connect(strNewURL, a_message, a_method);
                                break;

                            case  MO_CONN_CONTINUE:
// 									MO_Msg_ReceiveResponse();
// 									MO_Int_Msg_ParseHTTPContinueResponse();
// 									rc = HttpConnection.HTTP_OK;
                                break;
                            case MO_CONN_SOMEOTHERHTTPRESP:
                                rc = lastRC;
                                break;
                            case -409:
                                MO_Msg_ReceiveResponse();
                                break;

                            default:
                                break;
                            }

                        }

                    }
                    catch (Exception e)
                    {
                        iExceptionCounter++;
                    } finally
                    {
                        MO_Int_Msg_Disconnect();
                    }
                } else
                {
                    if (conn != null)
                    {
                        conn.close();
                        conn = null;
                    }
                }


            }
            catch (Exception e)
            {
                iExceptionCounter++;
                rc = -1;
                lastRC = -1;
                lastException = e.toString();
                e.printStackTrace();
                
            } finally
            {
                MO_Int_Msg_Disconnect();
            }

        } else
        {
            throw new IllegalArgumentException("URL or message invalid");
        }

        return rc;

    }


    /**
     * closes any open connection and input/output streams
     * 
     * @exception IOException
     */
    private void MO_Int_Msg_Disconnect() throws IOException
    {
        try
        {
            if (is != null)
            {
                is.close();
                is = null; 
            }
            if (os != null)
            {
                os.close();
                os = null; 
            }
            if (conn != null)
            {
                conn.close();
                conn = null;
            }

        } catch (Exception e)
        {
            iExceptionCounter++;
            throw new IllegalArgumentException("Error al cerrar la conexion");
        }

    }


    /**
     * Getter method: returns the last HTTP response code
     * 
     * @return returns the last HTTP response code
     */
    public int MO_Msg_GetLastResponseCode()
    {
        return lastRC;
    }

    /**
     * Getter method: returns the last exception message
     * 
     * @return returns the last exception message
     */
    public String MO_Msg_GetLastException()
    {
        return lastException;
    }

    /**
     * Getter method: returns the last response message
     * 
     * @return returns the last response message
     */
    public String MO_Msg_GetLastResponseMessage()
    {
        return lastRespMessage;
    }

    /**
     * Getter method: returns the last content type string
     * 
     * @return returns the last content type string
     */
    public String MO_Msg_GetLastContentType()
    {
        return lastContentType;
    }

    /**
     * Getter method: returns the last content encoding string
     * 
     * @return returns the last content encoding string
     */
    public String MO_Msg_GetLastContentEncoding()
    {
        return lastContentEncoding;
    }

    /**
     * Getter method: returns the last content length
     * 
     * @return returns the last content length
     */
    public int MO_Msg_GetLastContentLength()
    {
        return lastContentLength;
    }

    /**
     * Getter method: returns the accumulated content length
     * 
     * @return returns the accum content length
     */
    public int MO_Msg_GetAccumReceivedContentLength()
    {
        return accumContentLength;
    }

    /**
     * Getter method: returns the accumulated content length for
     * sent data
     * 
     * @return returns the accum content length sent
     */
    public int MO_Msg_GetAccumSentContentLength()
    {
        return accumSentContentLength;
    }


    /**
     * Getter method: returns the received content from the server in a string object
     * 
     * @return returns the received content from the server in a string object
     */
    public String MO_Msg_GetLastReceivedContentString()
    {
        String strToReturn = null;
        if (lastContentType == null)
        {
            lastContentType = "";
        } /* end if */

        if (lastContentEncoding == null)
        {
            lastContentEncoding = "";
        } /* end if */

        if (
           (lastContentType.startsWith("application/gzipped"))
           ||
           (lastContentEncoding.indexOf("gzip") >= 0)
           )
        {
            Runtime runtime = Runtime.getRuntime();
            rcvdUncompressedData = MO_Msg_GetLastReceivedContentUnGzip();
            if (rcvdUncompressedData != null)
            {
                try
                {
                    strToReturn = new String(rcvdUncompressedData, "UTF-8");

                } catch ( java.io.UnsupportedEncodingException ex)
                {
                    //System.err.println("Excepcion: " + ex);
                    strToReturn = new String(rcvdUncompressedData);
                }
                return strToReturn;
                //return new String(rcvdUncompressedData, "UTF-8");
            } else
            {
                return null;
            } /* end if */

        } else
        {
            try
            {
                if (rcvdData != null)
                {
                    strToReturn = new String(rcvdData, "UTF-8");
                } /* end if */

            } catch ( java.io.UnsupportedEncodingException ex )
            {
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
    public byte[] MO_Msg_GetLastReceivedContentByte()
    {
        return rcvdData;
        //return rcvdData.toString();

    }

    /**
     * empties the last received data object
     */
    public void MO_Msg_EmptyLastReceivedContent()
    {
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
        try
        {
            if (rcvdData != null)
            {
                rcvdUncompressedData = myGzip.inflate(rcvdData);
                //strToReturn = MO_Msg_Int_UnGzip(rcvdData);
            } /* end if */
        } catch (Exception ex)
        {
            //no pude leer la data!
        	int i=0;
            //System.err.println("Exception at decompressing: " + ex.getMessage());
            //System.err.println("Exception at decompressing: " + ex);
        }
        return rcvdUncompressedData;
        //return strToReturn;
    }

    /**
     * Sends a request to the specified URL, etc.
     * 
     * @param a_message receives the content to be sent to the server
     * 
     * @return 0 if OK
     *         -1 if NOT OK (exception thrown)
     *         HTTP return code whenever not equal to 200 OK
     * @exception IOException
     */
    private int MO_Msg_SendRequest(String a_message) throws IOException
    {
        rc = 0;

        try
        {

            if (method == HttpConnection.POST)
            {
                os = conn.openOutputStream();

                if (
                   (strEncoding != null)
                   &&
                   (strEncoding.length() > 0)
                   )
                {
                    os.write(a_message.getBytes(strEncoding));
                } else
                {
                    os.write(a_message.getBytes());
                } /* end if */

                os.close();
                os = null;

            }

            rc = conn.getResponseCode();
            lastRC = rc;
            lastRespMessage = conn.getResponseMessage();
            if (rc != HttpConnection.HTTP_OK)
            {
                if (
                   (rc == HttpConnection.HTTP_MOVED_PERM)
                   ||
                   (rc == HttpConnection.HTTP_MOVED_TEMP)
                   ||
                   (rc == HttpConnection.HTTP_SEE_OTHER)
                   ||
                   (rc == HttpConnection.HTTP_USE_PROXY)
                   ||
                   (rc == HttpConnection.HTTP_TEMP_REDIRECT)
                   )
                {
                    rc = MO_CONN_RELOCATION;
                } else
                    /* HTTP/1.1 100 Continue */
                    if (rc == 100)
                {
                    rc = MO_CONN_CONTINUE;
                } else
                {
                    rc = rc * -1; //we turn it negative for error handling purposes
                }
            }

        } catch (Exception e)
        {
            //throw new IllegalArgumentException("Error al enviar request");
            //System.err.println("Mi excepcion: " + e);
            if (os != null)
            {
                //os.close();
                os = null; 
            }
            iExceptionCounter++;
            rc = -1;
            lastRC = -1;
            lastException = e.toString();
            e.printStackTrace();
        }

        return rc;
    }


    /**
     * devuelve el content enviado por el servidor
     * SIEMPRE llamar a esta fx. luego de una llamada exitosa a MO_Msg_SendRequest
     * 
     * @exception IOException
     */
    private void MO_Msg_ReceiveResponse() throws IOException
    {

        rcvdData = null;
        byte[] contentResponse;
        len = 0;

        try
        {

            is = conn.openDataInputStream();


            if ( is != null )
            {

                // Get the ContentType
                String type = conn.getType();

                if (type != null)
                {
                    lastContentType = type;
                }

                // Get the Content Encoding
                lastContentEncoding = conn.getEncoding();

                // Get the length and process the data
                len = -1;
                len = (int)conn.getLength();
                if (len > 0)
                {
                    lastContentLength = len;
                    accumContentLength += lastContentLength;
                } /* end if */
                else
                {
                    len = 16384; //arbitrary read buffer
                }

                lastContentLength = 0;

                /* ENHANCEMENT 2009-08-17 */
                /* better use of memory and seems to be quicker on emu and N95 */
                //DataInputStream dataIn = conn.openDataInputStream();
                byte[] buffer = new byte[1024];
                int read = -1;
                // Read the content from url.
                ByteArrayOutputStream byteout = new ByteArrayOutputStream();
                while ((read = is.read(buffer)) >= 0)
                {
                    byteout.write(buffer, 0, read);
                }

                contentResponse = byteout.toByteArray();
                lastContentLength = contentResponse.length;


                /* BUGFIX 2009-08-05 */
                /* do this array copy ONLY if we are dealing with gzipped content */
                if (
                   (type != null)
                   &&
                   (type.startsWith("application/gzipped"))
                   ||
                   (lastContentEncoding != null)
                   &&
                   (lastContentEncoding.indexOf("gzip") >= 0)
                   )
                {
                    /* BUGFIX 2008-11-09 */
                    /* now copy exactly the readbuffer, we need this coz gzipped operations will fail
                    if we have trailing zeroes or garbage in the array */
                    /* this we didn't find out of before, because we always received Content-Length headers
                    in previous implementations of Zajag (i.e. .NET, etc.) */
                    byte[] contentResponseNew = new byte[lastContentLength];
                    for (int i = 0; i < lastContentLength; i++)
                    {
                        contentResponseNew[i] = contentResponse[i];
                    } /* end for */

                    contentResponse = null;
                    contentResponse = contentResponseNew;
                } /* end if */

            } else
            {
                throw new IllegalArgumentException("No se pudo leer respuesta del server");
            }

        } catch (Exception e)
        {
            iExceptionCounter++;
            throw new IllegalArgumentException("Error al recibir respuesta del server");
        } finally
        {
            if (is != null)
            {
                is.close();
                is = null;
            }

        }
        rcvdData = contentResponse;
    }

}

