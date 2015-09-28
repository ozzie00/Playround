/*
* Copyright 2014 OneME
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.oneme.toplay.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.ui.widget.DrawShadowFrameLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import com.oneme.toplay.SDKReceiver;


public class CnMapNextActivity extends BaseActivity {

    private static final String TAG = "CnMapNextActivity";

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;

    // Ozzie Zhang 2014-11-07 modify interval values
    // The update interval default value is 5
    private static final int UPDATE_INTERVAL_IN_SECONDS = 5;

    // A fast interval ceiling
    private static final int FAST_CEILING_IN_SECONDS = 1;

    // Update interval in milliseconds
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * UPDATE_INTERVAL_IN_SECONDS;

    // A fast ceiling of update intervals, used when the app is visible
    private static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * FAST_CEILING_IN_SECONDS;

    // Conversion from feet to meters
    private static final float METERS_PER_FEET = 0.3048f;

    // Conversion from kilometers to meters
    private static final int METERS_PER_KILOMETER = 1000;

    // Initial offset for calculating the map bounds
    private static final double OFFSET_CALCULATION_INIT_DIFF = 1.0;

    // Accuracy for calculating the map bounds
    private static final float OFFSET_CALCULATION_ACCURACY = 0.01f;

    // Maximum results returned from a Parse query
    private static final int MAX_Invite_SEARCH_RESULTS = 100;

    // Maximum post search radius for map in kilometers
    private static final int MAX_Invite_SEARCH_DISTANCE = 50;

    // Map fragment
    private SupportMapFragment mapFragment;

    // Represents the circle around a map
    private Circle mapCircle;

    // Fields for the map radius in feet
    private float radius;
    private float lastRadius;

    // Fields for helping process map and location changes
    private final Map<String, Marker> mapMarkers = new HashMap<String, Marker>();
    private int mostRecentMapUpdate;
    private boolean hasSetUpInitialLocation;
    private String selectedInviteObjectId;
    private BDLocation lastLocation;
    private BDLocation currentLocation;

    private Marker marker;

   // private LocationManagerProxy locationManager;

    // Adapter for the Parse query
    private ParseQueryAdapter<Invite> postsQueryAdapter;


    private DrawShadowFrameLayout mDrawShadowFrameLayout;


    // locate
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;

    MapView mMapView;
    BaiduMap mBaiduMap;

    // UI
    OnCheckedChangeListener radioButtonListener;
    Button requestLocButton;
    boolean isFirstLoc = true;




    public final class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            //Log.d(TAG, "action: " + s);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {

            } else if (s
                    .equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {

            }
        }
    }


    private SDKReceiver mReceiver;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // register sdk broadcast
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);

        setContentView(R.layout.ome_activity_navdrawer_bd_map);

        mDrawShadowFrameLayout = (DrawShadowFrameLayout) findViewById(R.id.main_content);

        //getSupportActionBar().show();



        //requestLocButton = (Button) findViewById(R.id.locate_button);
        //mCurrentMode     = LocationMode.NORMAL;
        //requestLocButton.setText("普通");
        //OnClickListener btnClickListener = new OnClickListener() {
        //    public void onClick(View v) {
        //        switch (mCurrentMode) {
        //            case NORMAL:
        //                requestLocButton.setText("跟随");
        //                mCurrentMode = LocationMode.FOLLOWING;
        //                mBaiduMap
        //                        .setMyLocationConfigeration(new MyLocationConfiguration(
        //                                mCurrentMode, true, mCurrentMarker));
        //                break;
                    //case COMPASS:
                    //    requestLocButton.setText("普通");
                    //    mCurrentMode = LocationMode.NORMAL;
                    //    mBaiduMap
                    //            .setMyLocationConfigeration(new MyLocationConfiguration(
                    //                    mCurrentMode, true, mCurrentMarker));
                    //    break;
        //              case FOLLOWING:
        //                  requestLocButton.setText("普通");//("罗盘");
        //                  mCurrentMode = LocationMode.NORMAL;
        //                  mBaiduMap
        //                          .setMyLocationConfigeration(new MyLocationConfiguration(
        //                                  mCurrentMode, true, mCurrentMarker));
        //                  break;
        //        }
        //    }
        //};
        //requestLocButton.setOnClickListener(btnClickListener);


        // init map

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.cn_map_fragment);

        mMapView  = mapFragment.getMapView(); //  findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // enable my location
        mBaiduMap.setMyLocationEnabled(true);
        // locate init
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        // time span  1s = 1000 ms
        option.setScanSpan(1000*60*5);
        mLocClient.setLocOption(option);
        mLocClient.start();


        // query data from parse cloud

        radius     = Application.getSearchDistance();
        lastRadius = radius;



        // Set up the map fragment
      //  mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.cn_map_fragment);

        // the calling order is very important
        // 1. set location source
      //  mapFragment.getBaiduMap().setMyLocationEnabled() .set.setLocationSource(this);

        // 2. if show location button
       // mapFragment.getBaiduMap().setMyLocationButtonEnabled(true);

        // 3. show location button and invoke locate
        mapFragment.getBaiduMap().setMyLocationEnabled(true);

        //Ozzie Zhang 2014-10-24 Disable Zoom Button on Map
     //   mapFragment.getBaiduMap().ssetZoomControlsEnabled(true);

        mapFragment.getBaiduMap().getUiSettings().setCompassEnabled(true);

       // marker = mapFragment.getBaiduMap().addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
       //         .period(50));

        // Set up the camera change handler
       // mapFragment.getBaiduMap().setOnCameraChangeListener(new OnCameraChangeListener() {
       //     public void onCameraChange(CameraPosition position) {
                // When the camera changes, update the query
                doMapQuery();
      //      }

       //     public void onCameraChangeFinish(CameraPosition position) {
                // When the camera changes, update the query
                // doMapQuery();
       //     }
       // });


        // for click map pin
        //add the onClickInfoWindowListener, when infoWindow is clicked,then invoke JoinActivity
    //    mapFragment.getBaiduMap(). .showInfoWindow(); setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

    //        @Override
    //        public void onInfoWindowClick(Marker marker) {

                // EventInfo eventInfo = eventMarkerMap.get(marker);

    //            BDLocation userLocation = (currentLocation == null) ? lastLocation : currentLocation;
    //            if (userLocation == null) {
                    //Toast.makeText(CnMapActivity.this,
                    //        getResources().getString(R.string.current_location_unavailable), Toast.LENGTH_LONG).show();
    //                return;
    //            }

    //            Intent invokeJoinActivityIntent = new Intent(CnMapActivity.this, JoinActivity.class);
    //            invokeJoinActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    //            invokeJoinActivityIntent.putExtra(Application.INTENT_EXTRA_LOCATION, userLocation);
    //            startActivity(invokeJoinActivityIntent);
    //        }
    //    });





    }


    //
    // Set up the query to update the map view
    //
    private void doMapQuery() {

        final int myUpdateNumber = ++mostRecentMapUpdate;


        final int mmapPin[] = {R.drawable.ome_pina, R.drawable.ome_pinb, R.drawable.ome_pinc, R.drawable.ome_pind, R.drawable.ome_pine,
                R.drawable.ome_pinf, R.drawable.ome_ping, R.drawable.ome_pinh, R.drawable.ome_pini, R.drawable.ome_pinj,
                R.drawable.ome_pink, R.drawable.ome_pinl, R.drawable.ome_pinm, R.drawable.ome_pinn, R.drawable.ome_pino,
                R.drawable.ome_pinp, R.drawable.ome_pinq, R.drawable.ome_pinr, R.drawable.ome_pins, R.drawable.ome_pint,
                R.drawable.ome_pinu, R.drawable.ome_pinv, R.drawable.ome_pinw, R.drawable.ome_pinx, R.drawable.ome_piny,
                R.drawable.ome_pinz};


        BDLocation myLoc = (currentLocation == null) ? lastLocation : currentLocation;
        // If location info isn't available, clean up any existing markers
        if (myLoc == null) {
            cleanUpMarkers(new HashSet<String>());
            return;
        }

        Application.setCurrentLatitude(Double.toString(myLoc.getLatitude()));
        Application.setCurrentLongitude(Double.toString(myLoc.getLongitude()));

        final ParseGeoPoint myPoint = geoPointFromLocation(myLoc);


        // Create the map Parse query
        ParseQuery<Invite> mapQuery = Invite.getQuery();
        mapQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        // Set up additional query filters
        mapQuery.whereWithinKilometers(AppConstant.OMEPARSELOCATIONKEY, myPoint, radius
                * METERS_PER_FEET / METERS_PER_KILOMETER);
        mapQuery.include(AppConstant.OMEPARSEUSERKEY);
        mapQuery.orderByDescending(AppConstant.OMEPARSECREATEDATKEY);
        mapQuery.setLimit(MAX_Invite_SEARCH_RESULTS);
        // Kick off the query in the background
        mapQuery.findInBackground(new FindCallback<Invite>() {
            @Override
            public void done(List<Invite> objects, ParseException e) {
                if (e != null) {
                    return;
                }

                //
                // Make sure we're processing results from
                // the most recent update, in case there
                // may be more than one in progress.
                //
                if (myUpdateNumber != mostRecentMapUpdate) {
                    return;
                }
                // Posts to show on the map
                Set<String> toKeep = new HashSet<String>();

                int newMap = 0;
                int oldMap = 0;
                // Loop through the results of the search
                for (Invite invite : objects) {
                    // Add this post to the list of map pins to keep
                    toKeep.add(invite.getObjectId());
                    // Check for an existing marker for this post
                    Marker oldMarker = mapMarkers.get(invite.getObjectId());
                    // Set up the map marker's location
                    MarkerOptions markerOpts =
                            new MarkerOptions().position(new LatLng(invite.getLocation().getLatitude(), invite
                                    .getLocation().getLongitude()));

                    // Set up the marker properties based on if it is within the search radius
                    if (invite.getLocation().distanceInKilometersTo(myPoint) > radius * METERS_PER_FEET / METERS_PER_KILOMETER) {
                        // Check for an existing out of range marker
                        if (oldMarker != null) {
                            if (oldMarker.getTitle()  == null) {
                                // Out of range marker already exists, skip adding it
                                continue;
                            } else {
                                // Marker now out of range, needs to be refreshed
                                oldMarker.remove();
                            }
                        }
                        // Display a red marker with a predefined title and no snippet
                        markerOpts = markerOpts.title(getResources().getString(R.string.post_out_of_range)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ome_pin_red));
                    } else {
                        // Check for an existing in range marker
                        if (oldMarker != null) {
                            if (oldMarker.getTitle() != null) {
                                // In range marker already exists, skip adding it
                                continue;
                            } else {
                                // Marker now in range, needs to be refreshed
                                oldMarker.remove();
                            }
                        }

                        //Ozzie Zhang 10-28-2014 please modify the username

                        // Display a green marker with the invite information

                        if (newMap < mmapPin.length) {
                            markerOpts = markerOpts.title(invite.getText())
                                    .icon(BitmapDescriptorFactory.fromResource(mmapPin[newMap++]));
                        } else {
                            markerOpts = markerOpts.title(invite.getText())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ome_pin));

                        }
                    }

                    // check getMap for preventing null pointer crash
                    if (mapFragment.getBaiduMap() != null) {
                        // Add a new marker
                        if (markerOpts != null) {
                            Marker marker = (Marker) mapFragment.getBaiduMap().addOverlay(markerOpts);  //addMarker(markerOpts);
                            mapMarkers.put(invite.getObjectId(), marker);
                        }
                        if (invite.getObjectId().equals(selectedInviteObjectId)) {
              //              marker.showInfoWindow();
                            selectedInviteObjectId = null;
                        }

                    }
                }
                // Clean up old markers.
                cleanUpMarkers(toKeep);
            }
        });
    }

    //
    // Helper method to clean up old markers
    //
    private void cleanUpMarkers(Set<String> markersToKeep) {
        for (String objId : new HashSet<String>(mapMarkers.keySet())) {
            if (!markersToKeep.contains(objId)) {
                Marker marker = mapMarkers.get(objId);
                marker.remove();
                mapMarkers.get(objId).remove();
                mapMarkers.remove(objId);
            }
        }
    }

    //
    // Helper method to get the Parse GEO point representation of a location
    //
    private ParseGeoPoint geoPointFromLocation(BDLocation loc) {
        return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
    }

    //
    // Displays a circle on the map representing the search radius
    //
   // private void updateCircle(LatLng myLatLng) {
   //     if (mapCircle == null) {
   //         mapCircle =
   //                 mapFragment.getBaiduMap().addCircle(
   //                         new CircleOptions().center(myLatLng).radius(radius * METERS_PER_FEET));
   //         int baseColor = Color.DKGRAY;
   //         mapCircle.setStrokeColor(baseColor);
   //         mapCircle.setStrokeWidth(2);
   //         mapCircle.setFillColor(Color.argb(30, Color.red(baseColor), Color.green(baseColor),
   //                 Color.blue(baseColor)));
   //     }
   //     mapCircle.setCenter(myLatLng);
   //     mapCircle.setRadius(radius * METERS_PER_FEET); // Convert radius in feet to meters.
   // }

    //
    // Zooms the map to show the area of interest based on the search radius
    //
    private void updateZoom(LatLng myLatLng) {
        // Get the bounds to zoom to
        LatLngBounds bounds = calculateBoundsWithCenter(myLatLng);
        // Zoom to the given bounds
        //mapFragment.getBaiduMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
    }

    //
    // Helper method to calculate the offset for the bounds used in map zooming
    //
    private double calculateLatLngOffset(LatLng myLatLng, boolean bLatOffset) {
        // The return offset, initialized to the default difference
        double latLngOffset = OFFSET_CALCULATION_INIT_DIFF;
        // Set up the desired offset distance in meters
        float desiredOffsetInMeters = radius * METERS_PER_FEET;
        // Variables for the distance calculation
        float[] distance    = new float[1];
        boolean foundMax    = false;
        double foundMinDiff = 0;
        // Loop through and get the offset
        do {
            // Calculate the distance between the point of interest
            // and the current offset in the latitude or longitude direction
            if (bLatOffset) {
                Location.distanceBetween(myLatLng.latitude, myLatLng.longitude, myLatLng.latitude
                        + latLngOffset, myLatLng.longitude, distance);
            } else {
                Location.distanceBetween(myLatLng.latitude, myLatLng.longitude, myLatLng.latitude,
                        myLatLng.longitude + latLngOffset, distance);
            }
            // Compare the current difference with the desired one
            float distanceDiff = distance[0] - desiredOffsetInMeters;
            if (distanceDiff < 0) {
                // Need to catch up to the desired distance
                if (!foundMax) {
                    foundMinDiff = latLngOffset;
                    // Increase the calculated offset
                    latLngOffset *= 2;
                } else {
                    double tmp = latLngOffset;
                    // Increase the calculated offset, at a slower pace
                    latLngOffset += (latLngOffset - foundMinDiff) / 2;
                    foundMinDiff = tmp;
                }
            } else {
                // Overshot the desired distance
                // Decrease the calculated offset
                latLngOffset -= (latLngOffset - foundMinDiff) / 2;
                foundMax = true;
            }
        } while (Math.abs(distance[0] - desiredOffsetInMeters) > OFFSET_CALCULATION_ACCURACY);
        return latLngOffset;
    }

    //
    // Helper method to calculate the bounds for map zooming
    //
    LatLngBounds calculateBoundsWithCenter(LatLng myLatLng) {
        // Create a bounds
        LatLngBounds.Builder builder =  new LatLngBounds.Builder();// LatLngBounds.builder();

        // Calculate east/west points that should to be included
        // in the bounds
        double lngDifference = calculateLatLngOffset(myLatLng, false);
        LatLng east = new LatLng(myLatLng.latitude, myLatLng.longitude + lngDifference);
        builder.include(east);
        LatLng west = new LatLng(myLatLng.latitude, myLatLng.longitude - lngDifference);
        builder.include(west);

        // Calculate north/south points that should to be included
        // in the bounds
        double latDifference = calculateLatLngOffset(myLatLng, true);
        LatLng north = new LatLng(myLatLng.latitude + latDifference, myLatLng.longitude);
        builder.include(north);
        LatLng south = new LatLng(myLatLng.latitude - latDifference, myLatLng.longitude);
        builder.include(south);

        return builder.build();
    }

    /*
     * Report location updates to the UI.
     */
    public void onLocationChanged(BDLocation location) {
        currentLocation = location;
        if (lastLocation != null
                && geoPointFromLocation(location)
                .distanceInKilometersTo(geoPointFromLocation(lastLocation)) < 0.01) {
            // If the location hasn't changed by more than 10 meters, ignore it.
            return;
        }
        lastLocation    = location;
        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (!hasSetUpInitialLocation) {
            // Zoom to the current location.
            updateZoom(myLatLng);
            hasSetUpInitialLocation = true;
        }
        // Update map radius indicator
       // updateCircle(myLatLng);
        doMapQuery();

        //Ozzie Zhang 2014-11-16 disable list query
        // doListQuery();
    }

    //
    // locate sdk listen
    //
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            lastLocation    = location;
            //currentLocation = location;


            // map view
            if (location == null || mMapView == null) {
                return;
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // map directory
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll  = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }

            //onLocationChanged(location);
            doMapQuery();
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        //unbindService(YOUR_SERVICE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();

        // Get the latest search distance preference
        radius = Application.getSearchDistance();
        // Checks the last saved location to show cached data if it's available
        if (lastLocation != null) {
            LatLng myLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            // If the search distance preference has been changed, move
            // map to new bounds.
            if (lastRadius != radius) {
                updateZoom(myLatLng);
            }
            // Update the circle map
            //updateCircle(myLatLng);
        }
        // Save the current radius
        lastRadius = radius;
        // Query for the latest data to update the views.
        doMapQuery();
    }

    @Override
    protected void onDestroy() {
        // stop locate
        mLocClient.stop();
        // close locate lay
        mBaiduMap.setMyLocationEnabled(false);
        //mMapView.onDestroy();
        //mMapView = null;
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_MAP;
    }

    @Override
    protected void onActionBarAutoShowOrHide(boolean shown) {
        super.onActionBarAutoShowOrHide(shown);
        mDrawShadowFrameLayout.setShadowVisible(shown, shown);
    }

}

