# MOFILER SDK FOR JavaScript:

Check the index.html and app.js files to see rapidly how to use the Mofiler SDK.


## Quick stuff to get you going

Include the following files in your html file as shown in index.html

        <script src="app/MF.js"></script>
        <script src="app/LocationService.js"></script>
        <script src="app/RESTApi.js"></script>
        <script src="app/MofilerClient.js"></script>
        <script src="app/Mofiler.js"></script>
        <script src="app/Object.js"></script>

This is all the code you need:

### Initialization

        MF.Mofiler.init({
            scope: this,
            url: 'mofiler.com:8081',
            appKey: 'KEY-TEST-JS',
            appName: 'TestAppJS',
            identity: {'username':'johndoe'},
            useLocation: false,
            success: function(response,operation){
                var res = JSON.parse(response);
                if (operation === MF.RESTApi.K_MOFILER_API_METHOD_NAME_inject){
                    console.log('This is the result from post ' + res.result);
                    MF.Mofiler.getValue("mykey0", "username", "johndoe");
                }
                else if (operation === MF.RESTApi.K_MOFILER_API_METHOD_NAME_get)
                    console.log('This is the result from get ' + res.value);
            },
            error: function(response,operation){
                var res = JSON.parse(response);
                var error = res.error;
                console.log("Error from server " + error);
            }
        });

The init() function takes the following properties / parameters:
 - scope: this
 - url: url pointing to Mofiler's API server
 - appKey: the application key assigned to your application
 - appName: then name of your application
 - identity: an array of key/value identities, or an object of just one identity is to be supplied
 - useLocation: boolean property, indicating whether location should be used (defaults to true if not given)
 - success / error: anonymous callback functions that are going to be called once an inject or get operation is performed.


### Inject values to Mofiler:

  		MF.Mofiler.injectValue("mykey", "myvalue");
        MF.Mofiler.injectValue("mykey2", "myvalue2", System.currentTimeMillis() + (1000*60*60*24));

Mofiler uses an internal stack and persistence in order to collect data from your application before attempting to send it over to the server, thus
ensuring internet usage and user experience is taken care of.
Should you want to send data over to Mofiler server right away, you just need to perform the following call:

        MF.Mofiler.flushDataToMofiler();


### Get values from Mofiler:

        MF.Mofiler.getValue("mykey", "username", "johndoe");

- usage: key to retrieve; identifier key to use, identifier key value to use.

### Deinitialization

Before exiting your application (i.e., when a browser window is closed and such event is of your interest), you should place a call to onDestroyApp(), like this:

	MF.Mofiler.onDestroyApp();


### Location and LocalStorage in modern browsers

The Mofiler JavaScript SDK has been coded following the lines of the Java-based SDKs such as JavaME, Blackberry and Android ones.
In this sense we also use location based services and use local storage capabilities if present, in order to get the most out of it. Nevertheless, 
you should be able to just run the SDK and include it in your app in a transparent manner, no matter what.


### Browser support

The Mofiler JavaScript SDK has been tested on these browsers (more to come, on a case-to-case basis, as needed) :
 - Chrome 36.0.1985.143 m
 - Firefox 31.0
 - Internet Explorer 11.0.9600.17239


### Callbacks (onError / onSuccess)

These will always be called either in success or if an error occurred. For your ease, you can always rely that
if an error occured you will get a JSONObject with the "error" within.
Otherwise, you get the "result" key.
In all cases, this is true even if the mofiler SDK could not connect to the server, so this way you can handle errors
in the same way.

