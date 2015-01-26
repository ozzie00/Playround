//
//  OMELoginViewController.h
//  ToPlay
//
//  Created by Ozzie Zhang on 9/22/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMELoginViewController.h"

#import "AppDelegate.h"
#import <Parse/Parse.h>
#import <FacebookSDK/FacebookSDK.h>


#import "OMEActivityView.h"

#import "OMEPlayerViewController.h"
#import "OMENewUserViewController.h"

#import "OMEPlayerViewController.h"




//Ozzie Zhang 2014-09-23 use new view
/*
@interface PAWLoginViewController ()

- (void)processFieldEntries;
- (void)textInputChanged:(NSNotification *)note;
- (BOOL)shouldEnableDoneButton;

@end
*/

@interface OMELoginViewController ()
<UITextFieldDelegate>

@property (nonatomic, assign) BOOL activityViewVisible;
@property (nonatomic, strong) UIView *activityView;

@property (nonatomic, strong) IBOutlet UIScrollView *scrollView;
@property (strong, nonatomic) IBOutlet UIView *backgroundView;
@property (strong, nonatomic) IBOutlet UIButton *loginButton;

@end

@implementation OMELoginViewController

static NSUInteger const OMEParseUsernameDefaultLength = 6;
static NSUInteger const OMEParsePasswordDefaultLength = 8;


//@synthesize doneButton;
@synthesize usernameField;
@synthesize passwordField;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}


#pragma mark - View lifecycle

- (void)viewDidLoad {
    [super viewDidLoad];

	if (/* DISABLES CODE */ (0)) {
	FBLoginView *loginView = [[FBLoginView alloc] initWithReadPermissions:
							  @[kOMEParseFacebookPublicProfile, kOMEParseFacebookEmail, kOMEParseFacebookUserFriends]];
	loginView.frame = CGRectOffset(loginView.frame, (self.view.center.x - (loginView.frame.size.width / 2)), (self.view.center.y + 30)); //
	[self.view addSubview:loginView];
	
	}
	
    // Do any additional setup after loading the view from its nib.
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textInputChanged:) name:UITextFieldTextDidChangeNotification object:usernameField];
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textInputChanged:) name:UITextFieldTextDidChangeNotification object:passwordField];

	//doneButton.enabled = NO;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void)viewWillAppear:(BOOL)animated {
	[usernameField becomeFirstResponder];
	[super viewWillAppear:animated];
	
	//Ozzie Zhang 2014-09-23 add 
	[self.navigationController setNavigationBarHidden:YES animated:animated];
}

-  (void)dealloc {
	[[NSNotificationCenter defaultCenter] removeObserver:self name:UITextFieldTextDidChangeNotification object:usernameField];
	[[NSNotificationCenter defaultCenter] removeObserver:self name:UITextFieldTextDidChangeNotification object:passwordField];
}


#pragma mark - Fackbook

- (BOOL)application:(UIApplication *)application
			openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
		 annotation:(id)annotation {
	
	// Call FBAppCall's handleOpenURL:sourceApplication to handle Facebook app responses
	BOOL wasHandled = [FBAppCall handleOpenURL:url sourceApplication:sourceApplication];
	
	// You can add your app-specific url handling code here if needed
	
	return wasHandled;
}

#pragma mark - IBActions

- (IBAction)login:(id)sender {
	//[usernameField resignFirstResponder];
	//[passwordField resignFirstResponder];
	
	[self dismissKeyboard];
	[self processFieldEntries];
}

- (IBAction)cancel:(id)sender {
	[self.presentingViewController dismissModalViewControllerAnimated:NO];
}

- (IBAction)createButtonSelected:(id)sender {
	OMENewUserViewController *newUserViewController = [[OMENewUserViewController alloc] initWithNibName:nil bundle:nil];
	[self.navigationController presentViewController:newUserViewController animated:YES completion:^{}];
}


//- (void)viewWillAppear:(BOOL)animated {
//	[super viewWillAppear:animated];
//
//	[self.navigationController setNavigationBarHidden:YES animated:animated];
//}

- (void)viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];
	
	[self.scrollView flashScrollIndicators];
}
- (void)viewDidLayoutSubviews {
	[super viewDidLayoutSubviews];
	
	self.activityView.frame = self.view.bounds;
	self.scrollView.contentSize = self.backgroundView.bounds.size;
}


#pragma mark - UITextField text field change notifications and helper methods

- (BOOL)shouldEnableDoneButton {
	BOOL enableDoneButton = NO;
	if (usernameField.text != nil &&
		usernameField.text.length > 0 &&
		passwordField.text != nil &&
		passwordField.text.length > 0) {
		enableDoneButton = YES;
	}
	return enableDoneButton;
}

- (void)textInputChanged:(NSNotification *)note {
	//doneButton.enabled = [self shouldEnableDoneButton];
}

#pragma mark - UITextFieldDelegate methods

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
	if (textField == usernameField) {
		[passwordField becomeFirstResponder];
	}
	if (textField == passwordField) {
		[passwordField resignFirstResponder];
		[self processFieldEntries];
	}

	return YES;
}


#pragma mark Delegate

- (void)newUserViewControllerDidSignup:(OMENewUserViewController *)controller {
	NSLog(@"login in using newUserViewControllerDidSignup");
	
	[self.delegate loginViewControllerDidLogin:self];
}

#pragma mark - Private methods:

#pragma mark Field validation

- (void)processFieldEntries {
	// Get the username text, store it in the app delegate for now
	NSString *username = usernameField.text;
	NSString *password = passwordField.text;
	NSString *noUsernameText = @"username";
	NSString *noPasswordText = @"password";
	NSString *errorText = @"No ";
	NSString *errorTextJoin = @" or ";
	NSString *errorTextEnding = @" entered";
	BOOL textError = NO;

	// Messaging nil will return 0, so these checks implicitly check for nil text.
	if (username.length <  1 || password.length < 1) {
		textError = YES;

		// Set up the keyboard for the first field missing input:
		if (password.length < 1) {
			[passwordField becomeFirstResponder];
		}
		if (username.length < 1) {
			[usernameField becomeFirstResponder];
		}
	}

	if (username.length < 1) {
		textError = YES;
		errorText = kOMEParseLoginUsernameError;
	}

	if (password.length < 1) {
		textError = YES;
		if (username.length < 1) {
			errorText = kOMEParseLoginUsernameError;
		}
		errorText = kOMEParseLoginPasswordError;
	}

	if (textError) {
		errorText = [errorText stringByAppendingString:errorTextEnding];
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:errorText
															message:nil
														   delegate:self
												  cancelButtonTitle:nil
												  otherButtonTitles:kOMEParseOK, nil];
		[alertView show];
		return;
	}
	
	// Everything looks good; try to log in.
	
	// Set up activity view
	self.activityViewVisible = YES;
	
	[PFUser logInWithUsernameInBackground:username password:password block:^(PFUser *user, NSError *error) {
		// Tear down the activity view in all cases.
		self.activityViewVisible = NO;
		
		if (user) {
			[self.delegate loginViewControllerDidLogin:self];
		} else {
			// Didn't get a user.
			NSLog(@"%s no such username !", __PRETTY_FUNCTION__);
			
			// Call into an object somewhere that has code for setting up a user.
			// The app delegate cares about this, but so do a lot of other objects.
			// For now, do this inline.
			
			PFUser *newuser = [PFUser user];
			newuser.username = username;
			newuser.password = password;
			
			[newuser signUpInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
				if (error) {
					UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEParseLoginNewUserError
																		message:nil
																	   delegate:self
															  cancelButtonTitle:nil
															  otherButtonTitles:kOMEParseOK, nil];
					[alertView show];
					return;
				} else {
					UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEParseLoginNewUserCreateInfo
																		message:nil
																	   delegate:self
															  cancelButtonTitle:nil
															  otherButtonTitles:kOMEParseOK, nil];
					[alertView show];
					
					return;
				}

			}];
			
			// Bring the keyboard back up, because they'll probably need to change something.
			[self.usernameField becomeFirstResponder];
		}
	}];


	/*
	
	// Everything looks good; try to log in.
	// Disable the done button for now.
	doneButton.enabled = NO;

	PAWActivityView *activityView = [[PAWActivityView alloc] initWithFrame:CGRectMake(0.f, 0.f, self.view.frame.size.width, self.view.frame.size.height)];
	UILabel *label = activityView.label;
	label.text = @"Logging in";
	label.font = [UIFont boldSystemFontOfSize:20.f];
	[activityView.activityIndicator startAnimating];
	[activityView layoutSubviews];

	[self.view addSubview:activityView];

	[PFUser logInWithUsernameInBackground:username password:password block:^(PFUser *user, NSError *error) {
		// Tear down the activity view in all cases.
		[activityView.activityIndicator stopAnimating];
		[activityView removeFromSuperview];

		if (user) {
			//Ozzie Zhang 2014-09-18 disable this for storyboard
			PAWWallViewController *wallViewController = [[PAWWallViewController alloc] initWithNibName:nil bundle:nil];
			[(UINavigationController *)self.presentingViewController pushViewController:wallViewController animated:NO];
			[self.presentingViewController dismissModalViewControllerAnimated:YES];
			
			//Ozzie Zhang 2014-09-18 add
			//OMEPlayerViewController *playerViewController = [[OMEPlayerViewController alloc] initWithNibName:nil bundle:nil]; //ozzie zhang 2014-09-03 change from initWithNibName:nil
			
			//[(UINavigationController *)self.presentingViewController pushViewController:playerViewController animated:NO];
			//[self.presentingViewController dismissModalViewControllerAnimated:YES];
			
			
		//	[self.presentingViewController dismissModalViewControllerAnimated:YES];
			
		//	PAWAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
		//	[appDelegate presentOMEMainViewController];
			
		//	UIViewController *vc = [[UIViewController alloc] init];
		//	[self.navigationController pushViewController:vc animated:YES];
			
			
		//	UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"OMEMainViewController" bundle:nil];
		//	UITabBarController *rootViewController = [storyboard instantiateViewControllerWithIdentifier:@"OMEMainViewController"];
		//	[[UIApplication sharedApplication].keyWindow setRootViewController:rootViewController];
			
			
		} else {
			// Didn't get a user.
			NSLog(@"%s can not find any a player !", __PRETTY_FUNCTION__);

			// Re-enable the done button if we're tossing them back into the form.
			doneButton.enabled = [self shouldEnableDoneButton];
			UIAlertView *alertView = nil;

			if (error == nil) {
				// the username or password is probably wrong.
				alertView = [[UIAlertView alloc] initWithTitle:@"Couldn’t log in:\nThe username or password were wrong." message:nil delegate:self cancelButtonTitle:nil otherButtonTitles:@"Ok", nil];
			} else {
				// Something else went horribly wrong:
				alertView = [[UIAlertView alloc] initWithTitle:[[error userInfo] objectForKey:@"error"] message:nil delegate:self cancelButtonTitle:nil otherButtonTitles:@"Ok", nil];
			}
			[alertView show];
			// Bring the keyboard back up, because they'll probably need to change something.
			[usernameField becomeFirstResponder];
		}
	}];
	
	*/
}


#pragma mark Keyboard

- (void)dismissKeyboard {
	[self.view endEditing:YES];
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
	
	CGRect keyboardFrame = [self.view convertRect:endFrame fromView:self.view.window];
	
	CGFloat scrollViewOffsetY = (CGRectGetHeight(keyboardFrame) -
								 (CGRectGetMaxY(self.view.bounds) -
								  CGRectGetMaxY(self.loginButton.frame) - 10.0f));
	
	if (scrollViewOffsetY < 0) {
		return;
	}
	
	CGFloat duration = [userInfo[UIKeyboardAnimationDurationUserInfoKey] doubleValue];
	UIViewAnimationCurve curve = [userInfo[UIKeyboardAnimationCurveUserInfoKey] integerValue];
	
	[UIView animateWithDuration:duration
						  delay:0.0
						options:curve << 16 | UIViewAnimationOptionBeginFromCurrentState
					 animations:^{
						 [self.scrollView setContentOffset:CGPointMake(0.0f, scrollViewOffsetY) animated:NO];
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
						 [self.scrollView setContentOffset:CGPointZero animated:NO];
					 }
					 completion:nil];
}

#pragma mark ActivityView

- (void)setActivityViewVisible:(BOOL)visible {
	if (self.activityViewVisible == visible) {
		return;
	}
	
	_activityViewVisible = visible;
	
	if (_activityViewVisible) {
		OMEActivityView *activityView = [[OMEActivityView alloc] initWithFrame:self.view.bounds];
		activityView.label.text = kOMEParseLogin;
		activityView.label.font = [UIFont boldSystemFontOfSize:20.f];
		[activityView.activityIndicator startAnimating];
		
		_activityView = activityView;
		[self.view addSubview:_activityView];
	} else {
		[_activityView removeFromSuperview];
		_activityView = nil;
	}
}



-(BOOL)hidesBottomBarWhenPushed
{
	return YES;
}


@end
