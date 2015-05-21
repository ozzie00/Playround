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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Following;
import com.oneme.toplay.base.LoadImageFromParseCloud;
import com.oneme.toplay.database.FollowingPlayer;
import com.oneme.toplay.database.Group;
import com.oneme.toplay.database.InviteLike;

import com.oneme.toplay.me.MyProfileActivity;
import com.oneme.toplay.ui.BaseActivity;
import com.parse.FindCallback;
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

import java.util.ArrayList;
import java.util.List;

public class LikeActivity extends BaseActivity {

    private static final String TAG = "LikeActivity";

    private ParseUser muser         = ParseUser.getCurrentUser();

    private String minviteObjectID  = null;

    private int mcount              = 0;
    private Boolean isFollowing     = false;
    private Drawable mfollowingdrawable= null;
    private Drawable mfollowdrawable   = null;


    // Places Listview
    ListView msearchresult;
    InviteLike mselectlike;

    //public String data;
    public ArrayList<InviteLike> msuggest;
    public ArrayAdapter<InviteLike> madapter;

    private static final int MAX_LIKE_SEARCH_RESULTS = 1000;


    // Adapter for the Parse query
    private ParseQueryAdapter<InviteLike> likeQueryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_join_like);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(LikeActivity.this,
                //        JoinNextActivity.class)));
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mfollowingdrawable = getResources().getDrawable(R.drawable.ome_activity_following_background);
        mfollowdrawable    = getResources().getDrawable(R.drawable.ome_activity_follow_background);


        mselectlike = new InviteLike();
        msuggest    = new ArrayList<InviteLike>();

        // fetch input extra
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            minviteObjectID = extras.getString(Application.INTENT_EXTRA_INVITEOBJECTID);
        }

        // setting search name list view
        msearchresult = (ListView) findViewById(R.id.join_like_list);
        msearchresult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final InviteLike mlike = madapter.getItem(position);
                String mmemberusername = mlike.getAuthorUsername();

                Intent invokeMyProfileActivityIntent = new Intent(LikeActivity.this, MyProfileActivity.class);
                invokeMyProfileActivityIntent.putExtra(Application.INTENT_EXTRA_USERNAME, mmemberusername);
                startActivity(invokeMyProfileActivityIntent);
            }
        });

        //if (muser != null) {
            new getInviteLikeRecord().execute(muser);
        //}

    }

    class getInviteLikeRecord extends AsyncTask<ParseUser,String,String> {
        private ProgressDialog playerLoadDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            playerLoadDialog = new ProgressDialog(LikeActivity.this);
            playerLoadDialog.show();
        }

        @Override
        protected String doInBackground(ParseUser... key) {
            final ParseUser user = key[0];
            msuggest             = new ArrayList<InviteLike>();

            runOnUiThread(new Runnable() {
                public void run() {

                    ParseQuery<InviteLike> query = InviteLike.getQuery();
                    query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                    query.include(AppConstant.OMEPARSEINVITELIKEAUTHORKEY);
                    query.whereEqualTo(AppConstant.OMEPARSEINVITELIKEPARENTIDKEY, minviteObjectID);
                    query.orderByDescending(AppConstant.OMEPARSECREATEDATKEY);
                    query.setLimit(MAX_LIKE_SEARCH_RESULTS);
                    query.findInBackground(new FindCallback<InviteLike>() {
                        public void done(List<InviteLike> likeList, ParseException e) {
                            if (e == null) {
                                // msuggest = new ArrayList<Group>(groupList.size());
                                for (InviteLike like : likeList) {
                                    InviteLike mlike = new InviteLike();
                                    mlike.setAuthor(like.getAuthor());
                                    mlike.setAuthorUsername(like.getAuthorUsername());
                                    mlike.setParentObjectId(like.getParentObjectId());
                                    msuggest.add(mlike);
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


    class resultListAdapter extends ArrayAdapter<InviteLike> {

        LayoutInflater mInflater;

        InviteLike mlike;
        ArrayList<InviteLike> mdata;
        ParseUser user;

        public resultListAdapter(Context c, ArrayList<InviteLike> data, ParseUser user){
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

        public void setData(ArrayList<InviteLike> mPpst) {
            //contains class items data.
            mdata = mPpst;
        }

        @Override
        public InviteLike getItem(int arg0) {
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
                convertView = mInflater.inflate(R.layout.ome_activity_join_like_item, null);
                convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder             = new ViewHolder();
                holder.avatar      = (ImageView)convertView.findViewById(R.id.join_like_avatar_icon_view);
                holder.name        = (TextView) convertView.findViewById(R.id.join_like_username_view);
                holder.description = (TextView) convertView.findViewById(R.id.join_like_whatsup_view);
                holder.follow      = (TextView) convertView.findViewById(R.id.join_like_follow_view);

                // need set imagebutton focusable is false, then onItemClick can work on list view
                holder.follow.setFocusable(false);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            mlike = getItem(position);

            if (mlike.getAuthor() != null) {
                LoadImageFromParseCloud.getAvatar(LikeActivity.this, mlike.getAuthor(), holder.avatar);
                //holder.description.setText(mgroup.getMemberUsername());
            }

            if (mlike.getAuthorUsername() != null) {
                holder.name.setText(mlike.getAuthorUsername());
            }

            if (user != null) {
                ParseQuery<FollowingPlayer> query = FollowingPlayer.getQuery();
                query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                query.include(AppConstant.OMEPARSEFOLLOWINGPLAYERUSERKEY);
                query.whereEqualTo(AppConstant.OMEPARSEFOLLOWINGPLAYERUSERNAMEKEY, mlike.getAuthorUsername());
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
                                        Following.unfollowPlayer(mlike.getAuthor(), user);
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
                                        Following.followingPlayer(mlike.getAuthor(), user);
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
                                        Following.followingPlayer(mlike.getAuthor(), user);
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
                                        Following.unfollowPlayer(mlike.getAuthor(), user);
                                        holder.follow.setText(getResources().getString(R.string.OMEPARSEFOLLOW));
                                        holder.follow.setTextColor(getResources().getColor(R.color.playround_default));

                                        // compatible for android version prior to api 16
                                        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                                            holder.follow.setBackground(mfollowdrawable);
                                        } else {
                                            holder.follow.setBackgroundDrawable(mfollowdrawable);
                                        }

                                    }
                                }
                            });
                        }

                    }
                });

            } else {
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
                        android.widget.Toast.makeText(LikeActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT),
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
                    ArrayList<InviteLike> filteredList = (ArrayList<InviteLike>) results.values;
                    if(results != null && results.count > 0) {
                        clear();
                        notifyDataSetChanged();
                        for (InviteLike like : filteredList) {
                            add(like);
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
