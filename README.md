BLACKBERRY OS 5.0:
===================
Check the HelloMofiler project to see rapidly how to use the Mofiler SDK.

Quick stuff to get you going:
		Mofiler mof;
        mof = Mofiler.getInstance();
        mof.setAppKey("MY-APPKEY-HERE");
        mof.setAppName("MyTestApplication");
        mof.addIdentity("username", "johndoe");
        mof.setListener(this);

Inject values to Mofiler:
  		mof.injectValue("mykey", "myvalue");
        mof.injectValue("mykey2", "myvalue2", System.currentTimeMillis() + (1000*60*60*24));

Get values from Mofiler:
        mof.getValue("mykey", "username", "johndoe");
-usage: key to retrieve; identifier key to use, identifier key value to use.

