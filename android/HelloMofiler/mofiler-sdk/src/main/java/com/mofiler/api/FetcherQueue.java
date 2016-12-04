package com.mofiler.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class FetcherQueue {
    private static FetcherQueue mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    private FetcherQueue (Context context){
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized FetcherQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FetcherQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
