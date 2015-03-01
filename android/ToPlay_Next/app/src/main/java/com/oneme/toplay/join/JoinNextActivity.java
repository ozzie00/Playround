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

package com.oneme.toplay.join;


import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.ParseGeoPoint;


public class JoinNextActivity extends ActionBarActivity {

    private ParseGeoPoint geoPoint;

    private Uri muserAvatarUri       = null;
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
    private String mworkoutname      = null;


    public class LeftPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { getString(R.string.joininfofragment_title),
                getString(R.string.joinscoreboardfragment_title), getString(R.string.joincommentfragment_title) };

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

            case 0:
                Bundle minfobundle         = new Bundle();
                InfoFragment minfoFragment = new InfoFragment();
                minfobundle.putString(Application.INTENT_EXTRA_WORKOUTNAME, mworkoutname);
                minfobundle.putString(Application.INTENT_EXTRA_COURT, mcourt);
                minfobundle.putString(Application.INTENT_EXTRA_SPORTTYPEVALUE, msporttypevalue);
                minfobundle.putString(Application.INTENT_EXTRA_TIME, minviteplaytime);
                minfobundle.putString(Application.INTENT_EXTRA_OTHER, mother);
                minfoFragment.setArguments(minfobundle);
               // transaction.replace(R.id.fragment_single, fragInfo);
               // transaction.commit();
                return minfoFragment;
            case 1: return new ScoreboardFragment();
            case 2: return new CommentFragment();
            default: return new InfoFragment();
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
        setContentView(R.layout.ome_activity_join_next);

       // getActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize the ViewPager and set an adapter
        ViewPager joinpager = (ViewPager)findViewById(R.id.join_next_pager);
        joinpager.setAdapter(new LeftPagerAdapter(getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip jointab = (PagerSlidingTabStrip) findViewById(R.id.join_next_fragment_tab);
        jointab.setViewPager(joinpager);
        //jointab.setTextColor(getResources().getColor(R.color.tab_focus));

        // fetch the clicked location
        Intent intent = getIntent();
        Location location = intent.getParcelableExtra(Application.INTENT_EXTRA_LOCATION);
        geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            muserAvatarUri    = Uri.parse(extras.getString(Application.INTENT_EXTRA_USERICONPATH));
            mhostUser         = extras.getString(Application.INTENT_EXTRA_USEROBJECTID);
            mhostUsername     = extras.getString(Application.INTENT_EXTRA_USERNAME);
            mOMEID            = extras.getString(Application.INTENT_EXTRA_USEROMEID);
            mworkoutname      = extras.getString(Application.INTENT_EXTRA_WORKOUTNAME);
            msporttypevalue   = extras.getString(Application.INTENT_EXTRA_SPORTTYPEVALUE);
            msporttype        = extras.getString(Application.INTENT_EXTRA_SPORTTYPE);
            mplayernumber     = extras.getString(Application.INTENT_EXTRA_PLAYERNUMBER);
            mplayerlevel      = extras.getString(Application.INTENT_EXTRA_PLAYERLEVEL);
            minviteplaytime   = extras.getString(Application.INTENT_EXTRA_TIME);
            mcourt            = extras.getString(Application.INTENT_EXTRA_COURT);
            minviteplayfee    = extras.getString(Application.INTENT_EXTRA_FEE);
            mother            = extras.getString(Application.INTENT_EXTRA_OTHER);
            msubmittime       = extras.getString(Application.INTENT_EXTRA_SUBMITTIME);
            minviteObjectID   = extras.getString(Application.INTENT_EXTRA_INVITEOBJECTID);
        }
    }

}
