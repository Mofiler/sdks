package com.mofiler;

import java.util.Hashtable;

import android.content.Context;

import com.mofiler.api.ApiListener;
import com.mofiler.api.Constants;
import com.mofiler.exception.AppKeyNotSetException;
import com.mofiler.exception.IdentityNotSetException;
import com.utils.database.DataBaseManager;


public final class Mofiler{
	static private Mofiler instance = null;
	private MofilerClient moClient;
	
	private String appKey;
	private String appName;
	private String appVersion;
	private String cookie;
	private Hashtable identity;
	private String strURL;
	private boolean useLocation = true; //defaults to true
	private boolean useVerboseContext = true; // defaults to true
	
	private Context context;

	private Mofiler(Context context) {
		//moClient = new MofilerClient(false);
		setContext(context);
		DataBaseManager.initializeDB(context);
		moClient = new MofilerClient(true, true, context);
	}

	static public Mofiler getInstance(Context context) {
		if(instance == null)
			instance = new Mofiler(context);
		return instance;
	}

	public void setContext(Context context){
		this.context = context;
	}

	public Context getContext(){
		return this.context;
	}
	
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getURL() {
		this.strURL = moClient.getURL();
		return strURL;
	}

	public void setURL(String a_URL) {
		this.strURL = a_URL;
		this.moClient.setURL(a_URL);
	}

	public boolean isUseLocation() {
		return useLocation;
	}

	public void setUseLocation(boolean useLocation) {
		this.useLocation = useLocation;
		moClient.setUseLocation(useLocation);
	}

	public boolean isUseVerboseContext() {
		return useVerboseContext;
	}

	public void setUseVerboseContext(boolean verbosecon) {
		this.useVerboseContext = verbosecon;
		moClient.setUseVerboseContext(this.useVerboseContext);
	}
	
	public void addIdentity(String key, String value) {
		if (this.identity == null)
			this.identity = new Hashtable();
		this.identity.put(key, value);
	}
	
	public String getIdentity(String key) {
		String objFound = null;
		if (this.identity != null)
			objFound = (String) this.identity.get(key);
			
		return objFound;
	}
	
	public void injectValue(String key, String value) throws AppKeyNotSetException, IdentityNotSetException{
		if (appKey != null){
			if (this.identity != null && (this.identity.size() > 0)){
				addAllAvailableHeaders();
				moClient.setIdentity(identity);
				moClient.pushValue(key, value);
			}
			else
				throw new IdentityNotSetException("Mofiler: user identity needs be set before you can send any values to the server");
		} 
		else
			throw new AppKeyNotSetException("Mofiler: api key needs be set before you can send any values to the server");
	}

	public void injectValue(String key, String value, long expireAfterMs) throws AppKeyNotSetException, IdentityNotSetException{
		if (appKey != null){
			if (this.identity != null && (this.identity.size() > 0)){
				addAllAvailableHeaders();
				moClient.setIdentity(identity);
				moClient.pushValue(key, value, expireAfterMs);
			}
			else
				throw new IdentityNotSetException("Mofiler: user identity needs be set before you can send any values to the server");
		} 
		else
			throw new AppKeyNotSetException("Mofiler: api key needs be set before you can send any values to the server");
	}
	

	public void getValue(String key, String identityKey, String identityValue) throws AppKeyNotSetException, IdentityNotSetException{
		if (appKey != null){
			if (this.identity != null && (this.identity.size() > 0)){
				addAllAvailableHeaders();
				moClient.setIdentity(identity);
				moClient.getValue(key, identityKey, identityValue);
			}
			else
				throw new IdentityNotSetException("Mofiler: user identity needs be set before you can send any values to the server");
		} 
		else
			throw new AppKeyNotSetException("Mofiler: api key needs be set before you can send any values to the server");
	}
	
	
	public void onDestroyApp(){
		flushDataToDisk();
	}

	public void flushDataToDisk(){
		moClient.doSaveDataToDisk();
	}
	
	public void flushDataToMofiler(){
		moClient.flushData();
	}
	
	public void setListener(ApiListener a_apilistener){
		moClient.setListener(a_apilistener);
	}
	
	private void addAllAvailableHeaders(){
		if (appKey != null)
			moClient.addHeaderKeyValue(Constants.K_MOFILER_API_HEADER_APPKEY, appKey);
		if (appName != null)
			moClient.addHeaderKeyValue(Constants.K_MOFILER_API_HEADER_APPNAME, appName);
		if (appVersion != null)
			moClient.addHeaderKeyValue(Constants.K_MOFILER_API_HEADER_APPVERSION, appVersion);
		if (cookie != null)
			moClient.addHeaderKeyValue(Constants.K_MOFILER_API_HEADER_COOKIE, cookie);
	}
	

}
