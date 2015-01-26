//
//  OMEMessageListViewController.h
//  ToPlay
//
//  Created by Ozzie Zhang on 10/5/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <UIKit/UIKit.h>




@class OMEMessageListViewController;

//Ozzie Zhang 2014-10-09 using PFQueryTableViewController replace UITableViewController
@interface OMEMessageListViewController : UITableViewController <UITableViewDataSource, UITableViewDelegate> {
	NSMutableArray *messageArray;

}

//@property (nonatomic, retain) NSMutableArray *messageArray;

@end
