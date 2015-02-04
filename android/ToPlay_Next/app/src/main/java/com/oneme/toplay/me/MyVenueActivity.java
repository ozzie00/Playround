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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.parse.ParseUser;

public class MyVenueActivity extends ActionBarActivity {

    TextView homevenueText;
    TextView backupvenueText;
    String homephone;
    String backupphone;

    View homephonebutton;
    View backupphonebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ome_activity_myvenue);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ParseUser user     = ParseUser.getCurrentUser();

        String homevenue   = user.getString(AppConstant.OMEPARSEUSERHOMEVENUEKEY);

        String backupvenue = user.getString(AppConstant.OMEPARSEUSERBACKUPVENUEKEY);

        homephone          = user.getString(AppConstant.OMEPARSEUSERHOMEVENUEPHONEKEY);

        backupphone        = user.getString(AppConstant.OMEPARSEUSERBACKUPVENUEPHONEKEY);

        homephonebutton    = (ImageButton)findViewById(R.id.myvenue_home_call_button);

        backupphonebutton  = (ImageButton)findViewById(R.id.myvenue_backup_call_button);

        homevenueText      = (TextView)findViewById(R.id.myvenue_home_username_view);

        // set home field name
        if (homevenue != null && !homevenue.equals(AppConstant.OMEPARSENULLSTRING)) {

            homevenueText.setText(homevenue);
        }

        // set home hone button
        if (homephone != null && !homephone.equals(AppConstant.OMEPARSENULLSTRING)) {

            // set home phone
            if (homephone.length() > AppConstant.OMEPHONEMINIMUMLENGTH){
                homephonebutton.setVisibility(View.VISIBLE);
                homephonebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent invokePhoneCall = new Intent(Intent.ACTION_CALL);
                        invokePhoneCall.setData(Uri.parse(AppConstant.OMEPARSEINVOKECALLPHONE + homephone));
                        startActivity(invokePhoneCall);
                    }
                });
            } else {
                homephonebutton.setVisibility(View.GONE);
            }
        }

        backupvenueText = (TextView)findViewById(R.id.myvenue_backup_username_view);

        // set backup field name
        if (backupvenue != null && !backupvenue.equals(AppConstant.OMEPARSENULLSTRING)) {

            backupvenueText.setText(backupvenue);
        }

        // set home hone button
        if (backupphone != null && !backupphone.equals(AppConstant.OMEPARSENULLSTRING)) {

            // set home phone
            if (backupphone.length() > AppConstant.OMEPHONEMINIMUMLENGTH){
                backupphonebutton.setVisibility(View.VISIBLE);
                backupphonebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent invokePhoneCall = new Intent(Intent.ACTION_CALL);
                        invokePhoneCall.setData(Uri.parse(AppConstant.OMEPARSEINVOKECALLPHONE + backupphone));
                        startActivity(invokePhoneCall);
                    }
                });
            } else {
                backupphonebutton.setVisibility(View.GONE);
            }
        }

        // call venue as home activity
        RelativeLayout myhome = (RelativeLayout) findViewById(R.id.myvenue_home_block);
        myhome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent invokeVenueAsHomeActivityIntent = new Intent(MyVenueActivity.this, VenueAsHomeActivity.class);
                startActivityForResult(invokeVenueAsHomeActivityIntent, AppConstant.OMEPARSEUSERVENUEASHOMERESULT);
            }
        });

        // call venue backup activity
        RelativeLayout mysecond = (RelativeLayout) findViewById(R.id.myvenue_backup_block);
        mysecond.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent invokeVenueAsBackupActivityIntent = new Intent(MyVenueActivity.this, VenueAsBackUpActivity.class);//CnVenueSearchActivity.class);
                startActivityForResult(invokeVenueAsBackupActivityIntent, AppConstant.OMEPARSEUSERVENUEASBACKUPRESULT);
            }
        });

        // call venue radar activity
        RelativeLayout myradar = (RelativeLayout) findViewById(R.id.myvenue_radar_block);
        myradar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent invokeRadarActivityIntent = new Intent(MyVenueActivity.this, VenueRadarActivity.class);
                startActivity(invokeRadarActivityIntent);
            }
        });

        // call mysport activity
        RelativeLayout myupload = (RelativeLayout) findViewById(R.id.myvenue_upload_block);
        myupload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent invokeUploadActivityIntent = new Intent(MyVenueActivity.this, UploadVenueActivity.class);
                startActivity(invokeUploadActivityIntent);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.OMEPARSEUSERVENUEASHOMERESULT  && resultCode == Activity.RESULT_OK) {
            homevenueText.setText(data.getStringExtra(Application.INTENT_EXTRA_HOMEVENUE));
            homephone = data.getStringExtra(Application.INTENT_EXTRA_HOMEVENUEPHONE);

            // set home hone button
            if (homephone != null && !homephone.equals(AppConstant.OMEPARSENULLSTRING)) {

                // set home phone
                if (homephone.length() > AppConstant.OMEPHONEMINIMUMLENGTH){
                    homephonebutton.setVisibility(View.VISIBLE);
                    homephonebutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent invokePhoneCall = new Intent(Intent.ACTION_CALL);
                            invokePhoneCall.setData(Uri.parse(AppConstant.OMEPARSEINVOKECALLPHONE + homephone));
                            startActivity(invokePhoneCall);
                        }
                    });
                } else {
                    homephonebutton.setVisibility(View.GONE);
                }
            }
        }

        if(requestCode==AppConstant.OMEPARSEUSERVENUEASBACKUPRESULT && resultCode==Activity.RESULT_OK){
            backupvenueText.setText(data.getStringExtra(Application.INTENT_EXTRA_BACKUPVENUE));
            backupphone = data.getStringExtra(Application.INTENT_EXTRA_BACKUPVENUEPHONE);

            // set home hone button
            if (backupphone != null && !backupphone.equals(AppConstant.OMEPARSENULLSTRING)) {

                // set home phone
                if (backupphone.length() > AppConstant.OMEPHONEMINIMUMLENGTH){
                    backupphonebutton.setVisibility(View.VISIBLE);
                    backupphonebutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent invokePhoneCall = new Intent(Intent.ACTION_CALL);
                            invokePhoneCall.setData(Uri.parse(AppConstant.OMEPARSEINVOKECALLPHONE + backupphone));
                            startActivity(invokePhoneCall);
                        }
                    });
                } else {
                    backupphonebutton.setVisibility(View.GONE);
                }
            }
        }
    }



}
