//
//  OMEMessageCell.h
//  ToPlay
//
//  Created by Ozzie Zhang on 10/4/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface OMEMessageCell : UITableViewCell


@property (nonatomic, weak) IBOutlet UIImageView *avatarImageView;

@property (nonatomic, weak) IBOutlet UILabel *nameLabel;

@property (nonatomic, weak) IBOutlet UILabel *contentLabel;

@property (nonatomic, weak) IBOutlet UILabel *timeLabel;




@end
