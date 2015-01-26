//
//  OMEMeTableViewController.m
//  ToPlay
//
//  Created by Ozzie Zhang on 9/17/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMEMeTableViewController.h"
#import "OMEMemeCell.h"
#import "OMEPlayer.h"
#import "OMEConstant.h"
#import "OMESettingTableViewController.h"

typedef NS_ENUM(uint8_t, OMEMeTableViewSection)
{
	OMEMeTableViewSectionProfile = 0,
	OMEMeTableViewSectionSetting,
	OMEMeTableViewSectionLogin,
	OMEMeTableViewNumberOfSections
};

typedef NS_ENUM(uint8_t, OMEMeTableViewSectionProfileSection)
{
	OMEMeTableViewSectionProfileInfo = 0,
	OMEMeTableViewSectionProfileNumberOfRow
};



typedef NS_ENUM(uint8_t, OMEMeTableViewSectionSettingSection)
{
	OMEMeTableViewSectionSettingInfo = 0,
	OMEMeTableViewSectionSettingNumberOfRow
};

typedef NS_ENUM(uint8_t, OMEMeTableViewSectionLoginSection)
{
	OMEMeTableViewSectionLoginInfo = 0,
	OMEMeTableViewSectionLoginNumberOfRow
};




@interface OMEMeTableViewController ()

@property UILabel *profileControl;
@property UILabel *settingControl;

@end

@implementation OMEMeTableViewController

@synthesize profileControl;
@synthesize settingControl;

/*
- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

 */


- (id) init {
	
	self = [super initWithStyle:UITableViewStyleGrouped];
	
	if (self != nil) {
		self.title = @"profile";
	}
	
	return self;
}


 
- (void)viewDidLoad
{
	[super viewDidLoad];
	
	if ([PFUser currentUser]) {
	
		self.navigationItem.title = [[PFUser currentUser] username];
		
	} else {
		self.navigationItem.title = @"me";
	}
	
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
#warning Potentially incomplete method implementation.
    // Return the number of sections.
    return OMEMeTableViewNumberOfSections;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
#warning Incomplete method implementation.
    // Return the number of rows in the section.
	switch (section) {
		case OMEMeTableViewSectionProfile:
			return 1;
			break;
		case OMEMeTableViewSectionSetting:
			return  1;
			break;
		case OMEMeTableViewSectionLogin:
			return 1;
			break;
	};
	return 0;
}

/*

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	
	OMEMemeCell *cell = (OMEMemeCell *)[tableView
									  dequeueReusableCellWithIdentifier:@"meCell"];
	//OMEPlayer *player = [self.players objectAtIndex:indexPath.row];
	cell.nameLabel.text = @"Tom";//player.name;
	cell.usernameLabel.text = @"Hank"; //player.username;
	cell.avatarImageView.image = [UIImage imageNamed:@"Icon-Small-40.png"];
	
	
	NSLog(@"me cellForRowAtIndexPath");
	
	
	
	NSString *CellIdentifier = [ NSString stringWithFormat:@"%d:%d", [indexPath indexAtPosition: 0], [indexPath indexAtPosition: 1]];
	
//	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	
	if (cell == nil) {
		cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:CellIdentifier];
	}
	
	cell.selectionStyle = UITableViewCellSelectionStyleBlue;
	
	//set cell content in each section
	switch ([indexPath indexAtPosition: OMEMeTableViewSectionProfile]) {
		case OMEMeTableViewSectionProfile  :
			//especial note the "1" means
			switch ([indexPath indexAtPosition: 1]) {
					
				  case OMEMeTableViewSectionProfileInfo: {
					 profileControl = [[UILabel alloc] init];
					 [cell addSubview: profileControl];
					 cell.textLabel.text = @"profile";
					  
					  NSLog(@"me profile !");
	 
					 break;
				  }
			}
			break;
		case OMEMeTableViewSectionSetting :
			switch ([indexPath indexAtPosition: 1]) {
				case OMEMeTableViewSectionSettingInfo: {
					settingControl = [[UILabel alloc] init];
					[cell addSubview:settingControl];
					cell.textLabel.text = @"setting";
					
					NSLog(@"setting control");
					break;
				}
			}
			break;
			
		 }
	 
	
	
	return cell;

}
 */


/*
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	//Get the cell with: UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
	if(indexPath.section == OMEMeTableViewSectionProfile) {
		//perform action for second dataset
	} else if (indexPath.section == OMEMeTableViewSectionSetting) {
		//perform action for first dataset
		
		OMESettingTableViewController *settingTableViewController = [[OMESettingTableViewController alloc] initWithNibName:nil bundle:nil];
		//loginViewController.delegate = self;
		//[self.navigationController setViewControllers:@[ loginViewController ] animated:YES];
		[self.navigationController pushViewController:settingTableViewController animated:YES];
		
		
	} else if (indexPath.section == OMEMeTableViewSectionLogin) {
		// check user if login, then logout, else login
		if ([PFUser currentUser]) {
			
			UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Logout"
																message:nil
															   delegate:self
													  cancelButtonTitle:nil
													  otherButtonTitles:kOMEParseOK, nil];
			[alertView show];
			[PFUser logOut];
			[[PFUser currentUser] refresh];
		} else {
			
			
		//	self.pawLoginViewController = [[PAWLoginViewController alloc] init];
			//	[self.navigationController setViewControllers:@[ loginViewController ] animated:YES];
			//[self.navigationController presentModalViewController:self.loginViewController animated:NO];
		//	[self presentViewController:pawLoginViewController animated:YES completion:nil];
		}
		
	}
}
*/




/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
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
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
