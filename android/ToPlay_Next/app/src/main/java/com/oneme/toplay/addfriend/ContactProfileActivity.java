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

package com.oneme.toplay.addfriend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactProfileActivity extends ActionBarActivity {


    private static final String TAG = "SearchOtherActivity";


    private static final int MAX_USER_SEARCH_RESULTS = 200;


    private static String mUsername = null;

    private static String mOMEID = null;

    // Adapter for the Parse query
    private ParseQueryAdapter<ParseUser> userQueryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_search_contact_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(getResources().getString(R.string.OMEPARSEADDCONTACTSEARCHDISPLAYPROFILE));

        // fetch username
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mUsername = extras.getString(Application.INTENT_EXTRA_USERNAME);
            mOMEID    = extras.getString(Application.INTENT_EXTRA_USEROMEID);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.OMEPARSEADDCONTACTSEARCHUSERNAMEERROR), Toast.LENGTH_LONG).show();
            return;
        }

        final RelativeLayout searchprofile = (RelativeLayout) findViewById(R.id.search_profile_view);

        TextView usernameView = (TextView) searchprofile.findViewById(R.id.search_profile_username_view);
        TextView contentView  = (TextView) searchprofile.findViewById(R.id.search_profile_content_view);
       // TextView lasttimeView = (TextView) view.findViewById(R.id.search_profile_lasttime_view);

        // Ozzie Zhang 2014-12-28 finish query for avatar icon for this user
        // use username to fetch user's avatar
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.whereContains(AppConstant.OMEPARSEUSERNAMEKEY, mUsername);
        query.orderByDescending(AppConstant.OMEPARSECREATEDAT);
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> user, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    ImageView avatarView       = (ImageView) searchprofile.findViewById(R.id.search_profile_avatar_view);
                    ParseFile mavatarImageFile = user.get(0).getParseFile(AppConstant.OMEPARSEUSERICONKEY);

                    // check if user set avatar
                    if (mavatarImageFile != null) {
                        Uri imageUri = Uri.parse(mavatarImageFile.getUrl());
                        Picasso.with(ContactProfileActivity.this).load(imageUri.toString()).into(avatarView);
                    }
                } else {
                    // Something went wrong. Look at the ParseException to see what's up.
                }
            }
        });

        // Ozzie Zhang 2014-11-04 need add query for avatar icon for this user
        // show username and invite content
        usernameView.setText(mUsername);

        //lasttimeView.setText(user.getString(AppConstant.OMEPARSEUSERLASTTIMEKEY));

        RelativeLayout searchprofileadd = (RelativeLayout) findViewById(R.id.search_profile_add_block);
        searchprofileadd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent invokeSendFriendRequestActivityIntent = new Intent(ContactProfileActivity.this, SendFriendRequestActivity.class);
                invokeSendFriendRequestActivityIntent.putExtra(Application.INTENT_EXTRA_USERNAME, mUsername);
                invokeSendFriendRequestActivityIntent.putExtra(Application.INTENT_EXTRA_USEROMEID, mOMEID);
               // invokeSendFriendRequestActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(invokeSendFriendRequestActivityIntent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}
