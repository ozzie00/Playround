//
//  OMEMapViewAnnotation.h
//  ToPlay
//
//  Created by Ozzie Zhang on 9/1/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <MapKit/MapKit.h>


@interface OMEMapViewAnnotation : NSObject <MKAnnotation>

@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *subtitle;
@property (nonatomic, copy) NSString *level;
@property (nonatomic, copy) NSString *starlevel;
@property (nonatomic, copy) NSString *distance;

@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;

-(id) initWithTitle:(NSString *) title AndCoordinate:(CLLocationCoordinate2D)coordinate;


@end
