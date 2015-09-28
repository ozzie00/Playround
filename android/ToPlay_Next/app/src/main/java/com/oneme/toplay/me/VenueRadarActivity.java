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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.adapter.VenueAdapter;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.CopyVenue;
import com.oneme.toplay.base.VenueToIntentExtra;
import com.oneme.toplay.database.Venue;
import com.oneme.toplay.ui.BaseActivity;
import com.oneme.toplay.venue.DetailInfoActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class VenueRadarActivity extends BaseActivity {

    private static final String TAG = "VenueRadarActivity";

    private static final int MAX_VENUE_SEARCH_RESULTS = 20;

    private static ParseGeoPoint mGeoPoint;

    private ListView msearchresult;

    //public String data;
    public ArrayList<Venue> msuggest;
    public ArrayAdapter<Venue> madapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FriendRadarActivity use the searchOtherActivity layout
        setContentView(R.layout.ome_activity_venue_radar);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(VenueRadarActivity.this,
                //        MeActivity.class)));
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setTitle(getResources().getString(R.string.OMEPARSEADDCONTACTSEARCHDISPLAYRESULTTITLE));

        mGeoPoint = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

        msuggest  = new ArrayList<Venue>();

        new getVenueRadarAutocomplete().execute(mGeoPoint);

        // setting search name list view
        msearchresult = (ListView) findViewById(R.id.venue_radar_list);
        msearchresult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Venue venue = madapter.getItem(position);
                Intent invokeVenueDetailInfoActivityIntent = new Intent(VenueRadarActivity.this, DetailInfoActivity.class);
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
            venueLoadDialog = new ProgressDialog(VenueRadarActivity.this);
            venueLoadDialog.show();
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
                                    CopyVenue.Done(venue, mvenue);

                                    if (msuggest.size() < mlimit) {
                                        msuggest.add(mvenue);
                                    }
                                }

                                if (msuggest.size() >= 1) {
                                    madapter = new VenueAdapter(VenueRadarActivity.this, msuggest);
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
            venueLoadDialog.dismiss();
        }

    }

}
