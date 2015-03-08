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


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oneme.toplay.Application;
import com.oneme.toplay.LoginActivity;
import com.oneme.toplay.join.MessageListActivity;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.database.InviteComment;
import com.oneme.toplay.database.Message;
import com.oneme.toplay.database.Sport;
import com.oneme.toplay.database.InviteLike;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.GetCallback;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


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

    private String mcontent          = null;
    private ParseUser muser          = ParseUser.getCurrentUser();
    private String musername         = null;
    private ParseQueryAdapter<InviteComment> commentQueryAdapter;

    private int mcommentnumber = 0;
    private int mlikenumber    = 0;
    private TextView mcomment;
    private TextView mlike;
    private ImageView mlikeimage;
    private boolean mylike  = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_join_next);

       // getActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (muser != null) {
            musername = muser.getUsername();
        }

        // fetch the clicked location
        Intent joinintent = getIntent();
        Location location = joinintent.getParcelableExtra(Application.INTENT_EXTRA_LOCATION);
        geoPoint          = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

        Bundle extras     = getIntent().getExtras();

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

        TextView mworkoutView = (TextView)findViewById(R.id.join_next_info_workout_name);
        mworkoutView.setText(mworkoutname);

        ImageView msporttype = (ImageView)findViewById(R.id.join_next_info_workout_sport);
        if (msporttypevalue != null) {
            msporttype.setImageDrawable(getResources().getDrawable(Sport.msporticonarray[Integer.parseInt(msporttypevalue)]));
        }

        TextView mworkoutlocation = (TextView)findViewById(R.id.join_next_info_workout_location);
        mworkoutlocation.setText(mcourt);

        TextView mworkoutdescription = (TextView)findViewById(R.id.join_next_info_workout_description_content);
        mworkoutdescription.setText(mother);


        LinearLayout mscoreblock = (LinearLayout)findViewById(R.id.join_next_info_workout_scoreboard_block);
        mscoreblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invokeCommentActivityIntent = new Intent(JoinNextActivity.this, CommentActivity.class);
                invokeCommentActivityIntent.putExtra(Application.INTENT_EXTRA_INVITEOBJECTID, minviteObjectID);
                startActivity(invokeCommentActivityIntent);

            }
        });

        // set comment number
        mcomment = (TextView)findViewById(R.id.join_next_info_workout_comment_number);

        ParseQuery<InviteComment> commentquery = InviteComment.getQuery();
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

        // set like number
        mlike = (TextView)findViewById(R.id.join_next_info_workout_like_number);

        // query like this invite
        ParseQuery<InviteLike> likequery = InviteLike.getQuery();
        likequery.whereEqualTo(AppConstant.OMEPARSEINVITELIKEPARENTIDKEY, minviteObjectID);
        likequery.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    mlikenumber = count;
                    mlike.setText(Integer.toString(mlikenumber));
                } else {
                    mlikenumber = 0;
                    mlike.setText(Integer.toString(mlikenumber));
                }
            }
        });

        // query me if like this invite
        if (musername != null) {
            ParseQuery<InviteLike> mylikequery = InviteLike.getQuery();
            mylikequery.include(AppConstant.OMEPARSEUSERKEY);
            mylikequery.whereEqualTo(AppConstant.OMEPARSEINVITELIKEPARENTIDKEY, minviteObjectID);
            mylikequery.findInBackground(new FindCallback<InviteLike>() {
                @Override
                public void done(List<InviteLike> inviteLikes, ParseException e) {
                    for (InviteLike like : inviteLikes) {
                        // This does not require a network access.
                        String username = like.getAuthorUsername();
                        if (musername.equals(username)) {
                            mylike = true;
                            mlikeimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_star_enable));
                            break;
                        }
                    }
                }
            });
        }


        // set comment block
        LinearLayout mcommentblock = (LinearLayout)findViewById(R.id.join_next_info_workout_comment_block);
        mcommentblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invokeCommentActivityIntent = new Intent(JoinNextActivity.this, CommentActivity.class);
                invokeCommentActivityIntent.putExtra(Application.INTENT_EXTRA_INVITEOBJECTID, minviteObjectID);
                startActivity(invokeCommentActivityIntent);

            }
        });

        // set like block
        LinearLayout mlikeblock = (LinearLayout)findViewById(R.id.join_next_info_workout_like_block);
        mlikeblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invokeLikeActivityIntent = new Intent(JoinNextActivity.this, LikeActivity.class);
                    invokeLikeActivityIntent.putExtra(Application.INTENT_EXTRA_INVITEOBJECTID, minviteObjectID);
                    startActivity(invokeLikeActivityIntent);
            }
        });

        // invoke like activity
        mlikeimage  = (ImageView)findViewById(R.id.join_next_info_workout_share_like);
        if (mylike == true) {
            mlikeimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_star_enable));
        }

        mlikeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musername != null) {
                    if (mylike == false) {
                        joinlike();
                        mlike.setText(Integer.toString(mlikenumber + 1));
                        mlikeimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_star_enable));
                    } else {
                        mlike.setText(Integer.toString(mlikenumber - 1));
                        mlikeimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_star));
                        removelike();
                    }
                }
            }
        });

        ImageView msendimage  = (ImageView)findViewById(R.id.join_next_info_workout_share_friend);
        msendimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ImageView mshareimage = (ImageView)findViewById(R.id.join_next_info_workout_share_share);
        mshareimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareInvitation();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.ome_activity_join_info_fragment_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_join_workout:
                submitjoinworkout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void submitjoinworkout() {
        AlertDialog.Builder mjoinplayAlertDialogBuilder = new AlertDialog.Builder(JoinNextActivity.this);

        // set dialog message
        mjoinplayAlertDialogBuilder
                .setMessage(getResources().getString(R.string.OMEPARSEINVITEPLAYERALERTINFO))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.OMEPARSEYES), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (ParseUser.getCurrentUser() == null) {

                            // jump to login activity
                            Intent invokeLoginActivityIntent = new Intent(JoinNextActivity.this, LoginActivity.class);
                            startActivity(invokeLoginActivityIntent);
                        } else {
                            // submit join request
                            joinInvitation();

                            Toast.makeText(JoinNextActivity.this, getResources().getString(R.string.OMEPARSEINVITEPLAYERJOINPLAYSUCCESSALERTINFO),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.OMEPARSENO), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog minviteplayAlertDialog = mjoinplayAlertDialogBuilder.create();

        // show it
        minviteplayAlertDialog.show();

    }

    // define submit join to cloud
    private void joinInvitation () {

        String mrequest = null;

        // Check user, then submit invitation
        if (ParseUser.getCurrentUser() == null) {

            android.widget.Toast.makeText(JoinNextActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT),
                    android.widget.Toast.LENGTH_SHORT).show();

            return;
        } else {

            String msubmittime = Time.currentTime();

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
                        // submitDialog.dismiss();
                    }
                });
            }

            // send add friend request
            // AddFriendActivity.addFriendUsingID(mOMEID, mhostUsername, mrequest);

            return;
        }
    }


    // define submit join like to cloud
    private void joinlike() {
        // Check user, then submit invitation
        if (muser == null) {

            android.widget.Toast.makeText(JoinNextActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT),
                    android.widget.Toast.LENGTH_SHORT).show();

            return;
        } else {
            ParseQuery<InviteLike> mylikequery = InviteLike.getQuery();
            mylikequery.whereEqualTo(AppConstant.OMEPARSEINVITELIKEPARENTIDKEY, minviteObjectID);
            mylikequery.whereEqualTo(AppConstant.OMEPARSEINVITELIKEAUTHORNAMEKEY, musername);
            mylikequery.findInBackground(new FindCallback<InviteLike>() {
                @Override
                public void done(List<InviteLike> inviteLikes, ParseException e) {
                    if (e == null) {
                        // found user has liked this invitation, then do nothing
                        if (inviteLikes == null || inviteLikes.size() == 0) {
                            // Create a like if user has not liked this invitation before
                            InviteLike mfindlike = new InviteLike();

                            mfindlike.setAuthor(muser);
                            mfindlike.setAuthorUsername(musername);
                            mfindlike.setParentObjectId(minviteObjectID);

                            ParseACL acl = new ParseACL();

                            // Give public read access
                            acl.setPublicReadAccess(true);
                            acl.setWriteAccess(muser, true);//acl.setPublicWriteAccess(true);//(muser, true);

                            mfindlike.setACL(acl);

                            // Save the join message
                            mfindlike.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    // submitDialog.dismiss();
                                    android.util.Log.d(" add like username ", musername);
                                }
                            });
                        }

                    } else {


                    }

                }
            });
        }

        return;

    }

    // define submit remove like to cloud
    private void removelike() {
        // Check user, then submit invitation
        if (muser == null) {

            android.widget.Toast.makeText(JoinNextActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT),
                    android.widget.Toast.LENGTH_SHORT).show();

            return;
        } else {

            ParseQuery<InviteLike> removequery = InviteLike.getQuery();
            removequery.whereEqualTo(AppConstant.OMEPARSEINVITELIKEPARENTIDKEY, minviteObjectID);
            removequery.whereEqualTo(AppConstant.OMEPARSEINVITELIKEAUTHORNAMEKEY, musername);
            removequery.findInBackground(new FindCallback<InviteLike>() {
                @Override
                public void done(List<InviteLike> likeList, ParseException e) {
                    if (e == null) {
                    for (InviteLike delete : likeList) {
                            delete.deleteInBackground();
                        }
                    } else {

                    }
                }
            });

        }

        return;

    }


    private void shareInvitation() {

        // create share content
        String sharecontent = null;

        String mtime = minviteplaytime;

        if (mcourt != null) {
            sharecontent = getResources().getString(R.string.OMEPARSEWANTTOKEY)
                    + " " + getResources().getString(R.string.OMETOPLAYATWORD)
                    + " " + mcourt + " " + getResources().getString(R.string.OMEPARSEINVITETITLEPLAY)
                    + " " + msporttype;
        }

        if (mtime != null) {
            sharecontent = getResources().getString(R.string.OMEPARSEWANTTOKEY)
                    + " " + getResources().getString(R.string.OMEPARSEINVITETITLEPLAY)
                    + " " + msporttype + " " + getResources().getString(R.string.OMETOPLAYINWORD)
                    + " " + mtime;
        }

        if (mcourt != null && mtime != null) {
            sharecontent = getResources().getString(R.string.OMEPARSEWANTTOKEY)
                    + " " + getResources().getString(R.string.OMETOPLAYATWORD)
                    + " " + mcourt + " " + getResources().getString(R.string.OMEPARSEINVITETITLEPLAY)
                    + " " + msporttype + " " + getResources().getString(R.string.OMETOPLAYINWORD)
                    + " " + mtime;
        }

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharecontent);
        shareIntent.setType(AppConstant.OMEPARSESHARETEXTFILE);
        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.OMEPARSEADDCOTACTSHAREQRCODEWITH)));

    }



}