//
//  OMEMapViewAnnotation.m
//  ToPlay
//
//  Created by Ozzie Zhang on 9/1/14.
//  Copyright (c) 2014 OneME. All rights reserved.
//

#import "OMEMapViewAnnotation.h"

@implementation OMEMapViewAnnotation


@synthesize title=_title;
@synthesize subtitle=_subtitle;
@synthesize level=_level;
@synthesize starlevel=_starlevel;
@synthesize distance=_distance;
@synthesize coordinate=_coordinate;


-(id) initWithTitle:(NSString *) title AndCoordinate:(CLLocationCoordinate2D)coordinate {
	
}

- (CLLocationCoordinate2D)coordinate;
{
	CLLocationCoordinate2D theCoordinate;
	theCoordinate.latitude = 37.810000;
	theCoordinate.longitude = -122.477450;
	return theCoordinate;
}

// required if you set the MKPinAnnotationView's "canShowCallout" property to YES
- (NSString *)title
{
	return @"Golden Gate Bridge";
}

// optional
- (NSString *)subtitle
{
	return @"Opened: May 27, 1937";
}

@end

