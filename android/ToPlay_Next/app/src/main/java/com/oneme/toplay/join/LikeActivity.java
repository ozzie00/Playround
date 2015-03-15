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
import android.widget.Toast;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.LoadImageFromParseCloud;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.base.third.RoundedTransformationBuilder;
import com.oneme.toplay.database.InviteLike;

import com.parse.ParseACL;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class LikeActivity extends ActionBarActivity {

    private static final String TAG = "LikeActivity";

    private ParseUser muser        = ParseUser.getCurrentUser();

    private String minviteObjectID = null;

    private Transformation mtransformation = null;

    private int mcount = 0;

    // Adapter for the Parse query
    private ParseQueryAdapter<InviteLike> likeQueryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_join_like);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // fetch input extra
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            minviteObjectID = extras.getString(Application.INTENT_EXTRA_INVITEOBJECTID);
        }

        // Set up a customized query
        ParseQueryAdapter.QueryFactory<InviteLike> factory = new ParseQueryAdapter.QueryFactory<InviteLike>() {
            public ParseQuery<InviteLike> create() {
                ParseQuery<InviteLike> query = InviteLike.getQuery();
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                query.include(AppConstant.OMEPARSEINVITELIKEAUTHORKEY);
                query.whereEqualTo(AppConstant.OMEPARSEINVITELIKEPARENTIDKEY, minviteObjectID);
                query.orderByDescending(AppConstant.OMEPARSECREATEDATKEY);
                return query;
            }
        };

        // Set up a progress dialog
        final ProgressDialog commentListLoadDialog = new ProgressDialog(LikeActivity.this);
        commentListLoadDialog.show();

        mtransformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(1)
                .cornerRadiusDp(AppConstant.OMEPARSEUSERICONRADIUS)
                .oval(true)
                .build();

        // Set up the query adapter
        likeQueryAdapter = new ParseQueryAdapter<InviteLike>(LikeActivity.this, factory) {
            @Override
            public View getItemView(InviteLike like, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.ome_activity_join_like_item, null);
                }

                ImageView avatarView = (ImageView) view.findViewById(R.id.join_like_avatar_icon_view);
                TextView usernameView = (TextView) view.findViewById(R.id.join_like_username_view);
                TextView whatsupView = (TextView) view.findViewById(R.id.join_like_whatsup_view);

                LoadImageFromParseCloud.getAvatar(LikeActivity.this, like.getAuthor(), avatarView);

                //ParseFile mfile = like.getAuthor().getParseFile(AppConstant.OMEPARSEUSERICONKEY);
                //Picasso.with(LikeActivity.this)
                //        .load(mfile.getUrl())
                //        .fit()
                //        .transform(mtransformation)
                //        .into(avatarView);

                // Ozzie Zhang 2014-11-04 need add query for avatar icon for this user
                // show username and invite content
                // avatarView.setImageDrawable(getResources().getDrawable(R.drawable.ome_map_avataricon));
                usernameView.setText(like.getAuthorUsername());
                // contentView.setText(comment.getContent());
                // submittimeView.setText(comment.getSubmitTime());

                commentListLoadDialog.dismiss();

                return view;
            }
        };

        // Disable automatic loading when the adapter is attached to a view.
        likeQueryAdapter.setAutoload(true);

        // Enable pagination, we'll not manage the query limit ourselves
        likeQueryAdapter.setPaginationEnabled(true);

        // Attach the query adapter to the view
        ListView commentListView = (ListView) findViewById(R.id.join_like_list);
        commentListView.setAdapter(likeQueryAdapter);

        // Set up the handler for an item's selection
        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

    }






}
