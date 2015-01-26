//
//  PAWCircleView.h
//  ToPlay
//
//  Created by Ozzie Zhang on 8/15/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <MapKit/MapKit.h>
#import "OMESearchRadius.h"

@interface OMECircleView : MKOverlayPathView

- (id)initWithSearchRadius:(OMESearchRadius *)searchRadius;

@property (nonatomic, readonly, strong) OMESearchRadius *searchRadius;

@end
