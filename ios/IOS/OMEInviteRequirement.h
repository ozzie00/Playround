//
//  OMEInviteRequirement.h
//  ToPlay
//
//  Created by Ozzie Zhang on 9/25/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>
#import <Parse/Parse.h>

#import "OMEInviteRequirement.h"
#import "AppDelegate.h"

@interface OMEInviteRequirement : NSObject <MKAnnotation>

typedef enum {
	OMEInviteRequirementFeeAA = 0,
	OMEInviteRequirementFeeFree,
	OMEInviteRequirementNumberOfSections
}kOMEInviteRequirementFee;

// Center latitude and longitude of the annotion view.
// The implementation of this property must be KVO compliant.
@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;

// Title and subtitle for use by selection UI.
@property (nonatomic, readonly, copy) NSString *title;
@property (nonatomic, readonly, copy) NSString *subtitle;

@property (nonatomic, readonly, copy) NSString *fromUsername;
@property (nonatomic, readonly, copy) NSString *username;
@property (nonatomic, readonly, copy) NSString *userLevel;
@property (nonatomic, readonly, copy) NSString *playerIcon;

@property (nonatomic, readonly, copy) NSString *sportType;
@property (nonatomic, readonly, copy) NSString *playerNumber;
@property (nonatomic, readonly, copy) NSString *playerLevel;
@property (nonatomic, readonly, copy) NSString *time;
@property (nonatomic, readonly, copy) NSString *court;
@property (nonatomic, readonly, copy) NSString *fee;
@property (nonatomic, readonly, copy) NSString *other;
@property (nonatomic, readonly, copy) NSString *submitTime;


// Other properties:
@property (nonatomic, readonly, strong) PFObject *object;
@property (nonatomic, readonly, strong) PFGeoPoint *geopoint;
@property (nonatomic, readonly, strong) PFUser *user;
@property (nonatomic, readonly, strong) PFUser *fromUser;
@property (nonatomic, assign) BOOL animatesDrop;

//according sport type to set pinImage
@property (nonatomic, readonly) MKPinAnnotationColor pinColor;


// Designated initializer.
- (id)initWithInviteRequirement:(CLLocationCoordinate2D)coordinate
					   andTitle:(NSString *)title
					andSubtitle:(NSString *)subtitle
			  andSportTypeTitle:(NSString *)sporttypetitle
				   andTimeTitle:(NSString *)timetitle;
//- (id)initWithPFObject:(PFObject *)object;
- (BOOL)equalToInviteRequirement:(OMEInviteRequirement *)inviterequirement;

- (void)setPlayerDetailInviteRequirementOutsideDistance:(BOOL)outside;

//- (CLLocationCoordinate2D *)getPlayerDetailInviteRequirementLocation:(CLLocationCoordinate2D)coordinate;

- (void)getPlayerDetailInviteRequirementOutsideDistance:(BOOL)outside  InviteRequirement:(OMEInviteRequirement *)inviterequirement;

- (id)initInviteRequirementWithLocation:(CLLocation *)clickedMapPinPlayerLocation;


@end
