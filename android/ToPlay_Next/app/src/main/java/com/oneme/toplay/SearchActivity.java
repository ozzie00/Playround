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

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oneme.toplay.addfriend.ContactProfileActivity;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.Sport;
import com.oneme.toplay.database.Venue;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;

public class SearchActivity extends ActionBarActivity {


    private static final String TAG = "SearchActivity";

    private static final int MAX_VENUE_SEARCH_RESULTS = 10;

    private Bundle appData;

    private static ParseGeoPoint mGeoPoint;

    private String venuequery;

    private String[] venuequeryarray;

    // Adapter for the Parse query
    private ParseQueryAdapter<Venue> venueQueryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_search_venue);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
       // getSupportActionBar().setDisplayShowTitleEnabled(false);
       // getSupportActionBar().setTitle(getResources().getString(R.string.OMEPARSEADDCONTACTSEARCHDISPLAYRESULTTITLE));

        appData = getIntent().getBundleExtra(SearchManager.APP_DATA);
        if (appData != null) {
            boolean jargon = appData.getBoolean(Application.INTENT_EXTRA_VENUESEARCH);

        }

        venuequery      = getIntent().getStringExtra(SearchManager.QUERY);

        //SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
        //        VenueSuggestionProvider.AUTHORITY, VenueSuggestionProvider.MODE);
        //suggestions.saveRecentQuery(venuequery, null);

        //venuequeryarray = venuequery.split("\\s");

        //android.util.Log.d("SearchActivity ", "query " + venuequeryarray);

        mGeoPoint = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

        // Set up a customized query
        ParseQueryAdapter.QueryFactory<Venue> factory = new ParseQueryAdapter.QueryFactory<Venue>() {
            public ParseQuery<Venue> create() {
                ParseQuery<Venue> queryname = Venue.getQuery();
                queryname.whereContains(AppConstant.OMETOPLAYVENUENAMEKEY, venuequery);

                ParseQuery<Venue> queryaddress = Venue.getQuery();
                queryaddress.whereContains(AppConstant.OMETOPLAYVENUEADDRESSKEY, venuequery);

                List<ParseQuery<Venue>> query = new ArrayList<ParseQuery<Venue>>();
                query.add(queryname);
                query.add(queryaddress);

                ParseQuery<Venue> mainQuery = ParseQuery.or(query);
                mainQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                mainQuery.setLimit(MAX_VENUE_SEARCH_RESULTS);

                return mainQuery;
            }
        };

        // Set up the query adapter
        venueQueryAdapter = new ParseQueryAdapter<Venue>(SearchActivity.this, factory) {
            @Override
            public View getItemView(Venue venue, View view, ViewGroup parent) {

                if (view == null) {
                    view = View.inflate(getContext(), R.layout.ome_activity_search_venue_item, null);
                }

                final ImageButton avatarButton  = (ImageButton) view.findViewById(R.id.search_venue_avatar_button);
                TextView nameView           = (TextView) view.findViewById(R.id.search_venue_name_view);
                TextView addressView        = (TextView) view.findViewById(R.id.search_venue_address_view);
               // TextView distanceView       = (TextView) view.findViewById(R.id.search_venue_distance_view);

                /*
                // calculate distance between me and venue
                Double  mdistance   = mGeoPoint.distanceInMilesTo(venue.getLocation());
                DecimalFormat df    = new DecimalFormat("##.00");
                String distancenote = null;
                if (mdistance < 1) {
                    mdistance    = mdistance * AppConstant.OMEMETERSINAKILOMETER;
                    distancenote =  getResources().getString(R.string.OMEPARSEMEMYVENUEDISTANCEMETERNOTE);
                } else {
                    distancenote = getResources().getString(R.string.OMEPARSEMEMYVENUEDISTANCEKMNOTE);
                }

                String mdistancestring = df.format(mdistance) + " " + distancenote ;
                */

                // remember call phone number
                final String phonenumber = venue.getPhone();

                // use corresponding venue icon according to sport type
                String sporttype = venue.getType();
                int index        = Sport.msportarraylist.indexOf(sporttype);

                avatarButton.setImageResource(Sport.msporticonarray[index]);
                nameView.setText(venue.getName());
                addressView.setText(venue.getAddress());
                //distanceView.setText(mdistancestring);

                // set callButton when phone number is not null
                final View callButton  = (ImageButton) view.findViewById(R.id.search_venue_call_button);
                if (venue.getPhone().length() > AppConstant.OMEPHONEMINIMUMLENGTH){
                    callButton.setVisibility(View.VISIBLE);
                } else {
                    callButton.setVisibility(View.GONE);
                }

                callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent invokePhoneCall = new Intent(Intent.ACTION_CALL);
                        invokePhoneCall.setData(Uri.parse(AppConstant.OMEPARSEINVOKECALLPHONE + phonenumber));
                        startActivity(invokePhoneCall);
                    }
                });

                return view;
            }
        };

        // Disable automatic loading when the adapter is attached to a view.
        venueQueryAdapter.setAutoload(true);

        // disable pagination, we'll manage the query limit ourselves
        venueQueryAdapter.setPaginationEnabled(false);

        // Attach the query adapter to the view
        ListView userListView = (ListView) findViewById(R.id.search_venue_list);
        userListView.setAdapter(venueQueryAdapter);

        // Set up the handler for an item's selection
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Venue item     = venueQueryAdapter.getItem(position);
                //  String mUsername     = item.getUsername();
                // String mOMEID        = item.getString(AppConstant.OMEPARSEUSEROMEIDKEY);

                //  Intent invokeSearchContactProfileActivityIntent = new Intent(getBaseContext(), ContactProfileActivity.class);
                //  invokeSearchContactProfileActivityIntent.putExtra(Application.INTENT_EXTRA_USERNAME, mUsername);
                //  invokeSearchContactProfileActivityIntent.putExtra(Application.INTENT_EXTRA_USEROMEID, mOMEID);
                //  invokeSearchContactProfileActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //  startActivity(invokeSearchContactProfileActivityIntent);
            }
        });
    }


}
