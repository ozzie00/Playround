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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.database.IdentityDatabase;
import com.oneme.toplay.database.Venue;
import com.oneme.toplay.database.VenueOwner;
import com.oneme.toplay.local.CnLocalWithoutMapActivity;
import com.oneme.toplay.local.LocalWithoutMapActivity;
import com.oneme.toplay.base.ClientFriendList;
import com.oneme.toplay.base.Options;
import com.oneme.toplay.service.CoreService;
import com.oneme.toplay.service.DataFile;
import com.oneme.toplay.venue.OwnerInfoUploadActivity;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import im.tox.jtoxcore.JTox;
//import im.tox.jtoxcore.ToxException;
//import im.tox.jtoxcore.ToxOptions;
//import im.tox.jtoxcore.callbacks.CallbackHandler;


public class SignUpActivity extends ActionBarActivity {

    private static final String TAG = "SignUpActivity";
    private ParseGeoPoint userLastLocation;
    private ParseFile mavatar;

    // UI references.
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordAgainEditText;
    private Boolean isVenueOwner;

    //private VenueOwner venueowner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.ome_activity_signup);

        // Fix for an android 4.1.x bug
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            );
        }

        // Set up the signup form.
        usernameEditText      = (EditText) findViewById(R.id.username_edit_text);
        passwordEditText      = (EditText) findViewById(R.id.password_edit_text);
        passwordAgainEditText = (EditText) findViewById(R.id.password_again_edit_text);
        passwordAgainEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.edittext_action_signup ||
                        actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    signup();
                    return true;
                }
                return false;
            }
        });



        // Set up the submit button click handler
        Button mActionButton = (Button) findViewById(R.id.action_registerbutton);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                signup();
            }
        });

    }


    private void signup() {
        final String username= usernameEditText.getText().toString().trim();
        String password      = passwordEditText.getText().toString().trim();
        String passwordAgain = passwordAgainEditText.getText().toString().trim();

        final CheckBox checkvenueowner = (CheckBox) findViewById(R.id.signup_checkbox_venueowner);
        if (checkvenueowner.isChecked()) {
            checkvenueowner.setChecked(false);
            isVenueOwner = true;
        } else {
            isVenueOwner = false;
        }

        // Validate the sign up data
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));
        if (username.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_username));
        }
        if (password.length() == 0) {
            if (validationError) {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_password));
        }
        if (!password.equals(passwordAgain)) {
            if (validationError) {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_mismatched_passwords));
        }
        validationErrorMessage.append(getString(R.string.error_end));



        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(SignUpActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        } else {
            if (!isVenueOwner) {
                // Set up a progress dialog
                final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
                dialog.setMessage(getString(R.string.progress_signup));
                dialog.show();

                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.put(AppConstant.OMEPARSEUSERTAGKEY, AppConstant.OMEPARSEUSERTAGPLAYER);
                //user.put(AppConstant.OMEPARSEUSERICONKEY, );

                // Set up a new Parse user in parse cloud
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        dialog.dismiss();
                        if (e != null) {
                            // Show the error message
                            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.OMEPARSESIGNUPERRORNOTE), Toast.LENGTH_LONG).show();
                        } else {
                            // login app
                            if (DispatchActivity.getGooglePlayServicesState()) {
                                Intent invokeLocalActivityIntent = new Intent(SignUpActivity.this, LocalWithoutMapActivity.class);
                                startActivity(invokeLocalActivityIntent);
                            } else {
                                Intent invokeCnLocalActivityIntent = new Intent(SignUpActivity.this, CnLocalWithoutMapActivity.class);
                                startActivity(invokeCnLocalActivityIntent);
                            }
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });
            } else {
                // register as venue owner
                // Set up a progress dialog
                final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
                dialog.setMessage(getString(R.string.progress_signup));
                dialog.show();

                final ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.put(AppConstant.OMEPARSEUSERTAGKEY, AppConstant.OMEPARSEUSERTAGVENUE);

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        dialog.dismiss();
                        if (e != null) {
                            // Show the error message
                            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.OMEPARSESIGNUPERRORNOTE), Toast.LENGTH_LONG).show();
                        } else {
                            VenueOwner venueowner = new VenueOwner();
                            venueowner.setLighted("1");
                            venueowner.setIndoor("1");
                            venueowner.setCourtNumber("1");
                            venueowner.setVerify(false);
                            venueowner.setUser(user);
                            venueowner.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        // Show the error message
                                        Toast.makeText(SignUpActivity.this, getResources().getString(R.string.OMEPARSESIGNUPERRORNOTE), Toast.LENGTH_LONG).show();
                                    } else {
                                        Intent invokeVenueOwnerActivityIntent = new Intent(SignUpActivity.this, OwnerInfoUploadActivity.class);
                                        startActivity(invokeVenueOwnerActivityIntent);

                                        setResult(RESULT_OK);
                                        finish();

                                    }
                                }
                            });

                           // Intent invokeVenueOwnerActivityIntent = new Intent(SignUpActivity.this, OwnerInfoUploadActivity.class);
                           // startActivity(invokeVenueOwnerActivityIntent);

                           // setResult(RESULT_OK);
                           // finish();
                        }

                    }
                });

            }
        }



    }


    private void SignUpMessenger(String username) {
      //  EditText accountField = (EditText) findViewById(R.id.create_account_name);

        /*
        String account        = username;//accountField.getText().toString();

        Pattern pattern               = Pattern.compile("\\s");
        Pattern pattern2              = Pattern.compile(File.separator);
        Matcher matcher               = pattern.matcher(account);
        boolean containsSpaces        = matcher.find();
        matcher                       = pattern2.matcher(account);
        boolean containsFileSeperator = matcher.find();

        if (account.equals("")) {
            Context context   = getApplicationContext();
            CharSequence text = getString(R.string.create_must_fill_in);
            int duration      = Toast.LENGTH_SHORT;
            Toast toast       = Toast.makeText(context, text, duration);
            toast.show();
        } else if (containsSpaces || containsFileSeperator) {
            Context context   = getApplicationContext();
            CharSequence text = getString(R.string.create_bad_profile_name);
            int duration      = Toast.LENGTH_SHORT;
            Toast toast       = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            // Add user to DB
            IdentityDatabase mIdentityDatabase = new IdentityDatabase(getApplicationContext());
            mIdentityDatabase.addUser(account, "");
            mIdentityDatabase.close();

            // Create a data file
            String ID = "";
            byte[] fileBytes = null;
            try {
                ClientFriendList mFriendList     = new ClientFriendList();
                CallbackHandler callbackHandler  = new CallbackHandler(mFriendList);
                SharedPreferences preferences    = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean udpEnabled               = preferences.getBoolean("enable_udp", false);
                ToxOptions toxOptions            = new ToxOptions(Options.ipv6Enabled, udpEnabled, Options.proxyEnabled);
                JTox jTox                        = new JTox(mFriendList, callbackHandler, toxOptions);
                DataFile mDataFile               = new DataFile(getApplicationContext(), account);
                mDataFile.saveFile(jTox.save());
                ID                               = jTox.getAddress();
                fileBytes                        = mDataFile.loadFile();
            } catch (ToxException e) {
                if (Application.APPDEBUG) {
                    Log.d("CreateAccount", e.getMessage());
                }
            }

            SharedPreferences preferences   = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(AppConstant.OMEPARSEUSERACTIVEACCOUNTSKEY, account);
            editor.putString(AppConstant.OMEPARSEUSERNICKNAMEKEY, account);
            editor.putString(AppConstant.OMEPARSEUSERSTATUSKEY, "online");
            editor.putString(AppConstant.OMEPARSEUSERSTATUALERTSKEY, getResources().getString(R.string.pref_default_status_message));
            editor.putString(AppConstant.OMEPARSEUSERIDKEY, ID);
            editor.putBoolean(AppConstant.OMEPARSEUSERLOGGEDINSKEY, true);
            editor.apply();

            // Start core service
            Intent startCoreService = new Intent(getApplicationContext(), CoreService.class);
            getApplicationContext().startService(startCoreService);


            if (DispatchActivity.getGooglePlayServicesState()) {
                Intent invokeLocalActivityIntent = new Intent(SignUpActivity.this, LocalWithoutMapActivity.class);
                startActivity(invokeLocalActivityIntent);
            } else {
                Intent invokeCnLocalActivityIntent = new Intent(SignUpActivity.this, CnLocalWithoutMapActivity.class);
                startActivity(invokeCnLocalActivityIntent);
            }

            setResult(RESULT_OK);
            finish();
        }

        */
    }




}

