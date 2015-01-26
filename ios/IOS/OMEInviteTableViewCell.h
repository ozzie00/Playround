//
//  OMEInviteTableViewCell.h
//  ToPlay
//
//  Created by Ozzie Zhang on 8/15/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <UIKit/UIKit.h>

extern CGFloat const OMEInviteTableViewCellLabelsFontSize;

typedef NS_ENUM(uint8_t, OMEInviteTableViewCellStyle)
{
    OMEInviteTableViewCellStyleLeft = 1,
    OMEInviteTableViewCellStyleRight
};

@class OMEInvite;

@interface OMEInviteTableViewCell : UITableViewCell

@property (nonatomic, assign, readonly) OMEInviteTableViewCellStyle inviteTableViewCellStyle;

+ (CGSize)sizeThatFits:(CGSize)boundingSize forPost:(OMEInvite *)post;

- (instancetype)initWithInviteTableViewCellStyle:(OMEInviteTableViewCellStyle)style
                               reuseIdentifier:(NSString *)reuseIdentifier;

- (void)updateFromPost:(OMEInvite *)post;

@end
