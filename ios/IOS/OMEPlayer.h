//
//  OMEPlayer.h
//  ToPlay
//
//  Created by Ozzie Zhang on 9/25/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface OMEPlayer : NSObject

@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *username;
@property (nonatomic, copy) UIImage *avatarImage;
@property (nonatomic, copy) NSMutableArray *following;
@property (nonatomic, copy) NSMutableArray *follower;
@property (nonatomic, copy) NSMutableArray *invite;
@property (nonatomic, assign) NSInteger level;
@property (nonatomic, assign) NSInteger rating;
@property (nonatomic, copy) NSMutableArray *favorite;

@end
