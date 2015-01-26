//
//  OMEChatRoomTableViewController.m
//  ToPlay
//
//  Created by Ozzie Zhang on 8/15/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

static CGFloat const kOMEChatRoomTableViewFontSize = 12.0f;
CGFloat kOMEChatRoomTableViewCellWidth = 230.0f; // subject to change.


// Cell dimension and positioning constants
static CGFloat const kOMECellPaddingTop = 5.0f;
static CGFloat const kOMECellPaddingBottom = 1.0f;
static CGFloat const kOMECellPaddingSides = 0.0f;
static CGFloat const kOMECellTextPaddingTop = 6.0f;
static CGFloat const kOMECellTextPaddingBottom = 5.0f;
static CGFloat const kOMECellTextPaddingSides = 5.0f;

static CGFloat const kOMECellUsernameHeight = 15.0f;
static CGFloat const kOMECellBackgroundHeight = 32.0f;
static CGFloat const kOMECellBackgroundffset = kOMECellBackgroundHeight - kOMECellUsernameHeight;

static CGFloat const KOMECellAvatarPaddingTop = 0.0f;
static CGFloat const kOMECellAvatarPaddingBottom = 10.0f;
static CGFloat const kOMECellAvatarPaddingSides = 8.0f;

static CGFloat const kOMECellAvatarWidth            = 40.0f;
static CGFloat const kOMECellAvatarHeight           = 40.0f;


// TableViewCell ContentView tags
static NSInteger kOMECellBackgroundTag = 2;
static NSInteger kOMECellTextLabelTag = 3;
static NSInteger kOMECellNameLabelTag = 4;

static NSInteger kOMECellTimeBackgroundTag = 5;
static NSInteger kOMECellTimeTextLabelTag = 6;
static NSInteger kOMECellTimeNameLabelTag = 7;

static NSInteger kOMECellUserBackgroundTag = 8;


static NSUInteger const kOMETableViewMainSection = 0;

#import "OMEChatRoomTableViewController.h"

#import "AppDelegate.h"

#import "OMEInviteTableViewCell.h"
#import "OMEInvite.h"
#import "OMEDebug.h"

#define DEBUG 1


@interface OMEChatRoomTableViewController ()

// NSNotification callbacks
- (void)distanceFilterDidChange:(NSNotification *)note;
- (void)locationDidChange:(NSNotification *)note;
- (void)postWasCreated:(NSNotification *)note;

@end

@implementation OMEChatRoomTableViewController

- (void)dealloc {
	[[NSNotificationCenter defaultCenter] removeObserver:self name:kOMEFilterDistanceChangeNotification object:nil];
	[[NSNotificationCenter defaultCenter] removeObserver:self name:kOMELocationChangeNotification object:nil];
	[[NSNotificationCenter defaultCenter] removeObserver:self name:kOMEInviteCreatedNotification object:nil];
}

- (id)initWithStyle:(UITableViewStyle)style {
	self = [super initWithStyle:style];
	if (self) {
		// Customize the table:

		// The className to query on
		self.parseClassName = kOMEParseClassKey;

		// The key of the PFObject to display in the label of the default cell style
		self.textKey = kOMEParseTextKey;

        // Whether the built-in pull-to-refresh is enabled
        if (NSClassFromString(kOMEParseRefresh)) {
            self.pullToRefreshEnabled = NO;
        } else {
            self.pullToRefreshEnabled = YES;
        }
		
		// Whether the built-in pagination is enabled
		self.paginationEnabled = YES;

		// The number of objects to show per page
		self.objectsPerPage = kOMEInviteSearchLimit;
	}
	return self;
}


#pragma mark - UIViewController

- (void)viewDidLoad {
	[super viewDidLoad];

	if (NSClassFromString(kOMEParseRefresh)) {
        // Use the new iOS 6 refresh control.
        UIRefreshControl *refreshControl = [[UIRefreshControl alloc] init];
        self.refreshControl = refreshControl;
        self.refreshControl.tintColor = [UIColor colorWithRed:28.0f/255.0f green:161.0f/255.0f blue:85.0f/255.0f alpha:1.0f];
        [self.refreshControl addTarget:self action:@selector(refreshControlValueChanged:) forControlEvents:UIControlEventValueChanged];
        self.pullToRefreshEnabled = NO;
    }
	
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(distanceFilterDidChange:) name:kOMEFilterDistanceChangeNotification object:nil];
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(locationDidChange:) name:kOMELocationChangeNotification object:nil];
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(postWasCreated:) name:kOMEInviteCreatedNotification object:nil];
	
	self.tableView.backgroundColor = [UIColor clearColor];
	self.tableView.separatorColor = [UIColor clearColor];
	
	// define kOMEMessageDetailTableViewCellWidth
	kOMEChatRoomTableViewCellWidth = self.tableView.frame.size.width - kOMECellAvatarPaddingSides -kOMECellAvatarWidth - kOMECellAvatarPaddingSides - kOMECellAvatarPaddingSides - kOMECellAvatarPaddingSides - kOMECellAvatarPaddingSides;
	

}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
	// Return YES for supported orientations
	return (interfaceOrientation == UIInterfaceOrientationPortrait);
}


#pragma mark - PFQueryTableViewController

- (void)objectsDidLoad:(NSError *)error {
    [super objectsDidLoad:error];
    
    // This method is called every time objects are loaded from Parse via the PFQuery
    if (NSClassFromString(kOMEParseRefresh)) {
        [self.refreshControl endRefreshing];
    }
}

- (void)objectsWillLoad {
    [super objectsWillLoad];
    
    // This method is called before a PFQuery is fired to get more objects
}

// Override to customize what kind of query to perform on the class. The default is to query for
// all objects ordered by createdAt descending.
- (PFQuery *)queryForTable {
	PFQuery *query = [PFQuery queryWithClassName:self.parseClassName];

	// If no objects are loaded in memory, we look to the cache first to fill the table
	// and then subsequently do a query against the network.
	if ([self.objects count] == 0) {
		query.cachePolicy = kPFCachePolicyCacheThenNetwork;
	}

	// Query for posts near our current location.

	// Get our current location:
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



// Override to customize the look of a cell representing an object. The default is to display
// a UITableViewCellStyleDefault style cell with the label being the first key in the object. 

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath object:(PFObject *)object {
	
	// Reuse identifiers for left and right cells
	static NSString *RightCellIdentifier = @"RightCell";
	static NSString *LeftCellIdentifier = @"LeftCell";
	static NSString *TimeCellIdentifier = @"TimeCell";

	// Try to reuse a cell
	BOOL cellIsRight = [[[object objectForKey:kOMEParseUserKey] objectForKey:kOMEParseUsernameKey] isEqualToString:[[PFUser currentUser] username]];
	BOOL cellIsLeft = !cellIsRight;
	//BOOL cellIsCenter = [object objectForKey:kOMEParseInviteTimeKey];
	
	UITableViewCell *cell;
	if (cellIsRight) { // User's post so create blue bubble
		cell = [tableView dequeueReusableCellWithIdentifier:RightCellIdentifier];
		if (cell == nil) {
			cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:RightCellIdentifier];
			
			OMEDebug("this is right cell !");
			
			UIImageView *backgroundImage = [[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"blueBubble.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(15.0f, 11.0f, 16.0f, 11.0f)]];
			[backgroundImage setTag:kOMECellBackgroundTag];
			[cell.contentView addSubview:backgroundImage];

			UILabel *textLabel = [[UILabel alloc] init];
			[textLabel setTag:kOMECellTextLabelTag];
			[cell.contentView addSubview:textLabel];
			
			UIImageView *avatarBackgroundImage = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"Icon-Small-40.png"]];
			
			[avatarBackgroundImage setTag:kOMECellUserBackgroundTag];
			[cell.contentView addSubview:avatarBackgroundImage];
			
			UILabel *nameLabel = [[UILabel alloc] init];
			[nameLabel setTag:kOMECellNameLabelTag];
			[cell.contentView addSubview:nameLabel];

			
		}
	} else { // Someone else's post so create gray bubble
		cell = [tableView dequeueReusableCellWithIdentifier:LeftCellIdentifier];
		if (cell == nil) {
			cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:LeftCellIdentifier];
			
			OMEDebug(@"this is left cell");
			
			UIImageView *backgroundImage = [[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"grayBubble.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(15.0f, 11.0f, 16.0f, 11.0f)]];
			[backgroundImage setTag:kOMECellBackgroundTag];
			[cell.contentView addSubview:backgroundImage];

			UILabel *textLabel = [[UILabel alloc] init];
			[textLabel setTag:kOMECellTextLabelTag];
			[cell.contentView addSubview:textLabel];
			
			UIImageView *avatarBackgroundImage = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"contactIcon.png"]];
			[avatarBackgroundImage setTag:kOMECellUserBackgroundTag];
			[cell.contentView addSubview:avatarBackgroundImage];

			
			UILabel *nameLabel = [[UILabel alloc] init];
			[nameLabel setTag:kOMECellNameLabelTag];
			[cell.contentView addSubview:nameLabel];
		}
		    /*
			cell = [tableView dequeueReusableCellWithIdentifier:TimeCellIdentifier];
			if (cell == nil) {
				cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:TimeCellIdentifier];
				
				NSLog(@"this is center cell");
				
				UIImageView *timeBackgroundImage = [[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"grayBubble.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(15.0f, 11.0f, 16.0f, 11.0f)]];
				[timeBackgroundImage setTag:kOMECellTimeBackgroundTag];
				[cell.contentView addSubview:timeBackgroundImage];
				
				UILabel *timetextLabel = [[UILabel alloc] init];
				[timetextLabel setTag:kOMECellTimeTextLabelTag];
				[cell.contentView addSubview:timetextLabel];
				
				UILabel *timenameLabel = [[UILabel alloc] init];
				[timenameLabel setTag:kOMECellTimeNameLabelTag];
				[cell.contentView addSubview:timenameLabel];
			}
		}
			 */
		
	}
	
	// Configure the cell content
	UILabel *textLabel = (UILabel*) [cell.contentView viewWithTag:kOMECellTextLabelTag];
	textLabel.text = [object objectForKey:kOMEParseTextKey];
	textLabel.lineBreakMode = UILineBreakModeWordWrap;
	textLabel.numberOfLines = 0;
	textLabel.font = [UIFont systemFontOfSize:kOMEChatRoomTableViewFontSize];
	textLabel.textColor = [UIColor blackColor];
	textLabel.backgroundColor = [UIColor clearColor];
	
	NSString *username = [NSString stringWithFormat:@"%@",[[object objectForKey:kOMEParseUserKey] objectForKey:kOMEParseUsernameKey]];
	UILabel *nameLabel = (UILabel*) [cell.contentView viewWithTag:kOMECellNameLabelTag];
	//Ozzie Zhang 2014-09-19 disable username
	nameLabel.text = username;
	nameLabel.font = [UIFont systemFontOfSize:kOMEChatRoomTableViewFontSize];
	nameLabel.backgroundColor = [UIColor clearColor];
	if (cellIsRight) {
		//nameLabel.textColor = [UIColor colorWithRed:175.0f/255.0f green:172.0f/255.0f blue:172.0f/255.0f alpha:1.0f];
		nameLabel.textColor = [UIColor blackColor];
		nameLabel.shadowColor = [UIColor colorWithRed:0.0f green:0.0f blue:0.0f alpha:0.35f];
		nameLabel.shadowOffset = CGSizeMake(0.0f, 0.5f);
	} else {
		nameLabel.textColor = [UIColor blackColor];
		nameLabel.shadowColor = [UIColor colorWithRed:0.9f green:0.9f blue:0.9f alpha:0.35f];
		nameLabel.shadowOffset = CGSizeMake(0.0f, 0.5f);
	}
	
	//Place the time content on the center position
	UIImageView *timeBackgroundImage = (UIImageView*) [cell.contentView viewWithTag:kOMECellTimeBackgroundTag];
	
	// Move cell content to the right position
	// Calculate the size of the post's text and username
	CGSize textSize = [[object objectForKey:kOMEParseTextKey] sizeWithFont:[UIFont systemFontOfSize:kOMEChatRoomTableViewFontSize] constrainedToSize:CGSizeMake(kOMEChatRoomTableViewCellWidth, FLT_MAX) lineBreakMode:UILineBreakModeWordWrap];
	CGSize nameSize = [username sizeWithFont:[UIFont systemFontOfSize:kOMEChatRoomTableViewFontSize] forWidth:kOMEChatRoomTableViewCellWidth lineBreakMode:UILineBreakModeTailTruncation];
	
	
	CGFloat cellHeight = [self tableView:tableView heightForRowAtIndexPath:indexPath]; // Get the height of the cell
	CGFloat textWidth = textSize.width > nameSize.width ? textSize.width : nameSize.width; // Set the width to the largest (text of username)
	
	//Place the time content in the corrent position
	
	//[nameLabel setFrame:CGRectMake(self.tableView.frame.size.width-textWidth-kOMECellTextPaddingSides-kOMECellPaddingSides,
	//							   kOMECellPaddingTop+kOMECellTextPaddingTop+textSize.height,
	//							   nameSize.width,
	//							   nameSize.height)];
//	[textLabel setFrame:CGRectMake(self.tableView.frame.size.width-textWidth-kOMECellTextPaddingSides-kOMECellPaddingSides,
//								   kOMECellPaddingTop+kOMECellTextPaddingTop,
//								   textSize.width,
//								   textSize.height)];
//	[timeBackgroundImage setFrame:CGRectMake(self.tableView.frame.size.width-textWidth-kOMECellTextPaddingSides*2-kOMECellPaddingSides,
//										 kOMECellPaddingTop,
//										 textWidth+kOMECellTextPaddingSides*2,
//										 cellHeight-kOMECellPaddingTop-kOMECellPaddingBottom)];
	
	UIImageView *backgroundImage = (UIImageView*) [cell.contentView viewWithTag:kOMECellBackgroundTag];
	
	UIImageView *avatarBackgroundImage = (UIImageView*) [cell.contentView viewWithTag:kOMECellUserBackgroundTag];
	
	// Move cell content to the right position
	// Calculate the size of the post's text and username
//	CGSize textSize = [[object objectForKey:kOMEParseTextKey] sizeWithFont:[UIFont systemFontOfSize:kOMEChatRoomTableViewFontSize] constrainedToSize:CGSizeMake(kOMEChatRoomTableViewCellWidth, FLT_MAX) lineBreakMode:UILineBreakModeWordWrap];
//	CGSize nameSize = [username sizeWithFont:[UIFont systemFontOfSize:kOMEChatRoomTableViewFontSize] forWidth:kOMEChatRoomTableViewCellWidth lineBreakMode:UILineBreakModeTailTruncation];
	
	
//	CGFloat cellHeight = [self tableView:tableView heightForRowAtIndexPath:indexPath]; // Get the height of the cell
//	CGFloat textWidth = textSize.width > nameSize.width ? textSize.width : nameSize.width; // Set the width to the largest (text of username)
	
	// Place the content in the correct position depending on the type
	if (cellIsRight) {
		//[nameLabel setFrame:CGRectMake(65,
		//							   kOMECellPaddingTop+kOMECellTextPaddingTop+textSize.height,
		//							   nameSize.width,
		//							   nameSize.height)];
		[nameLabel setFrame:CGRectMake(self.tableView.frame.size.width-kOMECellAvatarPaddingSides
									   -kOMECellAvatarWidth-kOMECellAvatarPaddingSides-nameSize.width,
									   kOMECellPaddingTop+kOMECellTextPaddingTop+nameSize.height,
									   nameSize.width,
									   kOMECellTextPaddingTop+nameSize.height)];
	/*
		[textLabel setFrame:CGRectMake(65,
									   kOMECellPaddingTop+kOMECellTextPaddingTop, 
									   textSize.width, 
									   textSize.height)];		
		//[backgroundImage setFrame:CGRectMake(self.tableView.frame.size.width-textWidth-kOMECellTextPaddingSides*2-kOMECellPaddingSides - 70,
		//									 kOMECellPaddingTop,
		//									 textWidth+kOMECellTextPaddingSides*2,
		//									 cellHeight-kOMECellPaddingTop-kOMECellPaddingBottom)];
		
		[backgroundImage setFrame:CGRectMake(60, kOMECellPaddingTop,
											 textWidth + kOMECellTextPaddingSides*2,
										     cellHeight - kOMECellPaddingTop - kOMECellPaddingBottom)];
		
		[avatarBackgroundImage setFrame:CGRectMake(260,
											 kOMECellPaddingTop,
											 40,
											 cellHeight - kOMECellPaddingTop - kOMECellPaddingBottom - kOMECellAvatarPaddingBottom)];
	*/
		
		[textLabel setFrame:CGRectMake(self.tableView.frame.size.width-kOMECellAvatarPaddingSides
									   -kOMECellAvatarWidth-kOMECellAvatarPaddingSides-textSize.width,
									   kOMECellPaddingTop+kOMECellTextPaddingTop,
									   // self.tableView.frame.size.width-kOMECellAvatarPaddingSides
									   //   -kOMECellAvatarWidth-kOMECellAvatarPaddingSides-kOMECellAvatarPaddingSides-kOMECellAvatarWidth-kOMECellAvatarPaddingSides,//
									   textSize.width,
									   kOMECellTextPaddingTop+textSize.height)];
		
		[backgroundImage setFrame:CGRectMake(self.tableView.frame.size.width-textSize.width
											 -kOMECellTextPaddingSides-kOMECellAvatarPaddingSides*2-kOMECellAvatarWidth,
											 kOMECellPaddingTop+kOMECellTextPaddingTop,
											 textSize.width+kOMECellTextPaddingSides*2,
											 kOMECellTextPaddingTop+textSize.height+nameSize.height)];
		
		[avatarBackgroundImage setFrame:CGRectMake(self.tableView.frame.size.width
												   -kOMECellAvatarPaddingSides-kOMECellAvatarWidth,kOMECellPaddingTop+kOMECellTextPaddingTop,
												   kOMECellAvatarWidth,
												   kOMECellAvatarHeight)];

		
	} else {
		[nameLabel setFrame:CGRectMake(kOMECellAvatarPaddingSides+kOMECellAvatarWidth+kOMECellAvatarPaddingSides+kOMECellTextPaddingSides*3,
									   kOMECellPaddingTop+kOMECellTextPaddingTop+textSize.height,
									   nameSize.width,
									   nameSize.height)];
		//[nameLabel setFrame:CGRectMake(70,
		//							   kOMECellPaddingTop+kOMECellTextPaddingTop+textSize.height,
		//							   nameSize.width,
		//							   nameSize.height)];

		[textLabel setFrame:CGRectMake(70,
									   kOMECellPaddingTop+kOMECellTextPaddingTop, 
									   textSize.width, 
									   textSize.height)];
		[backgroundImage setFrame:CGRectMake(60,
											 kOMECellPaddingTop, 
											 textWidth + kOMECellTextPaddingSides*2,
											 cellHeight - kOMECellPaddingTop - kOMECellPaddingBottom)];
		[avatarBackgroundImage setFrame:CGRectMake(kOMECellAvatarPaddingSides,
												   kOMECellPaddingTop,
												   40,
											 cellHeight - kOMECellPaddingTop - kOMECellPaddingBottom - kOMECellAvatarPaddingBottom)];
	}

	cell.selectionStyle = UITableViewCellSelectionStyleNone;
	return cell;
}

/*
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath object:(PFObject *)object {
    PAWPostTableViewCellStyle cellStyle = PAWPostTableViewCellStyleLeft;
    if ([object[kOMEParseUserKey][kOMEParseUsernameKey] isEqualToString:[[PFUser currentUser] username]]) {
        cellStyle = PAWPostTableViewCellStyleRight;
    }
	
    NSString *reuseIdentifier = nil;
    switch (cellStyle) {
        case PAWPostTableViewCellStyleLeft:
        {
            static NSString *leftCellIdentifier = @"left";
            reuseIdentifier = leftCellIdentifier;
        }
            break;
        case PAWPostTableViewCellStyleRight:
        {
            static NSString *rightCellIdentifier = @"right";
            reuseIdentifier = rightCellIdentifier;
        }
            break;
    }
	
    PAWPostTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:reuseIdentifier];
    if (cell == nil) {
        cell = [[PAWPostTableViewCell alloc] initWithInviteTableViewCellStyle:cellStyle
                                                            reuseIdentifier:reuseIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
	
    PAWPost *post = [[PAWPost alloc] initWithPFObject:object];
    [cell updateFromPost:post];
	
    return cell;
}
*/

- (UITableViewCell *)tableView:(UITableView *)tableView cellForNextPageAtIndexPath:(NSIndexPath *)indexPath {
	UITableViewCell *cell = [super tableView:tableView cellForNextPageAtIndexPath:indexPath];
	cell.textLabel.font = [cell.textLabel.font fontWithSize:kOMEChatRoomTableViewFontSize];
	return cell;
}


#pragma mark - UITableViewDelegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	// call super because we're a custom subclass.
	[super tableView:tableView didSelectRowAtIndexPath:indexPath];

	[tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
	// Account for the load more cell at the bottom of the tableview if we hit the pagination limit:
	if ( (NSUInteger)indexPath.row >= [self.objects count]) {
		return [tableView rowHeight];
	}

	// Retrieve the text and username for this row:
	PFObject *object = [self.objects objectAtIndex:indexPath.row];
	OMEInvite *postFromObject = [[OMEInvite alloc] initWithPFObject:object];
	NSString *text = postFromObject.title;
	NSString *username = postFromObject.user.username;
	
	// Calculate what the frame to fit the post text and the username
	CGSize textSize = [text sizeWithFont:[UIFont systemFontOfSize:kOMEChatRoomTableViewFontSize] constrainedToSize:CGSizeMake(kOMEChatRoomTableViewCellWidth, FLT_MAX) lineBreakMode:UILineBreakModeWordWrap];
	CGSize nameSize = [username sizeWithFont:[UIFont systemFontOfSize:kOMEChatRoomTableViewFontSize] forWidth:kOMEChatRoomTableViewCellWidth lineBreakMode:UILineBreakModeTailTruncation];

	// And return this height plus cell padding and the offset of the bubble image height (without taking into account the text height twice)
	CGFloat rowHeight = kOMECellPaddingTop + textSize.height + nameSize.height + kOMECellBackgroundffset;
	return rowHeight;
}


#pragma mark - PAWWallViewControllerSelection

- (void)highlightCellForInvite:(OMEInvite *)invite {
	// Find the cell matching this object.
	for (PFObject *object in [self objects]) {
		OMEInvite *inviteFromObject = [[OMEInvite alloc] initWithPFObject:object];
		if ([invite equalToInvite:inviteFromObject]) {
			// We found the object, scroll to the cell position where this object is.
			NSUInteger index = [[self objects] indexOfObject:object];

			NSIndexPath *indexPath = [NSIndexPath indexPathForRow:index inSection:kOMETableViewMainSection];
			[self.tableView scrollToRowAtIndexPath:indexPath atScrollPosition:UITableViewScrollPositionMiddle animated:YES];
			[self.tableView selectRowAtIndexPath:indexPath animated:NO scrollPosition:UITableViewScrollPositionNone];

			return;
		}
	}

	// Don't scroll for posts outside the search radius.
	if ([invite.title compare:kOMECannotViewInvite] != NSOrderedSame) {
		// We couldn't find the post, so scroll down to the load more cell.
		NSUInteger rows = [self.tableView numberOfRowsInSection:kOMETableViewMainSection];
		NSIndexPath *indexPath = [NSIndexPath indexPathForRow:(rows - 1) inSection:kOMETableViewMainSection];
		[self.tableView scrollToRowAtIndexPath:indexPath atScrollPosition:UITableViewScrollPositionBottom animated:YES];
		[self.tableView selectRowAtIndexPath:indexPath animated:NO scrollPosition:UITableViewScrollPositionNone];
		[self.tableView deselectRowAtIndexPath:indexPath animated:YES];
	}
}

- (void)unhighlightCellForinvite:(OMEInvite *)invite {
	// Deselect the invite's row.
	for (PFObject *object in [self objects]) {
		OMEInvite *inviteFromObject = [[OMEInvite alloc] initWithPFObject:object];
		if ([invite equalToInvite:inviteFromObject]) {
			NSUInteger index = [[self objects] indexOfObject:object];
			NSIndexPath *indexPath = [NSIndexPath indexPathForRow:index inSection:0];
			[self.tableView deselectRowAtIndexPath:indexPath animated:YES];

			return;
		}
	}
}


#pragma mark - ()

- (void)distanceFilterDidChange:(NSNotification *)note {
	[self loadObjects];
}

- (void)locationDidChange:(NSNotification *)note {
	[self loadObjects];
}

- (void)postWasCreated:(NSNotification *)note {
	[self loadObjects];
}

- (void)refreshControlValueChanged:(UIRefreshControl *)refreshControl {
    [self loadObjects];
}


-(BOOL)hidesBottomBarWhenPushed
{
	return YES;
}


@end
