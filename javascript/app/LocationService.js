(function() {

	var locationValues,
	lastLocationTS,
	callback,
	isProcessingGEO = false;

	var queue = [];

	setCurrentLocation = function(position){
		if (MF.Object.isEmpty(locationValues))
			locationValues = {};
		locationValues['latitude'] = position.coords.latitude;
		locationValues['longitude'] = position.coords.longitude;
		isProcessingGEO = false;

		processQueue();
	};

	processQueue = function(){
		for(var i = queue.length; i--;){
		    var cb = queue[i];
		    queue.splice(i, 1);
		    cb();
		}
	};

	getTimeDifferenceInSec = function(startTime, endTime){
		var difference = endTime - startTime; // This will give difference in milliseconds
		return (difference / 1000)%60;
	};

	MF.LocationService = {

		/**
		 * Checks if the GeoLocation is supported.
		 * @return {Boolean} `true` if GeoLacalization is supported.
		 */
		supportGeoLocation: function() {
            return navigator.geolocation;
        },

        /**
		 * Starts the GeoLocation service and get the current location. Schedules to run every 30 seconds.
		 */
        startProvider: function(cb) {
        	var now = new Date().getTime();
        	if (navigator.geolocation && ( (!lastLocationTS) || getTimeDifferenceInSec(lastLocationTS,now)>30) ) {
            	navigator.geolocation.getCurrentPosition(setCurrentLocation);
            	lastLocationTS = now;
            	isProcessingGEO = true;
        	}
        	if (isProcessingGEO){
        		queue.unshift(cb); //add the callback to the queue
        	}else{ //if there time is less than 30 sec and its not pending processing, execute the callback directly
        		cb();
        	}

        },

        /**
		 * Gets the last known location.
		 * @return {Object} with the last known latitud and longitud.
		 */
        getLastKnownLocation: function(){
        	return locationValues;
        }

	}
}());