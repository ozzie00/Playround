//
//  OMEMainViewController.m
//  ToPlay
//
//  Created by Ozzie Zhang on 9/15/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMEConstant.h"
#import "OMEMainViewController.h"

#import "Reachability.h"

@interface OMEMainViewController () {
	Reachability *internetReachable;
}

@end

@implementation OMEMainViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
	
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(reachabilityChanged:)
												 name:kReachabilityChangedNotification
											   object:nil];
	
	[self checkInternetConnection];
}


#pragma mark - Check internet connection

// Checks if we have an internet connection or not
- (void)checkInternetConnection {
	internetReachable = [Reachability reachabilityWithHostname:kOMEParseInternetReachableWebSite];
	
	// Internet is reachable
	internetReachable.reachableBlock = ^(Reachability*reach)
	{
		// Update the UI on the main thread
		dispatch_async(dispatch_get_main_queue(), ^{
			NSLog(@"OMEMain now has internet connection!");
		});
	};
	
	// Internet is not reachable
	internetReachable.unreachableBlock = ^(Reachability*reach)
	{
		// Update the UI on the main thread
		dispatch_async(dispatch_get_main_queue(), ^{
			NSLog(@"OMEMain now no internet connection :(");
		});
	};
	
	[internetReachable startNotifier];
}

-(void)reachabilityChanged:(NSNotification*)note
{
	Reachability * reach = [note object];
	
	if(![reach isReachable]) {
	
		
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:kOMEParseInternetUnreachable
															message:nil
														   delegate:nil
												  cancelButtonTitle:nil
												  otherButtonTitles:kOMEParseOK, nil];
		[alertView show];

	}
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
