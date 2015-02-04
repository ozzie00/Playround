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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.addfriend.ContactProfileActivity;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.Venue;
import com.oneme.toplay.database.Sport;

import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class VenueRadarActivity extends ActionBarActivity {

    private static final String TAG = "VenueRadarActivity";

    private static final int MAX_VENUE_SEARCH_RESULTS = 20;

    private static ParseGeoPoint mGeoPoint;

    // Adapter for the Parse query
    private ParseQueryAdapter<Venue> venueQueryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FriendRadarActivity use the searchOtherActivity layout
        setContentView(R.layout.ome_activity_venue_radar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setTitle(getResources().getString(R.string.OMEPARSEADDCONTACTSEARCHDISPLAYRESULTTITLE));

        mGeoPoint = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

        // Set up a customized query
        ParseQueryAdapter.QueryFactory<Venue> factory = new ParseQueryAdapter.QueryFactory<Venue>() {
            public ParseQuery<Venue> create() {
                ParseQuery<Venue> query = Venue.getQuery();
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                query.whereNear(AppConstant.OMETOPLAYVENUELOCATIONKEY, mGeoPoint);
                //query.whereWithinKilometers(AppConstant.OMETOPLAYVENUELOCATIONKEY, mGeoPoint, (Application.getSearchDistance())
                //        * AppConstant.OMEFEETTOMETERS / AppConstant.OMEMETERSINAKILOMETER);
                query.setLimit(MAX_VENUE_SEARCH_RESULTS);
                return query;
            }
        };

        // Set up a progress dialog
        final ProgressDialog messageListLoadDialog = new ProgressDialog(VenueRadarActivity.this);
        messageListLoadDialog.show();

        // Set up the query adapter
        venueQueryAdapter = new ParseQueryAdapter<Venue>(VenueRadarActivity.this, factory) {
            @Override
            public View getItemView(Venue venue, View view, ViewGroup parent) {

                if (view == null) {
                    view = View.inflate(getContext(), R.layout.ome_activity_venue_radar_item, null);
                }

                final ImageButton avatarButton  = (ImageButton) view.findViewById(R.id.venue_radar_avatar_button);
                TextView nameView           = (TextView) view.findViewById(R.id.venue_radar_name_view);
                TextView addressView        = (TextView) view.findViewById(R.id.venue_radar_address_view);
                TextView distanceView       = (TextView) view.findViewById(R.id.venue_radar_distance_view);

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

                // remember call phone number
                final String phonenumber = venue.getPhone();

                // use corresponding venue icon according to sport type
                String sporttype = venue.getType();
                int index        = Sport.msportarraylist.indexOf(sporttype);

                avatarButton.setImageResource(Sport.msporticonarray[index]);
                nameView.setText(venue.getName());
                addressView.setText(venue.getAddress());
                distanceView.setText(mdistancestring);

                // set callButton when phone number is not null
                final View callButton  = (ImageButton) view.findViewById(R.id.venue_radar_call_button);
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

                messageListLoadDialog.dismiss();

                return view;
            }
        };

        // Disable automatic loading when the adapter is attached to a view.
        venueQueryAdapter.setAutoload(true);

        // disable pagination, we'll manage the query limit ourselves
        venueQueryAdapter.setPaginationEnabled(false);

        // Attach the query adapter to the view
        ListView userListView = (ListView) findViewById(R.id.venue_radar_list);
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
