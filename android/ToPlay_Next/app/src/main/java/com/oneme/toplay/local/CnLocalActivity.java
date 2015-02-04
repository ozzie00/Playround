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

package com.oneme.toplay.local;


import com.oneme.toplay.MainActivity;
import com.oneme.toplay.MapActivity;
import com.oneme.toplay.R;
import com.oneme.toplay.Application;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.invite.InviteActivity;
import com.oneme.toplay.join.JoinActivity;
import com.oneme.toplay.me.MeActivity;
import com.oneme.toplay.me.SettingActivity;
import com.oneme.toplay.CnMapActivity;

import com.oneme.toplay.LoginActivity;

import com.oneme.toplay.service.CoreService;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

//import com.amap.api.maps2d.AMap;
//import com.amap.api.maps2d.SupportMapFragment;
//import com.amap.api.maps2d.model.Circle;
//import com.amap.api.maps2d.model.Marker;
//import com.amap.api.maps2d.model.LatLng;
//import com.amap.api.maps2d.model.LatLngBounds;
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.location.LocationManagerProxy;
//import com.amap.api.location.LocationProviderProxy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class CnLocalActivity  { // extends ActionBarActivity implements AMapLocationListener,Runnable {

/*
    private static final String TAG = "CnLocalActivity";

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
    private static final int MAX_Invite_SEARCH_DISTANCE = 60;

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
    private AMapLocation lastLocation;
    private AMapLocation currentLocation;


    private LocationManagerProxy locationManager;
    // Adapter for the Parse query
    private ParseQueryAdapter<Invite> postsQueryAdapter;

    private AMap mMap;

    // private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;


    private LocationManagerProxy aMapLocManager = null;
    private AMapLocation aMapLocation;//for locate timeout
    private Handler handler = new Handler();


    // Adapter for the Parse query
    private ParseQueryAdapter<Invite> inviteQueryAdapter;

    // user last location
    private ParseGeoPoint userLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setHasOptionsMenu(true);
        //  setMenuVisibility(true);
        supportInvalidateOptionsMenu();
        setContentView(R.layout.ome_activity_cn_local);
        //  setUpMapIfNeeded();

        //setHasOptionsMenu(true);

        aMapLocManager = LocationManagerProxy.getInstance(this);
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

        // Ozzie Zhang 2014-11-20 change to 12000
        // set 12 second limit to location
        handler.postDelayed(this, 12000);


        radius = Application.getSearchDistance();
        lastRadius = radius;


        // Set up a customized query
        ParseQueryAdapter.QueryFactory<Invite> factory =
                new ParseQueryAdapter.QueryFactory<Invite>() {
                    public ParseQuery<Invite> create() {
                        AMapLocation myLoc = (currentLocation == null) ? lastLocation : currentLocation;
                        ParseQuery<Invite> query = Invite.getQuery();
                        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                        query.include("user");
                        query.orderByDescending("createdAt");
                        query.whereWithinKilometers("location", geoPointFromLocation(myLoc), radius
                                * METERS_PER_FEET / METERS_PER_KILOMETER);
                        query.setLimit(MAX_Invite_SEARCH_RESULTS);
                        return query;
                    }
                };

        // Set up a progress dialog
        final ProgressDialog listLoadDialog = new ProgressDialog(CnLocalActivity.this);
        listLoadDialog.show();

        // Set up the query adapter
        inviteQueryAdapter = new ParseQueryAdapter<Invite>(CnLocalActivity.this, factory) {

            int listPinCount = 0;

            @Override
            public View getItemView(Invite invite, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.ome_activity_local_list, null);
                }

                ImageView avatarView = (ImageView) view.findViewById(R.id.avatar_view);
                TextView usernameView = (TextView) view.findViewById(R.id.username_view);
                TextView contentView = (TextView) view.findViewById(R.id.content_view);
                TextView submittimeView = (TextView) view.findViewById(R.id.duration);
                //  ImageView pinView        = (ImageView) view.findViewById(R.id.pin_view);
                //  ImageView rightarrowView = (ImageView) view.findViewById(R.id.rightarrow_view);


                // Ozzie Zhang 2014-11-04 need add query for avatar icon for this user
                // show username and invite content
                avatarView.setImageDrawable(getResources().getDrawable(R.drawable.ome_map_avataricon));
                contentView.setText(invite.getText());
                usernameView.setText(invite.getFromUsername());
                submittimeView.setText(invite.getSubmitTime());

                listLoadDialog.dismiss();

                return view;
            }
        };


        // Disable automatic loading when the adapter is attached to a view.
        inviteQueryAdapter.setAutoload(false);

        // Enable pagination, we'll not manage the query limit ourselves
        inviteQueryAdapter.setPaginationEnabled(true);

        // Attach the query adapter to the view
        ListView localInvitationListView = (ListView) findViewById(R.id.local_listview);
        localInvitationListView.setAdapter(inviteQueryAdapter);


        // Set up the handler for an item's selection
        localInvitationListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Invite item = inviteQueryAdapter.getItem(position);
                ParseUser hostuser = item.getFromUser();
                selectedInviteObjectId = item.getObjectId();

                Location clickedItemUserLocation = (currentLocation == null) ? lastLocation : currentLocation;
                clickedItemUserLocation.setLatitude(item.getLocation().getLatitude());
                clickedItemUserLocation.setLongitude(item.getLocation().getLongitude());

                Intent invokeJoinActivityIntent = new Intent(getBaseContext(), JoinActivity.class);//LocalActivity.this, JoinActivity.class);
                invokeJoinActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                invokeJoinActivityIntent.putExtra(Application.INTENT_EXTRA_LOCATION, clickedItemUserLocation);
                putInviteToIntentExtra(invokeJoinActivityIntent, item);

                startActivity(invokeJoinActivityIntent);


            }

        });

    }



            //
            // Report location updates to the UI.
            //
            public void onLocationChanged(AMapLocation location) {

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
                    //   updateZoom(myLatLng);
                    hasSetUpInitialLocation = true;
                }
                // Update map radius indicator
                //updateCircle(myLatLng);
                // Ozzie Zhang 2014-11-16 disable map query
                //doMapQuery();
                doListQuery();
            }


            //
            // Set up a query to update the list view
            //
            private void doListQuery() {
                Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
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
            private ParseGeoPoint geoPointFromLocation(Location loc) {
                return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
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
                }
                while (Math.abs(distance[0] - desiredOffsetInMeters) > OFFSET_CALCULATION_ACCURACY);
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

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.ome_local_menu, menu);

        if (ParseUser.getCurrentUser() != null) {
            MenuItem settingItem = menu.add(getResources().getString(R.string.meactivity_title));

            // menu.findItem(R.id.action_setting).
            settingItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    Intent invokeMeActivityIntent = new Intent(CnLocalActivity.this, MeActivity.class);
                    invokeMeActivityIntent.putExtra(AppConstant.OMEPARSEPARENTCLASSNAME, AppConstant.OMEPARSELOCALWITHOUTMAPACTIVITYCLASS);
                    startActivity(invokeMeActivityIntent);


                    // intent.putExtra("ParentClassName","A");
                    // startActivity(new Intent(LocalWithoutMapActivity.this, MeActivity.class));
                    return true;
                }
            });

        }

        if (ParseUser.getCurrentUser() != null){
            MenuItem logoutItem = menu.add(getResources().getString(R.string.OMEPARSELOGOUT));
            //menu.findItem(R.id.action_login)
            logoutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    // update user last time and last location

                    //get point according to  current latitude and longitude
                    userLastLocation = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

                    if (ParseUser.getCurrentUser() != null) {
                        ParseUser.getCurrentUser().put(AppConstant.OMEPARSEUSERLASTTIMEKEY, Time.currentTime());
                        ParseUser.getCurrentUser().put(AppConstant.OMEPARSEUSERLASTLOCATIONKEY, userLastLocation);

                        ParseUser.getCurrentUser().saveInBackground();

                    }

                    // preference logout
                    SharedPreferences preferences   = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("loggedin", false);
                    editor.apply();


                    // Stop the Core Service
                    Intent stopCoreServiceIntent = new Intent(CnLocalActivity.this, CoreService.class);
                    CnLocalActivity.this.stopService(stopCoreServiceIntent);



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
                    startActivity(new Intent(CnLocalActivity.this, LoginActivity.class));
                    return true;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);

    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_message:
                // Check username
                if (ParseUser.getCurrentUser() == null) {
                    Toast.makeText(CnLocalActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT), Toast.LENGTH_SHORT).show();
                    // jump to login activity
                    Intent invokeLoginActivityIntent = new Intent(CnLocalActivity.this, LoginActivity.class);
                    startActivity(invokeLoginActivityIntent);
                } else {
                    Intent invokeMessageIntent = new Intent(CnLocalActivity.this, MainActivity.class);
                    invokeMessageIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(invokeMessageIntent);
                }
                return true;
            case R.id.action_map:
                //Ozzie Zhang 10-29-2014 please change JoinActivity to InviteActivity, now only for test
                Intent invokeMapActivityIntent = new Intent(CnLocalActivity.this, CnMapActivity.class);
                invokeMapActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(invokeMapActivityIntent);
                return true;
            case R.id.action_invite:
                invokeInviteActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            Intent invokeLoginActivityIntent = new Intent(CnLocalActivity.this, com.oneme.toplay.LoginActivity.class);
            startActivity(invokeLoginActivityIntent);
            finish();
        } else {

            // Only allow posts if we have a location
            Location mLocation = (currentLocation == null) ? lastLocation : currentLocation;
            if (mLocation == null) {
                //Toast.makeText(LocalActivity.this,
                //        getResources().getString(R.string.current_location_unavailable), Toast.LENGTH_LONG).show();
                return;
            }

            //set current latitude and longitude
            Application.setCurrentLatitude(Double.toString(mLocation.getLatitude()));
            Application.setCurrentLongitude(Double.toString(mLocation.getLongitude()));

            //Ozzie Zhang 10-29-2014 please change JoinActivity to InviteActivity, now only for test
            Intent intent = new Intent(CnLocalActivity.this, InviteActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }



    //
    // Define a DialogFragment to display the error dialog generated in showErrorDialog.
    //
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;

        //
        // Default constructor. Sets the dialog field to null
        //
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        //
        // Set the dialog to display
        //
        // @param dialog An error dialog
        //
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        //
        // This method must return a Dialog to the DialogFragment.
        //
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    //
    // Define a call to invoke SearchDistanceActivity .
    //
    public void callSearchDistanceActivity(View view){

    }

    private void putInviteToIntentExtra(Intent intent, Invite invite) {

        intent.putExtra(Application.INTENT_EXTRA_USEROBJECTID, invite.getUser().getObjectId());
        intent.putExtra(Application.INTENT_EXTRA_USERNAME, invite.getUser().getUsername());
        intent.putExtra(Application.INTENT_EXTRA_USERLEVEL, invite.getUserLevel());
      //  intent.putExtra(Application.INTENT_EXTRA_USERICON, invite.getObjectId());
        intent.putExtra(Application.INTENT_EXTRA_SPORTTYPE, invite.getSportType());
        intent.putExtra(Application.INTENT_EXTRA_PLAYERNUMBER, invite.getPlayerNumber());
        intent.putExtra(Application.INTENT_EXTRA_PLAYERLEVEL, invite.getPlayerLevel());
        intent.putExtra(Application.INTENT_EXTRA_TIME, invite.getPlayTime());
        intent.putExtra(Application.INTENT_EXTRA_COURT, invite.getCourt());
        intent.putExtra(Application.INTENT_EXTRA_FEE, invite.getFee());
        intent.putExtra(Application.INTENT_EXTRA_OTHER, invite.getOther());
      //  intent.putExtra(Application.INTENT_EXTRA_FROMUSER, invite.getFromUser());
      //  intent.putExtra(Application.INTENT_EXTRA_FROMUSERNAME, invite.getObjectId());
        intent.putExtra(Application.INTENT_EXTRA_SUBMITTIME, invite.getSubmitTime());

    }


    //
    // this method is deprecated
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
                //updateZoom(myLatLng);
            }
        }
        // Save the current radius
        lastRadius = radius;
        // Query for the latest data to update the views.
        //Ozzie Zhang 2014-11-16 disable list query
        doListQuery();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.cn_local_map_fragment)).getMap();
        }
    }

    */


}
