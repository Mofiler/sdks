package org.jsonrpc;

import java.util.Hashtable;
import org.json.me.JSONObject;

/**
 * A JSON-RPC client interface.
 * @author Ariel Aguirre <odhixon@gmail.com>
 * @version 1.0
 */
public interface IJSONRPCClient {

    /**
     * Is debug mode.
     * @return Debug mode.
     */
    public boolean isDebug();

    /**
     * Set debug mode.
     * @param debug Debug mode.
     */
    public void setDebug(boolean debug);

    /**
     * Get the full URL for the JSON-RPC server.
     * @return The full URL.
     */
    public String getUrl();

    /**
     * Set the full URL for the JSON-RPC server.
     * @param url The full URL.
     */
    public void setUrl(String url);

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
    public JSONObject execute(String method, Hashtable params, boolean requiredResponse) throws Exception;
}
