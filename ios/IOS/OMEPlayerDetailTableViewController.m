//
//  OMEPlayerDetailTableViewController.m
//  ToPlay
//
//  Created by Ozzie Zhang on 9/24/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

// this class name is not suitable for this class, because this class mainly display the dedicated invite content corresponding to the invite requirement of OMEInvitePlayerTableViewController

#import "OMEPlayerDetailTableViewController.h"
#import "OMEInviteRequirement.h"
#import "OMEPlayerViewController.h"
#import "OMEConstant.h"
#import "OMEDebug.h"
#import "OMETime.h"

@class OMEPlayerDetailTableViewController;
@class OMEPlayerViewController;
@class OMEInviteRequirement;


typedef enum {
	OMEPlayerDetailTableViewControllerSectionPlayerInfo = 0,
	OMEPlayerDetailTableViewControllerSectionInviteRequirement,
	OMEPlayerDetailTableViewControllerSectionJoinPlay,
	OMEPlayerDetailTableViewControllerNumberOfSections
} kOMEPlayerDetailTableViewControllerSections;

typedef enum {
	OMEPlayerDetailTableViewControllerSectionPlayerInfoName = 0,
	OMEPlayerDetailTableViewControllerSectionPlayerInfoLevel,
	OMEPlayerDetailTableViewControllerSectionPlayerInfoNumberOfRows
} kOMEPlayerDetailTableViewControllerSectionPlayerInfoRows;

typedef enum {
	OMEPlayerDetailTableViewControllerSectionInviteRequirementDetailSportType = 0,
	OMEPlayerDetailTableViewControllerSectionInviteRequirementDetailPlayerNumber,
	OMEPlayerDetailTableViewControllerSectionInviteRequirementDetailPlayerLevel,
	OMEPlayerDetailTableViewControllerSectionInviteRequirementDetailTime,
	OMEPlayerDetailTableViewControllerSectionInviteRequirementDetailCourt,
	OMEPlayerDetailTableViewControllerSectionInviteRequirementDetailFee,
	OMEPlayerDetailTableViewControllerSectionInviteRequirementDetailOther,
	OMEPlayerDetailTableViewControllerSectionInviteRequirementDetailNumberOfRows
} kOMEPlayerDetailTableViewControllerSectionInviteRequirementRows;


typedef enum {
	OMEPlayerDetailTableViewControllerSectionJoinPlayButton = 0,
	OMEPlayerDetailTableViewControllerSectionJoinPlayNumberOfRows
} kOMEPlayerDetailTableViewControllerSectionJoinPlayRows;

static uint16_t const OMEPlayerDetailTableViewControllerSectionSubmitInviteNumberOfRows = 1;

@interface OMEPlayerDetailTableViewController ()

@property UILabel *playerNameControl;
@property UILabel *playerLevelControl;

@property UILabel *inviteRequirementSportTypeControl;
@property UILabel *inviteRequirementPlayerNumberControl;
@property UILabel *inviteRequirementPlayerLevelControl;
@property UILabel *inviteRequirementTimeControl;
@property UILabel *inviteRequirementCourtControl;
@property UILabel *inviteRequirementFeeControl;
@property UILabel *inviteRequirementOtherControl;

@property UIButton *joinPlayControl;

@property (nonatomic, strong) OMEInviteRequirement *playerDetail;

@property (nonatomic) CLLocation *clickedMapPinPlayerLocation;

@end


@implementation OMEPlayerDetailTableViewController 


@synthesize delegate;
@synthesize playerDetail;
@synthesize clickedMapPinPlayerLocation;

@synthesize playerNameControl;
@synthesize playerLevelControl;
@synthesize inviteRequirementSportTypeControl;
@synthesize inviteRequirementPlayerNumberControl;
@synthesize inviteRequirementPlayerLevelControl;
@synthesize inviteRequirementTimeControl;
@synthesize inviteRequirementCourtControl;
@synthesize inviteRequirementFeeControl;
@synthesize inviteRequirementOtherControl;
@synthesize joinPlayControl;

- (void)viewDidLoad {
    [super viewDidLoad];
	
	//[UIButton buttonWithType:UIButtonTypeSystem];
	
	//self.delegate = self;
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark -
#pragma mark init OMEPlayerDetailTableViewController

- (id) init {
	
	self = [super initWithStyle:UITableViewStyleGrouped];
	
	/*
	
	if (self != nil) {
		self.title = @"";
	}
	*/
	return self;
	 
}


- (void) dealloc {
//	[dealloc playerDetailInviteRequirement];
}




#pragma mark - Table view data source


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
#warning Potentially incomplete method implementation.
    // Return the number of sections.
    return OMEPlayerDetailTableViewControllerNumberOfSections;
	//return 0;
}



- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
#warning Incomplete method implementation.
	switch (section) {
		case OMEPlayerDetailTableViewControllerSectionPlayerInfo:
			return OMEPlayerDetailTableViewControllerSectionPlayerInfoNumberOfRows;
			break;
		case OMEPlayerDetailTableViewControllerSectionInviteRequirement:
			return OMEPlayerDetailTableViewControllerSectionInviteRequirementDetailNumberOfRows;
			break;
        case OMEPlayerDetailTableViewControllerSectionJoinPlay:
	        return OMEPlayerDetailTableViewControllerSectionJoinPlayNumberOfRows;
		    break;
	};
	
    // Return the number of rows in the section.
    //return 0;
}


- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
	switch (section) {
		case OMEPlayerDetailTableViewControllerSectionPlayerInfo:
			return kOMEParseInvitePlayerInfoHeader;
			break;
		case OMEPlayerDetailTableViewControllerSectionInviteRequirement:
			return kOMEParseInviteRequirementHeader;
			break;
		case OMEPlayerDetailTableViewControllerSectionJoinPlay:
		    return kOMEParseInviteJoinPlayHeader;
			break;
	}
	
	//return nil;
	
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
	
	NSString *CellIdentifier = [ NSString stringWithFormat:@"%d:%d", [indexPath indexAtPosition:OMEPlayerDetailTableViewControllerSectionPlayerInfo], [indexPath indexAtPosition:OMEPlayerDetailTableViewControllerSectionInviteRequirement]];
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	
	if (cell == nil) {
		cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:CellIdentifier];
	}
	
	cell.selectionStyle = UITableViewCellSelectionStyleBlue;
	
	// Get clicked Map Pin corresponding detail invite requirement
	OMEPlayerViewController *playerViewController = [[OMEPlayerViewController alloc] init];
	self.delegate = playerViewController;
	
	clickedMapPinPlayerLocation = self.delegate.getClickedMapPinDetailsInfo;
	
	playerDetail = [[OMEInviteRequirement alloc] initInviteRequirementWithLocation:clickedMapPinPlayerLocation];

	//set cell content in each section
	switch ([indexPath indexAtPosition: OMEPlayerDetailTableViewControllerSectionPlayerInfo]) {
		case OMEPlayerDetailTableViewControllerSectionPlayerInfo:
			//especial note the "1" means
			switch ([indexPath indexAtPosition: 1]) {
				
				  case OMEPlayerDetailTableViewControllerSectionPlayerInfoName: {
					playerNameControl = [[UILabel alloc] init];
					[cell addSubview: playerNameControl];
				
					NSLog(@"detail user name !");
					// Get detail data
					if (playerDetail.fromUsername != nil) {
					    cell.textLabel.text = [NSString stringWithFormat:@"%@       %@",  kOMEParseInvitePlayerInfoPlayerName, playerDetail.fromUsername];
					} else {
						cell.textLabel.text = [NSString stringWithFormat:@"%@", kOMEParseInvitePlayerInfoPlayerName];
					}
					break;
				 
				}
				 
				case OMEPlayerDetailTableViewControllerSectionPlayerInfoLevel:
					playerLevelControl = [[UILabel alloc] init];
					[cell addSubview: playerLevelControl];
					
					// Get detail data
					if (playerDetail.userLevel != nil) {
					    cell.textLabel.text = [NSString stringWithFormat:@"%@       %@", kOMEParseInvitePlayerInfoPlayerLevel, playerDetail.userLevel];
					} else {
						cell.textLabel.text = [NSString stringWithFormat:@"%@", kOMEParseInvitePlayerInfoPlayerLevel];
					}
					break;
			}
			break;
		case OMEPlayerDetailTableViewControllerSectionInviteRequirement:
			//especially note the "1" means
			switch ([indexPath indexAtPosition: 1]) {
				case OMEPlayerDetailTableViewControllerSectionInviteRequirementDetailSportType:
					inviteRequirementSportTypeControl= [[UILabel alloc] init];
					[cell addSubview: inviteRequirementSportTypeControl];
					// Get detail data
					if (playerDetail.sportType != nil) {
						cell.textLabel.text = [NSString stringWithFormat:@"%@        %@", kOMEParseInviteRequirementSportType, playerDetail.sportType];
					} else {
						cell.textLabel.text = [NSString stringWithFormat:@"%@", kOMEParseInviteRequirementSportType];
					}
					break;
				case OMEPlayerDetailTableViewControllerSectionInviteRequirementDetailPlayerNumber:
					inviteRequirementPlayerNumberControl = [[UILabel alloc] init];
					[cell addSubview: inviteRequirementPlayerNumberControl];
					// Get detail data
					if (playerDetail.playerNumber != nil) {
						cell.textLabel.text = [NSString stringWithFormat:@"%@    %@", kOMEParseInviteRequirementPlayerNumber, playerDetail.playerNumber];
					} else {
						cell.textLabel.text = [NSString stringWithFormat:@"%@", kOMEParseInviteRequirementPlayerNumber];
					}
					break;
				case OMEPlayerDetailTableViewControllerSectionInviteRequirementDetailPlayerLevel:
					inviteRequirementPlayerLevelControl = [[UILabel alloc] init];
					[cell addSubview: inviteRequirementPlayerLevelControl];
					// Get detail data
					if (playerDetail.playerLevel != nil) {
						cell.textLabel.text = [NSString stringWithFormat:@"%@        %@", kOMEParseInviteRequirementPlayerLevel, playerDetail.playerLevel];
					} else {
						cell.textLabel.text = [NSString stringWithFormat:@"%@", kOMEParseInviteRequirementPlayerLevel];
					}
					break;
				case OMEPlayerDetailTableViewControllerSectionInviteRequirementDetailTime:
					inviteRequirementTimeControl = [[UILabel alloc] init];
					[cell addSubview: inviteRequirementTimeControl];
					// Get detail data
					if (playerDetail.time != nil) {
						cell.textLabel.text = [NSString stringWithFormat:@"%@         %@", kOMEParseInviteRequirementTime, playerDetail.time];
					} else {
						cell.textLabel.text = [NSString stringWithFormat:@"%@", kOMEParseInviteRequirementTime];
					}
					break;
				case OMEPlayerDetailTableViewControllerSectionInviteRequirementDetailCourt:
					inviteRequirementCourtControl = [[UILabel alloc] init];
					[cell addSubview: inviteRequirementCourtControl];
					// Get detail data
					if (playerDetail.court != nil) {
						cell.textLabel.text = [NSString stringWithFormat:@"%@        %@", kOMEParseInviteRequirementCourt, playerDetail.court];
					} else {
						cell.textLabel.text = [NSString stringWithFormat:@"%@", kOMEParseInviteRequirementCourt];
					}
					break;
				case OMEPlayerDetailTableViewControllerSectionInviteRequirementDetailFee:
					inviteRequirementFeeControl = [[UILabel alloc] init];
					[cell addSubview: inviteRequirementFeeControl];
					// Get detail data
					if (playerDetail.fee != nil) {
						cell.textLabel.text = [NSString stringWithFormat:@"%@           %@", kOMEParseInviteRequirementFee, playerDetail.fee];
					} else {
						cell.textLabel.text = [NSString stringWithFormat:@"%@", kOMEParseInviteRequirementFee];
					}
					break;
				case OMEPlayerDetailTableViewControllerSectionInviteRequirementDetailOther:
					inviteRequirementOtherControl = [[UILabel alloc] init];
					[cell addSubview: inviteRequirementOtherControl];
					// Get detail data
					if (playerDetail.other != nil) {
						cell.textLabel.text = [NSString stringWithFormat:@"%@        %@", kOMEParseInviteRequirementOther, playerDetail.other];
					} else {
						cell.textLabel.text = [NSString stringWithFormat:@"%@", kOMEParseInviteRequirementOther];
					}
					break;
			}
			break;
		case OMEPlayerDetailTableViewControllerSectionJoinPlay:
			//especially note the "1" means
			switch ([indexPath indexAtPosition:1]) {
				case OMEPlayerDetailTableViewControllerSectionJoinPlayButton:
					joinPlayControl = [UIButton buttonWithType: UIButtonTypeRoundedRect];
					[cell addSubview:joinPlayControl];
					[joinPlayControl setFrame:CGRectMake(8, 0, 304, 50)];
					[joinPlayControl setEnabled:YES];
					[joinPlayControl setBackgroundImage:[UIImage imageNamed:@"Button@2x.png"] forState:UIControlStateNormal];
					[joinPlayControl setTitle:kOMEParseInviteJoinPlayButton forState:UIControlStateNormal];
					[joinPlayControl setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
					[joinPlayControl addTarget:self action:@selector(joinplayButtonPressed:) forControlEvents:UIControlEventTouchUpInside];
					break;
			}
			break;
	 
	}
	
	return cell;
}



#pragma mark
#pragma mark - join play button pressed event
-(IBAction)joinplayButtonPressed:(id)sender {
	
	NSLog(@"joinplayButtonPressed is called!");
	
	//check the user is login state
	if ([PFUser currentUser]) {
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEParseInvitePlayerAlertInfo
														message:nil
													   delegate:self
											  cancelButtonTitle:kOMEParseYes
											  otherButtonTitles:kOMEParseNo, nil];
	
		[alertView show];
	} else {
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEParseInviteLoginAlert
															message:nil
														   delegate:self
												  cancelButtonTitle:nil
												  otherButtonTitles:kOMEParseOK, nil];
		
		[alertView show];
		

		
	}
	
	
}

#pragma mark -
#pragma mark UIAlertViewDelegate
// Called when a button is clicked. The view will be automatically dismissed after this call returns
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
	
	
	//user choose "YES" button
	//need deal with invitetoplay feature
	if (buttonIndex == alertView.cancelButtonIndex) {
		
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEParseInvitePlayerJoinPlaySuccessAlertInfo
															message:nil
														   delegate:self
												  cancelButtonTitle:nil
												  otherButtonTitles:kOMEParseOK, nil];
		
		
		//get fromUsername from  current clicked Map Pin details info
		// Get clicked Map Pin corresponding detail invite requirement
		OMEPlayerViewController *playerViewController = [[OMEPlayerViewController alloc] init];
		self.delegate = playerViewController;
		
		clickedMapPinPlayerLocation = self.delegate.getClickedMapPinDetailsInfo;
		
		playerDetail = [[OMEInviteRequirement alloc] initInviteRequirementWithLocation:clickedMapPinPlayerLocation];
		
		if ([PFUser currentUser]) {
			
			// Prepare Data for joinMessageObject
			
			PFUser *fromUser       = [PFUser currentUser];
			PFUser *user           = fromUser;
			NSString *fromUsername = fromUser.username;
			NSString *username     = fromUsername;
			
			
			NSString *toUsername   = playerDetail.fromUsername;
			PFUser *toUser         = playerDetail.fromUser;
			
			// check the toUser
			if (toUser == nil) {
				OMEDebug("toUser == nil");
				//toUser = fromUser;
			}
			
			if (toUsername == nil) {
				OMEDebug("toUsername == nil");
			}
			
			
			if ([toUsername isEqualToString:fromUsername]) {
				UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEParseMessageJoinPlayNotSelf
																	message:nil
																   delegate:nil
														  cancelButtonTitle:nil
														  otherButtonTitles:kOMEParseOK, nil];
				[alertView show];
				
				[self.navigationController popViewControllerAnimated:YES];
				
				return nil;
				
			}
			
			// check the toUser and fromUser
			OMEDebug("please add code deal with this case");
			
			NSString *sendTime = [OMETime getCurrentTime];
			
			//Ozzie Zhang 2014-10-10
			OMEDebug("need change this line, now it is only for test!");
			NSString *messageContent = [NSString stringWithFormat:@"%@%@! %@%@ %@%@.", kOMEParseMessageJoinPlayRequestFirstPart, toUsername, fromUsername, kOMEParseMessageJoinPlayRequestSecnodPart, playerDetail.sportType, kOMEParseMessageJoinPlayRequestThirdPart];
			
			//OMEDebug(@"messageContent = %@", messageContent);
			
			// Stitch together a inviteObject and send this async to Parse
			// When user want to join this play, and create join message, then
			// transfer this join message to author who invite this play
			PFObject *joinMessageObject = [PFObject objectWithClassName:kOMEParseMessageClassKey];
			
			//Ozzie Zhang 2014-10-02 add level for
			//[inviteObject setObject:level forKey:kOMEParseUserLevelKey];
			[joinMessageObject setObject:user forKey:kOMEParseUserKey];
			[joinMessageObject setObject:username forKey:kOMEParseUsernameKey];
			[joinMessageObject setObject:fromUsername forKey:kOMEParseMessageFromUsernameKey];
			[joinMessageObject setObject:fromUser forKey:kOMEParseMessageFromUserKey];
			[joinMessageObject setObject:toUsername forKey:kOMEParseMessageToUsernameKey];
			[joinMessageObject setObject:toUser forKey:kOMEParseMessageToUserKey];
			[joinMessageObject setObject:messageContent forKey:kOMEParseMessageContentKey];
			[joinMessageObject setObject:sendTime forKey:kOMEParseMessageSendTimeKey];
			
			// Use PFACL to restrict future modifications to this object.
			PFACL *toUserACL = [PFACL ACLWithUser:toUser];
			[toUserACL setPublicReadAccess:NO];
			[toUserACL setPublicWriteAccess:NO];
			[toUserACL setReadAccess:YES forUser:toUser];
			[joinMessageObject setACL:toUserACL];
			[joinMessageObject saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
				if (error) {
					NSLog(@"Can not save!");
					NSLog(@"%@", error);
					
					//Ozzie Zhang 2014-09-28 need modify cancelButtonTitle:nil
					UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:[[error userInfo] objectForKey:kOMEParseError]
																		message:nil
																	   delegate:nil
															  cancelButtonTitle:nil
															  otherButtonTitles:kOMEParseOK, nil];
					[alertView show];
					return;
				}
				if (succeeded) {
					NSLog(@"Successfully Submit Join play request saved!");
					dispatch_async(dispatch_get_main_queue(), ^{
						[[NSNotificationCenter defaultCenter] postNotificationName:kOMEInviteCreatedNotification object:nil];
					});
				} else {
					NSLog(@"Failed to save.");
				}
			}];
			
			[alertView show];
				//[NSThread sleepForTimeInterval:1.0];
		}
	} else {
		
	}
	
	//back to OMEMainViewController Scene
	[self.navigationController popViewControllerAnimated:YES];
	
	return nil;

}

// position, we need to deal with the consequences of that.
- (void)alertViewCancel:(UIAlertView *)alertView {
	
	NSLog(@"cancel button is called");
	//back to OMEMainViewController Scene
	[self.navigationController popViewControllerAnimated:YES];
	return nil;
}



/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    } else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

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


-(BOOL)hidesBottomBarWhenPushed
{
	return YES;
}


@end
