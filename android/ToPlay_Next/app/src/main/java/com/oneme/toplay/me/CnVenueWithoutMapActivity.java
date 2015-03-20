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

package com.oneme.toplay.me;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;


import com.oneme.toplay.Application;
import com.oneme.toplay.LoginActivity;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.invite.InviteNextActivity;

import com.parse.ParseGeoPoint;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.Map;

//import com.oneme.toplay.SDKReceiver;


public class CnVenueWithoutMapActivity extends ActionBarActivity {

    private static final String TAG = "CnLocalWithoutMapActivity";

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
    private ParseQueryAdapter<Invite> inviteQueryAdapter;

    // user last location
    private ParseGeoPoint userLastLocation;


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

        setContentView(R.layout.ome_activity_bd_without_map);



        // init map

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.cn_without_map_fragment);

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
        option.setScanSpan(1000000*60);
        mLocClient.setLocOption(option);
        mLocClient.start();

        // query data from parse cloud

        radius     = 2 * Application.getSearchDistance();
        lastRadius = radius;

        /*

      //  mMapView.setBuiltInZoomControls(true); // 设置启用内置的缩放控件
        MapController mapController = mMapView.getController(); // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
        mapController.setZoom(12); // 设置地图zoom级别


        MKSearch mKSearch = new MKSearch();
        mKSearch.init(mapManager, new MySearchListener());// 注意，MKSearchListener只支持一个，以最后一次设置为准
        mKSearch.poiSearchNearBy("KFC", new GeoPoint((int) (39.915 * 1E6),
                (int) (116.404 * 1E6)), 5000);

        */


    }


    private void doListQuery() {
        BDLocation myLoc = (currentLocation == null) ? lastLocation : currentLocation;
        // If location info is available, load the data
        if (myLoc != null) {
            // Refreshes the list view with new data based
            // usually on updated location data.
            inviteQueryAdapter.loadObjects();
        }
    }


    //
    // Helper method to get the Parse GEO point representation of a location
    //
    private ParseGeoPoint geoPointFromLocation(BDLocation loc) {
        return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
    }


    //
    // locate sdk listen
    //
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            lastLocation    = location;
            currentLocation = location;


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

            // set current location
            Application.setCurrentLatitude(Double.toString(location.getLatitude()));
            Application.setCurrentLongitude(Double.toString(location.getLongitude()));
            doListQuery();
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
               // updateZoom(myLatLng);
            }
            // Update the circle map
            //updateCircle(myLatLng);
        }
        // Save the current radius
        lastRadius = radius;
        // Query for the latest data to update the views.
        //doMapQuery();
        doListQuery();
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





    //
    // invoke Map activity intent.
    //
    private void invokeInviteActivity() {

        // check the user if login
        if (ParseUser.getCurrentUser() == null) {

            //Toast.makeText(JoinActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT),
            //        Toast.LENGTH_SHORT).show();

            // jump to login activity
            Intent invokeLoginActivityIntent = new Intent(CnVenueWithoutMapActivity.this, LoginActivity.class);
            startActivity(invokeLoginActivityIntent);
            finish();
        } else {

            // Only allow posts if we have a location
            BDLocation mLocation = (currentLocation == null) ? lastLocation : currentLocation;
            if (mLocation == null) {
                //Toast.makeText(LocalActivity.this,
                //        getResources().getString(R.string.current_location_unavailable), Toast.LENGTH_LONG).show();
                return;
            }

            //set current latitude and longitude
            Application.setCurrentLatitude(Double.toString(mLocation.getLatitude()));
            Application.setCurrentLongitude(Double.toString(mLocation.getLongitude()));

            //Ozzie Zhang 10-29-2014 please change JoinActivity to InviteActivity, now only for test
            Intent intent = new Intent(CnVenueWithoutMapActivity.this, InviteNextActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    private void putInviteToIntentExtra(Intent intent, Invite invite) {

        if (invite.getUser() != null) {
            intent.putExtra(Application.INTENT_EXTRA_USEROBJECTID, invite.getUser().getObjectId());
            intent.putExtra(Application.INTENT_EXTRA_USERNAME, invite.getUser().getUsername());
            // check user omeID
            if (invite.getUser().getString(AppConstant.OMEPARSEUSEROMEIDKEY) == null) {
                intent.putExtra(Application.INTENT_EXTRA_USEROMEID, AppConstant.OMEPARSEUSEROMEIDNULL);
            } else {
                intent.putExtra(Application.INTENT_EXTRA_USEROMEID, invite.getUser().getString(AppConstant.OMEPARSEUSEROMEIDKEY));
            }
        }

        intent.putExtra(Application.INTENT_EXTRA_SPORTTYPEVALUE, invite.getSportTypeValue());
        intent.putExtra(Application.INTENT_EXTRA_SPORTTYPE, invite.getSportType());
        intent.putExtra(Application.INTENT_EXTRA_PLAYERNUMBER, invite.getPlayerNumber());
        intent.putExtra(Application.INTENT_EXTRA_PLAYERLEVEL, invite.getPlayerLevel());
        intent.putExtra(Application.INTENT_EXTRA_TIME, invite.getPlayTime());
        intent.putExtra(Application.INTENT_EXTRA_COURT, invite.getCourt());
        intent.putExtra(Application.INTENT_EXTRA_FEE, invite.getFee());
        intent.putExtra(Application.INTENT_EXTRA_OTHER, invite.getOther());
        intent.putExtra(Application.INTENT_EXTRA_SUBMITTIME, invite.getSubmitTime());
        intent.putExtra(Application.INTENT_EXTRA_INVITEOBJECTID, invite.getObjectId());
    }


}

