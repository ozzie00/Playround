//
//  OMEFeedbackViewController.m
//  ToPlay
//
//  Created by Ozzie Zhang on 10/10/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMEFeedbackViewController.h"

#import "AppDelegate.h"
#import <Parse/Parse.h>

#import "OMETime.h"
#import "OMEConstant.h"
#import "OMEMessage.h"

@interface OMEFeedbackViewController ()

@end

@implementation OMEFeedbackViewController
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@synthesize textView;
@synthesize characterCount;


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
	
	
	self.navigationItem.title = kOMEFeedbackTitle;
	
	// Do any additional setup after loading the view from its nib.
	
	//Ozzie Zhang 2014-09-05 disable
	//self.characterCount.text = @"0/140";
	// Show the keyboard/accept input.
	
	NSDate *today;
	today = [NSDate date];
	NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
	
	//tomorrow = [today dateByAddingTimeInterval: secondsPerDay];
	
	[dateFormatter setDateFormat:@"MMM dd, YYYY"];
	
	NSString *currentTime = [dateFormatter stringFromDate:today];
	
	textView.text = [NSString stringWithFormat:@"%@ %@",kOMEFeedbackPlacehlod, currentTime];
	textView.textColor = [UIColor lightGrayColor];
	textView.delegate = self;


	//[textView becomeFirstResponder];
}


- (BOOL) textViewShouldBeginEditing:(UITextView *)newtextView
{
	newtextView.text = @"";
	newtextView.textColor = [UIColor blackColor];
	return YES;
}

-(void) textViewDidChange:(UITextView *)newtextView
{
	
	if(newtextView.text.length == 0){
		newtextView.textColor = [UIColor lightGrayColor];
		newtextView.text = kOMEFeedbackPlacehlod;
		[newtextView becomeFirstResponder];
	}
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
	// Return YES for supported orientations
	return (interfaceOrientation == UIInterfaceOrientationPortrait);
}


- (void)dealloc {
	
}



#pragma mark - implement delegate method to create invite requirement

#pragma mark UINavigationBar-based actions

- (IBAction)cancelPost:(id)sender {
	[self dismissModalViewControllerAnimated:YES];
}

- (IBAction)postPost:(id)sender {
	// Resign first responder to dismiss the keyboard and capture in-flight autocorrect suggestions
	[textView resignFirstResponder];
	
	
	// Capture current text field contents:
	

	// Data prep:
	AppDelegate *appDelegate                 = [[UIApplication sharedApplication] delegate];
	CLLocationCoordinate2D currentCoordinate = appDelegate.currentLocation.coordinate;
	PFGeoPoint *currentPoint                 = [PFGeoPoint geoPointWithLatitude:currentCoordinate.latitude longitude:currentCoordinate.longitude];
	PFUser *user           = [PFUser currentUser];
	PFUser *fromUser       = user;
	NSString *toUsername   = @"ToPlayFeedback";
	NSString *fromUsername = user.username;
	
	NSString *sendTime = [OMETime getCurrentTime];
	
	
	OMEMessage *feedback = [[OMEMessage alloc] initWithMessage:nil
												 andToUsername:toUsername
												   andFromUser:fromUser
											   andFromUsername:fromUsername
											 andmessageContent:textView.text
												   andsendTime:sendTime];
	
	// Stitch together a messageObject and send this async to Parse
	PFObject *messageObject = [PFObject objectWithClassName:kOMEParseMessageClassKey];
	
	[messageObject setObject:currentPoint forKey:kOMEParseLocationKey];
	//[messageObject setobject:nil forKey:kOMEParseMessageToUserKey];
	[messageObject setObject:toUsername forKey:kOMEParseMessageToUsernameKey];
	[messageObject setObject:fromUser forKey:kOMEParseMessageFromUserKey];
	[messageObject setObject:fromUsername forKey:kOMEParseMessageFromUsernameKey];
	[messageObject setObject:textView.text forKey:kOMEParseMessageContentKey];
	[messageObject setObject:sendTime forKey:kOMEParseMessageSendTimeKey];
	
	// Use PFACL to restrict future modifications to this object.
	PFACL *readOnlyACL = [PFACL ACL];
	[readOnlyACL setPublicReadAccess:NO];
	[readOnlyACL setPublicWriteAccess:NO];
	[readOnlyACL setReadAccess:YES  forUserId:toUsername];
	[messageObject setACL:readOnlyACL];
	[messageObject saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
		if (error) {
			NSLog(@"Can not save!");
			NSLog(@"%@", error);
			UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:[[error userInfo] objectForKey:@"error"] message:nil delegate:self cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
			[alertView show];
			return;
		}
		if (succeeded) {
			NSLog(@"Successfully saved!");
			NSLog(@"%@", messageObject);
			UIAlertView *alert = [[UIAlertView alloc] initWithTitle:kOMEFeedbackReceiveInfo
															message:nil
														   delegate:nil
												  cancelButtonTitle:kOMEParseOK
												  otherButtonTitles:nil];
			[alert show];
			
		} else {
			NSLog(@"Failed to save.");
		}
	}];
	
	[self dismissModalViewControllerAnimated:YES];
}






@end
