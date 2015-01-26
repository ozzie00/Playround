//
//  OMEWelcomeViewController.h
//  ToPlay
//
//  Created by Ozzie Zhang on 9/22/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMEWelcomeViewController.h"

#import "OMEPlayerViewController.h"
#import "OMELoginViewController.h"
#import "OMENewUserViewController.h"
#import "OMEMainViewController.h"

@implementation OMEWelcomeViewController

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - Transition methods

- (IBAction)loginButtonSelected:(id)sender {
	OMELoginViewController *loginViewController = [[OMELoginViewController alloc] initWithNibName:nil bundle:nil];
	[self.navigationController presentViewController:loginViewController animated:YES completion:^{}];
}

- (IBAction)createButtonSelected:(id)sender {
	OMENewUserViewController *newUserViewController = [[OMENewUserViewController alloc] initWithNibName:nil bundle:nil];
	[self.navigationController presentViewController:newUserViewController animated:YES completion:^{}];
}

- (IBAction)guestButtonSelected:(id)sender {
	 OMEMainViewController *mainViewController = [[OMEMainViewController alloc] initWithNibName:nil bundle:nil];
	[self.navigationController presentViewController:mainViewController animated:YES completion:^{}];
	
	NSLog(@"Guest go to mainViewController ");
	
	//OMELoginViewController *viewController = [[OMELoginViewController alloc] initWithNibName:nil bundle:nil];
//	viewController.delegate = self;
	//[self.navigationController setViewControllers:@[ viewController ] animated:NO];
}


#pragma mark - View lifecycle

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}


@end
