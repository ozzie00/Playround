//
//  OMEPlayerViewController.h
//  ToPlay
//
//  Created by Ozzie Zhang on 9/17/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <Parse/Parse.h>

#import "OMEInvite.h"
#import "OMEPlayerDetailTableViewController.h"
#import "OMESettingTableViewController.h"


@class OMEPlayerViewController;

@protocol OMEPlayerViewControllerDelegate <NSObject>
@required
- (void)playerViewControllerWantsToPresentSettings:(OMEPlayerViewController *)controller;
- (void)highlightCellForPost:(OMEInvite *)post;
- (void)unhighlightCellForPost:(OMEInvite *)post;
@end

@interface OMEPlayerViewController : UIViewController <MKMapViewDelegate, CLLocationManagerDelegate,OMEPlayerDetailTableViewControllerDelegate>

@property (nonatomic, weak) id<OMEPlayerViewControllerDelegate> delegate;
@property (strong, nonatomic) IBOutlet MKMapView *mapView;
@property (strong, nonatomic)  OMEPlayerDetailTableViewController *playerDetailTableViewController;

@end

/*
@protocol OMEPlayerViewControllerHighlight <NSObject>

- (void)highlightCellForPost:(OMEInvite *)post;
- (void)unhighlightCellForPost:(OMEInvite *)post;

@end

*/