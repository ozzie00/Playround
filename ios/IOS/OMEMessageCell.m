//
//  OMEMessageCell.m
//  ToPlay
//
//  Created by Ozzie Zhang on 10/4/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMEMessageCell.h"

@implementation OMEMessageCell

@synthesize avatarImageView;
@synthesize nameLabel;
@synthesize contentLabel;
@synthesize timeLabel;

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
