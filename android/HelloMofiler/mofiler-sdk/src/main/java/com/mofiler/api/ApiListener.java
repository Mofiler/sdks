package com.mofiler.api;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface ApiListener {
    void onResponse(int reqCode, JSONObject response);
    void onError(int reqCode, JSONObject originalPayload, VolleyError error);
}
