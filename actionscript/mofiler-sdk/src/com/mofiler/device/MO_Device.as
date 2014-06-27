package com.mofiler.device {
	import com.mofiler.api.Constants;
	import com.mofiler.api.Hashtable;
	
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.net.NetworkInfo;
	import flash.net.NetworkInterface;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
	import flash.net.URLRequestHeader;
	import flash.net.URLRequestMethod;
	import flash.system.Capabilities;

public class MO_Device {

	private var headers:Hashtable;
	
	public function MO_Device()
	{
		//generic constructor
		/*
		 * This class should represent everything that can be informed by a device, such as:
		 * -network interface being used
		 * -display dimensions
		 * -device name
		 * -device manufacturer
		 * -colors
		 * -audio capabilities
		 * -touchscreen / keyboard capabilities
		 * -orientation, accelerometer, etc.
		 */
		headers = new Hashtable();
	}
	
	public function getDisplaySize():String {
            return Capabilities.screenResolutionX + "x" + Capabilities.screenResolutionY;
	}
	
	
	public static function getDeviceManufacturer():String {
		//return "Flash";
		return Capabilities.manufacturer;
	}
	
	public static function getDeviceModelName():String {
            var devModelName:String= Capabilities.manufacturer;
            if (devModelName == null)
                devModelName = "";
            return devModelName;
	}
	
	public static function getLocale():String {
		var devLocale:String= Capabilities.language;
		if (devLocale == null)
			devLocale = "";
		return devLocale;
	}
	public static function getOS():String {
		return Capabilities.os;
	}

	public static var K_MOFILER_API_URL_METHOD_inject:String            = "/api/values/";
	private var strURL:String;

	public function pushValue(identity:Hashtable, key:String, value:String, expireAfterMs:Number):void
	{

		var loader:URLLoader = new URLLoader();
		var request:URLRequest = new URLRequest("http://"+getURL()+K_MOFILER_API_URL_METHOD_inject);
		request.method = URLRequestMethod.POST;
		request.contentType = "application/json";
		
		var arrHeader:Array = new Array();

		arrHeader.push(new URLRequestHeader("Accept-Encoding", "application/gzipped"));
		
		var k:Array = headers.getAllKeys();
		var v:Array = headers.getAllItems();
		for (var i:int=0;i<k.length;i++){
			arrHeader.push(new URLRequestHeader(k[i], v[i]));
		}
		
		request.requestHeaders = arrHeader;
		
		
		var strData:String = "";
		k = identity.getAllKeys();
		v = identity.getAllItems();
		strData += "{ \""+Constants.K_MOFILER_API_IDENTITY+"\": [ {";
		for (var j:int=0;j<k.length;j++){
			if(j>0) strData += ",";
			strData += "\""+k[j]+"\": \""+v[j]+"\"";
		}
		strData += "} ],";

		strData += "\""+Constants.K_MOFILER_API_USER_VALUES+"\":";
		strData += "[ { \"tstamp\": "+new Date().getTime()+", \""+key+"\": \""+value+"\" } ], ";
		strData += "\""+Constants.K_MOFILER_API_DEVICE_CONTEXT+"\": {";
//		strData += "{ \"X-Mofiler-SessionID\": \"-1737187713851127507\",";
		strData += "\""+Constants.K_MOFILER_API_DEVICE_CONTEXT_DISPLAYSIZE+"\": \""+getDisplaySize()+"\",";
		strData += "\""+Constants.K_MOFILER_API_DEVICE_CONTEXT_MODELNAME+"\": \""+getDeviceModelName()+"\",";
		strData += "\""+Constants.K_MOFILER_API_DEVICE_CONTEXT_OS+"\": \""+getOS()+"\",";
		strData += "\""+Constants.K_MOFILER_API_DEVICE_CONTEXT_NETWORK+"\": \""; 
		if (NetworkInfo.isSupported) {
			var network:NetworkInfo = NetworkInfo.networkInfo;
			for each (var object:NetworkInterface in network.findInterfaces()) {
				if (object.hardwareAddress) {
					strData += object.displayName+":"+object.hardwareAddress+";"; 
				}
			}
		}
		strData += "\","; 
		
		strData += "\""+Constants.K_MOFILER_API_DEVICE_CONTEXT_MANUFACTURER+"\": \""+getDeviceManufacturer()+"\",";
//		strData += "\"X-Mofiler-InstallID\": \"8171096689745553707\",";
		strData += "\""+Constants.K_MOFILER_API_DEVICE_CONTEXT_LOCALE+"\": \""+getLocale()+"\" } }";

		request.data = strData;
		
		loader.dataFormat = URLLoaderDataFormat.TEXT; 
		loader.load(request);
		loader.addEventListener(Event.COMPLETE, textLoadComplete);
		loader.addEventListener(IOErrorEvent.IO_ERROR, error_Handler1);
		function error_Handler1(error_Event:IOErrorEvent):void {
			trace("url error = "+error_Event.text);
			
		}
		///////;
		function textLoadComplete(event:Event):void {
			var v_data:String = loader.data;
			trace("result OK: "+v_data);
		}

		
	}
	
	public function addHeaderKeyValue(header:String, value:String):void
	{
		headers.put(header, value);
	}
	
	public function getURL():String {
		return strURL;
	}
	
	public function setURL(a_URL:String):void {
		this.strURL = a_URL;
	}
	

}
}