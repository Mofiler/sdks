# MOFILER SDK FOR ACTIONSCRIPT 3:

Check the HelloMofiler project to see rapidly how to use the Mofiler SDK.


## Quick stuff to get you going

Include the mofiler-sdk.ane file as a library (Native Extension) to your project (see Native Extensions tab in the project properties of the HelloMofiler example)

Add these permissions to your project: 

For Android add to project_name-app.xml file:

  <uses-permission android:name="android.permission.INTERNET"/>
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
  <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
  <uses-permission android:name="android.permission.READ_PHONE_STATE"/> 
  <uses-permission android:name="android.permission.READ_SMS" />
  <uses-permission android:name="android.permission.READ_PHONE_STATE" />

If you are going to use the Verbose Extras mode, you also should add these to your manifest:

  <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
  <uses-permission android:name="android.permission.DISABLE_KEYGUARD"/>
  <uses-permission android:name="android.permission.WAKE_LOCK" />

This is all the code you need:

### Initialization
        import com.mofiler.Mofiler;


      var m:Mofiler = Mofiler.getInstance();
      m.setAppKey("myAppKey");
      m.setAppName("myAppName");
      m.setURL("mofiler.com:8081");
      m.setUseVerboseContext(true);
      m.setUseLocation(true);
      
### Define unique identifiers for your user:

      m.addIdentity("username", "flash_jhondoe2");
      m.addIdentity("pin", "12345");

### Inject values to Mofiler:

      m.injectValue("testKey0-1", "testValue0");
      m.injectValue("testKey1-1", "testValue");
      m.injectValue("testKey2-1", "testValue");
      
      m.flushDataToMofiler(); //<--force flush to API server


-- TO BE IMPLEMTED
In future release...

        ### Get values from Mofiler:

                m.getValue("mykey", "username", "johndoe");

        - usage: key to retrieve; identifier key to use, identifier key value to use.


        ### Listeners

        If you set a listener, it will have to implement the interface ApiListener and the following abstract method
        		methodResponded(String a_methodCalled, Vector a_vectBusinessObject)

        This will always be called either in success or if an error occurred. For your ease, you can always rely that
        if an error occured you will get a JSONObject with the "error" within.
        Otherwise, you get the "result" key.
        In all cases, this is true even if the mofiler SDK could not connect to the server, so this way you can handle errors
        in the same way.

-- /TO BE IMPLEMTED


