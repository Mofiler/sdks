MF.RESTApi = function(options) {
		var applicationHeaders,
		identity,
		values = [],
		sessionId,
		deviceInfo,
		config = {
			installationId: undefined,
			operation: undefined,
			url: undefined,
			scope: undefined,
			callback: undefined
		},
		body;

		MF.apply(config,options);

		var base_url = 'http://' + config.url;

		// var strSessionIDValue=Math.random()*1e25|0;
		// console.log(strSessionIDValue);

		var iExceptionCounter=0;

		var Math_random = Math.random;
		for (var i=1e6, lookupTable=[]; i--;) lookupTable.push(Math_random()*1e25|0);
	    
	    function lookup() {
	      return ++i >= lookupTable.length ? lookupTable[i=0] : lookupTable[i];
	    }

	    sessionId = lookup();

		function constructHeaders(){
			if (MF.Object.isEmpty(applicationHeaders))
				applicationHeaders = {};
			applicationHeaders[MF.K_MOFILER_API_HEADER_INSTALLID] = config.installationId;
			applicationHeaders[MF.K_MOFILER_API_HEADER_SESSIONID] = sessionId;
			applicationHeaders[MF.K_MOFILER_API_HEADER_API_VERSION] = MF.K_MOFILER_API_VERSION;

			applicationHeaders[MF.K_MOFILER_API_HEADER_NOISELEVEL] = iExceptionCounter + '';
		};

		function buildDeviceContext(){
			if (MF.Object.isEmpty(deviceInfo))
				deviceInfo = {};

			deviceInfo[MF.K_MOFILER_API_DEVICE_CONTEXT_MANUFACTURER] = 'Browser';
			deviceInfo[MF.K_MOFILER_API_DEVICE_CONTEXT_MODELNAME] = ''; //user.agent
			deviceInfo[MF.K_MOFILER_API_DEVICE_CONTEXT_DISPLAYSIZE] = '';
			deviceInfo[MF.K_MOFILER_API_DEVICE_CONTEXT_LOCALE] = '';
			deviceInfo[MF.K_MOFILER_API_DEVICE_CONTEXT_NETWORK] = '';
			deviceInfo[MF.K_MOFILER_API_HEADER_SESSIONID] = sessionId;
			deviceInfo[MF.K_MOFILER_API_HEADER_INSTALLID] = config.installationId;
		};

		function createCORSRequest(method, url) {
		  var xhr = new XMLHttpRequest();

		  if ("withCredentials" in xhr) {

		    // Check if the XMLHttpRequest object has a "withCredentials" property.
		    // "withCredentials" only exists on XMLHTTPRequest2 objects.
		    xhr.open(method, url, true);

		  } else if (typeof XDomainRequest != "undefined") {

		    // Otherwise, check if XDomainRequest.
		    // XDomainRequest only exists in IE, and is IE's way of making CORS requests.
		    xhr = new XDomainRequest();
		    xhr.open(method, url);

		  } else {

		    // Otherwise, CORS is not supported by the browser.
		    xhr = null;

		  }
		  // xhr.setRequestHeader("Origin", "*");
		  xhr.setRequestHeader("Content-Type", "application/json");
		  return xhr;
		}

		function doPostOperation(values){
			operation = MF.RESTApi.K_MOFILER_API_METHOD_NAME_inject;

			buildDeviceContext();

    		body = {};
			body[MF.K_MOFILER_API_DEVICE_CONTEXT] = deviceInfo;
			body[MF.K_MOFILER_API_IDENTITY] = identity;
			body[MF.K_MOFILER_API_USER_VALUES] = values;

			doRequest();
		}

		function doGetOperation(key, identityKey, identityValue){
			operation = MF.RESTApi.K_MOFILER_API_METHOD_NAME_get;

			var params = 'Browser' + '/' + identityKey + '/' + identityValue + '/' + key + '/';
			doRequest(params);
		}

		function doRequest(params){
			var strURL = base_url + MF.RESTApi.K_MOFILER_API_URL;

			if (params) 
				strURL += params;
			
			constructHeaders();

			var xhr = (operation === MF.RESTApi.K_MOFILER_API_METHOD_NAME_inject) ? createCORSRequest('POST', strURL) : createCORSRequest('GET', strURL);
			if (!xhr) {
			  throw new Error('CORS not supported');
			}

			for (headers in applicationHeaders) {
				xhr.setRequestHeader(headers, applicationHeaders[headers]);
			};

			xhr.onload = function() {
				if (config.callback){
			 		config.callback.call(config.scope,xhr,operation,true);

				}
			};

			xhr.onerror = function() {
				if (config.callback)
			  		config.callback.call(config.scope,xhr,operation,false, body);
			};

			if (!MF.Object.isEmpty(body))
				xhr.send(JSON.stringify(body));
			else
				xhr.send();
		}

		return {
			addPropertyKeyValuePair: function (header, value) {
				if (MF.Object.isEmpty(applicationHeaders))
					applicationHeaders = {};
				applicationHeaders[header] = value;
			},

			/*useThreadedConnections: function (a_bUseThreadedConns, a_bBlockThem) {
				console.log('init RESTApi with bThreadedConnections: ' + a_bUseThreadedConns + ' and bUseBlockingThreads: ' + a_bBlockThem)
			},

			addMethodListener: function (a_strMethodToListenTo, listener) {
				console.log('addMethodListener with a_strMethodToListenTo: ' + a_strMethodToListenTo + ' and listener: ' + listener)
			},*/

			setServerURL: function (url){
		    	config.url = url;
		        base_url = "http://" + url;
	    	},

			setIdentity: function (a_identity){
		    	identity = a_identity
	    	},

	    	getValue: function(key, identityKey, identityValue){
	    		doGetOperation(key, identityKey, identityValue);
	    	},

	    	pushKeyValueStack: function(values){
	    		doPostOperation(values);
	    	},

	    	pushKeyValue: function(key,value,opts){
	    		var val = {};
	    		val[key] = value;
	    		if (opts.hasOwnProperty('expireAfterMs')) va["expireAfter"] = opts['expireAfterMs'];
	    		if (opts.hasOwnProperty('location')) val[MF.RESTApi.K_MOFILER_API_LOCATION_KEY] = MF.LocationService.getLastKnownLocation();
	    		val[MF.RESTApi.K_MOFILER_API_TIMESTAMP_KEY] = new Date().getTime();
	    		values.push(val);

	    		doPostOperation(values);
	    	}
		}
};

//static values
// MF.RESTApi.K_MOFILER_API_URL_METHOD_inject  = "/api/values/";
// MF.RESTApi.K_MOFILER_API_URL_METHOD_get     = "/api/values/";
MF.RESTApi.K_MOFILER_API_URL     = "/api/values/";

MF.RESTApi.K_MOFILER_API_METHOD_NAME_inject = 'POST_VALUES';
MF.RESTApi.K_MOFILER_API_METHOD_NAME_get 	= 'GET_VALUES';
MF.RESTApi.K_MOFILER_API_TIMESTAMP_KEY      = 'tstamp';
MF.RESTApi.K_MOFILER_API_LOCATION_KEY     	= "location";