//
//  AppDelegate.m
//  ToPlay
//
//  Created by Ozzie Zhang on 8/5/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <Parse/Parse.h>
#import <FacebookSDK/FacebookSDK.h>
#import "iVersion.h"

#import "AppDelegate.h"
#import "OMEConstant.h"


#import "OMEWelcomeViewController.h"
#import "OMELoginViewController.h"
#import "OMESettingTableViewController.h"
#import "OMECommunityViewController.h"
#import "OMEPlayerViewController.h"
#import "OMESearchInviteViewController.h"
#import "OMEInviteRequirement.h"
#import "OMEInvitePlayerTableViewController.h"
#import "OMEMainViewController.h"


@interface AppDelegate ()
<OMELoginViewControllerDelegate,OMEPlayerViewControllerDelegate, OMESettingTableViewControllerDelegate,OMEInvitePlayerTableViewControllerDelegate,OMEMainViewControllerDelegate>

@end

@implementation AppDelegate

@synthesize window = _window;
//@synthesize viewController = _viewController;
@synthesize filterDistance;
@synthesize currentLocation;


#pragma mark - UIApplicationDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
	  
	//Ozzie Zhang 2014-09-15 disable this window to use OMEMainViewController to manage all view
    //self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    // Override point for customization after application launch.
	
	// ****************************************************************************
    // Parse initialization
    // [Parse setApplicationId:@"APPLICATION_ID" clientKey:@"CLIENT_KEY"];
	// ****************************************************************************
	[Parse setApplicationId:@"E7Vla8ducchhXCBxbjTi1GfiqKJsHKZKa1TdGIke" clientKey:@"B5cNapwaiststrI47QfZlKAxKBUxQggKOu7wfMtW"];
	
	// Grab values from NSUserDefaults:
	NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
	
	// Set the global tint on the navigation bar
	//[[UINavigationBar appearance] setTintColor:[UIColor colorWithRed:28.0f/255.0f green:161.0f/255.0f blue:85.0f/255.0f alpha:1.0f]];
	[[UINavigationBar appearance] setTintColor:[UIColor colorWithRed:48.0f/255.0f green:19.0f/255.0f blue:5.0f/255.0f alpha:1.0f]];
    //[[UINavigationBar appearance] setTintColor:[UIColor colorWithRed:200.0f/255.0f green:83.0f/255.0f blue:70.0f/255.0f alpha:1.0f]];
	//[[UINavigationBar appearance] setBackgroundImage:[UIImage imageNamed:@"bar.png"] forBarMetrics:UIBarMetricsDefault];
	
	//Set the global tint on the tab bar
//	[[UITabBar appearance] setTintColor:[UIColor colorWithRed:48.0f/255.0f green:19.0f/255.0f blue:5.0f/255.0f  alpha:2.0f]]; //ozzie zhang
	//[[UITabBar appearance] setBackgroundImage:[UIImage imageNamed:@"bar.png"]];
	
	// Desired search radius:
	if ([userDefaults doubleForKey:kOMEdefaultsFilterDistanceKey]) {
		// use the ivar instead of self.accuracy to avoid an unnecessary write to NAND on launch.
		filterDistance = [userDefaults doubleForKey:kOMEdefaultsFilterDistanceKey];
	} else {
		// if we have no accuracy in defaults, set it to 1000 feet.
		self.filterDistance = kOMEdefaultsSearchRadius * kOMEFeetToMeters;
	}

	
	//Ozzie Zhang 2014-09-15 setting tab bar background
	
	//[[UITabBar appearance] setTintColor:[UIColor colorWithRed:28.0f/255.0f green:161.0f/255.0f blue:85.0f/255.0f  alpha:1.0f]];
    [[UITabBar appearance] setTintColor:[UIColor colorWithRed:48.0f/255.0f green:19.0f/255.0f blue:5.0f/255.0f  alpha:1.0f]];
 	
	//Ozzie Zhang 2014-09-15 add tab bar controller manage all four view controller (player, message, community,me)
	//self.rootTabBarController = [[UITabBarController alloc] init];
	
    
//	NSArray* allControllers = [NSArray arrayWithObjects:   meTableViewController, nil];
	
//	self.rootTabBarController.viewControllers = allControllers;
	
	
	//Ozzie Zhang 2014-09-09 embedded navigation controller to tab bar controller, using tab bar controller control the following view : Contacts, Me, Community
	UINavigationController *navController = nil;
	
	//PAWWallViewController *wallViewController = [[PAWWallViewController alloc] initWithNibName:nil bundle:nil]; //ozzie zhang 2014-09-03 change from initWithNibName:nil
	//navController = [[UINavigationController alloc] initWithRootViewController:wallViewController];
	//navController.navigationBarHidden = NO;
	
	
	//self.rootTabBarController.viewControllers = allControllers;
	//self.viewController = self.rootTabBarController.viewControllers;
	//self.window.rootViewController = self.rootTabBarController.viewControllers;

	self.navigationController = [[UINavigationController alloc] initWithRootViewController:[[UIViewController alloc] init]];
	
	if ([PFUser currentUser]) {
		
		//[self presentPlayerViewControllerAnimated:NO];
		[self presentOMEMainViewControllerAnimated:NO];
		
		//Ozzie Zhang 2014-09-17 disable for OMEPlayerViewController
		// Skip straight to the main view.
		//PAWWallViewController *wallViewController = [[PAWWallViewController alloc] initWithNibName:nil bundle:nil]; //ozzie zhang 2014-09-03 change from initWithNibName:nil
		//navController = [[UINavigationController alloc] initWithRootViewController:wallViewController];
		//navController.navigationBarHidden = NO;
		
	//	OMEPlayerViewController *playerViewController = [[OMEPlayerViewController alloc] initWithNibName:nil bundle:nil]; //ozzie zhang 2014-09-03 change from initWithNibName:nil
	//    navController = [[UINavigationController alloc] initWithRootViewController:playerViewController];
	//    navController.navigationBarHidden = NO;
		
		//self.viewController = navController;
		//self.window.rootViewController = self.viewController;
	} else {
		// Go to the welcome screen and have them log in or create an account.
		//[self presentWelcomeViewController];
		
		[self presentLoginViewController];

	}
	
	
//	[self.window addSubview:_rootTabBarController.view];
	
	[PFAnalytics trackAppOpenedWithLaunchOptions:launchOptions];
	
    [self.window makeKeyAndVisible];
    return YES;
}

- (void)applicationWillTerminate:(UIApplication *)application {
	[[NSUserDefaults standardUserDefaults] synchronize];
}

#pragma mark -
#pragma mark facebook
/*
- (BOOL)application:(UIApplication *)application
			openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
		 annotation:(id)annotation {
	// attempt to extract a token from the url
	return [FBAppCall handleOpenURL:url sourceApplication:sourceApplication];
}
*/

- (BOOL)application:(UIApplication *)application
			openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
		 annotation:(id)annotation {
	
	// Call FBAppCall's handleOpenURL:sourceApplication to handle Facebook app responses
	BOOL wasHandled = [FBAppCall handleOpenURL:url sourceApplication:sourceApplication];
	
	// You can add your app-specific url handling code here if needed
	
	return wasHandled;
}


#pragma mark - check update version
+ (void)initialize
{
	//set the bundle ID. normally you wouldn't need to do this
	//as it is picked up automatically from your Info.plist file
	//but we want to test with an app that's actually on the store
	[iVersion sharedInstance].applicationBundleID = @"com.oneme.toplay";
	[iVersion sharedInstance].appStoreID = 914733757;
	
	//configure iVersion. These paths are optional - if you don't set
	//them, iVersion will just get the release notes from iTunes directly (if your app is on the store)
	//[iVersion sharedInstance].remoteVersionsPlistURL = @"http://charcoaldesign.co.uk/iVersion/versions.plist";
	//[iVersion sharedInstance].localVersionsPlistPath = @"versions.plist";
}


#pragma mark - AppDelegate

- (void)setFilterDistance:(CLLocationAccuracy)aFilterDistance {
	filterDistance = aFilterDistance;

	NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
	[userDefaults setDouble:filterDistance forKey:kOMEdefaultsFilterDistanceKey];
	[userDefaults synchronize];

	// Notify the app of the filterDistance change:
	NSDictionary *userInfo = [NSDictionary dictionaryWithObject:[NSNumber numberWithDouble:filterDistance] forKey:kOMEFilterDistanceKey];
	dispatch_async(dispatch_get_main_queue(), ^{
		[[NSNotificationCenter defaultCenter] postNotificationName:kOMEFilterDistanceChangeNotification object:nil userInfo:userInfo];
	});
}

- (void)setCurrentLocation:(CLLocation *)aCurrentLocation {
	currentLocation = aCurrentLocation;

	// Notify the app of the location change:
	NSDictionary *userInfo = [NSDictionary dictionaryWithObject:currentLocation forKey:kOMELocationKey];
	dispatch_async(dispatch_get_main_queue(), ^{
		[[NSNotificationCenter defaultCenter] postNotificationName:kOMELocationChangeNotification object:nil userInfo:userInfo];
	});
}


- (void)didCreateInviteRequirement:(OMEInviteRequirement *)inviteRequirement {
	
	NSLog(@"Delegate OMEInvitePlayerTableViewController implement");
	NSLog(@"AppDelegate.m get ready to didCreateInviteRequirement() !");
	

	
	
	// Data prep:
	//AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
	//CLLocationCoordinate2D currentCoordinate = appDelegate.currentLocation.coordinate;
	//PFGeoPoint *currentPoint = [PFGeoPoint geoPointWithLatitude:currentCoordinate.latitude longitude:currentCoordinate.longitude];
	PFUser *user = [PFUser currentUser];
	
	
	
	// Stitch together a inviteObject and send this async to Parse
	PFObject *inviteObject = [PFObject objectWithClassName:kOMEParseClassKey];
	//[inviteObject setObject:textView.text forKey:kOMEParseTextKey];
	[inviteObject setObject:user forKey:kOMEParseUserKey];
	//[inviteObject setObject:currentPoint forKey:kOMEParseLocationKey];
	
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
			NSLog(@"Successfully saved!");
			NSLog(@"%@", inviteObject);
			dispatch_async(dispatch_get_main_queue(), ^{
				[[NSNotificationCenter defaultCenter] postNotificationName:kOMEInviteCreatedNotification object:nil];
			});
		} else {
			NSLog(@"Failed to save.");
		}
	}];

	
}


- (void)presentWelcomeViewController {
	// Go to the welcome screen and have them log in or create an account.
	OMEWelcomeViewController *welcomeViewController = [[OMEWelcomeViewController alloc] initWithNibName:nil bundle:nil];
	//welcomeViewController.title = @"Welcome";
	
	//UINavigationController *navController = [[UINavigationController alloc] initWithRootViewController:welcomeViewController];
	//navController.navigationBarHidden = YES;

	
	
//	self.viewController = navController;
//	self.window.rootViewController = self.viewController;
}


- (void)presentLoginViewController {
	// Go to login screen and have them log in or create an account.
	OMELoginViewController *loginViewController = [[OMELoginViewController alloc] initWithNibName:nil bundle:nil];
	loginViewController.delegate = self;
	[self.navigationController setViewControllers:@[ loginViewController ] animated:NO];
}

#pragma mark Delegate

- (void)loginViewControllerDidLogin:(OMELoginViewController *)controller {
	NSLog(@"AppDelegate loginViewControlllerDidLogin !");
	[self presentOMEMainViewControllerAnimated:NO ];
	
}

- (void)presentOMEMainViewControllerAnimated:(BOOL)animated {
	
	OMEMainViewController *mainViewController = [[OMEMainViewController alloc] initWithNibName:nil bundle:nil];
	mainViewController.delegate = self;
	[self.navigationController setViewControllers:@[mainViewController] animated:animated];
}



#pragma mark -
#pragma mark OMEPlayerViewController


- (void)presentPlayerViewControllerAnimated:(BOOL)animated {
	OMEPlayerViewController *playerViewController = [[OMEPlayerViewController alloc] initWithNibName:nil bundle:nil];
	playerViewController.delegate = self;
	[self.navigationController setViewControllers:@[playerViewController] animated:animated];
}




- (void)mainViewControllerDidPresent:(OMEMainViewController *)controller {
	
}

#pragma mark Delegate

- (void)playerViewControllerWantsToPresentSettings:(OMEPlayerViewController *)controller {
	[self presentSearchInviteViewController];
}

#pragma mark -
#pragma mark SettingsViewController

- (void)presentSearchInviteViewController {
	OMESearchInviteViewController *searchInviteViewController = [[OMESearchInviteViewController alloc] initWithNibName:nil bundle:nil];
//	searchInviteViewController.delegate = self;
//	settingsViewController.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
	[self.navigationController presentViewController:searchInviteViewController animated:YES completion:nil];
}

#pragma mark Delegate

- (void)settingTableViewControllerDidLogout:(OMESettingTableViewController *)controller {
	
	NSLog(@"settingTableViewControllerDidLogout is called");
	[controller dismissViewControllerAnimated:YES completion:nil];
	[self presentLoginViewController];
	//[self presentWelcomeViewController];
}


@end
