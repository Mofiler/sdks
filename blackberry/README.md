# MOFILER SDK FOR BLACKBERRY OS 5.0:

Check the HelloMofiler project to see rapidly how to use the Mofiler SDK.


## Quick stuff to get you going

This is all the code you need:

### Initialization

        Mofiler mof;
        mof = Mofiler.getInstance();
        mof.setAppKey("MY-APPKEY-HERE");
        mof.setAppName("MyTestApplication");
        mof.setURL("mofiler.com:8081");
        mof.addIdentity("username", "johndoe");
        mof.addIdentity("email", "john@doe.com");
        mof.addIdentity("bbPIN", "227BBA67");
        mof.setListener(this);

### Inject values to Mofiler:

  		mof.injectValue("mykey", "myvalue");
        mof.injectValue("mykey2", "myvalue2", System.currentTimeMillis() + (1000*60*60*24));

Mofiler uses an internal stack and persistence in order to collect data from your application before attempting to send it over to the server, thus
ensuring internet usage and user experience is taken care of.
Should you want to send data over to Mofiler server right away, you just need to perform the following call:

        mof.flushDataToMofiler();


### Get values from Mofiler:

        mof.getValue("mykey", "username", "johndoe");

- usage: key to retrieve; identifier key to use, identifier key value to use.

### Deinitialization

Before exiting your application (i.e., before calling System.exit()), you need to place a call to onDestroyApp(), like this:

	mof.onDestroyApp();

For example, on your application MainScreen (net.rim.device.api.ui.container.MainScreen) you could do this:

    public boolean onClose() {
        // TODO Auto-generated method stub
        //Call Mofiler.onDestroyApp() when you know your main screen is going off and your app is going to be finished
        mof.onDestroyApp();
        return super.onClose();
    }


### Listeners

If you set a listener, it will have to implement the interface ApiListener and the following abstract method
		methodResponded(String a_methodCalled, Vector a_vectBusinessObject)

This will always be called either in success or if an error occurred. For your ease, you can always rely that
if an error occured you will get a JSONObject with the "error" within.
Otherwise, you get the "result" key.
In all cases, this is true even if the mofiler SDK could not connect to the server, so this way you can handle errors
in the same way.


## LICENSING NOTICE: 

We use LWUIT_IO to provide Mofiler SDK with the persistence queue, so it doesn't try go on the internet every time an event happens, which could carry possible undesired network fees.
Instead, Mofiler SDK will try to hold on until the queue is full to try push the values to the Mofiler server.
We've only modified LWUIT_IO modules to make it tidy/tiny as possible of use as we only needed the persistence framework from LWUIT. Otherwise we'd have to include the full LWUIT
library within Mofiler SDK, when not really needed.
This is possible due to LWUIT being published under GPL+CPE (Class Path Exception). All code remains pretty much the same as LWUIT version 1.5, property of Oracle 
and/or its affiliates. Copyright notices have been unaltered - neither package names.
More on this can be read here (LWUIT's official blog at the time of 1.5 version being released): http://lwuit.blogspot.com.ar/2008/05/licensing-terms-of-lwuit.html

