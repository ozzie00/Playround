//
//  OMEPlayerViewController.m
//  ToPlay
//
//  Created by Ozzie Zhang on 9/17/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <CoreLocation/CoreLocation.h>

#import "OMEInviteCreateViewController.h"
#import "AppDelegate.h"
#import "OMEChatRoomTableViewController.h"
#import "OMECircleView.h"

#import "OMESearchRadius.h"
#import "OMEInvite.h"
#import "OMEMapViewAnnotation.h"
#import "OMEPlayerDetailTableViewController.h"
#import "OMESettingTableViewController.h"
#import "OMESearchInviteViewController.h"
#import "OMEPlayerViewController.h"



CLLocation *globalClickedMapPinInfo;

// private methods and properties
@interface OMEPlayerViewController () 

@property (nonatomic, strong) CLLocationManager *_locationManager;
@property (nonatomic, strong) OMESearchRadius *searchRadius;
@property (nonatomic, strong) OMECircleView *circleView;
@property (nonatomic, strong) NSMutableArray *annotations;
@property (nonatomic, copy) NSString *className;
@property (nonatomic, assign) BOOL mapPinsPlaced;
@property (nonatomic, assign) BOOL mapPannedSinceLocationUpdate;
@property (nonatomic, copy) CLLocation *ClickedMapPinLocation;
@property (nonatomic, copy) NSString *testDelegate;


// posts:
@property (nonatomic, strong) NSMutableArray *allPosts;

- (void)startStandardUpdates;

// CLLocationManagerDelegate methods:
- (void)locationManager:(CLLocationManager *)manager
    didUpdateToLocation:(CLLocation *)newLocation
           fromLocation:(CLLocation *)oldLocation;

- (void)locationManager:(CLLocationManager *)manager
       didFailWithError:(NSError *)error;

- (IBAction)searchButtonSelected:(id)sender;
- (IBAction)postButtonSelected:(id)sender;

- (void)queryForAllPostsNearLocation:(CLLocation *)currentLocation withNearbyDistance:(CLLocationAccuracy)nearbyDistance;
- (void)updatePostsForLocation:(CLLocation *)location withNearbyDistance:(CLLocationAccuracy) filterDistance;

// NSNotification callbacks
- (void)distanceFilterDidChange:(NSNotification *)note;
- (void)locationDidChange:(NSNotification *)note;
- (void)postWasCreated:(NSNotification *)note;



@end

@implementation OMEPlayerViewController

@synthesize mapView;
@synthesize playerDetailTableViewController;
@synthesize _locationManager = locationManager;
@synthesize searchRadius;
@synthesize circleView;
@synthesize annotations;
@synthesize className;
@synthesize allPosts;
@synthesize mapPinsPlaced;
@synthesize mapPannedSinceLocationUpdate;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
		self.className = kOMEParseClassKey;
		annotations = [[NSMutableArray alloc] initWithCapacity:10];
		allPosts = [[NSMutableArray alloc] initWithCapacity:10];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    // Do any additional setup after loading the view
	
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(distanceFilterDidChange:) name:kOMEFilterDistanceChangeNotification object:nil];
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(locationDidChange:) name:kOMELocationChangeNotification object:nil];
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(postWasCreated:) name:kOMEInviteCreatedNotification object:nil];
		// Don't forget to add NSLocationWhenInUseUsageDescription in Info.plist and give it a string
	
	self._locationManager = [[CLLocationManager alloc] init];
	self._locationManager.delegate = self;
	
	// Check for iOS 8. Without this guard the code will crash with "unknown selector" on iOS 7.
	if ([self._locationManager respondsToSelector:@selector(requestWhenInUseAuthorization)]) {
		[self._locationManager requestWhenInUseAuthorization];
	}
	//[self._locationManager startUpdatingLocation];
	self.mapView.showsUserLocation = YES;
	
	//default place MIT
	self.mapView.region = MKCoordinateRegionMake(CLLocationCoordinate2DMake(39.983910f,116.316488f), MKCoordinateSpanMake(0.008516f, 0.021801f));
	self.mapPannedSinceLocationUpdate = NO;
	[self startStandardUpdates];
	
	playerDetailTableViewController = [[OMEPlayerDetailTableViewController alloc] init];
	[playerDetailTableViewController setDelegate:self];
	
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void)requestAlwaysAuthorization
{
	CLAuthorizationStatus status = [CLLocationManager authorizationStatus];
	
	// If the status is denied or only granted for when in use, display an alert
	if (status == kCLAuthorizationStatusAuthorizedWhenInUse || status == kCLAuthorizationStatusDenied) {
	    NSString *title;
		title = (status == kCLAuthorizationStatusDenied) ? @"Location services are off" : @"Background location is not enabled";
		NSString *message = @"To use background location you must turn on 'Always' in the Location Services Settings";
		
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:title
															message:message
														   delegate:self
												  cancelButtonTitle:@"Cancel"
												  otherButtonTitles:@"Settings", nil];
		[alertView show];
	} else if (status == kCLAuthorizationStatusNotDetermined) {
	   // The user has not enabled any location services. Request background authorization.
	   [self._locationManager requestAlwaysAuthorization];
	}
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
	if (buttonIndex == 1) {
		// Send the user to the Settings for this app
		NSURL *settingsURL = [NSURL URLWithString:UIApplicationOpenSettingsURLString];
		[[UIApplication sharedApplication] openURL:settingsURL];
	}
}

#pragma mark - CLLocationManagerDelegate methods and helpers

- (void)startStandardUpdates {
	if (nil == locationManager) {
		locationManager = [[CLLocationManager alloc] init];
	}
	
	locationManager.delegate = self;
	locationManager.desiredAccuracy = kCLLocationAccuracyBest;
	
	// Set a movement threshold for new events.
	//Ozzie Zhang 2014-09-28
	// will change to kCLLocationAccuracyBestForNavigation or kCLLocationAccuracyBest
	// kCLLocationAccuracyNearestTenMeters will be removed
	//kCLLocationAccuracyKilometer
	
	locationManager.distanceFilter = kCLLocationAccuracyNearestTenMeters;
	
	[locationManager startUpdatingLocation];
	
	CLLocation *currentLocation = locationManager.location;
	if (currentLocation) {
		AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
		appDelegate.currentLocation = currentLocation;
	}
}

- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status {
	NSLog(@"%s", __PRETTY_FUNCTION__);
	switch (status) {
		case kCLAuthorizationStatusAuthorized:
			//Ozzie Zhang 2014-09-05
			NSLog(@"kCLAuthorizationStatusAuthorized");
			// Re-enable the post button if it was disabled before.
			self.navigationItem.rightBarButtonItem.enabled = YES;
			[locationManager startUpdatingLocation];
			break;
		case kCLAuthorizationStatusDenied:
			//Ozzie Zhang 2014-09-05
			NSLog(@"kCLAuthorizationStatusDenied");
		{{
			UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Can not access your place.\n\nTo see the new invitation or send an invitation near your place, go to \"Location Services\" menu from \"Settings\" in your mobile device, then find ToPlay in the list, change option from \"OFF\" to \"ON\"." message:nil delegate:self cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
			//Ozzie Zhang 2014-09-05
			[alertView show];
			// Disable the post button.
			self.navigationItem.rightBarButtonItem.enabled = NO;
		}}
			break;
		case kCLAuthorizationStatusNotDetermined:
			NSLog(@"kCLAuthorizationStatusNotDetermined");
			break;
		case kCLAuthorizationStatusRestricted:
			NSLog(@"kCLAuthorizationStatusRestricted");
			break;
		case kCLAuthorizationStatusAuthorizedWhenInUse:
			NSLog(@"kCLAuthorizationStatusAuthorizedWhenInUse");
			break;
	}
}

- (void)locationManager:(CLLocationManager *)manager
    didUpdateToLocation:(CLLocation *)newLocation
           fromLocation:(CLLocation *)oldLocation {
	NSLog(@"%s", __PRETTY_FUNCTION__);
	
	AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
	appDelegate.currentLocation = newLocation;
}

- (void)locationManager:(CLLocationManager *)manager
       didFailWithError:(NSError *)error {
	NSLog(@"%s", __PRETTY_FUNCTION__);
	NSLog(@"Error: %@", [error description]);
	
	if (error.code == kCLErrorDenied) {
		[locationManager stopUpdatingLocation];
	} else if (error.code == kCLErrorLocationUnknown) {
		// todo: retry?
		// set a timer for five seconds to cycle location, and if it fails again, bail and tell the user.
	} else {
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"get ready to retrieve location"
		                                                message:[error description]
		                                               delegate:nil
		                                      cancelButtonTitle:nil
		                                      otherButtonTitles:@"OK", nil];
		[alert show];
	}
}

- (void)locationDidChange:(NSNotification *)note {
	AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
	
	
	// If they panned the map since our last location update, don't recenter it.
	if (!self.mapPannedSinceLocationUpdate) {
		// Set the map's region centered on their new location at 2x filterDistance
		MKCoordinateRegion newRegion = MKCoordinateRegionMakeWithDistance(appDelegate.currentLocation.coordinate, appDelegate.filterDistance * 2.0f, appDelegate.filterDistance * 2.0f);
		
		BOOL oldMapPannedValue = self.mapPannedSinceLocationUpdate;
		[mapView setRegion:newRegion animated:YES];
		self.mapPannedSinceLocationUpdate = oldMapPannedValue;
	} // else do nothing.
	
	// If we haven't drawn the search radius on the map, initialize it.
	if (self.searchRadius == nil) {
		self.searchRadius = [[OMESearchRadius alloc] initWithCoordinate:appDelegate.currentLocation.coordinate radius:appDelegate.filterDistance];
		[mapView addOverlay:self.searchRadius];
	} else {
		self.searchRadius.coordinate = appDelegate.currentLocation.coordinate;
	}
	
	// Update the map with new pins:
	[self queryForAllPostsNearLocation:appDelegate.currentLocation withNearbyDistance:appDelegate.filterDistance];
	// And update the existing pins to reflect any changes in filter distance:
	[self updatePostsForLocation:appDelegate.currentLocation withNearbyDistance:appDelegate.filterDistance];
}

#pragma mark - Fetch map pins


- (void)postWasCreated:(NSNotification *)note {
	AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
	[self queryForAllPostsNearLocation:appDelegate.currentLocation withNearbyDistance:appDelegate.filterDistance];
}



- (void)queryForAllPostsNearLocation:(CLLocation *)currentLocation withNearbyDistance:(CLLocationAccuracy)nearbyDistance {
	
	//NSLog(@"OMEPlayerViewController - queryForAllPostsNearLocation %@", self.className);

   // PFQuery *query = [PFQuery queryWithClassName:self.className];
	PFQuery *query = [PFQuery queryWithClassName:kOMEParseClassKey];
	
	if (currentLocation == nil) {
		NSLog(@"%s can not get current location, please turn on your location service !", __PRETTY_FUNCTION__);
		
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"please turn on your location service !"
															message:nil
														   delegate:self
												  cancelButtonTitle:@"OK"
												  otherButtonTitles:nil, nil];
		[alertView show];
	}

	// If no objects are loaded in memory, we look to the cache first to fill the table
	// and then subsequently do a query against the network.
	if ([self.allPosts count] == 0) {
		query.cachePolicy = kPFCachePolicyCacheThenNetwork;
	}

	// Query for posts sort of kind of near our current location.
	PFGeoPoint *point = [PFGeoPoint geoPointWithLatitude:currentLocation.coordinate.latitude longitude:currentLocation.coordinate.longitude];
	[query whereKey:kOMEParseLocationKey nearGeoPoint:point withinKilometers:kOMEInviteMaximumSearchDistance];
	[query includeKey:kOMEParseUserKey];
	query.limit = kOMEInviteSearchLimit;
	
	
	[query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
		if (error) {
			NSLog(@"location query error!"); // todo why is this ever happening?
		} else {
			// We need to make new post objects from objects,
			// and update allPosts and the map to reflect this new array.
			// But we don't want to remove all annotations from the mapview blindly,
			// so let's do some work to figure out what's new and what needs removing.
	
			// 1. Find genuinely new posts:
			NSMutableArray *newPosts = [[NSMutableArray alloc] initWithCapacity:kOMEInviteSearchLimit];
			// (Cache the objects we make for the search in step 2:)
			NSMutableArray *allNewPosts = [[NSMutableArray alloc] initWithCapacity:kOMEInviteSearchLimit];
			for (PFObject *object in objects) {
				OMEInvite *newPost = [[OMEInvite alloc] initWithPFObject:object];
				[allNewPosts addObject:newPost];
				BOOL found = NO;
				for (OMEInvite *currentPost in allPosts) {
					if ([newPost equalToInvite:currentPost]) {
						found = YES;
					}
				}
				if (!found) {
					[newPosts addObject:newPost];
				}
			}
			// newPosts now contains our new objects.
		
			// 2. Find posts in allPosts that didn't make the cut.
			NSMutableArray *postsToRemove = [[NSMutableArray alloc] initWithCapacity:kOMEInviteSearchLimit];
			for (OMEInvite *currentPost in allPosts) {
				BOOL found = NO;
				// Use our object cache from the first loop to save some work.
				for (OMEInvite *allNewPost in allNewPosts) {
					if ([currentPost equalToInvite:allNewPost]) {
						found = YES;
					}
				}
				if (!found) {
					[postsToRemove addObject:currentPost];
				}
			}
			// postsToRemove has objects that didn't come in with our new results.
		
			// 3. Configure our new posts; these are about to go onto the map.
			for (OMEInvite *newPost in newPosts) {
				CLLocation *objectLocation = [[CLLocation alloc] initWithLatitude:newPost.coordinate.latitude longitude:newPost.coordinate.longitude];
				// if this post is outside the filter distance, don't show the regular callout.
				CLLocationDistance distanceFromCurrent = [currentLocation distanceFromLocation:objectLocation];
				[newPost setTitleAndSubtitleOutsideDistance:( distanceFromCurrent > nearbyDistance ? YES : NO )];
				// Animate all pins after the initial load:
				newPost.animatesDrop = mapPinsPlaced;
			}
			
			// At this point, newAllPosts contains a new list of post objects.
			// We should add everything in newPosts to the map, remove everything in postsToRemove,
			// and add newPosts to allPosts.
			[mapView removeAnnotations:postsToRemove];
			[mapView addAnnotations:newPosts];
			[allPosts addObjectsFromArray:newPosts];
			[allPosts removeObjectsInArray:postsToRemove];
			
			self.mapPinsPlaced = YES;
		}
	 
	}];
 
 
}



// When we update the search filter distance, we need to update our pins' titles to match.
- (void)updatePostsForLocation:(CLLocation *)currentLocation withNearbyDistance:(CLLocationAccuracy) nearbyDistance {
	for (OMEInvite *invite in allPosts) {
		CLLocation *objectLocation = [[CLLocation alloc] initWithLatitude:invite.coordinate.latitude longitude:invite.coordinate.longitude];
		// if this post is outside the filter distance, don't show the regular callout.
		CLLocationDistance distanceFromCurrent = [currentLocation distanceFromLocation:objectLocation];
		if (distanceFromCurrent > nearbyDistance) { // Outside search radius
			[invite setTitleAndSubtitleOutsideDistance:YES];
			[mapView viewForAnnotation:invite];
			//Ozzie Zhang 2014-09-05 enable and change MKAnnotationView * to MKPinAnnotationView.
			[(MKPinAnnotationView *) [mapView viewForAnnotation:invite] setPinColor:invite.pinColor];
		} else {
			[invite setTitleAndSubtitleOutsideDistance:NO]; // Inside search radius
			[mapView viewForAnnotation:invite];
			//Ozzie Zhang 2014-09-05 enable and change MKAnnotationView * to MKPinAnnotationView.
			[(MKPinAnnotationView *) [mapView viewForAnnotation:invite] setPinColor:invite.pinColor];
		}
	}
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

#pragma mark - UINavigationBar-based actions
/*
- (IBAction)searchButtonSelected:(id)sender {
	OMESearchViewController *searchViewController = [[OMESearchViewController alloc] initWithNibName:nil bundle:nil];
	searchViewController.modalTransitionStyle =  UIModalTransitionStyleCrossDissolve;//  UIModalTransitionStyleFlipHorizontal; //ozzie zhang 2014-08-31
	[self.navigationController presentViewController:searchViewController animated:YES completion:nil];
}

- (IBAction)postButtonSelected:(id)sender {
	PAWWallPostCreateViewController *createPostViewController = [[PAWWallPostCreateViewController alloc] initWithNibName:nil bundle:nil];
	[self.navigationController presentViewController:createPostViewController animated:YES completion:nil];
}
*/




- (void)viewWillAppear:(BOOL)animated {
	[locationManager startUpdatingLocation];
	[super viewWillAppear:animated];
}

- (void)viewDidDisappear:(BOOL)animated {
	[locationManager stopUpdatingLocation];
	[super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void)dealloc {
	[locationManager stopUpdatingLocation];
	
	[[NSNotificationCenter defaultCenter] removeObserver:self name:kOMEFilterDistanceChangeNotification object:nil];
	[[NSNotificationCenter defaultCenter] removeObserver:self name:kOMELocationChangeNotification object:nil];
	[[NSNotificationCenter defaultCenter] removeObserver:self name:kOMEInviteCreatedNotification object:nil];
	
	self.mapPinsPlaced = NO; // reset this for the next time we show the map.
}

#pragma mark - NSNotificationCenter notification handlers

- (void)distanceFilterDidChange:(NSNotification *)note {
	CLLocationAccuracy filterDistance = [[[note userInfo] objectForKey:kOMEFilterDistanceKey] doubleValue];
	AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
	
	if (self.searchRadius == nil) {
		self.searchRadius = [[OMESearchRadius alloc] initWithCoordinate:appDelegate.currentLocation.coordinate radius:appDelegate.filterDistance];
		[mapView addOverlay:self.searchRadius];
	} else {
		self.searchRadius.radius = appDelegate.filterDistance;
	}
	
	// Update our pins for the new filter distance:
	[self updatePostsForLocation:appDelegate.currentLocation withNearbyDistance:filterDistance];
	
	// If they panned the map since our last location update, don't recenter it.
	if (!self.mapPannedSinceLocationUpdate) {
		// Set the map's region centered on their location at 2x filterDistance
		MKCoordinateRegion newRegion = MKCoordinateRegionMakeWithDistance(appDelegate.currentLocation.coordinate, appDelegate.filterDistance * 2.0f, appDelegate.filterDistance * 2.0f);
		
		[mapView setRegion:newRegion animated:YES];
		self.mapPannedSinceLocationUpdate = NO;
	} else {
		// Just zoom to the new search radius (or maybe don't even do that?)
		MKCoordinateRegion currentRegion = mapView.region;
		MKCoordinateRegion newRegion = MKCoordinateRegionMakeWithDistance(currentRegion.center, appDelegate.filterDistance * 2.0f, appDelegate.filterDistance * 2.0f);
		
		BOOL oldMapPannedValue = self.mapPannedSinceLocationUpdate;
		[mapView setRegion:newRegion animated:YES];
		self.mapPannedSinceLocationUpdate = oldMapPannedValue;
	}
}

#pragma mark - UINavigationBar-based actions

- (IBAction)searchButtonSelected:(id)sender {
	OMESearchInviteViewController *searchInviteViewController = [[OMESearchInviteViewController alloc] initWithNibName:nil bundle:nil];
	searchInviteViewController.modalTransitionStyle =  UIModalTransitionStyleCrossDissolve;//  UIModalTransitionStyleFlipHorizontal; //ozzie zhang 2014-08-31
	[self.navigationController presentViewController:searchInviteViewController animated:YES completion:nil];
}

- (IBAction)postButtonSelected:(id)sender {
	OMEInviteCreateViewController *createPostViewController = [[OMEInviteCreateViewController alloc] initWithNibName:nil bundle:nil];
	[self.navigationController presentViewController:createPostViewController animated:YES completion:nil];
}


#pragma mark - CLLocationManagerDelegate methods and helpers




#pragma mark - MKMapViewDelegate methods

- (MKOverlayView *)mapView:(MKMapView *)mapView viewForOverlay:(id <MKOverlay>)overlay {
	MKOverlayView *result = nil;
	float version = [[[UIDevice currentDevice] systemVersion] floatValue];
	
	// Only display the search radius in iOS 5.1+
	if (version >= 5.1f && [overlay isKindOfClass:[OMESearchRadius class]]) {
		result = [[OMECircleView alloc] initWithSearchRadius:(OMESearchRadius *)overlay];
		[(MKOverlayPathView *)result setFillColor:[[UIColor darkGrayColor] colorWithAlphaComponent:0.2f]];
		[(MKOverlayPathView *)result setStrokeColor:[[UIColor darkGrayColor] colorWithAlphaComponent:0.7f]];
		[(MKOverlayPathView *)result setLineWidth:2.0];
	}
	return result;
}


#pragma mark -
#pragma mark set OMEPlayerDetailTableViewController as right callout view
// user tapped the disclosure button in the callout
//
- (void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)view calloutAccessoryControlTapped:(UIControl *)control {
	
	// here we illustrate how to detect which annotation type was clicked on for its callout
	id <MKAnnotation> annotation = [view annotation];
	if ([annotation isKindOfClass:[OMEMapViewAnnotation class]])
	{
		NSLog(@"clicked player Detail Information");
	}
	
	//Ozzie Zhang 2014-09-29 add get pin location for query detail player invite requirement
	globalClickedMapPinInfo = [[CLLocation alloc] initWithLatitude:annotation.coordinate.latitude longitude:annotation.coordinate.longitude];

	//Ozzie Zhang 2014-09-09 debug
	// UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Fourth annotation" message:@"Message" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
    // [alertView show];
	
	//Ozzie Zhang 2014-09-08 debug this for player detail information
	//NSAssert([self.parentViewController isKindOfClass:[PAWWallViewController class]], @"Not PAWWallViewController");
	
	//Ozzie Zhang 2014-09-09 work,but 2014-09-17 for debug Ozzie Zhang disable playerDetailViewController
	//Ozzie Zhang 2014-09-18 add init playerDetailViewController
	playerDetailTableViewController = [(OMEPlayerDetailTableViewController *)[OMEPlayerDetailTableViewController alloc] init];
    [self.navigationController pushViewController:self.playerDetailTableViewController animated:YES];
}

#pragma mark -
#pragma mark get current map pin detail pin for OMEPlayerDetailTableViewController delegate
- (CLLocation *)getClickedMapPinDetailsInfo{
	
	return globalClickedMapPinInfo;
	
}


- (MKAnnotationView *)mapView:(MKMapView *)aMapView viewForAnnotation:(id<MKAnnotation>)annotation {
	// Let the system handle user location annotations.
	if ([annotation isKindOfClass:[MKUserLocation class]]) {
		return nil;
	}
	
	static NSString *pinIdentifier = @"CustomPinAnnotation";
	
	// Handle any custom annotations.
	if ([annotation isKindOfClass:[OMEInvite class]])
	{
		// Try to dequeue an existing pin view first.
		MKPinAnnotationView *pinView = (MKPinAnnotationView*)[aMapView dequeueReusableAnnotationViewWithIdentifier:pinIdentifier];
		
		if (!pinView)
		{
			// If an existing pin view was not available, create one.
			pinView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation
			                                          reuseIdentifier:pinIdentifier];
		}
		else {
			pinView.annotation = annotation;
		}
		
		pinView.enabled = YES;
		pinView.image = [UIImage imageNamed:@"pin.png"];
		
		//Ozzie Zhang 2014-09-05
		//pinView.pinColor = [(PAWPost *)annotation pinColor];
		//pinView.pinColor = MKPinAnnotationColorPurple;
		
		//pinView.animatesDrop = [((PAWPost *)annotation) animatesDrop];
		pinView.animatesDrop = YES;
		pinView.canShowCallout = YES;
		
		// add a detail disclosure button to the callout which will open a new view controller page
		//
		// note: when the detail disclosure button is tapped, we respond to it via:
		//       calloutAccessoryControlTapped delegate method
		//
		// by using "calloutAccessoryControlTapped", it's a convenient way to find out which annotation was tapped
		//
		
		
		
		
		UIView *annotationView = [[UIView alloc] initWithFrame:CGRectMake(0, 0,70,90)];
		annotationView.backgroundColor = [UIColor clearColor];
		UIImageView *calloutView = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"playerMapIcon.png"]];
		
	    [annotationView addSubview:calloutView];
		
		pinView.leftCalloutAccessoryView = annotationView;
		
		UIButton *rightButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
		[rightButton addTarget:nil action:nil forControlEvents:UIControlEventTouchUpInside];
		pinView.rightCalloutAccessoryView = rightButton;
		
		
		return pinView;
	}
	
	return nil;
}



- (void)mapView:(MKMapView *)mapView didSelectAnnotationView:(MKAnnotationView *)view {
	id<MKAnnotation> annotation = [view annotation];
	if ([annotation isKindOfClass:[OMEInvite class]]) {
		OMEInvite *post = [view annotation];
		//Ozzie Zhang 2014-09-05 disable calling PAWWallPostsTableViewController
		//[wallPostsTableViewController highlightCellForPost:post];
	} else if ([annotation isKindOfClass:[MKUserLocation class]]) {
		// Center the map on the user's current location:
		AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
		MKCoordinateRegion newRegion = MKCoordinateRegionMakeWithDistance(appDelegate.currentLocation.coordinate, appDelegate.filterDistance * 2, appDelegate.filterDistance * 2);
		
		[self.mapView setRegion:newRegion animated:YES];
		self.mapPannedSinceLocationUpdate = NO;
	}
}

- (void)mapView:(MKMapView *)mapView didDeselectAnnotationView:(MKAnnotationView *)view {
	id<MKAnnotation> annotation = [view annotation];
	if ([annotation isKindOfClass:[OMEInvite class]]) {
		OMEInvite *post = [view annotation];
		//Ozzie Zhang 2014-09-05 disable calling PAWWallPostsTableViewController
		//[wallPostsTableViewController unhighlightCellForPost:post];
	}
}

- (void)mapView:(MKMapView *)mapView regionWillChangeAnimated:(BOOL)animated {
	self.mapPannedSinceLocationUpdate = YES;
}

#pragma mark - Fetch map pins


// When we update the search filter distance, we need to update our pins' titles to match.

#pragma mark -
#pragma mark - back from OMESearchInviteViewController or OMEInvitePlayerTableViewController scene
- (void)prepareForSegue:(UIStoryboardSegue *)segue
				 sender:(id)sender {
	if ([segue.identifier isEqualToString:@"search"]) {
		OMESearchInviteViewController *searchInviteViewController = segue.destinationViewController;
		//searchInviteViewController.delegate = (id)self;
	}

	
	if ([segue.identifier isEqualToString:@"invite"]) {
		OMEInvitePlayerTableViewController *invitePlayerTableViewController = segue.destinationViewController;
		invitePlayerTableViewController.delegate = (id)self;
	}
	
	
	
	
}



@end
