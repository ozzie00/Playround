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

package com.oneme.toplay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.database.Message;
import com.oneme.toplay.ui.BaseActivity;
import com.oneme.toplay.ui.MessageListActivity;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Activity which displays a login screen to the user, offering registration as well.
 */
public class MessageReplyActivity extends BaseActivity {
    // UI references.
    private EditText postEditText;
    private TextView characterCountTextView;
    private Button postButton;

    private String replyToUsername = null;
    private String replyToUser     = null;

    private int maxCharacterCount = Application.getConfigHelper().getPostMaxCharacterCount();
    private ParseGeoPoint geoPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ome_activity_message_reply);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 finish();
                //navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(MessageReplyActivity.this,
                //        MessageListActivity.class)));
            }
        });


        //Intent intent = getIntent();
        Bundle extras   = getIntent().getExtras();
        replyToUsername = extras.getString(AppConstant.OMEPARSEMESSAGETOUSERNAMEKEY);
        replyToUser     = extras.getString(Application.INTENT_EXTRA_USEROBJECTID);

        postEditText = (EditText) findViewById(R.id.reply_edittext);

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
                updateCharacterCountTextViewText();
            }
        });

        characterCountTextView = (TextView) findViewById(R.id.character_count_textview);

        postButton = (Button) findViewById(R.id.reply_button);
        postButton.setText(getResources().getString(R.string.OMEPARSEMESSAGEREPLYBUTTON));

        postButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                reply();
            }
        });

        updatePostButtonState();
        updateCharacterCountTextViewText();
    }

    private void reply () {
        String text = postEditText.getText().toString().trim();

        ParseUser muser  = ParseUser.getCurrentUser();
        String musername =  muser.getUsername();

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(MessageReplyActivity.this);
        dialog.setMessage(getString(R.string.progress_send));
        dialog.show();

        // Create a post.
        Message reply = new Message();

        // Set the location to the current user's location
        reply.setUser(muser);
        reply.setUsername(musername);
        //   join.setLocation(geoPoint);
        //Ozzie Zhang 2014-11-12 this setToUser should be host User, technology issue
        reply.setToUser(ParseUser.getCurrentUser());
        //  join.setMessageFromUser();

        reply.setToUsername(replyToUsername);
        reply.setMessageFromUser(muser);
        reply.setFromUsername(musername);
        reply.setContent(text);
        reply.setSendTime(Time.currentTime());

        // Set the location to the current user's location
       // post.setLocation(geoPoint);
        reply.setUser(muser);
        ParseACL acl = new ParseACL();

        // Do not give public read access
        acl.setPublicReadAccess(false);

        // set readAccess by ObjectId
        acl.setReadAccess(replyToUser, true);
        reply.setACL(acl);

        // Save the post
        reply.saveInBackground(new SaveCallback() {
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

    private void updateCharacterCountTextViewText () {
        String characterCountString = String.format("%d/%d", postEditText.length(), maxCharacterCount);
        characterCountTextView.setText(characterCountString);
    }
}
