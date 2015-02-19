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

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import com.oneme.toplay.R;
import com.oneme.toplay.Application;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.base.third.GetOutputMediaFile;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.database.Sport;
import com.oneme.toplay.invite.InviteActivity;
import com.oneme.toplay.invite.InviteNextActivity;
import com.oneme.toplay.join.JoinActivity;
import com.oneme.toplay.MapActivity;
import com.oneme.toplay.MessageListActivity;
import com.oneme.toplay.base.FriendInfo;
import com.oneme.toplay.base.FriendRequest;
import com.oneme.toplay.base.Tuple;
import com.oneme.toplay.service.CoreService;
import com.oneme.toplay.MainActivity;
import com.oneme.toplay.LoginActivity;
import com.oneme.toplay.me.MeActivity;
import com.oneme.toplay.SearchActivity;


import com.oneme.toplay.weather.RemoteFetch;
import com.oneme.toplay.weather.WeatherActivity;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
//import com.shamanland.fab.FloatingActionButton;
//import com.shamanland.fab.ShowHideOnScroll;
import com.squareup.picasso.Picasso;
//import com.shamanland.fab.FloatingActionButton;
//import com.shamanland.fab.ShowHideOnScroll;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class LocalWithoutMapActivity extends ActionBarActivity implements LocationListener {

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
    private static final int MAX_POST_SEARCH_RESULTS = 100;

    // Maximum post search radius for map in kilometers
    private static final int MAX_POST_SEARCH_DISTANCE = 10;


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

    private String venuequery = null;

    private Menu menu;

    Handler handler;



    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    public LocalWithoutMapActivity(){
        handler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radius = Application.getSearchDistance();
        lastRadius = radius;

        setContentView(R.layout.ome_activity_local);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        String locationProvider = LocationManager.NETWORK_PROVIDER;
        lastLocation            = locationManager.getLastKnownLocation(locationProvider);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 10, this);


        // Set up a customized query
        ParseQueryAdapter.QueryFactory<Invite> factory =
                new ParseQueryAdapter.QueryFactory<Invite>() {
                    public ParseQuery<Invite> create() {
                        // update user newest location
                        Location myLocation = (currentLocation == null) ? lastLocation : currentLocation;
                        Application.setCurrentLatitude(Double.toString(myLocation.getLatitude()));
                        Application.setCurrentLongitude(Double.toString(myLocation.getLongitude()));

                        ParseQuery<Invite> query = Invite.getQuery();
                        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                        query.include("user");
                        query.orderByDescending("createdAt");
                        query.whereWithinKilometers("location", geoPointFromLocation(myLocation), radius
                                * METERS_PER_FEET / METERS_PER_KILOMETER);
                        query.setLimit(MAX_POST_SEARCH_RESULTS);
                        return query;
                    }
                };

        // Set up a progress dialog
       // final ProgressDialog listLoadDialog = new ProgressDialog(LocalWithoutMapActivity.this);
       // listLoadDialog.setMessage(getString(R.string.progress_local));
       // listLoadDialog.show();

        // get current geo point
        mGeoPoint = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

        // Set up the query adapter
        // Set up the query adapter
        inviteQueryAdapter = new ParseQueryAdapter<Invite>(LocalWithoutMapActivity.this, factory) {

            int listPinCount = 0;

            @Override
            public View getItemView(Invite invite, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.ome_activity_local_list, null);
                }

                ImageView avatarView        = (ImageView) view.findViewById(R.id.avatar_view);
                TextView usernameView       = (TextView) view.findViewById(R.id.username_view);
                //TextView contentView        = (TextView) view.findViewById(R.id.content_view);
                TextView venueaddressView   = (TextView) view.findViewById(R.id.local_venue_address);
                TextView playnumberView     = (TextView) view.findViewById(R.id.local_person_number);
                TextView playtimeView       = (TextView) view.findViewById(R.id.local_play_time);
                TextView distanceView       = (TextView) view.findViewById(R.id.local_distance_to_me);
                //TextView submittimeView     = (TextView) view.findViewById(R.id.duration);
                ImageView sporttypeiconView = (ImageView) view.findViewById(R.id.sport_type_icon);

                // Ozzie Zhang 2014-11-04 need add query for avatar icon for this user
                ParseUser user             = invite.getUser();
                ParseFile mavatarImageFile =  null;

                if (user != null) {
                    mavatarImageFile = user.getParseFile(AppConstant.OMEPARSEUSERICONKEY);
                }

                if (mavatarImageFile != null) {
                    Uri imageUri = Uri.parse(mavatarImageFile.getUrl());
                    Picasso.with(LocalWithoutMapActivity.this).load(imageUri.toString()).into(avatarView);
                }

                //contentView.setText(invite.getText());
                usernameView.setText(invite.getFromUsername());
                //submittimeView.setText(invite.getSubmitTime());
                venueaddressView.setText(invite.getCourt());
                playnumberView.setText(invite.getPlayerNumber());
                playtimeView.setText(invite.getPlayTime());

                // calculate distance between me and venue
                if (invite.getLocation() != null) {
                    Double mdistance = mGeoPoint.distanceInMilesTo(invite.getLocation());
                    DecimalFormat df = new DecimalFormat("##.00");
                    String distancenote = null;
                    if (mdistance < 1) {
                        mdistance = mdistance * AppConstant.OMEMETERSINAKILOMETER;
                        distancenote = getResources().getString(R.string.OMEPARSEMEMYVENUEDISTANCEMETERNOTE);
                    } else {
                        distancenote = getResources().getString(R.string.OMEPARSEMEMYVENUEDISTANCEKMNOTE);
                    }

                    String mdistancestring = df.format(mdistance) + " " + distancenote;
                    distanceView.setText(mdistancestring);
                }

                //distanceView


                String sporttypevalue = invite.getSportTypeValue();
                sporttypeiconView.setImageDrawable(getResources().getDrawable(Sport.msporticonarray[Integer.parseInt(sporttypevalue)]));

               // listLoadDialog.dismiss();

                return view;
            }
        };


        // Disable automatic loading when the adapter is attached to a view.
        inviteQueryAdapter.setAutoload(false);

        // Enable pagination, we'll not manage the query limit ourselves
        inviteQueryAdapter.setPaginationEnabled(true);

        // Attach the query adapter to the view
        localInvitationListView = (ListView) findViewById(R.id.local_listview);
        localInvitationListView.setAdapter(inviteQueryAdapter);




        // Set up the handler for an item's selection
        localInvitationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Invite invite    = inviteQueryAdapter.getItem(position);
                selectedInviteObjectId = invite.getObjectId();

                Location clickedItemUserLocation = (currentLocation == null) ? lastLocation : currentLocation;
                clickedItemUserLocation.setLatitude(invite.getLocation().getLatitude());
                clickedItemUserLocation.setLongitude(invite.getLocation().getLongitude());

                ParseUser user             = invite.getUser();
                ParseFile mavatarImageFile = null;

                if (user != null) {
                    mavatarImageFile = user.getParseFile(AppConstant.OMEPARSEUSERICONKEY);
                }

                // create tmp file for avatar
                File file                   = GetOutputMediaFile.newFile(AppConstant.OMEPARSEAVATARFILETYPEPNG);
                final File avatarTmpFile    = file;
                final Uri tmpAvatarImageUri = Uri.fromFile(avatarTmpFile);

                // fetch user avatar from parse cloud, save it tmp file
                if (mavatarImageFile != null && avatarTmpFile != null) {

                    mavatarImageFile.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, com.parse.ParseException pe) {
                            if (pe == null) {
                                try {
                                    // data has the bytes for the resume
                                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                   // avatarImageView.setImageBitmap(bmp);

                                    // output image to file
                                    FileOutputStream fos = new FileOutputStream(avatarTmpFile);
                                    bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                                    fos.close();
                                } catch (Exception e) {
                                    if (Application.APPDEBUG) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {

                            }

                        }
                    });
                }

                Intent invokeJoinActivityIntent = new Intent(LocalWithoutMapActivity.this, JoinActivity.class);

                if (tmpAvatarImageUri != null) {
                    // Add avatar Uri instance to an Intent
                    invokeJoinActivityIntent.putExtra(Application.INTENT_EXTRA_USERICONPATH, tmpAvatarImageUri.toString());
                }

                invokeJoinActivityIntent.putExtra(Application.INTENT_EXTRA_LOCATION, clickedItemUserLocation);
                putInviteToIntentExtra(invokeJoinActivityIntent, invite);

                // Log.d("LocalActivity", "selectedInviteObjectId " + selectedInviteObjectId);

                startActivity(invokeJoinActivityIntent);

                /*
                // Set up the handler for the invite button click
                Button inviteButton = (Button) findViewById(R.id.inviteplay_button);
                inviteButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Only allow posts if we have a location
                        Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
                        if (myLoc == null) {
                            Toast.makeText(LocalWithoutMapActivity.this,
                                    "Please try again after your location appears on the map.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        //Ozzie Zhang 10-29-2014 please change JoinActivity to InviteActivity, now only for test
                        Intent intent = new Intent(LocalWithoutMapActivity.this, InviteActivity.class);
                        intent.putExtra(Application.INTENT_EXTRA_LOCATION, myLoc);
                        startActivity(intent);

                    }
                });

                */
            }
        });

        /*
        // Set up the handler for the local button click
        Button localButton = (Button) findViewById(R.id.localinvitation);

        // Set up the handler for the message button click
        Button messageButton = (Button) findViewById(R.id.message_button);
        messageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Ozzie Zhang 10-29-2014 please change JoinActivity to InviteActivity, now only for test
                //  Intent invokeMessageListIntent = new Intent(LocalActivity.this, MessageListActivity.class);


                // Check username
                if (ParseUser.getCurrentUser() == null) {
                    Toast.makeText(LocalWithoutMapActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT), Toast.LENGTH_SHORT).show();

                    // jump to login activity
                    Intent invokeLoginActivityIntent = new Intent(LocalWithoutMapActivity.this, LoginActivity.class);
                    startActivity(invokeLoginActivityIntent);
                } else {

                    Intent invokeMessageListIntent = new Intent(LocalWithoutMapActivity.this, MainActivity.class);

                    invokeMessageListIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(invokeMessageListIntent);
                }
            }
        });

        */


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
    @Override
    public void onStop() {
        // If the client is connected
        super.onStop();
    }

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
                final JSONObject json = RemoteFetch.getJSON(LocalWithoutMapActivity.this, city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(LocalWithoutMapActivity.this,
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
        int id = actualId / 100;
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
            Log.e("SimpleWeather", "One or more fields not found in the JSON data  " + e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.ome_local_menu, menu);

        Typeface weatherFont;
        weatherFont = Typeface.createFromAsset(LocalWithoutMapActivity.this.getAssets(), "fonts/weather.ttf");

        TextView weathertv = new TextView(this);
        weathertv.setTypeface(weatherFont);
        updateWeatherData(weathertv, "beijing");
        weathertv.setPadding(5, 0, 5, 0);
        //tv.setTypeface(null, Typeface.BOLD);
        weathertv.setTextSize(20);
        weathertv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invokeWeatherActivityIntent = new Intent(LocalWithoutMapActivity.this, WeatherActivity.class);
                invokeWeatherActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(invokeWeatherActivityIntent);
            }
        });
        menu.add(0, 1001, 1, weathertv.getText()).setActionView(weathertv).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        if (ParseUser.getCurrentUser() != null) {
            MenuItem settingItem = menu.add(getResources().getString(R.string.meactivity_title));


            // menu.findItem(R.id.action_setting).
            settingItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    Intent invokeMeActivityIntent = new Intent(LocalWithoutMapActivity.this, MeActivity.class);
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
                    Intent stopCoreServiceIntent = new Intent(LocalWithoutMapActivity.this, CoreService.class);
                    LocalWithoutMapActivity.this.stopService(stopCoreServiceIntent);



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
                    startActivity(new Intent(LocalWithoutMapActivity.this, LoginActivity.class));
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
            //case R.id.action_weather:
            //    Intent invokeWeatherActivityIntent = new Intent(LocalWithoutMapActivity.this, WeatherActivity.class);
            //    invokeWeatherActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //    startActivity(invokeWeatherActivityIntent);
            //    return true;
            case R.id.action_search_venue:
                if (ParseUser.getCurrentUser() != null) {
                    onSearchRequested();
                } else {
                    Toast.makeText(LocalWithoutMapActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_map:
                Intent invokeMapActivityIntent = new Intent(LocalWithoutMapActivity.this, MapActivity.class);
                invokeMapActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(invokeMapActivityIntent);
                return true;
            case R.id.action_message:
                // Check username
                if (ParseUser.getCurrentUser() == null) {
                    Toast.makeText(LocalWithoutMapActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT), Toast.LENGTH_SHORT).show();
                    // jump to login activity
                    Intent invokeLoginActivityIntent = new Intent(LocalWithoutMapActivity.this, LoginActivity.class);
                    startActivity(invokeLoginActivityIntent);
                } else {
                    Intent invokeMessageIntent = new Intent(LocalWithoutMapActivity.this, MessageListActivity.class);//MainActivity.class);
                    invokeMessageIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(invokeMessageIntent);
                }
                return true;
            case R.id.action_invite:
                invokeInviteActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*
     * invoke Map activity intent.
     */
    private void invokeInviteActivity() {

        // check the user if login
        if (ParseUser.getCurrentUser() == null) {

            //Toast.makeText(JoinActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT),
            //        Toast.LENGTH_SHORT).show();

            // jump to login activity
            Intent invokeLoginActivityIntent = new Intent(LocalWithoutMapActivity.this, LoginActivity.class);
            startActivity(invokeLoginActivityIntent);
            //finish();
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
            Intent invokeInviteActivityIntent = new Intent(LocalWithoutMapActivity.this, InviteNextActivity.class);
            invokeInviteActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(invokeInviteActivityIntent);

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

