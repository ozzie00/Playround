//
//  OMEPlayerDetailTableViewController.h
//  ToPlay
//
//  Created by Ozzie Zhang on 9/24/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

// this class name is not suitable for this class, because this class mainly display the dedicated invite content corresponding to the invite requirement of OMEInvitePlayerTableViewController

#import <UIKit/UIKit.h>

@class OMEPlayerDetailTableViewController;
@class OMEPlayerViewController;
@class OMEInviteRequirement;

@protocol OMEPlayerDetailTableViewControllerDelegate <NSObject>
@required

- (CLLocation *)getClickedMapPinDetailsInfo;

@end

@interface OMEPlayerDetailTableViewController : UITableViewController <UITableViewDataSource, UITableViewDelegate, UIAlertViewDelegate> {
	
}

- (id) init;
- (void) dealloc;
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView;
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section;
- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section;
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath;

@property (strong, nonatomic) IBOutlet UITableView *tableView;

@property (weak, nonatomic) id<OMEPlayerDetailTableViewControllerDelegate> delegate;

- (IBAction)joinplayButtonPressed:(id)sender;


@end
