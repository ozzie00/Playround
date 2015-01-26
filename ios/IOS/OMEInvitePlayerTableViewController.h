//
//  OMEInvitePlayerTableViewController.h
//  ToPlay
//
//  Created by Ozzie Zhang on 9/24/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "OMEPickSportTypeTableViewController.h"

@class OMEInvitePlayerTableViewController;
@class OMEInviteRequirement;


@protocol OMEInvitePlayerTableViewControllerDelegate <NSObject>
@required
- (void)didCreateInviteRequirement:(OMEInviteRequirement *)inviteRequirement;
@end

@interface OMEInvitePlayerTableViewController : UITableViewController <UITableViewDataSource, UITableViewDelegate, UIAlertViewDelegate>

@property (nonatomic, weak) id<OMEInvitePlayerTableViewControllerDelegate> delegate;
@property (strong, nonatomic) IBOutlet UITableView *tableView;
@property (strong, nonatomic) IBOutlet UILabel *sporttypeDetailLabel;

@property (strong, nonatomic) IBOutlet UISlider *playernumber;

@property (strong, nonatomic) IBOutlet UISlider *playerlevel;

@property (strong, nonatomic) IBOutlet UITextField *time;

@property (strong, nonatomic) IBOutlet UITextField *court;

@property (strong, nonatomic) IBOutlet UISegmentedControl *fee;

@property (strong, nonatomic) IBOutlet UITextField *other;



@property (strong, nonatomic) IBOutlet UILabel *playernumberLabel;

@property (strong, nonatomic) IBOutlet UILabel *playerlevelLabel;



- (IBAction)playernumberValueChanged:(id)sender;

- (IBAction)playerlevelValuedChanged:(id)sender;

- (IBAction)feeValuedChanged:(id)sender;

@property (strong, nonatomic) IBOutlet UIButton *invitetoplayButton;

- (IBAction)invitetoplayPressed:(id)sender;

@end
