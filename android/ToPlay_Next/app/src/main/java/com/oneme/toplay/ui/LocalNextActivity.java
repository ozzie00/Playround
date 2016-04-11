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

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oneme.toplay.Application;
import com.oneme.toplay.LoginActivity;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.InviteToIntentExtra;
import com.oneme.toplay.base.LoadImageFromParseCloud;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.base.third.Common;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.database.Sport;
import com.oneme.toplay.invite.InviteNextActivity;
import com.oneme.toplay.join.JoinNextActivity;
import com.oneme.toplay.me.MeActivity;
import com.oneme.toplay.service.CoreService;
import com.oneme.toplay.ui.widget.DrawShadowFrameLayout;
import com.oneme.toplay.weather.RemoteFetch;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Date;

//import com.shamanland.fab.FloatingActionButton;
//import com.shamanland.fab.ShowHideOnScroll;
//import com.shamanland.fab.FloatingActionButton;
//import com.shamanland.fab.ShowHideOnScroll;


public class LocalNextActivity extends BaseActivity implements LocationListener {

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

    //Ozzie Zhang 2014-11-06 change interval value
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
    private static final int MAX_POST_SEARCH_RESULTS = 200;

    // Maximum post search radius for map in kilometers
    private static final int MAX_POST_SEARCH_DISTANCE = 10;

    // Minimum interval time
    private static final int MIN_INTERVAL_TIME = 3000;

    // Minimum distance for update location
    private static final int MIN_DISTANCE_METER = 10;



    private DrawShadowFrameLayout mDrawShadowFrameLayout;

    // Fields for the map radius in feet
    private float radius;
    private float lastRadius;

    // Fields for helping process map and location changes
    private int mostRecentMapUpdate;
    private boolean hasSetUpInitialLocation;
    private String selectedInviteObjectId;
    private Location lastLocation;
    private Location currentLocation;

    private ProgressBar progressBar;

    private LocationManager locationManager;

    // Adapter for the Parse query
    private ParseQueryAdapter<Invite> inviteQueryAdapter;

     // user last location
    private ParseGeoPoint userLastLocation;


    private ListView localInvitationListView;

    private static ParseGeoPoint mGeoPoint;

    private int mcount        = 0;

    private int hourpart      = 3;

    private String venuequery = null;

    private ParseUser muser   = ParseUser.getCurrentUser();

    private String musername  = null;

    private Menu menu;

    Handler handler;

    public LocalNextActivity(){
        handler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radius = 3*Application.getSearchDistance();
        lastRadius = radius;

        setContentView(R.layout.ome_activity_navdrawer_local);

        mDrawShadowFrameLayout = (DrawShadowFrameLayout) findViewById(R.id.main_content);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        String locationProvider = LocationManager.PASSIVE_PROVIDER;
        lastLocation            = locationManager.getLastKnownLocation(locationProvider);

        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MIN_INTERVAL_TIME, MIN_DISTANCE_METER, this);

        // Set up a customized query
        ParseQueryAdapter.QueryFactory<Invite> factory =
                new ParseQueryAdapter.QueryFactory<Invite>() {
                    public ParseQuery<Invite> create() {
                        // update user newest location
                        Location myLocation = (currentLocation == null) ? lastLocation : currentLocation;

                        // check mylocation is null
                        if (myLocation != null) {
                            Application.setCurrentLatitude(String.valueOf(myLocation.getLatitude()));
                            Application.setCurrentLongitude(String.valueOf(myLocation.getLongitude()));
                        }

                        ParseQuery<Invite> query = Invite.getQuery();
                        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                        query.include(AppConstant.OMEPARSEUSERKEY);
                        query.orderByDescending(AppConstant.OMEPARSECREATEDATKEY);
                        //query.whereWithinKilometers(AppConstant.OMEPARSELOCATIONKEY, geoPointFromLocation(myLocation), radius
                        //        * METERS_PER_FEET / METERS_PER_KILOMETER);
                        query.setLimit(MAX_POST_SEARCH_RESULTS);

                        return query;
                    }
                };


        // Set up a progress dialog
        final ProgressDialog listLoadDialog = new ProgressDialog(LocalNextActivity.this);
        listLoadDialog.setMessage(getString(R.string.progress_local));
        listLoadDialog.show();

        Double mCurrentLatitude  = Double.valueOf(Application.getCurrentLatitude());
        Double mCurrentLongitude = Double.valueOf(Application.getCurrentLongitude());

        // get current geo point
        if ((mCurrentLatitude <= AppConstant.OMEPARSEMAXLATITUDE && mCurrentLatitude >= AppConstant.OMEPARSEMINLATITUDE)
                && (mCurrentLongitude <= AppConstant.OMEPARSEMAXLONGITUDE && mCurrentLongitude >= AppConstant.OMEPARSEMINLONGITUDE)){
            mGeoPoint = new ParseGeoPoint(mCurrentLatitude, mCurrentLongitude);
        } else {
            mGeoPoint = new ParseGeoPoint(Application.CURRENT_LATITUDE, Application.CURRENT_LONGITUDE);
        }

        // Set up the query adapter
        // Set up the query adapter
        inviteQueryAdapter = new ParseQueryAdapter<Invite>(LocalNextActivity.this, factory) {

            int listPinCount = 0;

            @Override
            public View getItemView(Invite invite, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.ome_activity_local_list, null);
                }

                //ImageView workoutimageView  = (ImageView) view.findViewById(R.id.local_workoutimage_view);
                ImageView avatarView        = (ImageView) view.findViewById(R.id.local_avatar_view);
                TextView usernameView       = (TextView) view.findViewById(R.id.local_username_view);
                TextView workoutnameView    = (TextView) view.findViewById(R.id.local_workoutname_view);
                TextView venueaddressView   = (TextView) view.findViewById(R.id.local_venue_address);
                TextView playtimeView       = (TextView) view.findViewById(R.id.local_play_time);
                TextView distanceView       = (TextView) view.findViewById(R.id.local_distance_to_me);
                ImageView sporttypeiconView = (ImageView) view.findViewById(R.id.local_sport_type_icon);

                LoadImageFromParseCloud.getAvatar(LocalNextActivity.this, invite.getUser(), avatarView);

                //LoadImageFromParseCloud.getworkoutImage(LocalNextActivity.this, invite, workoutimageView);


                String mplaytime = invite.getPlayTime();
                usernameView.setText(invite.getFromUsername());
                workoutnameView.setText(invite.getWorkoutName());
                venueaddressView.setText(invite.getCourt());

                if (mplaytime.contains(AppConstant.OMEPARSESLASHSTRING)) {
                    // the old version time format contains slash
                    playtimeView.setText(invite.getPlayTime());
                } else {
                    // the new version time format contains space
                    // reformat the play time, original format is MMM dd yyyy HH:mm
                    String[] mpart   = mplaytime.split(AppConstant.OMEPARSESPACESTRING);

                    // check if mpart[0] is integer, because old version can be "mar", this will
                    // lead to app crash
                    boolean bmonth = Common.isInteger(mpart[0]);
                    int imonth     = 0;

                    if (bmonth) {
                        imonth = Integer.parseInt(mpart[0]);
                    } else {
                        // set default value is 1
                        imonth = 1;
                    }

                    // MMM's first month january is 0, then when show it, need add 1
                    String mmonth    = Integer.toString(imonth + 1);
                    String mday      = mpart[1];
                    String mhour     = mpart[hourpart];
                    playtimeView.setText(mday + AppConstant.OMEPARSESLASHSTRING + mmonth + AppConstant.OMEPARSESPACESTRING + mhour);
                }

                // calculate distance between me and venue
                if (invite.getLocation() != null) {
                    Double mdistance = mGeoPoint.distanceInMilesTo(invite.getLocation());
                    DecimalFormat df = new DecimalFormat(AppConstant.OMEPARSEDISTANCEFORMATSTRING);
                    String distancenote = null;
                    if (mdistance < 1) {
                        mdistance = mdistance * AppConstant.OMEMETERSINAKILOMETER;
                        distancenote = getResources().getString(R.string.OMEPARSEMEMYVENUEDISTANCEMETERNOTE);
                    } else {
                        distancenote = getResources().getString(R.string.OMEPARSEMEMYVENUEDISTANCEKMNOTE);
                    }

                    String mdistancestring = df.format(mdistance) + AppConstant.OMEPARSESPACESTRING + distancenote;
                    distanceView.setText(mdistancestring);
                }

                String sporttypevalue = invite.getSportTypeValue();
                sporttypeiconView.setImageDrawable(getResources().getDrawable(Sport.msporticonarray[Integer.parseInt(sporttypevalue)]));

                listLoadDialog.dismiss();

                return view;
            }
        };


        // Disable automatic loading when the adapter is attached to a view.
        inviteQueryAdapter.setAutoload(true);

        // Enable pagination, we'll not manage the query limit ourselves
        inviteQueryAdapter.setPaginationEnabled(false);

        // Attach the query adapter to the view
        localInvitationListView = (ListView) findViewById(R.id.local_listview);
        localInvitationListView.setAdapter(inviteQueryAdapter);


        // Set up the handler for an item's selection
        localInvitationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Invite invite = inviteQueryAdapter.getItem(position);
                selectedInviteObjectId = invite.getObjectId();

                //Location clickedItemUserLocation = (currentLocation == null) ? lastLocation : currentLocation;
                //clickedItemUserLocation.setLatitude(invite.getLocation().getLatitude());
                //clickedItemUserLocation.setLongitude(invite.getLocation().getLongitude());

                Intent invokeJoinActivityIntent = new Intent(LocalNextActivity.this, JoinNextActivity.class);

                //invokeJoinActivityIntent.putExtra(Application.INTENT_EXTRA_LOCATION, clickedItemUserLocation);
                InviteToIntentExtra.putExtra(invokeJoinActivityIntent, invite);

                // Log.d("LocalActivity", "selectedInviteObjectId " + selectedInviteObjectId);

                startActivity(invokeJoinActivityIntent);


            }
        });


        // add invitation
        FloatingActionButton minviteFab = (FloatingActionButton) findViewById(R.id.local_fab);
        minviteFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                invokeInviteActivity();
            }
        });


    }


    //  @Override
    //  public void onLocationChanged(Location location) {

    //      String str = "Latitude: "+location.getLatitude()+"
    //      Longitude: "+location.getLongitude();

    //      Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show();
    //  }

    @Override
    public void onProviderDisabled(String provider) {

        /******** Called when User off Gps *********/

        Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        /******** Called when User on Gps  *********/

        Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    /*
     * Called when the Activity is no longer visible at all. Stop updates and disconnect.
     */
  //  @Override
  //  public void onStop() {
        // If the client is connected
      //  super.onStop();
  //  }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */
    @Override
    public void onStart() {
        super.onStart();
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
            //     LatLng myLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            // If the search distance preference has been changed, move
            // map to new bounds.
            if (lastRadius != radius) {
                //  updateZoom(myLatLng);
            }
            // Update the circle map
            //updateCircle(myLatLng);
        }
        // Save the current radius
        lastRadius = radius;
        // Query for the latest data to update the views.
        // Ozzie Zhang 2014-11-16 disable map query
        // doMapQuery();
        doListQuery();



      //  FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.local_fab);
      //  fab.setSize(FloatingActionButton.SIZE_NORMAL);
      //  fab.setColor(Color.parseColor("#" + AppConstant.OMETOPLAYOLYMPICGREEN));
      //  fab.initBackground();
      //  fab.setImageResource(R.drawable.mic);
      //  localInvitationListView.setOnTouchListener(new ShowHideOnScroll(fab));
      //  fab.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View v) {
      //          Toast.makeText(getBaseContext(), "voice turned on ", Toast.LENGTH_LONG).show();
      //      }
      //  });


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
        //  LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (!hasSetUpInitialLocation) {
            // Zoom to the current location.
            //   updateZoom(myLatLng);
            hasSetUpInitialLocation = true;
        }
        // Update map radius indicator
        //updateCircle(myLatLng);

        //Ozzie Zhang 2014-11-16 disable map query
        //doMapQuery();
        doListQuery();
    }

    /*
     * In response to a request to start updates, send a request to Location Services
     */
    private void startPeriodicUpdates() {

    }

    /*
     * In response to a request to stop updates, send a request to Location Services
     */
    private void stopPeriodicUpdates() {
    }

    /*
     * Get the current location
     */
    private Location getLocation() {
        // If Google Play Services is available
        //if (servicesConnected()) {
        // Get the current location
        //} else {
        return null;
        // }
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
     * Helper method to get the Parse GEO point representation of a location
     */
    private ParseGeoPoint geoPointFromLocation(Location loc) {
        return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
    }


    private void updateWeatherData(final TextView weatherIcon, final String city){
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetch.getJSON(LocalNextActivity.this, city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(LocalNextActivity.this,
                                    getResources().getString(R.string.OMWPARSEWEATHERPLACENOTFOUND),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(weatherIcon, json);
                        }
                    });
                }
            }
        }.start();
    }

    private void newsetWeatherIcon(TextView weatherIcon, int actualId, int day){
        int id      = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            // if(currentTime>=sunrise && currentTime<sunset) {
            icon = getResources().getString(R.string.OMEPARSEWEATHERSUNNY);
            //} else {
            //    icon = getActivity().getString(R.string.weather_clear_night);
            //}
        } else {
            switch(id) {
                case 2 : icon = getResources().getString(R.string.OMEPARSEWEATHERTHUNDER);
                    break;
                case 3 : icon = getResources().getString(R.string.OMEPARSEWEATHERDRIZZLE);
                    break;
                case 7 : icon = getResources().getString(R.string.OMEPARSEWEATHERFOGGY);
                    break;
                case 8 : icon = getResources().getString(R.string.OMEPARSEWEATHERCLOUDY);
                    break;
                case 6 : icon = getResources().getString(R.string.OMEPARSEWEATHERSNOWY);
                    break;
                case 5 : icon = getResources().getString(R.string.OMEPARSEWEATHERRAINY);
                    break;
            }
        }
        switch(day) {
            case 0 : weatherIcon.setText(icon);
                break;
        }
    }

    private void renderWeather(TextView weatherIcon, JSONObject json){
        try {
            JSONObject detailsindex = json.getJSONArray("list").getJSONObject(0);
            JSONObject details      = detailsindex.getJSONArray("weather").getJSONObject(0);
            newsetWeatherIcon(weatherIcon, details.getInt("id"), 0);
        }catch(Exception e){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.ome_local_menu, menu);

        /*
        Typeface weatherFont;
        weatherFont = Typeface.createFromAsset(LocalNextActivity.this.getAssets(), "fonts/weather.ttf");

        TextView weathertv = new TextView(this);
        weathertv.setTypeface(weatherFont);
        updateWeatherData(weathertv, "beijing");
        weathertv.setPadding(5, 0, 5, 0);
        //tv.setTypeface(null, Typeface.BOLD);
        weathertv.setTextSize(20);
        weathertv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invokeWeatherActivityIntent = new Intent(LocalNextActivity.this, WeatherActivity.class);
                invokeWeatherActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(invokeWeatherActivityIntent);
            }
        });
        menu.add(0, 1001, 1, weathertv.getText()).setActionView(weathertv).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        */

        if (muser != null) {
            MenuItem settingItem = menu.add(getResources().getString(R.string.meactivity_title));


            // menu.findItem(R.id.action_setting).
            settingItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    Intent invokeMeActivityIntent = new Intent(LocalNextActivity.this, MeActivity.class);
                    invokeMeActivityIntent.putExtra(AppConstant.OMEPARSEPARENTCLASSNAME, AppConstant.OMEPARSELOCALWITHOUTMAPACTIVITYCLASS);
                    startActivity(invokeMeActivityIntent);


                    // intent.putExtra("ParentClassName","A");
                    // startActivity(new Intent(LocalWithoutMapActivity.this, MeActivity.class));
                    return true;
                }
            });

        }

        if (muser != null){
            MenuItem logoutItem = menu.add(getResources().getString(R.string.OMEPARSELOGOUT));
            //menu.findItem(R.id.action_login)
            logoutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    // update user last time and last location

                    //get point according to  current latitude and longitude
                    userLastLocation = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

                    if (muser != null) {
                        muser.put(AppConstant.OMEPARSEUSERLASTTIMEKEY, Time.currentTime());
                        muser.put(AppConstant.OMEPARSEUSERLASTLOCATIONKEY, userLastLocation);

                        muser.saveInBackground();

                    }

                    // preference logout
                    SharedPreferences preferences   = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("loggedin", false);
                    editor.apply();


                    // Stop the Core Service
                    Intent stopCoreServiceIntent = new Intent(LocalNextActivity.this, CoreService.class);
                    LocalNextActivity.this.stopService(stopCoreServiceIntent);



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
                    startActivity(new Intent(LocalNextActivity.this, LoginActivity.class));
                    finish();
                    return true;
                }
            });

        }

    return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();

        appData.putBoolean(Application.INTENT_EXTRA_VENUESEARCH, true);
       // appData.putString(Application.INTENT_EXTRA_VENUESEARCH, venuequery);
        startSearch(null, false, appData, false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            //case R.id.action_search_venue:
            //    onSearchRequested();
            //    return true;
            //case R.id.action_map:
            //    Intent invokeMapActivityIntent = new Intent(LocalNextActivity.this, MapActivity.class);
            //    invokeMapActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //    startActivity(invokeMapActivityIntent);
            //    return true;
            //case R.id.action_message:
                // Check username
            //    Intent invokeMessageIntent = new Intent(LocalNextActivity.this, MessageListActivity.class);//MainActivity.class);
            //    invokeMessageIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //    startActivity(invokeMessageIntent);
            //    return true;
            //case R.id.action_invite:
            //    invokeInviteActivity();
            //    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*
     * invoke Map activity intent.
     */
    private void invokeInviteActivity() {

            // Only allow posts if we have a location
            Location mLocation = (currentLocation == null) ? lastLocation : currentLocation;
            if (mLocation == null) {
                Toast.makeText(LocalNextActivity.this,
                        getResources().getString(R.string.current_location_unavailable), Toast.LENGTH_LONG).show();
                return;
            }

            //set current latitude and longitude
            Application.setCurrentLatitude(String.valueOf(mLocation.getLatitude()));
            Application.setCurrentLongitude(String.valueOf(mLocation.getLongitude()));

            //Ozzie Zhang 10-29-2014 please change JoinActivity to InviteActivity, now only for test
            Intent invokeInviteActivityIntent = new Intent(LocalNextActivity.this, InviteNextActivity.class);
            invokeInviteActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(invokeInviteActivityIntent);


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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_EXPLORE;
    }

    @Override
    protected void onActionBarAutoShowOrHide(boolean shown) {
        super.onActionBarAutoShowOrHide(shown);
        mDrawShadowFrameLayout.setShadowVisible(shown, shown);
    }
}

