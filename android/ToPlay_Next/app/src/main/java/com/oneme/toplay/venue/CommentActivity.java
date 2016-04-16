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

package com.oneme.toplay.venue;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
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
import com.oneme.toplay.database.VenueComment;
import com.parse.CountCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "CommentActivity";

    private ParseUser muser        = ParseUser.getCurrentUser();

    private String minviteObjectID = null;

    private String mcontent        = null;

    private int mcount = 0;

    // Adapter for the Parse query
    private ParseQueryAdapter<VenueComment> commentQueryAdapter;

    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_venue_comment);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

     //   ActionBar actionBar = getSupportActionBar();
     //   actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
     //           | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);


        // fetch input extra
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            minviteObjectID = extras.getString(Application.INTENT_EXTRA_INVITEOBJECTID);
        }

        ParseQuery<VenueComment> countquery = VenueComment.getQuery();
        countquery.whereEqualTo(AppConstant.OMEPARSEINVITECOMMENTPARENTIDKEY, minviteObjectID);
        countquery.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                mcount = i;
            }
        });

        // Set up a customized query
        ParseQueryAdapter.QueryFactory<VenueComment> factory = new ParseQueryAdapter.QueryFactory<VenueComment>() {
            public ParseQuery<VenueComment> create() {
                ParseQuery<VenueComment> query = VenueComment.getQuery();
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                query.include(AppConstant.OMEPARSEINVITECOMMENTAUTHORKEY);
                //query.whereEqualTo(AppConstant.OMEPARSEINVITECOMMENTAUTHORNAMEKEY, ParseUser.getCurrentUser().getUsername());
                query.whereEqualTo(AppConstant.OMEPARSEINVITECOMMENTPARENTIDKEY, minviteObjectID);
                query.orderByDescending(AppConstant.OMEPARSECREATEDATKEY);
                return query;
            }
        };


        // Set up a progress dialog
        final ProgressDialog commentListLoadDialog = new ProgressDialog(CommentActivity.this);
        commentListLoadDialog.show();

        // Set up the query adapter
        commentQueryAdapter = new ParseQueryAdapter<VenueComment>(CommentActivity.this, factory) {
            @Override
            public View getItemView(VenueComment comment, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.ome_activity_venue_comment_item, null);
                }

                ImageView avatarView    = (ImageView) view.findViewById(R.id.venue_comment_avatar_icon_view);
                TextView usernameView   = (TextView) view.findViewById(R.id.venue_comment_username_view);
                TextView contentView    = (TextView) view.findViewById(R.id.venue_comment_content_view);
                TextView submittimeView = (TextView) view.findViewById(R.id.venue_comment_submit_time_view);

                LoadImageFromParseCloud.getAvatar(CommentActivity.this, comment.getAuthor(), avatarView);

                // Ozzie Zhang 2014-11-04 need add query for avatar icon for this user
                // show username and invite content
                // avatarView.setImageDrawable(getResources().getDrawable(R.drawable.ome_map_avataricon));
                usernameView.setText(comment.getAuthorUsername());
                contentView.setText(comment.getContent());
                submittimeView.setText(comment.getSubmitTime());

                commentListLoadDialog.dismiss();

                return view;
            }
        };

        // Disable automatic loading when the adapter is attached to a view.
        commentQueryAdapter.setAutoload(true);

        // Enable pagination, we'll not manage the query limit ourselves
        commentQueryAdapter.setPaginationEnabled(true);

        //  final ParseQueryAdapter adapter = new ParseQueryAdapter(this,AppConstant.OMETOPLAYMESSAGECLASSKEY);
        //  adapter.setTextKey(AppConstant.OMEPARSEMESSAGETOUSERNAMEKEY);

        // Attach the query adapter to the view
        ListView commentListView = (ListView) findViewById(R.id.venue_comment_list);
        commentListView.setAdapter(commentQueryAdapter);

        // Set up the handler for an item's selection
        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

        // set comment input
        final EditText mcomment = (EditText)findViewById(R.id.venue_comment_input_text);
        if (muser != null) {
            mcomment.setVisibility(View.VISIBLE);
        }
        mcomment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //mcomment.setBackground(getResources().getDrawable(R.drawable.ome_textfield_rectangle_background));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mcontent = s.toString();
                //  mcomment.setBackgroundColor(getResources().getColor(R.color.white_absolute));
                //  mcomment.setBackground(getResources().getDrawable(R.drawable.ome_textfield_input_rectangle_background));
            }

            @Override
            public void afterTextChanged(Editable s) {
                mcontent = s.toString();
                //  mcomment.setBackground(getResources().getDrawable(R.drawable.ome_textfield_rectangle_background));
            }
        });

        final Button maddbutton = (Button)findViewById(R.id.venue_comment_add_text);
        if (muser != null) {
            maddbutton.setVisibility(View.VISIBLE);


            maddbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitcomment();
                }
            });
        }

     }


    private void submitcomment() {
        if (muser != null) {
            VenueComment comment = new VenueComment();
            String username       = muser.getUsername();
            String time           = Time.currentTime();
            comment.setAuthor(muser);
            comment.setAuthorUsername(username);
            comment.setParentObjectId(minviteObjectID);
            comment.setSubmitTime(time);
            if (mcontent != null && mcontent.length() >= 1) {
                comment.setContent(mcontent);
                ParseACL acl = new ParseACL();

                // Give public read access
                acl.setPublicReadAccess(true);
                comment.setACL(acl);
                comment.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        finish();
                    }
                });


            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ome_activity_comment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_load:
                menuItem = item;
                menuItem.setActionView(R.layout.ome_activity_comment_progressbar);
                menuItem.expandActionView();
                //TestTask task = new TestTask();
                //task.execute("test");
                break;
            default:
                break;
        }
        return true;
    }

}
