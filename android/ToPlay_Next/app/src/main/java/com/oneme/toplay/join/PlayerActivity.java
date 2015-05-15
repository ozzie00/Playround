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
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Following;
import com.oneme.toplay.base.LoadImageFromParseCloud;
import com.oneme.toplay.database.FollowingPlayer;
import com.oneme.toplay.database.Group;

import com.oneme.toplay.me.MyProfileActivity;
import com.oneme.toplay.ui.BaseActivity;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.FindCallback;

import java.util.ArrayList;
import java.util.List;


public class PlayerActivity extends BaseActivity {

    private static final String TAG    = "PlayerActivity";
    private ParseUser muser            = ParseUser.getCurrentUser();
    private String minviteObjectID     = null;
    private String mgroupAdminUsername = null;
    private int mcount                 = 0;
    private Boolean isFollowing        = false;
    private Drawable mfollowingdrawable= null;
    private Drawable mfollowdrawable   = null;

    // Places Listview
    ListView msearchresult;
    Group mselectgroup;

    //public String data;
    public ArrayList<Group> msuggest;
    public ArrayAdapter<Group> madapter;

    private static final int MAX_GROUP_SEARCH_RESULTS = 1000;

    // Adapter for the Parse query
    private ParseQueryAdapter<Group> groupQueryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_player);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // finish();
                navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(PlayerActivity.this,
                        JoinNextActivity.class)));
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mfollowingdrawable = getResources().getDrawable(R.drawable.ome_activity_following_background);
        mfollowdrawable    = getResources().getDrawable(R.drawable.ome_activity_follow_background);

        mselectgroup = new Group();
        msuggest     = new ArrayList<Group>();

        // fetch input extra
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            minviteObjectID     = extras.getString(Application.INTENT_EXTRA_INVITEOBJECTID);
            mgroupAdminUsername = extras.getString(Application.INTENT_EXTRA_USERNAME);
        }

        // setting search name list view
        msearchresult = (ListView) findViewById(R.id.player_list);
        msearchresult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Group group      = madapter.getItem(position);
                String mmemberusername = group.getMemberUsername();

                Intent invokeMyProfileActivityIntent = new Intent(PlayerActivity.this, MyProfileActivity.class);
                invokeMyProfileActivityIntent.putExtra(Application.INTENT_EXTRA_USERNAME, mmemberusername);
                startActivity(invokeMyProfileActivityIntent);
            }
        });

        //new getGroupRecord().execute(AppConstant.OMEPARSENULLSTRING);
       // if (muser != null) {
            new getGroupRecord().execute(muser);
       // }

    }


    class getGroupRecord extends AsyncTask<ParseUser,String,String> {
        private ProgressDialog playerLoadDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            playerLoadDialog = new ProgressDialog(PlayerActivity.this);
            playerLoadDialog.show();
        }

        @Override
        protected String doInBackground(ParseUser... key) {
            //mnameKey = key[0];
            //mnameKey = mnameKey.trim();
            final ParseUser user = key[0];
            msuggest = new ArrayList<Group>();

            runOnUiThread(new Runnable() {
                public void run() {

                    ParseQuery<Group> query = Group.getQuery();
                    query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                    query.include(AppConstant.OMEPARSEGROUPMEMBERUSERKEY);
                    query.whereEqualTo(AppConstant.OMEPARSEGROUPPARENTIDKEY, minviteObjectID);
                    query.whereEqualTo(AppConstant.OMEPARSEGROUPADMINNAMEKEY, mgroupAdminUsername);
                    query.orderByDescending(AppConstant.OMEPARSECREATEDATKEY);
                    query.setLimit(MAX_GROUP_SEARCH_RESULTS);
                    query.findInBackground(new FindCallback<Group>() {
                        public void done(List<Group> groupList, ParseException e) {
                            if (e == null) {
                                // msuggest = new ArrayList<Group>(groupList.size());
                                for (Group group : groupList) {
                                    Group mgroup = new Group();
                                    //mgroup.setGroupAdmin(group.getGroupAdmin());
                                    mgroup.setGroupAdminUsername(group.getGroupAdminUsername());
                                    mgroup.setParentObjectId(group.getParentObjectId());
                                    mgroup.setGroupPulic(group.getGroupPublic());
                                    mgroup.setMemberUser(group.getMemberUser());
                                    mgroup.setMemberUsername(group.getMemberUsername());
                                    mgroup.setGroupWorkout(group.getGroupWorkout());
                                    mgroup.setGroupSport(group.getGroupSport());
                                    mgroup.setSportTypeValue(group.getSportTypeValue());
                                    mgroup.setMemberJoinTime(group.getMemberJoinTime());
                                    msuggest.add(mgroup);
                                }

                                if (msuggest.size() >= 1) {
                                    madapter = new resultListAdapter(getApplicationContext(), msuggest, user);
                                    msearchresult.setAdapter(madapter);
                                    madapter.notifyDataSetChanged();
                                }

                            } else {


                            }
                        }
                    });


                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            playerLoadDialog.dismiss();
        }

    }


    class resultListAdapter extends ArrayAdapter<Group> {

        LayoutInflater mInflater;

        Group mgroup;
        ArrayList<Group> mdata;
        ParseUser user;

        public resultListAdapter(Context c, ArrayList<Group> data, ParseUser user){
            super(c, 0);
            this.mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mdata     = data;
            this.user      = user;
        }

        @Override
        public int getCount() {

            if(mdata!=null){
                return mdata.size();
            }else{
                return 0;
            }
        }

        public void setData(ArrayList<Group> mPpst) {
            //contains class items data.
            mdata = mPpst;
        }

        @Override
        public Group getItem(int arg0) {
            // TODO Auto-generated method stub
            //return arg0;
            return mdata.get(arg0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if(mdata == null ){
                return null;
            }

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.ome_activity_player_item, null);
                convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder             = new ViewHolder();
                holder.avatar      = (ImageView)convertView.findViewById(R.id.player_avatar_icon_view);
                holder.name        = (TextView) convertView.findViewById(R.id.player_username_view);
                holder.description = (TextView) convertView.findViewById(R.id.player_description_view);
                holder.follow      = (TextView) convertView.findViewById(R.id.player_follow_view);

                // need set imagebutton focusable is false, then onItemClick can work on list view
                holder.follow.setFocusable(false);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            mgroup                      = getItem(position);
            final ParseUser mmemberUser = mgroup.getMemberUser();
            String mmemeberUsername     = mmemberUser.getUsername();

            if (mmemberUser != null) {
                LoadImageFromParseCloud.getAvatar(PlayerActivity.this, mmemberUser, holder.avatar);
            }

            if (mmemeberUsername != null) {
                holder.name.setText(mmemeberUsername);
            }

            // check if current user is null
            if (mmemberUser != null && user != null) {
                ParseQuery<FollowingPlayer> query = FollowingPlayer.getQuery();
                query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                query.include(AppConstant.OMEPARSEFOLLOWINGPLAYERUSERKEY);
                query.whereEqualTo(AppConstant.OMEPARSEFOLLOWINGPLAYERUSERNAMEKEY, mmemeberUsername);
                query.whereEqualTo(AppConstant.OMEPARSEFOLLOWERPLAYERUSERNAMEKEY, user.getUsername());
                query.countInBackground(new CountCallback() {
                    @Override
                    public void done(int i, ParseException e) {
                        mcount = i;
                        if (mcount >= 1) {
                            isFollowing = true;
                            holder.follow.setText(getResources().getString(R.string.OMEPARSEFOLLOWING));
                            holder.follow.setTextColor(getResources().getColor(R.color.white_absolute));

                            // compatible for android version prior to api 16
                            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                                holder.follow.setBackground(mfollowingdrawable);
                            } else {
                                holder.follow.setBackgroundDrawable(mfollowingdrawable);
                            }

                            holder.follow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (isFollowing) {
                                        isFollowing = false;
                                        Following.unfollowPlayer(mmemberUser, user);
                                        holder.follow.setText(getResources().getString(R.string.OMEPARSEFOLLOW));
                                        holder.follow.setTextColor(getResources().getColor(R.color.playround_default));

                                        // compatible for android version prior to api 16
                                        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                                            holder.follow.setBackground(mfollowdrawable);
                                        } else {
                                            holder.follow.setBackgroundDrawable(mfollowdrawable);
                                        }
                                    } else {
                                        isFollowing = true;
                                        Following.followingPlayer(mmemberUser, user);
                                        holder.follow.setText(getResources().getString(R.string.OMEPARSEFOLLOWING));
                                        holder.follow.setTextColor(getResources().getColor(R.color.white_absolute));

                                        // compatible for android version prior to api 16
                                        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                                            holder.follow.setBackground(mfollowingdrawable);
                                        } else {
                                            holder.follow.setBackgroundDrawable(mfollowingdrawable);
                                        }
                                    }
                                }
                            });
                        } else {
                            isFollowing = false;
                            holder.follow.setText(getResources().getString(R.string.OMEPARSEFOLLOW));
                            holder.follow.setTextColor(getResources().getColor(R.color.playround_default));

                            // compatible for android version prior to api 16
                            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                                holder.follow.setBackground(mfollowdrawable);
                            } else {
                                holder.follow.setBackgroundDrawable(mfollowdrawable);
                            }

                            holder.follow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!isFollowing) {
                                        isFollowing = true;
                                        Following.followingPlayer(mmemberUser, user);
                                        holder.follow.setText(getResources().getString(R.string.OMEPARSEFOLLOWING));
                                        holder.follow.setTextColor(getResources().getColor(R.color.white_absolute));

                                        // compatible for android version prior to api 16
                                        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                                            holder.follow.setBackground(mfollowingdrawable);
                                        } else {
                                            holder.follow.setBackgroundDrawable(mfollowingdrawable);
                                        }
                                    } else {
                                        isFollowing = false;
                                        Following.unfollowPlayer(mmemberUser, user);
                                        holder.follow.setText(getResources().getString(R.string.OMEPARSEFOLLOW));
                                        holder.follow.setTextColor(getResources().getColor(R.color.playround_default));

                                        // compatible for android version prior to api 16
                                        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                                            holder.follow.setBackground(mfollowdrawable);
                                        }
                                        else {
                                            holder.follow.setBackgroundDrawable(mfollowdrawable);
                                        }
                                    }
                                }
                            });
                        }

                    }
                });


            } else if (mmemberUser != null && user == null) {
                // current user does not login
                holder.follow.setText(getResources().getString(R.string.OMEPARSEFOLLOWING));
                holder.follow.setTextColor(getResources().getColor(R.color.white_absolute));

                // compatible for android version prior to api 16
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                    holder.follow.setBackground(mfollowingdrawable);
                } else {
                    holder.follow.setBackgroundDrawable(mfollowingdrawable);
                }

                holder.follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.widget.Toast.makeText(PlayerActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT),
                                android.widget.Toast.LENGTH_LONG).show();
                    }
                });


            }

            return convertView;
        }

        class ViewHolder {
            ImageView avatar;
            TextView name;
            TextView description;
            TextView follow;
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public Filter getFilter() {
            Filter myFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();

                    // actually this constraint is no useful
                    if (constraint != null) {
                        filterResults.values = mdata;
                        filterResults.count  = mdata.size();
                        return filterResults;
                    } else {
                        filterResults.values = mdata;
                        filterResults.count  = mdata.size();
                        return filterResults;
                    }
                }

                @Override
                protected void publishResults(CharSequence contraint, FilterResults results) {
                    ArrayList<Group> filteredList = (ArrayList<Group>) results.values;
                    if(results != null && results.count > 0) {
                        clear();
                        notifyDataSetChanged();
                        for (Group group : filteredList) {
                            add(group);
                        }
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return myFilter;
        }
    }






}
