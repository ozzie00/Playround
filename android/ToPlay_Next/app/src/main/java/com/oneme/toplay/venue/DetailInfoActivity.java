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

package com.oneme.toplay.venue;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Homeing;
import com.oneme.toplay.base.IntentExtraToVenue;
import com.oneme.toplay.base.LoadImageFromParseCloud;
import com.oneme.toplay.base.VenueToIntentExtra;
import com.oneme.toplay.database.Photo;
import com.oneme.toplay.database.PhotoLink;
import com.oneme.toplay.database.Sport;
import com.oneme.toplay.database.Venue;
import com.oneme.toplay.database.VenueAsHome;
import com.oneme.toplay.database.VenueComment;

import com.oneme.toplay.ui.BaseActivity;
import com.oneme.toplay.ui.LocalNextActivity;
import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;


public class DetailInfoActivity extends BaseActivity {

    private Venue mvenue = null;

    private String minviteObjectID   = null;

    private ParseUser muser          = ParseUser.getCurrentUser();
    private String musername         = null;
    private ParseQueryAdapter<VenueComment> commentQueryAdapter;

    private Boolean isAsHome           = false;
    private Boolean isBooked           = false;
    private Drawable mashomedrawable   = null;
    private Drawable mmyhomedrawable   = null;
    private Drawable mbookingdrawable  = null;
    private Drawable mbookeddrawable   = null;

    private int mcommentnumber      = 0;
    private int mcount              = 0;
    private int mlikenumber         = 0;
    private TextView mgroup;
    private TextView mcomment;
    private TextView mlike;
    private ImageView mlikeimage;
    private boolean mylike  = false;
    private ImageView mphoto;
    private ParseQuery<PhotoLink> mlinkquery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_venue_detail_info);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 finish();
                //navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(DetailInfoActivity.this,
                //        LocalNextActivity.class)));
            }
        });

       // getActionBar().show();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mashomedrawable = getResources().getDrawable(R.drawable.ome_activity_following_background);
        mmyhomedrawable = getResources().getDrawable(R.drawable.ome_activity_follow_background);

        mbookingdrawable = getResources().getDrawable(R.drawable.ome_activity_following_background);
        mbookeddrawable  = getResources().getDrawable(R.drawable.ome_activity_follow_background);

        if (muser != null) {
            musername = muser.getUsername();
        }

        // get ready to extra data
        Intent detailIntent = getIntent();
        mvenue = new Venue();

        if (mvenue != null && detailIntent != null) {
            IntentExtraToVenue.getExtra(detailIntent, mvenue);

            mphoto = (ImageView) findViewById(R.id.venue_detail_info_photo);
           // mphoto.setVisibility(View.GONE);
            mlinkquery = PhotoLink.getQuery();
            mlinkquery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
            mlinkquery.whereEqualTo(AppConstant.OMETOPLAYPHOTOLINKPHOTOTOBJECTKEY, mvenue.getObjectId());

            android.util.Log.d(" mlinkquery ", " count ");

            mlinkquery.countInBackground(new CountCallback() {
                @Override
                public void done(int i, ParseException e) {
                    if (i >= 1 && e == null) {

                        android.util.Log.d(" mlinkquery ", " count >= 1");

                        mlinkquery.getFirstInBackground(new GetCallback<PhotoLink>() {
                            @Override
                            public void done(PhotoLink photoLink, ParseException e) {
                                ParseQuery<Photo> query = Photo.getQuery();
                                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                                query.getInBackground(photoLink.getPhotoObjectId(), new GetCallback<Photo>() {
                                    @Override
                                    public void done(Photo photo, ParseException e) {
                                        if (e == null) {
                                            mphoto.setVisibility(View.VISIBLE);
                                            LoadImageFromParseCloud.getPhoto(DetailInfoActivity.this, photo, mphoto);
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        mphoto.setVisibility(View.GONE);
                    }
                }
            });




            TextView venuename = (TextView) findViewById(R.id.venue_detail_info_name);
            venuename.setText(mvenue.getName());

            ImageView venuetype = (ImageView)findViewById(R.id.venue_detail_info_sport);
            int index = Sport.msportarraylist.indexOf(mvenue.getType());
            if (index >= 0) {
                venuetype.setImageResource(Sport.msporticonarray[index]);
            }

            TextView venuelocation = (TextView) findViewById(R.id.venue_detail_info_location);
            venuelocation.setText(mvenue.getAddress());

            ImageButton venuephone = (ImageButton) findViewById(R.id.venue_detail_info_phone_button);
            if (mvenue.getPhone() != null && mvenue.getPhone().length() > AppConstant.OMEPHONEMINIMUMLENGTH){
                venuephone.setVisibility(View.VISIBLE);

                venuephone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent invokePhoneCall = new Intent(Intent.ACTION_CALL);
                        invokePhoneCall.setData(Uri.parse(AppConstant.OMEPARSEINVOKECALLPHONE + mvenue.getPhone()));
                        startActivity(invokePhoneCall);
                    }
                });

            }



            TextView courtnumber = (TextView) findViewById(R.id.venue_detail_info_header_court_number);
            courtnumber.setText(mvenue.getCourtNumber());

            TextView lightnumber = (TextView) findViewById(R.id.venue_detail_info_header_lighted_number);
            lightnumber.setText(mvenue.getLighted());

            TextView indoornumber = (TextView) findViewById(R.id.venue_detail_info_header_indoor_number);
            indoornumber.setText(mvenue.getIndoor());

            //TextView playernumber = (TextView) findViewById(R.id.venue_detail_info_header_player_number);
            //playernumber.setText(mvenue.getPlayerAsHome(););

            TextView maccess = (TextView) findViewById(R.id.venue_detail_info_header_access);
            String maccessvalue = mvenue.getPublic();
            if (maccessvalue.equals(AppConstant.OMEPARSEVENUEACCESSPUBLIC)) {
                maccess.setText(getResources().getString(R.string.OMEPARSEVENUEACCESSPUBLIC));
            } else if (maccessvalue.equals(AppConstant.OMEPARSEVENUEACCESSPRIVATE)) {
                maccess.setText(getResources().getString(R.string.OMEPARSEVENUEACCESSPRIVATE));
            }

            TextView mdescritpion = (TextView) findViewById(R.id.venue_detail_info_description_content);
            mdescritpion.setText(mvenue.getDescription());

            // set comment number
            mcomment = (TextView)findViewById(R.id.venue_detail_info_comment_number);

            ParseQuery<VenueComment> commentquery = VenueComment.getQuery();
            commentquery.whereEqualTo(AppConstant.OMEPARSEINVITECOMMENTPARENTIDKEY, minviteObjectID);
            commentquery.countInBackground(new CountCallback() {
                public void done(int count, ParseException e) {
                    if (e == null) {
                        mcommentnumber = count;
                        mcomment.setText(Integer.toString(mcommentnumber));
                    } else {
                        mcommentnumber = 0;
                        mcomment.setText(Integer.toString(mcommentnumber));
                    }
                }
            });

            // set comment block
            LinearLayout mcommentblock = (LinearLayout)findViewById(R.id.venue_detail_info_comment_block);
            mcommentblock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent invokeCommentActivityIntent = new Intent(DetailInfoActivity.this, com.oneme.toplay.join.CommentActivity.class);
                    invokeCommentActivityIntent.putExtra(Application.INTENT_EXTRA_INVITEOBJECTID, mvenue.getObjectId());
                    startActivity(invokeCommentActivityIntent);

                }
            });

            // set as home textview
            final TextView venueashome = (TextView) findViewById(R.id.venue_detail_info_as_home);

            // check if current user is null
            if (muser != null) {
                ParseQuery<VenueAsHome> query = VenueAsHome.getQuery();
                query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                query.whereEqualTo(AppConstant.OMEPARSEVENUEHOMEPARENTIDKEY, mvenue.getObjectId());
                query.whereEqualTo(AppConstant.OMEPARSEVENUEHOMEAUTHORNAMEKEY, musername);
                query.countInBackground(new CountCallback() {
                    @Override
                    public void done(int i, ParseException e) {
                        mcount = i;
                        if (mcount >= 1) {
                            isAsHome = true;
                            venueashome.setText(getResources().getString(R.string.OMEPARSEMYHOMEFIELD));
                            venueashome.setTextColor(getResources().getColor(R.color.playround_default));

                            // compatible for android version prior to api 16
                            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                                venueashome.setBackground(mmyhomedrawable);
                            } else {
                                venueashome.setBackgroundDrawable(mmyhomedrawable);
                            }

                            venueashome.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (isAsHome) {
                                        isAsHome = false;
                                        Homeing.cancelAsHome(mvenue, muser);
                                        venueashome.setText(getResources().getString(R.string.OMEPARSESELECTASHOMEFIELD));
                                        venueashome.setTextColor(getResources().getColor(R.color.white_absolute));

                                        // compatible for android version prior to api 16
                                        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                                            venueashome.setBackground(mashomedrawable);
                                        } else {
                                            venueashome.setBackgroundDrawable(mashomedrawable);
                                        }
                                    } else {
                                        isAsHome = true;
                                        Homeing.setAsHome(mvenue, muser);
                                        venueashome.setText(getResources().getString(R.string.OMEPARSEMYHOMEFIELD));
                                        venueashome.setTextColor(getResources().getColor(R.color.playround_default));

                                        // compatible for android version prior to api 16
                                        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                                            venueashome.setBackground(mmyhomedrawable);
                                        } else {
                                            venueashome.setBackgroundDrawable(mmyhomedrawable);
                                        }
                                    }
                                }
                            });
                        } else {
                            isAsHome = false;
                            venueashome.setText(getResources().getString(R.string.OMEPARSESELECTASHOMEFIELD));
                            venueashome.setTextColor(getResources().getColor(R.color.white_absolute));

                            // compatible for android version prior to api 16
                            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                                venueashome.setBackground(mashomedrawable);
                            } else {
                                venueashome.setBackgroundDrawable(mashomedrawable);
                            }

                            venueashome.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!isAsHome) {
                                        isAsHome = true;
                                        Homeing.setAsHome(mvenue, muser);
                                        venueashome.setText(getResources().getString(R.string.OMEPARSEMYHOMEFIELD));
                                        venueashome.setTextColor(getResources().getColor(R.color.playround_default));

                                        // compatible for android version prior to api 16
                                        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                                            venueashome.setBackground(mmyhomedrawable);
                                        } else {
                                            venueashome.setBackgroundDrawable(mmyhomedrawable);
                                        }
                                    } else {
                                        isAsHome = false;
                                        Homeing.cancelAsHome(mvenue, muser);
                                        venueashome.setText(getResources().getString(R.string.OMEPARSESELECTASHOMEFIELD));
                                        venueashome.setTextColor(getResources().getColor(R.color.white_absolute));

                                        // compatible for android version prior to api 16
                                        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                                            venueashome.setBackground(mashomedrawable);
                                        }
                                        else {
                                            venueashome.setBackgroundDrawable(mashomedrawable);
                                        }
                                    }
                                }
                            });
                        }

                    }
                });


            } else {
                // current user does not login
                venueashome.setText(getResources().getString(R.string.OMEPARSESELECTASHOMEFIELD));
                venueashome.setTextColor(getResources().getColor(R.color.white_absolute));

                // compatible for android version prior to api 16
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                    venueashome.setBackground(mashomedrawable);
                } else {
                    venueashome.setBackgroundDrawable(mashomedrawable);
                }

                venueashome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.widget.Toast.makeText(DetailInfoActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT),
                                android.widget.Toast.LENGTH_LONG).show();
                    }
                });


            }

            // set booking textview
            final TextView bookingvenue = (TextView) findViewById(R.id.venue_detail_info_booking);

            // check if current user is null
            if (muser != null && mvenue != null) {

                bookingvenue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent invokeBookingVenueActivityIntent = new Intent(DetailInfoActivity.this, BookingActivity.class);
                        VenueToIntentExtra.putExtra(invokeBookingVenueActivityIntent, mvenue);
                        startActivity(invokeBookingVenueActivityIntent);
                    }
                });

            } else {

                Toast.makeText(DetailInfoActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT), Toast.LENGTH_LONG).show();
            }

        }

    }





}
