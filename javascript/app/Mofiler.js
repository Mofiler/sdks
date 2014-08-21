// MF.Mofiler = (function() {
(function() {

	/*var appKey,
		appName,
		appVersion,
		identities,
		cookie,
		strURL,
		useLocation = true;*/

	// var moClient = new MF.MofilerClient(true,false);

	var moClient;

	var config = {

		identities: undefined,

		identity: undefined,

		url: undefined,
    
    	appKey: undefined,
    
    	appName: undefined,

    	appVersion: undefined,

    	cookie: undefined,

    	useLocation: true,

    	callback: undefined,

    	success: undefined,

    	error: undefined
    };

    callback = function(response,operation,success){
    	if (success && config.success)
    		config.success.call(config.scope,response,operation);
    	else if (!success && config.error)
    		config.error.call(config.scope,response,operation);
    };

	addAllAvailableHeaders = function(){
		if (config.appKey)
			moClient.addHeaderKeyValue(MF.K_MOFILER_API_HEADER_APPKEY, config.appKey);
		if (config.appName)
			moClient.addHeaderKeyValue(MF.K_MOFILER_API_HEADER_APPNAME, config.appName);
		if (config.appVersion)
			moClient.addHeaderKeyValue(MF.K_MOFILER_API_HEADER_APPVERSION, config.appVersion);
		if (config.cookie)
			moClient.addHeaderKeyValue(MF.K_MOFILER_API_HEADER_COOKIE, config.cookie);
	};

	MF.Mofiler = {

		init: function(options){
			MF.apply(config,options);
			moClient = new MF.MofilerClient({
				useDeferredSend: true,
				useLocation: config.useLocation,
				url: config.url,
				callback: config.callback || callback,
				scope: config.scope
			});

			this.setURL(config.url);
			this.addIdentities(config.identity);
		},

		getAppKey: function(){
			return config.appKey;
		},
		setAppKey: function(key){
			config.appKey = key;
		},
		
		getAppVersion: function(){
			return config.appVersion;
		},
		setAppVersion: function(version){
			config.appVersion = version;
		},
		
		getAppName: function(){
			return config.appName;
		},
		setAppName: function(name){
			config.appName = name;
		},

		getIdentity: function(key){
			return config.identities.hasOwnProperty(key) ? config.identities[key] : undefined;
		},
		addIdentity: function(key,value){
			if (MF.isEmpty(config.identities))
				config.identities = [];
			var id = {};
			id[key]=value;
			config.identities.push(id);
		},
		addIdentities: function(ids){
			if (!ids) return;

			if (MF.isEmpty(config.identities))
				config.identities = [];
			config.identities.push(ids);
		},

		/*getCookie: function(){
			return config.cookie;
		},
		setCookie: function(cookie){
			config.cookie = cookie;
		},*/

		getStrURL: function(){
			config.url = moClient.getUrl();
			return config.url;
		},
		setURL: function(a_URL){
			config.url = a_URL;
			moClient.setURL(a_URL);
		},

		isUseLocation: function(){
			return config.useLocation;
		},
		setUseLocation: function(a_useLocation){
			config.useLocation = a_useLocation;
			moClient.setUseLocation(config.useLocation);
		},

		setCallback: function(callback){
			moClient.setCallback(callback);
		},

		injectValue: function(key, value){
			if (config.appKey){
				if (!MF.isEmpty(config.identities)) { //
					addAllAvailableHeaders();
					moClient.setIdentity(config.identities);
					moClient.pushValue(key, value);
				}else
					throw "Mofiler: user identity needs be set before you can send any values to the server";
			}else
				throw "Mofiler: api key needs be set before you can send any values to the server"
		},

		getValue: function(key, identityKey, identityValue){
			if (config.appKey){
				if (!MF.isEmpty(config.identities)) { //
					addAllAvailableHeaders();
					moClient.setIdentity(config.identities);
					moClient.getValue(key, identityKey, identityValue);
				}else
					throw "Mofiler: user identity needs be set before you can send any values to the server";
			}else
				throw "Mofiler: api key needs be set before you can send any values to the server"
		},

		onDestroyApp: function(){
			this.flushDataToDisk();
		},

		flushDataToDisk: function(){
			moClient.saveData();
		},
		
		flushDataToMofiler: function(){
			moClient.flushData();
		}
	}
})();