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

package com.oneme.toplay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.third.RoundedTransformationBuilder;
import com.oneme.toplay.database.Message;
import com.oneme.toplay.invite.InviteActivity;
import com.oneme.toplay.local.CnLocalActivity;
import com.oneme.toplay.local.LocalActivity;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class MessageListActivity extends ActionBarActivity {

    private static final String TAG = "MessageListActivity";

    private ParseUser muser  = ParseUser.getCurrentUser();

    private String musername = null;

    private int mcount = 0;
    
    private Transformation mtransformation = null;


    private static final int MAX_MESSAGE_SEARCH_RESULTS = 100;

    // Adapter for the Parse query
    private ParseQueryAdapter<Message> messageQueryAdapter;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         //        radius = Application.getSearchDistance();
         //lastRadius = radius;

         setContentView(R.layout.ome_activity_messagelist);

         // Check username, if null then jump to login activity
         if (muser == null) {
             //Toast.makeText(MessageListActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT), Toast.LENGTH_SHORT).show();

             // jump to login activity
             Intent invokeLoginActivityIntent = new Intent(MessageListActivity.this, LoginActivity.class);
             startActivity(invokeLoginActivityIntent);
         } else {

             musername = muser.getUsername();

             // Set up a customized query
             ParseQueryAdapter.QueryFactory<Message> factory = new ParseQueryAdapter.QueryFactory<Message>() {
                 public ParseQuery<Message> create() {
                     ParseQuery<Message> query = Message.getQuery();
                     query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                     query.include(AppConstant.OMEPARSEMESSAGEFROMUSERKEY);
                     query.whereEqualTo(AppConstant.OMEPARSEMESSAGETOUSERNAMEKEY, musername);
                     query.orderByDescending(AppConstant.OMEPARSECREATEDATKEY);
                     query.setLimit(MAX_MESSAGE_SEARCH_RESULTS);
                     return query;
                 }
             };

             // Set up a progress dialog
             final ProgressDialog messageListLoadDialog = new ProgressDialog(MessageListActivity.this);
             messageListLoadDialog.show();

             mtransformation = new RoundedTransformationBuilder()
                     .borderColor(Color.WHITE)
                     .borderWidthDp(1)
                     .cornerRadiusDp(AppConstant.OMEPARSEUSERICONRADIUS)
                     .oval(true)
                     .build();

             //     List<ParseObject> listObj =   .find();


             // Set up the query adapter
             messageQueryAdapter = new ParseQueryAdapter<Message>(MessageListActivity.this, factory) {
                 @Override
                 public View getItemView(Message message, View view, ViewGroup parent) {
                     if (view == null) {
                         view = View.inflate(getContext(), R.layout.ome_activity_messagelist_item, null);
                     }

                     ImageView avatarView  = (ImageView) view.findViewById(R.id.avatar_view);
                     TextView usernameView = (TextView) view.findViewById(R.id.username_view);
                     TextView contentView  = (TextView) view.findViewById(R.id.content_view);
                     TextView sendtimeView = (TextView) view.findViewById(R.id.newestsendtime);

                     //ParseFile mfile  = message.getMessageFromUser().getParseFile(AppConstant.OMEPARSEUSERICONKEY);
                     //Picasso.with(MessageListActivity.this)
                     //        .load(mfile.getUrl())
                     //        .fit()
                     //        .transform(mtransformation)
                     //        .into(avatarView);

                     // Ozzie Zhang 2014-11-04 need add query for avatar icon for this user
                     // show username and invite content
                     // avatarView.setImageDrawable(getResources().getDrawable(R.drawable.ome_map_avataricon));
                     usernameView.setText(message.getFromUsername());
                     contentView.setText(message.getContent());
                     sendtimeView.setText(message.getSendTime());

                     messageListLoadDialog.dismiss();

                     return view;
                 }
             };

             // Disable automatic loading when the adapter is attached to a view.
             messageQueryAdapter.setAutoload(true);

             // Enable pagination, we'll not manage the query limit ourselves
             messageQueryAdapter.setPaginationEnabled(true);

             //  final ParseQueryAdapter adapter = new ParseQueryAdapter(this,AppConstant.OMETOPLAYMESSAGECLASSKEY);
             //  adapter.setTextKey(AppConstant.OMEPARSEMESSAGETOUSERNAMEKEY);

             // Attach the query adapter to the view
             ListView messageListView = (ListView) findViewById(R.id.messagelist_itemview);
             messageListView.setAdapter(messageQueryAdapter);

             // Set up the handler for an item's selection
             messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                     final Message item = messageQueryAdapter.getItem(position);
                     String hostusername = item.getFromUsername();

                     Intent invokeMessageReplyActivityIntent = new Intent(getBaseContext(), MessageReplyActivity.class);//LocalActivity.this, JoinActivity.class);
                     invokeMessageReplyActivityIntent.putExtra(AppConstant.OMEPARSEMESSAGETOUSERNAMEKEY, hostusername);
                     invokeMessageReplyActivityIntent.putExtra(Application.INTENT_EXTRA_USEROBJECTID, item.getUser().getObjectId());
                     startActivity(invokeMessageReplyActivityIntent);

                 }
             });

         }



     }

    // Check google play service
    private boolean checkGooglePlayServicesAvailable() {
        int mgoogleplayStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MessageListActivity.this);

        // Check google play
        if (mgoogleplayStatus != ConnectionResult.SUCCESS) {
            //Toast.makeText(this, getResources().getString(R.string.google_play_service_unavailable), Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

}
