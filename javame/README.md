# MOFILER SDK FOR JavaME:

Check the HelloMofiler project to see rapidly how to use the Mofiler SDK.


## Quick stuff to get you going

Include the library .jar file in your libs path or into your project (depending on the IDE you use). 

For a brief on how to build the HelloMofiler application, follow these steps:
Download and install:
1.1)JDK 1.6+
1.2)WTK 2.5.2 or newer (http://java.sun.com/products/sjwtoolkit/download.html)
1.3)ant (http://ant.apache.org/manual/install.html)
1.4)antenna (http://antenna.sourceforge.net/)
1.5)Proguard (http://proguard.sourceforge.net/)
on your computer.

Then you just need to run the target "build_all" in build.xml inside HelloMofiler project folder.

NOTE: HelloMofiler is build with Prosciutto. You can use any other JavaME-compatible IDE / development platform in order to user Mofiler J2ME SDK.

This is all the code you need:

### Initialization

        Mofiler mof;
        mof = Mofiler.getInstance();
        mof.setAppKey("MY-APPKEY-HERE");
        mof.setAppName("MyTestJ2MEApplication");
        mof.setURL("mofiler.com:8081");
        mof.addIdentity("username", "johndoe");
        mof.addIdentity("email", "john@doe.com");
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

For example, on your application MIDlet you could do this:

    protected void destroyApp( boolean unconditional )throws MIDletStateChangeException
    {
        mof.onDestroyApp();
        exitMIDlet();
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

