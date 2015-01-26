//
//  OMEInviteRequirement.m
//  ToPlay
//
//  Created by Ozzie Zhang on 9/25/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMEInviteRequirement.h"

#import "OMEDebug.h"

@interface OMEInviteRequirement ()

// Redefine these properties to make them read/write for internal class accesses and mutations.
@property (nonatomic, assign) CLLocationCoordinate2D coordinate;

@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *subtitle;

// Title and subtitle for use by selection UI.
@property (nonatomic, copy) NSString *username;
@property (nonatomic, copy) NSString *fromUsername;
@property (nonatomic, copy) NSString *userLevel;
@property (nonatomic, copy) NSString *playerIcon;

@property (nonatomic, copy) NSString *sportType;
@property (nonatomic, copy) NSString *playerNumber;
@property (nonatomic, copy) NSString *playerLevel;
@property (nonatomic, copy) NSString *time;
@property (nonatomic, copy) NSString *submitTime;
@property (nonatomic, copy) NSString *court;
@property (nonatomic, copy) NSString *fee;
@property (nonatomic, copy) NSString *other;

 
// Other properties:
@property (nonatomic, strong) PFObject *object;
@property (nonatomic, strong) PFGeoPoint *geopoint;
@property (nonatomic, strong) PFUser *user;
@property (nonatomic, strong) PFUser *fromUser;

//according sport type to set pinImage
@property (nonatomic, assign) MKPinAnnotationColor pinColor;

// Invite Requirements:
@property (nonatomic, strong) __block NSMutableArray *allInviteRequirements;



- (void)queryForInviteRequirementAtLocation:(CLLocation *)currentLocation;


@end





@implementation OMEInviteRequirement




@synthesize coordinate;

@synthesize title;
@synthesize subtitle;

// Title and subtitle for use by selection UI.
@synthesize username;
@synthesize fromUsername;
@synthesize userLevel;
@synthesize playerIcon;

@synthesize sportType;
@synthesize playerNumber;
@synthesize playerLevel;
@synthesize time;
@synthesize court;
@synthesize fee;
@synthesize other;


// Other properties:
@synthesize object;
@synthesize geopoint;
@synthesize user;
@synthesize fromUser;

//according sport type to set pinImage
@synthesize pinColor;


@synthesize allInviteRequirements;

- (id)initWithInviteRequirement:(CLLocationCoordinate2D)acoordinate
					   andTitle:(NSString *)atitle
					andSubtitle:(NSString *)asubtitle
			  andSportTypeTitle:(NSString *)asporttypetitle
				   andTimeTitle:(NSString *)atimetitle{
	self = [super init];
	if (self) {
		//Ozzie Zhang 2014-09-05 switch subtitle with title
		self.coordinate = acoordinate;
		self.subtitle = atitle;
		self.title = asubtitle;
		
		self.sportType = asporttypetitle;

		self.animatesDrop = YES;
		
		self.allInviteRequirements = [[NSMutableArray alloc] initWithCapacity:kOMEInviteRequirementSearchLimit];
	}
	return self;
}

- (id)initWithPFObject:(PFObject *)newObject {
	self.object       = newObject;
	self.geopoint     = [newObject objectForKey:kOMEParseLocationKey];
	self.user         = [newObject objectForKey:kOMEParseUserKey];
	self.fromUser     = [newObject objectForKey:kOMEParseInviteFromUserKey];
	self.username     = [[newObject objectForKey:kOMEParseUserKey] objectForKey:kOMEParseUsernameKey];
	self.fromUsername = [newObject objectForKey:kOMEParseInviteFromUsernameKey];
	
	self.userLevel    = [newObject objectForKey:kOMEParseUserLevelKey];
	self.playerIcon   = [newObject objectForKey:kOMEParseUserIconKey];
	
	self.sportType    = [newObject objectForKey:kOMEParseInviteSportTypeKey];
	self.playerNumber = [newObject objectForKey:kOMEParseInvitePlayerNumberKey];
	self.playerLevel  = [newObject objectForKey:kOMEParseInvitePlayerLevelKey];
	self.time         = [newObject objectForKey:kOMEParseInviteTimeKey];
	self.court        = [newObject objectForKey:kOMEParseInviteCourtKey];
	self.fee          = [newObject objectForKey:kOMEParseInviteFeeKey];
	self.other        = [newObject objectForKey:kOMEParseInviteOtherInfoKey];
	self.submitTime   = [newObject objectForKey:kOMEParseInviteSubmitTimeKey];

	if (self) {
		self.object = newObject;
		self.user = newObject[kOMEParseUserKey];
	}
	return self;

}


 
- (BOOL)equalToInviteRequirement:(OMEInviteRequirement *)inviterequirement {
	if (inviterequirement == nil) {
		return NO;
	}
	
	if (inviterequirement.object && self.object) {
		// We have a PFObject inside the OMEInvite, use that instead.
		if ([inviterequirement.object.objectId compare:self.object.objectId] != NSOrderedSame) {
			return NO;
		}
		return YES;
	} else {
		// Fallback code:
		
		if ([inviterequirement.title isEqualToString:self.title] &&
			[inviterequirement.subtitle isEqualToString:self.subtitle] &&
			inviterequirement.coordinate.latitude != self.coordinate.latitude &&
			inviterequirement.coordinate.longitude != self.coordinate.longitude  &&
			[inviterequirement.username isEqualToString:self.username] &&
			[inviterequirement.fromUsername isEqualToString:self.fromUsername] &&
			[inviterequirement.userLevel isEqualToString:self.userLevel] &&
			[inviterequirement.sportType isEqualToString:self.sportType] &&
			[inviterequirement.time isEqualToString:self.time] &&
			[inviterequirement.submitTime isEqualToString:self.submitTime] &&
			[inviterequirement.playerNumber isEqualToString:self.playerNumber] &&
			[inviterequirement.playerLevel isEqualToString:self.playerLevel] &&
			[inviterequirement.court isEqualToString:self.court] &&
			[inviterequirement.fee isEqualToString:self.fee] &&
			[inviterequirement.other isEqualToString:self.other]) {
			return NO;
		}
		
		return YES;
	}
}


- (void)setPlayerDetailInviteRequirementOutsideDistance:(BOOL)outside {
	if (outside) {
		//Ozzie Zhang 2014-09-05 switch subtitle to title
		self.subtitle = nil;
		self.title = kOMECannotViewInvite;
		self.pinColor = MKPinAnnotationColorPurple;
		
		//Ozzie Zhang 2014-09-29 
		//self.sportType = [self.object objectForKey:kOMEParseInviteSportTypeKey];
		//self.time = [self.object objectForKey:kOMEParseInviteTimeKey];
		
	} else {
		//Ozzie Zhang 2014-09-05 switch subtitle to title
		
		NSLog(@"OMEInviteRequirement setPlayerDetailInviteRequirementOutsideDistance");
		
		//self.title infomation mush keep match with aTitle in initWithPFObject method
		//all this for query one Object from data base
		NSString *userinfo = [[NSString alloc] init];
		userinfo = [[self.object objectForKey:kOMEParseUserKey] objectForKey:kOMEParseUsernameKey];
	
		self.title        = [NSString stringWithFormat:@"%@ %@", userinfo, kOMEParseWantToKey];
		self.subtitle     = [self.object objectForKey:kOMEParseTextKey];
		self.user         = [self.object objectForKey:kOMEParseUserKey];
		self.fromUser     = [self.object objectForKey:kOMEParseInviteFromUserKey];
		
		//Ozzie Zhang 2014-10-01 please enable these line code
		self.username     = self.user.username; //[self.object objectForKey:kOMEParseUsernameKey];
		self.fromUsername = [self.object objectForKey:kOMEParseInviteFromUsernameKey];
 		self.sportType    = [self.object objectForKey:kOMEParseInviteSportTypeKey];
		self.playerNumber = [self.object objectForKey:kOMEParseInvitePlayerNumberKey];
		self.playerLevel  = [self.object objectForKey:kOMEParseInvitePlayerLevelKey];
		
		//Ozzie Zhang 2014-10-01 please enable these line code
		self.time         = [self.object objectForKey:kOMEParseInviteTimeKey];
		self.court        = [self.object objectForKey:kOMEParseInviteCourtKey];
		self.fee          = [self.object objectForKey:kOMEParseInviteFeeKey];
		self.other        = [self.object objectForKey:kOMEParseInviteOtherInfoKey];
		self.submitTime   = [self.object objectForKey:kOMEParseInviteSubmitTimeKey];
		self.pinColor     = MKPinAnnotationColorGreen;
		
		}
}

//Ozzie Zhang 2014-09-29 default outside value is NO
- (void)getPlayerDetailInviteRequirementOutsideDistance:(BOOL)outside  InviteRequirement:(OMEInviteRequirement *)inviterequirement{
	
	if (outside) {
		self.subtitle = nil;
		self.title = kOMECannotViewInvite;
		self.pinColor = MKPinAnnotationColorPurple;
		
		inviterequirement.title        = kOMECannotViewInvite;
		inviterequirement.subtitle     = nil;

	} else {
	    inviterequirement.title        = self.title;
		inviterequirement.subtitle     = self.subtitle;
		inviterequirement.userLevel    = self.userLevel;
		inviterequirement.sportType    = self.sportType;
		inviterequirement.playerNumber = self.playerNumber;
		inviterequirement.playerLevel  = self.playerLevel;
		inviterequirement.time         = self.time;
		inviterequirement.court        = self.court;
		inviterequirement.fee          = self.fee;
		inviterequirement.other        = self.other;
		inviterequirement.pinColor     = self.pinColor;
	}
}



- (id)initInviteRequirementWithLocation:(CLLocation *)clickedMapPinPlayerLocation {
	
	PFGeoPoint *clickPoint = [PFGeoPoint geoPointWithLatitude:clickedMapPinPlayerLocation.coordinate.latitude longitude:clickedMapPinPlayerLocation.coordinate.longitude];
	
	//PFObject *object;
	PFQuery *query = [PFQuery queryWithClassName:kOMEParseClassKey];
	
    //kPFCachePolicyCacheThenNetwork
	query.cachePolicy = kPFCachePolicyCacheElseNetwork;
	[query whereKey:kOMEParseLocationKey nearGeoPoint:clickPoint];
	
	//only query the clicked Map Pin detail invite requirement
	query.limit = 1;
	
	//To avoid simultaneous calls of a PFQuery, call [query cancel] before findObjects is called:
	[query cancel];
	
	//Please use getFirstObject, not findObjectsInBackgroundWithBlock
	
	self.object       = [query getFirstObject];
	
	//self.object = [query getFirstObjectInBackgroundWithBlock];
	
	//PFUser *userfrom = [self.object objectForKey:kOMEParseUserKey];
	//NSString *usernamefrom = userfrom.username;
	//NSLog(@"object  = %@", self.object);
	
	self.geopoint     = [self.object objectForKey:kOMEParseLocationKey];
	self.user         = [self.object objectForKey:kOMEParseUserKey];
	self.username     = [self.object objectForKey:kOMEParseUsernameKey];
	self.fromUser     = [self.object objectForKey:kOMEParseInviteFromUserKey];
	self.fromUsername = [self.object objectForKey:kOMEParseInviteFromUsernameKey];
	

	//NSLog(@"self.user  = %@", self.user);
	//NSLog(@"self.username = %@", self.user.username);
	//Ozzie Zhang 2014-10-01 remember debug username issue
//	if (self.user.username == nil) {
//	    self.username = kOMEParseNoUsername;
//		NSLog(@" no username !");
//	} else {
//		self.username = self.user.username;
//		NSLog(@" username = %@", self.username);
//	}
	//OMEDebug("please check this line code");
	self.userLevel    = [self.object objectForKey:kOMEParseUserLevelKey];
	self.playerIcon   = [self.object objectForKey:kOMEParseUserIconKey];
	
	
	self.sportType    = [self.object objectForKey:kOMEParseInviteSportTypeKey];
	self.playerNumber = [self.object objectForKey:kOMEParseInvitePlayerNumberKey];
	self.playerLevel  = [self.object objectForKey:kOMEParseInvitePlayerLevelKey];
	self.time         = [self.object objectForKey:kOMEParseInviteTimeKey];
	self.court        = [self.object objectForKey:kOMEParseInviteCourtKey];
	self.fee          = [self.object objectForKey:kOMEParseInviteFeeKey];
	self.other        = [self.object objectForKey:kOMEParseInviteOtherInfoKey];

	
	return self;
}



/*
- (id)initInviteRequirementWithLocation:(CLLocation *)clickedMapPinPlayerLocation {
	
     [self queryForInviteRequirementAtLocation:clickedMapPinPlayerLocation];
	
	NSLog(@" current InviteRequirement %d %@", [allInviteRequirements count], allInviteRequirements);
	
	return [self.allInviteRequirements objectAtIndex:0];
}

- (void)queryForInviteRequirementAtLocation:(CLLocation *)currentLocation {
	
	NSLog(@" enter queryForInviteRequirementAtLocation %@", currentLocation);
	
	PFQuery *query = [PFQuery queryWithClassName:kOMEParseClassKey];
	
	if (currentLocation == nil) {
		NSLog(@"%s can not get current location, please turn on your location service !", __PRETTY_FUNCTION__);
		
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"please turn on your location service !"
															message:nil
														   delegate:self
												  cancelButtonTitle:@"OK"
												  otherButtonTitles:nil, nil];
		[alertView show];
	}
	
	// If no objects are loaded in memory, we look to the cache first to fill the table
	// and then subsequently do a query against the network.
	if ([self.allInviteRequirements count] == 0) {
		
		query.cachePolicy = kPFCachePolicyCacheThenNetwork;
	}
	
	// Query for posts sort of kind of near our current location.
	PFGeoPoint *point = [PFGeoPoint geoPointWithLatitude:currentLocation.coordinate.latitude longitude:currentLocation.coordinate.longitude];
	[query whereKey:kOMEParseLocationKey nearGeoPoint:point withinKilometers:kOMEInviteMaximumSearchDistance];
	//[query includeKey:kOMEParseUserKey];
	query.limit = 1;
	
	//[query cancel];

	
	NSLog(@"self.allInviteRequirements = %d", [self.allInviteRequirements count]);
	
	[query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
		if (error) {
			NSLog(@"location query error!"); // todo why is this ever happening?
		} else {
			// We need to make new post objects from objects,
			// and update allPosts and the map to reflect this new array.
			// But we don't want to remove all annotations from the mapview blindly,
			// so let's do some work to figure out what's new and what needs removing.
			
			NSLog(@"begin query !");
			
			
			
			// 1. Find genuinely new posts:
			NSMutableArray *newInviteRequirements = [[NSMutableArray alloc] initWithCapacity:kOMEInviteSearchLimit];
			// (Cache the objects we make for the search in step 2:)
			for (PFObject *newobject in objects) {
				OMEInviteRequirement *newInviteRequirement = [[OMEInviteRequirement alloc] initWithPFObject:newobject];
				[newInviteRequirements addObject:newInviteRequirement];
				NSLog(@"begin addObject !");

			}
			// newPosts now contains our new objects.
			
			
			// At this point, newAllPosts contains a new list of post objects.
			// We should add everything in newPosts to the map, remove everything in postsToRemove,
			// and add newPosts to allPosts.
			//[self.allInviteRequirements addObjectsFromArray:newInviteRequirements];
			
			self.allInviteRequirements = [[NSMutableArray alloc] initWithArray:newInviteRequirements];
			
			NSLog(@"add  count = %d", [self.allInviteRequirements count]);
		}
	 
	}];
 
 
}
*/


@end