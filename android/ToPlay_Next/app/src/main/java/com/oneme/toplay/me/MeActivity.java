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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v7.app.ActionBarActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.addfriend.ShowQRcodeActivity;

import com.oneme.toplay.base.LoadImageFromParseCloud;
import com.oneme.toplay.base.third.RoundedTransformationBuilder;
import com.parse.ParseUser;
import com.parse.ParseFile;


import com.squareup.picasso.Transformation;

//import com.nostra13.universalimageloader.core.ImageLoader;

//import com.squareup.picasso.Picasso;


/**
 * Activity that displays the settings screen.
 */
public class MeActivity extends ActionBarActivity {

    private LinearLayout msettingLinerLayout;
    private Button mloginButton;

    private TextView mUsernameText;
    private TextView mAboutText;
    private String mUsername               = null;
    private String mAbout                  = null;
    private ParseUser muser                = ParseUser.getCurrentUser();
    private Transformation mtransformation = null;
    private ParseFile mfile                = null;


    private List<Float> availableOptions = Application.getConfigHelper().getSearchDistanceAvailableOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ome_activity_me);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (ParseUser.getCurrentUser() != null) {
            addUsernameText();
        } else {
            //Toast.makeText(SettingActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT),
            //         Toast.LENGTH_SHORT).show();
        }

       // addAboutText();
        addSetting();

        mtransformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(1)
                .cornerRadiusDp(AppConstant.OMEPARSEUSERICONRADIUS)
                .oval(false)
                .build();


        // call profile activity
        RelativeLayout profile          = (RelativeLayout) findViewById(R.id.me_profile_block);

        final ImageView avatarImageView = (ImageView)findViewById(R.id.me_avatar_view);


        if (muser != null) {
            LoadImageFromParseCloud.getAvatar(MeActivity.this, muser, avatarImageView);

           // mfile = muser.getParseFile(AppConstant.OMEPARSEUSERICONKEY);
           // Picasso.with(MeActivity.this)
           //         .load(mfile.getUrl())
           //         .fit()
           //         .transform(mtransformation)
           //         .into(avatarImageView);
        }

        // transfer file path to invoked activity
        profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent invokeProfileActivityIntent = new Intent(MeActivity.this, ProfileActivity.class);
                startActivity(invokeProfileActivityIntent);
            }
        });

        ImageView qrcode = (ImageView) findViewById(R.id.me_qrcode_view);
        qrcode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent invokeShowQRcodeActivityIntent = new Intent(MeActivity.this, ShowQRcodeActivity.class);
                startActivity(invokeShowQRcodeActivityIntent);
            }
        });

        // call mysport activity
        RelativeLayout mysport = (RelativeLayout) findViewById(R.id.me_sport_block);
        mysport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent invokeMySportActivityIntent = new Intent(MeActivity.this, MySportActivity.class);
                invokeMySportActivityIntent.putExtra(Application.INTENT_EXTRA_USERNAME, mUsername);
                startActivity(invokeMySportActivityIntent);
            }
        });

        // call myvenue activity
        RelativeLayout myaddress = (RelativeLayout) findViewById(R.id.me_address_block);
        myaddress.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent invokeMyVenueActivityIntent = new Intent(MeActivity.this, MyVenueActivity.class);
                //invokeMySportActivityIntent.putExtra(Application.INTENT_EXTRA_USERNAME, mUsername);
                startActivity(invokeMyVenueActivityIntent);
            }
        });



        // call setting activity
        RelativeLayout setting = (RelativeLayout) findViewById(R.id.me_setting_block);
        setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent invokeSettingActivityIntent = new Intent(MeActivity.this, SettingActivity.class);
                startActivity(invokeSettingActivityIntent);
            }
        });


    }


    public void onClickSetting(View v) {

        Intent invokeSettingActivityIntent = new Intent(MeActivity.this, SettingActivity.class);
        startActivity(invokeSettingActivityIntent);
    }


    // define username text
    private void addUsernameText() {
        mUsername     = ParseUser.getCurrentUser().getUsername();
        mUsernameText = (TextView) findViewById(R.id.me_username_view);
        mUsernameText.setText(mUsername);
    }

    // define username text
    private void addSetting() {
        mAboutText = (TextView) findViewById(R.id.me_setting);
        mAboutText.setText(getResources().getString(R.string.OMEPARSEMEMYSETTINGS));
    }

    // define username text
    private void addAboutText() {
    //    mAboutText = (TextView) findViewById(R.id.me_about);
    //    mAboutText.setText(getResources().getString(R.string.app_version));
    }


    @Override
    public Intent getSupportParentActivityIntent() {

        Intent parentIntent = getIntent();

        //getting the parent class name
        String className = parentIntent.getStringExtra(AppConstant.OMEPARSEPARENTCLASSNAME);

        Intent newIntent = null;

        try {
            //you need to define the class with package name
            newIntent = new Intent(MeActivity.this,Class.forName(className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newIntent;
    }
}
