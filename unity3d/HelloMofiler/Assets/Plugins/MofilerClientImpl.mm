//
//  MofilerClientImpl.m
//  MofilerExt
//
//  Created by Damian Hernaez on 4/19/17.
//

#import <Foundation/Foundation.h>
#import "MofilerClientImpl.h"
#import <CoreLocation/CoreLocation.h>
#import <Mofiler/Mofiler-Swift.h>


@implementation MofilerUnityDelegate : NSObject
//callback and arguments


NSString* appName;
NSString* appKey;

-(NSString*) getAppKey{
     Mofiler* mof = [Mofiler sharedInstance];
    mof.appKey = @"TEST";
    
    return mof.appKey;
}

- (bool)  flushDataToMofiler
{
    Mofiler* mof = [Mofiler sharedInstance];
    
    [mof flushDataToMofiler];

    return true;
}

-(bool)setAppKey:(const char*)appKeyString{
    // Mofiler* mof = [Mofiler sharedInstance];
    NSLog(@"----Inside Implementation setAppKey");
    Mofiler* mof = [Mofiler sharedInstance];

    if(!mof.isInitialized) {
        appKey =[NSString stringWithUTF8String:appKeyString];
    
        mof.appKey = [NSString stringWithUTF8String:appKeyString];
    
        [mof setSdkTypeAndVersionWithSdk_type: @"Unity iOS SDK" sdk_version:@"1.0.7"];
    
        //TODO setUseLocation
        if(![mof.appName  isEqual: @""])
            [mof initializeWithAppKey: appKey appName: mof.appName useLoc: true useAdvertisingId: true];
    
        NSLog(@"--AppKey %@", mof.appKey);
        NSLog(@"--AppName %@", mof.appName);
    
        return true;
    } else {
        NSLog(@"Already initialized!");
        NSLog(@"--AppKey %@", mof.appKey);
        NSLog(@"--AppName %@", mof.appName);
        return false;
    }
    
}


-(bool)setURL:(const char*)urlString{
    NSLog(@"----Inside Implementation setURL");
    Mofiler* mof = [Mofiler sharedInstance];
    
    mof.url = [NSString stringWithUTF8String:urlString];
    NSLog(@"%@", mof.url);
    
    return true;

}

- (bool) setAppName:(const char*)appNameString
{
    NSLog(@"----Inside Implementation setAppName");
    Mofiler* mof = [Mofiler sharedInstance];

    if(!mof.isInitialized) {

        mof.appName = [NSString stringWithUTF8String:appNameString];
        appName =[NSString stringWithUTF8String:appNameString];
    
        //TODO setUseLocation
        if(![mof.appKey  isEqual: @""])
            [mof initializeWithAppKey: mof.appKey appName: appName useLoc: true useAdvertisingId: true];

        NSLog(@"--AppKey %@", mof.appKey);
        NSLog(@"--AppName %@", mof.appName);
        return true;
    } else {
        NSLog(@"Already initialized!");
        NSLog(@"--AppKey %@", mof.appKey);
        NSLog(@"--AppName %@", mof.appName);
        return false;
    }


}
- (bool)  setUseVerboseContext: (bool) state
{
    Mofiler* mof = [Mofiler sharedInstance];
    mof.useVerboseContext = state;
    return true;
}

- (bool)  SetUseLocation: (bool) state
{
    //Mofiler* mof = [Mofiler sharedInstance];
  
    //TODO: PRIVATE VALUE, RECOMPILE MOFILER LIBRARY TO ALLOW ACCESS
    
  //  [mof initializeWithAppKey: appKey appName: appName
    //                   useLoc:state useAdvertisingId: state];
    
    return true;
}

- (bool) addIdentity:(const char*)key
                    :(const char*)value
{
    Mofiler* mof = [Mofiler sharedInstance];
    
    [mof addIdentityWithIdentity:@{[NSString stringWithUTF8String:(char*)key]:[NSString stringWithUTF8String:(char*)value]}];
    return true;
}

- (bool) injectValue:(const char*)key
                    :(const char*)value
{
    Mofiler* mof = [Mofiler sharedInstance];

    [mof injectValueWithNewValue:@{[NSString stringWithUTF8String:(char*)key]:[NSString stringWithUTF8String:(char*)value]} expirationDateInMilliseconds:nil];
    return true;
}





@end

static MofilerUnityDelegate* delegateObject = nil;



extern "C" {
    
    void _SetURL(const char* urlString)
    {
        
        NSLog(@"----Entered Plugin");
        //return retBool;
        if (delegateObject == nil)
            delegateObject = [[MofilerUnityDelegate alloc] init];
        NSLog(@"----Init Plugin");
        [delegateObject setURL:urlString];
        
    }
    
    void _SetAppKey(const char* appKeyString)
    {
        NSLog(@"----Entered Plugin");
        //return retBool;
        if (delegateObject == nil)
            delegateObject = [[MofilerUnityDelegate alloc] init];
        NSLog(@"----Init Plugin");
        [delegateObject setAppKey:appKeyString];
    }
    
    void _SetAppName(const char* appNameString)
    {
        if (delegateObject == nil)
            delegateObject = [[MofilerUnityDelegate alloc] init];
        [delegateObject setAppName:appNameString];
    }
    
    void _SetUseLocation(bool state)
    {
        if (delegateObject == nil)
            delegateObject = [[MofilerUnityDelegate alloc] init];
        [delegateObject SetUseLocation:state];
    }

    void _SetUseVerboseContext(bool state)
    {
        if (delegateObject == nil)
            delegateObject = [[MofilerUnityDelegate alloc] init];
        [delegateObject setUseVerboseContext:state];
    }

    void _SetReadPhoneState(bool state)
    {
    }

  
    void _AddIdentity(const char* keyString, const char* valueString)
    {
        if (delegateObject == nil)
            delegateObject = [[MofilerUnityDelegate alloc] init];
        [delegateObject addIdentity:keyString
                                   :valueString];
    }
    void _InjectValue(const char* keyString, const char* valueString)
    {
        if (delegateObject == nil)
            delegateObject = [[MofilerUnityDelegate alloc] init];
        [delegateObject injectValue:keyString
                                   :valueString];
    }
    
    void _FlushDataToMofiler()
    {
        
        //return retBool;
        if (delegateObject == nil)
            delegateObject = [[MofilerUnityDelegate alloc] init];
        
        //[delegateObject SetUseLocation: YES];
        [delegateObject flushDataToMofiler];
        
    
    }
    
}
