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
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import com.oneme.toplay.addfriend.ContactProfileActivity;
import com.oneme.toplay.Application;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.Sport;
import com.oneme.toplay.database.Message;
import com.oneme.toplay.database.PlayerIcon;
import com.oneme.toplay.R;

import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class MatchDetailActivity extends ActionBarActivity {

    private static final String TAG = "MatchDetailActivity";

    private LinearLayout mMatchDetailLinerLayout;

    private ImageView msporttypeIcon;
    private TextView mtimeText;
    private TextView mcourtText;

    private ParseGeoPoint geoPoint;
    private String minviteObjectID   = null;
    private String msporttypevalue   = null;
    private String minviteplaytime   = null;
    private String mcourt            = null;

    private static final int MAX_REQUEST_MESSAGE_SEARCH_RESULTS = 20;

    private TextView mplayerheadertext = null;

    // Adapter for the Parse query
    private ParseQueryAdapter<Message> messageQueryAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ome_activity_match_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(getResources().getString(R.string.OMEPARSEMEMATCHDETAILTITLE));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            msporttypevalue   = extras.getString(Application.INTENT_EXTRA_SPORTTYPEVALUE);
            minviteplaytime   = extras.getString(Application.INTENT_EXTRA_TIME);
            mcourt            = extras.getString(Application.INTENT_EXTRA_COURT);
            minviteObjectID   = extras.getString(Application.INTENT_EXTRA_INVITEOBJECTID);
        }

        // add LinearLayout
        mMatchDetailLinerLayout =(LinearLayout) findViewById(R.id.match_detail_linerlayout);

        // add LayoutParams
        mMatchDetailLinerLayout.setOrientation(LinearLayout.VERTICAL);

        mplayerheadertext = (TextView) findViewById(R.id.player_partner_header);
        mplayerheadertext.setVisibility(View.VISIBLE);

        // Set up a customized query
        ParseQueryAdapter.QueryFactory<Message> factory = new ParseQueryAdapter.QueryFactory<Message>() {
            public ParseQuery<Message> create() {
                ParseQuery<Message> query = Message.getQuery();
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                query.orderByDescending(AppConstant.OMEPARSECREATEDAT);
                query.whereEqualTo(AppConstant.OMEPARSEMESSAGEFOROBJECTIDKEY, minviteObjectID);
                query.setLimit(MAX_REQUEST_MESSAGE_SEARCH_RESULTS);
                return query;
            }
        };

        // Set up a progress dialog
        final ProgressDialog messageListLoadDialog = new ProgressDialog(MatchDetailActivity.this);
        messageListLoadDialog.show();

        // Set up the query adapter
        messageQueryAdapter = new ParseQueryAdapter<Message>(MatchDetailActivity.this, factory) {
            @Override
            public View getItemView(Message message, View view, ViewGroup parent) {

                if (view == null) {
                    view = View.inflate(getContext(), R.layout.ome_activity_match_detail_list, null);
                }

               // ImageView avatarView  = (ImageView) view.findViewById(R.id.match_detail_avatar_view);
                TextView usernameView = (TextView) view.findViewById(R.id.match_detail_username_view);
                TextView contentView  = (TextView) view.findViewById(R.id.match_detail_content_view);
                TextView lasttimeView = (TextView) view.findViewById(R.id.match_detail_lasttime_view);

                // Ozzie Zhang 2014-11-04 need add query for avatar icon for this user
                // show username and invite content
                usernameView.setText(message.getFromUsername());
                //contentView.setText(message.getContent());
                lasttimeView.setText(message.getSendTime());

                messageListLoadDialog.dismiss();

                return view;
            }
        };

        // Disable automatic loading when the adapter is attached to a view.
        messageQueryAdapter.setAutoload(true);

        // Enable pagination, we'll not manage the query limit ourselves
        messageQueryAdapter.setPaginationEnabled(true);

        // Attach the query adapter to the view
        ListView userListView = (ListView) findViewById(R.id.match_detail_listview);
        userListView.setAdapter(messageQueryAdapter);

        // Set up the handler for an item's selection
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Message message = messageQueryAdapter.getItem(position);

                String mUsername      = message.getFromUsername();
                String mOMEID         = message.getFromOmeID();

                Intent invokeContactProfileActivityIntent = new Intent(MatchDetailActivity.this, ContactProfileActivity.class);

                if (mUsername != null) {
                    invokeContactProfileActivityIntent.putExtra(Application.INTENT_EXTRA_USERNAME, mUsername);
                }

                if (mOMEID != null) {
                    invokeContactProfileActivityIntent.putExtra(Application.INTENT_EXTRA_USEROMEID, mOMEID);
                }

               // invokeContactProfileActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(invokeContactProfileActivityIntent);
            }
        });

        // add sport type text
        addSportTypeText();

        // add join play time text
        addInviteTimeText();

        // add join court text
        addInviteCourtText();
    }

    // define sport type text
    private void addSportTypeText() {
        msporttypeIcon = (ImageView) findViewById(R.id.match_detail_sporttypeicon);
        msporttypeIcon.setImageDrawable(getResources().getDrawable(Sport.msporticonarray[Integer.parseInt(msporttypevalue)]));
    }

    // define invite play time text
    private void addInviteTimeText() {
        mtimeText = (TextView) findViewById(R.id.match_detail_invitetimeText);
        mtimeText.setText(minviteplaytime);
    }

    // define invite court text
    private void addInviteCourtText() {
        mcourtText = (TextView) findViewById(R.id.match_detail_invitecourtText);
        mcourtText.setText(mcourt);
    }



}