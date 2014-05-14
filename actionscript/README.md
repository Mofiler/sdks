# MOFILER SDK FOR ACTIONSCRIPT 3:

Check the HelloMofiler project to see rapidly how to use the Mofiler SDK.


## Quick stuff to get you going

Include the mofiler-sdk.swc file as a library to your project (see Library path tab in the project properties for the HelloMofiler example)

Add these permissions to your project: 

For Android add to <project_name>-app.xml file:

    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>


For BlackBerry add to bar-descriptor.xml:

   <permission>read_device_identifying_information</permission>
   <permission>read_personally_identifiable_information</permission>
   <permission>access_internet</permission>
   <permission>access_shared</permission>

This is all the code you need:

### Initialization

        var mof:Mofiler = new Mofiler();
        mof.setAppKey("myAppKey");
        mof.setAppName("myAppName");
        mof.setAppVersion("1.0");
        mof.setURL("mofiler.com:8081");
        mof.addIdentity("username", "flash_jhondoe");
        mof.addIdentity("email", "john@doe.com");

### Inject values to Mofiler:

  		mof.injectValue("mykey", "myvalue");
        mof.injectValue("mykey2", "myvalue2", new Date().getTime() + (1000*60*60*24));

### Obtaining user identification properties

In the HelloMofiler project you will find an example of how to obtain different variables that identify the device and the user that may help creating the identity. For these to work, you need to use an already built ANE (device-info-util.ane) that you will find in the project /lib folder.

NOTE: the ANE was built based on https://github.com/katopz/ane-device-file-util/ and was modified to support Android and BlackBerry. 

This ANE will provide methods to access cross-platform native information like Mac Address, Device Name, etc. and some specific information like PIN unique to BlackBerry Platform. 

HOW TO USE IT
    import com.debokeh.anes.utils.DeviceInfoUtil;

    if(DeviceInfoUtil.getPIN()!=null){
        mof.addIdentity("pin", DeviceInfoUtil.getPIN());
    }
    if(DeviceInfoUtil.getCurrentDeviceName()!=null){
        mof.addIdentity("deviceName", DeviceInfoUtil.getCurrentDeviceName());
    }
    if(DeviceInfoUtil.getCurrentMACAddress()!=null){
        mof.addIdentity("macAddress", DeviceInfoUtil.getCurrentMACAddress());
    }
    if(DeviceInfoUtil.getCurrentSSID()!=null){
        mof.addIdentity("ssid", DeviceInfoUtil.getCurrentSSID());
    }
    if(DeviceInfoUtil.getIMEI()!=null){
        mof.addIdentity("imei", DeviceInfoUtil.getIMEI());
    }
    if(DeviceInfoUtil.getDeviceModelName()!=null){
        mof.addIdentity("deviceModelName", DeviceInfoUtil.getDeviceModelName());
    }

-- TO BE IMPLEMTED
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
-- /TO BE IMPLEMTED


