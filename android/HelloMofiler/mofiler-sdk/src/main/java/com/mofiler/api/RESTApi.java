package com.mofiler.api;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

        JSONObject json = new JSONObject();
        json.put(a_strKey, a_strValue);
        json.put("expireAfter", expireAfter);
        json.put(K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());

        RESTApiRequest request = new RESTApiRequest(Request.Method.POST, strURL, json, listener);
        FetcherQueue.getInstance(mContext).addToRequestQueue(request.getJsonObjectRequest());
        return request.getRequestCode();
    }

    public int pushKeyValue(String a_strKey, String a_strValue, long expireAfter, JSONObject a_jsonLocation, ApiListener listener) throws JSONException {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;

        JSONObject json = new JSONObject();
        json.put(a_strKey, a_strValue);
        json.put("expireAfter", expireAfter);
        json.put(K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());
        json.put(K_MOFILER_API_LOCATION_KEY, a_jsonLocation);

        RESTApiRequest request = new RESTApiRequest(Request.Method.POST, strURL, json, listener);
        FetcherQueue.getInstance(mContext).addToRequestQueue(request.getJsonObjectRequest());
        return request.getRequestCode();
    }


    public int pushKeyValue(String a_strKey, String a_strValue, ApiListener listener) throws JSONException {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;

        JSONObject json = new JSONObject();
        json.put(a_strKey, a_strValue);
        json.put(K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());

        RESTApiRequest request = new RESTApiRequest(Request.Method.POST, strURL, json, listener);
        FetcherQueue.getInstance(mContext).addToRequestQueue(request.getJsonObjectRequest());
        return request.getRequestCode();
    }

    public int pushKeyValue(String a_strKey, String a_strValue, JSONObject a_jsonLocation, ApiListener listener) throws JSONException {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;

        JSONObject json = new JSONObject();
        json.put(a_strKey, a_strValue);
        json.put(K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());
        json.put(K_MOFILER_API_LOCATION_KEY, a_jsonLocation);

        RESTApiRequest request = new RESTApiRequest(Request.Method.POST, strURL, json, listener);
        FetcherQueue.getInstance(mContext).addToRequestQueue(request.getJsonObjectRequest());
        return request.getRequestCode();
    }

    public int pushKeyValue(String a_strKey, JSONObject a_jsonValue, ApiListener listener) throws JSONException {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;

        JSONObject json = new JSONObject();
        json.put(a_strKey, a_jsonValue);
        json.put(K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());

        RESTApiRequest request = new RESTApiRequest(Request.Method.POST, strURL, json, listener);
        FetcherQueue.getInstance(mContext).addToRequestQueue(request.getJsonObjectRequest());
        return request.getRequestCode();
    }

    public int pushKeyValue(String a_strKey, JSONObject a_jsonValue, JSONObject a_jsonLocation, ApiListener listener) throws JSONException {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;
        JSONObject json = new JSONObject();
        json.put(a_strKey, a_jsonValue);
        json.put(K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());
        json.put(K_MOFILER_API_LOCATION_KEY, a_jsonLocation);

        RESTApiRequest request = new RESTApiRequest(Request.Method.POST, strURL, json, listener);
        FetcherQueue.getInstance(mContext).addToRequestQueue(request.getJsonObjectRequest());
        return request.getRequestCode();
    }

    public int pushKeyArray(String a_strKey, JSONArray a_jsonArray, ApiListener listener) throws JSONException {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;
        JSONObject json = new JSONObject();
        json.put(a_strKey, a_jsonArray);
        json.put(K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());

        RESTApiRequest request = new RESTApiRequest(Request.Method.POST, strURL, json, listener);
        FetcherQueue.getInstance(mContext).addToRequestQueue(request.getJsonObjectRequest());
        return request.getRequestCode();
    }

    public int pushKeyValueStack(JSONArray jsonData, ApiListener listener) throws JSONException {
        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_inject;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(K_MOFILER_API_USER_VALUES, jsonData);

        RESTApiRequest request = new RESTApiRequest(Request.Method.POST, strURL, jsonObject, listener);
        FetcherQueue.getInstance(mContext).addToRequestQueue(request.getJsonObjectRequest());
        return request.getRequestCode();
    }

    public int getValue(String a_strKey, String a_identityKey, String a_identityValue, ApiListener listener) throws JSONException {
        String manufacturer;
        try {
            manufacturer = URLEncoder.encode(MO_Device.getDeviceManufacturer(), "utf-8");
        }catch (UnsupportedEncodingException e) {
            manufacturer = MO_Device.getDeviceManufacturer();
        }

        String strURL = K_MOFILER_API_URL_BASE + K_MOFILER_API_URL_METHOD_get + manufacturer + "/" + a_identityKey + "/" + a_identityValue + "/" + a_strKey + "/";
        RESTApiRequest request = new RESTApiRequest(Request.Method.GET, strURL, null, listener);
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

}

