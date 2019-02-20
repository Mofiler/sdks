Initialization
```
MofilerBridge.SetURL ("mofiler.com/mock”); // <— /mock for testing
MofilerBridge.SetAppKey ("APP-KEY-1");
MofilerBridge.SetAppName (“App Name");
MofilerBridge.SetUseVerboseContext(true);
MofilerBridge.SetUseLocation(true);
MofilerBridge.SetReadPhoneState(false);

// any user identification, at least one
MofilerBridge.AddIdentity("user_id", "12345"); 
MofilerBridge.AddIdentity("username", "bt"); 
MofilerBridge.AddIdentity("email", "bt@mofiler.com"); 
MofilerBridge.AddIdentity("fb_id", "54321"); 
```

Adding more identities, anywhere in the app
```
MofilerBridge.AddIdentity("username", "bt”);
```

Injecting Values, anywhere in the app 
```
MofilerBridge.InjectValue(“valueKey", “valueStr");
MofilerBridge.FlushDataToMofiler();
```
