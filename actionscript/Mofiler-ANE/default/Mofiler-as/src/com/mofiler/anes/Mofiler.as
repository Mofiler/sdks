package com.mofiler.anes {
import com.mofiler.api.Constants;
import com.mofiler.api.Hashtable;
import com.mofiler.device.MO_Device;
import com.mofiler.exception.AppKeyNotSetException;
import com.mofiler.exception.IdentityNotSetException;


public class Mofiler{
	static private var instance:Mofiler= null;
	
	private var appKey:String;
	private var appName:String;
//	private var appVersion:String;
	private var cookie:String;
	private var identity:Hashtable;
	private var modev:MO_Device= new MO_Device();
	
	function Mofiler() {

	}
	
	static public function getInstance():Mofiler {
		if(instance == null)
			instance = new Mofiler();
		return instance;
	}

	public function getAppKey():String {
		return appKey;
	}

	public function setAppKey(appKey:String):void {
		this.appKey = appKey;
	}

	public function getAppName():String {
		return appName;
	}

	public function setAppName(appName:String):void {
		this.appName = appName;
	}

	public function getCookie():String {
		return cookie;
	}

	public function setCookie(cookie:String):void {
		this.cookie = cookie;
	}

	public function getURL():String {
		return modev.getURL();
	}

	public function setURL(a_URL:String):void {
		modev.setURL(a_URL);
	}

	public function setUseVerboseContext(state:Boolean):void {
		//not implemented
	}
	public function setUseLocation(state:Boolean):void {
		//not implemented
	}
	public function flushDataToMofiler():void {
		//not implemented
	}
	
	public function addIdentity(key:String, value:String):void {
		if (this.identity == null)
			this.identity = new Hashtable();
		
//		trace("adding identity k:"+key+" v:"+value);
		this.identity.put(key, value);
	}
	
	public function getIdentity(key:String):String {
		var objFound:String= null;
		if (this.identity != null)
			objFound = String(this.identity.get(key));
			
		return objFound;
	}
	
	public function injectValue(key:String, value:String, expireAfterMs:Number = 0):void {
		if (appKey != null){
			if (this.identity != null && (this.identity.size > 0)){
				addAllAvailableHeaders();
				modev.pushValue(identity, key, value, expireAfterMs);
			}
			else
				throw new IdentityNotSetException("Mofiler: user identity needs be set before you can send any values to the server");
		} 
		else
			throw new AppKeyNotSetException("Mofiler: api key needs be set before you can send any values to the server");
	}
	
	
	private function addAllAvailableHeaders():void {
		if (appKey != null)
			modev.addHeaderKeyValue(Constants.K_MOFILER_API_HEADER_APPKEY, appKey);
		if (appName != null)
			modev.addHeaderKeyValue(Constants.K_MOFILER_API_HEADER_APPNAME, appName);
//		if (appVersion != null)
//			modev.addHeaderKeyValue(Constants.K_MOFILER_API_HEADER_APPVERSION, appVersion);
		if (cookie != null)
			modev.addHeaderKeyValue(Constants.K_MOFILER_API_HEADER_COOKIE, cookie);
	}
}
}