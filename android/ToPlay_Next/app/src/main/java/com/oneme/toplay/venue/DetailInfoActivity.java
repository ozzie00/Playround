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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Currency;
import java.util.Locale;


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

    String mcurrency       = AppConstant.OMEPARSENULLSTRING;
    String mcurrencysymbol = AppConstant.OMEPARSENULLSTRING;

    String m3rd;
    String mname;
    String mnameid;
    String mcardname;
    String mcardid;
    String mcardprice;


    String[] mprimelist     = {};
    String[] mcardnamelist  = {};
    String[] mcardidlist    = {};
    String[] mcardpricelist = {};

    private int mchoice = -1;
    private int mjsonarraylength = -1;

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

            mlinkquery.countInBackground(new CountCallback() {
                @Override
                public void done(int i, ParseException e) {
                    if (i >= 1 && e == null) {

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

            // set prime membership block
            RelativeLayout mprimeblock = (RelativeLayout)findViewById(R.id.venue_detail_prime_membership_block);
            TextView mprimemembership  = (TextView)findViewById(R.id.venue_detail_prime_membership_note);
            Button mprimebuy           = (Button)findViewById(R.id.venue_detail_prime_membership_buy);
            String mprimeinfo          = mvenue.getPrimeInfo();

            // if business type is venuePrime, then show buy button for prime membership
            if (mvenue.getBusiness().equals(AppConstant.OMETOPLAYVENUEBUSINESSPRIME) && !mprimeinfo.equals(AppConstant.OMEPARSENULLSTRING)) {
                mprimeblock.setVisibility(View.VISIBLE);
                mprimemembership.setVisibility(View.VISIBLE);
                mprimebuy.setVisibility(View.VISIBLE);


                int mlengthest = 0;


                try {
                    // Accordint to json format, Parse prime info json to build prime list
                    JSONObject jsonRootObject = new JSONObject(mprimeinfo);

                    JSONArray jsonArray       = jsonRootObject.getJSONArray(AppConstant.OMETOPLAYVENUEJSONLIST);

                    mjsonarraylength          = jsonArray.length();

                    m3rd      = jsonRootObject.getString(AppConstant.OMETOPLAYVENUEJSON3RD);
                    mname     = jsonRootObject.getString(AppConstant.OMETOPLAYVENUEJSONNAME);
                    mnameid   = jsonRootObject.getString(AppConstant.OMETOPLAYVENUEJSONNAMEID);
                    mcurrency = jsonRootObject.getString(AppConstant.OMETOPLAYVENUEJSONCURRENCY);

                    mprimelist      = new String[jsonArray.length()];
                    mcardnamelist   = new String[jsonArray.length()];
                    mcardidlist     = new String[jsonArray.length()];
                    mcardpricelist  = new String[jsonArray.length()];

                    for (Locale ll: Locale.getAvailableLocales()){
                        try {
                            Currency currency = Currency.getInstance(ll);

                            if (mcurrency.equals(currency.getCurrencyCode()) ) {
                                mcurrencysymbol = currency.getSymbol();
                            }

                        }catch (Exception e){
                            // when the locale is not supported
                        }
                    }

                    // iterate the jsonArray and get lengthest card name
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mcardnamelist[i]          = jsonObject.getString(AppConstant.OMETOPLAYVENUEJSONCARDNAME);
                        if (mlengthest >= mcardnamelist[i].length()) {

                        } else {
                            mlengthest = mcardnamelist[i].length();
                        }
                    }

                    // iterate the jsonArray, align the card name via filled with space, then add to mprimelist
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mcardnamelist[i]          = jsonObject.getString(AppConstant.OMETOPLAYVENUEJSONCARDNAME);
                        mcardidlist[i]            = jsonObject.getString(AppConstant.OMETOPLAYVENUEJSONCARDID);
                        mcardpricelist[i]         = jsonObject.getString(AppConstant.OMETOPLAYVENUEJSONCARDPRICE);

                        for (int j = 0; j < (mlengthest - mcardnamelist[i].length()); j++) {
                            mcardnamelist[i] = mcardnamelist[i] + " ";
                        }
                        mprimelist[i] = mcardnamelist[i]  + "     " +  mcurrencysymbol + " " +jsonObject.getString(AppConstant.OMETOPLAYVENUEJSONCARDPRICE);

                    }

                } catch (JSONException je) {
                    je.printStackTrace();
                }

                final ArrayAdapter<String> mbuylist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, mprimelist); // select_dialog_singlechoice

                mprimebuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailInfoActivity.this);
                        // Set the dialog title
                        builder.setTitle(R.string.OMEPARSEVENUEPRICKPRIMEMEMBERSHIP)
                                // Specify the list array, the items to be selected by default (null for none),
                                // and the listener through which to receive callbacks when items are selected
                                .setSingleChoiceItems(mbuylist, -1, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mcardname  = mcardnamelist[which];
                                                mcardid    = mcardidlist[which] ;
                                                mcardprice = mcardpricelist[which];
                                                mchoice    = which;
                                            }
                                        })
                                        // Set the action buttons
                                .setPositiveButton(R.string.OMEPARSEOK, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {

                                        // check user choose option item
                                        if (mchoice >= 0 && mjsonarraylength > mchoice ) {

                                            // User clicked OK, so save the mSelectedItems results somewhere
                                            // or return them to the component that opened the dialog

                                            Intent invokeBuyPrimeMembershipActivityIntent = new Intent(DetailInfoActivity.this, BuyPrimeMembershipActivity.class);

                                            if (m3rd != null) {
                                                invokeBuyPrimeMembershipActivityIntent.putExtra(Application.INTENT_EXTRA_VENUEJSON3RD, m3rd);
                                            }

                                            if (mname != null) {
                                                invokeBuyPrimeMembershipActivityIntent.putExtra(Application.INTENT_EXTRA_VENUEJSONNAME, mname);
                                            }

                                            if (mnameid != null) {
                                                invokeBuyPrimeMembershipActivityIntent.putExtra(Application.INTENT_EXTRA_VENUEJSONNAMEID, mnameid);
                                            }

                                            if (mcurrency != null) {
                                                invokeBuyPrimeMembershipActivityIntent.putExtra(Application.INTENT_EXTRA_VENUEJSONCURRENCY, mcurrency);
                                            }

                                            if (mcardname != null) {
                                                invokeBuyPrimeMembershipActivityIntent.putExtra(Application.INTENT_EXTRA_VENUEJSONCARDNAME, mcardname);
                                            }

                                            if (mcardid != null) {
                                                invokeBuyPrimeMembershipActivityIntent.putExtra(Application.INTENT_EXTRA_VENUEJSONCARDID, mcardid);
                                            }

                                            if (mcardprice != null) {
                                                invokeBuyPrimeMembershipActivityIntent.putExtra(Application.INTENT_EXTRA_VENUEJSONCARDPRICE, mcardprice);
                                            }

                                            startActivity(invokeBuyPrimeMembershipActivityIntent);
                                        } else {

                                            android.widget.Toast.makeText(DetailInfoActivity.this, getResources().getString(R.string.OMEPARSEPAYBUYPRIMEMEMBERSHIPNOCHOOSE),
                                                    android.widget.Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setNegativeButton(R.string.OMEPARSENO, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });

                        builder.show();

                    }


                });



            } else {
                mprimeblock.setVisibility(View.GONE);
                mprimemembership.setVisibility(View.GONE);
                mprimebuy.setVisibility(View.GONE);
            }


        }



    }





}
