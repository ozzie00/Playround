//
//  OMESettingTableViewController.h
//  ToPlay
//
//  Created by Ozzie Zhang on 9/17/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <UIKit/UIKit.h>

@class  OMESettingTableViewController;

@protocol OMESettingTableViewControllerDelegate <NSObject>

- (void)settingTableViewControllerDidLogout:(OMESettingTableViewController *)controller;

@end

@interface OMESettingTableViewController : UITableViewController <UITableViewDataSource, UITableViewDelegate, UIAlertViewDelegate> 

@property (nonatomic, weak) id<OMESettingTableViewControllerDelegate> delegate;

@property (strong, nonatomic) IBOutlet UITableView *tableView;

- (IBAction)logoutPressed:(id)sender;

//- (IBAction)logoutPressed:(id)sender;

@end
