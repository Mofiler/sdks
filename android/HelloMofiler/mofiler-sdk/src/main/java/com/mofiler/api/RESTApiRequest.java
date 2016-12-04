package com.mofiler.api;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Random;

public class RESTApiRequest {

    private int mRequestCode;
    private ApiListener mListener;
    private JsonObjectRequest mJsonObjectRequest;
    private JSONObject mOriginalPayload;

    public RESTApiRequest(int method, String url, JSONObject jsonRequest, ApiListener listener) {
        Random r = new Random();
        mRequestCode = r.nextInt();
        mListener = listener;
        mOriginalPayload = jsonRequest;
        mJsonObjectRequest = new JsonObjectRequest(method, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (mListener != null) {
                    mListener.onResponse(mRequestCode, response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mListener != null) {
                    mListener.onError(mRequestCode, mOriginalPayload, error);
                }
            }
        });
    }

    public JsonObjectRequest getJsonObjectRequest() {
        return mJsonObjectRequest;
    }

    public int getRequestCode() {
        return mRequestCode;
    }
}
