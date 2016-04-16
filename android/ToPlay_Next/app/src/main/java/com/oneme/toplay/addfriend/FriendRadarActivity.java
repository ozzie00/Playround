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

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class FriendRadarActivity extends AppCompatActivity {

    private static final String TAG = "FriendRadarActivity";

    private static final int MAX_USER_SEARCH_RESULTS = 100;

    private static ParseGeoPoint mGeoPoint;

    // Adapter for the Parse query
    private ParseQueryAdapter<ParseUser> userQueryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FriendRadarActivity use the searchOtherActivity layout
        setContentView(R.layout.ome_activity_search_other);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setTitle(getResources().getString(R.string.OMEPARSEADDCONTACTSEARCHDISPLAYRESULTTITLE));

        // fetch username
        Bundle extras = getIntent().getExtras();

        mGeoPoint = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

        // Set up a customized query
        ParseQueryAdapter.QueryFactory<ParseUser> factory = new ParseQueryAdapter.QueryFactory<ParseUser>() {
            public ParseQuery<ParseUser> create() {
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);

             //   query.orderByDescending(AppConstant.OMEPARSEUSERLASTLOCATIONKEY);

                query.whereWithinKilometers(AppConstant.OMEPARSEUSERLASTLOCATIONKEY, mGeoPoint, Application.getSearchDistance()
                        * AppConstant.OMEFEETTOMETERS / AppConstant.OMEMETERSINAKILOMETER);

                query.setLimit(MAX_USER_SEARCH_RESULTS);
                return query;
            }
        };

        // Set up a progress dialog
        final ProgressDialog messageListLoadDialog = new ProgressDialog(FriendRadarActivity.this);
        messageListLoadDialog.show();

        // Set up the query adapter
        userQueryAdapter = new ParseQueryAdapter<ParseUser>(FriendRadarActivity.this, factory) {
            @Override
            public View getItemView(ParseUser user, View view, ViewGroup parent) {

                if (view == null) {
                    view = View.inflate(getContext(), R.layout.ome_activity_search_contact_item, null);
                }

                final ImageView avatarView  = (ImageView) view.findViewById(R.id.search_other_avatar_view);
                TextView usernameView       = (TextView) view.findViewById(R.id.search_other_username_view);
                TextView contentView        = (TextView) view.findViewById(R.id.search_other_content_view);
                TextView lasttimeView       = (TextView) view.findViewById(R.id.search_other_lasttime_view);

                // Ozzie Zhang 2014-11-04 need add query for avatar icon for this user
                // show username and invite content
                //avatarView.setImageDrawable(getResources().getDrawable(R.drawable.ome_map_avataricon));

                ParseFile mavatarImageFile =  null; user.getParseFile(AppConstant.OMEPARSEUSERICONKEY);

                if (user != null) {
                    mavatarImageFile = user.getParseFile(AppConstant.OMEPARSEUSERICONKEY);
                }

                // fetch user avatar from parse cloud, save it tmp file
                if (mavatarImageFile != null) {
                    mavatarImageFile.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, com.parse.ParseException pe) {
                            if (pe == null) {
                                try {
                                    // data has the bytes for the resume
                                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    avatarView.setImageBitmap(bmp);
                              } catch (Exception e) {
                                    if (Application.APPDEBUG) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {

                            }

                        }
                    });
                }

                usernameView.setText(user.getUsername());
                lasttimeView.setText(user.getString(AppConstant.OMEPARSEUSERLASTTIMEKEY));
                // contentView.setText(user.getContent());
                // sendtimeView.setText(user.getSendTime());

                messageListLoadDialog.dismiss();

                return view;
            }
        };

        // Disable automatic loading when the adapter is attached to a view.
        userQueryAdapter.setAutoload(true);

        // Enable pagination, we'll not manage the query limit ourselves
        userQueryAdapter.setPaginationEnabled(true);

        // Attach the query adapter to the view
        ListView userListView = (ListView) findViewById(R.id.search_contact_list);
        userListView.setAdapter(userQueryAdapter);

        // Set up the handler for an item's selection
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final ParseUser item = userQueryAdapter.getItem(position);
                String mUsername     = item.getUsername();
                String mOMEID        = item.getString(AppConstant.OMEPARSEUSEROMEIDKEY);

                Intent invokeSearchContactProfileActivityIntent = new Intent(getBaseContext(), ContactProfileActivity.class);
                invokeSearchContactProfileActivityIntent.putExtra(Application.INTENT_EXTRA_USERNAME, mUsername);
                invokeSearchContactProfileActivityIntent.putExtra(Application.INTENT_EXTRA_USEROMEID, mOMEID);
                invokeSearchContactProfileActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(invokeSearchContactProfileActivityIntent);
            }
        });
    }


}
