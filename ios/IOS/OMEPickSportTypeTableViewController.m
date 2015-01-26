//
//  OMEPickSportTypeTableViewController.m
//  ToPlay
//
//  Created by Ozzie Zhang on 9/24/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMEPickSportTypeTableViewController.h"

#import "OMEConstant.h"

@interface OMEPickSportTypeTableViewController ()

@end

@implementation OMEPickSportTypeTableViewController {
    NSArray *_sporttypes;
	NSUInteger _selectedIndex;
}


- (void)viewDidLoad {
    [super viewDidLoad];
	
	 _sporttypes = @[kOMEParseInviteSportTypeBadminton,
					kOMEParseInviteSportTypeBasketball,
					kOMEParseInviteSportTypeCycling,
					kOMEParseInviteSportTypeRunning,
					kOMEParseInviteSportTypeSnooker,
					kOMEParseInviteSportTypeTableFootball,
                    kOMEParseInviteSportTypeTabeTennis,
					kOMEParseInviteSportTypeTennis
					 ];
	
	_selectedIndex = [_sporttypes indexOfObject:self.sporttype];
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
#warning Potentially incomplete method implementation.
	// Return the number of sections.
	return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
#warning Incomplete method implementation.
	// Return the number of rows in the section.
	return [_sporttypes count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"SportTypeCell" ];
	cell.textLabel.text = _sporttypes[indexPath.row];
	
	if (indexPath == _selectedIndex) {
		cell.accessoryType = UITableViewCellAccessoryCheckmark;
	} else {
		cell.accessoryType = UITableViewCellAccessoryNone;
	}
	
	// Configure the cell...
	
	return cell;
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
	
	if (_selectedIndex != NSNotFound) {
		UITableViewCell *cell = [tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:_selectedIndex inSection:0]];
		cell.accessoryType = UITableViewCellAccessoryNone;
	}
	
	_selectedIndex = indexPath.row;
	
	UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
	cell.accessoryType = UITableViewCellAccessoryCheckmark;
	
	NSString *sporttype = _sporttypes[indexPath.row];
	[self.delegate pickSportTypeTableViewController:self didSelectSportType:sporttype];
	
}



@end
