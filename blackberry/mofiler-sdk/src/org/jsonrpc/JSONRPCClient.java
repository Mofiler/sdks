package org.jsonrpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.json.me.JSONObject;

/**
 * A JSON-RPC client object.
 * @author Ariel Aguirre <odhixon@gmail.com>
 * @version 1.0
 */
public class JSONRPCClient implements IJSONRPCClient {

    /**
     * Turns debugging on/off
     */
    private boolean debug;
    /**
     * The full URL for the JSON-RPC server.
     */
    private String url;
    /**
     * The incremental request id.
     */
    private int requestId;

    /**
     * Constructs an JSON-RPC client with a specified string representing a URL.
     * @param url The full URL for the JSON-RPC server.
     */
    public JSONRPCClient(String url) throws Exception {
        //Set url.
        this.url = url;
        //Initialize request id.
        requestId = 0;
        //Set debug mode off.
        debug = false;
    }

    /**
     * Is debug mode.
     * @return
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Set debug mode.
     * @param debug
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Get the full URL for the JSON-RPC server.
     * @return The full URL.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the full URL for the JSON-RPC server.
     * @param url The full URL.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * This method opens an HttpConnection on the URL stored in the url variable,
     * sends an JSON-RPC request and processes the response sent back from the
     * server.
     * @param method Contains the method on the server that the client will access.
     * @param params Contains a list of parameters to be sent to the server.
     * @param requiredResponse  Required response execute service.
     * @return The JSON object.
     * returned by the server
     */
    public JSONObject execute(String method, Hashtable params, boolean requiredResponse) throws Exception {
        // J2ME classes
        HttpConnection con = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        //Request object
        JSONObject request = new JSONObject();
        request.put("version", "1.1");
        request.put("method", method);
        request.put("params", params);
        if (requestId == Integer.MAX_VALUE) {
            requestId = 1;
        } else {
            requestId++;
        }
        request.put("id", requestId);
        //Response object
        JSONObject response = null;
        try {
            if (debug) {
                System.out.println("json-rpc request= " + request.toString());
            }
            con = (HttpConnection) Connector.open(url, Connector.READ_WRITE);
            con.setRequestMethod(HttpConnection.POST);
            con.setRequestProperty("Content-Type", "application/json");

            byte[] jsonBytes = request.toString().getBytes();

            // Open output stream
            outputStream = con.openOutputStream();
            outputStream.write(jsonBytes);

            if(requiredResponse) {
                    // Open input stream
                    inputStream = con.openInputStream();
                    // Create the byte array output stream
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int c;
                    while ((c = inputStream.read()) != -1) {
                        baos.write(c);
                    }
                    jsonBytes = baos.toByteArray();
                    String jsonText = new String(jsonBytes, 0, jsonBytes.length, "US-ASCII");
                    if (debug) {
                        System.out.println("json-rpc response= " + jsonText);
                    }
                    // Close the byte array output stream
                    baos.close();
        
                    response = new JSONObject(jsonText);
            } else {
                response = new JSONObject();
                response.put("result", new JSONObject());
            }
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }//end try/catch
        }//end try/finally
        if (response.has("error")) {
            String messageError = response.getJSONObject("error").getString("message");
            throw new Exception(messageError);
        }
        return response.getJSONObject("result");
    }
}
