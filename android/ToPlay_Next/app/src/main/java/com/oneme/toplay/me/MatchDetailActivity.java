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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.adapter.PlayerGridAdapter;
import com.oneme.toplay.addfriend.ContactProfileActivity;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.Message;
import com.oneme.toplay.database.PlayerIcon;
import com.oneme.toplay.database.Sport;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;


public class MatchDetailActivity extends AppCompatActivity {

    private static final String TAG = "MatchDetailActivity";

    private EditText edittext;
    private EditText edittext1;

    private final Context context                = MatchDetailActivity.this;
    private final Context msubmitjoinplayRequest = MatchDetailActivity.this;
    private LinearLayout mMatchDetailLinerLayout;
    private Button mjoinplayButton;


    private TextView mhostUsernameText;
    private ImageView mhostUsericonImage;
    private TextView mhostLevelText;
    private TextView msporttypeText;
    private ImageView msporttypeIcon;
    private TextView mplayerleveText;
    private TextView mplayernumberText;
    private TextView minviteplayfeeText;
    private TextView mtimeText;
    private TextView mcourtText;
    private TextView motherText;
    private TextView msubmitTimeText;

    private ParseGeoPoint geoPoint;
    private String minviteObjectID   = null;

    private String mhostUsername     = null;
    private String mhostUsericonPath = null;
    private String mhostLevel        = null;
    private String mfromusername     = null;
    private String msporttype        = null;
    private String msporttypevalue   = null;
    private String mplayerlevel      = null;
    private String mplayernumber     = null;
    private String minviteplaytime   = null;
    private String mcourt            = null;
    private String minviteplayfee    = null;
    private String mother            = null;
    private String msubmittime       = null;
    private String mhostUser         = null;
    private String mOMEID            = null;


    private static final int MAX_REQUEST_MESSAGE_SEARCH_RESULTS = 20;

    private static String mUsername = null;

    // Fields for the map radius in feet
    private float radius;

    private String selectedInviteObjectId;


    private List<Message> mquerymessage;

    private ArrayList<PlayerIcon> mplayerlist     = null;

    private PlayerGridAdapter  mplayerGridAdapter = null;

    private GridView mplayergridView              = null;

    private ProgressDialog mProgressDialog;

    private TextView mplayerheadertext = null;

    // Adapter for the Parse query
    private ParseQueryAdapter<Message> messageQueryAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ome_activity_match_detail);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setHomeButtonEnabled(true);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
       // getSupportActionBar().setTitle(getResources().getString(R.string.OMEPARSEMEMATCHDETAILTITLE));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            msporttype        = extras.getString(Application.INTENT_EXTRA_SPORTTYPE);
            msporttypevalue   = extras.getString(Application.INTENT_EXTRA_SPORTTYPEVALUE);
            mplayerlevel      = extras.getString(Application.INTENT_EXTRA_PLAYERLEVEL);
            mplayernumber     = extras.getString(Application.INTENT_EXTRA_PLAYERNUMBER);
            minviteplaytime   = extras.getString(Application.INTENT_EXTRA_TIME);
            mcourt            = extras.getString(Application.INTENT_EXTRA_COURT);
            minviteplayfee    = extras.getString(Application.INTENT_EXTRA_FEE);
            mother            = extras.getString(Application.INTENT_EXTRA_OTHER);
            msubmittime       = extras.getString(Application.INTENT_EXTRA_SUBMITTIME);
            minviteObjectID   = extras.getString(Application.INTENT_EXTRA_INVITEOBJECTID);
        }

        // add LinearLayout
        mMatchDetailLinerLayout =(LinearLayout) findViewById(R.id.match_detail_linerlayout);

        // add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mMatchDetailLinerLayout.setOrientation(LinearLayout.VERTICAL);

        mplayerheadertext = (TextView) findViewById(R.id.player_partner_header);
        mplayerheadertext.setVisibility(View.VISIBLE);

        mplayerlist = new ArrayList<PlayerIcon>();

        // Set up a customized query
        ParseQueryAdapter.QueryFactory<Message> factory = new ParseQueryAdapter.QueryFactory<Message>() {
            public ParseQuery<Message> create() {
                ParseQuery<Message> query = Message.getQuery();
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                query.orderByDescending(AppConstant.OMEPARSECREATEDATKEY);
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
        /*

        try {

            // query player that want to join this match
            ParseQuery<Message> query = new ParseQuery<Message>(AppConstant.OMETOPLAYMESSAGECLASSKEY);// Message.getQuery();

            //  query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
            query.orderByDescending(AppConstant.OMEPARSECREATEDAT);
            query.whereEqualTo(AppConstant.OMEPARSEMESSAGEFOROBJECTIDKEY, minviteObjectID);
            query.setLimit(MAX_REPLY_MESSAGE_SEARCH_RESULTS);
            //        return query;
            mquerymessage = query.find();

         //   Toast.makeText(MatchDetailActivity.this, "omeid " + mquerymessage.size(),
         //           Toast.LENGTH_LONG).show();

            for (Message message : mquerymessage) {
                // ParseFile image = (ParseFile) message.get("phones");
                //  PhoneList map = new PhoneList();
                // map.setPhone(image.getUrl());
                String momeid      =  message.getFromUsername(); //.getString(AppConstant.OMEPARSEUSERLASTLOCATIONKEY);//AppConstant.OMEPARSEUSEROMEIDKEY);
                Toast.makeText(MatchDetailActivity.this, "omeid " + momeid,
                                 Toast.LENGTH_LONG).show();

                PlayerIcon mplayer = new PlayerIcon(homeIcon, "test", momeid);
                mplayerlist.add(mplayer);
            }
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        */

        // add sport type text
        addSportTypeText();

        // add player level text
        addPlayerLevelText();

        // add player number text
        addPlayerNumberText();

        // add join play time text
        addInviteTimeText();

        // add join court text
        addInviteCourtText();

        // add fee button text
        addInvitePlayFeeText();

        // add join other text
        addInviteOtherText();

        // Execute RemoteFetchDataTask AsyncTask
      //  new RemoteFetchDataTask().execute();
    }

    /*

    // RemoteFetchDataTask AsyncTask
    private class RemoteFetchDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(MatchDetailActivity.this);
            // Set progressdialog message
            mProgressDialog.setMessage(getResources().getString(R.string.progress_local));
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            mplayerlist = new ArrayList<PlayerIcon>();
            Bitmap homeIcon = BitmapFactory.decodeResource(MatchDetailActivity.this.getResources(), R.drawable.ome_map_avataricon);

            try {
                // query player that want to join this match
                ParseQuery<Message> query =  new ParseQuery<Message>(AppConstant.OMETOPLAYMESSAGECLASSKEY);// Message.getQuery();

                //  query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                query.orderByDescending(AppConstant.OMEPARSECREATEDAT);
                query.whereEqualTo(AppConstant.OMEPARSEMESSAGEFOROBJECTIDKEY, minviteObjectID);
                query.setLimit(MAX_REPLY_MESSAGE_SEARCH_RESULTS);
                //        return query;
                mquerymessage = query.find();

               Toast.makeText(MatchDetailActivity.this, "omeid " + mquerymessage.size(),
                        Toast.LENGTH_LONG).show();

               // mquerymessage.get(1).getMessageFromUser();
                for (Message message : mquerymessage) {
                    // ParseFile image = (ParseFile) message.get("phones");
                    //  PhoneList map = new PhoneList();
                    // map.setPhone(image.getUrl());
                    String momeid      =  "tt";// message.getContent();//.getUsername();//.getString(AppConstant.OMEPARSEUSERLASTLOCATIONKEY);//AppConstant.OMEPARSEUSEROMEIDKEY);
                    //Toast.makeText(MatchDetailActivity.this, "omeid " + momeid,
                    //                 Toast.LENGTH_LONG).show();

                    PlayerIcon mplayer = new PlayerIcon(homeIcon, "test", momeid);
                    mplayerlist.add(mplayer);
                }
            } catch (ParseException e) {
                // Log.e("Error", e.getMessage());
                // e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mplayergridView    = (GridView) findViewById(R.id.match_detail_player_gridview);
            mplayerGridAdapter = new PlayerGridAdapter(MatchDetailActivity.this, R.layout.ome_activity_match_detail_grid, mplayerlist);
            mplayergridView.setAdapter(mplayerGridAdapter);

            mProgressDialog.dismiss();

            if (mplayerlist.size() == 0) {
                mplayerheadertext.setVisibility(View.GONE);
            } else {
                mplayerheadertext.setVisibility(View.VISIBLE);
            }



            mplayergridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String mUsername = mplayerlist.get(position).getTitle();
                    String mOMEID    = mplayerlist.get(position).getOMEID();

                  //  Intent invokeUserProfileActivityIntent = new Intent(MatchDetailActivity.this, ContactProfileActivity.class);
                  //  invokeUserProfileActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  //  invokeUserProfileActivityIntent.putExtra(Application.INTENT_EXTRA_USERNAME, mUsername);
                  //  invokeUserProfileActivityIntent.putExtra(Application.INTENT_EXTRA_USEROMEID, mOMEID);
                  //  startActivity(invokeUserProfileActivityIntent);
                }
            });
        }
    }
*/


    // define sport type text
    private void addSportTypeText() {
        msporttypeText = (TextView) findViewById(R.id.match_detail_sporttype);
        msporttypeText.setText(getResources().getString(R.string.OMEPARSEINVITEREQUIREMENTSPORTTYPE));
        msporttypeText = (TextView) findViewById(R.id.match_detail_sporttypeText);
        msporttypeText.setText(msporttype);

        msporttypeIcon = (ImageView) findViewById(R.id.match_detail_sporttypeicon);
        msporttypeIcon.setImageDrawable(getResources().getDrawable(Sport.msporticonarray[Integer.parseInt(msporttypevalue)]));


    }

    // define player level text
    private void addPlayerLevelText() {

        mplayerleveText = (TextView) findViewById(R.id.match_detail_playerlevel);
        mplayerleveText.setText(getResources().getString(R.string.OMEPARSEINVITEREQUIREMENTPLAYERLEVEL));
        mplayerleveText = (TextView) findViewById(R.id.match_detail_playerlevelText);
        mplayerleveText.setText(mplayerlevel);

    }

    // define player number button text
    private void addPlayerNumberText() {

        mplayernumberText = (TextView) findViewById(R.id.match_detail_playernumber);
        mplayernumberText.setText(getResources().getString(R.string.OMEPARSEINVITEREQUIREMENTPLAYERNUMBER));
        mplayernumberText = (TextView) findViewById(R.id.match_detail_playernumberText);
        mplayernumberText.setText(mplayernumber);

    }

    // define invite play time text
    private void addInviteTimeText() {

        mtimeText = (TextView) findViewById(R.id.match_detail_invitetime);
        mtimeText.setText(getResources().getString(R.string.OMEPARSEINVITEREQUIREMENTTIME));
        mtimeText = (TextView) findViewById(R.id.match_detail_invitetimeText);
        mtimeText.setText(minviteplaytime);


    }

    // define invite court text
    private void addInviteCourtText() {

        mcourtText = (TextView) findViewById(R.id.match_detail_invitecourt);
        mcourtText.setText(getResources().getString(R.string.OMEPARSEINVITEREQUIREMENTCOURT));
        mcourtText = (TextView) findViewById(R.id.match_detail_invitecourtText);
        mcourtText.setText(mcourt);

    }


    // define invite play fee text
    private void addInvitePlayFeeText() {

        minviteplayfeeText = (TextView) findViewById(R.id.match_detail_invitefee);
        minviteplayfeeText.setText(getResources().getString(R.string.OMEPARSEINVITEREQUIREMENTFEE));
        minviteplayfeeText = (TextView) findViewById(R.id.match_detail_invitefeeText);
        minviteplayfeeText.setText(minviteplayfee);
    }


    // define invite court text
    private void addInviteOtherText() {

        motherText = (TextView) findViewById(R.id.match_detail_inviteother);
        motherText.setText(getResources().getString(R.string.OMEPARSEINVITEREQUIREMENTOTHER));
        motherText = (TextView) findViewById(R.id.match_detail_inviteotherText);
        motherText.setText(mother);

    }

}