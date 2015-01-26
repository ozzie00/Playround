//
//  OMEInviteCreateViewController.m
//  ToPlay
//
//  Created by Ozzie Zhang on 8/15/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMEInviteCreateViewController.h"

#import "AppDelegate.h"
#import <Parse/Parse.h>

@interface OMEInviteCreateViewController ()

- (void)updateCharacterCount:(UITextView *)aTextView;
- (BOOL)checkCharacterCount:(UITextView *)aTextView;
- (void)textInputChanged:(NSNotification *)note;

@end

@implementation OMEInviteCreateViewController

@synthesize textView;
@synthesize characterCount;
@synthesize postButton;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad {
    [super viewDidLoad];

	// Do any additional setup after loading the view from its nib.
	
	self.characterCount = [[UILabel alloc] initWithFrame:CGRectMake(0.0f, 0.0f, 154.0f, 21.0f)];
	self.characterCount.backgroundColor = [UIColor clearColor];
	self.characterCount.textColor = [UIColor whiteColor];
	self.characterCount.shadowColor = [UIColor colorWithWhite:0.0f alpha:0.7f];
	self.characterCount.shadowOffset = CGSizeMake(0.0f, -1.0f);
	//Ozzie Zhang 2014-09-05 disable 
	//self.characterCount.text = @"0/140";

	[self.textView setInputAccessoryView:self.characterCount];

	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textInputChanged:) name:UITextViewTextDidChangeNotification object:textView];
	[self updateCharacterCount:textView];
	[self checkCharacterCount:textView];

	// Show the keyboard/accept input.
	[textView becomeFirstResponder];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void)dealloc {
	[[NSNotificationCenter defaultCenter] removeObserver:self name:UITextViewTextDidChangeNotification object:textView];
}



#pragma mark - implement delegate method to create invite requirement
- (void)invitePlayerTableViewController:(OMEInvitePlayerTableViewController *)controller didCreateInviteRequirement:(OMEInviteRequirement *)inviteRequirement {
	
}

#pragma mark UINavigationBar-based actions

- (IBAction)cancelPost:(id)sender {
	[self dismissModalViewControllerAnimated:YES];
}

- (IBAction)postPost:(id)sender {
	// Resign first responder to dismiss the keyboard and capture in-flight autocorrect suggestions
	[textView resignFirstResponder];


	// Capture current text field contents:
	[self updateCharacterCount:textView];
	BOOL isAcceptableAfterAutocorrect = [self checkCharacterCount:textView];

	if (!isAcceptableAfterAutocorrect) {
		[textView becomeFirstResponder];
		return;
	}

	// Data prep:
	AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
	CLLocationCoordinate2D currentCoordinate = appDelegate.currentLocation.coordinate;
	PFGeoPoint *currentPoint = [PFGeoPoint geoPointWithLatitude:currentCoordinate.latitude longitude:currentCoordinate.longitude];
	PFUser *user = [PFUser currentUser];
	
	

	// Stitch together a inviteObject and send this async to Parse
	PFObject *inviteObject = [PFObject objectWithClassName:kOMEParseClassKey];
	[inviteObject setObject:textView.text forKey:kOMEParseTextKey];
	[inviteObject setObject:user forKey:kOMEParseUserKey];
	[inviteObject setObject:currentPoint forKey:kOMEParseLocationKey];
	
	
	
	// Use PFACL to restrict future modifications to this object.
	PFACL *readOnlyACL = [PFACL ACL];
	[readOnlyACL setPublicReadAccess:YES];
	[readOnlyACL setPublicWriteAccess:NO];
	[inviteObject setACL:readOnlyACL];
	[inviteObject saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
		if (error) {
			NSLog(@"Can not save!");
			NSLog(@"%@", error);
			UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:[[error userInfo] objectForKey:@"error"] message:nil delegate:self cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
			[alertView show];
			return;
		}
		if (succeeded) {
			NSLog(@"Successfully saved!");
			NSLog(@"%@", inviteObject);
			dispatch_async(dispatch_get_main_queue(), ^{
				[[NSNotificationCenter defaultCenter] postNotificationName:kOMEInviteCreatedNotification object:nil];
			});
		} else {
			NSLog(@"Failed to save.");
		}
	}];

	[self dismissModalViewControllerAnimated:YES];
}


#pragma mark -
#pragma mark - implement OMEInvitePlayerTableViewControllerDelegate method
- (void)didCreateInviteRequirement:(OMEInviteRequirement *)inviteRequirement {
	
	NSLog(@"Delegate OMEInvitePlayerTableViewController implement in OMEInviteCreateViewController.m");
	NSLog(@"AppDelegate.m get ready to didCreateInviteRequirement() !");
	
	
	// Data prep:
	AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
	CLLocationCoordinate2D currentCoordinate = appDelegate.currentLocation.coordinate;
	PFGeoPoint *currentPoint = [PFGeoPoint geoPointWithLatitude:currentCoordinate.latitude longitude:currentCoordinate.longitude];
	PFUser *user = [PFUser currentUser];
	NSString *username = user.username;
	
	
	
	// Stitch together a inviteObject and send this async to Parse
	PFObject *inviteObject = [PFObject objectWithClassName:kOMEParseClassKey];
	[inviteObject setObject:textView.text forKey:kOMEParseTextKey];
	[inviteObject setObject:user forKey:kOMEParseUserKey];
	[inviteObject setObject:username forKey:kOMEParseUsernameKey];
	[inviteObject setObject:currentPoint forKey:kOMEParseLocationKey];
	
	// Use PFACL to restrict future modifications to this object.
	PFACL *readOnlyACL = [PFACL ACL];
	[readOnlyACL setPublicReadAccess:YES];
	[readOnlyACL setPublicWriteAccess:NO];
	[inviteObject setACL:readOnlyACL];
	[inviteObject saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
		if (error) {
			NSLog(@"Can not save!");
			NSLog(@"%@", error);
			UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:[[error userInfo] objectForKey:@"error"] message:nil delegate:self cancelButtonTitle:nil otherButtonTitles:@"Ok", nil];
			[alertView show];
			return;
		}
		if (succeeded) {
			NSLog(@"OMEInviteCreateViewController Successfully saved!");
			NSLog(@"%@", inviteObject);
			dispatch_async(dispatch_get_main_queue(), ^{
				[[NSNotificationCenter defaultCenter] postNotificationName:kOMEInviteCreatedNotification object:nil];
			});
		} else {
			NSLog(@"Failed to save.");
		}
	}];
	
}


#pragma mark UITextView notification methods

- (void)textInputChanged:(NSNotification *)note {
	// Listen to the current text field and count characters.
	UITextView *localTextView = [note object];
	[self updateCharacterCount:localTextView];
	[self checkCharacterCount:localTextView];
}

#pragma mark Private helper methods

- (void)updateCharacterCount:(UITextView *)aTextView {
	NSUInteger count = aTextView.text.length;
	//Ozzie Zhang 2014-09-05 disable
	//self.characterCount.text = [NSString stringWithFormat:@"%i/140", count];
	if (count > kOMEInviteOtherMaximumCharacterCount || count == 0) {
		self.characterCount.font = [UIFont boldSystemFontOfSize:self.characterCount.font.pointSize];
	} else {
		self.characterCount.font = [UIFont systemFontOfSize:self.characterCount.font.pointSize];
	}
}

- (BOOL)checkCharacterCount:(UITextView *)aTextView {
	NSUInteger count = aTextView.text.length;
	if (count > kOMEInviteOtherMaximumCharacterCount || count == 0) {
		postButton.enabled = NO;
		return NO;
	} else {
		postButton.enabled = YES;
		return YES;
	}
}

@end
