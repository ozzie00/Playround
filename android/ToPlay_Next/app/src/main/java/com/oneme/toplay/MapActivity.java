package com.oneme.toplay;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.join.JoinNextActivity;
import com.oneme.toplay.me.SettingActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class MapActivity extends AppCompatActivity  implements LocationListener,
    GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener{

    private static final String TAG = "MapActivity";

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
    private static final int MAX_Invite_SEARCH_DISTANCE = 50;
    
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
    
    // A request to connect to Location Services
    private LocationRequest locationRequest;
    
    // Stores the current instantiation of the location client in this object
    private LocationClient locationClient;
    
    // Adapter for the Parse query
    private ParseQueryAdapter<Invite> postsQueryAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radius = Application.getSearchDistance();
        lastRadius = radius;
        setContentView(R.layout.ome_activity_main);
        
        // Create a new global location parameters object
        locationRequest = LocationRequest.create();
        
        // Set the update interval
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        
        // Use high accuracy
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        
        // Set the interval ceiling to one minute
        locationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
        
        // Create a new location client, using the enclosing class to handle callbacks.
        locationClient = new LocationClient(MapActivity.this, MapActivity.this, MapActivity.this);
        
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
        postsQueryAdapter = new ParseQueryAdapter<Invite>(MapActivity.this, factory) {
            @Override
            public View getItemView(Invite invite, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.ome_activity_local_list, null);
                }
        
                ImageView avatarView     = (ImageView) view.findViewById(R.id.avatar_view);
                TextView usernameView    = (TextView) view.findViewById(R.id.username_view);
                TextView contentView     = (TextView) view.findViewById(R.id.content_view);
               // ImageView rightarrowView = (ImageView) view.findViewById(R.id.rightarrow_view);
        
        
                // show username and invite content
                avatarView.setImageDrawable(getResources().getDrawable(R.drawable.ome_map_heicon));
                contentView.setText(invite.getUser().getUsername());
                usernameView.setText(invite.getText());
              //  rightarrowView.setImageDrawable(getResources().getDrawable(R.drawable.ome_rightarrow));
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
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        
        mapFragment.getMap().getUiSettings().setZoomControlsEnabled(true);
        //mapFragment.getMap().getUiSettings().setMyLocationButtonEnabled(false);
        mapFragment.getMap().getUiSettings().setCompassEnabled(true);
        // mapFragment.getMap().setPadding(10, 300, 200, 300);
        
        // Enable the current location "blue dot"
        mapFragment.getMap().setMyLocationEnabled(true);
        
        // Customize my location button positon
        // Hacker way to get mylocation button id
        // ZoomControl id = 0x1
        //MyLocation button id = 0x2
        // Find myLocationButton view
        View myLocationButton = mapFragment.getView().findViewById(2);
        
        if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
             // ZoomControl is inside of RelativeLayout
             RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();
             // Align it to - parent BOTTOM|RIGHT
             params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
             params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        
             params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
             params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        
             // Update margins, set to 10dp
             final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                   getResources().getDisplayMetrics());
             params.setMargins(margin, margin, margin, margin);
        
             myLocationButton.setLayoutParams(params);
        }
        
        
        //mapFragment.getMap().setPadding(320, 480, 0, 0);
        
        
        // Set up the camera change handler
        mapFragment.getMap().setOnCameraChangeListener(new OnCameraChangeListener() {
            public void onCameraChange(CameraPosition position) {
                // When the camera changes, update the query
                doMapQuery();
            }
        });
       
       
       
        //add the onClickInfoWindowListener, when infoWindow is clicked,then invoke JoinActivity
        mapFragment.getMap().setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
       
            @Override
            public void onInfoWindowClick(Marker marker) {
       
            // EventInfo eventInfo = eventMarkerMap.get(marker);
       
            Location userLocation = (currentLocation == null) ? lastLocation : currentLocation;
            if (userLocation == null) {
                //Toast.makeText(MapActivity.this,
                //        getResources().getString(R.string.current_location_unavailable), Toast.LENGTH_LONG).show();
                return;
            }

            Intent invokeJoinActivityIntent = new Intent(MapActivity.this, JoinNextActivity.class);
            invokeJoinActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            invokeJoinActivityIntent.putExtra(Application.INTENT_EXTRA_LOCATION, userLocation);
            startActivity(invokeJoinActivityIntent);
            }
        });


        /*
        // Set up the handler for the invite button click
        Button homeButton = (Button) findViewById(R.id.home);

        // Set up the handler for the invite button click
        Button localButton = (Button) findViewById(R.id.localinvitation);
        localButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Only allow posts if we have a location
                Location mLocation = (currentLocation == null) ? lastLocation : currentLocation;
                if (mLocation == null) {
                    Toast.makeText(MapActivity.this,
                            getResources().getString(R.string.current_location_unavailable), Toast.LENGTH_LONG).show();
                    return;
                }

                //set current latitude and longitude
                Application.setCurrentLatitude(Double.toString(mLocation.getLatitude()));
                Application.setCurrentLongitude(Double.toString(mLocation.getLongitude()));
        
                //Ozzie Zhang 10-29-2014 please change JoinActivity to InviteActivity, now only for test
                Intent intent = new Intent(MapActivity.this, LocalActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.putExtra(Application.INTENT_EXTRA_LOCATION, mLocation);
                startActivity(intent);

            }
        });
        

        // Set up the handler for the message button click
        Button messageButton = (Button) findViewById(R.id.message_button);
        messageButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
        
                //Ozzie Zhang 10-29-2014 please change JoinActivity to InviteActivity, now only for test
                Intent invokeMessageListIntent = new Intent(MapActivity.this, com.oneme.toplay.message.ui.LoginActivity.class);
                invokeMessageListIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(invokeMessageListIntent);

            }
        });

        // Set up the handler for the invite button click
        Button inviteButton = (Button) findViewById(R.id.inviteplay_button);
        inviteButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Only allow posts if we have a location
                Location mLocation = (currentLocation == null) ? lastLocation : currentLocation;
                if (mLocation == null) {
                    Toast.makeText(MapActivity.this,
                            getResources().getString(R.string.current_location_unavailable), Toast.LENGTH_LONG).show();
                    return;
                }

                //set current latitude and longitude
                Application.setCurrentLatitude(Double.toString(mLocation.getLatitude()));
                Application.setCurrentLongitude(Double.toString(mLocation.getLongitude()));

                //Ozzie Zhang 10-29-2014 please change JoinActivity to InviteActivity, now only for test
                Intent intent = new Intent(MapActivity.this, InviteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // intent.putExtra(Application.INTENT_EXTRA_LOCATION, mLocation);
                startActivity(intent);
            }
        });

        */



    }
    
    /*
     * Called when the Marker is clicked
     */
    
      //@Override
      //public void onMapClick(LatLng point) {
      //  tvLocInfo.setText("only for test");
      //    mapFragment.getMap().animateCamera(CameraUpdateFactory.newLatLng(point));
      //}
    
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
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MapActivity.this);
        
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, MapActivity.this, 0);
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
            updateZoom(myLatLng);
            hasSetUpInitialLocation = true;
        }
        // Update map radius indicator
        updateCircle(myLatLng);
        doMapQuery();
        
        //Ozzie Zhang 2014-11-16 disable list query
        // doListQuery();
    }
    
    /*
     * In response to a request to start updates, send a request to Location Services
     */
    private void startPeriodicUpdates() {
        locationClient.requestLocationUpdates(locationRequest, MapActivity.this);
    }
    
    /*
     * In response to a request to stop updates, send a request to Location Services
     */
    private void stopPeriodicUpdates() {
        locationClient.removeLocationUpdates(MapActivity.this);
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
            postsQueryAdapter.loadObjects();
        }
    }
    
    /*
     * Set up the query to update the map view
     */
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
        mapQuery.orderByDescending(AppConstant.OMEPARSECREATEDATKEY);
        mapQuery.setLimit(MAX_Invite_SEARCH_RESULTS);
        // Kick off the query in the background
        mapQuery.findInBackground(new FindCallback<Invite>() {
            @Override
            public void done(List<Invite> objects, ParseException e) {
                if (e != null) {
                    return;
                }
                /*
                 * Make sure we're processing results from
                 * the most recent update, in case there
                 * may be more than one in progress.
                 */
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
                            if (oldMarker.getSnippet() == null) {
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
                                         .icon(BitmapDescriptorFactory.fromResource(mmapPin[newMap++]));
                        } else {
                            markerOpts = markerOpts.title(invite.getText())
                                         .icon(BitmapDescriptorFactory.fromResource(R.drawable.ome_pin));
                  
                        }
                    }
                  
                    // check getMap for preventing null pointer crash
                    if (mapFragment.getMap() != null) {
                        // Add a new marker
                        Marker marker = mapFragment.getMap().addMarker(markerOpts);
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
     * Displays a circle on the map representing the search radius
     */
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
    
    /*
     * Zooms the map to show the area of interest based on the search radius
     */
    private void updateZoom(LatLng myLatLng) {
        // Get the bounds to zoom to
        LatLngBounds bounds = calculateBoundsWithCenter(myLatLng);
        // Zoom to the given bounds
        mapFragment.getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
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
        settingItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MapActivity.this, SettingActivity.class));
                return true;
            }
        });
    
        if (ParseUser.getCurrentUser() != null){
            MenuItem logoutItem = menu.add(getResources().getString(R.string.OMEPARSELOGOUT));
            //menu.findItem(R.id.action_login)
            logoutItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
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
            loginItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    startActivity(new Intent(MapActivity.this, LoginActivity.class));
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



}
