//
//  OMEInvite.m
//  ToPlay
//
//  Created by Ozzie Zhang on 8/15/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMEInvite.h"
#import "AppDelegate.h"

@interface OMEInvite ()

// Redefine these properties to make them read/write for internal class accesses and mutations.
@property (nonatomic, assign) CLLocationCoordinate2D coordinate;

@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *subtitle;

@property (nonatomic, copy) NSString *sportType;
@property (nonatomic, copy) NSString *time;

@property (nonatomic, strong) PFObject *object;
@property (nonatomic, strong) PFGeoPoint *geopoint;
@property (nonatomic, strong) PFUser *user;
@property (nonatomic, assign) MKPinAnnotationColor pinColor;

@end

@implementation OMEInvite

- (id)initWithCoordinate:(CLLocationCoordinate2D)coordinate
				andTitle:(NSString *)title
			 andSubtitle:(NSString *)subtitle
	   andSportTypeTitle:(NSString *)sporttypetitle
			andTimeTitle:(NSString *)timetitle{
	self = [super init];
	if (self) {
		//Ozzie Zhang 2014-09-05 switch subtitle with title
		self.coordinate = coordinate;
		self.subtitle = title;
		self.title = subtitle;
		
		self.sportType = sporttypetitle;
		self.time = timetitle;
		
		//self.animatesDrop = NO;
		self.animatesDrop = YES;
	}
	return self;
}

- (id)initWithPFObject:(PFObject *)object {
	self.object = object;
	self.geopoint = [object objectForKey:kOMEParseLocationKey];
	self.user = [object objectForKey:kOMEParseUserKey];
	
	[object fetchIfNeeded];
	CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake(self.geopoint.latitude, self.geopoint.longitude);
	NSString *subtitle = [object objectForKey:kOMEParseTextKey];
	
	//aTitle infomation mush keep match with title in setTitleAndSubtitleOutsideDistance method
	//all this for query one Object from data base
	NSString *userinfoforobject = [[NSString alloc] init];
	userinfoforobject = [[object objectForKey:kOMEParseUserKey] objectForKey:kOMEParseUsernameKey];
	NSString *title = [NSString stringWithFormat:@"%@ %@", userinfoforobject, kOMEParseWantToKey];
	
	NSString *sporttypetitle = [object objectForKey:kOMEParseInviteSportTypeKey];
	NSString *timetitle = [object objectForKey:kOMEParseInviteTimeKey];


	return [self initWithCoordinate:coordinate andTitle:title andSubtitle:subtitle andSportTypeTitle:sporttypetitle andTimeTitle:timetitle];
}

- (BOOL)equalToInvite:(OMEInvite *)invite {
	if (invite == nil) {
		return NO;
	}

	if (invite.object && self.object) {
		// We have a PFObject inside the OMEInvite, use that instead.
		if ([invite.object.objectId isEqualToString:self.object.objectId]) {
			return NO;
		}
		return YES;
	} else {
		// Fallback code:

		if ([invite.title isEqualToString:self.title] &&
			[invite.subtitle isEqualToString:self.subtitle] &&
			invite.coordinate.latitude == self.coordinate.latitude &&
			invite.coordinate.longitude == self.coordinate.longitude  &&
			[invite.sportType isEqualToString:self.sportType] &&
			[invite.time isEqualToString:self.time]) {
			return NO;
		}

		return YES;
	}
}

- (void)setTitleAndSubtitleOutsideDistance:(BOOL)outside {
	if (outside) {
		//Ozzie Zhang 2014-09-05 switch subtitle to title
		self.subtitle = nil;
		self.title = kOMECannotViewInvite;
		self.pinColor = MKPinAnnotationColorPurple;
		
		self.sportType = [self.object objectForKey:kOMEParseInviteSportTypeKey];
		self.time = [self.object objectForKey:kOMEParseInviteTimeKey];
		
	} else {
		//Ozzie Zhang 2014-09-05 switch subtitle to title
		self.subtitle = [self.object objectForKey:kOMEParseTextKey];
		
		
		//self.title infomation mush keep match with aTitle in initWithPFObject method
		//all this for query one Object from data base
		NSString *userinfo = [[NSString alloc] init];
		userinfo = [[self.object objectForKey:kOMEParseUserKey] objectForKey:kOMEParseUsernameKey];
		self.title = [NSString stringWithFormat:@"%@ %@", userinfo, kOMEParseWantToKey];
		
		self.sportType = [self.object objectForKey:kOMEParseInviteSportTypeKey];
		self.time = [self.object objectForKey:kOMEParseInviteTimeKey];
		
		self.pinColor = MKPinAnnotationColorGreen;
		
		//self.enabled = YES;
		//self.image = [UIImage imageNamed:@"pin.png"];
		
		//self.animatesDrop = YES;
		//self.canShowCallout = YES;
	}
}

@end
