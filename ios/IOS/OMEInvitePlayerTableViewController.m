//
//  OMEInvitePlayerTableViewController.m
//  ToPlay
//
//  Created by Ozzie Zhang on 9/24/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMEInvitePlayerTableViewController.h"
#import "OMEInviteRequirement.h"
#import "AppDelegate.h"

#import "OMEConstant.h"
#import "OMETime.h"
#import "OMEDebug.h"

#import "OMEInviteCreateViewController.h"

typedef enum {
	OMEInvitePlayerTableViewControllerRequirement = 0,
	OMEInvitePlayerTableViewControllerInvitetoPlay,
	OMEInvitePlayerTableViewControllerNumberOfSections
} kOMEInvitePlayerTableViewControllerSections;

typedef enum {
	OMEInvitePlayerTableViewControllerSportType = 0,
	OMEInvitePlayerTableViewControllerPlayerNumber,
	OMEInvitePlayerTableViewControllerPlayerLevel,
	//OMEInvitePlayerTableViewControllerPlayerGender,
	OMEInvitePlayerTableViewControllerTime,
	OMEInvitePlayerTableViewControllerCourt,
	OMEInvitePlayerTableViewControllerFee,
	OMEInvitePlayerTableViewControllerOther,
	OMEInvitePlayerTableViewControllerRequirementNumberOfRows
} kOMEInvitePlayerTableViewControllerRequirementRows;

static uint16_t const OMEInvitePlayerTableViewControllerInvitetoPlayNumberOfRows = 1;

@interface OMEInvitePlayerTableViewController () <OMEPickSportTypeTableViewControllerDelegate>

- (NSString *)getCurrentTime;

@property (nonatomic, assign) NSString *feeText;

@property (nonatomic, assign) OMEInviteRequirement *inviteRequirement;

@end

@implementation OMEInvitePlayerTableViewController {
	NSString *_sporttype;
//	NSString *_feeText;
	OMEInvitePlayerTableViewController *invitePlayerTableViewController;
}


@synthesize delegate;
@synthesize tableView;
@synthesize sporttypeDetailLabel;
@synthesize playernumber;
@synthesize playerlevel;
@synthesize time;
@synthesize court;
@synthesize fee;
@synthesize other;
@synthesize invitetoplayButton;
@synthesize inviteRequirement;


- (void)viewDidLoad {
    [super viewDidLoad];
	
	//Ozzie Zhang 2014-09-28 disable natigation bar button
	//self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"InvitePlay" style:UIBarButtonItemStylePlain target:self action:@selector(invitetoplayPressed:)];
	
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textInputChanged:) name:UITextFieldTextDidChangeNotification object:court];
	
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textInputChanged:) name:UITextFieldTextDidChangeNotification object:other];
	
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textInputChanged:) name:UITextFieldTextDidChangeNotification object:time];
	
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
	//[court becomeFirstResponder];
	[super viewWillAppear:animated];
	
}

-  (void)dealloc {
	//[[NSNotificationCenter defaultCenter] removeObserver:self name:UITextFieldTextDidChangeNotification object:court];

}

- (void)textInputChanged:(NSNotification *)note {
	//doneButton.enabled = [self shouldEnableDoneButton];
}

#pragma mark - UITextFieldDelegate methods

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
	if (textField == court) {
		//[other becomeFirstResponder];
	}
	if (textField == other) {
	//	[other resignFirstResponder];
		[self processFieldEntries];
	}
	
	return YES;
}

#pragma mark Field validation

- (void)processFieldEntries {
	// Get the username text, store it in the app delegate for now
	NSString *courtAddress = court.text;
	NSString *otherInfo= other.text;
	NSString *nocourtAddressText = @"court address";
	NSString *nootherInfoText = @"other info";
	NSString *errorText = @"No ";
	NSString *errorTextJoin = @" or ";
	NSString *errorTextEnding = @" entered";
	BOOL textError = NO;
	
	// Messaging nil will return 0, so these checks implicitly check for nil text.
	if (courtAddress.length == 0 || otherInfo.length == 0) {
		textError = YES;
		
		// Set up the keyboard for the first field missing input:
		if (otherInfo.length == 0) {
		//	[other becomeFirstResponder];
		}
		if (courtAddress.length == 0) {
		//	[court becomeFirstResponder];
		}
	}
	
	if (courtAddress.length == 0) {
		textError = YES;
		errorText = [errorText stringByAppendingString:nocourtAddressText];
	}
	
	if (otherInfo.length == 0) {
		textError = YES;
		if (courtAddress.length == 0) {
			errorText = [errorText stringByAppendingString:errorTextJoin];
		}
		errorText = [errorText stringByAppendingString:nootherInfoText];
	}
	
	if (textError) {
		errorText = [errorText stringByAppendingString:errorTextEnding];
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:errorText message:nil delegate:self cancelButtonTitle:nil otherButtonTitles:kOMEParseInviteErrorInfo, nil];
		[alertView show];
		return;
	}
}


#pragma mark Keyboard

//Ozzie Zhang change self.View to self.tableView
- (void)dismissKeyboard {
	[self.tableView endEditing:YES];
}

- (void)registerForKeyboardNotifications {
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(keyboardWillShow:)
												 name:UIKeyboardWillShowNotification object:nil];
	
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(keyboardWillHide:)
												 name:UIKeyboardWillHideNotification object:nil];
}

- (void)keyboardWillShow:(NSNotification*)notification {
	NSDictionary *userInfo = [notification userInfo];
	CGRect endFrame = [userInfo[UIKeyboardFrameEndUserInfoKey] CGRectValue];
	
	CGRect keyboardFrame = [self.tableView convertRect:endFrame fromView:self.tableView.window];
	
	CGFloat scrollViewOffsetY = (CGRectGetHeight(keyboardFrame) -
								 (CGRectGetMaxY(self.view.bounds) -
								  CGRectGetMaxY(self.invitetoplayButton.frame) - 10.0f));
	
	if (scrollViewOffsetY < 0) {
		return;
	}
	
	CGFloat duration = [userInfo[UIKeyboardAnimationDurationUserInfoKey] doubleValue];
	UIViewAnimationCurve curve = [userInfo[UIKeyboardAnimationCurveUserInfoKey] integerValue];
	
	[UIView animateWithDuration:duration
						  delay:0.0
						options:curve << 16 | UIViewAnimationOptionBeginFromCurrentState
					 animations:^{
//						 [self.scrollView setContentOffset:CGPointMake(0.0f, scrollViewOffsetY) animated:NO];
					 }
					 completion:nil];
	
}

- (void)keyboardWillHide:(NSNotification*)notification {
	NSDictionary *userInfo = [notification userInfo];
	CGFloat duration = [userInfo[UIKeyboardAnimationDurationUserInfoKey] doubleValue];
	UIViewAnimationCurve curve = [userInfo[UIKeyboardAnimationCurveUserInfoKey] integerValue];
	
	[UIView animateWithDuration:duration
						  delay:0.0
						options:curve << 16 | UIViewAnimationOptionBeginFromCurrentState
					 animations:^{
//						 [self.scrollView setContentOffset:CGPointZero animated:NO];
					 }
					 completion:nil];
}


#pragma mark -
#pragma mark - back from OMEPickSportTypeTableViewController scene
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
	if ([segue.identifier isEqualToString:@"PickSportType"]) {
		OMEPickSportTypeTableViewController *pickSportTypeViewController = segue.destinationViewController;
		pickSportTypeViewController.delegate = self;
		pickSportTypeViewController.sporttype = _sporttype;
	}
	
	
}

#pragma mark - 
#pragma mark implement delegate method to choose sport type
-(void)pickSportTypeTableViewController:(OMEPickSportTypeTableViewController *)controller didSelectSportType:(NSString *)sporttype {
	_sporttype = sporttype;
	self.sporttypeDetailLabel.text = _sporttype;
	
	[self.navigationController popViewControllerAnimated:YES];
	
//	inviteRequirement.sportType = _sporttype;
	
}



#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
#warning Potentially incomplete method implementation.
    // Return the number of sections.
    return OMEInvitePlayerTableViewControllerNumberOfSections;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
#warning Incomplete method implementation.
	
	switch (section) {
		case OMEInvitePlayerTableViewControllerRequirement:
			return OMEInvitePlayerTableViewControllerRequirementNumberOfRows;
			break;
		case OMEInvitePlayerTableViewControllerInvitetoPlay:
			return OMEInvitePlayerTableViewControllerInvitetoPlayNumberOfRows;
			break;
		case OMEInvitePlayerTableViewControllerNumberOfSections:
			return 0;
			break;
	};

    // Return the number of rows in the section.
    return 0;
}




- (IBAction)playernumberValueChanged:(id)sender {
	
	self.playernumber.minimumValue = 1;
	self.playernumber.maximumValue = 15;
	
	[self.playernumber setMinimumValue:1.0];
	[self.playernumber setMaximumValue:15.0];
	
	int  sliderValue;
	
	sliderValue = lroundf(self.playernumber.value);

	
	NSLog(@" playernumber %f", self.playernumber.value);
	self.playernumberLabel.text = [NSString stringWithFormat:@"%d", sliderValue];
	[self.playernumber setValue:sliderValue animated:YES];
	
//	inviteRequirement.playerNumber = [NSString stringWithFormat:@"%d", sliderValue];
	
}

- (IBAction)playerlevelValuedChanged:(id)sender {
	int  sliderValue;
	
	sliderValue = lroundf(self.playerlevel.value);
	
	NSLog(@" playerlevel %f", self.playerlevel.value);
	self.playerlevelLabel.text = [NSString stringWithFormat:@"%d", sliderValue];
	[self.playerlevel setValue:sliderValue animated:YES];
	
//	inviteRequirement.playerLevel = [NSString stringWithFormat:@"%d", sliderValue];
								  
}

- (IBAction)feeValuedChanged:(id)sender {
	[self.fee titleForSegmentAtIndex:self.fee.selectedSegmentIndex];
	NSLog(@" fee %d", self.fee.selectedSegmentIndex);
	
	_feeText = kOMEParseInviteFeeDefault;
	
	if (self.fee.selectedSegmentIndex == 0) {
		
	//	inviteRequirement.fee = @"AA";
		_feeText = kOMEParseInviteFeeDefault;
		
	} else if (self.fee.selectedSegmentIndex == 1) {
//	    inviteRequirement.fee = @"Free";
		_feeText = kOMEParseInviteFeeFree;
	}
	
}


- (IBAction)invitetoplayPressed:(id)sender {
	
	if ([PFUser currentUser]) {
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEParseInviteAlertInfo
														message:nil
													   delegate:self
											  cancelButtonTitle:kOMEParseYes
												  otherButtonTitles:kOMEParseNo, nil];
		[alertView show];
	} else {
		//notify user to login or signup
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEParseInviteLoginAlert
															message:nil
														   delegate:self
												  cancelButtonTitle:nil
												  otherButtonTitles:kOMEParseOK, nil];
		[alertView show];
		
	}
	
	
	//after submit invite play, will popViewControllerAnimated to OMEMainViewController
	//[self.navigationController popViewControllerAnimated:YES];
}


// Called after the user changes the selection.
- (void)tableView:(UITableView *)aTableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	if (indexPath.section == OMEInvitePlayerTableViewControllerInvitetoPlay) {
		[aTableView deselectRowAtIndexPath:indexPath animated:YES];
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEParseInviteReallyPlay
															message:nil
														   delegate:self
												  cancelButtonTitle:kOMEParseLogout
												  otherButtonTitles:kOMEParseCancel, nil];
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
		
		NSLog(@"click Yes !");
		
		//set default value for time, sporttype, fee, court and other
		// default Order time is tomorrow
		time.text = [OMETime getTomorrowTime];
		
		if ([sporttypeDetailLabel.text isEqualToString:kOMEParseInviteSportTypeDetailDefault] ) {
		    sporttypeDetailLabel.text = [NSString stringWithFormat:kOMEParseInviteSportTypeDefault];
		}
	
		if (_feeText == nil) {
			_feeText = kOMEParseInviteFeeDefault;
		}
		
		if (court.text == nil) {
			NSLog(@"hi court.text !");
			court.text = kOMEParseInviteCourtDefault;
		}
		
		NSLog(@"check court.text %@", court.text);
		
		if (other.text == nil) {
			other.text = kOMEParseInviteOtherDefault;
		}
		
		NSLog(@"check other.text %@", other.text);
		
		//check if the user login or push alert
		if ([PFUser currentUser]) {

		    // Data prep:
		    AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
		    CLLocationCoordinate2D currentCoordinate = appDelegate.currentLocation.coordinate;
		    PFGeoPoint *currentPoint = [PFGeoPoint geoPointWithLatitude:currentCoordinate.latitude longitude:currentCoordinate.longitude];
		    PFUser *hostUser       = [PFUser currentUser];
			PFUser *fromUser       = hostUser;
			
			OMEDebug("hostUsername = %@", hostUser.username);
			
			NSString *hostUsername = hostUser.username;
			NSString *fromUsername = hostUser.username;
			
			NSString *currentTime  = [OMETime getCurrentTime];
			
		
		    // Stitch together a inviteObject and send this async to Parse
		    PFObject *inviteObject = [PFObject objectWithClassName:kOMEParseClassKey];
		    [inviteObject setObject:([NSString stringWithFormat:@"%@ %@ %@ %@", kOMEParseInviteTitlePlay,sporttypeDetailLabel.text, kOMEParseInviteTitleIn, time.text]) forKey:kOMEParseTextKey];
		    [inviteObject setObject:hostUser forKey:kOMEParseUserKey];
			[inviteObject setObject:hostUsername forKey:kOMEParseUsernameKey];
			[inviteObject setObject:fromUser forKey:kOMEParseInviteFromUserKey];
			[inviteObject setObject:fromUsername forKey:kOMEParseInviteFromUsernameKey];
			[inviteObject setObject:currentTime forKey:kOMEParseInviteSubmitTimeKey];
			
			
			//Ozzie Zhang 2014-10-02 add level for
		//	[inviteObject setObject:level forKey:kOMEParseUserLevelKey];
		    [inviteObject setObject:currentPoint forKey:kOMEParseLocationKey];
		
		
	  	    [inviteObject setObject:sporttypeDetailLabel.text forKey:kOMEParseInviteSportTypeKey];
	        [inviteObject setObject:_playernumberLabel.text forKey:kOMEParseInvitePlayerNumberKey];
		    [inviteObject setObject:_playerlevelLabel.text forKey:kOMEParseInvitePlayerLevelKey];
		    [inviteObject setObject:time.text forKey:kOMEParseInviteTimeKey];
			[inviteObject setObject:court.text forKey:kOMEParseInviteCourtKey];
		    [inviteObject setObject:_feeText forKey:kOMEParseInviteFeeKey];
            [inviteObject setObject:other.text forKey:kOMEParseInviteOtherInfoKey];
		
		    // Use PFACL to restrict future modifications to this object.
		    PFACL *readOnlyACL = [PFACL ACL];
		    [readOnlyACL setPublicReadAccess:YES];
		    [readOnlyACL setPublicWriteAccess:NO];
		    [inviteObject setACL:readOnlyACL];
		    [inviteObject saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
			    if (error) {
				    NSLog(@"Can not save!");
				    NSLog(@"%@", error);
					
					//Ozzie Zhang 2014-09-28 need modify cancelButtonTitle:nil
				    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:[[error userInfo] objectForKey:kOMEParseError] message:nil delegate:self cancelButtonTitle:nil otherButtonTitles:kOMEParseOK, nil];
				    [alertView show];
			    }
			    if (succeeded) {
				    NSLog(@"OMEInvitePlayerTableViewController Successfully saved!");
				    dispatch_async(dispatch_get_main_queue(), ^{
				    	[[NSNotificationCenter defaultCenter] postNotificationName:kOMEInviteCreatedNotification object:nil];
				    });
			    } else {
				    NSLog(@"Failed to save.");
			    }
		    }];
			
			// Set invite requirement
			OMEInviteRequirement *newInviteRequirement = [[OMEInviteRequirement alloc] init];
			[newInviteRequirement setPlayerDetailInviteRequirementOutsideDistance:NO];
			
			
			//Ozzie Zhang 2014-10-10 set delegate:nil replace delegate:self, let crash lost
			UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEParseInvitePlayerSubmitSuccessAlertInfo
																message:nil
															   delegate:nil
													  cancelButtonTitle:nil
													  otherButtonTitles:kOMEParseOK, nil];
			[alertView show];
			
		} else {
			//notify user to login or signup
		    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEParseInviteLoginAlert
																message:nil
															   delegate:nil
													  cancelButtonTitle:nil
													  otherButtonTitles:kOMEParseOK, nil];
			[alertView show];
		}
	} else {
		//user choose "No" button
		NSLog(@"click Cancel !");
		//[self.delegate invitePlayerTableViewController:self didCreateInviteRequirement:nil];
	}
	
	//after submit invite play, will popViewControllerAnimated to OMEMainViewController
	[self.navigationController popViewControllerAnimated:YES];
}



// position, we need to deal with the consequences of that.
- (void)alertViewCancel:(UIAlertView *)alertView {
	
	NSLog(@"cancel button is called");
	//after submit invite play, will popViewControllerAnimated to OMEMainViewController
	[self.navigationController popViewControllerAnimated:YES];
}

-(BOOL)hidesBottomBarWhenPushed
{
	return YES;
}


@end
