//
//  OMEMainViewController.h
//  ToPlay
//
//  Created by Ozzie Zhang on 9/15/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <UIKit/UIKit.h>


@class OMEMainViewController;

@protocol OMEMainViewControllerDelegate <NSObject>

- (void)mainViewControllerDidPresent:(OMEMainViewController *)controller;


@end

@interface OMEMainViewController : UITabBarController

@property (nonatomic, weak) id<OMEMainViewControllerDelegate> delegate;
@end
