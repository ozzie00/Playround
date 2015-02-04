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
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.third.AutoCompleteTextViewAdapter;
import com.oneme.toplay.database.Venue;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class VenueAsBackUpActivity extends FragmentActivity {

    private static final String TAG = "VenueAsBackUpActivity";

    private static final int MAX_VENUE_SEARCH_RESULTS = 20;

    private static ParseGeoPoint mGeoPoint;

    private String mcourt = null;

    private String mphone = null;

    private ArrayList<String> venueNameList = new ArrayList<String>();

    private ArrayList<String> venuePhoneList = new ArrayList<String>();

    AutoCompleteTextView venueAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_venue_as_backup);

        mGeoPoint = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

        try {
            ParseQuery<Venue> query = Venue.getQuery();
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.whereNear(AppConstant.OMETOPLAYVENUELOCATIONKEY, mGeoPoint);
            query.setLimit(MAX_VENUE_SEARCH_RESULTS);
            List<Venue> mquery = query.find();
            for (Venue venue : mquery) {
                venueNameList.add(venue.getName());
                venuePhoneList.add(venue.getPhone());
            }
        } catch (ParseException pe) {
            // Log.d();

        }

       //ArrayAdapter<String> venueNameAdapter = new ArrayAdapter<String>(this,
       //         android.R.layout.simple_expandable_list_item_1, venueNameList){

       //          };

       AutoCompleteTextViewAdapter venueNameAdapter = new AutoCompleteTextViewAdapter(this, android.R.layout.simple_list_item_2, venueNameList);

        // Attach the query adapter to the view
        venueAutoComplete = (AutoCompleteTextView) findViewById(R.id.venue_as_backup_autocomplete);
        venueAutoComplete.setHint(getResources().getString(R.string.OMEPARSEMEMYVENUEASBACKUPPLACEHOLD));
        venueAutoComplete.setAdapter(venueNameAdapter);//sugAdapter);

        venueAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Set up a progress dialog
                final ProgressDialog messageListLoadDialog = new ProgressDialog(VenueAsBackUpActivity.this);
                messageListLoadDialog.show();

                String str     = (String) adapterView.getItemAtPosition(position);
                mphone         = venuePhoneList.get(position);
                mcourt         = venueAutoComplete.getEditableText().toString();
                ParseUser user = ParseUser.getCurrentUser();
                user.put(AppConstant.OMEPARSEUSERBACKUPVENUEPHONEKEY, mphone);
                user.put(AppConstant.OMEPARSEUSERBACKUPVENUEKEY, mcourt);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        messageListLoadDialog.dismiss();
                        if (e != null) {
                            // Show the error message
                            Toast.makeText(VenueAsBackUpActivity.this, " ", Toast.LENGTH_LONG).show();
                        } else {
                            Intent newIntent = new Intent();
                            newIntent.putExtra(Application.INTENT_EXTRA_BACKUPVENUE, mcourt);
                            newIntent.putExtra(Application.INTENT_EXTRA_BACKUPVENUEPHONE, mphone);

                            setResult(RESULT_OK, newIntent);

                           // setResult(RESULT_OK);
                            finish();

                        }
                    }
                });
            }
        });

        venueAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mcourt = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                mcourt = s.toString();

            }
        });


    }




}


