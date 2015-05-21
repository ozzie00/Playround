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
import android.content.ComponentName;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.LoadImageFromParseCloud;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.base.third.CircleDisplay;
import com.oneme.toplay.base.third.RoundedTransformationBuilder;
import com.oneme.toplay.database.InviteComment;
import com.oneme.toplay.database.InviteScore;

import com.oneme.toplay.ui.BaseActivity;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ScoreboardActivity extends BaseActivity {

    private static final String TAG = "ScoreboardActivity";

    private ParseUser muser        = ParseUser.getCurrentUser();


    private static final int MAX_MESSAGE_SEARCH_RESULTS = 100;

    private String minviteObjectID = null;
    private String msporttype      = null;

    private String mcontent        = null;
    private Transformation mtransformation = null;

    // Adapter for the Parse query
    private ParseQueryAdapter<InviteScore> scoreQueryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_join_next_score);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(ScoreboardActivity.this,
                //        JoinNextActivity.class)));
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // fetch input extra
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            minviteObjectID = extras.getString(Application.INTENT_EXTRA_INVITEOBJECTID);
            msporttype      = extras.getString(Application.INTENT_EXTRA_SPORTTYPE);
        }

        // Set up a customized query
        ParseQueryAdapter.QueryFactory<InviteScore> factory = new ParseQueryAdapter.QueryFactory<InviteScore>() {
            public ParseQuery<InviteScore> create() {
                ParseQuery<InviteScore> query = InviteScore.getQuery();
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                query.include(AppConstant.OMEPARSEINVITESCOREAUTHORKEY);
                query.whereEqualTo(AppConstant.OMEPARSEINVITESCOREPARENTIDKEY, minviteObjectID);
                query.orderByDescending(AppConstant.OMEPARSEINVITESCORERATEKEY);
                query.setLimit(MAX_MESSAGE_SEARCH_RESULTS);
                return query;
            }
        };


        // Set up a progress dialog
        final ProgressDialog scoreListLoadDialog = new ProgressDialog(ScoreboardActivity.this);
        scoreListLoadDialog.show();

        mtransformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(1)
                .cornerRadiusDp(AppConstant.OMEPARSEUSERICONRADIUS)
                .oval(true)
                .build();

        // Set up the query adapter
        scoreQueryAdapter = new ParseQueryAdapter<InviteScore>(ScoreboardActivity.this, factory) {
            @Override
            public View getItemView(InviteScore score, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.ome_activity_join_next_score_item, null);
                }

                ImageView avatarView    = (ImageView)view.findViewById(R.id.join_score_avatar_icon_view);
                TextView rankView       = (TextView)view.findViewById(R.id.join_score_rank_view);
                TextView usernameView   = (TextView)view.findViewById(R.id.join_score_username_view);
                TextView contentView    = (TextView)view.findViewById(R.id.join_score_content_view);
                //TextView submittimeView = (TextView)view.findViewById(R.id.join_score_submit_time_view);

                LoadImageFromParseCloud.getAvatar(ScoreboardActivity.this, score.getAuthor(), avatarView);

                //ParseFile mfile = score.getAuthor().getParseFile(AppConstant.OMEPARSEUSERICONKEY);
                //Picasso.with(ScoreboardActivity.this)
                //        .load(mfile.getUrl())
                //        .fit()
                //        .transform(mtransformation)
                //        .into(avatarView);

                // Ozzie Zhang 2014-11-04 need add query for avatar icon for this user
                // show username and invite content
                // avatarView.setImageDrawable(getResources().getDrawable(R.drawable.ome_map_avataricon));
                //rankView.setText();
                usernameView.setText(score.getAuthorUsername());
                contentView.setText(score.getContent());
                //submittimeView.setText(comment.getSubmitTime());



                scoreListLoadDialog.dismiss();

                return view;
            }
        };

        // Disable automatic loading when the adapter is attached to a view.
        scoreQueryAdapter.setAutoload(true);

        // Enable pagination, we'll not manage the query limit ourselves
        scoreQueryAdapter.setPaginationEnabled(true);

        //  final ParseQueryAdapter adapter = new ParseQueryAdapter(this,AppConstant.OMETOPLAYMESSAGECLASSKEY);
        //  adapter.setTextKey(AppConstant.OMEPARSEMESSAGETOUSERNAMEKEY);

        // Attach the query adapter to the view
        ListView scoreListView = (ListView) findViewById(R.id.join_next_score_list);
        scoreListView.setAdapter(scoreQueryAdapter);

        // Set up the handler for an item's selection
        scoreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });


    }



}
