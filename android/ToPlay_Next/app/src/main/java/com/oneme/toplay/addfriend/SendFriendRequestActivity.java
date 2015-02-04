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

package com.oneme.toplay.addfriend;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;

public class SendFriendRequestActivity { //extends ActionBarActivity {

    /*
    private static final String TAG = "SendFriendRequestActivity";

    private static String mUsername = null;

    private static String mOMEID = null;

    private EditText requestText;
    private String requestmessage;

    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_send_friend_request);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(getResources().getString(R.string.OMEPARSEADDCONTACTSENDREQUESTTITLE));

        context = getApplicationContext();

        // fetch username
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mUsername = extras.getString(Application.INTENT_EXTRA_USERNAME);
            mOMEID    = extras.getString(Application.INTENT_EXTRA_USEROMEID);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.OMEPARSEADDCONTACTSEARCHUSERNAMEERROR), Toast.LENGTH_LONG).show();
            return;
        }

        requestText    = (EditText) findViewById(R.id.send_request_message);
        requestmessage = requestText.getText().toString();

        if (requestmessage == null) {
            requestmessage = getResources().getString(R.string.OMEPARSEADDCONTACTDEFAULTREQUESTMESSAGE);
        }

        RelativeLayout sendrequest = (RelativeLayout) findViewById(R.id.send_friend_request_block);
        sendrequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int result = AddFriendActivity.addFriendUsingID(getApplicationContext(), mOMEID, mUsername, requestmessage);

                if (result == 0) {
                    Toast.makeText(SendFriendRequestActivity.this, getResources().getString(R.string.OMEPARSEADDCONTACTSENDREQUESTSUCCESS),
                            Toast.LENGTH_SHORT).show();
                } else if(result == AppConstant.OMETOPLAYNOVALIDPUBLICKEY) {
                    Toast.makeText(SendFriendRequestActivity.this, getResources().getString(R.string.OMEPARSEADDCONTACTSENDREQUESTINVALIDFRIENDID),
                            Toast.LENGTH_SHORT).show();
                } else if(result == AppConstant.OMETOPLAYFRIENDEXIST) {
                    Toast.makeText(SendFriendRequestActivity.this, getResources().getString(R.string.OMEPARSEADDCONTACTSENDREQUESTFRIENDEXIST),
                            Toast.LENGTH_SHORT).show();
                } else if(result == AppConstant.OMETOPLAYNOKEYOWN) {
                    Toast.makeText(SendFriendRequestActivity.this, getResources().getString(R.string.OMEPARSEADDCONTACTSENDREQUESTADDYOURSELF),
                            Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });

    }
    */

/*
    public int addFriendUsingID(String FriendPublicKey, String RequestMessage) {
        if(!isKeyOwn(FriendPublicKey)) {
            if (validateFriendKey(FriendPublicKey)) {
                String ID      = FriendPublicKey;
                String message = RequestMessage;//friendMessage.getText().toString();
                String alias   = "";//friendAlias.getText().toString();

                String[] friendData = {ID, message, alias};

                Database mDatabase = new Database(getApplicationContext());
                if (!mDatabase.doesFriendExist(ID)) {
                    try {
                        Singleton mSingleton = Singleton.getInstance();
                        mSingleton.jTox.addFriend(friendData[0], friendData[1]);
                    } catch (ToxException e) {
                        e.printStackTrace();
                    } catch (FriendExistsException e) {
                        e.printStackTrace();
                    }

                    Log.d("AddFriendActivity", "Adding friend to database");
                    mDatabase.addFriend(ID, "Friend Request Sent",alias, originalUsername);
                } else {
                    return AppConstant.OMETOPLAYFRIENDEXIST;
                }
                mDatabase.close();
                return 0;
            } else {
                return  AppConstant.OMETOPLAYNOVALIDPUBLICKEY;
            }
        } else {
            return  AppConstant.OMETOPLAYNOKEYOWN;
        }
    }
*/

}
