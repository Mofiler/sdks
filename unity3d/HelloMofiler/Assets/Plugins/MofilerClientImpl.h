//
//  MofilerClientImpl.h
//  MofilerExt
//
//  Created by Damian Hernaez on 4/19/17.
//  Copyright © 2017 Todsaporn Banjerdkit (katopz). All rights reserved.
//

#ifndef MofilerClientImpl_h
#define MofilerClientImpl_h


#endif /* MofilerClientImpl_h */
#import <Foundation/Foundation.h>



@interface MofilerUnityDelegate : NSObject

//-(NSString*) getAppKey;
- (bool) setAppKey:(const char*)appKeyString;
- (bool) setURL:(const char*)urlString;
- (bool)  flushDataToMofiler;
- (bool) setAppName:(const char*)appNameString;
- (bool) addIdentity:(const char*)key
                    :(const char*)value ;
- (bool) injectValue:(const char*)key
                    :(const char*)value ;

- (bool)  setUseVerboseContext: (bool) state;
- (bool)  SetUseLocation: (bool) state;


@end
