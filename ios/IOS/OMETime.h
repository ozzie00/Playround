//
//  OMETime.h
//  ToPlay
//
//  Created by Ozzie Zhang on 10/9/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface OMETime : NSObject

+ (NSString *)getDateStringFromDate:(float)Date;
+ (float)getDateFromDateString:(NSString *)timestring;
+ (NSString *)getCurrentTime;
+ (NSString *)getTomorrowTime;

@end
