//
//  OMELoginViewController.h
//  ToPlay
//
//  Created by Ozzie Zhang on 9/22/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <UIKit/UIKit.h>

@class OMELoginViewController;

@protocol OMELoginViewControllerDelegate <NSObject>

- (void)loginViewControllerDidLogin:(OMELoginViewController *)controller;

@end


@interface OMELoginViewController : UIViewController <UITextFieldDelegate>

@property (nonatomic, weak) id<OMELoginViewControllerDelegate> delegate;
@property (strong, nonatomic) IBOutlet UITextField *usernameField;
@property (strong, nonatomic) IBOutlet UITextField *passwordField;


- (IBAction)cancel:(id)sender;
- (IBAction)login:(id)sender;

- (IBAction)createButtonSelected:(id)sender;


@end
