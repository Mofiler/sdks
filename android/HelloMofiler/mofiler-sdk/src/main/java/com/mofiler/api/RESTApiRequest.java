package com.mofiler.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

public class RESTApiRequest {

    private int mRequestCode;
    private ApiListener mListener;
    private JsonObjectRequest mJsonObjectRequest;
    private JSONObject mOriginalPayload;

    public RESTApiRequest(int method, String url, JSONObject jsonRequest, final Hashtable headers, ApiListener listener) {
        Random r = new Random();
        mRequestCode = r.nextInt();
        mListener = listener;
        mOriginalPayload = jsonRequest;

        Response.Listener<JSONObject> localSuccessListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (mListener != null) {
                    mListener.onResponse(mRequestCode, response);
                }
            }
        };

        Response.ErrorListener localErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mListener != null) {
                    mListener.onError(mRequestCode, mOriginalPayload, error);
                }
            }
        };

        mJsonObjectRequest = new JsonObjectRequest(method, url, jsonRequest, localSuccessListener, localErrorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (headers != null && headers.size() > 0)
                    return headers;
                else
                    return super.getHeaders();
            }
        };
    }

    public JsonObjectRequest getJsonObjectRequest() {
        return mJsonObjectRequest;
    }

    public int getRequestCode() {
        return mRequestCode;
    }

}
