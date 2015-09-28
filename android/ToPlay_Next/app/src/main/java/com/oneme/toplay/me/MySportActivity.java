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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.database.Sport;
import com.oneme.toplay.ui.BaseActivity;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;


public class MySportActivity extends BaseActivity {

    private static final String TAG = "MySportActivity";

    private static final int MAX_USER_SEARCH_RESULTS = 100;

    private static String mUsername = null;

    // Adapter for the Parse query
    private ParseQueryAdapter<Invite> inviteQueryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_mysport);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(MySportActivity.this,
                //        MeActivity.class)));
            }
        });


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setTitle(getResources().getString(R.string.OMEPARSEMEMYSPORTTITLE));

        // fetch username
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mUsername = extras.getString(Application.INTENT_EXTRA_USERNAME);
        }

        // Set up a customized query
        ParseQueryAdapter.QueryFactory<Invite> factory = new ParseQueryAdapter.QueryFactory<Invite>() {
            public ParseQuery<Invite> create() {
                ParseQuery<Invite> query = Invite.getQuery();
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                query.orderByDescending(AppConstant.OMEPARSECREATEDATKEY);
                query.whereEqualTo(AppConstant.OMEPARSEINVITEFROMUSERNAMEKEY, mUsername);
                query.setLimit(MAX_USER_SEARCH_RESULTS);
                return query;
            }
        };


        // Set up a progress dialog
        final ProgressDialog messageListLoadDialog = new ProgressDialog(MySportActivity.this);
        messageListLoadDialog.show();

        // Set up the query adapter
        inviteQueryAdapter = new ParseQueryAdapter<Invite>(MySportActivity.this, factory) {
            @Override
            public View getItemView(Invite invite, View view, ViewGroup parent) {

                if (view == null) {
                    view = View.inflate(getContext(), R.layout.ome_activity_mysport_item, null);
                }

                ImageView sporttypeiconView  = (ImageView) view.findViewById(R.id.mysporttype_icon_view);
                TextView usernameView = (TextView) view.findViewById(R.id.mysport_username_view);
                TextView contentView  = (TextView) view.findViewById(R.id.mysport_content_view);
                TextView lasttimeView = (TextView) view.findViewById(R.id.mysport_lasttime_view);

                // Ozzie Zhang 2014-11-04 need add query for avatar icon for this user
                // show username and invite content
                String sporttypevalue = invite.getSportTypeValue();
                sporttypeiconView.setImageDrawable(getResources().getDrawable(Sport.msporticonarray[Integer.parseInt(sporttypevalue)]));

                usernameView.setText(invite.getSportType());
                contentView.setText(invite.getPlayTime());
                lasttimeView.setText(invite.getSubmitTime());

                messageListLoadDialog.dismiss();

                return view;
            }
        };

        // Disable automatic loading when the adapter is attached to a view.
        inviteQueryAdapter.setAutoload(true);

        // Enable pagination, we'll not manage the query limit ourselves
        inviteQueryAdapter.setPaginationEnabled(true);

        // Attach the query adapter to the view
        ListView userListView = (ListView) findViewById(R.id.my_sport_list);
        userListView.setAdapter(inviteQueryAdapter);

        // Set up the handler for an item's selection
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Invite invite = inviteQueryAdapter.getItem(position);
                String mUsername    = invite.getFromUsername();

                Intent invokeMatchDetailActivityIntent = new Intent(getBaseContext(), MatchDetailActivity.class);
             //   invokeMatchDetailActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                putInviteToIntentExtra(invokeMatchDetailActivityIntent, invite);
                startActivity(invokeMatchDetailActivityIntent);
            }
        });
    }

    private void putInviteToIntentExtra(Intent intent, Invite invite) {
        intent.putExtra(Application.INTENT_EXTRA_SPORTTYPEVALUE, invite.getSportTypeValue());
        intent.putExtra(Application.INTENT_EXTRA_SPORTTYPE, invite.getSportType());
        intent.putExtra(Application.INTENT_EXTRA_PLAYERNUMBER, invite.getPlayerNumber());
        intent.putExtra(Application.INTENT_EXTRA_PLAYERLEVEL, invite.getPlayerLevel());
        intent.putExtra(Application.INTENT_EXTRA_TIME, invite.getPlayTime());
        intent.putExtra(Application.INTENT_EXTRA_COURT, invite.getCourt());
        intent.putExtra(Application.INTENT_EXTRA_FEE, invite.getFee());
        intent.putExtra(Application.INTENT_EXTRA_OTHER, invite.getOther());
        intent.putExtra(Application.INTENT_EXTRA_SUBMITTIME, invite.getSubmitTime());
        intent.putExtra(Application.INTENT_EXTRA_INVITEOBJECTID, invite.getObjectId());

    }


}
