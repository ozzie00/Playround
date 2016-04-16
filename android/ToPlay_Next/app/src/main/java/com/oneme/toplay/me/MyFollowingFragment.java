/*
* Copyright 2015 OneME
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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Following;
import com.oneme.toplay.base.LoadImageFromParseCloud;
import com.oneme.toplay.database.FollowingPlayer;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MyFollowingFragment extends Fragment {
    private String mmemberusername = null;
    private ParseUser muser        = ParseUser.getCurrentUser();

    private TextView mfollowingnumber = null;
    private TextView mfollowingtext   = null;

    ListView msearchresult;

    private ArrayList<FollowingPlayer> msuggest;
    private ArrayAdapter<FollowingPlayer> madapter;

    private static final int MAX_FOLLOWING_SEARCH_RESULTS = 1000;

    private String mnameKey             = null;
    private int mcount                  = 0;
    private Boolean isFollowing         = false;
    private Drawable mfollowingdrawable = null;
    private Drawable mfollowdrawable    = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle   = this.getArguments();
        mmemberusername = bundle.getString(Application.INTENT_EXTRA_USERNAME);

        View rootView   = inflater.inflate(R.layout.ome_activity_myfollowing_fragment, container, false);

        mfollowingdrawable = getResources().getDrawable(R.drawable.ome_activity_following_background);
        mfollowdrawable    = getResources().getDrawable(R.drawable.ome_activity_follow_background);

        msuggest        = new ArrayList<FollowingPlayer>();
        madapter        = new resultListAdapter(getActivity(), msuggest, muser);

        // setting search name list view
        msearchresult = (ListView)rootView.findViewById(R.id.myprofile_follow_list);


        msearchresult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final FollowingPlayer mfollowing     = madapter.getItem(position);
                String mmemberusername               = mfollowing.getFollowingUsername();
                Intent invokeMyProfileActivityIntent = new Intent(getActivity(), MyProfileActivity.class);

                invokeMyProfileActivityIntent.putExtra(Application.INTENT_EXTRA_USERNAME, mmemberusername);
                startActivity(invokeMyProfileActivityIntent);
            }
        });

        mfollowingnumber = (TextView)rootView.findViewById(R.id.myprofile_count_number);
        mfollowingtext   = (TextView)rootView.findViewById(R.id.myprofile_count_text);



        if (mmemberusername != null) {
            new getFollowingAsyncTask().execute(mmemberusername);
        }

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    class getFollowingAsyncTask extends AsyncTask<String,String,String> {

        private ProgressDialog loadDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... key) {
            mnameKey = key[0];
            mnameKey = mnameKey.trim();

            Activity mactivity = getActivity();

            if (mactivity != null && isAdded()) {
                mactivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ParseQuery<FollowingPlayer> followingquery = FollowingPlayer.getQuery();
                        followingquery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                        followingquery.include(AppConstant.OMEPARSEFOLLOWINGPLAYERUSERKEY);
                        followingquery.include(AppConstant.OMEPARSEFOLLOWERPLAYERUSERKEY);
                        followingquery.whereEqualTo(AppConstant.OMEPARSEFOLLOWERPLAYERUSERNAMEKEY, mnameKey);
                        followingquery.orderByDescending(AppConstant.OMEPARSECREATEDATKEY);
                        followingquery.setLimit(MAX_FOLLOWING_SEARCH_RESULTS);
                        followingquery.findInBackground(new FindCallback<FollowingPlayer>() {
                            public void done(List<FollowingPlayer> followingList, ParseException e) {
                                if (e == null) {
                                    mfollowingnumber.setText(Integer.toString(followingList.size()));
                                    mfollowingtext.setText(getResources().getString(R.string.OMEPARSEFOLLOWINGLOWERCASE));
                                    for (FollowingPlayer following : followingList) {
                                        FollowingPlayer mfollowing = new FollowingPlayer();
                                        mfollowing.setFollowingUser(following.getFollowingUser());
                                        mfollowing.setFollowingUsername(following.getFollowingUsername());
                                        mfollowing.setFollowerUser(following.getFollowerUser());
                                        mfollowing.setFollowerUsername(following.getFollowerUsername());

                                        if (msuggest.size() < MAX_FOLLOWING_SEARCH_RESULTS) {
                                            msuggest.add(mfollowing);
                                        }
                                    }
                                    msearchresult.setAdapter(madapter);
                                    madapter.notifyDataSetChanged();

                                } else {

                                }
                            }
                        });

                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Activity activity = getActivity();
            if(isAdded() && activity != null){
                return;
            }
        }
    }


    class resultListAdapter extends ArrayAdapter<FollowingPlayer> {
        LayoutInflater mInflater;

        FollowingPlayer mfollowing;
        ArrayList<FollowingPlayer> mdata;
        ParseUser user;

        public resultListAdapter(Context context, ArrayList<FollowingPlayer> data, ParseUser user){
            super(context, 0);
            this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        public void setData(ArrayList<FollowingPlayer> mPpst) {
            //contains class items data.
            mdata = mPpst;
        }

        @Override
        public FollowingPlayer getItem(int arg0) {
            // TODO Auto-generated method stub
            //return arg0;
            return mdata.get(arg0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            ///int type = getItemViewType(arg0);
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
                holder.avatar      = (ImageView) convertView.findViewById(R.id.player_avatar_icon_view);
                holder.name        = (TextView) convertView.findViewById(R.id.player_username_view);
                holder.description = (TextView) convertView.findViewById(R.id.player_description_view);
                holder.follow      = (TextView) convertView.findViewById(R.id.player_follow_view);

                // need set follow focusable is false, then onItemClick can work on list view
                holder.follow.setFocusable(false);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            mfollowing                     = getItem(position);
            final ParseUser mfollowingUser = mfollowing.getFollowingUser();
            String mfollowerUsename        = mfollowingUser.getUsername();

            if (mfollowingUser != null) {
                LoadImageFromParseCloud.getAvatar(getActivity(), mfollowingUser, holder.avatar);
            }
            if (mfollowerUsename!=null) {
                holder.name.setText(mfollowerUsename);
            }

            // check if current user is null
            if (mfollowingUser != null && user != null) {
                ParseQuery<FollowingPlayer> query = FollowingPlayer.getQuery();
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                query.include(AppConstant.OMEPARSEFOLLOWINGPLAYERUSERKEY);
                query.whereEqualTo(AppConstant.OMEPARSEFOLLOWINGPLAYERUSERNAMEKEY, mfollowerUsename);
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
                                        Following.unfollowPlayer(mfollowingUser, user);
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
                                        Following.followingPlayer(mfollowingUser, user);
                                        holder.follow.setText(getResources().getString(R.string.OMEPARSEFOLLOWING));
                                        holder.follow.setTextColor(getResources().getColor(R.color.white_absolute));
                                        holder.follow.setBackground(getResources().getDrawable(R.drawable.ome_activity_following_background));

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
                                        Following.followingPlayer(mfollowingUser, user);
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
                                        Following.unfollowPlayer(mfollowingUser, user);
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


            } else if (mfollowingUser != null && user == null) {
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
                        android.widget.Toast.makeText(getActivity(), getResources().getString(R.string.OMEPARSEINVITELOGINALERT),
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
                    ArrayList<FollowingPlayer> filteredList = (ArrayList<FollowingPlayer>) results.values;
                    if(results != null && results.count > 0) {
                        clear();
                        notifyDataSetChanged();
                        for (FollowingPlayer following : filteredList) {
                            add(following);
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