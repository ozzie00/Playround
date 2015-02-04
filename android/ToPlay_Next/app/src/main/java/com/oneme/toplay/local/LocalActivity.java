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

import com.oneme.toplay.Application;
//import com.oneme.toplay.LoginActivity;
import com.oneme.toplay.MapActivity;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.invite.InviteActivity;
import com.oneme.toplay.join.JoinActivity;
import com.oneme.toplay.me.SettingActivity;

import com.oneme.toplay.LoginActivity;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
//import android.app.ActionBar;
import android.support.v7.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class LocalActivity extends FragmentActivity implements LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, OnItemClickListener {

    private static final String TAG = "LocalActivity";

    /*
     * Define a request code to send to Google Play services This code is returned in
     * Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /*
     * Constants for location update parameters
     */
    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;

    //Ozzie Zhang 2014-11-07 change interval value
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

    /*
     * Constants for handling location results
     */
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

    /*
     * Other class member variables
     */
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
    private Location lastLocation;
    private Location currentLocation;

    private ProgressBar progressBar;

    // A request to connect to Location Services
    private LocationRequest locationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient locationClient;

    // Adapter for the Parse query
    private ParseQueryAdapter<Invite> inviteQueryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radius = 3*(Application.getSearchDistance());
        lastRadius = radius;

        setContentView(R.layout.ome_activity_local);

        // Create a new global location parameters object
        locationRequest = LocationRequest.create();

        // Set the update interval
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Use high accuracy
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the interval ceiling to one minute
        locationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        // Create a new location client, using the enclosing class to handle callbacks.
        locationClient = new LocationClient(LocalActivity.this, LocalActivity.this, LocalActivity.this);

        // Set up a customized query
        ParseQueryAdapter.QueryFactory<Invite> factory =
                new ParseQueryAdapter.QueryFactory<Invite>() {
                    public ParseQuery<Invite> create() {
                        Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
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
        final ProgressDialog listLoadDialog = new ProgressDialog(LocalActivity.this);
        listLoadDialog.show();

        // Set up the query adapter
        inviteQueryAdapter = new ParseQueryAdapter<Invite>(LocalActivity.this, factory) {

            int listPinCount = 0;

            @Override
            public View getItemView(Invite invite, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.ome_activity_local_list, null);
                }

                ImageView avatarView = (ImageView) view.findViewById(R.id.avatar_view);
                TextView usernameView = (TextView) view.findViewById(R.id.username_view);
                //TextView contentView = (TextView) view.findViewById(R.id.content_view);
                TextView submittimeView = (TextView) view.findViewById(R.id.duration);
                //  ImageView pinView        = (ImageView) view.findViewById(R.id.pin_view);
                //  ImageView rightarrowView = (ImageView) view.findViewById(R.id.rightarrow_view);


                // Ozzie Zhang 2014-11-04 need add query for avatar icon for this user
                // show username and invite content
                avatarView.setImageDrawable(getResources().getDrawable(R.drawable.ome_map_avataricon));
                //contentView.setText(invite.getText());
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
        localInvitationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Invite item = inviteQueryAdapter.getItem(position);
                ParseUser hostuser = item.getFromUser();
                selectedInviteObjectId = item.getObjectId();

                Location clickedItemUserLocation = (currentLocation == null) ? lastLocation : currentLocation;
                clickedItemUserLocation.setLatitude(item.getLocation().getLatitude());
                clickedItemUserLocation.setLongitude(item.getLocation().getLongitude());

                Intent invokeJoinActivityIntent = new Intent(getBaseContext(), JoinActivity.class);//LocalActivity.this, JoinActivity.class);
                invokeJoinActivityIntent.putExtra(Application.INTENT_EXTRA_LOCATION, clickedItemUserLocation);
                putInviteToIntentExtra(invokeJoinActivityIntent, item);

                startActivity(invokeJoinActivityIntent);

            }
        });

    }


    /*
     * invoke Map activity intent.
     */
    private void invokeMapActivity() {

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
        Intent intent = new Intent(LocalActivity.this, InviteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    /*
     * Called when the Activity is no longer visible at all. Stop updates and disconnect.
     */
    @Override
    public void onStop() {
        // If the client is connected
        if (locationClient.isConnected()) {
            stopPeriodicUpdates();
        }

        // After disconnect() is called, the client is considered "dead".
        locationClient.disconnect();

        super.onStop();
    }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */
    @Override
    public void onStart() {
        super.onStart();

        // Connect to the location services client
        locationClient.connect();
    }

    /*
     * Called when the Activity is resumed. Updates the view.
     */
    @Override
    protected void onResume() {
        super.onResume();

        Application.getConfigHelper().fetchConfigIfNeeded();

        // Get the latest search distance preference
        radius = Application.getSearchDistance();
        // Checks the last saved location to show cached data if it's available
        if (lastLocation != null) {
            LatLng myLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            // If the search distance preference has been changed, move
            // map to new bounds.
            if (lastRadius != radius) {
             //   updateZoom(myLatLng);
            }
            // Update the circle map
            //updateCircle(myLatLng);
        }
        // Save the current radius
        lastRadius = radius;
        // Query for the latest data to update the views.
        //Ozzie Zhang 2014-11-16 disable map query
        //doMapQuery();
        doListQuery();
    }

    /*
     * Handle results returned to this Activity by other Activities started with
     * startActivityForResult(). In particular, the method onConnectionFailed() in
     * LocationUpdateRemover and LocationUpdateRequester may call startResolutionForResult() to start
     * an Activity that handles Google Play services problems. The result of this call returns here,
     * to onActivityResult.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Choose what to do based on the request code
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:

                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:

                        break;

                    // If any other result was returned by Google Play services
                    default:
                        break;
                }

                // If any other request code was received
            default:
                break;
        }
    }

    /*
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(LocalActivity.this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
                        // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, LocalActivity.this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getSupportFragmentManager(), Application.APPTAG);
            }
            return false;
        }
    }

    /*
     * Called by Location Services when the request to connect the client finishes successfully. At
     * this point, you can request the current location or start periodic updates
     */
    public void onConnected(Bundle bundle) {

        currentLocation = getLocation();
        startPeriodicUpdates();
    }

    /*
     * Called by Location Services if the connection to the location client drops because of an error.
     */
    public void onDisconnected() {
    }

    /*
     * Called by Location Services if the attempt to Location Services fails.
     */
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Google Play services can resolve some errors it detects. If the error has a resolution, try
        // sending an Intent to start a Google Play services activity that can resolve error.
        if (connectionResult.hasResolution()) {
            try {

                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            } catch (IntentSender.SendIntentException e) {

            }
        } else {
            // If no resolution is available, display a dialog to the user with the error.
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    /*
     * Report location updates to the UI.
     */
    public void onLocationChanged(Location location) {
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
        // Ozzie Zhang 2014-11-16 disable map query
        //doMapQuery();
        doListQuery();
    }

    /*
     * In response to a request to start updates, send a request to Location Services
     */
    private void startPeriodicUpdates() {
        locationClient.requestLocationUpdates(locationRequest, LocalActivity.this);
    }

    /*
     * In response to a request to stop updates, send a request to Location Services
     */
    private void stopPeriodicUpdates() {
        locationClient.removeLocationUpdates(LocalActivity.this);
    }

    /*
     * Get the current location
     */
    private Location getLocation() {
        // If Google Play Services is available
        if (servicesConnected()) {
            // Get the current location
            return locationClient.getLastLocation();
        } else {
            return null;
        }
    }

    /*
     * Set up a query to update the list view
     */
    private void doListQuery() {
        Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
        // If location info is available, load the data
        if (myLoc != null) {
            // Refreshes the list view with new data based
            // usually on updated location data.
            inviteQueryAdapter.loadObjects();
        }
    }

    /*
     * Set up the query to update the map view
     */
    private void doMapQuery() {
    }

    /*
     * Helper method to clean up old markers
     */
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

    /*
     * Helper method to get the Parse GEO point representation of a location
     */
    private ParseGeoPoint geoPointFromLocation(Location loc) {
        return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
    }


    /*
     * Helper method to calculate the offset for the bounds used in map zooming
     */
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

    /*
     * Helper method to calculate the bounds for map zooming
     */
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
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.setting, menu);



        MenuItem settingItem = menu.add(getResources().getString(R.string.OMEPARSESETTING));

        // menu.findItem(R.id.action_setting).
        settingItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(LocalActivity.this, SettingActivity.class));
                return true;
            }
        });

        if (ParseUser.getCurrentUser() != null){
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
                    startActivity(new Intent(LocalActivity.this, LoginActivity.class));
                    return true;
                }
            });
        }

        return true;

    }

    /*
     * Show a dialog returned by Google Play services for the connection error code
     */
    private void showErrorDialog(int errorCode) {
        // Get the error dialog from Google Play services
        Dialog errorDialog =
                GooglePlayServicesUtil.getErrorDialog(errorCode, this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(getSupportFragmentManager(), Application.APPTAG);
        }
    }

    /*
     * Define a DialogFragment to display the error dialog generated in showErrorDialog.
     */
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        /*
         * Set the dialog to display
         *
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    /*
     * Define a call to invoke SearchDistanceActivity .
     */
    public void callSearchDistanceActivity(View view){

    }


    @Override
    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
       // if(position==0){
         //   Intent i = new Intent(this, abc.class);
         //   startActivity(i);
            //Toast.makeText(this, "You pressed the first item in the list",
            //  Toast.LENGTH_SHORT).show();
       // } else {
            //Toast.makeText(this, "You pressed all other items in the list",
            //        Toast.LENGTH_SHORT).show();

       // }
        // else if(position==1){
        // start another activity here...
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
}
