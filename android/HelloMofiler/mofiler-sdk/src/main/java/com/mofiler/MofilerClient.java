package com.mofiler;

import java.util.Hashtable;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.VolleyError;
import com.mofiler.api.ApiListener;
import com.mofiler.api.RESTApi;
import com.mofiler.daos.MofilerDao;
import com.mofiler.service.LocationService;
import com.mofiler.service.LocationServiceImpl;

import static com.mofiler.api.Constants.K_MOFILER_API_USER_VALUES;

// this class will hold the actual client / server logic, at the uppermost level, and will hold
// data in persistence until it deserves to be pushed to the server, in chunks, in a single push.
public class MofilerClient {

    private RESTApi restApi;
    private JSONArray jsonUserValues;
    private MofilerValueStack mofilerValues;
    private MofilerInstallationInfo mofilerInstallation;
    private static int K_MOFILER_STACK_LENGTH = 10;
    private static int K_MOFILER_MAX_STACK_LENGTH = 1000; //if we reach this, just eliminate all data to avoid bloat
    //private MofilerDeferredObject deferredObj;
    private boolean bUseDeferredSend = false;
    private String strURL;
    private Context context;
    private LocationService locationService;
    private boolean useLocation;
    private boolean useVerboseContext;
    private InjectListener injectListener = new InjectListener();

    public static String K_MOFILER_PROBE_KEY_NAME = "mofiler_probe";

    public MofilerClient(boolean a_bUseDeferredSend, boolean a_bUseLocation, Context context) {
        this.context = context;

        loadDataFromStorage();

        restApi = new RESTApi(mofilerInstallation.getInstallationId());
        restApi.setContext(context);
        this.bUseDeferredSend = a_bUseDeferredSend;
        this.useLocation = a_bUseLocation;
        locationService = new LocationServiceImpl(context);

        if (a_bUseLocation) {
            locationService.startProvider();
        }

    }

    public void setContext(Context context) {
        this.context = context;
        restApi.setContext(context);
    }

    public boolean isUseLocation() {
        return useLocation;
    }

    public void setUseLocation(boolean useLocation) {
        this.useLocation = useLocation;
        locationService.stopProvider(); //stop it if it was working for some reason
        if (useLocation) { //only start it if they're requesting to get location updates
            locationService.startProvider();
        }
    }

    public boolean isUseVerboseContext() {
        return useVerboseContext;
    }

    public void setUseVerboseContext(boolean verbosecon) {
        this.useVerboseContext = verbosecon;
        this.restApi.setUseVerboseDeviceContext(verbosecon);
    }

    public void addHeaderKeyValue(String header, String value) {
        restApi.addPropertyKeyValuePair(header, value);
    }

    private JSONArray discardKeyValueIfExistsInArray(JSONArray a_jsonArrToLookIn, String key, String value) {
        JSONArray newJsonArray = new JSONArray();

        try {
            for (int i = 0; i < a_jsonArrToLookIn.length(); i++) {
                JSONObject tmpObj = a_jsonArrToLookIn.getJSONObject(i);
                if (tmpObj.has(key) && !MofilerClient.K_MOFILER_PROBE_KEY_NAME.equals(key)) {
                    //discard this object from array and put the new key value instead
                    //Except it is the mofiler_probe data, then please keep it and send it to the server.
                } else {
                    newJsonArray.put(tmpObj);
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return newJsonArray;
    }

    private JSONObject makeExtrasObject(String extras) {
        JSONObject extrasObj = null;
        try {
            extrasObj = new JSONObject(extras);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return extrasObj;
    }

    private void internal_populateVector(String key, String value) throws JSONException {
        jsonUserValues = discardKeyValueIfExistsInArray(jsonUserValues, key, value);
        JSONObject tmpObj = new JSONObject();

        if (MofilerClient.K_MOFILER_PROBE_KEY_NAME.equals(key)) {
            JSONObject jsobj = makeExtrasObject(value);
            tmpObj.put(key, (jsobj == null) ? value : jsobj);
        } else {
            tmpObj.put(key, value);
        }

        tmpObj.put(RESTApi.K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());

        if (useLocation)
            tmpObj.put(RESTApi.K_MOFILER_API_LOCATION_KEY, locationService.getLastKnownLocationJSON());

        jsonUserValues.put(tmpObj);
    }

    private void internal_populateVector(String key, String value, long expireAfterMs) throws JSONException {
        jsonUserValues = discardKeyValueIfExistsInArray(jsonUserValues, key, value);
        JSONObject tmpObj = new JSONObject();

        if (MofilerClient.K_MOFILER_PROBE_KEY_NAME.equals(key)) {
            JSONObject jsobj = makeExtrasObject(value);
            tmpObj.put(key, (jsobj == null) ? value : jsobj);
        } else {
            tmpObj.put(key, value);
        }

        tmpObj.put("expireAfter", expireAfterMs);
        tmpObj.put(RESTApi.K_MOFILER_API_TIMESTAMP_KEY, System.currentTimeMillis());

        if (useLocation)
            tmpObj.put(RESTApi.K_MOFILER_API_LOCATION_KEY, locationService.getLastKnownLocationJSON());

        jsonUserValues.put(tmpObj);
    }

    public void pushValue(String key, String value) {
        if (!bUseDeferredSend) {
            pushValue_single(key, value);
        } else {
            pushValue_array(key, value);
        }
    }

    private void pushValue_array_internal(String key, String value, long expireAfterMs, boolean bUseExpireAfter) {
        try {

            //we need to load from DB and save to DB on each call, as we might be coming from another process with
            //verbose extras being triggered by our service.
            loadDataFromStorage();

            if (((jsonUserValues.length() % K_MOFILER_STACK_LENGTH) != 0) || (jsonUserValues.length() == 0)) {
                if (bUseExpireAfter)
                    internal_populateVector(key, value, expireAfterMs);
                else
                    internal_populateVector(key, value);
                doSaveDataToDisk();

            } else if (jsonUserValues.length() > K_MOFILER_MAX_STACK_LENGTH) {
                //send this and clean all
                restApi.pushKeyValueStack(jsonUserValues, injectListener);
                jsonUserValues = new JSONArray();
                mofilerValues.setJsonStack(jsonUserValues);
                if (bUseExpireAfter)
                    internal_populateVector(key, value, expireAfterMs);
                else
                    internal_populateVector(key, value);
                doSaveDataToDisk();
            } else {
                //send stack data and then push the new data into the stack
                //deferredObj = new MofilerDeferredObject(key, value);
                restApi.pushKeyValueStack(jsonUserValues, injectListener);
                doSaveDataToDisk();
                jsonUserValues = new JSONArray();
                if (bUseExpireAfter)
                    internal_populateVector(key, value, expireAfterMs);
                else
                    internal_populateVector(key, value);
                doSaveDataToDisk();
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private void pushValue_array(String key, String value) {
        pushValue_array_internal(key, value, -1, false);
    }

    private void pushValue_array(String key, String value, long expireAfterMs) {
        pushValue_array_internal(key, value, expireAfterMs, true);
    }

    private void pushValue_single(String key, String value) {
        try {
            if (useLocation)
                restApi.pushKeyValue(key, value, locationService.getLastKnownLocationJSON(), injectListener);
            else
                restApi.pushKeyValue(key, value, injectListener);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void pushValue(String key, String value, long expireAfterMs) {

        if (!bUseDeferredSend) {
            pushValue_single(key, value, expireAfterMs);
        } else {
            pushValue_array(key, value, expireAfterMs);
        }
    }

    private void pushValue_single(String key, String value, long expireAfterMs) {
        try {
            if (useLocation)
                restApi.pushKeyValue(key, value, expireAfterMs, locationService.getLastKnownLocationJSON(), injectListener);
            else
                restApi.pushKeyValue(key, value, expireAfterMs, injectListener);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void getValue(String key, String identityKey, String identityValue, ApiListener listener) {

        try {
            restApi.getValue(key, identityKey, identityValue, listener);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void setIdentity(Hashtable hashIds) {
        restApi.setIdentity(hashIds);
    }

    public String getURL() {
        this.strURL = this.restApi.getServerURL();
        return this.strURL;
    }

    public void setURL(String a_URL) {
        this.strURL = a_URL;
        this.restApi.setServerURL(a_URL);
    }

    public void flushData() {
        try {
            restApi.pushKeyValueStack(jsonUserValues, injectListener);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    synchronized public void loadDataFromStorage() {
        Vector vectTmp = MofilerDao.getCurrentDataInDB(context, 1);

        if (vectTmp != null) {
            mofilerInstallation = (MofilerInstallationInfo) vectTmp.elementAt(0);
            mofilerValues = (MofilerValueStack) vectTmp.elementAt(1);
        }

        /* instantiate mofiler stack */
        if (mofilerValues == null) {
            jsonUserValues = new JSONArray();
            mofilerValues = new MofilerValueStack("[]");
        } else {
            try {
                jsonUserValues = mofilerValues.getJsonStack();
            } catch (Exception ex) {
                ex.printStackTrace();
                jsonUserValues = new JSONArray();
                mofilerValues = new MofilerValueStack(null);
            }
        }

        /* instantiate mofiler installation id */
        if (mofilerInstallation == null) {
        	/* if it did not exist, generate a new installation ID */
            mofilerInstallation = new MofilerInstallationInfo();
            mofilerInstallation.generateId(true);
            //save it immediately
            MofilerDao.saveCurrentDataInDB(context, mofilerInstallation, mofilerValues);
        }
    }

    synchronized public void doSaveDataToDisk() {
        mofilerValues.setJsonStack(jsonUserValues);
        MofilerDao.saveCurrentDataInDB(context, mofilerInstallation, mofilerValues);
    }

    private JSONArray concatArray(JSONArray arr1, JSONArray arr2)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (int i = 0; i < arr1.length(); i++) {
            result.put(arr1.get(i));
        }
        for (int i = 0; i < arr2.length(); i++) {
            result.put(arr2.get(i));
        }
        return result;
    }


    private class InjectListener implements ApiListener {

        @Override
        public void onResponse(int reqCode, JSONObject response) {
            // no op, this successful listener is not used for injects
            if (bUseDeferredSend) {
                //all went good, so erase current value stack
                jsonUserValues = new JSONArray();
                mofilerValues.setJsonStack(jsonUserValues);
                doSaveDataToDisk();
            }
        }

        @Override
        public void onError(int reqCode, JSONObject originalPayload, VolleyError error) {
            // error listener only used for injects
            if (bUseDeferredSend) {
                try {
                    JSONArray jsonTmpArray = originalPayload.getJSONArray(K_MOFILER_API_USER_VALUES);
                    if (jsonTmpArray != null && jsonTmpArray.length() > K_MOFILER_MAX_STACK_LENGTH) {
                        //clean all
                        jsonUserValues = new JSONArray();
                        mofilerValues.setJsonStack(jsonUserValues);
                        doSaveDataToDisk();
                    } else {
                        jsonUserValues = concatArray(jsonUserValues, jsonTmpArray);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
