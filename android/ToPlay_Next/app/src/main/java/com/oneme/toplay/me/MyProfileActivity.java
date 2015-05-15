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

import android.content.ComponentName;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.astuetz.PagerSlidingTabStrip;
import com.oneme.toplay.Application;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.R;
import com.oneme.toplay.base.LoadImageFromParseCloud;

import com.oneme.toplay.ui.BaseActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;



public class MyProfileActivity extends BaseActivity {

    private String mmemberusername = null;

    public class LeftPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {getResources().getString(R.string.myfollowingfragment_title), getResources().getString(R.string.myfollowerfragment_title)};

        public LeftPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {
                case 0: {
                    Bundle mfollowingbundle            = new Bundle();
                    MyFollowingFragment mfollowingFragment = new MyFollowingFragment();
                    if (mmemberusername != null) {
                        mfollowingbundle.putString(Application.INTENT_EXTRA_USERNAME, mmemberusername);
                    }
                    mfollowingFragment.setArguments(mfollowingbundle);
                    return mfollowingFragment;
                }
                case 1: {
                    Bundle mfollowerbundle               = new Bundle();
                    MyFollowerFragment mfollowerFragment = new MyFollowerFragment();
                    if (mmemberusername != null) {
                        mfollowerbundle.putString(Application.INTENT_EXTRA_USERNAME, mmemberusername);
                    }
                    mfollowerFragment.setArguments(mfollowerbundle);
                    return mfollowerFragment;
                }
                default:
                    Bundle mfollowingbundle                = new Bundle();
                    MyFollowingFragment mfollowingFragment = new MyFollowingFragment();
                    if (mmemberusername != null) {
                        mfollowingbundle.putString(Application.INTENT_EXTRA_USERNAME, mmemberusername);
                    }
                    mfollowingFragment.setArguments(mfollowingbundle);
                    return mfollowingFragment;
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_myprofile);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
               // navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(MyProfileActivity.this,
               //         MeActivity.class)));
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setTitle(getResources().getString(R.string.meprofilreactivity_title));

        // Initialize the ViewPager and set an adapter
        ViewPager profilepager = (ViewPager)findViewById(R.id.myprofile_pager);
        profilepager.setAdapter(new LeftPagerAdapter(getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip profiletabs = (PagerSlidingTabStrip) findViewById(R.id.myprofile_fragment_tab);
        profiletabs.setViewPager(profilepager);
        profiletabs.setTextColor(getResources().getColor(R.color.grey_dark));

        Bundle extras     = getIntent().getExtras();

        if (extras != null) {
            mmemberusername            = extras.getString(Application.INTENT_EXTRA_USERNAME);
            final ImageView avatarView = (ImageView)findViewById(R.id.myprofile_avatar_icon_view);
            TextView usernameView      = (TextView)findViewById(R.id.myprofile_username_view);

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
            query.include(AppConstant.OMEPARSEUSERKEY);
            query.whereEqualTo(AppConstant.OMEPARSEUSERNAMEKEY, mmemberusername);
            query.setLimit(1);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    if (e == null) {
                        LoadImageFromParseCloud.getAvatar(getApplicationContext(),list.get(0), avatarView);
                    }
                }
            });

            usernameView.setText(mmemberusername);
        }
    }

}
