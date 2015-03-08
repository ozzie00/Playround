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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oneme.toplay.Application;
import com.oneme.toplay.LoginActivity;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.database.Message;
import com.oneme.toplay.database.Sport;
import com.oneme.toplay.R;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class InfoFragment extends Fragment {

    private String mhostUser       = null;
    private String mhostUsername   = null;
    private String mOMEID          = null;
    private String msporttypevalue = null;
    private String msporttype      = null;
    private String minviteplaytime = null;
    private String mcourt          = null;
    private String mother          = null;
    private String minviteObjectID = null;
    private String mworkout        = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle   = this.getArguments();
        mhostUser       = bundle.getString(Application.INTENT_EXTRA_USEROBJECTID);
        mhostUsername   = bundle.getString(Application.INTENT_EXTRA_USERNAME);
        mOMEID          = bundle.getString(Application.INTENT_EXTRA_USEROMEID);
        mworkout        = bundle.getString(Application.INTENT_EXTRA_WORKOUTNAME);
        msporttypevalue = bundle.getString(Application.INTENT_EXTRA_SPORTTYPEVALUE);
        msporttype      = bundle.getString(Application.INTENT_EXTRA_SPORTTYPE);
        mcourt          = bundle.getString(Application.INTENT_EXTRA_COURT);
        minviteplaytime = bundle.getString(Application.INTENT_EXTRA_TIME);
        mother          = bundle.getString(Application.INTENT_EXTRA_OTHER);
        minviteObjectID = bundle.getString(Application.INTENT_EXTRA_INVITEOBJECTID);

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.ome_activity_join_info_fragment, container, false);

        TextView mworkoutname = (TextView)rootView.findViewById(R.id.join_info_workout_name);
        mworkoutname.setText(mworkout);

        ImageView msporttype = (ImageView)rootView.findViewById(R.id.join_info_workout_sport);
        if (msporttypevalue != null) {
            msporttype.setImageDrawable(getResources().getDrawable(Sport.msporticonarray[Integer.parseInt(msporttypevalue)]));
        }


        TextView mworkoutlocation = (TextView)rootView.findViewById(R.id.join_info_workout_location);
        mworkoutlocation.setText(mcourt);

        TextView mworkoutdescription = (TextView)rootView.findViewById(R.id.join_info_workout_description_content);
        mworkoutdescription.setText(mother);




        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(
        Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ome_activity_join_info_fragment_menu, menu);
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
        AlertDialog.Builder mjoinplayAlertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set title
        // alertDialogBuilder.setTitle("Your Title");

        // set dialog message
        mjoinplayAlertDialogBuilder
                .setMessage(getResources().getString(R.string.OMEPARSEINVITEPLAYERALERTINFO))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.OMEPARSEYES), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (ParseUser.getCurrentUser() == null) {

                            // jump to login activity
                            Intent invokeLoginActivityIntent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(invokeLoginActivityIntent);
                        } else {
                            // submit join request
                            joinInvitation();

                            Toast.makeText(getActivity(), getResources().getString(R.string.OMEPARSEINVITEPLAYERJOINPLAYSUCCESSALERTINFO),
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

            android.widget.Toast.makeText(getActivity(), getResources().getString(R.string.OMEPARSEINVITELOGINALERT),
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
}