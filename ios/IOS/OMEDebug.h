//
//  OMEDebug.h
//  ToPlay
//
//  Created by Ozzie Zhang on 10/9/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#ifndef ToPlay_OMEDebug_h
#define ToPlay_OMEDebug_h

#import <Foundation/Foundation.h>

#ifdef DEBUG
#   define OMEDebug(fmt, ...) NSLog((@"[OME Log]%s [Line %d] " fmt), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__)
#else
#   define OMEDebug(...)
#endif

// OMEALog always displays output regardless of the DEBUG setting
#define OMEADebug(fmt, ...) NSLog((@"[OME Log]%s [Line %d] " fmt), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__)

@interface OMEDebug : NSObject

@end


#endif