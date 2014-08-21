var MF = MF || {};

(function() {
        var global = this,
        objectPrototype = Object.prototype,
        toString = objectPrototype.toString,
        enumerables = [//'hasOwnProperty', 'isPrototypeOf', 'propertyIsEnumerable',
                       'valueOf', 'toLocaleString', 'toString', 'constructor'];

        MF.global = global;

        for (i in { toString: 1 }) {
            enumerables = null;
        }

        /**
         * An array containing extra enumerables for old browsers
         * @property {String[]}
         */
        MF.enumerables = enumerables;

        /**
         * Copies all the properties of `config` to the specified `object`. There are two levels
         * of defaulting supported:
         * 
         *      Ext.apply(obj, { a: 1 }, { a: 2 });
         *      //obj.a === 1
         * 
         *      Ext.apply(obj, {  }, { a: 2 });
         *      //obj.a === 2
         * 
         * Note that if recursive merging and cloning without referencing the original objects
         * or arrays is needed, use {@link Ext.Object#merge} instead.
         * 
         * @param {Object} object The receiver of the properties.
         * @param {Object} config The primary source of the properties.
         * @param {Object} [defaults] An object that will also be applied for default values.
         * @return {Object} returns `object`.
         */
        MF.apply = function(object, config, defaults) {
            if (defaults) {
                MF.apply(object, defaults);
            }

            if (object && config && typeof config === 'object') {
                var i, j, k;

                for (i in config) {
                    object[i] = config[i];
                }

                if (enumerables) {
                    for (j = enumerables.length; j--;) {
                        k = enumerables[j];
                        if (config.hasOwnProperty(k)) {
                            object[k] = config[k];
                        }
                    }
                }
            }

            return object;
        };

        MF.apply(MF, {

            K_MOFILER_API_VERSION: '0.1',
            K_MOFILER_API_HEADER_NOISELEVEL: 'X-Mofiler-NoiseLevel',
            K_MOFILER_API_HEADER_SESSIONID: 'X-Mofiler-SessionID',
            K_MOFILER_API_HEADER_INSTALLID: 'X-Mofiler-InstallID',
            K_MOFILER_API_HEADER_API_VERSION: 'X-Mofiler-ApiVersion',
            K_MOFILER_API_HEADER_APPKEY: 'X-Mofiler-AppKey',

            /*OPTIONAL*/
            K_MOFILER_API_HEADER_APPNAME: 'X-Mofiler-AppName',
            K_MOFILER_API_HEADER_APPVERSION: 'X-Mofiler-AppVersion',
            K_MOFILER_API_HEADER_COOKIE: 'X-Mofiler-Cookie',

            /* IDENTITY keys */
            K_MOFILER_API_IDENTITY: 'identity',

            /* user_values keys */
            K_MOFILER_API_USER_VALUES: 'user_values',

            /* Device context keys */
            K_MOFILER_API_DEVICE_CONTEXT: 'mofiler_device_context',
            K_MOFILER_API_DEVICE_CONTEXT_NETWORK: 'network',
            K_MOFILER_API_DEVICE_CONTEXT_DISPLAYSIZE: 'display',
            K_MOFILER_API_DEVICE_CONTEXT_MANUFACTURER: 'manufacturer',
            K_MOFILER_API_DEVICE_CONTEXT_MODELNAME: 'model',
            K_MOFILER_API_DEVICE_CONTEXT_LOCALE: 'locale',

            /**
             * Returns true if the passed value is empty, false otherwise. The value is deemed to be empty if it is either:
             *
             * - `null`
             * - `undefined`
             * - a zero-length array
             * - a zero-length string (Unless the `allowEmptyString` parameter is set to `true`)
             *
             * @param {Object} value The value to test.
             * @param {Boolean} [allowEmptyString=false] `true` to allow empty strings.
             * @return {Boolean}
             */
            isEmpty: function(value, allowEmptyString) {
                return (value == null) || (!allowEmptyString ? value === '' : false) || (MF.isArray(value) && value.length === 0);
            },

            /**
             * Returns `true` if the passed value is a JavaScript Array, `false` otherwise.
             *
             * @param {Object} target The target to test.
             * @return {Boolean}
             * @method
             */
            isArray: ('isArray' in Array) ? Array.isArray : function(value) {
                return toString.call(value) === '[object Array]';
            },

            supportsLocalStorage: function() {
                try {
                    // IE10/Win8 throws "Access Denied" accessing window.localStorage, so
                    // this test needs to have a try/catch
                    if ('localStorage' in window && window['localStorage'] !== null) {
                        //this should throw an error in private browsing mode in iOS as well
                        localStorage.setItem('localstorage-test', 'test success');
                        //clean up if setItem worked
                        localStorage.removeItem('localstorage-test');
                        return true;
                    }
                } catch ( e ) {
                    // ignore
                }

                return false;
            }

        }); // MF.apply(MF
}());