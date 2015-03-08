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

package com.oneme.toplay;


/*
import com.amap.api.maps.MapView;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.invite.InviteActivity;
import com.oneme.toplay.join.JoinActivity;
import com.oneme.toplay.local.CnLocalActivity;
import com.oneme.toplay.local.LocalActivity;
import com.oneme.toplay.me.SettingActivity;

import com.oneme.toplay.LoginActivity;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;



import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.MarkerOptions;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;

import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.model.CircleOptions;



import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;






public class CnMapActivity extends FragmentActivity implements LocationSource, AMapLocationListener,Runnable {


    private static final String TAG = "CnMapActivity";

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
    private com.amap.api.maps2d.SupportMapFragment mapFragment;

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
    private Location lastLocation;
    private Location currentLocation;

    private LocationManagerProxy locationManager;

    // Adapter for the Parse query
    private ParseQueryAdapter<Invite> postsQueryAdapter;

    private AMap mMap;

    // private OnLocationChangedListener mListener;
    private OnLocationChangedListener mListener;
    private Marker marker;// 定位雷达小图标

    private LocationManagerProxy aMapLocManager = null;
    private AMapLocation aMapLocation;// 用于判断定位超时
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setHasOptionsMenu(true);
        //  setMenuVisibility(true);
        supportInvalidateOptionsMenu();
        setContentView(R.layout.ome_activity_cn_map);
        //  setUpMapIfNeeded();

        //setHasOptionsMenu(true);

        aMapLocManager = LocationManagerProxy.getInstance(CnMapActivity.this);
        aMapLocManager.setGpsEnable(true);

        //
        // mAMapLocManager.setGpsEnable(false);//
        // 1.0.2 version new method， true express includes gps location ，false express network，default is true
        // Location API location mix GPS with network
        //  first parameter is location provider，second parameter is the shortest time 2000ms，
        //  third parameter is distance with meter，fourth parameter is location listener
        //
        aMapLocManager.requestLocationUpdates(
                LocationProviderProxy.AMapNetwork, 2000, 10, this);
       // aMapLocManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 2000, 10, PendingIntent);

        // Ozzie Zhang 2014-11-20 change to 120000 (four zero)
        // set 12 second limit to location
        handler.postDelayed(this, 12000);

        radius = Application.getSearchDistance();
        lastRadius = radius;

        // Set up a customized query
        ParseQueryAdapter.QueryFactory<Invite> factory =
                new ParseQueryAdapter.QueryFactory<Invite>() {
                    public ParseQuery<Invite> create() {
                        Location myLocation = (currentLocation == null) ? lastLocation : currentLocation;
                        ParseQuery<Invite> query = Invite.getQuery();
                        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                        query.include("user");
                        query.orderByDescending("createdAt");
                        query.whereWithinKilometers("location", geoPointFromLocation(myLocation), radius
                                * METERS_PER_FEET / METERS_PER_KILOMETER);
                        query.setLimit(MAX_Invite_SEARCH_RESULTS);
                        return query;
                    }
                };

        // Set up the query adapter
        postsQueryAdapter = new ParseQueryAdapter<Invite>(CnMapActivity.this, factory) {
            @Override
            public View getItemView(Invite invite, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.ome_activity_local_list, null);
                }

                ImageView avatarView = (ImageView) view.findViewById(R.id.avatar_view);
                TextView usernameView = (TextView) view.findViewById(R.id.username_view);
                TextView contentView = (TextView) view.findViewById(R.id.content_view);
                //ImageView rightarrowView = (ImageView) view.findViewById(R.id.rightarrow_view);


                // show username and invite content
                avatarView.setImageDrawable(getResources().getDrawable(R.drawable.ome_map_avataricon));
                contentView.setText(invite.getUser().getUsername());
                usernameView.setText(invite.getText());
               // rightarrowView.setImageDrawable(getResources().getDrawable(R.drawable.ome_rightarrow));
                return view;
            }
        };

        // Disable automatic loading when the adapter is attached to a view.
        postsQueryAdapter.setAutoload(false);

        // Disable pagination, we'll manage the query limit ourselves
        postsQueryAdapter.setPaginationEnabled(false);

        // Attach the query adapter to the view
        //    ListView postsListView = (ListView) findViewById(R.id.posts_listview);
        //    postsListView.setAdapter(postsQueryAdapter);

        // Set up the handler for an item's selection
        // postsListView.setOnItemClickListener(new OnItemClickListener() {
        //   public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        // });

        // Set up the map fragment
        mapFragment = (com.amap.api.maps2d.SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.cn_map_fragment);

        // the calling order is very important
        // 1. set location source
        mapFragment.getMap().setLocationSource(this);

        // 2. if show location button
        mapFragment.getMap().getUiSettings().setMyLocationButtonEnabled(true);

        // 3. show location button and invoke locate
        mapFragment.getMap().setMyLocationEnabled(true);

        //Ozzie Zhang 2014-10-24 Disable Zoom Button on Map
        mapFragment.getMap().getUiSettings().setZoomControlsEnabled(true);

        mapFragment.getMap().getUiSettings().setCompassEnabled(true);

        marker = mapFragment.getMap().addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .period(50));

        // Set up the camera change handler
        mapFragment.getMap().setOnCameraChangeListener(new OnCameraChangeListener() {
            public void onCameraChange(com.amap.api.maps2d.model.CameraPosition position) {
                // When the camera changes, update the query
                doMapQuery();
            }

            public void onCameraChangeFinish(com.amap.api.maps2d.model.CameraPosition position) {
                // When the camera changes, update the query
               // doMapQuery();
            }
        });


        // for click map pin
        //add the onClickInfoWindowListener, when infoWindow is clicked,then invoke JoinActivity
        mapFragment.getMap().setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                // EventInfo eventInfo = eventMarkerMap.get(marker);

                Location userLocation = (currentLocation == null) ? lastLocation : currentLocation;
                if (userLocation == null) {
                    //Toast.makeText(CnMapActivity.this,
                    //        getResources().getString(R.string.current_location_unavailable), Toast.LENGTH_LONG).show();
                    return;
                }

                Intent invokeJoinActivityIntent = new Intent(CnMapActivity.this, JoinActivity.class);
                invokeJoinActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                invokeJoinActivityIntent.putExtra(Application.INTENT_EXTRA_LOCATION, userLocation);
                startActivity(invokeJoinActivityIntent);
            }
        });



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


        Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
        // If location info isn't available, clean up any existing markers
        if (myLoc == null) {
            cleanUpMarkers(new HashSet<String>());
            return;
        }
        final ParseGeoPoint myPoint = geoPointFromLocation(myLoc);
        // Create the map Parse query
        ParseQuery<Invite> mapQuery = Invite.getQuery();
        mapQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        // Set up additional query filters
        mapQuery.whereWithinKilometers(AppConstant.OMEPARSELOCATIONKEY, myPoint, MAX_Invite_SEARCH_DISTANCE);
        mapQuery.include(AppConstant.OMEPARSEUSERKEY);
        mapQuery.orderByDescending(AppConstant.OMEPARSECREATEDAT);
        mapQuery.setLimit(MAX_Invite_SEARCH_RESULTS);
        // Kick off the query in the background
        mapQuery.findInBackground(new FindCallback<Invite>() {
            @Override
            public void done(List<Invite> objects, ParseException e) {
                if (e != null) {
                    if (Application.APPDEBUG) {
                        Log.d(Application.APPTAG, "An error occurred while querying for map invitations.", e);
                    }
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
                    com.amap.api.maps2d.model.Marker oldMarker = mapMarkers.get(invite.getObjectId());
                    // Set up the map marker's location
                    com.amap.api.maps2d.model.MarkerOptions markerOpts =
                            new com.amap.api.maps2d.model.MarkerOptions().position(new com.amap.api.maps2d.model.LatLng(invite.getLocation().getLatitude(), invite
                                    .getLocation().getLongitude()));
                    // Set up the marker properties based on if it is within the search radius
                    if (invite.getLocation().distanceInKilometersTo(myPoint) > radius * METERS_PER_FEET / METERS_PER_KILOMETER) {
                        // Check for an existing out of range marker
                        if (oldMarker != null) {
                            if (oldMarker.getSnippet() == null) {
                                // Out of range marker already exists, skip adding it
                                continue;
                            } else {
                                // Marker now out of range, needs to be refreshed
                                oldMarker.remove();
                            }
                        }
                        // Display a red marker with a predefined title and no snippet
                        markerOpts = markerOpts.title(getResources().getString(R.string.post_out_of_range)).icon(com.amap.api.maps2d.model.BitmapDescriptorFactory.fromResource(R.drawable.ome_pin_red));
                    } else {
                        // Check for an existing in range marker
                        if (oldMarker != null) {
                            if (oldMarker.getSnippet() != null) {
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
                                    .icon(com.amap.api.maps2d.model.BitmapDescriptorFactory.fromResource(mmapPin[newMap++]));
                        } else {
                            markerOpts = markerOpts.title(invite.getText())
                                    .icon(com.amap.api.maps2d.model.BitmapDescriptorFactory.fromResource(R.drawable.ome_pin));

                        }
                    }

                    // check getMap for preventing null pointer crash
                    if (mapFragment.getMap() != null) {
                        // Add a new marker
                        com.amap.api.maps2d.model.Marker marker = mapFragment.getMap().addMarker(markerOpts);
                        mapMarkers.put(invite.getObjectId(), marker);
                        if (invite.getObjectId().equals(selectedInviteObjectId)) {
                            marker.showInfoWindow();
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
                com.amap.api.maps2d.model.Marker marker = mapMarkers.get(objId);
                marker.remove();
                mapMarkers.get(objId).remove();
                mapMarkers.remove(objId);
            }
        }
    }


    //
    // Helper method to get the Parse GEO point representation of a location
    //
    private ParseGeoPoint geoPointFromLocation(Location loc) {
        return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
    }

    //
    // Displays a circle on the map representing the search radius
    //
    private void updateCircle(LatLng myLatLng) {
        if (mapCircle == null) {
            mapCircle =
                    mapFragment.getMap().addCircle(
                            new CircleOptions().center(myLatLng).radius(radius * METERS_PER_FEET));
            int baseColor = Color.DKGRAY;
            mapCircle.setStrokeColor(baseColor);
            mapCircle.setStrokeWidth(2);
            mapCircle.setFillColor(Color.argb(30, Color.red(baseColor), Color.green(baseColor),
                    Color.blue(baseColor)));
        }
        mapCircle.setCenter(myLatLng);
        mapCircle.setRadius(radius * METERS_PER_FEET); // Convert radius in feet to meters.
    }

    //
    // Zooms the map to show the area of interest based on the search radius
    //
    private void updateZoom(LatLng myLatLng) {
        // Get the bounds to zoom to
        LatLngBounds bounds = calculateBoundsWithCenter(myLatLng);
        // Zoom to the given bounds
        mapFragment.getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
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
        float[] distance = new float[1];
        boolean foundMax = false;
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
        LatLngBounds.Builder builder = LatLngBounds.builder();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem settingItem = menu.add(getResources().getString(R.string.OMEPARSESETTING));

        // menu.findItem(R.id.action_setting).
        settingItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(CnMapActivity.this, SettingActivity.class));
                return true;
            }
        });

        if (ParseUser.getCurrentUser() != null) {
            MenuItem logoutItem = menu.add(getResources().getString(R.string.OMEPARSELOGOUT));
            //menu.findItem(R.id.action_login)
            logoutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    // Call the Parse log out method
                    ParseUser.logOut();
                    finish();
                    return true;
                }
            });
        } else {
            MenuItem loginItem = menu.add(getResources().getString(R.string.OMEPARSELOGIN));
            //menu.findItem(R.id.action_login)
            loginItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    startActivity(new Intent(CnMapActivity.this, LoginActivity.class));
                    return true;
                }
            });
        }


        //Ozzie Zhang 2014-11-16 add for search
        //getMenuInflater().inflate(R.menu.ome_main, menu);


        return true;

    }


    //
    // this method deprecate
    //
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    //
    // call back function after successfully locate
    //
    //@Override
    // public void onLocationChanged(AMapLocation aLocation) {
    //     if (mListener != null && aLocation != null) {
    //mListener.onLocationChanged(aLocation);// 显示系统小蓝点
    //          mGPSMarker.setPosition(new LatLng( aLocation.getLatitude(),aLocation.getLongitude()));
    //      }
    //  }



    //
    // Report location updates to the UI.
    //
    public void onLocationChanged(AMapLocation location) {

        if (mListener != null && location != null) {
            mListener.onLocationChanged(location);// 显示系统小蓝点
            marker.setPosition(new LatLng(location.getLatitude(), location
                    .getLongitude()));// 定位雷达小图标

        }

        if (location != null) {

            // check if location timeout
            this.aMapLocation = location;
            Double geoLat = location.getLatitude();
            Double geoLng = location.getLongitude();
        }

        currentLocation = location;
        if (lastLocation != null
                && geoPointFromLocation(location)
                .distanceInKilometersTo(geoPointFromLocation(lastLocation)) < 0.01) {
            // If the location hasn't changed by more than 10 meters, ignore it.
            return;
        }
        lastLocation = location;
        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (!hasSetUpInitialLocation) {
            // Zoom to the current location.
            updateZoom(myLatLng);
            hasSetUpInitialLocation = true;
        }
        // Update map radius indicator
        updateCircle(myLatLng);
        // Ozzie Zhang 2014-11-16 disable map query
        doMapQuery();
        //doListQuery();
    }


    // stop location
    private void stopLocation() {
        if (aMapLocManager != null) {
            aMapLocManager.removeUpdates(this);
            aMapLocManager.destory();
        }
        aMapLocManager = null;
    }

    @Override
    public void run() {
        if (aMapLocation == null) {
            //Toast.makeText(this, getResources().getString(R.string.locate_fail), Toast.LENGTH_LONG).show();
            stopLocation();
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        setUpMapIfNeeded();
//    }


    //
    // Called when the Activity is resumed. Updates the view.
    //
    @Override
    protected void onResume() {
        super.onResume();

        setUpMapIfNeeded();

        Application.getConfigHelper().fetchConfigIfNeeded();

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
            updateCircle(myLatLng);
        }
        // Save the current radius
        lastRadius = radius;
        // Query for the latest data to update the views.
        doMapQuery();

        //Ozzie Zhang 2014-11-16 disable list query
        //doListQuery();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.cn_map_fragment)).getMap();
        }
    }


    //
    // invoke locate
    //
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (aMapLocManager == null) {
            aMapLocManager = LocationManagerProxy.getInstance(this);

            aMapLocManager.requestLocationUpdates(
                    LocationProviderProxy.AMapNetwork, 2000, 10, this);
        }
    }


    @Override
    public void deactivate() {
        mListener = null;
        if (aMapLocManager != null) {
            aMapLocManager.removeUpdates(this);
            aMapLocManager.destory();
        }
        aMapLocManager = null;
    }
}

*/





import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.baidu.mapapi.map.OverlayOptions;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.invite.InviteActivity;
import com.oneme.toplay.join.JoinActivity;
import com.oneme.toplay.local.CnLocalActivity;
import com.oneme.toplay.local.LocalActivity;
import com.oneme.toplay.me.SettingActivity;

import com.oneme.toplay.LoginActivity;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;


//import com.oneme.toplay.SDKReceiver;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.SupportMapFragment;


public class CnMapActivity extends ActionBarActivity {

    private static final String TAG = "CnMapActivity";

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

        setContentView(R.layout.ome_activity_bd_map);

        getSupportActionBar().show();



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
        // time span  1s = 1000 * 1000 ms
        option.setScanSpan(1000000*60*5);
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
                        Marker marker = (Marker)mapFragment.getBaiduMap().addOverlay(markerOpts);  //addMarker(markerOpts);
                        mapMarkers.put(invite.getObjectId(), marker);
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
            if (location == null || mMapView == null)
                return;
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

}

