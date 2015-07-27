package com.oneme.toplay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.database.VenueOwner;
import com.oneme.toplay.ui.LocalNextActivity;
import com.oneme.toplay.venue.OwnerInfoUploadActivity;
import com.oneme.toplay.venue.OwnerMainActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.ParseQuery;
import com.parse.GetCallback;

//import im.tox.jtoxcore.JTox;
//import im.tox.jtoxcore.ToxException;
//import im.tox.jtoxcore.ToxOptions;
//import im.tox.jtoxcore.callbacks.CallbackHandler;

//import com.oneme.toplay.message.ui.SignUpActivity;

public class LoginActivity extends ActionBarActivity {


    private static final String TAG = "LoginActivity";

    // UI references.
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_login);

        //getSupportActionBar().hide();

        // Set up the login form.
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        String account = usernameEditText.getText().toString().trim();

        //String musername = usernameEditText.getText().toString().trim();
        //final String mpassword = passwordEditText.getText().toString().trim();

        // Set up a progress dialog
       // final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
       // dialog.setMessage(getString(R.string.progress_login));


        // click login button
        Button actionButton = (Button) findViewById(R.id.action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                final String musername = usernameEditText.getText().toString().trim();
                final String mpassword = passwordEditText.getText().toString().trim();

                // Set up a progress dialog
                final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage(getString(R.string.progress_login));

                // Validate the log in data
                boolean validationError = false;
                StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));
                if (musername.length() == 0) {
                    validationError = true;
                    validationErrorMessage.append(getString(R.string.error_blank_username));
                }
                if (mpassword.length() == 0) {
                    if (validationError) {
                        validationErrorMessage.append(getString(R.string.error_join));
                    }
                    validationError = true;
                    validationErrorMessage.append(getString(R.string.error_blank_password));
                }
                validationErrorMessage.append(getString(R.string.error_end));

                // If there is a validation error, display the error
                if (validationError) {
                    Toast.makeText(LoginActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                            .show();
                    return;
                }


                dialog.show();

                ParseUser.logInInBackground(musername,mpassword, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException pe) {
                        if (pe == null) {
                            //createMessengerAccount(musername);
                            //loginLocalMessenger(musername);

                            //get point according to  current latitude and longitude
                            ParseGeoPoint userLastLocation = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

                            ParseUser user = ParseUser.getCurrentUser();
                            if (user != null) {
                                user.put(AppConstant.OMEPARSEUSERLASTTIMEKEY, Time.currentTime());
                                user.put(AppConstant.OMEPARSEUSERLASTLOCATIONKEY, userLastLocation);
                                user.saveInBackground();
                            }

                            // check user tag, according to user tag login respectively main activity
                            if (user.getString(AppConstant.OMEPARSEUSERTAGKEY).equalsIgnoreCase(AppConstant.OMEPARSEUSERTAGPLAYER)) {

                                if (DispatchActivity.getGooglePlayServicesState()) {
                                    dialog.dismiss();
                                    Intent invokeLocalActivityIntent = new Intent(LoginActivity.this, LocalNextActivity.class);// LocalWithoutMapActivity.class);
                                    startActivity(invokeLocalActivityIntent);
                                    finish();
                                } else {
                                    dialog.dismiss();
                                    Intent invokeCnLocalActivityIntent = new Intent(LoginActivity.this, LocalNextActivity.class);//CnLocalWithoutMapActivity.class);
                                    startActivity(invokeCnLocalActivityIntent);
                                    finish();
                                }
                            } else if (user.getString(AppConstant.OMEPARSEUSERTAGKEY).equalsIgnoreCase(AppConstant.OMEPARSEUSERTAGVENUE)) {
                                dialog.dismiss();

                                ParseQuery<VenueOwner> query = VenueOwner.getQuery();
                                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                                query.include(AppConstant.OMETOPLAYVENUEOWNERCLASSKEY);
                                query.whereEqualTo(AppConstant.OMEPARSEUSERKEY, user);
                                query.setLimit(1);

                                query.getFirstInBackground(new GetCallback<VenueOwner>() {
                                    public void done(VenueOwner venueowner, ParseException pe) {
                                        if (pe == null) {

                                            if (venueowner.getVerify() != true) {
                                                if (venueowner.getIdCopyImage() == null) {
                                                    Intent invokeVenueOwnerActivityIntent = new Intent(LoginActivity.this, OwnerInfoUploadActivity.class);
                                                    startActivity(invokeVenueOwnerActivityIntent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.OMEPARSEVENUEOWNERWAITINGFORVERIFY), Toast.LENGTH_LONG)
                                                            .show();
                                                }
                                            } else if (venueowner.getVerify() == true) {
                                                Intent invokeOwnerMainActivityIntent = new Intent(LoginActivity.this, OwnerMainActivity.class);
                                                startActivity(invokeOwnerMainActivityIntent);
                                                finish();
                                            }

                                        } else {
                                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.OMEPARSECANNOTGETVENUEINFO), Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                });

                            }
                        } else {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.OMEPARSELOGINUSERNAMEPASSWORDNOTMATCH), Toast.LENGTH_LONG)
                                    .show();
                            finish();

                        }

                    }
                });

            }
        });

      //  createMessengerAccount(account);
      //  loginLocalMessenger(account);

        // click register button
        Button actionregisterbutton = (Button) findViewById(R.id.action_registerbutton);
        actionregisterbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent invokeSignUpActivityIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(invokeSignUpActivityIntent);
                finish();
            }
        });

            // Fix for an android 4.1.x bug
            if (Build.VERSION.SDK_INT != Build.VERSION_CODES.JELLY_BEAN
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getWindow().setFlags(
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                );
            }
    }



    // Check to see if any users exist. If not, create account for local messenger
    private void createMessengerAccount(String username) {

        /*
        // login messenger
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        IdentityDatabase db           = new IdentityDatabase(getApplicationContext());

        if (Application.APPDEBUG) {
            for (String databaseName : getApplicationContext().databaseList()) {
               // getApplicationContext().deleteDatabase(databaseName);
                File databasesPath = getApplicationContext().getDatabasePath("ignored").getParentFile();
                Log.d(TAG, " databasesPath " + databasesPath.toString() +  " dabasename " + databaseName);
            }
        }


        if (Application.APPDEBUG) {
            Log.d(TAG, "@1@");
        }
        // Check to see if any users exist. If not, start the create account activity instead
        if (!db.doUsersExist()) {
            if (Application.APPDEBUG) {
                Log.d(TAG, " no user exist, and create");

            }
            IdentityDatabase mIdentityDatabase = new IdentityDatabase(getApplicationContext());
            mIdentityDatabase.addUser(username, "");
            mIdentityDatabase.close();

            // Create a data file
            String ID        = "";
            byte[] fileBytes = null;
            try {
                if (Application.APPDEBUG) {
                    Log.d(TAG, "@2@");

                }
                ClientFriendList mFriendList    = new ClientFriendList();
                CallbackHandler callbackHandler = new CallbackHandler(mFriendList);
                SharedPreferences mpreferences  = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean udpEnabled              = mpreferences.getBoolean("enable_udp", false);
                ToxOptions toxOptions           = new ToxOptions(Options.ipv6Enabled, udpEnabled, Options.proxyEnabled);
                JTox jTox                       = new JTox(mFriendList, callbackHandler, toxOptions);
                DataFile mDataFile              = new DataFile(getApplicationContext(), username);
                mDataFile.saveFile(jTox.save());
                ID                              = jTox.getAddress();
                fileBytes                       = mDataFile.loadFile();
            } catch (ToxException e) {
                if (Application.APPDEBUG) {
                    Log.d(TAG, e.getMessage());
                }
            }

            if (Application.APPDEBUG) {
                Log.d(TAG, "@3@");

            }
            SharedPreferences mpreferences  = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = mpreferences.edit();
            editor.putString(AppConstant.OMEPARSEUSERACTIVEACCOUNTSKEY, username);
            editor.putString(AppConstant.OMEPARSEUSERNICKNAMEKEY, username);
            editor.putString(AppConstant.OMEPARSEUSERSTATUSKEY, "online");
            editor.putString(AppConstant.OMEPARSEUSERSTATUALERTSKEY, getResources().getString(R.string.pref_default_status_message));
            editor.putString(AppConstant.OMEPARSEUSERIDKEY, ID);
            editor.putBoolean(AppConstant.OMEPARSEUSERLOGGEDINSKEY, true);
            editor.apply();


            // Start core service
            Intent startCoreService = new Intent(getApplicationContext(), CoreService.class);
            getApplicationContext().startService(startCoreService);
           // finish();
        } else if(preferences.getBoolean("loggedin", false)) {
            db.close();
            // Attempt to start service in case it's not running
            Intent startMessenger = new Intent(getApplicationContext(), CoreService.class);
            getApplicationContext().startService(startMessenger);

            // Launch local activity
            if (DispatchActivity.getGooglePlayServicesState()) {
                Intent invokeLocalActivityIntent = new Intent(LoginActivity.this, LocalWithoutMapActivity.class);
                startActivity(invokeLocalActivityIntent);
            } else {
                Intent invokeCnLocalActivityIntent = new Intent(LoginActivity.this, CnLocalWithoutMapActivity.class);
                startActivity(invokeCnLocalActivityIntent);
            }

            finish();
        }
        */

    }


    private void loginLocalMessenger(String username) {

        /*
        IdentityDatabase db = new IdentityDatabase(getApplicationContext());

        if (Application.APPDEBUG) {
            Log.d(TAG, " login database");

        }

        if (db.login(username)) {

            if (Application.APPDEBUG) {
                Log.d(TAG, "@5@");

            }
            // Set that we're logged in and active user's details
            String[] details                = db.getUserDetails(username);
            db.close();
            SharedPreferences preferences   = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("loggedin", true);
            editor.putString("active_account", username);
            editor.putString("nickname", details[0]);
            editor.putString("status", details[1]);
            editor.putString("status_message", details[2]);
            editor.apply();

            // Init Tox and start service
            Intent startTox = new Intent(getApplicationContext(), CoreService.class);
            getApplicationContext().startService(startTox);

            // Launch main activity
            //Intent main = new Intent(getApplicationContext(), MainActivity.class);
            // startActivity(main);

            // finish();

            //dialog.dismiss();
            if (DispatchActivity.getGooglePlayServicesState()) {
                Intent invokeLocalActivityIntent = new Intent(LoginActivity.this, LocalWithoutMapActivity.class);
                startActivity(invokeLocalActivityIntent);
            } else {
                Intent invokeCnLocalActivityIntent = new Intent(LoginActivity.this, CnLocalWithoutMapActivity.class);
                startActivity(invokeCnLocalActivityIntent);
            }

            //finish();

        } else {

            if (Application.APPDEBUG) {
                Toast.makeText(LoginActivity.this, " login database error", Toast.LENGTH_LONG)
                        .show();

            }

        }
        */
    }


}