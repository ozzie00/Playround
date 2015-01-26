//
//  OMEInviteCreateViewController.h
//  ToPlay
//
//  Created by Ozzie Zhang on 8/15/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "OMEInvitePlayerTableViewController.h"


@interface OMEInviteCreateViewController : UIViewController <OMEInvitePlayerTableViewControllerDelegate >

@property (nonatomic, strong) IBOutlet UITextView *textView;
@property (nonatomic, strong) IBOutlet UILabel *characterCount;
@property (nonatomic, strong) IBOutlet UIBarButtonItem *postButton;

- (IBAction)cancelPost:(id)sender;
- (IBAction)postPost:(id)sender;





@end
