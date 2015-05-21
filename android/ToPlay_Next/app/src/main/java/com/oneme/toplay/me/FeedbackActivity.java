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

package com.oneme.toplay.me;

import android.content.ComponentName;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.database.Message;
import com.oneme.toplay.ui.BaseActivity;
import com.oneme.toplay.ui.SettingNextActivity;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;



/**
 * Activity which displays a login screen to the user, offering registration as well.
 */
public class FeedbackActivity extends BaseActivity {
    // UI references.
    private EditText postEditText;
    private TextView characterCountTextView;
    private Button postButton;

    private int maxCharacterCount = Application.getConfigHelper().getPostMaxCharacterCount();
    private ParseGeoPoint geoPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ome_activity_post);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(FeedbackActivity.this,
                //         SettingNextActivity.class)));
            }
        });

        //Intent intent = getIntent();
        //Location location = intent.getParcelableExtra(Application.INTENT_EXTRA_LOCATION);
        //geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

        postEditText = (EditText) findViewById(R.id.post_edittext);

        postEditText.setHint(getResources().getString(R.string.OMEPARSEFEEDBACKHINT));

        // no text entered. Center the hint text.

        postEditText.setGravity(Gravity.CENTER);

        postEditText.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                if (postEditText.length() > 0){
                    // position the text type in the left top corner
                    postEditText.setGravity(Gravity.LEFT | Gravity.TOP);
                }else{
                    // no text entered. Center the hint text.
                    postEditText.setGravity(Gravity.RIGHT);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                updatePostButtonState();

            }
        });


/*
      // set button position and align
      LinearLayout.LayoutParams inviteplayButtonParam = new LinearLayout.LayoutParams(
              900,
              120);

      inviteplayButtonParam.setMargins(8, 60, 8, 8);
      inviteplayButtonParam.gravity = Gravity.CENTER;

      // add "Join Play" Button
      postButton = new Button(this);
      postButton.setBackgroundColor(AppConstant.OMETOPLAYDEFAULTCOLOR);
      postButton.setTextColor(Color.WHITE);
      postButton.setLayoutParams(inviteplayButtonParam);
  */

        postButton = (Button) findViewById(R.id.post_button);
        postButton.setText(getResources().getString(R.string.OMEPARSEFEEDBACKSENDBUTTON));
        postButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                post();
            }
        });

        updatePostButtonState();

    }

    private void post () {
        String text = postEditText.getText().toString().trim();

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(FeedbackActivity.this);
        dialog.setMessage(getResources().getString(R.string.OMEPARSEFEEDBACKSENDING));
        dialog.show();

        // Create a post.
        Message post = new Message();

        // Set the location to the current user's location
        // post.setLocation(geoPoint);

        post.setToUsername("ToPlayFeedback");
        //post.setFromUsername(ParseUser.getCurrentUser().getUsername());
        post.setContent(text);

        post.setSendTime(Time.currentTime());


        ParseACL acl = new ParseACL();

        // Give public read access

        acl.setPublicReadAccess(false);
        acl.setReadAccess("ToPlayFeedback", true);

        post.setACL(acl);

        // Save the post
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
                finish();
            }
        });
    }

    private String getPostEditTextText () {
        return postEditText.getText().toString().trim();
    }

    private void updatePostButtonState () {
        int length = getPostEditTextText().length();
        boolean enabled = length > 0 && length < maxCharacterCount;
        postButton.setEnabled(enabled);
    }

}
