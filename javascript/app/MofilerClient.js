MF.MofilerClient = function(opts) {
		
		var restApi,
		jsonUserValues,
		jsonAuxUserValues,
		K_MOFILER_STACK_LENGTH = 10,
		K_MOFILER_MAX_STACK_LENGTH = 1000, //if we reach this, just eliminate all data to avoid bloat
		config = {
			useDeferredSend: undefined,
			useLocation: undefined,
			callback: undefined,
			scope: undefined,
			url: undefined,
		},
		locationService;

		var auxValues = {};

		MF.apply(config,opts);

		loadDataFromStorage = function(){
			console.log('loadDataFromStorage');
			if (!MF.supportsLocalStorage()) { return false; }
			var data = localStorage.getItem("user.values");
			jsonUserValues = JSON.parse(data);
		}();

		restApi = new MF.RESTApi(
		{
			installationId: 1,
			url: config.url,
			callback: function(response,operation,success,values){
				if (success){
					if (operation === MF.RESTApi.K_MOFILER_API_METHOD_NAME_inject){
						if (config.useDeferredSend){
		        			//all went good, so erase current value stack
							// jsonUserValues = [];
							//concat the 
							doSaveData();
							// jsonUserValues = jsonAuxUserValues.slice();
							// jsonAuxUserValues = [];
							// console.log(jsonUserValues);
		        		}
		        	}
		        	if (config.callback)
		        		config.callback.call(config.scope,response.responseText,operation,success);
				}else{
					config.callback.call(config.scope,response.responseText,operation,success);

					if (config.useDeferredSend){
	        			if (values.length > K_MOFILER_MAX_STACK_LENGTH){
							jsonUserValues = [];
							doSaveData();
		        		}else{
		        			//asi o al reves?
		        			jsonUserValues = values.concat(jsonUserValues);
		        			// jsonUserValues = jsonUserValues.concat(values);
		        		}
		        	}
				}
			},
			scope: this
		});

        /*if (config.useLocation){
            // locationService = new LocationService(180, LocationService.K_GPSHANDLER_MODE_ASSIST); //setup 30 secs timeout for retrieving current location
            // locationService.startProvider();
        }*/

        pushValue_single = function(key, value, expireAfterMs){
			// opts = opts || {};
			// opts['expireAfter'] = expireAfterMs;
			if (config.useLocation){
				MF.LocationService.startProvider(function(){
					// opts['location'] = MF.LocationService.getLastKnownLocation();
					// restApi.pushKeyValue(key, value, opts);
					pushValue_single_internal(key, value, expireAfterMs);
				});
			}
			else
				pushValue_single_internal(key, value, expireAfterMs);
				// restApi.pushKeyValue(key, value, opts);
		};

		pushValue_single_internal = function(key,value, expireAfterMs){
			var tmpObj = {};
			tmpObj[key] = value;
			if (typeof expireAfterMs !== "undefined") tmpObj.put("expireAfter", expireAfterMs);
			tmpObj[MF.RESTApi.K_MOFILER_API_TIMESTAMP_KEY] = new Date().getTime();
			if (config.useLocation)
				tmpObj[MF.RESTApi.K_MOFILER_API_LOCATION_KEY] = MF.LocationService.getLastKnownLocation();
			jsonUserValues.push(tmpObj);
			restApi.pushKeyValueStack(jsonUserValues);
			jsonUserValues = [];
		};

		pushValue_array = function(key,value,opts){
			opts = opts || {};
			if (config.useLocation){
				MF.LocationService.startProvider(function(){
					pushValue_array_internal(key, value, opts['expireAfterMs']);		
				});
			}else{
				pushValue_array_internal(key, value, opts['expireAfterMs']);
			}
		};

		pushValue_array_internal = function(key,value, expireAfterMs){
			var l = (jsonUserValues)? jsonUserValues.length : 0;
			if ( (MF.isEmpty(jsonUserValues)) || ((jsonUserValues.length % K_MOFILER_STACK_LENGTH) !== 0) ) { 
				internal_populateVector(key, value, expireAfterMs);
			}else if (jsonUserValues.length > K_MOFILER_MAX_STACK_LENGTH){
				restApi.pushKeyValueStack(jsonUserValues);
				jsonUserValues = [];
				internal_populateVector(key, value, expireAfterMs);
				doSaveData();
			}else{
				restApi.pushKeyValueStack(jsonUserValues); //post to server is async, need to ad values to a temp array
				doSaveData();
				jsonUserValues = [];
				internal_populateVector(key, value, expireAfterMs);
			}
		};

		internal_populateVector = function(key, value, expireAfterMs){
			if (MF.isEmpty(jsonUserValues)) 
				jsonUserValues = [];
			
			if (!auxValues.hasOwnProperty(key)){
				auxValues[key] = value; //aux object to keep track of the added keys
				
				var tmpObj = {};
				tmpObj[key] = value;
				if (typeof expireAfterMs !== "undefined") tmpObj.put("expireAfter", expireAfterMs);
				tmpObj[MF.RESTApi.K_MOFILER_API_TIMESTAMP_KEY] = new Date().getTime();
				if (config.useLocation)
					tmpObj[MF.RESTApi.K_MOFILER_API_LOCATION_KEY] = MF.LocationService.getLastKnownLocation();
				jsonUserValues.push(tmpObj);
			}
		};

		doSaveData = function(){
			//save data to localStorage
			if (!MF.supportsLocalStorage()) { return false; }
			localStorage.setItem('user.values', JSON.stringify(jsonUserValues));
		};

		//return
		return {
			/*
			isUseLocation: function () {
		    	return useLocation;
		    },
			*/
		    getValue: function (key, identityKey, identityValue) {
		    	restApi.getValue(key, identityKey, identityValue);
		    },

		    /*
		    getURL: function () {
		    	strURL = restApi.getServerURL();
				return strURL;
		    },
			*/
			addHeaderKeyValue: function (header, value) {
		    	restApi.addPropertyKeyValuePair(header, value);	
		    },

		    setIdentity: function (hashIds) {
		    	restApi.setIdentity(hashIds);
		    },

		    setURL: function (url) {
		    	strURL = url;
				restApi.setServerURL(url);
		    },
		    
		    setCallback: function (callback) {
		    	config.callback = callback;
		    },

		    pushValue: function (key,value) {
		    	if (!config.useDeferredSend)
					pushValue_single(key, value);
				else
					pushValue_array(key, value);
		    },

		    flushData: function () {
		    	restApi.pushKeyValueStack(jsonUserValues);
		    },

			saveData: function(){
				doSaveData();
			}
		}
};