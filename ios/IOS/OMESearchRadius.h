//
//  OMESearchRadius.h
//  ToPlay
//
//  Created by Ozzie Zhang on 8/15/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//


#import <MapKit/MapKit.h>

@interface OMESearchRadius : NSObject <MKOverlay>

@property (nonatomic, assign) CLLocationCoordinate2D coordinate;
@property (nonatomic, assign) CLLocationDistance radius;
@property (nonatomic, assign) MKMapRect boundingMapRect;

- (id)initWithCoordinate:(CLLocationCoordinate2D)coordinate radius:(CLLocationDistance)radius;

@end
