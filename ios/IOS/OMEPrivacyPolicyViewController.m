//
//  OMEPrivacyPolicyViewController.m
//  ToPlay
//
//  Created by Ozzie Zhang on 9/22/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMEPrivacyPolicyViewController.h"

@interface OMEPrivacyPolicyViewController ()

@end

@implementation OMEPrivacyPolicyViewController

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
    // Do any additional setup after loading the view.
	
	NSURL *url = [NSURL URLWithString:@"http://www.ballround.com.s3-website-us-west-2.amazonaws.com/ToPlay_privacy_policy.html"];
    
    NSURLRequest *request = [NSURLRequest requestWithURL: url];
    [self.webView loadRequest : request];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
