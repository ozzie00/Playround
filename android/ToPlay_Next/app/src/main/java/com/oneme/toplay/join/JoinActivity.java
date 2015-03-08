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


import com.oneme.toplay.Application;
import com.oneme.toplay.database.Sport;

import com.oneme.toplay.LoginActivity;

import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.database.Message;


import com.oneme.toplay.addfriend.AddFriendActivity;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ParseObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.TextView;



import com.google.android.gms.location.LocationClient;
import com.squareup.picasso.Picasso;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;



import android.os.Parcel;
import android.os.Parcelable;

public class JoinActivity extends ActionBarActivity {

    private static final String TAG = "JoinActivity";

    private EditText edittext;
    private EditText edittext1;

    private final Context context                = JoinActivity.this;
    private final Context msubmitjoinplayRequest = JoinActivity.this;
    private LinearLayout mJoinLinerLayout;
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

    // Stores the current instantiation of the location client in this object
    private LocationClient locationClient;

    // Adapter for the Parse query
    private ParseQueryAdapter<Invite> postsQueryAdapter;

    /*
     * Constants for handling location results
     */
    // Conversion from feet to meters
    private static final float METERS_PER_FEET = 0.3048f;

    // Conversion from kilometers to meters
    private static final int METERS_PER_KILOMETER = 1000;

    // Initial offset for calculating the map bounds
    private static final double OFFSET_CALCULATION_INIT_DIFF = 1.0;

    // Accuracy for calculating the map bounds
    private static final float OFFSET_CALCULATION_ACCURACY = 0.01f;

    // Maximum results returned from a Parse query
    private static final int MAX_POST_SEARCH_RESULTS = 20;

    // Maximum post search radius for map in kilometers
    private static final int MAX_POST_SEARCH_DISTANCE = 100;

    // Fields for the map radius in feet
    private float radius;

    private String selectedInviteObjectId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ome_activity_join);

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

        // add LinearLayout
        mJoinLinerLayout =(LinearLayout) findViewById(R.id.join_linerlayout);

        // add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mJoinLinerLayout.setOrientation(LinearLayout.VERTICAL);

        // add host username text
        addHostUsernameText();

        // add host level text
        addHostLevelText();

        // add host submit time text
        addHostSubmitTimeText();

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

        // add join play button
        addJoinPlayButton();



        AlertDialog.Builder minviteplayAlert = new AlertDialog.Builder(JoinActivity.this);

        // Setting Dialog Message
        minviteplayAlert.setMessage(getResources().getString(R.string.OMEPARSEINVITEREALLYPLAY));

        // Setting Positive "Yes" Btn
        minviteplayAlert.setPositiveButton(getResources().getString(R.string.OMEPARSEYES),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        //Toast.makeText(getApplicationContext(),
                          //      "You clicked on YES", Toast.LENGTH_SHORT)
                         //       .show();
                    }

                });
        // Setting Negative "NO" Btn
        minviteplayAlert.setNegativeButton(getResources().getString(R.string.OMEPARSENO),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                   //     Toast.makeText(getApplicationContext(),
                   //             "You clicked on NO", Toast.LENGTH_SHORT)
                   //             .show();
                        dialog.cancel();
                    }
                });

        // Showing Alert Dialog
        //   inviteAlert.show();

    }


    // define host level text
    private void addHostLevelText() {
        mhostLevelText = (TextView) findViewById(R.id.hostplayerlevel);
        if (mhostLevel != null) {
            mhostLevelText.setText(mhostLevel);
        }
    }

    // define host username text
    private void addHostUsernameText() {
        mhostUsernameText = (TextView) findViewById(R.id.hostplayerusername);
        if (mhostUsername != null) {
            mhostUsernameText.setText(mhostUsername);
        }

        ImageView mavatarImage = (ImageView)findViewById(R.id.join_avatar_view);

        try {
            if (muserAvatarUri != null) {
                mavatarImage.setImageBitmap(getBitmapFromUri(muserAvatarUri));
            }
        } catch (IOException ioe) {
            if (Application.APPDEBUG) {
                ioe.printStackTrace();
            }
        }

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {

        try {
            ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor             = parcelFileDescriptor.getFileDescriptor();
            Bitmap image                              = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (IOException ioe) {


        }
        return null;
    }

    // define host username text
    private void addHostSubmitTimeText() {
        msubmitTimeText = (TextView) findViewById(R.id.hostsubmittime);
        if (msubmittime != null) {
            msubmitTimeText.setText(msubmittime);
        }
    }



    // define sport type text
    private void addSportTypeText() {
        msporttypeText = (TextView) findViewById(R.id.joinsporttype);
        msporttypeText.setText(getResources().getString(R.string.OMEPARSEINVITEREQUIREMENTSPORTTYPE));
        msporttypeText = (TextView) findViewById(R.id.joinsporttypeText);
        if (msporttype != null) {
            msporttypeText.setText(msporttype);
        }

        msporttypeIcon = (ImageView) findViewById(R.id.joinsporticon);
        if (msporttypevalue != null) {
            msporttypeIcon.setImageDrawable(getResources().getDrawable(Sport.msporticonarray[Integer.parseInt(msporttypevalue)]));
        }


  }

    // define player level text
    private void addPlayerLevelText() {

        mplayerleveText = (TextView) findViewById(R.id.joinplayerlevel);
        mplayerleveText.setText(getResources().getString(R.string.OMEPARSEINVITEREQUIREMENTPLAYERLEVEL));
        mplayerleveText = (TextView) findViewById(R.id.joinplayerlevelText);
        if (mplayerlevel != null) {
            mplayerleveText.setText(mplayerlevel);
        }

    }

    // define player number button text
    private void addPlayerNumberText() {

        mplayernumberText = (TextView) findViewById(R.id.joinplayernumber);
        mplayernumberText.setText(getResources().getString(R.string.OMEPARSEINVITEREQUIREMENTPLAYERNUMBER));
        mplayernumberText = (TextView) findViewById(R.id.joinplayernumberText);
        if (mplayernumber != null) {
            mplayernumberText.setText(mplayernumber);
        }

    }

    // define invite play time text
    private void addInviteTimeText() {

        mtimeText = (TextView) findViewById(R.id.joininvitetime);
        mtimeText.setText(getResources().getString(R.string.OMEPARSEINVITEREQUIREMENTTIME));
        mtimeText = (TextView) findViewById(R.id.joininvitetimeText);
        if (minviteplaytime != null) {
            mtimeText.setText(minviteplaytime);
        }


    }

    // define invite court text
    private void addInviteCourtText() {

        mcourtText = (TextView) findViewById(R.id.joininvitecourt);
        mcourtText.setText(getResources().getString(R.string.OMEPARSEINVITEREQUIREMENTCOURT));
        mcourtText = (TextView) findViewById(R.id.joininvitecourtText);
        if (mcourt != null) {
            mcourtText.setText(mcourt);
        }

    }


    // define invite play fee text
    private void addInvitePlayFeeText() {

        minviteplayfeeText = (TextView) findViewById(R.id.joininvitefee);
        minviteplayfeeText.setText(getResources().getString(R.string.OMEPARSEINVITEREQUIREMENTFEE));
        minviteplayfeeText = (TextView) findViewById(R.id.joininvitefeeText);
        if (minviteplayfee != null) {
            minviteplayfeeText.setText(minviteplayfee);
        }
    }


    // define invite court text
    private void addInviteOtherText() {

        motherText = (TextView) findViewById(R.id.joininviteother);
        motherText.setText(getResources().getString(R.string.OMEPARSEINVITEREQUIREMENTOTHER));
        motherText = (TextView) findViewById(R.id.joininviteotherText);
        if (mother != null) {
            motherText.setText(mother);
        }

    }

    //define invite play button
    private void addJoinPlayButton() {

        // set button position and align
        LinearLayout.LayoutParams joinplayButtonParam = new LinearLayout.LayoutParams(
                900,
                120);

        joinplayButtonParam.setMargins(8, 60, 8, 8);
        joinplayButtonParam.gravity = Gravity.CENTER;

        // add "Join Play" Button
        mjoinplayButton = new Button(this);
        mjoinplayButton.setBackgroundColor(AppConstant.OMETOPLAYDEFAULTCOLOR);
        mjoinplayButton.setTextColor(Color.WHITE);
        mjoinplayButton.setLayoutParams(joinplayButtonParam);
        mjoinplayButton.setText(getResources().getString(R.string.OMEPARSEINVITEJOINPLAYBUTTON));
        // minviteplayButton.setId(5);


        // add invite play button listener
        mjoinplayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder mjoinplayAlertDialogBuilder = new AlertDialog.Builder(context);

                mjoinplayButton.setBackgroundColor(Color.WHITE);
                mjoinplayButton.setTextColor(AppConstant.OMETOPLAYDEFAULTCOLOR);

                // set title
                // alertDialogBuilder.setTitle("Your Title");

                // set dialog message
                mjoinplayAlertDialogBuilder
                        .setMessage(getResources().getString(R.string.OMEPARSEINVITEPLAYERALERTINFO))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.OMEPARSEYES), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (ParseUser.getCurrentUser() == null) {

                                    //Toast.makeText(JoinActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT),
                                    //        Toast.LENGTH_SHORT).show();

                                    // jump to login activity
                                    Intent invokeLoginActivityIntent = new Intent(JoinActivity.this, LoginActivity.class);
                                    startActivity(invokeLoginActivityIntent);
                                } else {
                                    // submit join request
                                    joinInvitation();

                                    Toast.makeText(JoinActivity.this, getResources().getString(R.string.OMEPARSEINVITEPLAYERJOINPLAYSUCCESSALERTINFO),
                                            Toast.LENGTH_SHORT).show();

                                    JoinActivity.this.finish();

                                }
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.OMEPARSENO), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                mjoinplayButton.setBackgroundColor(AppConstant.OMETOPLAYDEFAULTCOLOR);
                                mjoinplayButton.setTextColor(Color.WHITE);
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog minviteplayAlertDialog = mjoinplayAlertDialogBuilder.create();

                // show it
                minviteplayAlertDialog.show();
            }
        });

        // add join play button
        mJoinLinerLayout.addView(mjoinplayButton);
    }

    // Ozzie Zhang 10-29-2014 please modify this function
    // define submit join to cloud
    private void joinInvitation () {

        String mrequest = null;

        // Check user, then submit invitation
        if (ParseUser.getCurrentUser() == null) {

            Toast.makeText(this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT),
                    Toast.LENGTH_SHORT).show();

            return;
        } else {

            msubmittime = Time.currentTime();

            // Create a post.
            Message message = new Message();

            // Set the location to the current user's location
            message.setUser(ParseUser.getCurrentUser());
            message.setUsername(ParseUser.getCurrentUser().getUsername());

            //message.setToUser(ParseUser.getCurrentUser());

            message.setToUsername(mhostUsername);
            message.setMessageFromUser(ParseUser.getCurrentUser());
            message.setFromUsername(ParseUser.getCurrentUser().getUsername());

            if (minviteObjectID != null) {
                message.setReplyForObjectID(minviteObjectID);
            }

            if (mOMEID != null) {
                message.setFromOmeID(mOMEID);
            }

            mrequest = getResources().getString(R.string.OMEPARSEMESSAGEJOINPLAYREQUESTFIRSTPART) + " " + mhostUsername
                    + " " + getResources().getString(R.string.OMEPARSEMESSAGEJOINPLAYREQUESTSECONDPART) + " " + ParseUser.getCurrentUser().getUsername()
                    + " " + getResources().getString(R.string.OMEPARSEWANTTOKEY) + " " + getResources().getString(R.string.OMEPARSEMESSAGEJOINPLAYREQUESTTHIRDPART) + " "
                    + getResources().getString(R.string.OMEPARSEINVITETITLEPLAY) + " " + msporttype;

            message.setContent(mrequest);
            message.setSendTime(msubmittime);

            ParseACL acl = new ParseACL();

            // Give public read access
            acl.setPublicReadAccess(false);

            // set readAccess by ObjectId
            if (mhostUser != null) {
                acl.setReadAccess(mhostUser, true);
                message.setACL(acl);

                // Save the join message
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        finish();
                        // submitDialog.dismiss();
                    }
                });
            }

            // send add friend request
            // AddFriendActivity.addFriendUsingID(mOMEID, mhostUsername, mrequest);

            return;
        }
    }

}
