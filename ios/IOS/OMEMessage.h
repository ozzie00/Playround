//
//  OMEMessage.h
//  ToPlay
//
//  Created by Ozzie Zhang on 10/4/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>
#import <Parse/Parse.h>

@interface OMEMessage : NSObject


// Center latitude and longitude of the annotion view.
// The implementation of this property must be KVO compliant.
@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;

// message basic properties
@property (nonatomic, readonly, copy) NSString *fromUsername;
@property (nonatomic, readonly, strong) PFUser *fromUser;
@property (nonatomic, readonly, copy) NSString *toUsername;
@property (nonatomic, readonly, strong) PFUser *toUser;
@property (nonatomic, readonly, copy) NSString *fromGroup;
@property (nonatomic, readonly, copy) NSString *toGroup;
@property (nonatomic, readonly, copy) NSString *messageContent;
@property (nonatomic, readonly, copy) NSString *sendTime;
@property (nonatomic, readonly, copy) UIImage *avatarImage;

// Other properties:
@property (nonatomic, readonly, strong) PFObject *object;
@property (nonatomic, readonly, strong) PFGeoPoint *geopoint;
@property (nonatomic, readonly, strong) PFUser *user;
//@property (nonatomic, assign) BOOL animatesDrop;

//according sport type to set pinImage
@property (nonatomic, readonly) MKPinAnnotationColor pinColor;
//@property (nonatomic, readonly) NSMutableArray *messageList;
//@property (nonatomic, readonly) NSArray *messageList;


// Designated initializer

- (id)initWithMessage:(PFUser *)atouser
		andToUsername:(NSString *)atousername
		  andFromUser:(PFUser *)afromuser
	  andFromUsername:(NSString *)afromusername
	andmessageContent:(NSString *)amessagecontent
		  andsendTime:(NSString *)asendtime;

- (id)initWithMessagePFObject:(PFObject *)object;
- (BOOL)equalToMessage:(OMEMessage *)newMessage;

- (void)queryNewestMessageForUser:(PFUser *)currentUser;
- (void)queryNewestMessageFromfromUsername:(NSString *)username;

- (NSMutableArray *)getMessageListfromUsername:(NSString *)username;
- (OMEMessage *)getMessageListfromUser:(PFUser *)user;


@end
