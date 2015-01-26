//
//  OMEPickSportTypeTableViewController.h
//  ToPlay
//
//  Created by Ozzie Zhang on 9/24/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <UIKit/UIKit.h>


@class OMEPickSportTypeTableViewController;

@protocol OMEPickSportTypeTableViewControllerDelegate <NSObject>
- (void)pickSportTypeTableViewController:(OMEPickSportTypeTableViewController *)controller didSelectSportType:(NSString *)sporttype;
@end

@interface OMEPickSportTypeTableViewController : UITableViewController
@property (nonatomic, weak) id <OMEPickSportTypeTableViewControllerDelegate> delegate;
@property (nonatomic, strong) NSString *sporttype;
@end