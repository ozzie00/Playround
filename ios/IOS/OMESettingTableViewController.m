//
//  OMESettingTableViewController.m
//  ToPlay
//
//  Created by Ozzie Zhang on 9/17/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMESettingTableViewController.h"


#import "AppDelegate.h"
#import <Parse/Parse.h>

#import "OMELoginViewController.h"

#import "OMEActivityView.h"

#import "OMENewUserViewController.h"
#import "OMEPlayerViewController.h"
#import "OMEConstant.h"


typedef NS_ENUM(uint8_t, OMESettingTableViewSection)
{
	//OMESettingTableViewSectionMe = 0,
	OMESettingTableViewSectionFeedback = 0,
	OMESettingTableViewSectionFacebook,
	//OMESettingTableViewSectionLogin,
	OMESettingTableViewSectionLogout,
	OMESettingTableViewNumberOfSections
};

static uint16_t const OMESettingTableViewLogoutNumberOfRows = 1;

@interface OMESettingTableViewController ()


@end

@implementation OMESettingTableViewController


- (id) init {
	self = [super initWithStyle:UITableViewStyleGrouped];
	if (self != nil) {
		self.title = kOMEParseMe;
	}
}


- (IBAction)logoutPressed:(id)sender {
	
	if ([PFUser currentUser])  {
		
		
	   UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEParseSettingLogoutAlertInfo
													message:nil
													   delegate:self
											  cancelButtonTitle:kOMEParseLogout
											  otherButtonTitles:kOMEParseCancel, nil];
	   [alertView show];
	} else {
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEParseNotLogin
															message:nil
														   delegate:nil
												  cancelButtonTitle:nil
												  otherButtonTitles:kOMEParseOK, nil];
		[alertView show];
	
		
	}
	
	
}


#pragma mark UITableViewDataSource

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
	return OMESettingTableViewNumberOfSections;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	switch (section) {
		//case OMESettingTableViewSectionMe:
		//	return 1;
		//	break;
		case OMESettingTableViewSectionFeedback:
			return 1;
			break;
		case OMESettingTableViewSectionFacebook:
			return 1;
			break;
	//	case OMESettingTableViewSectionLogin:
	//		return 1;
	//		break;
		case OMESettingTableViewSectionLogout:
			return 1;
			break;
	};
}

/*


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	NSString * CellIdentifier = [NSString stringWithFormat:@"%d%d", [indexPath indexAtPosition:0], [indexPath indexAtPosition:1]];
	
	UITableViewCell *cell = [tableView dequeueReusableHeaderFooterViewWithIdentifier:CellIdentifier];
	
	if (cell == nil) {
		cell = [[ UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:CellIdentifier];
	}
	
	cell.selectionStyle = UITableViewCellSelectionStyleBlue;
	
	switch ([indexPath indexAtPosition: OMESettingTableViewSectionMe]) {
		case OMESettingTableViewSectionMe:
			switch ([indexPath indexAtPosition:1]) {
				case 0:
			        meControl = [[UILabel alloc] init];
				    [cell addSubview:meControl];
			        cell.textLabel.text = kOMEParseMe;
			        break;
			}
			break;
		case 1:
			switch ([indexPath indexAtPosition:1]) {
				case 0:
					feedbackControl = [[UILabel alloc] init];
					[cell addSubview:feedbackControl];
					cell.textLabel.text = kOMEParseFeedback;
					break;
			}
			break;
		case 2:
			switch ([indexPath indexAtPosition:1]) {
				case 0:
					logoutControl = [[UILabel alloc] init];
					[cell addSubview:logoutControl];
					cell.textLabel.text = kOMEParseLogout;
					break;
			}
	}
	return cell;
}

*/

#pragma mark UITableViewDelegate

// Called after the user changes the selection.
- (void)tableView:(UITableView *)aTableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	if (indexPath.section == OMESettingTableViewSectionLogout) {
		[aTableView deselectRowAtIndexPath:indexPath animated:YES];
		
		
		[PFUser logOut];
		//[self dismissViewControllerAnimated:YES completion:nil];
		
		[self.navigationController popViewControllerAnimated:YES];
		
		//UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEParseSettingLogoutAlertInfo
		//													message:nil
		//												   delegate:self
		//										  cancelButtonTitle:kOMEParseLogout
		//										  otherButtonTitles:kOMEParseCancel, nil];
		//[alertView show];
	}
}

/*
// Called when a button is clicked. The view will be automatically dismissed after this call returns
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    // Log out.
	
	NSLog(@"logout button of alertView is called");
	[PFUser logOut];
	
	NSLog(@"PFUser logOut is called");
	
	[self.presentingViewController dismissModalViewControllerAnimated:YES];
		
	//PAWAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
	//[appDelegate presentLoginViewController];
	
	[self.delegate settingTableViewControllerDidLogout:self];
	
	NSLog(@"after settingTableViewControllerDidLogout called");
	
	//[controller dismissViewControllerAnimated:YES completion:nil];
	//[self presentLoginViewController];
}

// Nil implementation to avoid the default UIAlertViewDelegate method, which says:
// "Called when we cancel a view (eg. the user clicks the Home button). This is not called when the user clicks the cancel button.
// If not defined in the delegate, we simulate a click in the cancel button"
// Since we have "Log out" at the cancel index (to get it out from the normal "Ok whatever get this dialog outta my face"
// position, we need to deal with the consequences of that.
- (void)alertViewCancel:(UIAlertView *)alertView {
	NSLog(@"Cancel button of alertView is called");
	return;
}
*/

#pragma mark -
#pragma mark UIAlertViewDelegate

// Called when a button is clicked. The view will be automatically dismissed after this call returns
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
	NSLog(@"logout button is called");
	
	[PFUser logOut];
	//[self dismissViewControllerAnimated:YES completion:nil];
	
	[self.navigationController popViewControllerAnimated:YES];

	
	if (buttonIndex == alertView.cancelButtonIndex) {
		// Log out.
		//[PFUser logOut];
		
		NSLog(@"before settingTableViewControllerDidLogout called");
		
		[self.navigationController popViewControllerAnimated:YES];
		
		//[self.delegate settingTableViewControllerDidLogout:self];
	}
}

// Nil implementation to avoid the default UIAlertViewDelegate method, which says:
// "Called when we cancel a view (eg. the user clicks the Home button). This is not called when the user clicks the cancel button.
// If not defined in the delegate, we simulate a click in the cancel button"
// Since we have "Log out" at the cancel index (to get it out from the normal "Ok whatever get this dialog outta my face"
// position, we need to deal with the consequences of that.
- (void)alertViewCancel:(UIAlertView *)alertView {
	
	NSLog(@"cancel button is called");
	return;
}

-(BOOL)hidesBottomBarWhenPushed
{
	return YES;
}

@end


