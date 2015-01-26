//
//  OMEChatViewController.h
//  ToPlay
//
//  Created by Ozzie Zhang on 9/18/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <Parse/Parse.h>

#import "OMEInvite.h"
#import "OMEPlayerDetailTableViewController.h"
#import "OMESettingTableViewController.h"


@interface OMEChatViewController : UIViewController <MKMapViewDelegate, CLLocationManagerDelegate>

//@property (nonatomic, strong) IBOutlet MKMapView *mapView;
@property (strong, nonatomic) IBOutlet MKMapView *mapView;


@property (strong, nonatomic) IBOutlet OMEPlayerDetailTableViewController *playerDetailTableViewController;


@property (strong, nonatomic) IBOutlet UITabBarItem *meTabBarItem;


@property (strong, nonatomic) UITabBarController *tabBarController;

@end

@protocol OMEChatViewControllerHighlight <NSObject>
@required
- (void)highlightCellForPost:(OMEInvite *)post;
- (void)unhighlightCellForPost:(OMEInvite *)post;

@end