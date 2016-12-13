package com.mofiler.api;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.mofiler.device.MO_Device;

import static com.mofiler.api.Constants.K_MOFILER_API_USER_VALUES;


public class RESTApi {

    public String K_MOFILER_API_URL_BASE_POSTFIX = null;
    public String K_MOFILER_API_URL_BASE = "http://" + K_MOFILER_API_URL_BASE_POSTFIX;
    public static final String K_MOFILER_API_URL_METHOD_inject = "/api/values/";
    public static final String K_MOFILER_API_URL_METHOD_get = "/api/values/";

    public static final String K_MOFILER_API_TIMESTAMP_KEY = "tstamp";
    public static final String K_MOFILER_API_LOCATION_KEY = "location";

    private Hashtable mApplicationHeaders = null;
    private Hashtable mIdentities = null;

    private long mSessionId;
    private String mInstallationId;
    private boolean mUseVerboseExtras;

    private Context mContext;

    public RESTApi(String a_strInstallationId) {
        //generic constructor
        Random rndNbr = new Random();
        mSessionId = rndNbr.nextLong();
        mInstallationId = a_strInstallationId;
    }

    public void setContext(Context ctx){
        mContext = ctx;
    }

    public void setUseVerboseDeviceContext(boolean bVerbose) {
        mUseVerboseExtras = bVerbose;
    }

    public void addPropertyKeyValuePair(String header, String value) {
        if (mApplicationHeaders == null)
            mApplicationHeaders = new Hashtable();
        mApplicationHeaders.put(header, value);
    }

    public void setIdentity(Hashtable hashIds) {
        mIdentities = hashIds;
    }

    /*

    /pushKeyValue

Method:
    PUT

Request:
    No content

    */
    public int pushKeyValue(String a_strKey, String a_strValue, long expireAfter, ApiListener listener) throws JSONException {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;
        JSONObject json = getDecoratedJsonWithKeyValue(a_strKey, a_strValue);
        json.put("expireAfter", expireAfter);
        return postRequest(strURL, json, listener);
    }

    public int pushKeyValue(String a_strKey, String a_strValue, long expireAfter, JSONObject a_jsonLocation, ApiListener listener) throws JSONException {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;

        JSONObject json = getDecoratedJsonWithKeyValue(a_strKey, a_strValue);
        json.put("expireAfter", expireAfter);
        json.put(K_MOFILER_API_LOCATION_KEY, a_jsonLocation);

        return postRequest(strURL, json, listener);
    }


    public int pushKeyValue(String a_strKey, String a_strValue, ApiListener listener) throws JSONException {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;
        JSONObject json = getDecoratedJsonWithKeyValue(a_strKey, a_strValue);
        return postRequest(strURL, json, listener);
    }

    public int pushKeyValue(String a_strKey, String a_strValue, JSONObject a_jsonLocation, ApiListener listener) throws JSONException {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;

        JSONObject json = getDecoratedJsonWithKeyValue(a_strKey, a_strValue);
        json.put(K_MOFILER_API_LOCATION_KEY, a_jsonLocation);

        return postRequest(strURL, json, listener);
    }

    public int pushKeyValue(String a_strKey, JSONObject a_jsonValue, ApiListener listener) throws JSONException {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;

        JSONObject json = new JSONObject();
        json.put(a_strKey, a_jsonValue);
        json.put(K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());
        addIdentityAndDeviceContext(json);

        return postRequest(strURL, json, listener);
    }

    public int pushKeyValue(String a_strKey, JSONObject a_jsonValue, JSONObject a_jsonLocation, ApiListener listener) throws JSONException {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;
        JSONObject json = new JSONObject();
        json.put(a_strKey, a_jsonValue);
        json.put(K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());
        json.put(K_MOFILER_API_LOCATION_KEY, a_jsonLocation);

        return postRequest(strURL, json, listener);
    }

    public int pushKeyArray(String a_strKey, JSONArray a_jsonArray, ApiListener listener) throws JSONException {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;
        JSONObject json = new JSONObject();
        json.put(a_strKey, a_jsonArray);
        json.put(K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());

        return postRequest(strURL, json, listener);
    }

    public int pushKeyValueStack(JSONArray jsonData, ApiListener listener) throws JSONException {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(K_MOFILER_API_USER_VALUES, jsonData);

        return postRequest(strURL, jsonObject, listener);
    }

    public int getValue(String a_strKey, String a_identityKey, String a_identityValue, ApiListener listener) throws JSONException {
        String manufacturer;
        try {
            manufacturer = URLEncoder.encode(MO_Device.getDeviceManufacturer(), "utf-8");
        }catch (UnsupportedEncodingException e) {
            manufacturer = MO_Device.getDeviceManufacturer();
        }

        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_get + manufacturer + "/" + a_identityKey + "/" + a_identityValue + "/" + a_strKey + "/";
        RESTApiRequest request = new RESTApiRequest(Request.Method.GET, strURL, null, getRequestHeaders(), listener);
        FetcherQueue.getInstance(mContext).addToRequestQueue(request.getJsonObjectRequest());
        return request.getRequestCode();
    }


    public void setServerURL(String a_strURL) {
        K_MOFILER_API_URL_BASE_POSTFIX = a_strURL;
        K_MOFILER_API_URL_BASE = "http://" + K_MOFILER_API_URL_BASE_POSTFIX;
    }

    public String getServerURL() {
        return K_MOFILER_API_URL_BASE_POSTFIX;
    }

    private JSONArray buildIdentityVector(Hashtable identityPairs) {
        Enumeration e = identityPairs.keys();
        JSONArray jsonToReturn = new JSONArray();

        try {
            while ( e.hasMoreElements() ) {
                String sKey = (String)e.nextElement();
                JSONObject tmpObj = new JSONObject();
                tmpObj.put(sKey, identityPairs.get(sKey));
                jsonToReturn.put(tmpObj);
            }
        }
        catch(JSONException ex) {
            ex.printStackTrace();
        }
        return jsonToReturn;
    }

    private JSONObject buildDeviceContextJSONObject(Context context) {
        JSONObject jsonobjInner = new JSONObject();

        try {
            jsonobjInner.put(Constants.K_MOFILER_API_DEVICE_CONTEXT_MANUFACTURER, MO_Device.getDeviceManufacturer());
            jsonobjInner.put(Constants.K_MOFILER_API_DEVICE_CONTEXT_MODELNAME, MO_Device.getDeviceModelName());
            jsonobjInner.put(Constants.K_MOFILER_API_DEVICE_CONTEXT_DISPLAYSIZE, MO_Device.getDisplaySize(context));
            jsonobjInner.put(Constants.K_MOFILER_API_DEVICE_CONTEXT_LOCALE, MO_Device.getLocale());
            jsonobjInner.put(Constants.K_MOFILER_API_DEVICE_CONTEXT_NETWORK, MO_Device.getConnectionString(context));

        	/* 2014-03-24 added into the body */
            jsonobjInner.put(Constants.K_MOFILER_API_HEADER_SESSIONID, mSessionId);
            jsonobjInner.put(Constants.K_MOFILER_API_HEADER_INSTALLID, mInstallationId);

        	/* 2014-11-16 added extras */
            jsonobjInner.put(Constants.K_MOFILER_API_DEVICE_CONTEXT_EXTRAS, MO_Device.getExtras(context, mUseVerboseExtras));

        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }

        return jsonobjInner;
    }

    private JSONObject addIdentityAndDeviceContext(JSONObject json) throws JSONException {
        json.put(Constants.K_MOFILER_API_IDENTITY, buildIdentityVector(mIdentities));
        json.put(Constants.K_MOFILER_API_DEVICE_CONTEXT, buildDeviceContextJSONObject(mContext));
        return json;
    }

    private JSONObject getBasicJsonWithKeyValue(String key, String value) throws JSONException {
        JSONObject json = new JSONObject();
        json.put(key, value);
        json.put(K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());
        return json;
    }

    private JSONObject getDecoratedJsonWithKeyValue(String key, String value) throws JSONException {
        JSONObject json = getBasicJsonWithKeyValue(key, value);
        addIdentityAndDeviceContext(json);
        return json;
    }

    private int postRequest(String url, JSONObject json, ApiListener listener){
        RESTApiRequest request = new RESTApiRequest(Request.Method.POST, url, json, getRequestHeaders(), listener);
        FetcherQueue.getInstance(mContext).addToRequestQueue(request.getJsonObjectRequest());
        return request.getRequestCode();
    }

    private Hashtable getRequestHeaders() {
        Hashtable headers = new Hashtable();

        // first put technical constants
        headers.put(Constants.K_MOFILER_API_HEADER_INSTALLID, mInstallationId);
        headers.put(Constants.K_MOFILER_API_HEADER_SESSIONID, String.valueOf(mSessionId));
        headers.put(Constants.K_MOFILER_API_HEADER_API_VERSION, Constants.K_MOFILER_API_VERSION);

        // now add application headers
        headers.putAll(mApplicationHeaders);

        return headers;
    }

}

