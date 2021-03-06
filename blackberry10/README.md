# MOFILER SDK FOR BLACKBERRY 10 (Cascades):

Check the HelloMofiler project to see rapidly how to use the Mofiler SDK.


## How to include the library in your Cascades project

Read the following article

http://supportforums.blackberry.com/t5/Native-Development-Knowledge/How-to-use-a-third-party-shared-or-static-library-in-a-Cascades/ta-p/1886279

## Quick stuff to get you going

     In your app's .project you need to locate <projects></projects> and add your library as <project>libraryname</project> in there.
    Then in your app's .pro file
        add a LIBS += -llibraryname
        add relative path to your library's headers using INCLUDEPATH+=
        in the relevant CONFIG(release, debug|release) sections, add LIBS += -Lrealtivepathtobinary
    (If you add a shared library you need to add it to the BAR file as well)


## Initialization
You can either use it all in Qt C++ or from QML javascript. If your application uses QML for UI implementation, you'll need to create a new Mofiler instance and expose Mofiler to QML, like this:

    QmlDocument *qml = QmlDocument::create("asset:///main.qml").parent(this);

    mofObject = new Mofiler();
    // Expose this class to QML so that we can call it's functions from C++ code.
    qml->setContextProperty("mofiler", mofObject);

If you stick to C++, just having a new Mofiler() call to create a new object will do.


### Initialization: javascript

Then from javascript, initializtion should go like setting properties, like this:

                mofiler.appKey = "HELLOMOFILERCASCADESKEY";
                mofiler.appName = "HelloMofiler";
                mofiler.appVersion = "1.0";
                mofiler.url = "http://mofiler.com:8081";
                mofiler.addIdentity("username", "johndoe");

Note that you can either set properties with the assignment operator "=" or by calling the setters in mofiler, such as setAppName, setAppVersion, etc.

                mofiler.setAppKey("HELLOMOFILERCASCADESKEY");
                mofiler.setAppName("HelloMofiler");
                mofiler.setAppVersion("1.0");
                mofiler.setUrl("http://mofiler.com:8081");


### Initialization: C++

All properties in Mofiler have setters/getters. These are the current prototypes:

		/* APP KEY */
		QString getAppKey();
		void setAppKey(QString a_strAppKey);

		/* APP NAME */
		QString getAppName();
		void setAppName(QString a_strAppName);

		/* APP VERSION */
		QString getAppVersion();
		void setAppVersion(QString a_strAppVersion);

		/* cookie */
		QString getCookie();
		void setCookie(QString a_strCookie);

		/* URL */
		QUrl getUrl();
		void setUrl(QUrl a_url);

For example:

                mofObject.setAppKey("HELLOMOFILERCASCADESKEY");
                mofObject.setAppName("HelloMofiler");
                mofObject.setAppVersion("1.0");
                mofObject.setUrl("http://mofiler.com:8081");


## Usage: injection

Injecting values to Mofiler is easy. Just call the method injectValues().

From javascript:

                    mofiler.injectValue("mykey", "myvalue");

Or from C++:

					mofObject.injectValue("mykey", "myvalue");

Whose prototype is:

					void Mofiler::injectValue(QString key, QString value);

In this latter case, both arguments to this method are of type QString. You should also know there's an optional third argument called "expireAfterMs", which is set in milliseconds since EPOCH, with which you tell Mofiler that this particular value has an expiration time/date (this must be understood as in, this value for this key makes sense if and only if current time has not exceeded the expiration value in milliseconds).

The prototype for this latter function is:

					void Mofiler::injectValue(QString key, QString value, long expireAfterMs);


## Usage: reading values from Mofiler

The method Mofiler SDK exposes for this is called getValue().

From javascript:

                    mofiler.getValue("mykey", "myidentitykey", "myidentityvalue");

                    For example:

                    mofiler.getValue("mykey0", "username", "johndoe");

Or from C++:

                    mofObject.getValue("mykey0", "username", "johndoe");

Both getValue() and injectValue() work asynchronously. When the operation finishes, a signal is emitted indicated this. The signal exposed is called "methodResponded" and carries a QVariant with whatever te server has thrown as a response to the request.

To read the value corresponding to a key as intended by getValue(), you must watch the following property "value" within the mofiler object.
In QML, you can meake use of property binding by setting the rightmost part of the equation to "mofiler.value" and bind it to a property of yours, like this:

                    property variant myvar: mofiler.value
                    
                    onMyvarChanged: {
                        console.log("Response gotten from Mofiler server: ");
                        var varcont = JSON.stringify(myvar); //note that checking either "myvar" or "mofiler.value" would be the same
                        console.log(varcont);
                        if (mofiler.value != undefined){
                            console.log("value gotten for requested key: " + JSON.stringify(mofiler.value));    
                        }
                    }

Within mofiler.value, the server will always answer something along the lines of:

´´´json
                    {"result": "ok", "value": "thevalueyouwantedtoretrieve"}
´´´json

Should the value for this key not yet exist in the server, or has not yet been approved by the application administrator, then "value" is undefined or null.


## Application Permissions:
The application must have the *access_device_model_name* permission to access hardware info so Mofiler can get such info as the model name of the device.
Read more: https://developer.blackberry.com/native/reference/cascades/bb__device__hardwareinfo.html#function-modelname

In order for Mofiler to be able to connect to the server (and thus be of any use), the application must have *access_internet* enabled also.

    * access_internet
    * access_device_model_name


## Libs

You need to add the following line to your .pro file: 
    
    LIBS += -lbbdata -lbbdevice -lbbsystem


## LICENSING NOTICE
This library takes some of the code/config values found in the template made by Isaac Gordezky, uploaded to Blackberry Cascades community samples a this URL in github:
https://github.com/blackberry/Cascades-Community-Samples/tree/master/Cascades-Library-Template