package org.jsonrpc;

import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * A JSONAble interface.
 * @author Ariel Aguirre <odhixon@gmail.com>
 * @version 1.0
 */
public interface IJSONAble {

    /**
     * Converts to JSON object.
     * @return A JSON object.
     * @throws JSONException
     */
    JSONObject toJSON() throws JSONException;

    /**
     * Convert to object from JSON object
     * @param jsonObject A JSON object.
     * @throws JSONException
     */
    void fromJSON(JSONObject jsonObject) throws JSONException;
}
