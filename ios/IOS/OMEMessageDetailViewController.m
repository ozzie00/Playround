//
//  OMEMessageDetailViewController.m
//  ToPlay
//
//  Created by Ozzie Zhang on 10/5/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//


static CGFloat const kOMEMessageDetailTableViewFontSize        = 16.0f;
static CGFloat const kOMEMessageDetailTableViewFontSizeForTime = 12.0f;

 CGFloat kOMEMessageDetailTableViewCellWidth = 230.0f;

// Cell dimension and positioning constants
static CGFloat const kOMECellPaddingTop             = 6.0f;
static CGFloat const kOMECellPaddingBottom          = 1.0f;
static CGFloat const kOMECellPaddingSides           = 0.0f;

static CGFloat const kOMECellReplyTimePaddingTop    = 2.0f;
static CGFloat const kOMECellReplyTimePaddingBottom = 2.0f;
static CGFloat const kOMECellReplyTimePaddingSides  = 3.0f;

static CGFloat const kOMECellReplyTimeBetweenTextSpan = 8.0f;

static CGFloat const kOMECellTextPaddingTop         = 6.0f;
static CGFloat const kOMECellTextPaddingBottom      = 5.0f;
static CGFloat const kOMECellTextPaddingSides       = 5.0f;

static CGFloat const KOMECellAvatarPaddingTop       = 0.0f;
static CGFloat const kOMECellAvatarPaddingBottom    = 10.0f;
static CGFloat const kOMECellAvatarPaddingSides     = 10.0f;

static CGFloat const kOMECellAvatarWidth            = 40.0f;
static CGFloat const kOMECellAvatarHeight           = 40.0f;

// TableViewCell ContentView tags
static NSInteger kOMECellBackgroundTag     = 2;
static NSInteger kOMECellTextLabelTag      = 3;

static NSInteger kOMECellTimeBackgroundTag = 5;
static NSInteger kOMECellTimeTextLabelTag  = 6;

static NSInteger kOMECellUserBackgroundTag = 8;


// Message Reply Frame dimension and positioning constants
static NSInteger kOMEReplyFramePaddingTop         = 4.0f;
static NSInteger kOMEReplyFramePaddingBottom      = 4.0f;
static NSInteger kOMEReplyFramePaddingSlides      = 8.0f;
static NSInteger kOMEReplyFrameHeight             = 40.0f;

static NSInteger kOMEReplySendButtonPaddingTop    = 4.0f;
static NSInteger kOMEReplySendButtonPaddingBootom = 4.0f;
static NSInteger kOMEReplySendButtonPaddingSlides = 4.0f;
static NSInteger kOMEReplySendButtonHeight        = 32.0f;
static NSInteger kOMEReplySendButtonWidth         = 56.0f;


static NSUInteger const kOMETableViewMainSection  = 0;


#import "AppDelegate.h"

#import "OMEConstant.h"
#import "OMEMessageDetailViewController.h"
#import "OMEMessageListViewController.h"

#import "OMEMessageCell.h"
#import "OMEMessage.h"
#import "OMEInvite.h"
#import "OMETime.h"
#import "OMEDebug.h"

@interface OMEMessageDetailViewController ()

@property (nonatomic, copy) NSString *fromUsername;
@property (nonatomic, copy) PFUser *fromUser;
@property (nonatomic, retain) OMEMessage *message;

@end

@implementation OMEMessageDetailViewController {
	BOOL isFirstAppearance;
	NSInteger readTimeAtIndex;
	NSString *replyContent;
	UIView *replyAccessoryView;
    CGFloat textHeight;

	PFUser *toUser;
}


@synthesize replyTextField;
@synthesize enteredText;
@synthesize fromUsername;
//@synthesize fromUser;
@synthesize delegate;
@synthesize message;
//@synthesize inputView;
@synthesize inputAccessoryView;
@synthesize inputTextField;
@synthesize sendButton;
//@synthesize footerView;




#define DEBUG 1


- (void)viewDidLoad {
    [super viewDidLoad];
	
	BOOL isFirstAppearance = YES;
	
	// Dedicate variable for setting time cell height
	readTimeAtIndex = -1;
	
	if (NSClassFromString(kOMEParseRefresh)) {
		// Use the new iOS 6 refresh control.
		UIRefreshControl *refreshControl = [[UIRefreshControl alloc] init];
		self.refreshControl = refreshControl;
		self.refreshControl.tintColor = [UIColor colorWithRed:28.0f/255.0f green:161.0f/255.0f blue:85.0f/255.0f alpha:1.0f];
		[self.refreshControl addTarget:self action:@selector(refreshControlValueChanged:) forControlEvents:UIControlEventValueChanged];
		self.pullToRefreshEnabled = NO;
	}
	
	UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(dismissKeyboard:)];
	[self.view addGestureRecognizer:tap];
	
	
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
	//self.navigationItem.leftBarButtonItem = self.editButtonItem;
	
	//self.navigationItem.hidesBackButton = YES;

	
	UIImage *originalImage    = [UIImage imageNamed:@"backIcon.png"];
	UIEdgeInsets insets       = UIEdgeInsetsMake(0, 5, 0, 5);
	UIImage *stretchableImage = [originalImage resizableImageWithCapInsets:insets];
	
	
	// get messageList count
	NSUInteger messageListCount  = [self.delegate getMessageListCountDidSelectMessage:self];
	NSString *messageButtonTitle = [NSString stringWithFormat:@"%@(%d)",  kOMEMessageTitle, messageListCount];
	
	
	
	// define left button "Messages" to back OMEMessageListViewController
	UIBarButtonItem *backButton = [[UIBarButtonItem alloc] initWithTitle:messageButtonTitle
																   style:UIBarButtonItemStyleBordered
																  target:self
																  action:@selector(backToMessageListView:)];
	[backButton setBackButtonBackgroundImage:stretchableImage forState:UIControlStateNormal barMetrics:0];
	self.navigationItem.leftBarButtonItem = backButton;
	
	// define right button to invoke keyboard for writing message to reply FromUsername's message
	//UIBarButtonItem *replyButton = [[UIBarButtonItem alloc] initWithTitle:@"Reply" style:UIBarButtonItemStyleBordered target:self action:@selector(replyMessageToFromUsername:)];
	
	//self.navigationItem.rightBarButtonItem = replyButton;
	

	// Get fromUserame via delegate
	self.fromUsername = [self.delegate getFromUsernameDidSelectMessage:self];
	
	OMEDebug("fromUsername = %@", self.fromUsername);
	

//	PFQuery *query = [PFQuery queryWithClassName:@"GameScore"];
//	[query whereKey:@"playerEmail" equalTo:@"dstemkoski@example.com"];
//	[query getFirstObjectInBackgroundWithBlock:^(PFObject *object, NSError *error) {
// if (!object) {
//	  NSLog(@"The getFirstObject request failed.");
//  } else {
	  // The find succeeded.
//	  NSLog(@"Successfully retrieved the object.");
//  }
//	}];
	
	PFQuery *query = [PFUser query];
	[query whereKey:kOMEParseUsernameKey equalTo:self.fromUsername]; // find all the women
	NSArray *finduser = [query findObjects];
//	OMEDebug("finduser count = %d", [finduser count]);
//	OMEDebug(" %@", [finduser objectAtIndex:0]);
	
	toUser = (PFUser *)[finduser objectAtIndex:0];
//	OMEDebug("testUset = %@", toUser);
	
	
	// Display FromUsername at the center of navigation bar
	self.navigationItem.title = self.fromUsername;
	
	// Define reply corresponding view frame
	CGRect replyFrame = CGRectMake(0.0, self.view.frame.size.height - self.navigationController.navigationBar.frame.size.height - kOMEReplyFrameHeight, self.tableView.frame.size.width, kOMEReplyFrameHeight);
	replyAccessoryView = [[UIView alloc] initWithFrame:replyFrame];
	replyAccessoryView.backgroundColor = [UIColor colorWithRed:220/255.0 green:222/255.0 blue:227/255.0 alpha:1];
	[self.view addSubview:replyAccessoryView];
	//self.tableView.tableFooterView = replyAccessoryView;
	
	//UIImageView *imView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"bar.png"]];
	//self.tableView.tableFooterView = imView;

	
	// Define reply corresponding textfield
	
	replyTextField = [[UITextField alloc] initWithFrame:CGRectMake(kOMEReplyFramePaddingSlides, self.tableView.frame.size.height - self.navigationController.navigationBar.frame.size.height- kOMEReplyFrameHeight + kOMEReplyFramePaddingTop, self.tableView.frame.size.width - kOMEReplyFramePaddingSlides - kOMEReplySendButtonPaddingSlides*2 - kOMEReplySendButtonWidth, kOMEReplyFrameHeight - kOMEReplyFramePaddingTop - kOMEReplyFramePaddingBottom)];
	replyTextField.placeholder = kOMEMessageInputTextPlacehold;
	[replyTextField setBackgroundColor:[UIColor whiteColor]];
	//[replyTextField addTarget:self action:@selector(replyMessageToFromUsername:)
	//		forControlEvents:UIControlEventTouchUpInside];
	[self.view addSubview:replyTextField];
	
	
	// define reply corresponding send button
	
	UIButton  *replySendButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
	replySendButton.frame = CGRectMake(self.tableView.frame.size.width - kOMEReplySendButtonPaddingSlides - kOMEReplySendButtonWidth , self.tableView.frame.size.height - self.navigationController.navigationBar.frame.size.height- kOMEReplyFrameHeight+kOMEReplyFramePaddingTop, kOMEReplySendButtonWidth, kOMEReplyFrameHeight - kOMEReplyFramePaddingTop - kOMEReplyFramePaddingBottom);
	[replySendButton setTitle:kOMEMessageSendButtonTitle  forState:UIControlStateNormal];
	[replySendButton setTitleColor:[UIColor colorWithRed:28.0f/255.0f green:161.0f/255.0f blue:85.0f/255.0f alpha:1.0f] forState:UIControlStateNormal];
   // [replySendButton setBackgroundColor:[UIColor colorWithRed:28.0f/255.0f green:161.0f/255.0f blue:85.0f/255.0f alpha:1.0f]];
	[replySendButton addTarget:self action:@selector(replyMessageToFromUsername:)
		 forControlEvents:UIControlEventTouchUpInside];
	[self.view addSubview:replySendButton];
	
	// define navigation property
	[self.navigationController setNavigationBarHidden:NO animated:NO];
	
	
	// set nofification for keyboard events
	//[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow:) name:UIKeyboardWillShowNotification object:nil];
	//[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardDidShow:) name:UIKeyboardDidShowNotification object:nil];
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(postWasCreated:) name:kOMEInviteCreatedNotification object:nil];
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(dismissKeyboard:) name:UIKeyboardDidHideNotification object:nil];
	[[NSNotificationCenter defaultCenter] removeObserver:self name:UITextFieldTextDidChangeNotification object:inputTextField];
	[[NSNotificationCenter defaultCenter] removeObserver:self name:UITextFieldTextDidChangeNotification object:replyTextField];
	
	
	
	//self.tableView.backgroundColor = [UIColor clearColor];
	self.tableView.separatorColor = [UIColor clearColor];
	
	// define kOMEMessageDetailTableViewCellWidth
	kOMEMessageDetailTableViewCellWidth = self.tableView.frame.size.width - kOMECellAvatarPaddingSides -kOMECellAvatarWidth - kOMECellAvatarPaddingSides - kOMECellAvatarPaddingSides - kOMECellAvatarPaddingSides - kOMECellAvatarPaddingSides;
	
	}




#pragma mark - UITableViewDataSource





- (id)initWithStyle:(UITableViewStyle)style {
	self = [super initWithStyle:style];
	if (self) {
		// Customize the table:
		
		// The className to query on
		self.parseClassName = kOMEParseMessageClassKey;
		
		// The key of the PFObject to display in the label of the default cell style
		self.textKey = kOMEParseMessageContentKey;
		
		// Whether the built-in pull-to-refresh is enabled
		if (NSClassFromString(kOMEParseRefresh)) {
			self.pullToRefreshEnabled = NO;
		} else {
			self.pullToRefreshEnabled = YES;
		}
		
		// Whether the built-in pagination is enabled
		self.paginationEnabled = YES;
		
		//Ozzie Zhang 2014-10-07 disable this line code
		// The number of objects to show per page
		// self.objectsPerPage = kOMEInviteSearchLimit;
	}
	return self;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void)backToMessageListView:(id)sender {
	
	[[self navigationController] popViewControllerAnimated:NO];
	
}


- (void)viewWillDisappear:(BOOL)animated {

	[super viewDidAppear:animated];
	
	if (isFirstAppearance) {
		NSLog(@"root view controller is moving to parent");
		isFirstAppearance = NO;
	} else {
		//NSLog(@"root view controller, not moving to parent");
	}
	
	//self.isBeingDismissed for presenting modal view controller
	if (self.isMovingFromParentViewController || self.isBeingDismissed) {
		
		NSLog(@"isMovingFromParentViewController ");
	}
	
}


// this viewDidDisapper can execute backing to OMEMessageListViewController
- (void) viewDidDisappear:(BOOL)animated {
	
	if (self.parentViewController == nil) {
		//NSLog(@"viewDidDisappear doesn't have parent so it's been popped");
		//release stuff here
		
		[[self navigationController] popViewControllerAnimated:NO];
		
	} else {
		NSLog(@"PersonViewController view just hidden");
	}
	
}

- (void)viewWillAppear:(BOOL)animated {
	[super viewWillAppear:animated];
	
	//	[self.replyTextField becomeFirstResponder];
}

- (void) viewDidAppear:(BOOL)animated {
	//isMovingToParentViewController();
	/*
	 // Define reply corresponding view frame
	 CGRect replyFrame = CGRectMake(0.0, self.view.window.frame.size.height - kOMEReplyFrameHeight, self.view.window.frame.size.width, kOMEReplyFrameHeight);
	 replyAccessoryView = [[UIView alloc] initWithFrame:replyFrame];
	 replyAccessoryView.backgroundColor = [UIColor colorWithRed:220/255.0 green:222/255.0 blue:227/255.0 alpha:1];
	 [self.view.window addSubview:replyAccessoryView];
	 
	 // Define reply corresponding textfield
	 
	 replyTextField = [[UITextField alloc] initWithFrame:CGRectMake(kOMEReplyFramePaddingSlides, self.view.window.frame.size.height - kOMEReplyFrameHeight + kOMEReplyFramePaddingTop, self.view.window.frame.size.width - kOMEReplyFramePaddingSlides - kOMEReplySendButtonPaddingSlides*2 - kOMEReplySendButtonWidth, kOMEReplyFrameHeight - kOMEReplyFramePaddingTop - kOMEReplyFramePaddingBottom)];
	 replyTextField.placeholder = kOMEMessageInputTextPlacehold;
	 [replyTextField setBackgroundColor:[UIColor whiteColor]];
	 [replyTextField addTarget:self action:@selector(sendReplyMessageToFromUsername:)
	 forControlEvents:UIControlEventTouchUpInside];
	 
	 self.replyTextField.inputAccessoryView = inputAccessoryView;
	 [self.view.window addSubview:replyTextField];
	 
	 
	 // define reply corresponding send button
	 
	 UIButton  *replySendButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
	 replySendButton.frame = CGRectMake(self.view.window.frame.size.width - kOMEReplySendButtonPaddingSlides - kOMEReplySendButtonWidth , self.view.window.frame.size.height - kOMEReplyFrameHeight + kOMEReplyFramePaddingTop, kOMEReplySendButtonWidth, kOMEReplyFrameHeight - kOMEReplyFramePaddingTop - kOMEReplyFramePaddingBottom);
	 [replySendButton setTitle:kOMEMessageSendButtonTitle  forState:UIControlStateNormal];
	 [replySendButton setTitleColor:[UIColor colorWithRed:28.0f/255.0f green:161.0f/255.0f blue:85.0f/255.0f alpha:1.0f] forState:UIControlStateNormal];
	 //[replySendButton setBackgroundColor:[UIColor colorWithRed:28.0f/255.0f green:161.0f/255.0f blue:85.0f/255.0f alpha:1.0f]];
	 [replySendButton addTarget:self action:@selector(sendReplyMessageToFromUsername:)
	 forControlEvents:UIControlEventTouchUpInside];
	 [self.view.window addSubview:replySendButton];
	 
	 */
	
	
	//	[inputTextField becomeFirstResponder];
 
 
	
	
	
}



- (void)willMoveToParentViewController:(UIViewController *)parent {
	
}








- (void)viewDidLayoutSubviews {
	[super viewDidLayoutSubviews];
	
	//self.activityView.frame = self.view.bounds;
	//self.replyTextField.contentSize = self.backgroundView.bounds.size;
}


#pragma mark - UITextFieldDelegate methods

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
	
	//	[replyTextField resignFirstResponder];
	
	if (textField == replyTextField) {
		[inputTextField  becomeFirstResponder];
		//		[self processFieldEntries];
	}
	
	if (textField == self.inputTextField) {
		[textField resignFirstResponder];
		[replyTextField becomeFirstResponder];
		//return NO;
		
	}
	
	
	return YES;
}


- (void)completeCurrentWord:(id)sender {
	
	//	NSString *username = usernameField.text;
	
	
	
}

/*

 - (void)customedKeyboardDidChange
 {
 //	self.inputTextField.inputView = nil;
 //	[self.inputTextField reloadInputViews];
 }
 
 - (void)systemKeyboardDidChange
 {
	//self.inputTextField.inputView = _inputView;
 //	[self.inputTextField reloadInputViews];
 }
 
 - (void)numberButtonClicked:(id)sender
 {
	//[[UIDevice currentDevice] playInputClick];
 }

- (void)keyboardWillShow:(id)notification {
	
}

- (void)keyboardDidShow:(id)notification {
	
}*/
 
 /*
 - (void)keyboardDidShow:(id)notification
 {
	_keyboardDefaultView = [self getSystemKeyboardView];
	if (_keyboardDefaultView && [_numberTextField isFirstResponder])
	{
 _switchNumButton = [UIButton buttonWithType:UIButtonTypeCustom];
 [_switchNumButton setTitle:@"123" forState:UIControlStateNormal];
 [_switchNumButton setBackgroundImage:[UIImage imageNamed:@"num.png"] forState:UIControlStateNormal];
 _switchNumButton.frame = CGRectMake(1, 173, 78, 42);
 [_switchNumButton addTarget:self action:@selector(changeCutomeButtonClicked:) forControlEvents:UIControlEventTouchUpInside];
 [_keyboardDefaultView addSubview:_switchNumButton];
	}
	else
	{
 if (_switchNumButton) {
 [_switchNumButton removeFromSuperview];
 }
	}
 
 }
 
 - (void)keyboardWillShow:(id)notification {
	
 }
 */


#pragma mark Keyboard

- (void)dismissKeyboard:(id)sender {
	
	//[inputTextField resignFirstResponder];
	
	if (sender == inputTextField) {
		[inputTextField resignFirstResponder];
		//[replyTextField becomeFirstResponder];
	} else if (sender == replyTextField) {
		[replyTextField resignFirstResponder];
	//	[inputTextField becomeFirstResponder];
		
	}
	
	[self.view endEditing:YES];
}


- (void)replyMessageToFromUsername:(id)sender {
//	[inputTextField becomeFirstResponder];
		//inputTextField.hidden = YES;

    self->replyContent = replyTextField.text;
	
	[self dismissKeyboard:replyTextField];
    NSLog(@"replyMessageToFromUsername replyContent = %@", replyContent);
	self.replyTextField.text = nil;
	
	// Create message reply to
	if ([PFUser currentUser]) {
		
		// Data prep:
		PFUser *user           = [PFUser currentUser];
		PFUser *fromuser       = user;
		NSString *username     = fromuser.username;
		NSString *fromusername = fromuser.username;
		
		OMEDebug(@"please change tousername, now only for test");
		NSString *tousername     = self.fromUsername;
		NSString *messagecontent = self->replyContent;
		NSString *sendtime       = [OMETime getCurrentTime];
		
		OMEDebug(@"tousername = %@, fromusername = %@", tousername, fromusername);
		
		// Stitch together a inviteObject and send this async to Parse
		PFObject *sendMessageObject = [PFObject objectWithClassName:kOMEParseMessageClassKey];
		[sendMessageObject setObject:user forKey:kOMEParseUserKey];
		[sendMessageObject setObject:username forKey:kOMEParseUsernameKey];
		[sendMessageObject setObject:tousername forKey:kOMEParseMessageToUsernameKey];
		[sendMessageObject setObject:messagecontent forKey:kOMEParseMessageContentKey];
		[sendMessageObject setObject:fromuser forKey:kOMEParseMessageFromUserKey];
		[sendMessageObject setObject:fromusername forKey:kOMEParseMessageFromUsernameKey];
		[sendMessageObject setObject:sendtime forKey:kOMEParseMessageSendTimeKey];
		
		// check toUser object is nil
		if (toUser != nil) {
			PFUser *touser         = toUser;
			[sendMessageObject setObject:touser forKey:kOMEParseMessageToUserKey];
			
			OMEDebug("Please check read and write access");
			// Use PFACL to restrict future modifications to this object.
			PFACL *touserACL = [PFACL ACLWithUser:touser];
			[touserACL setPublicReadAccess:NO];
			[touserACL setPublicWriteAccess:NO];
			[touserACL setReadAccess:YES forUser:touser];
			[sendMessageObject setACL:touserACL];
			[sendMessageObject saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
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
					//	NSLog(@"Successfully Send Message to %@ saved!", toUser.username);
					dispatch_async(dispatch_get_main_queue(), ^{
						[[NSNotificationCenter defaultCenter] postNotificationName:kOMEMessageSendNotification object:nil];
					});
				} else {
					NSLog(@"Failed to save.");
				}
			}];
		} else {
			//Ozzie Zhang 2014-09-28 need modify cancelButtonTitle:nil
			UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEMessageNoUserInfo
																message:nil
															   delegate:nil
													  cancelButtonTitle:nil
													  otherButtonTitles:kOMEParseOK, nil];
			[alertView show];
			return;
			
		}
	}
}


- (void)sendReplyMessageToFromUsername:(id)sender {
	
	self->replyContent = inputTextField.text;
	NSLog(@"sendReplyMessageToFromUsername replyContent = %@", replyContent);

	self.inputTextField.text = nil;
	[self dismissKeyboard:inputTextField];
	
	// set nil to clear the latest input message, so need check the message length
	// when send message to FromUsername
	self.inputTextField.text = nil;
	
	
	// Create message reply to
	if ([PFUser currentUser]) {
		
		// Data prep:
		PFUser *user           = [PFUser currentUser];
		PFUser *fromuser       = user;
		NSString *username     = fromuser.username;
		NSString *fromusername = fromuser.username;
		
		OMEDebug(@"please change tousername, now only for test");
		NSString *tousername     = self.fromUsername;
		NSString *messagecontent = self->replyContent;
		NSString *sendtime       = [OMETime getCurrentTime];
		
		OMEDebug(@"tousername = %@, fromusername = %@", tousername, fromusername);
		
		// Stitch together a inviteObject and send this async to Parse
		PFObject *sendMessageObject = [PFObject objectWithClassName:kOMEParseMessageClassKey];
		[sendMessageObject setObject:user forKey:kOMEParseUserKey];
		[sendMessageObject setObject:username forKey:kOMEParseUsernameKey];
		[sendMessageObject setObject:tousername forKey:kOMEParseMessageToUsernameKey];
		[sendMessageObject setObject:messagecontent forKey:kOMEParseMessageContentKey];
		[sendMessageObject setObject:fromuser forKey:kOMEParseMessageFromUserKey];
		[sendMessageObject setObject:fromusername forKey:kOMEParseMessageFromUsernameKey];
		[sendMessageObject setObject:sendtime forKey:kOMEParseMessageSendTimeKey];
		
		// check toUser object is nil
		if (toUser != nil) {
			PFUser *touser         = toUser;
			[sendMessageObject setObject:touser forKey:kOMEParseMessageToUserKey];
			
			OMEDebug("Please check read and write access");
			// Use PFACL to restrict future modifications to this object.
			PFACL *touserACL = [PFACL ACLWithUser:touser];
			[touserACL setPublicReadAccess:NO];
			[touserACL setPublicWriteAccess:NO];
			[touserACL setReadAccess:YES forUser:touser];
			[sendMessageObject setACL:touserACL];
			[sendMessageObject saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
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
					//	NSLog(@"Successfully Send Message to %@ saved!", toUser.username);
					dispatch_async(dispatch_get_main_queue(), ^{
						[[NSNotificationCenter defaultCenter] postNotificationName:kOMEMessageSendNotification object:nil];
					});
				} else {
					NSLog(@"Failed to save.");
				}
			}];
		} else {
			//Ozzie Zhang 2014-09-28 need modify cancelButtonTitle:nil
			UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEMessageNoUserInfo
																message:nil
															   delegate:nil
													  cancelButtonTitle:nil
													  otherButtonTitles:kOMEParseOK, nil];
			[alertView show];
			return;
			
		}
	}

}



// Define the accessory for keyboard
- (UIView *)inputAccessoryView {
	if (!inputAccessoryView) {
		
		// Define the frame on keyboard
		CGRect accessFrame                 = CGRectMake(0.0, 10.0, self.tableView.frame.size.width, kOMEReplyFrameHeight);
		inputAccessoryView                 = [[UIView alloc] initWithFrame:accessFrame];
		inputAccessoryView.backgroundColor = [UIColor colorWithRed:220/255.0 green:222/255.0 blue:227/255.0 alpha:1];
		
		// Define input textfield to receive user input
		inputTextField               = [[UITextField alloc] init];
		inputTextField.frame         = CGRectMake(kOMEReplyFramePaddingSlides, kOMEReplyFramePaddingTop, self.tableView.frame.size.width - kOMEReplyFramePaddingSlides - kOMEReplySendButtonPaddingSlides*2 - kOMEReplySendButtonWidth, kOMEReplySendButtonHeight);
		inputTextField.placeholder   = kOMEMessageInputTextPlacehold;
	//	inputTextField.autocorrectionType = UITextAutocorrectionTypeNo;
	//	inputTextField.spellCheckingType = UITextSpellCheckingTypeNo;
		inputTextField.returnKeyType = UIReturnKeySend;
		[inputTextField setBackgroundColor:[UIColor whiteColor]];
		//[inputTextField addTarget:self action:@selector(dismissKeyboard:)
		//		 forControlEvents:UIControlEventTouchUpInside];
		[inputAccessoryView addSubview:inputTextField];
	//	self.inputTextField.delegate = self;
		
		//	self.inputTextField.keyboardType = UIKeyboardTypeNamePhonePad;
	
		
		
		// Define send button for submitting user input content
		sendButton       = [UIButton buttonWithType:UIButtonTypeRoundedRect];
		sendButton.frame = CGRectMake(self.tableView.frame.size.width - kOMEReplySendButtonPaddingSlides - kOMEReplySendButtonWidth, 4.0, kOMEReplySendButtonWidth, kOMEReplySendButtonHeight);
		[sendButton setTitle:kOMEMessageSendButtonTitle  forState:UIControlStateNormal];
		[sendButton setTitleColor:[UIColor colorWithRed:28.0f/255.0f green:161.0f/255.0f blue:85.0f/255.0f alpha:1.0f] forState:UIControlStateNormal];
		//[sendButton setTitleColor:[UIColor blueColor] forState:UIControlStateSelected];
		//[sendButton setBackgroundColor:[UIColor colorWithRed:28.0f/255.0f green:161.0f/255.0f blue:85.0f/255.0f alpha:1.0f]];
		[sendButton addTarget:self action:@selector(sendReplyMessageToFromUsername:)
			 forControlEvents:UIControlEventTouchUpInside];
		[inputAccessoryView addSubview:self.sendButton];
		
	}
	return inputAccessoryView;
}




#pragma mark - Table view data source


/*
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
#warning Potentially incomplete method implementation.
    // Return the number of sections.
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
#warning Incomplete method implementation.
    // Return the number of rows in the section.
	return 1;
}

- (CGFloat)tableView:(UITableView *)tableView estimatedHeightForRowAtIndexPath:(NSIndexPath *)indexPath {
	//return 0;
}

*/


// Override to customize what kind of query to perform on the class. The default is to query for
// all objects ordered by createdAt descending.
- (PFQuery *)queryForTableOrig {
	PFQuery *query = [PFQuery queryWithClassName:kOMEParseMessageClassKey];
	
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

// Override to customize what kind of query to perform on the class. The default is to query for
// all objects ordered by createdAt descending.
- (PFQuery *)queryForTable {
 
	//PFQuery *query = [PFQuery queryWithClassName:kOMEParseMessageClassKey];
	
	// If no objects are loaded in memory, we look to the cache first to fill the table
	// and then subsequently do a query against the network.
	//if ([self.objects count] == 0) {
	//	query.cachePolicy = kPFCachePolicyCacheThenNetwork;
	//}
	
	OMEDebug("2. query");

	
	// 1. query message from fromUsername(she) to me, will display in left side
	
	PFUser *currentuser            = [PFUser currentUser];
	NSString *forMeFromUsername    = [self.delegate getFromUsernameDidSelectMessage:self];

	PFQuery *forMeQuery            = [PFQuery queryWithClassName:kOMEParseMessageClassKey];
	
	if ([self.objects count] == 0) {
		forMeQuery.cachePolicy = kPFCachePolicyCacheElseNetwork;
	}

	[forMeQuery whereKey:kOMEParseMessageToUsernameKey equalTo:currentuser.username];
	
	PFQuery *forMeFromUserQuery    = [PFQuery orQueryWithSubqueries:@[forMeQuery]];
	
	if ([self.objects count] == 0) {
		forMeQuery.cachePolicy = kPFCachePolicyCacheElseNetwork;
	}
	
	[forMeFromUserQuery whereKey:kOMEParseMessageFromUsernameKey equalTo:forMeFromUsername];
	
	//[forMeFromUserQuery orderByAscending:@"createdAt"];
	
	OMEDebug("[forMeFromUserQuery countObjects] %d ", [forMeFromUserQuery countObjects]);

	
	// 2.  query message from me to fromUsername(she), will display in right side
	NSString *meToUsername         = [self.delegate getFromUsernameDidSelectMessage:self];
	
	PFQuery *meToQuery             = [PFQuery queryWithClassName:kOMEParseMessageClassKey];
	
	if ([self.objects count] == 0) {
		forMeQuery.cachePolicy = kPFCachePolicyCacheElseNetwork;
	}
	
	[meToQuery whereKey:kOMEParseMessageFromUsernameKey equalTo:currentuser.username];
	
	PFQuery *meToFromUsernameQuery = [PFQuery orQueryWithSubqueries:@[meToQuery]];
	
	if ([self.objects count] == 0) {
		forMeQuery.cachePolicy = kPFCachePolicyCacheElseNetwork;
	}
	
	[meToFromUsernameQuery whereKey:kOMEParseMessageToUsernameKey equalTo:forMeFromUsername];
	
	//[meToFromUsernameQuery orderByAscending:@"createdAt"];
	
	OMEDebug("forMeFromUsername = %@", forMeFromUsername);
	OMEDebug("[meToFromUsernameQuery countObjects] %d ", [meToFromUsernameQuery countObjects]);
	
	// 3. output query result with Ascend sort
	PFQuery *query = [PFQuery orQueryWithSubqueries:@[forMeFromUserQuery,meToFromUsernameQuery]];
	
	if ([self.objects count] == 0) {
		forMeQuery.cachePolicy = kPFCachePolicyCacheElseNetwork;
	}
	
	[query orderByAscending:@"createdAt"];

    OMEDebug("[query countObjects] %d ", [query countObjects]);
	
	return query;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForNextPageAtIndexPath:(NSIndexPath *)indexPath {
	UITableViewCell *cell = [super tableView:tableView cellForNextPageAtIndexPath:indexPath];
	cell.textLabel.font = [cell.textLabel.font fontWithSize:kOMEMessageDetailTableViewFontSize];
	return cell;
}


#pragma mark - UITableViewDelegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	// call super because we're a custom subclass.
	[super tableView:tableView didSelectRowAtIndexPath:indexPath];
	
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
	// Account for the load more cell at the bottom of the tableview if we hit the pagination limit:
	if ( (NSUInteger)indexPath.row >= [self.objects count]) {
		
		OMEDebug(@"indexPath.row >= [self.objects count]");
		return [tableView rowHeight];
	}
	
	
	// Retrieve the text and username for this row:
	PFObject *object              = [self.objects objectAtIndex:indexPath.row];
	OMEMessage *messageFromObject = [[OMEMessage alloc] initWithMessagePFObject:object];
	NSString *text                = messageFromObject.messageContent;
	NSString *time                = messageFromObject.sendTime;
	
	// Calculate what the frame to fit the post text and the username
	CGSize textSize     = [text sizeWithFont:[UIFont systemFontOfSize:kOMEMessageDetailTableViewFontSize] constrainedToSize:CGSizeMake(kOMEMessageDetailTableViewCellWidth, FLT_MAX) lineBreakMode:UILineBreakModeWordWrap];
	CGSize timeTextSize = [time sizeWithFont:[UIFont systemFontOfSize:kOMEMessageDetailTableViewFontSizeForTime] constrainedToSize:CGSizeMake(kOMEMessageDetailTableViewCellWidth, FLT_MAX) lineBreakMode:UILineBreakModeWordWrap];

	

	// And return this height plus cell padding and the offset of the bubble image height (without taking into account the text height twice)
//	CGFloat rowHeight   = kOMECellPaddingTop*4 + textSize.height + nameSize.height + timeTextSize.height + kOMECellBackgroundffset*2;
	
	CGFloat rowHeight   = kOMECellPaddingTop+kOMECellReplyTimePaddingTop+timeTextSize.height+ kOMECellReplyTimePaddingBottom+kOMECellReplyTimeBetweenTextSpan+kOMECellTextPaddingTop+textHeight+kOMECellTextPaddingBottom+ kOMECellPaddingBottom;
	
   // NSLog(@"cell height = %f,  newTextSizeHeight = %f!", rowHeight,newTextSizeHeight);

	if ( kOMECellTextPaddingTop+timeTextSize.height+kOMECellTextPaddingBottom < kOMECellAvatarHeight) {
		rowHeight   = kOMECellPaddingTop+kOMECellReplyTimePaddingTop+timeTextSize.height+ kOMECellReplyTimePaddingBottom+kOMECellReplyTimeBetweenTextSpan+KOMECellAvatarPaddingTop+ kOMECellAvatarHeight+ kOMECellPaddingBottom;
	}
	
    //return rowHeight;
	
	return  100;
}

//- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
//{
//	return 60.; // you can have your own choice, of course
//}

//- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
//{
//	return 60.; // you can have your own choice, of course
//}

// Override to customize the look of a cell representing an object. The default is to display
// a UITableViewCellStyleDefault style cell with the label being the first key in the object.

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath object:(PFObject *)object {
	
	// Reuse identifiers for left and right cells
	static NSString *RightCellIdentifier = @"RightCell";
	static NSString *LeftCellIdentifier  = @"LeftCell";
	static NSString *TimeCellIdentifier  = @"TimeCell";
	
	// Try to reuse a cell
	BOOL cellIsRight = [[object objectForKey:kOMEParseMessageFromUsernameKey] isEqualToString:[[PFUser currentUser] username]];
	BOOL cellIsLeft  = [[object objectForKey:kOMEParseMessageToUsernameKey] isEqualToString:[[PFUser currentUser] username]];
	//BOOL cellIsCenter = [[[object objectForKey:kOMEParseUserKey] objectForKey:kOMEParseUsernameKey] objectForKey:kOMEParseReplyTimeKey];
	
	UITableViewCell *cell;
	
	//create cell according to cell type including current user, from user and reply time
	if (cellIsRight) {
		//current user use blueBubble
		cell = [tableView dequeueReusableCellWithIdentifier:RightCellIdentifier];
		if (cell == nil) {
			cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:RightCellIdentifier];
			
			UIImageView *backgroundImage = [[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"blueBubble.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(15.0f, 11.0f, 16.0f, 11.0f)]];
			[backgroundImage setTag:kOMECellBackgroundTag];
			[cell.contentView addSubview:backgroundImage];
			
			UILabel *textLabel = [[UILabel alloc] init];
			[textLabel setTag:kOMECellTextLabelTag];
			[cell.contentView addSubview:textLabel];
			
			UIImageView *avatarBackgroundImage = [[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"me40x40icon.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(5.0f, 8.0f, 5.0f, 8.0f)]]; //resizableImageWithCapInsets:UIEdgeInsetsMake(5.0f, 8.0f, 5.0f, 8.0f)
			
			[avatarBackgroundImage setTag:kOMECellUserBackgroundTag];
			[cell.contentView addSubview:avatarBackgroundImage];
			
			UIImageView *timeBackgroundImage = [[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"timeBubble.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(5.0f, 8.0f, 5.0f, 8.0f)]];
			[timeBackgroundImage setTag:kOMECellTimeBackgroundTag];
			[cell.contentView addSubview:timeBackgroundImage];
			
			UILabel *timeTextLabel = [[UILabel alloc] init];
			[timeTextLabel setTag:kOMECellTimeTextLabelTag];
			[cell.contentView addSubview:timeTextLabel];
		}
	} else if (cellIsLeft) {
		cell = [tableView dequeueReusableCellWithIdentifier:LeftCellIdentifier];
		if (cell == nil) {
			cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:LeftCellIdentifier];
			
			UIImageView *backgroundImage = [[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"grayBubble.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(15.0f, 11.0f, 16.0f, 11.0f)]];
			[backgroundImage setTag:kOMECellBackgroundTag];
			[cell.contentView addSubview:backgroundImage];
			
			UILabel *textLabel = [[UILabel alloc] init];
			[textLabel setTag:kOMECellTextLabelTag];
			[cell.contentView addSubview:textLabel];
			
			UIImageView *avatarBackgroundImage = [[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"he40x40icon.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(5.0f, 8.0f, 5.0f, 8.0f)]];
			[avatarBackgroundImage setTag:kOMECellUserBackgroundTag];
			[cell.contentView addSubview:avatarBackgroundImage];
			
			
			//Ozzie Zhang 2014-10-08 change nameLabel to save time
			
			
			UIImageView *timeBackgroundImage = [[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"timeBubble.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(5.0f, 8.0f, 5.0f, 8.0f)]];
			[timeBackgroundImage setTag:kOMECellTimeBackgroundTag];
			[cell.contentView addSubview:timeBackgroundImage];
			
			UILabel *timeTextLabel = [[UILabel alloc] init];
			[timeTextLabel setTag:kOMECellTimeTextLabelTag];
			[cell.contentView addSubview:timeTextLabel];

		}
	} else {
		if (0) {
		cell = [tableView dequeueReusableCellWithIdentifier:TimeCellIdentifier];
		if (cell == nil) {
			cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:TimeCellIdentifier];
			
			UIImageView *backgroundImage = [[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"grayBubble.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(15.0f, 11.0f, 16.0f, 11.0f)]];
			[backgroundImage setTag:kOMECellBackgroundTag];
			[cell.contentView addSubview:backgroundImage];
			
			UILabel *textLabel = [[UILabel alloc] init];
			[textLabel setTag:kOMECellTextLabelTag];
			[cell.contentView addSubview:textLabel];
			
			UIImageView *avatarBackgroundImage = [[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"he40x40icon.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(5.0f, 8.0f, 5.0f, 8.0f)]];
			[avatarBackgroundImage setTag:kOMECellUserBackgroundTag];
			[cell.contentView addSubview:avatarBackgroundImage];

			
			UIImageView *timeBackgroundImage = [[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"timeBubble.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(5.0f, 8.0f, 5.0f, 8.0f)]];
			[timeBackgroundImage setTag:kOMECellTimeBackgroundTag];
			[cell.contentView addSubview:timeBackgroundImage];
			
			UILabel *timeTextLabel = [[UILabel alloc] init];
			[timeTextLabel setTag:kOMECellTimeTextLabelTag];
			[cell.contentView addSubview:timeTextLabel];
			
		}
		}
	}
	
	// Configure the cell content
	UILabel *textLabel        = (UILabel*) [cell.contentView viewWithTag:kOMECellTextLabelTag];
	textLabel.text            = [object objectForKey:kOMEParseMessageContentKey];
	textLabel.lineBreakMode   = UILineBreakModeWordWrap;
	textLabel.numberOfLines   = 0;
	textLabel.font            = [UIFont fontWithName:@"Arial" size:kOMEMessageDetailTableViewFontSize];
	textLabel.textColor       = [UIColor darkTextColor];
	textLabel.backgroundColor = [UIColor clearColor];
	
	
	UILabel *timeTextLabel        = (UILabel*) [cell.contentView viewWithTag:kOMECellTimeTextLabelTag];
	timeTextLabel.text            = [object objectForKey:kOMEParseMessageSendTimeKey];
    timeTextLabel.lineBreakMode   = UILineBreakModeWordWrap;
	timeTextLabel.numberOfLines   = 0;
	timeTextLabel.font            = [UIFont systemFontOfSize:kOMEMessageDetailTableViewFontSizeForTime];
	timeTextLabel.textColor       = [UIColor  whiteColor];
	timeTextLabel.backgroundColor = [UIColor clearColor];
	
	// Move cell content to the right position
	// Calculate the size of the message's text and username
	CGSize textSize     = [[object objectForKey:kOMEParseMessageContentKey] sizeWithFont:[UIFont systemFontOfSize:kOMEMessageDetailTableViewFontSize] constrainedToSize:CGSizeMake(kOMEMessageDetailTableViewCellWidth, FLT_MAX) lineBreakMode:UILineBreakModeWordWrap];
	CGSize timeTextSize = [[object objectForKey:kOMEParseMessageSendTimeKey] sizeWithFont:[UIFont systemFontOfSize:kOMEMessageDetailTableViewFontSizeForTime] constrainedToSize:CGSizeMake(kOMEMessageDetailTableViewCellWidth, FLT_MAX) lineBreakMode:UILineBreakModeWordWrap];
	
	
	// Use textHeight to adjust cell height
	textHeight = textSize.height;
	
	CGFloat cellHeight  = [self tableView:tableView heightForRowAtIndexPath:indexPath]; // Get the height of the cell

	//Place the time content in the corrent position
	UIImageView *backgroundImage       = (UIImageView*) [cell.contentView viewWithTag:kOMECellBackgroundTag];
	UIImageView *avatarBackgroundImage = (UIImageView*) [cell.contentView viewWithTag:kOMECellUserBackgroundTag];
	UIImageView *timeBackgroundImage   = (UIImageView*) [cell.contentView viewWithTag:kOMECellTimeBackgroundTag];
	
	// Place the content in the correct position depending on the type
	// touser content on right
	if (cellIsRight) {
		
		[timeTextLabel setFrame:CGRectMake(self.tableView.frame.size.width/2-timeTextSize.width/2
										   +kOMECellReplyTimePaddingSides,
										   kOMECellPaddingTop+kOMECellReplyTimePaddingTop,
										   timeTextSize.width,
										   timeTextSize.height)];
		
		[timeBackgroundImage setFrame:CGRectMake(self.tableView.frame.size.width/2-timeTextSize.width/2,
												 kOMECellPaddingTop,
												 timeTextSize.width+kOMECellReplyTimePaddingSides*2,
												 timeTextSize.height+kOMECellReplyTimePaddingBottom)];
		
		[textLabel setFrame:CGRectMake(self.tableView.frame.size.width-kOMECellAvatarPaddingSides
									   -kOMECellAvatarWidth-kOMECellAvatarPaddingSides-textSize.width,
									   kOMECellPaddingTop+kOMECellReplyTimePaddingTop+timeTextSize.height+kOMECellReplyTimePaddingBottom+kOMECellReplyTimeBetweenTextSpan+kOMECellTextPaddingTop,
									  // self.tableView.frame.size.width-kOMECellAvatarPaddingSides
									//   -kOMECellAvatarWidth-kOMECellAvatarPaddingSides-kOMECellAvatarPaddingSides-kOMECellAvatarWidth-kOMECellAvatarPaddingSides,//
									   textSize.width,
									   kOMECellTextPaddingTop+textSize.height)];
		
		[backgroundImage setFrame:CGRectMake(self.tableView.frame.size.width-textSize.width
											-kOMECellTextPaddingSides-kOMECellAvatarPaddingSides*2-kOMECellAvatarWidth,
											 kOMECellPaddingTop+kOMECellReplyTimePaddingTop+timeTextSize.height+kOMECellReplyTimePaddingBottom+kOMECellReplyTimeBetweenTextSpan+kOMECellTextPaddingTop,
											textSize.width+kOMECellTextPaddingSides*2,
											kOMECellTextPaddingTop+textSize.height+kOMECellReplyTimePaddingBottom)];
		
		[avatarBackgroundImage setFrame:CGRectMake(self.tableView.frame.size.width
												   -kOMECellAvatarPaddingSides-kOMECellAvatarWidth,kOMECellPaddingTop+kOMECellReplyTimePaddingTop+timeTextSize.height+kOMECellReplyTimePaddingBottom+kOMECellReplyTimeBetweenTextSpan+kOMECellTextPaddingTop,
                                                   kOMECellAvatarWidth,
								                   kOMECellAvatarHeight)];
		
	} else if (cellIsLeft)  {
		// fromuser content on left
		
		[timeTextLabel setFrame:CGRectMake(self.tableView.frame.size.width/2-timeTextSize.width/2
										   +kOMECellReplyTimePaddingSides,
										   kOMECellPaddingTop+kOMECellReplyTimePaddingTop,
										   timeTextSize.width,
										   timeTextSize.height)];

		[timeBackgroundImage setFrame:CGRectMake(self.tableView.frame.size.width/2-timeTextSize.width/2,
												 kOMECellPaddingTop,
												 timeTextSize.width+kOMECellReplyTimePaddingSides*2,
												 timeTextSize.height+kOMECellReplyTimePaddingBottom)];
		
		[textLabel setFrame:CGRectMake(kOMECellAvatarPaddingSides+kOMECellAvatarWidth
									   +kOMECellAvatarPaddingSides+kOMECellTextPaddingSides*2,
									   kOMECellPaddingTop+kOMECellReplyTimePaddingTop+timeTextSize.height+kOMECellReplyTimePaddingBottom+kOMECellReplyTimeBetweenTextSpan+kOMECellTextPaddingTop,
									   textSize.width,
									   textSize.height)];
		[backgroundImage setFrame:CGRectMake(kOMECellAvatarPaddingSides+kOMECellAvatarWidth
											 +kOMECellAvatarPaddingSides,kOMECellPaddingTop+kOMECellReplyTimePaddingTop+timeTextSize.height+kOMECellReplyTimePaddingBottom+kOMECellReplyTimeBetweenTextSpan+kOMECellTextPaddingTop,//kOMECellPaddingTop,
											 textSize.width + kOMECellTextPaddingSides*2,
											 kOMECellTextPaddingTop+textSize.height+kOMECellReplyTimePaddingBottom)];
		[avatarBackgroundImage setFrame:CGRectMake(kOMECellAvatarPaddingSides,
												   kOMECellPaddingTop+kOMECellReplyTimePaddingTop
												   +timeTextSize.height+kOMECellReplyTimePaddingBottom
												   +kOMECellReplyTimeBetweenTextSpan+kOMECellTextPaddingTop,
												   kOMECellAvatarWidth,
												   kOMECellAvatarHeight)];
	} else {
		if (0){
		//time text label is center
		[timeTextLabel setFrame:CGRectMake(self.tableView.frame.size.width/2-timeTextSize.width/2
										   +kOMECellReplyTimePaddingSides,
										   kOMECellPaddingTop+kOMECellReplyTimePaddingTop,
										   timeTextSize.width,
										   timeTextSize.height)];
		//timebackgroundImage is center
		[timeBackgroundImage setFrame:CGRectMake(self.tableView.frame.size.width/2-timeTextSize.width/2,
												 kOMECellPaddingTop,
												 timeTextSize.width+kOMECellReplyTimePaddingSides*2,
												 timeTextSize.height+kOMECellReplyTimePaddingBottom)];
		[textLabel setFrame:CGRectMake(kOMECellAvatarPaddingSides+kOMECellAvatarWidth
									   +kOMECellAvatarPaddingSides+kOMECellTextPaddingSides*2,
									   kOMECellPaddingTop+kOMECellReplyTimePaddingTop+timeTextSize.height+kOMECellReplyTimePaddingBottom+kOMECellReplyTimeBetweenTextSpan+kOMECellTextPaddingTop,
									   textSize.width,
									   textSize.height)];
		[backgroundImage setFrame:CGRectMake(kOMECellAvatarPaddingSides+kOMECellAvatarWidth
											 +kOMECellAvatarPaddingSides,kOMECellPaddingTop+kOMECellReplyTimePaddingTop+timeTextSize.height+kOMECellReplyTimePaddingBottom+kOMECellReplyTimeBetweenTextSpan+kOMECellTextPaddingTop,//kOMECellPaddingTop,
											 textSize.width + kOMECellTextPaddingSides*2,
											 kOMECellTextPaddingTop+textSize.height+kOMECellReplyTimePaddingBottom)];
		[avatarBackgroundImage setFrame:CGRectMake(kOMECellAvatarPaddingSides,
												   kOMECellPaddingTop+kOMECellReplyTimePaddingTop+timeTextSize.height+kOMECellReplyTimePaddingBottom+kOMECellReplyTimeBetweenTextSpan+kOMECellTextPaddingTop,
												   kOMECellAvatarWidth,
												   kOMECellAvatarHeight)];

		
	}
	}
	

//	{
//		[nameLabel setFrame:CGRectMake(kOMECellAvatarPaddingSides+kOMECellAvatarWidth+kOMECellAvatarPaddingSides+kOMECellTextPaddingSides,
//									   kOMECellPaddingTop+cellHeight-kOMECellPaddingBottom-nameSize .height-kOMECellPaddingTop,
//									   nameSize.width,
//									   nameSize.height)];
		
//		[textLabel setFrame:CGRectMake(kOMECellAvatarPaddingSides+kOMECellAvatarWidth+kOMECellAvatarPaddingSides+kOMECellTextPaddingSides,
//									   kOMECellPaddingTop+kOMECellTextPaddingTop,
//									   textSize.width,
//									   textSize.height)];
//		[backgroundImage setFrame:CGRectMake(kOMECellAvatarPaddingSides+kOMECellAvatarWidth+kOMECellAvatarPaddingSides,
//											 kOMECellPaddingTop,
//											 textWidth + kOMECellTextPaddingSides*2,
//											 cellHeight - kOMECellPaddingTop - kOMECellPaddingBottom)];
//
//	}
	
	cell.selectionStyle = UITableViewCellSelectionStyleNone;
	
	//[self.tableView endUpdates];
	
	//[self.tableView reloadData];
	
	
	return cell;
}


/*
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	
	
	OMEMessageCell *cell = [self.tableView dequeueReusableCellWithIdentifier:kOMEMessageCell];

	if (cell == nil) {
		
		NSLog(@" cell == nil");
		
		cell = [[OMEMessageCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:kOMEMessageCell];
	}
	
    // Configure the cell...
	
	cell.nameLabel.text = message.fromUsername;
	cell.contentLabel.text = @"xx";
	cell.timeLabel.text = @"xx";
	
	
	NSLog(@"cell.timeLabel.text = %@", cell.timeLabel.text);
	 
 

	
	UITableViewCell *cell = [self.tableView dequeueReusableCellWithIdentifier:kOMEMessageCell];
	
	if (cell == nil) {
		
		NSLog(@" cell == nil");
		cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:kOMEMessageCell];
	}
 
	//cell.nameLabel.text = [NSString stringWithFormat:@"%d", ];
	
	//self.message = [self.messages objectAtIndex:indexPath.row];
	
	
//	cell.nameLabel.text = message.fromUsername;
//	cell.contentLabel.text = message.content;
//	cell.timeLabel.text = message.time;
//	cell.avatarImageView.image = [[UIImage imageNamed:@"Icon-Small-40.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(8, 54, 8, 54)];
	
//	NSLog(@"cell.timeLabel.text = %@", cell.timeLabel.text);
	
	cell.detailTextLabel.text = @"test cell";
	
	NSLog(@"cell.detailTextLabel.text = %@", cell.detailTextLabel.text);
	
    return cell;
}
*/

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


/*

- (id)initWithTitle:(NSString *)title message:(NSString *)message delegate:(id)delegate cancelButtonTitle:(NSString *)cancelButtonTitle okButtonTitle:(NSString *)okayButtonTitle
{
	
	//if (self = [super initWithTitle:title message:message delegate:delegate cancelButtonTitle:cancelButtonTitle otherButtonTitles:okayButtonTitle, nil])
	//{
		UITextField *theTextField = [[UITextField alloc] initWithFrame:CGRectMake(12.0, 45.0, 260.0, 25.0)];
		[theTextField setBackgroundColor:[UIColor whiteColor]];
//		[self addSubview:theTextField];
		self.textField = theTextField;
//		[theTextField release];
		CGAffineTransform translate = CGAffineTransformMakeTranslation(0.0, 130.0);
//		[self setTransform:translate];
	//}
	return self;
}

 */


- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField {
	
//	replyField.inputAccessoryView = self.accessoryToolBar;
	
	return YES;
	
}

- (void)show
{
	[replyTextField becomeFirstResponder];
//	[super show];
}
- (NSString *)enteredText
{
	return replyTextField.text;
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
