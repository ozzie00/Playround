//
//  OMETime.m
//  ToPlay
//
//  Created by Ozzie Zhang on 10/9/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMETime.h"

@implementation OMETime

+ (NSString *)getDateStringFromTimestamp:(float)timestamp {
	
	NSTimeInterval interval = timestamp;
	NSDate *date = [NSDate dateWithTimeIntervalSince1970:interval];
	NSDateFormatter *formatter=[[NSDateFormatter alloc]init];
	[formatter setTimeStyle:NSDateFormatterShortStyle];
	NSString *datestring=[formatter stringFromDate:date];
	
	NSLog(@"date: %@", datestring);
	
	return datestring;
}


+ (float)getTimestampFromDateString:(NSString *)datestring {
	
	// Convert string to date object
	NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
	[dateFormat setDateFormat:@"MM DD YYYY HH:mm "];
	NSDate *datenew = [dateFormat dateFromString:datestring];
	
	NSDateFormatter *formatternew=[[NSDateFormatter alloc]init];
	[formatternew setTimeStyle:NSDateFormatterShortStyle];
	NSString *_datenew=[formatternew stringFromDate:datenew];
	
	
	NSLog(@"_datenew: %@", _datenew);
	
}

+ (NSString *)getCurrentTime {
	
	// get current time
//	NSTimeInterval secondsPerDay = 24 * 60 * 60;
	NSDate *today, *tomorrow;
	today = [NSDate date];
	NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
	
	//tomorrow = [today dateByAddingTimeInterval: secondsPerDay];
	
	[dateFormatter setDateFormat:@"MMM dd, YYYY HH:mm"];
	
	NSString *currentTime = [dateFormatter stringFromDate:today];
	
	return currentTime;
}

+ (NSString *)getTomorrowTime {
	
	// get tomorrow time
	NSTimeInterval secondsPerDay = 24 * 60 * 60;
	NSDate *today, *tomorrow;
	today = [NSDate date];
	NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
	
	tomorrow = [today dateByAddingTimeInterval: secondsPerDay];
	
	[dateFormatter setDateFormat:@"MMM dd, YYYY HH:mm"];
	
	NSString *tomorrowTime = [dateFormatter stringFromDate:tomorrow];
	
	return tomorrowTime;
}



@end
