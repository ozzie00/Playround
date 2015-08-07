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

package com.oneme.toplay.venue;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.adapter.SportTypeAdapter;
import com.oneme.toplay.adapter.VenueAdapter;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.CopyVenue;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.base.VenueToIntentExtra;
import com.oneme.toplay.database.Group;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.database.InviteScore;
import com.oneme.toplay.database.Sport;
import com.oneme.toplay.database.Venue;
import com.oneme.toplay.invite.SearchActivity;
import com.oneme.toplay.ui.BaseActivity;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public final class PrimeBusinessActivity extends BaseActivity  {

    private static final String TAG           = "PrimeBusinessActivity";


    private final Context context = PrimeBusinessActivity.this;

    private ParseGeoPoint mGeoPoint;
    private ParseUser muser        = ParseUser.getCurrentUser();
    private String mworkoutname    = null;
    private String msporttype      = null;

    private static final int MAX_VENUE_SEARCH_RESULTS = 200;

    private ListView msearchresult;

    //public String data;
    public ArrayList<Venue> msuggest;
    public ArrayAdapter<Venue> madapter;

    // Add spinner for sport type
    String[] msportarray = {

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_venue_prime);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle(null);
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(DetailInfoActivity.this,
                //        LocalNextActivity.class)));
            }
        });

        // Note: msportarray items are correponding to msporticonarray of Sport Class
        msportarray = getResources().getStringArray(R.array.sport_type_array);

        //get point according to  current latitude and longitude
        mGeoPoint = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

        msuggest   = new ArrayList<Venue>();

        new getVenueRadarAutocomplete().execute(mGeoPoint);

        // choose sport
        Spinner msportspinner              = (Spinner)findViewById(R.id.spinner_nav);
        SportTypeAdapter msportTypeAdapter = new SportTypeAdapter(PrimeBusinessActivity.this, R.layout.ome_sport_row, msportarray, Sport.msporticonarray);
        msportspinner.setAdapter(msportTypeAdapter);

        msportspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                msporttype = Sport.msportarraylist.get(pos);
                new getVenueTypeAutocomplete().execute(mGeoPoint);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // setting search name list view
        msearchresult = (ListView) findViewById(R.id.prime_venue_list);
        msearchresult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Venue venue = madapter.getItem(position);

                Intent invokeVenueDetailInfoActivityIntent = new Intent(PrimeBusinessActivity.this, DetailInfoActivity.class);
                VenueToIntentExtra.putExtra(invokeVenueDetailInfoActivityIntent, venue);
                startActivity(invokeVenueDetailInfoActivityIntent);
            }
        });

    }

    class getVenueRadarAutocomplete extends AsyncTask<ParseGeoPoint,String,String> {
        private ProgressDialog venueLoadDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(ParseGeoPoint...key) {
            final ParseGeoPoint geoPoint = key[0];
            msuggest = new ArrayList<Venue>();

            runOnUiThread(new Runnable() {
                public void run() {

                    ParseQuery<Venue> query = Venue.getQuery();
                    query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                    query.whereNear(AppConstant.OMETOPLAYVENUELOCATIONKEY, geoPoint);
                    query.whereEqualTo(AppConstant.OMETOPLAYVENUEBUSINESSKEY, AppConstant.OMETOPLAYVENUEBUSINESSPRIME);
                    query.setLimit(MAX_VENUE_SEARCH_RESULTS);
                    query.findInBackground(new FindCallback<Venue>() {
                        public void done(List<Venue> venueList, ParseException e) {
                            if (e == null) {
                                for (Venue venue : venueList) {
                                    Venue mvenue = new Venue();
                                    CopyVenue.Done(venue,mvenue);

                                    if (msuggest.size() < MAX_VENUE_SEARCH_RESULTS) {
                                        msuggest.add(mvenue);
                                    }
                                }

                                if (msuggest.size() >= 1) {
                                    madapter = new VenueAdapter(PrimeBusinessActivity.this, msuggest);
                                    msearchresult.setAdapter(madapter);
                                    madapter.notifyDataSetChanged();
                                }


                            } else {

                            }
                        }
                    });

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }

    class getVenueTypeAutocomplete extends AsyncTask<ParseGeoPoint,String,String> {
        private ProgressDialog venueLoadDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(ParseGeoPoint...key) {
            final ParseGeoPoint geoPoint = key[0];
            msuggest = new ArrayList<Venue>();

            runOnUiThread(new Runnable() {
                public void run() {

                    ParseQuery<Venue> query = Venue.getQuery();
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                    query.whereNear(AppConstant.OMETOPLAYVENUELOCATIONKEY, geoPoint);
                    query.whereEqualTo(AppConstant.OMETOPLAYVENUEBUSINESSKEY, AppConstant.OMETOPLAYVENUEBUSINESSPRIME);
                    query.whereEqualTo(AppConstant.OMETOPLAYVENUETYPEKEY, msporttype);
                    query.setLimit(MAX_VENUE_SEARCH_RESULTS);
                    query.findInBackground(new FindCallback<Venue>() {
                        public void done(List<Venue> venueList, ParseException e) {
                            if (e == null) {
                                for (Venue venue : venueList) {
                                    Venue mvenue = new Venue();
                                    CopyVenue.Done(venue,mvenue);

                                    if (msuggest.size() < MAX_VENUE_SEARCH_RESULTS) {
                                        msuggest.add(mvenue);
                                    }
                                }

                                if (msuggest.size() >= 1) {
                                    madapter = new VenueAdapter(PrimeBusinessActivity.this, msuggest);
                                    msearchresult.setAdapter(madapter);
                                    madapter.notifyDataSetChanged();
                                }


                            } else {

                            }
                        }
                    });

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }


}
