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

package com.oneme.toplay.join;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.LoadImageFromParseCloud;
import com.oneme.toplay.base.third.RoundedTransformationBuilder;
import com.oneme.toplay.database.Group;
import com.oneme.toplay.database.InviteScore;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.FindCallback;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class PlayerActivity extends ActionBarActivity {

    private static final String TAG        = "ScoreboardActivity";
    private ParseUser muser                = ParseUser.getCurrentUser();
    private String minviteObjectID         = null;
    private String mgroupAdminUsername     = null;
    private String mcontent                = null;
    private Group mgroup                   = null;
    private Transformation mtransformation = null;
    private ImageView avatarView           = null;


    // Adapter for the Parse query
    private ParseQueryAdapter<Group> groupQueryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_join_next_player);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // fetch input extra
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            minviteObjectID     = extras.getString(Application.INTENT_EXTRA_INVITEOBJECTID);
            mgroupAdminUsername = extras.getString(Application.INTENT_EXTRA_USERNAME);

        }

        /*
        CircleDisplay cd = (CircleDisplay) findViewById(R.id.join_score_circleDisplay);
        cd.setAnimDuration(3000);
        cd.setValueWidthPercent(55f);
        cd.setTextSize(36f);
        cd.setColor(Color.GREEN);
        cd.setDrawText(true);
        cd.setDrawInnerCircle(true);
        cd.setFormatDigits(1);
       // cd.setTouchEnabled(true);
        //cd.setSelectionListener(this);
        cd.setUnit("%");
        cd.setStepSize(0.5f);
        // cd.setCustomText(...); // sets a custom array of text
        cd.showValue(75f, 100f, true);
        */


        // Set up a customized query
        ParseQueryAdapter.QueryFactory<Group> factory = new ParseQueryAdapter.QueryFactory<Group>() {
            public ParseQuery<Group> create() {
                ParseQuery<Group> query = Group.getQuery();
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                query.include(AppConstant.OMEPARSEGROUPMEMBERUSERKEY);
                query.whereEqualTo(AppConstant.OMEPARSEGROUPPARENTIDKEY, minviteObjectID);
                query.whereEqualTo(AppConstant.OMEPARSEGROUPADMINNAMEKEY, mgroupAdminUsername);
                query.orderByDescending(AppConstant.OMEPARSEINVITESCORERATEKEY);
                return query;
            }
        };


        // Set up a progress dialog
        final ProgressDialog playerListLoadDialog = new ProgressDialog(PlayerActivity.this);
        playerListLoadDialog.show();

        mtransformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(1)
                .cornerRadiusDp(AppConstant.OMEPARSEUSERICONRADIUS)
                .oval(true)
                .build();


        // Set up the query adapter
        groupQueryAdapter = new ParseQueryAdapter<Group>(PlayerActivity.this, factory) {
            @Override
            public View getItemView(Group group, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.ome_activity_join_next_player_item, null);
                }

                avatarView              = (ImageView)view.findViewById(R.id.join_next_player_avatar_icon_view);
                TextView usernameView   = (TextView)view.findViewById(R.id.join_next_player_username_view);
                TextView contentView    = (TextView)view.findViewById(R.id.join_next_player_description_view);
                //TextView submittimeView = (TextView)view.findViewById(R.id.join_score_submit_time_view);


                LoadImageFromParseCloud.getAvatar(PlayerActivity.this, group.getMemberUser(), avatarView);

                //ParseFile mfile  = group.getMemberUser().getParseFile(AppConstant.OMEPARSEUSERICONKEY);
                //Picasso.with(PlayerActivity.this)
                //        .load(mfile.getUrl())
                //        .fit()
                //        .transform(mtransformation)
                //        .into(avatarView);


                //ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
                //userQuery.whereEqualTo(AppConstant.OMEPARSEUSERNAMEKEY, group.getMemberUsername());
                //userQuery.getFirstInBackground(new GetCallback<ParseUser>() {
                //    @Override
                //    public void done(ParseUser parseUser, ParseException e) {
                //        ParseFile mfile = parseUser.getParseFile(AppConstant.OMEPARSEUSERICONKEY);
                //        Picasso.with(PlayerActivity.this)
                //                .load(mfile.getUrl())
                //                .fit()
                //                .transform(mtransformation)
                //                .into(avatarView);

                //    }
                //});


                // Ozzie Zhang 2014-11-04 need add query for avatar icon for this user
                // show username and invite content
                // avatarView.setImageDrawable(getResources().getDrawable(R.drawable.ome_map_avataricon));
                usernameView.setText(group.getMemberUsername());
                //submittimeView.setText(comment.getSubmitTime());



                playerListLoadDialog.dismiss();

                return view;
            }
        };

        // Disable automatic loading when the adapter is attached to a view.
        groupQueryAdapter.setAutoload(true);

        // Enable pagination, we'll not manage the query limit ourselves
        groupQueryAdapter.setPaginationEnabled(true);

        //  final ParseQueryAdapter adapter = new ParseQueryAdapter(this,AppConstant.OMETOPLAYMESSAGECLASSKEY);
        //  adapter.setTextKey(AppConstant.OMEPARSEMESSAGETOUSERNAMEKEY);

        // Attach the query adapter to the view
        ListView scoreListView = (ListView) findViewById(R.id.join_next_player_list);
        scoreListView.setAdapter(groupQueryAdapter);

        // Set up the handler for an item's selection
        scoreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });


    }



}
