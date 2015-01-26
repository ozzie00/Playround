//
//  OMENewUserViewController.h
//  ToPlay
//
//  Created by Ozzie Zhang on 9/22/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

//Ozzie Zhang 2014-09-23 disable this

#import <UIKit/UIKit.h>

@interface OMENewUserViewController : UIViewController <UITextFieldDelegate>

@property (nonatomic, strong) IBOutlet UIBarButtonItem *doneButton;

@property (nonatomic, strong) IBOutlet UITextField *usernameField;
@property (nonatomic, strong) IBOutlet UITextField *passwordField;
@property (nonatomic, strong) IBOutlet UITextField *passwordAgainField;

- (IBAction)cancel:(id)sender;
- (IBAction)done:(id)sender;
- (IBAction)signup:(id)sender;

@end

/*

#import <UIKit/UIKit.h>

@class OMENewUserViewController;

@protocol OMENewUserViewControllerDelegate <NSObject>

- (void)newUserViewControllerDidSignup:(OMENewUserViewController *)controller;

@end

@interface OMENewUserViewController : UIViewController


@property (nonatomic, weak) id<OMENewUserViewControllerDelegate> delegate;


//@property (nonatomic, strong) IBOutlet UIBarButtonItem *doneButton;
//@property (strong, nonatomic) IBOutlet UIButton *doneButton;

- (IBAction)cancel:(id)sender;

- (IBAction)signup:(id)sender;

@property (strong, nonatomic) IBOutlet UITextField *usernameField;
@property (strong, nonatomic) IBOutlet UITextField *passwordField;

@property (strong, nonatomic) IBOutlet UITextField *passwordAgainField;

@end

*/