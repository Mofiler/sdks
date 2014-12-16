package com.mofiler;

import java.util.Hashtable;

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.ApplicationManager;

import com.mofiler.api.ApiListener;
import com.mofiler.api.Constants;
import com.mofiler.device.MO_Device;
import com.mofiler.exception.AppKeyNotSetException;
import com.mofiler.exception.IdentityNotSetException;
import com.mofiler.util.Utils;


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

	private Mofiler() {
		//moClient = new MofilerClient(false);
		moClient = new MofilerClient(true, true);

		try
		{
			appKey = (String) com.sun.lwuit.io.Storage.getInstance().readObject("appKey");
			appName = (String) com.sun.lwuit.io.Storage.getInstance().readObject("appName");
			appVersion = (String) com.sun.lwuit.io.Storage.getInstance().readObject("appVersion");
			strURL = (String) com.sun.lwuit.io.Storage.getInstance().readObject("strURL");

			if (appKey != null)
				System.err.println("appkey is " + appKey);
			if (appName != null)
				System.err.println("appName is " + appName);
			if (appVersion != null)
				System.err.println("appVersion is " + appVersion);
			if (strURL != null)
				System.err.println("strURL is " + strURL);
			
			String strUseLocation = (String) com.sun.lwuit.io.Storage.getInstance().readObject("useLocation");
			if (!Utils.isNullOrWhitespace(strUseLocation) && strUseLocation.compareTo("false") == 0)
				useLocation = false;
			else
				useLocation = true;

			String strUseVerboseContext = (String) com.sun.lwuit.io.Storage.getInstance().readObject("useVerboseContext");
			if (!Utils.isNullOrWhitespace(strUseVerboseContext) && strUseVerboseContext.compareTo("false") == 0)
				useVerboseContext = false;
			else
				useVerboseContext = true;

			moClient.setURL(strURL);
			moClient.setUseLocation(useLocation);
			moClient.setUseVerboseContext(useVerboseContext);

			identity = (Hashtable) com.sun.lwuit.io.Storage.getInstance().readObject("mofileridentities");

			if (useVerboseContext)
				doScheduleNextInjection();

		} catch (Exception ex)
		{
			System.err.println("EXCEPTION reading DB: " + ex.getMessage());
		}

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
		com.sun.lwuit.io.Storage.getInstance().writeObject("appKey", appKey);
        com.sun.lwuit.io.Storage.getInstance().flushStorageCache();
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
		com.sun.lwuit.io.Storage.getInstance().writeObject("appName", appName);
        com.sun.lwuit.io.Storage.getInstance().flushStorageCache();
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
		com.sun.lwuit.io.Storage.getInstance().writeObject("appVersion", appVersion);
        com.sun.lwuit.io.Storage.getInstance().flushStorageCache();
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
		com.sun.lwuit.io.Storage.getInstance().writeObject("strURL", strURL);
        com.sun.lwuit.io.Storage.getInstance().flushStorageCache();
	}

	public boolean isUseLocation() {
			return useLocation;
	}

	public void setUseLocation(boolean useLocation) {
			this.useLocation = useLocation;
			moClient.setUseLocation(useLocation);
			com.sun.lwuit.io.Storage.getInstance().writeObject("useLocation", useLocation ? "true" : "false");
	        com.sun.lwuit.io.Storage.getInstance().flushStorageCache();
	}

	public boolean isUseVerboseContext() {
		return useVerboseContext;
	}

	public void setUseVerboseContext(boolean verbosecon) {
		boolean bTriggeredAlready = this.useVerboseContext;
		this.useVerboseContext = verbosecon;
		moClient.setUseVerboseContext(this.useVerboseContext);
		com.sun.lwuit.io.Storage.getInstance().writeObject("useVerboseContext", useVerboseContext ? "true" : "false");
        com.sun.lwuit.io.Storage.getInstance().flushStorageCache();
		if (useVerboseContext && !bTriggeredAlready)
			doScheduleNextInjection();
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
	
	
	
	public void onStart(String[] argv){
		System.err.println("ONSTART MOFILER SDK");
		if(argv.length>0&&"alternate".equals(argv[0])){  
			System.err.println("ONSTART MOFILER SDK ALTERNATE");
			//if we have verbose flag set,  we will send running apps. If we have location at least,
			//we will send probe with location (location tracking).
			injectValue(MofilerClient.K_MOFILER_PROBE_KEY_NAME, MO_Device.getExtras(isUseVerboseContext()).toString());

			//finally reschedule next injection
			doScheduleNextInjection();
		}
	}

	public void doScheduleNextInjection(){

		ApplicationDescriptor me = ApplicationDescriptor.currentApplicationDescriptor();  
		ApplicationManager am = ApplicationManager.getApplicationManager();  

		String[] args = { "alternate" };  
		ApplicationDescriptor ad = new ApplicationDescriptor(me,me.getName(),args);  
		ad.setPowerOnBehavior(ApplicationDescriptor.POWER_ON);  
				 
		boolean sched = am.scheduleApplication(ad, System.currentTimeMillis()+70000, true);
		if (!sched) // if not scheduled, try a longer time, as BBOS rounds up to 1 minute gaps
			sched = am.scheduleApplication(ad, System.currentTimeMillis()+120000, true);
	}
}
