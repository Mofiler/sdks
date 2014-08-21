(function() {

	MFObject = MF.Object = {

		/**
		 * Checks if there are any properties on this object.
		 * @param {Object} object
		 * @return {Boolean} `true` if there no properties on the object.
		 */
		isEmpty: function(object){
		    for (var key in object) {
		        if (object.hasOwnProperty(key)) {
		            return false;
		        }
		    }
		    return true;    
		}

	}
}());