//
//  OMENewUserViewController.h
//  ToPlay
//
//  Created by Ozzie Zhang on 9/22/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMENewUserViewController.h"

#import <Parse/Parse.h>
#import "OMEActivityView.h"

#import "OMEWelcomeViewController.h"
#import "OMELoginViewController.h"
#import "OMEMainViewController.h"
#import "OMEConstant.h"

@interface OMENewUserViewController ()

- (void)processFieldEntries;
- (void)textInputChanged:(NSNotification *)note;
- (BOOL)shouldEnableDoneButton;

@end

@implementation OMENewUserViewController

@synthesize usernameField;
@synthesize passwordField;
@synthesize passwordAgainField;
@synthesize doneButton;



#pragma mark - UIViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textInputChanged:) name:UITextFieldTextDidChangeNotification object:usernameField];
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textInputChanged:) name:UITextFieldTextDidChangeNotification object:passwordField];
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textInputChanged:) name:UITextFieldTextDidChangeNotification object:passwordAgainField];

	doneButton.enabled = NO;
}

- (void)viewWillAppear:(BOOL)animated {
	[usernameField becomeFirstResponder];
	[super viewWillAppear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void)dealloc {
	[[NSNotificationCenter defaultCenter] removeObserver:self name:UITextFieldTextDidChangeNotification object:usernameField];
	[[NSNotificationCenter defaultCenter] removeObserver:self name:UITextFieldTextDidChangeNotification object:passwordField];
	[[NSNotificationCenter defaultCenter] removeObserver:self name:UITextFieldTextDidChangeNotification object:passwordAgainField];
}


#pragma mark - UITextFieldDelegate

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
	if (textField == usernameField) {
		[passwordField becomeFirstResponder];
	}
	if (textField == passwordField) {
		[passwordAgainField becomeFirstResponder];
	}
	if (textField == passwordAgainField) {
		[passwordAgainField resignFirstResponder];
		[self processFieldEntries];
	}

	return YES;
}

#pragma mark - ()

- (BOOL)shouldEnableDoneButton {
	BOOL enableDoneButton = NO;
	if (usernameField.text != nil &&
		usernameField.text.length > 0 &&
		passwordField.text != nil &&
		passwordField.text.length > 0 &&
		passwordAgainField.text != nil &&
		passwordAgainField.text.length > 0) {
		enableDoneButton = YES;
	}
	return enableDoneButton;
}

- (void)textInputChanged:(NSNotification *)note {
	doneButton.enabled = [self shouldEnableDoneButton];
}

- (IBAction)cancel:(id)sender {
	[self.presentingViewController dismissModalViewControllerAnimated:YES];
}

- (IBAction)done:(id)sender {
	[usernameField resignFirstResponder];
	[passwordField resignFirstResponder];
	[passwordAgainField resignFirstResponder];
	[self processFieldEntries];
}

 
//- (IBAction)cancel:(id)sender {
//}

- (IBAction)signup:(id)sender {
	[usernameField resignFirstResponder];
	[passwordField resignFirstResponder];
	[passwordAgainField resignFirstResponder];
	[self processFieldEntries];
}

- (void)processFieldEntries {
	// Check that we have a non-zero username and passwords.
	// Compare password and passwordAgain for equality
	// Throw up a dialog that tells them what they did wrong if they did it wrong.

	NSString *username = usernameField.text;
	NSString *password = passwordField.text;
	NSString *passwordAgain = passwordAgainField.text;
	
	NSString *errorText = @"Please ";
	NSString *usernameBlankText = @"enter username";
	NSString *passwordBlankText = @"enter password";
	NSString *joinText = @", and ";
	NSString *passwordMismatchText = @"enter the same password again";

	BOOL textError = NO;

	// Messaging nil will return 0, so these checks implicitly check for nil text.
	if (username.length == 0 || password.length == 0 || passwordAgain.length == 0) {
		textError = YES;

		// Set up the keyboard for the first field missing input:
		if (passwordAgain.length == 0) {
			[passwordAgainField becomeFirstResponder];
		}
		if (password.length == 0) {
			[passwordField becomeFirstResponder];
		}
		if (username.length == 0) {
			[usernameField becomeFirstResponder];
		}

		if (username.length == 0) {
			errorText = [errorText stringByAppendingString:usernameBlankText];
		}

		if (password.length == 0 || passwordAgain.length == 0) {
			if (username.length == 0) { // We need some joining text in the error:
				errorText = [errorText stringByAppendingString:joinText];
			}
			errorText = [errorText stringByAppendingString:passwordBlankText];
		}
	} else if ([password compare:passwordAgain] != NSOrderedSame) {
		// We have non-zero strings.
		// Check for equal password strings.
		textError = YES;
		errorText = [errorText stringByAppendingString:passwordMismatchText];
		[passwordField becomeFirstResponder];
	}

	if (textError) {
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:errorText message:nil delegate:self cancelButtonTitle:nil otherButtonTitles:kOMEParseOK, nil];
		[alertView show];
		return;
	}

	// Everything looks good; try to log in.
	// Disable the done button for now.
	//doneButton.enabled = NO;
	
	
	
	OMEActivityView *activityView = [[OMEActivityView alloc] initWithFrame:CGRectMake(0.f, 0.f, self.view.frame.size.width, self.view.frame.size.height)];
	UILabel *label = activityView.label;
	label.text = kOMEParseSignUp;
	label.font = [UIFont boldSystemFontOfSize:20.f];
	[activityView.activityIndicator startAnimating];
	[activityView layoutSubviews];

	[self.view addSubview:activityView];

	// Call into an object somewhere that has code for setting up a user.
	// The app delegate cares about this, but so do a lot of other objects.
	// For now, do this inline.

	PFUser *user = [PFUser user];
	user.username = username;
	user.password = password;
	[user signUpInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
		if (error) {
			UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:[[error userInfo] objectForKey:kOMEParseError] message:nil delegate:self cancelButtonTitle:nil otherButtonTitles:kOMEParseOK, nil];
			[alertView show];
			doneButton.enabled = [self shouldEnableDoneButton];
			[activityView.activityIndicator stopAnimating];
			[activityView removeFromSuperview];
			// Bring the keyboard back up, because they'll probably need to change something.
			[self.usernameField becomeFirstResponder];
			return;
		}

		// Success!
		[activityView.activityIndicator stopAnimating];
		[activityView removeFromSuperview];
		
		
		// [self.navigationController popToRootViewControllerAnimated:YES];

		//Ozzie Zhang 2014-09-18 disable for storyboard solution
	//	OMEMainViewController *mainViewController = [[OMEMainViewController alloc] initWithNibName:nil bundle:nil];
	//	[(UINavigationController *)self.presentingViewController pushViewController:mainViewController animated:NO];
	//	[self.presentingViewController dismissModalViewControllerAnimated:YES];
	
		
		OMELoginViewController *loginViewController = [[OMELoginViewController alloc] initWithNibName:nil bundle:nil];
		[self.navigationController presentViewController:loginViewController animated:YES completion:^{}];
		
		
		//Ozzie Zhang 2014-09-18 add
		//OMEPlayerViewController *playerViewController = [[OMEPlayerViewController alloc] initWithNibName:nil bundle:nil]; //ozzie zhang 2014-09-03 change from initWithNibName:nil

		//[(UINavigationController *)self.presentingViewController pushViewController:playerViewController animated:NO];
		//[self.presentingViewController dismissModalViewControllerAnimated:YES];
	}];
}

@end
