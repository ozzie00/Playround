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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;

import com.google.android.gms.location.LocationClient;

import java.io.FileDescriptor;
import java.io.IOException;

import com.oneme.toplay.Application;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.database.Message;
import com.oneme.toplay.database.Sport;
import com.oneme.toplay.LoginActivity;
import com.oneme.toplay.R;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class JoinActivity extends ActionBarActivity {

    private static final String TAG = "JoinActivity";

    private final Context context   = JoinActivity.this;
    private LinearLayout mJoinLinerLayout;
    private Button mjoinplayButton;

    private TextView mhostUsernameText;
    private TextView mhostLevelText;
    private ImageView msporttypeIcon;
    private TextView mtimeText;
    private TextView mcourtText;
    private TextView msubmitTimeText;


    private Uri muserAvatarUri       = null;
    private String minviteObjectID   = null;
    private String mhostUsername     = null;
    private String mhostLevel        = null;
    private String msporttype        = null;
    private String msporttypevalue   = null;
    private String minviteplaytime   = null;
    private String mcourt            = null;
    private String msubmittime       = null;
    private String mhostUser         = null;
    private String mOMEID            = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ome_activity_join);

        // fetch the clicked location
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            muserAvatarUri    = Uri.parse(extras.getString(Application.INTENT_EXTRA_USERICONPATH));
            mhostUser         = extras.getString(Application.INTENT_EXTRA_USEROBJECTID);
            mhostUsername     = extras.getString(Application.INTENT_EXTRA_USERNAME);
            mOMEID            = extras.getString(Application.INTENT_EXTRA_USEROMEID);
            msporttypevalue   = extras.getString(Application.INTENT_EXTRA_SPORTTYPEVALUE);
            msporttype        = extras.getString(Application.INTENT_EXTRA_SPORTTYPE);
            minviteplaytime   = extras.getString(Application.INTENT_EXTRA_TIME);
            mcourt            = extras.getString(Application.INTENT_EXTRA_COURT);
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

        // add join play time text
        addInviteTimeText();

        // add join court text
        addInviteCourtText();

        // add join play button
        addJoinPlayButton();
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
        msporttypeIcon = (ImageView) findViewById(R.id.joinsporticon);
        if (msporttypevalue != null) {
            msporttypeIcon.setImageDrawable(getResources().getDrawable(Sport.msporticonarray[Integer.parseInt(msporttypevalue)]));
        }

    }

    // define invite play time text
    private void addInviteTimeText() {
        mtimeText = (TextView) findViewById(R.id.joininvitetimeText);
        if (minviteplaytime != null) {
            mtimeText.setText(minviteplaytime);
        }

    }

    // define invite court text
    private void addInviteCourtText() {
        mcourtText = (TextView) findViewById(R.id.joininvitecourtText);
        if (mcourt != null) {
            mcourtText.setText(mcourt);
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

                // set dialog message
                mjoinplayAlertDialogBuilder
                        .setMessage(getResources().getString(R.string.OMEPARSEINVITEPLAYERALERTINFO))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.OMEPARSEYES), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (ParseUser.getCurrentUser() == null) {

                                    // jump to login activity
                                    Intent invokeLoginActivityIntent = new Intent(JoinActivity.this, LoginActivity.class);
                                    startActivity(invokeLoginActivityIntent);
                                } else if (mhostUsername.equalsIgnoreCase(ParseUser.getCurrentUser().getUsername())) {
                                    Toast.makeText(JoinActivity.this, getResources().getString(R.string.OMEPARSECANNOTJOINYOURSELFALERT),
                                            Toast.LENGTH_LONG).show();
                                    JoinActivity.this.finish();
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

            Toast.makeText(JoinActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT),
                    Toast.LENGTH_SHORT).show();

            return;
        } else {

            msubmittime = Time.currentTime();

            // Create a post.
            Message message = new Message();

            // Set the location to the current user's location
            message.setUser(ParseUser.getCurrentUser());
            message.setUsername(ParseUser.getCurrentUser().getUsername());

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
                    }
                });
            }

            return;
        }
    }

}
