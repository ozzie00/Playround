//
//  OMEMessageDetailViewController.h
//  ToPlay
//
//  Created by Ozzie Zhang on 10/5/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <UIKit/UIKit.h>



@class OMEMessageDetailViewController;

@protocol OMEMessageDetailViewControllerDelegate <NSObject>
@required
- (NSString *)getFromUsernameDidSelectMessage: (OMEMessageDetailViewController *)controller;
- (PFUser *)getFromUserDidSelectMessage: (OMEMessageDetailViewController *)controller;
- (NSUInteger)getMessageListCountDidSelectMessage:(OMEMessageDetailViewController *)controller;

@end

//PFQueryTableViewController

//Ozzie Zhang 2014-10-07 change the line code from UITableViewController to PFQueryTableViewController
//UITableViewController
@interface OMEMessageDetailViewController :   PFQueryTableViewController

@property (nonatomic, weak) id<OMEMessageDetailViewControllerDelegate> delegate;

@property (strong, nonatomic) IBOutlet UITextField *replyField;

@property (nonatomic, retain) UITextField *replyTextField;
@property (nonatomic, retain) UITextField *inputTextField;
@property (nonatomic, copy) UIButton *sendButton;

@property (readonly) NSString *enteredText;




// Called and presented when object becomes first responder.  Goes up the responder chain.
//@property (readonly, retain) UIView *inputView NS_AVAILABLE_IOS(3_2);

@property (readonly, retain) UIView *inputAccessoryView NS_AVAILABLE_IOS(3_2);

// If called while object is first responder, reloads inputView and inputAccessoryView.
//- (void)reloadInputViews NS_AVAILABLE_IOS(3_2);

@end


