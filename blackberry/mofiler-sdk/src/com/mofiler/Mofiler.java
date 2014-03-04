package com.mofiler;

import com.mofiler.api.Constants;
import com.mofiler.exception.AppKeyNotSetException;


public final class Mofiler{
	static private Mofiler instance = null;
	private MofilerClient moClient;
	
	private String appKey;
	private String appName;
	private String appVersion;
	private String cookie;

	private Mofiler() {
		moClient = new MofilerClient();
	}
	
	static public Mofiler getInstance() {
		if(instance == null)
			instance = new Mofiler();
		return instance;
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
	
	
	public void injectValue(String key, String value) throws AppKeyNotSetException{
		if (appKey != null){
			addAllAvailableHeaders();
			moClient.pushValue(key, value);
		} 
		else {
			throw new AppKeyNotSetException("mofiler api key needs be set before you can send any values to the server");
		}
	}

	public void injectValue(String key, String value, long expireAfterMs) throws AppKeyNotSetException{
		if (appKey != null){
			addAllAvailableHeaders();
			moClient.pushValue(key, value, expireAfterMs);
		} 
		else {
			throw new AppKeyNotSetException("mofiler api key needs be set before you can send any values to the server");
		}
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
