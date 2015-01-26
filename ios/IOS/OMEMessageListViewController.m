//
//  OMEMessageListViewController.m
//  ToPlay
//
//  Created by Ozzie Zhang on 10/5/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMEMessageListViewController.h"

#import "AppDelegate.h"
#import "OMEDebug.h"
#import "OMEConstant.h"
#import "OMEMessage.h"
#import "OMEMessageCell.h"



#import "OMEMessageDetailViewController.h"


@interface OMEMessageListViewController () <OMEMessageDetailViewControllerDelegate>

@property (nonatomic, retain)OMEMessage *message;
@property (nonatomic, retain)NSString *FromUsername;
@property (nonatomic, retain)PFUser *fromuser;
@property (nonatomic, strong) __block NSMutableArray *messageList;
@property (nonatomic, copy) NSString *className;


// NSNotification callback
- (void)haveNewMessageList:(NSNotification *)note;

@end

@implementation OMEMessageListViewController {
	NSMutableArray *_messageListInBlock;
}


//@synthesize messageArray;
@synthesize message;
@synthesize FromUsername;
//@synthesize messageList;


- (void)viewDidLoad {
    [super viewDidLoad];
	
	if (self != nil) {
		self.className      = kOMEParseMessageClassKey;
		self.messageList    = [[NSMutableArray alloc] initWithCapacity:kOMEMessageListLimit];
		_messageListInBlock = self.messageList;
	}

	self.message = [[OMEMessage alloc] init];
	
	//PFUser *newuser = [PFUser currentUser];
	
//	OMEDebug("current username = %@", newuser.username);
	
	//[self queryMessageForUsername:newuser.username];
	
	if (NSClassFromString(kOMEParseRefresh)) {
		// Use the new iOS 6 refresh control.
		UIRefreshControl *refreshControl = [[UIRefreshControl alloc] init];
		self.refreshControl = refreshControl;
		self.refreshControl.tintColor = [UIColor colorWithRed:28.0f/255.0f green:161.0f/255.0f blue:85.0f/255.0f alpha:1.0f];
		[self.refreshControl addTarget:self action:@selector(refreshControlValueChanged:) forControlEvents:UIControlEventValueChanged];
		//self.pullToRefreshEnabled = NO;
	}
	
	
	//[self.tableView reloadData];
	
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
	
	
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(haveNewMessageList:) name:kOMEMessageHaveNewNotification object:nil];
	
	
	
	//UIATarget.localTarget().pushTimeout(20);
	//window.navigationBar().name()["Welcome"].withValueForKey(1, "isVisible");
	//UIATarget.localTarget().popTimeout();
	
}

- (id)init {
	self = [super init];
	
	//NSMutableArray *messageList = [NSMutableArray array];
	
	if (self != nil) {
		self.className      = kOMEParseMessageClassKey;
		self.messageList    = [[NSMutableArray alloc] initWithCapacity:kOMEMessageListLimit];
		_messageListInBlock = self.messageList;
	}
	
	return self;
}

- (id)initWithStyle:(UITableViewStyle)style {
	self = [super initWithStyle:style];
	if (self) {
		// Customize the table:
		
		// The className to query on
	//	self.parseClassName = kOMEParseMessageClassKey;
		
		// The key of the PFObject to display in the label of the default cell style
	//	self.textKey = kOMEParseMessageContentKey;
		
		// Whether the built-in pull-to-refresh is enabled
	//	if (NSClassFromString(@"UIRefreshControl")) {
	//		self.pullToRefreshEnabled = NO;
	//	} else {
	//		self.pullToRefreshEnabled = YES;
	//	}
		
		// Whether the built-in pagination is enabled
	//	self.paginationEnabled = YES;
		
		//Ozzie Zhang 2014-10-07 disable this line code
		// The number of objects to show per page
		// self.objectsPerPage = kOMEInviteSearchLimit;
	}
	//return self;
}


- (void) viewDidAppear:(BOOL)animated {
	
	PFUser *newuser = [PFUser currentUser];
	[self queryMessageForUsername:newuser.username];
	//[self generateMessageListFromMessage];
	
}


- (void) viewWillDisappear:(BOOL)animated {
	
	[self.tableView reloadData];
	
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - Fetch message List


- (void)haveNewMessageList:(NSNotification *)note {
	PFUser *newuser = [PFUser currentUser];
	
	[self queryMessageForUsername:newuser.username];
}

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
		
		return;
	}
	
	// If no objects are loaded in memory, we look to the cache first to fill the table
	// and then subsequently do a query against the network.
	if ([_messageList count] == 0) {
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
			
			//self.messageList = [[NSMutableArray alloc] initWithArray:objects];
			if (/* DISABLES CODE */ (0)) {
			    NSMutableArray *allMessages = [[NSMutableArray alloc] initWithCapacity:kOMEMessageSearchLimit];
			    NSMutableArray *newestMessage = [[NSMutableArray alloc] initWithCapacity:kOMEMessageListLimit];
		
			    allMessages = self.messageList;

			
			    // 1. Find genuinely new posts:
			    NSMutableArray *newMessages = [[NSMutableArray alloc] initWithCapacity:kOMEMessageSearchLimit];
			    // (Cache the objects we make for the search in step 2:)
			    NSMutableArray *allnewMessages = [[NSMutableArray alloc] initWithCapacity:kOMEMessageSearchLimit];
			    for (PFObject *object in objects) {
			    	OMEMessage *newMessage = [[OMEMessage alloc] initWithMessagePFObject:object];
				    [allnewMessages addObject:newMessage];
				    BOOL found = NO;
				    for (OMEMessage *currentMessage in allMessages) {
					    if ([newMessage equalToMessage:currentMessage]) {
						    found = YES;
					    }
				    }
				    if (!found) {
					    [newMessages addObject:newMessage];
				    }
			    }
			    // newMessages now contains our new objects.
			
			    // 2. Find posts in allPosts that didn't make the cut.
			    NSMutableArray *messagesToRemove = [[NSMutableArray alloc] initWithCapacity:kOMEMessageSearchLimit];
			    for (OMEMessage *currentMessage in allMessages) {
				    BOOL found = NO;
				    // Use our object cache from the first loop to save some work.
				    for (OMEMessage *allNewMessage in allnewMessages) {
					    if ([currentMessage equalToMessage:allNewMessage]) {
						    found = YES;
					    }
				    }
				    if (!found) {
					    [messagesToRemove addObject:currentMessage];
				    }
			    }
			    // postsToRemove has objects that didn't come in with our new results.
			
			
			    // At this point, newAllPosts contains a new list of post objects.
			    // We should add everything in newMessages to the map, remove everything in postsToRemove,
			    // and add newMessages to allPosts.
			    [allMessages addObjectsFromArray:newMessages];
			    [allMessages removeObjectsInArray:messagesToRemove];
			
			    OMEDebug("allMessages count = %d", [allMessages count]);
		    }
			
			
			//now use new code for sort
			NSMutableArray *allMessages = [[NSMutableArray alloc] initWithCapacity:kOMEMessageSearchLimit];
			NSMutableArray *newestMessage = [[NSMutableArray alloc] initWithCapacity:kOMEMessageListLimit];

			
			for (PFObject *newobject in objects) {
				OMEMessage *newMessage = [[OMEMessage alloc] initWithMessagePFObject:newobject];
				[allMessages addObject:newMessage];
			}
			
			// sort message according to fromUsername and newest send time
			NSSortDescriptor *fromusername = [[NSSortDescriptor alloc] initWithKey:@"fromUsername" ascending:NO];
			NSSortDescriptor *sendtime     = [[NSSortDescriptor alloc] initWithKey:@"sendTime" ascending:NO];
			NSArray *sortedmessage         = [allMessages sortedArrayUsingDescriptors:[NSMutableArray arrayWithObjects:sendtime, fromusername, nil]];
			NSString *usernameKey          = kOMEParseNullString;
			
			for (OMEMessage *messagenew in sortedmessage) {
				if ([usernameKey isEqualToString:[messagenew fromUsername]]) {
				} else {
					// add new fromUsername message to message list
					usernameKey  = [messagenew fromUsername];
					[newestMessage addObject:messagenew];
					//[_messageList addObject:messagenew];
					//OMEDebug("%@ %@", messagenew.fromUsername, messagenew.sendTime);
				}
			}
			
			self.messageList = [[NSMutableArray alloc] initWithArray:newestMessage];
			
			// add new message to messageList
			//_messageListInBlock = [newestMessage mutableCopy];
			
			
			//_messageListInBlock = [newestMessage copy];
			
			//[self.messageList arrayByAddingObjectsFromArray:newestMessage];
			//self.messageList = [newestMessage sortedArrayUsingDescriptors:[NSMutableArray arrayWithObjects:sendtime, fromusername, nil  ]];
			
			for (int i = 0; i < [_messageList count];i++) {
				//OMEDebug("_messageListInBlock %d %@ %@ %@", i, _messageList[i], ((OMEMessage *)_messageList[i]).fromUsername, ((OMEMessage *)_messageList[i]).sendTime);
			}
		
			
		}
	 
	}];
	
}


- (void)generateMessageListFromMessage {
	
	
	NSMutableArray *allMessages = [[NSMutableArray alloc] initWithArray:self.messageList];
	NSMutableArray *newestMessage = [[NSMutableArray alloc] initWithCapacity:kOMEMessageListLimit];
	
	
	// sort message according to fromUsername and newest send time
	NSSortDescriptor *fromusername = [[NSSortDescriptor alloc] initWithKey:@"fromUsername" ascending:NO];
	NSSortDescriptor *sendtime     = [[NSSortDescriptor alloc] initWithKey:@"sendTime" ascending:NO];
	NSArray *sortedmessage         = [allMessages sortedArrayUsingDescriptors:[NSMutableArray arrayWithObjects:sendtime, fromusername, nil]];
	NSString *usernameKey          = kOMEParseNullString;
	
	for (OMEMessage *messagenew in sortedmessage) {
		if ([usernameKey isEqualToString:[messagenew fromUsername]]) {
		} else {
			// add new fromUsername message to message list
			usernameKey  = [messagenew fromUsername];
			[newestMessage addObject:messagenew];
			[_messageList addObject:messagenew];
			OMEDebug("%@ %@", messagenew.fromUsername, messagenew.sendTime);
		}
	}
	
	self.messageList = [[NSMutableArray alloc] initWithArray:newestMessage];
	

}


//- (void)loadView
//{

//	UITableView *tableView = [[UITableView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame] style:UITableViewStylePlain];
	//tableView.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
	//tableView.delegate = self;
	//tableView.dataSource = self;
	//[tableView reloadData];
 
//	self.view = tableView;
	 
//}
 

#pragma mark - Table view data source
/*
// Override to customize what kind of query to perform on the class. The default is to query for
// all objects ordered by createdAt descending.
- (PFQuery *)queryForTable {
	PFQuery *query = [PFQuery queryWithClassName:kOMEParseMessageClassKey];
	
	// If no objects are loaded in memory, we look to the cache first to fill the table
	// and then subsequently do a query against the network.
	if ([self.objects count] == 0) {
		query.cachePolicy = kPFCachePolicyCacheThenNetwork;
	}
*/
	
 /*
	PFUser *user = self.fromuser;
	NSString *usrername = user.username;
	// Ozzie Zhang 2014-10-10 need change kOMEParseMessageFromUsernameKey, now only for debug
	OMEDebug();
	[query whereKey:kOMEParseMessageToUsernameKey equalTo:usrername];
	[query includeKey:kOMEParseUserKey];
	[query orderByAscending:@"createdAt"];
	*/
/*
	AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
	CLLocation *currentLocation = appDelegate.currentLocation;
	CLLocationAccuracy filterDistance = appDelegate.filterDistance;
	
	// And set the query to look by location
	PFGeoPoint *point = [PFGeoPoint geoPointWithLatitude:currentLocation.coordinate.latitude longitude:currentLocation.coordinate.longitude];
	[query whereKey:kOMEParseLocationKey nearGeoPoint:point withinKilometers:filterDistance / kOMEMetersInAKilometer];
	[query includeKey:kOMEParseUserKey];
	[query orderByAscending:@"createdAt"];

	
	return query;
}
*/


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
	return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	// Number of rows is the number of time zones in the region for the specified section.
	//OMEDebug("[self.messageList count] = %d", [self.messageList count]);
	return [self.messageList count];
	//return 10;
}


- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
	// The header for the section is the region name -- get this from the region at the section index.
	return nil;
}

- (CGFloat)tableView:(UITableView *)tableView  heightForRowAtIndexPath:(NSIndexPath *)indexPath {
	
	//NSLog(@"return kOMEdefaultMessageCellHeight !");
	return kOMEdefaultMessageCellHeight;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	//BOOL cellIsRight = [[[object objectForKey:kOMEParseMessageToUserKey] objectForKey:kOMEParseMessageToUsernameKey] isEqualToString:[[PFUser currentUser] username]];
	
	OMEMessageCell *cell = [self.tableView dequeueReusableCellWithIdentifier:kOMEMessageCell];
	
	if (cell == nil) {
		cell = [[OMEMessageCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:kOMEMessageCell];
	}
 
	//cell.nameLabel.text = [NSString stringWithFormat:@"%d", ];
	
	
//	NSLog(@"indexPath.row = %d", indexPath.row);
    self.message  = [self.messageList objectAtIndex:indexPath.row];
	
	//OMEDebug(" self.message.fromUsername = %@", self.message.fromUsername);
	cell.nameLabel.text    = self.message.fromUsername;
	cell.contentLabel.text = self.message.messageContent;
	cell.timeLabel.text    = self.message.sendTime;
	cell.avatarImageView.image = [UIImage imageNamed:@"he40x40icon.png"];// resizableImageWithCapInsets:UIEdgeInsetsMake(5, 8, 5, 8)];
 
	//cell.nameLabel.text = [object objectForKey:kOMEParseTextKey];
	
	//OMEDebug("%@", cell.nameLabel.text);
	return cell;
}

- (NSString *)getFromUsernameDidSelectMessage: (OMEMessageDetailViewController *)controller {
	
	return self.message.fromUsername;
	
}

- (PFUser *)getFromUserDidSelectMessage: (OMEMessageDetailViewController *)controller {
	
	return self.message.fromUser;
	
}

- (NSUInteger)getMessageListCountDidSelectMessage:(OMEMessageDetailViewController *)controller {
	return [self.messageList count];
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	
	UITableViewCell *cell = [self.tableView cellForRowAtIndexPath:indexPath];
	
	OMEMessageDetailViewController *messageDetailViewController = [[OMEMessageDetailViewController alloc] initWithNibName:nil bundle:nil];
	messageDetailViewController.delegate = self;

	self.message = [self.messageList objectAtIndex:indexPath.row];
	[self.navigationController pushViewController:messageDetailViewController animated:YES];
	
}

/*
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:<#@"reuseIdentifier"#> forIndexPath:indexPath];
    
    // Configure the cell...
    
    return cell;
}
*/

/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/


// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
	
	[self.tableView beginUpdates];
	
	@synchronized(self.messageList) {
		if (editingStyle == UITableViewCellEditingStyleDelete) {
			
			if (self.messageList.count > 0) {
				[self.messageList removeObjectAtIndex:indexPath.row];
				[self.tableView  deleteRowsAtIndexPaths:[NSMutableArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
			}
		} else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
		}
	}
	
	[self.tableView endUpdates];
	
	[self.tableView reloadData];
}


- (void)refreshControlValueChanged:(UIRefreshControl *)refreshControl {
	//[self loadObjects];
	return nil;
}



/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath {
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/


@end
