# Mofiler SDK for Android:

Check the HelloMofiler project to rapidly see how to use the Mofiler SDK.


## Quick stuff to get you going

Include the mofiler-sdk project as a library to your project (see Android properties in the project properties tab for the HelloMofiler example)

Add these permissions to your project AndroidManifest.xml file:

    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>

If you are going to use the Verbose Extras mode, you also should add these to your manifest:

    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />

And inside your application tag, add these:        

        <!-- MOFILER SERVICES -->
        <service android:name="com.mofiler.service.AlarmService" />


        <!-- MOFILER RECEIVERS -->
        <receiver
            android:name="com.mofiler.receivers.AlarmNotificationReceiver"
            android:enabled="true" />
        <receiver
            android:name="com.mofiler.receivers.OnBootReceiver"
            android:enabled="true" >
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" >
                </action>
            </intent-filter>
        </receiver>

Verbose Extras mode tracks the user activity not only within your application but within the context of the device.

This is all the code you need:

### Initialization

        Mofiler mof;
        mof = Mofiler.getInstance(getActivity());
        mof.setAppKey("MY-APPKEY-HERE-ANDROID");
        mof.setAppName("MyAndroidTestApplication");
        mof.setURL("mofiler.com:8081");
        mof.addIdentity("username", "johndoe");
        mof.addIdentity("email", "john@doe.com");
        mof.setUseLocation(true); //defaults to true
        mof.setUseVerboseContext(true); //defaults to false, but helps Mofiler get a lot of information about the device context
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


### Listeners

If you set a listener, it will have to implement the interface ApiListener and the following abstract method
		methodResponded(String a_methodCalled, Vector a_vectBusinessObject)

This will always be called either in success or if an error occurred. For your ease, you can always rely that
if an error occured you will get a JSONObject with the "error" within.
Otherwise, you get the "result" key.
In all cases, this is true even if the mofiler SDK could not connect to the server, so this way you can handle errors
in the same way.



