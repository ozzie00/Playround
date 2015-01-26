//
//  OMEMessage.m
//  ToPlay
//
//  Created by Ozzie Zhang on 10/4/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMEConstant.h"
#import "OMEMessage.h"
#import "OMEDebug.h"

#define DEBUG 1

@interface OMEMessage ()


// Redefine these properties to make them read/write for internal class accesses and mutations.

@property (nonatomic, assign) CLLocationCoordinate2D coordinate;

// fromUsername, content, time and avatarImage for use by selection UI.
@property (nonatomic, copy) NSString *fromUsername;
@property (nonatomic, strong) PFUser *fromUser;
@property (nonatomic, copy) NSString *toUsername;
@property (nonatomic, strong) PFUser *toUser;
@property (nonatomic, copy) NSString *fromGroup;
@property (nonatomic, copy) NSString *toGroup;
@property (nonatomic, copy) NSString *messageContent;
@property (nonatomic, copy) NSString *sendTime;
@property (nonatomic, copy) UIImage *avatarImage;

// Other properties:
@property (nonatomic, strong) PFObject *object;
@property (nonatomic, strong) PFGeoPoint *geopoint;
@property (nonatomic, strong) PFUser *user;
//@property (nonatomic, assign) BOOL animatesDrop;

//according sport type to set pinImage
@property (nonatomic, assign) MKPinAnnotationColor pinColor;

@property (nonatomic, copy) NSString *className;
// allMessages:
@property (nonatomic, strong) NSMutableArray *messageList;




@end

@implementation OMEMessage {
	
	__block NSMutableArray *_messageListInBlock;
}


@synthesize messageList;




- (id)init {
	self = [super init];
	
	//NSMutableArray *messageList = [NSMutableArray array];
	
	if (self != nil) {
		self.className      = kOMEParseMessageClassKey;
		messageList         = [[NSMutableArray alloc] initWithCapacity:kOMEMessageListLimit];
		_messageListInBlock = messageList;
	}

	return self;
}

- (void)dealloc {
	//free(messageList);
}

- (id)initWithMessage:(PFUser *)atouser
		andToUsername:(NSString *)atousername
		  andFromUser:(PFUser *)afromuser
	  andFromUsername:(NSString *)afromusername
	andmessageContent:(NSString *)amessagecontent
		  andsendTime:(NSString *)asendtime{
	self = [super init];
	if (self) {
		
		self.toUser         = atouser;
		self.toUsername     = atousername;
		self.fromUser       = afromuser;
		self.fromUsername   = afromusername;
		self.messageContent = amessagecontent;
	    self.sendTime       = asendtime;
	}
	return self;
}


- (id)initWithMessagePFObject:(PFObject *)newobject {
	self.toUser         = [newobject objectForKey:kOMEParseMessageToUserKey];
	self.toUsername     = [newobject objectForKey:kOMEParseMessageToUsernameKey];
	self.fromUser       = [newobject objectForKey:kOMEParseMessageFromUserKey];
	self.fromUsername   = [newobject objectForKey:kOMEParseMessageFromUsernameKey];
	self.messageContent = [newobject objectForKey:kOMEParseMessageContentKey];
	self.sendTime       = [newobject objectForKey:kOMEParseMessageSendTimeKey];
	
	return [self initWithMessage:self.toUser andToUsername:self.toUsername andFromUser:self.fromUser andFromUsername:self.fromUsername andmessageContent:self.messageContent andsendTime:self.sendTime];
}


- (BOOL)equalToMessage:(OMEMessage *)newMessage {
	if (newMessage == nil) {
		return NO;
	}
	
	if (newMessage.object && self.object) {
		// We have a PFObject inside the OMEInvite, use that instead.
		if ([newMessage.object.objectId isEqualToString:self.object.objectId]) {
			return NO;
		}
		return YES;
	} else {
		// Fallback code:
		
		if ([newMessage.fromUsername isEqualToString:self.fromUsername] &&
			[newMessage.toUsername isEqualToString:self.toUsername] &&
			[newMessage.messageContent isEqualToString:self.messageContent] &&
			[newMessage.sendTime isEqualToString:self.sendTime]) {
			return NO;
		}
		
		return YES;
	}
}


/*
- (NSMutableArray *)getMessageListfromUser:(PFUser *)user {
	
	//PFObject *object;
	PFQuery *query = [PFQuery queryWithClassName:kOMEParseClassKey];
	
	//kPFCachePolicyCacheThenNetwork
	query.cachePolicy = kPFCachePolicyCacheElseNetwork;
	
	//[query whereKey:kOMEParseLocationKey nearGeoPoint:clickPoint];
	
	//only query the clicked Map Pin detail invite requirement
	query.limit = 1;
	
	//To avoid simultaneous calls of a PFQuery, call [query cancel] before findObjects is called:
	[query cancel];
	
	//Please use getFirstObject, not findObjectsInBackgroundWithBlock
	self.object       = [query getFirstObject];
	
	
	//PFUser *userfrom = [self.object objectForKey:kOMEParseUserKey];
	//NSString *usernamefrom = userfrom.username;
	NSLog(@"object  = %@", self.object);
	
	self.time         = [self.object objectForKey:kOMEParseInviteTimeKey];
	
	
	return self;
}

 */


- (id)getMessageListfromUsername:(NSString *)username {
	
//	[self queryMessageForUsername:username];
	
	
	OMEDebug("[self.messageList count] = %d", [self.messageList count]);
	
	if ([self.messageList count] == 0) {
		
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:kOMEMessageListNoForUser
														message:nil
													   delegate:nil
											  cancelButtonTitle:kOMEParseOK
											  otherButtonTitles:nil];
		[alert show];
		return nil;
	} else {
		
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"have message"
														message:nil
													   delegate:nil
											  cancelButtonTitle:kOMEParseOK
											  otherButtonTitles:nil];
		[alert show];
		return self.messageList;
	}
}

/*
- (void)queryMessageForUsername:(NSString *)username {
	
	PFQuery *query = [PFQuery queryWithClassName:kOMEParseMessageClassKey];
	
	if (username == nil) {
		NSLog(@"%s can not get this newUser Message !", __PRETTY_FUNCTION__);
		
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEMessageNoUserInfo
															message:nil
														   delegate:nil
												  cancelButtonTitle:kOMEParseOK
												  otherButtonTitles:nil, nil];
		[alertView show];
	}
	
	// If no objects are loaded in memory, we look to the cache first to fill the table
	// and then subsequently do a query against the network.
	if ([messageList count] == 0) {
		query.cachePolicy = kPFCachePolicyCacheElseNetwork;//kPFCachePolicyCacheThenNetwork;
	}
	
	// Ozzie Zhang 2014-10-10 kOMEParseMessageFromUsernameKey only for test
	// should use kOMEParseMessagetoUserKey
	// Query for Messages sort of the newest message
	//
	[query whereKey:kOMEParseMessageToUsernameKey equalTo:username];
	//[query includeKey:kOMEParseMessageFromUsernameKey];
	[query orderByAscending:@"createdAt"];
	query.limit = kOMEMessageSearchLimit;
	
	
	//self.object       = [query getFirstObject];
	//OMEDebug(@"search object  = %@", self.object);
	
	
	[query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
		if (error) {
			NSLog(@"message query error!"); // todo why is this ever happening?
		} else {
			
			NSMutableArray *allMessages = [[NSMutableArray alloc] initWithCapacity:kOMEMessageSearchLimit];
			NSMutableArray *newestMessage = [[NSMutableArray alloc] initWithCapacity:kOMEMessageListLimit];
			
			for (PFObject *newobject in objects) {
				OMEMessage *newMessage = [[OMEMessage alloc] initWithMessagePFObject:newobject];
				[allMessages addObject:newMessage];
			}
			
			// sort message according to fromUsername and newest send time
			NSSortDescriptor *fromusername = [[NSSortDescriptor alloc] initWithKey:@"fromUsername" ascending:NO];
			NSSortDescriptor *sendtime = [[NSSortDescriptor alloc] initWithKey:@"sendTime" ascending:NO];
		    NSArray *sortedmessage = [allMessages sortedArrayUsingDescriptors:[NSMutableArray arrayWithObjects:sendtime, fromusername, nil]];
			NSString *usernameKey = kOMEParseNullString;
			
			for (OMEMessage *messagenew in sortedmessage) {
				if ([usernameKey isEqualToString:[messagenew fromUsername]]) {
					
				} else {
					// add new fromUsername message to message list
					usernameKey  = [messagenew fromUsername];
					[newestMessage addObject:messagenew];
					[_messageListInBlock addObject:messagenew];
					
				}
				
			}
			
			// add new message to messageList
			//self.messageList = [newestMessage mutableCopy];
			
			
			//_messageListInBlock = [newestMessage copy];
			
			//[self.messageList arrayByAddingObjectsFromArray:newestMessage];
			//self.messageList = [newestMessage sortedArrayUsingDescriptors:[NSMutableArray arrayWithObjects:sendtime, fromusername, nil  ]];
			
			
			for (int i = 0; i < [_messageListInBlock count];i++) {
				OMEDebug("_messageListInBlock %d %@ %@ %@", i, messageList[i], ((OMEMessage *)messageList[i]).fromUsername, ((OMEMessage *)messageList[i]).sendTime);
			}
			
			
		}
	 
	}];
	
}

*/


@end
