//
//  MofilerExt.m
//  Mofiler
//
//  Created by Todsaporn Banjerdkit on 12/2/12.
//  Copyright (c) 2012 Todsaporn Banjerdkit. All rights reserved.
//

//#import <SystemConfiguration/CaptiveNetwork.h>
//#import <UIKit/UIKit.h>
//#import <CoreLocation/CoreLocation.h>

// for getMACAddress
//#import <sys/socket.h>
//#import <sys/sysctl.h>
//#import <net/if.h>
//#import <net/if_dl.h>
#import <Mofiler/Mofiler-Swift.h>

//------------------------------------
//
// FRE Helper.
//
//------------------------------------

#import "FlashRuntimeExtensions.h"

#define DEFINE_ANE_FUNCTION(fn) FREObject (fn)(FREContext context, void* functionData, uint32_t argc, FREObject argv[])
#define MAP_FUNCTION(fn, data) { (const uint8_t*)(#fn), (data), &(fn) }
#define DISPATCH_STATUS_EVENT(extensionContext, code, level) FREDispatchStatusEventAsync((extensionContext), (uint8_t*)code, (uint8_t*)level)

//------------------------------------
//
// Core Methods.
//
//------------------------------------


NSString* appName;
NSString* appKey;

FREObject setURL(FREContext ctx, void* funcData, uint32_t argc, FREObject argv[])
{
    
    uint32_t string1Length;
    const uint8_t *url;
    FREGetObjectAsUTF8(argv[0], &string1Length, &url);
//    
//    //Singleton de mofiler
    Mofiler* mof = [Mofiler sharedInstance];
//
//    //Inicializa las demas propiedades que no son obligatorias
    mof.url = [NSString stringWithUTF8String:(char*)url];
    
    FREObject retBool = nil;
    FRENewObjectFromBool(true, &retBool);
    
    // Return data back to ActionScript
    return retBool;
}

FREObject setAppKey(FREContext ctx, void* funcData, uint32_t argc, FREObject argv[])
{
    
    uint32_t string1Length;
    const uint8_t *appKey;
    FREGetObjectAsUTF8(argv[0], &string1Length, &appKey);
//    
//    //Singleton de mofiler
    Mofiler* mof = [Mofiler sharedInstance];
//
//    mof.appKey = [NSString stringWithUTF8String:(char*)appKey];
    
    if(!mof.isInitialized) {
        appKey =[NSString stringWithUTF8String:(char*)appKey];
        
        mof.appKey = [NSString stringWithUTF8String:(char*)appKey];
        
        [mof setSdkTypeAndVersionWithSdk_type: @"Adobe Air iOS SDK" sdk_version:@"1.0.8"];
        
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

    
    
    FREObject retBool = nil;
    FRENewObjectFromBool(true, &retBool);
    
    // Return data back to ActionScript
    return retBool;
}

FREObject setAppName(FREContext ctx, void* funcData, uint32_t argc, FREObject argv[])
{
    
    uint32_t string1Length;
    const uint8_t *appName;
    FREGetObjectAsUTF8(argv[0], &string1Length, &appName);
    
//    //Singleton de mofiler
    Mofiler* mof = [Mofiler sharedInstance];
//
//    mof.appKey = [NSString stringWithUTF8String:(char*)appName];
    if(!mof.isInitialized) {
        
        mof.appName = [NSString stringWithUTF8String:(char*)appName];
        appName =[NSString stringWithUTF8String:(char*)appName];
        
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
    
    
    FREObject retBool = nil;
    FRENewObjectFromBool(true, &retBool);
    
    // Return data back to ActionScript
    return retBool;
}

FREObject addIdentity(FREContext ctx, void* funcData, uint32_t argc, FREObject argv[])
{
    FREObject element;
    FREGetArrayElementAt(argv[0], 0, &element);
    
    uint32_t string1Length;
    const uint8_t *key;
    FREGetObjectAsUTF8(element, &string1Length, &key);

    FREObject element2;
    FREGetArrayElementAt(argv[0], 1, &element2);
    
    uint32_t string2Length;
    const uint8_t *value;
    FREGetObjectAsUTF8(element, &string2Length, &value);
    
//    //Singleton de mofiler
    Mofiler* mof = [Mofiler sharedInstance];
    [mof addIdentityWithIdentity:@{[NSString stringWithUTF8String:(char*)key]:[NSString stringWithUTF8String:(char*)value]}];
    
    FREObject retBool = nil;
    FRENewObjectFromBool(true, &retBool);
    
    // Return data back to ActionScript
    return retBool;
}

FREObject injectValue(FREContext ctx, void* funcData, uint32_t argc, FREObject argv[])
{
    FREObject element;
    FREGetArrayElementAt(argv[0], 0, &element);
    
    uint32_t string1Length;
    const uint8_t *key;
    FREGetObjectAsUTF8(element, &string1Length, &key);
    
    FREObject element2;
    FREGetArrayElementAt(argv[0], 1, &element2);
    
    uint32_t string2Length;
    const uint8_t *value;
    FREGetObjectAsUTF8(element2, &string2Length, &value);
    
//    //Singleton de mofiler
    Mofiler* mof = [Mofiler sharedInstance];
    [mof injectValueWithNewValue:@{[NSString stringWithUTF8String:(char*)key]:[NSString stringWithUTF8String:(char*)value]} expirationDateInMilliseconds:nil];
    
    FREObject retBool = nil;
    FRENewObjectFromBool(true, &retBool);
    
    // Return data back to ActionScript
    return retBool;
}

FREObject setUseVerboseContext(FREContext ctx, void* funcData, uint32_t argc, FREObject argv[])
{
    
    uint32_t boolean;
    FREGetObjectAsBool(argv[0], &boolean);
    
//    //Singleton de mofiler
    Mofiler* mof = [Mofiler sharedInstance];
//
    mof.useVerboseContext = boolean;
    
    FREObject retBool = nil;
    FRENewObjectFromBool(true, &retBool);
    
    // Return data back to ActionScript
    return retBool;
}

FREObject setUseLocation(FREContext ctx, void* funcData, uint32_t argc, FREObject argv[])
{
    
    uint32_t boolean;
    FREGetObjectAsBool(argv[0], &boolean);
    
//    //Singleton de mofiler
//    Mofiler* mof = [Mofiler sharedInstance];
//
//    mof.useLocation = boolean;
    
    FREObject retBool = nil;
    FRENewObjectFromBool(true, &retBool);
    
    // Return data back to ActionScript
    return retBool;
}

FREObject setReadPhoneState(FREContext ctx, void* funcData, uint32_t argc, FREObject argv[])
{
    uint32_t boolean;
    FREGetObjectAsBool(argv[0], &boolean);
    
    //    //Singleton de mofiler
    //    Mofiler* mof = [Mofiler sharedInstance];
    //    setReadPhoneState
    
    FREObject retBool = nil;
    FRENewObjectFromBool(true, &retBool);
    
    // Return data back to ActionScript
    return retBool;
}

FREObject flushDataToMofiler(FREContext ctx, void* funcData, uint32_t argc, FREObject argv[])
{
    
//    //Singleton de mofiler
    Mofiler* mof = [Mofiler sharedInstance];
//
//    //Fuerza el envio de datos alojados
    [mof flushDataToMofiler];
    
    FREObject retBool = nil;
    FRENewObjectFromBool(true, &retBool);
    
    // Return data back to ActionScript
    return retBool;
}




//------------------------------------
//
// FRE Required Methods.
//
//------------------------------------

// The context initializer is called when the runtime creates the extension context instance.

void MofilerContextInitializer(void* extData, const uint8_t* ctxType, FREContext ctx, uint32_t* numFunctionsToSet, const FRENamedFunction** functionsToSet)
{       
    static FRENamedFunction functionMap[] =
    {
        MAP_FUNCTION(setURL, NULL),
        MAP_FUNCTION(setAppKey, NULL),
        MAP_FUNCTION(setAppName, NULL),
        MAP_FUNCTION(addIdentity, NULL),
        MAP_FUNCTION(setReadPhoneState, NULL),
        MAP_FUNCTION(setUseVerboseContext, NULL),
        MAP_FUNCTION(setUseLocation, NULL),
        MAP_FUNCTION(injectValue, NULL),
        MAP_FUNCTION(flushDataToMofiler, NULL),
    };
    
    *numFunctionsToSet = sizeof( functionMap ) / sizeof( FRENamedFunction );
	*functionsToSet = functionMap;
}

// The context finalizer is called when the extension's ActionScript code
// calls the ExtensionContext instance's dispose() method.
// If the AIR runtime garbage collector disposes of the ExtensionContext instance, the runtime also calls ContextFinalizer().

void MofilerContextFinalizer(FREContext ctx)
{
	return;
}

// The extension initializer is called the first time the ActionScript side of the extension
// calls ExtensionContext.createExtensionContext() for any context.

void MofilerExtInitializer(void** extDataToSet, FREContextInitializer* ctxInitializerToSet, FREContextFinalizer* ctxFinalizerToSet)
{
	*extDataToSet = NULL;
	*ctxInitializerToSet = &MofilerContextInitializer;
	*ctxFinalizerToSet = &MofilerContextFinalizer;
}

// The extension finalizer is called when the runtime unloads the extension. However, it is not always called.

void MofilerExtFinalizer(void* extData)
{
	return;
}
