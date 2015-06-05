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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.adapter.VenueAdapter;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.VenueToIntentExtra;
import com.oneme.toplay.database.Venue;
import com.oneme.toplay.ui.BaseActivity;
import com.oneme.toplay.ui.widget.DrawShadowFrameLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class VenueNextActivity extends BaseActivity {

    private static final String TAG = "VenueNextActivity";

    private static final int MAX_VENUE_SEARCH_RESULTS = 20;

    private Bundle appData;

    private String venuequery;

    private ListView msearchresult;

    //public String data;
    public ArrayList<Venue> msuggest;
    public ArrayAdapter<Venue> madapter;

    private static ParseGeoPoint mGeoPoint;

    private String mnameKey = null;

    private MenuItem menuItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_venue_next);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
        //        | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(JoinNextActivity.this,
                //        LocalNextActivity.class)));
            }
        });

        mGeoPoint = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

        msuggest   = new ArrayList<Venue>();

        new getVenueRadarAutocomplete().execute(mGeoPoint);

        // fetch query result

        //new getVenueNameAutocomplete().execute("beijing");//venuequery);

        // setting search name list view
        msearchresult = (ListView) findViewById(R.id.search_venue_list);
        msearchresult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Venue venue = madapter.getItem(position);

                Intent invokeVenueDetailInfoActivityIntent = new Intent(VenueNextActivity.this, DetailInfoActivity.class);
                VenueToIntentExtra.putExtra(invokeVenueDetailInfoActivityIntent, venue);
                startActivity(invokeVenueDetailInfoActivityIntent);
            }
        });

        final EditText searchedittext = (EditText) findViewById(R.id.search_venue_content_text_view);
        searchedittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // if the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    return true;
                }
                return false;
            }
        });

        searchedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText  = s.toString();
                new getVenueNameAutocomplete().execute(newText);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    class getVenueNameAutocomplete extends AsyncTask<String,String,String> {
        private ProgressDialog venueLoadDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // venueLoadDialog = new ProgressDialog(VenueNextActivity.this);
           // venueLoadDialog.show();
        }

        @Override
        protected String doInBackground(String... key) {
            mnameKey = key[0];
            mnameKey = mnameKey.trim();
            msuggest = new ArrayList<Venue>();

            final int mlimit = MAX_VENUE_SEARCH_RESULTS + MAX_VENUE_SEARCH_RESULTS;

            runOnUiThread(new Runnable() {
                public void run() {

                    ParseQuery<Venue> query = Venue.getQuery();
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                    query.whereContains(AppConstant.OMETOPLAYVENUENAMEKEY, mnameKey);
                    query.setLimit(MAX_VENUE_SEARCH_RESULTS);
                    query.findInBackground(new FindCallback<Venue>() {
                        public void done(List<Venue> venueList, ParseException e) {
                            if (e == null) {
                                for (Venue venue : venueList) {
                                    Venue mvenue = new Venue();
                                    mvenue.setName(venue.getName());
                                    mvenue.setLevel(venue.getLevel());
                                    mvenue.setType(venue.getType());
                                    mvenue.setAddress(venue.getAddress());
                                    // mvenue.setLocation(venue.getLocation());
                                    mvenue.setPhone(venue.getPhone());
                                    mvenue.setCourtNumber(venue.getCourtNumber());
                                    mvenue.setLighted(venue.getLighted());
                                    mvenue.setIndoor(venue.getIndoor());
                                    mvenue.setPublic(venue.getPublic());
                                    mvenue.setObjectId(venue.getObjectId());

                                    if (msuggest.size() < mlimit) {
                                        msuggest.add(mvenue);
                                    }
                                }

                            } else {

                            }
                        }
                    });

                    ParseQuery<Venue> addressquery = Venue.getQuery();
                    addressquery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                    addressquery.whereContains(AppConstant.OMETOPLAYVENUEADDRESSKEY, mnameKey);
                    addressquery.setLimit(MAX_VENUE_SEARCH_RESULTS);
                    addressquery.findInBackground(new FindCallback<Venue>() {
                        public void done(List<Venue> venueList, ParseException e) {
                            if (e == null) {
                                for (Venue venue : venueList) {
                                    Venue mvenue = new Venue();
                                    mvenue.setName(venue.getName());
                                    mvenue.setLevel(venue.getLevel());
                                    mvenue.setType(venue.getType());
                                    mvenue.setAddress(venue.getAddress());
                                   // mvenue.setLocation(venue.getLocation());
                                    mvenue.setPhone(venue.getPhone());
                                    mvenue.setCourtNumber(venue.getCourtNumber());
                                    mvenue.setLighted(venue.getLighted());
                                    mvenue.setIndoor(venue.getIndoor());
                                    mvenue.setPublic(venue.getPublic());
                                    mvenue.setObjectId(venue.getObjectId());

                                    if (msuggest.size() < mlimit) {
                                        msuggest.add(mvenue);
                                    }
                                }

                                if (msuggest.size() >= 1) {
                                    madapter = new VenueAdapter(VenueNextActivity.this, msuggest);
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
           // venueLoadDialog.dismiss();
        }

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

            final int mlimit = MAX_VENUE_SEARCH_RESULTS + MAX_VENUE_SEARCH_RESULTS;

            runOnUiThread(new Runnable() {
                public void run() {

                    ParseQuery<Venue> query = Venue.getQuery();
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                    query.whereNear(AppConstant.OMETOPLAYVENUELOCATIONKEY, geoPoint);
                    query.setLimit(MAX_VENUE_SEARCH_RESULTS);
                    query.findInBackground(new FindCallback<Venue>() {
                        public void done(List<Venue> venueList, ParseException e) {
                            if (e == null) {
                                for (Venue venue : venueList) {
                                    Venue mvenue = new Venue();
                                    mvenue.setName(venue.getName());
                                    mvenue.setLevel(venue.getLevel());
                                    mvenue.setType(venue.getType());
                                    mvenue.setAddress(venue.getAddress());
                                    // mvenue.setLocation(venue.getLocation());
                                    mvenue.setPhone(venue.getPhone());
                                    mvenue.setCourtNumber(venue.getCourtNumber());
                                    mvenue.setLighted(venue.getLighted());
                                    mvenue.setIndoor(venue.getIndoor());
                                    mvenue.setPublic(venue.getPublic());
                                    mvenue.setObjectId(venue.getObjectId());

                                    if (msuggest.size() < mlimit) {
                                        msuggest.add(mvenue);
                                    }
                                }

                                if (msuggest.size() >= 1) {
                                    madapter = new VenueAdapter(VenueNextActivity.this, msuggest);
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
