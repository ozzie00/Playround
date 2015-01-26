//
//  OMESearchViewController.m
//  ToPlay
//
//  Created by Ozzie Zhang on 9/1/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <Parse/Parse.h>

#import "AppDelegate.h"
#import "OMEConstant.h"
#import "OMESearchInviteViewController.h"



@interface OMESearchInviteViewController ()

- (NSString *)distanceLabelForCell:(NSIndexPath *)indexPath;
- (OMELocationAccuracy)distanceForCell:(NSIndexPath *)indexPath;

@property (nonatomic, assign) CLLocationAccuracy filterDistance;

@end

typedef enum {
	kOMESearchTableViewDistance = 0,
	//kOMESearchTableViewLevel,
	//kOMESearchTableViewCoach,
	//kOMESearchTableVIewCourt,
	kOMESearchTableViewNumberOfSections
} kOMESearchTableViewSections;

typedef enum {
	kOMESearchTableViewDistanceSection100MeterRow = 0,
	kOMESearchTableViewDistanceSection500MeterRow,
	kOMESearchTableViewDistanceSection1000MeterRow,
	kOMESearchTableViewDistanceSection5000MeterRow,
	kOMESearchTableViewDistanceSection10000MeterRow,
//	kOMESearchTableViewDistanceSection20000MeterRow,
//	kOMESearchTableViewDistanceSection50000MeterRow,
	kOMESearchTableViewDistanceNumberOfRows
} kOMESearchTableViewDistanceSectionRows;

static uint16_t const kOMESearchTableViewLogoutNumberOfRows = 1;

@implementation OMESearchInviteViewController

@synthesize tableView;
@synthesize filterDistance;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
		AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
		self.filterDistance = appDelegate.filterDistance;
    }
    return self;
}


#pragma mark - Custom setters

// Always fault our filter distance through to the app delegate. We just cache it locally because it's used in the tableview's cells.
- (void)setFilterDistance:(CLLocationAccuracy)aFilterDistance {
	AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
	appDelegate.filterDistance = aFilterDistance;
	filterDistance = aFilterDistance;
}

#pragma mark - View lifecycle

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark - Private helper methods

- (NSString *)distanceLabelForCell:(NSIndexPath *)indexPath {
	NSString *cellText = nil;
	switch (indexPath.row) {
		case kOMESearchTableViewDistanceSection100MeterRow:
			cellText = @"100 m";
			break;
		case kOMESearchTableViewDistanceSection500MeterRow:
			cellText = @"500 m";
			break;
		case kOMESearchTableViewDistanceSection1000MeterRow:
			cellText = @" 1 km";
			break;
		case kOMESearchTableViewDistanceSection5000MeterRow:
			cellText = @" 5 km";
			break;
		case kOMESearchTableViewDistanceSection10000MeterRow:
			cellText = @"10 km";
			break;
		//case kOMESearchTableViewDistanceSection20000MeterRow:
		//	cellText = @"20 km";
		//	break;
		//case kOMESearchTableViewDistanceSection50000MeterRow:
		//	cellText = @"50 km";
		//	break;
		case kOMESearchTableViewDistanceNumberOfRows: // never reached.
		default:
			cellText = @"Too far for you";
			break;
	}
	return cellText;
}

- (OMELocationAccuracy)distanceForCell:(NSIndexPath *)indexPath {
	OMELocationAccuracy distance = 0.0;
	switch (indexPath.row) {
		case kOMESearchTableViewDistanceSection100MeterRow:
			distance = 328;
			break;
		case kOMESearchTableViewDistanceSection500MeterRow:
			distance = 1640;
			break;
		case kOMESearchTableViewDistanceSection1000MeterRow:
			distance = 3280;//meter conver to feet
			break;
		case kOMESearchTableViewDistanceSection5000MeterRow:
			distance = 16404;
			break;
		case kOMESearchTableViewDistanceSection10000MeterRow:
			distance = 32808;
			break;
		//case kOMESearchTableViewDistanceSection20000MeterRow:
		//	distance = 65616;
		//	break;
		//case kOMESearchTableViewDistanceSection50000MeterRow:
		//	distance = 164040;
		//	break;
		case kOMESearchTableViewDistanceNumberOfRows: // never reached.
		default:
			distance = 10000 * kOMEFeetToMiles;
			break;
	}

	return distance;
}

#pragma mark - UINavigationBar-based actions

- (IBAction)done:(id)sender {
	[self.presentingViewController dismissModalViewControllerAnimated:YES];
}

#pragma mark - UITableViewDataSource methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
	return kOMESearchTableViewNumberOfSections;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	switch ((kOMESearchTableViewSections)section) {
		case kOMESearchTableViewDistance:
			return kOMESearchTableViewDistanceNumberOfRows;
			break;
/*		case kOMESearchTableViewLevel:
			return kOMESearchTableViewDistanceNumberOfRows;
			break;
		case kOMESearchTableViewCoach:
			return kOMESearchTableViewDistanceNumberOfRows;
			break;
		case kOMESearchTableVIewCourt:
			return kOMESearchTableViewDistanceNumberOfRows;
			break;
*/		case kOMESearchTableViewNumberOfSections:
			return 0;
			break;
	};
}

- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString *identifier = @"SearchTableView";
	if (indexPath.section == kOMESearchTableViewDistance) {
		UITableViewCell *cell = [aTableView dequeueReusableCellWithIdentifier:identifier];
		if ( cell == nil )
		{
			cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
		}

		// Configure the cell.
		cell.textLabel.text = [self distanceLabelForCell:indexPath];

		if (self.filterDistance == 0.0) {
			NSLog(@"We have a zero filter distance !");
		}

		OMELocationAccuracy filterDistanceInFeet = self.filterDistance * ( 1 / kOMEFeetToMeters);
		OMELocationAccuracy distanceForCell = [self distanceForCell:indexPath];
		if (abs(distanceForCell - filterDistanceInFeet) < 0.001 ) {
			cell.accessoryType = UITableViewCellAccessoryCheckmark;
		} else {
			cell.accessoryType = UITableViewCellAccessoryNone;
		}

		return cell;
	} else {
		return nil;
	}
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
	switch ((kOMESearchTableViewSections)section) {
		case kOMESearchTableViewDistance:
			return @"Distance";
			break;
/*		case kOMESearchTableViewLevel:
			return @"Level";
			break;
		case kOMESearchTableViewCoach:
			return @"Coach";
			break;
		case kOMESearchTableVIewCourt:
			return @"Court";
			break;
*/		case kOMESearchTableViewNumberOfSections:
			return @"";
			break;
	}
}

#pragma mark - UITableViewDelegate methods

// Called after the user changes the selection.
- (void)tableView:(UITableView *)aTableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	if (indexPath.section == kOMESearchTableViewDistance) {
		[aTableView deselectRowAtIndexPath:indexPath animated:YES];

		// if we were already selected, bail and save some work.
		UITableViewCell *selectedCell = [aTableView cellForRowAtIndexPath:indexPath];
		if (selectedCell.accessoryType == UITableViewCellAccessoryCheckmark) {
			return;
		}

		// uncheck all visible cells.
		for (UITableViewCell *cell in [aTableView visibleCells]) {
			if (cell.accessoryType != UITableViewCellAccessoryNone) {
				cell.accessoryType = UITableViewCellAccessoryNone;
			}
		}
		selectedCell.accessoryType = UITableViewCellAccessoryCheckmark;

		OMELocationAccuracy distanceForCellInFeet = [self distanceForCell:indexPath];
		self.filterDistance = distanceForCellInFeet * kOMEFeetToMeters;
	} //else if (indexPath.section == kOMESearchTableViewLogout) {
	//	[aTableView deselectRowAtIndexPath:indexPath animated:YES];
	//	UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"This will not delete any data, You can still log in with this account." message:nil delegate:self cancelButtonTitle:@"Log out" otherButtonTitles:@"Cancel", nil];
	//	[alertView show];
	//}
}

#pragma mark - UIAlertViewDelegate methods

// Called when a button is clicked. The view will be automatically dismissed after this call returns
/*
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
	if (buttonIndex == kOMESearchLogoutDialogLogout) {
		// Log out.
		[PFUser logOut];

		[self.presentingViewController dismissModalViewControllerAnimated:YES];

		PAWAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
		[appDelegate presentWelcomeViewController];
	} else if (buttonIndex == kOMESearchLogoutDialogCancel) {
		return;
	}
}
*/

// Nil implementation to avoid the default UIAlertViewDelegate method, which says:
// "Called when we cancel a view (eg. the user clicks the Home button). This is not called when the user clicks the cancel button.
// If not defined in the delegate, we simulate a click in the cancel button"
// Since we have "Log out" at the cancel index (to get it out from the normal "Ok whatever get this dialog outta my face"
// position, we need to deal with the consequences of that.
- (void)alertViewCancel:(UIAlertView *)alertView {
	return;
}

-(BOOL)hidesBottomBarWhenPushed
{
	return YES;
}


@end
