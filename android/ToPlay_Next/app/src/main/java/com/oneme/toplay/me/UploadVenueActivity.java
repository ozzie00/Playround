/*
* Copyright 2015 OneME
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
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.database.Venue;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

public class UploadVenueActivity extends ActionBarActivity {


    private final String TAG= "UploadVenueActivity";

    private EditText mnameText;
    private EditText maddressText;
    private EditText mphoneText;

    private String mname    = null;
    private String maddress = null;
    private String mphone   = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ome_activity_uploadvenue);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // add name
        mnameText = (EditText) findViewById(R.id.uploadvenue_name_Text);
        mnameText.setHint(getResources().getString(R.string.OMEPARSEMEMYVENUENAMEPLACEHOLD));
        mnameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (mnameText.length() > 0){
                    // position the text type in the left top corner
                    mnameText.setGravity(Gravity.LEFT | Gravity.TOP);
                }else{
                    // no text entered. Center the hint text.
                    mnameText.setGravity(Gravity.LEFT);
                }
                mname = arg0.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {

                mname = s.toString();

            }
        });

        // add address
        maddressText = (EditText) findViewById(R.id.uploadvenue_address_Text);
        maddressText.setHint(getResources().getString(R.string.OMEPARSEMEMYVENUEADDRESSPLACEHOLD));
        maddressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (maddressText.length() > 0){
                    // position the text type in the left top corner
                    maddressText.setGravity(Gravity.LEFT | Gravity.TOP);
                }else{
                    // no text entered. Center the hint text.
                    maddressText.setGravity(Gravity.LEFT);
                }
                maddress = arg0.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                maddress = s.toString();
            }
        });

        // add mphone
        mphoneText = (EditText) findViewById(R.id.uploadvenue_phone_Text);
        mphoneText.setHint(getResources().getString(R.string.OMEPARSEMEMYVENUEPHONEPLACEHOLD));
       // mphoneText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mphoneText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        mphoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (mphoneText.length() > 0){
                    // position the text type in the left top corner
                    mphoneText.setGravity(Gravity.LEFT | Gravity.TOP);
                }else{
                    // no text entered. Center the hint text.
                    mphoneText.setGravity(Gravity.LEFT);
                }
                mphone = arg0.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                mphone = s.toString();

            }
        });



        Button submit =(Button)findViewById(R.id.uploadvenue_submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitVenue();
            }
        });

    }


    private void submitVenue () {

        // Set up a progress dialog
        final ProgressDialog messageListLoadDialog = new ProgressDialog(UploadVenueActivity.this);
        messageListLoadDialog.show();

        // Create an invitation.
        Venue venue = new Venue();

        ParseGeoPoint mGeoPoint = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));;
        // Set the detailed venue information
        venue.setName(mname);
        venue.setAddress(maddress);
        venue.setPhone(mphone);
        venue.setLocation(mGeoPoint);
        venue.setCourtNumber("1");
        venue.setIndoor("1");
        venue.setLighted("1");
        venue.setPublic("Public");

        venue.setUploadedBy(ParseUser.getCurrentUser().getUsername());
        ParseACL acl = new ParseACL();

        // Give public read access
        acl.setPublicReadAccess(true);
        venue.setACL(acl);

        // Save the post
        venue.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                messageListLoadDialog.dismiss();
                finish();
            }
        });

        return;

    }

}
