//
//  OMEChatRoomTableViewController.h
//  ToPlay
//
//  Created by Ozzie Zhang on 8/15/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>

#import "OMEInvite.h"
#import "OMEPlayerViewController.h"

@interface OMEChatRoomTableViewController : PFQueryTableViewController <OMEPlayerViewControllerDelegate>

- (void)highlightCellForInvite:(OMEInvite *)invite;
- (void)unhighlightCellForInvite:(OMEInvite *)invite;

@end
