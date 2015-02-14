package com.oneme.toplay;



import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import android.location.Geocoder;

import android.location.Address;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;

import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.VenueOwner;
import com.oneme.toplay.local.LocalWithoutMapActivity;
import com.oneme.toplay.local.CnLocalWithoutMapActivity;

import com.oneme.toplay.venue.OwnerInfoUploadActivity;
import com.oneme.toplay.venue.OwnerMainActivity;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;




//
// Activity which starts an intent for either the logged in (MainActivity) or logged out
// (SignUpOrLoginActivity) activity.
//
public class DispatchActivity extends Activity {

    private static final String TAG = "DispatchActivity";

    public static boolean mavailable = false;

    public DispatchActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser user = ParseUser.getCurrentUser();


            // Check google play service available
            if (checkGooglePlayServicesAvailable()) {

                // Check if there is current user info
                if (user != null) {

                    // check user tag
                    if (user.getString(AppConstant.OMEPARSEUSERTAGKEY).equalsIgnoreCase(AppConstant.OMEPARSEUSERTAGPLAYER)) {

                        // Start an intent for the logged in activity
                        startActivity(new Intent(this, LocalWithoutMapActivity.class));
                        finish();
                    } else if (user.getString(AppConstant.OMEPARSEUSERTAGKEY).equalsIgnoreCase(AppConstant.OMEPARSEUSERTAGVENUE)) {
                        ParseQuery<VenueOwner> query = VenueOwner.getQuery();
                        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                        query.include(AppConstant.OMEPARSEVENUEOWNERCLASSKEY);
                        query.whereEqualTo(AppConstant.OMEPARSEUSERKEY, user);
                        query.setLimit(1);

                        query.getFirstInBackground(new GetCallback<VenueOwner>() {
                            public void done(VenueOwner venueowner, ParseException pe) {
                                if (pe == null) {

                                    if (venueowner.getVerify() != true) {
                                        if (venueowner.getIdCopyImage() == null) {
                                            Intent invokeVenueOwnerActivityIntent = new Intent(DispatchActivity.this, OwnerInfoUploadActivity.class);
                                            startActivity(invokeVenueOwnerActivityIntent);
                                            finish();
                                        } else {
                                            Toast.makeText(DispatchActivity.this, getResources().getString(R.string.OMEPARSEVENUEOWNERWAITINGFORVERIFY), Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    } else if (venueowner.getVerify() == true) {
                                        Intent invokeOwnerMainActivityIntent = new Intent(DispatchActivity.this, OwnerMainActivity.class);
                                        startActivity(invokeOwnerMainActivityIntent);
                                        finish();
                                    }

                                } else {
                                    Toast.makeText(DispatchActivity.this, getResources().getString(R.string.OMEPARSECANNOTGETVENUEINFO), Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });

                    }

                } else {
                    // Start and intent for the logged out activity
                    startActivity(new Intent(this, LocalWithoutMapActivity.class));
                    finish();
                }
            } else {

                // Check if there is current user info
                if (user != null) {

                    // check user tag
                    if (user.getString(AppConstant.OMEPARSEUSERTAGKEY).equalsIgnoreCase(AppConstant.OMEPARSEUSERTAGPLAYER)) {

                        // Start an intent for the logged in activity
                        startActivity(new Intent(this, CnLocalWithoutMapActivity.class));
                        finish();
                    } else if (user.getString(AppConstant.OMEPARSEUSERTAGKEY).equalsIgnoreCase(AppConstant.OMEPARSEUSERTAGVENUE)) {
                        ParseQuery<VenueOwner> query = VenueOwner.getQuery();
                        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                        query.include(AppConstant.OMEPARSEVENUEOWNERCLASSKEY);
                        query.whereEqualTo(AppConstant.OMEPARSEUSERKEY, user);
                        query.setLimit(1);

                        query.getFirstInBackground(new GetCallback<VenueOwner>() {
                            public void done(VenueOwner venueowner, ParseException pe) {
                                if (pe == null) {

                                    if (venueowner.getVerify() != true) {
                                        if (venueowner.getIdCopyImage() == null) {
                                            Intent invokeVenueOwnerActivityIntent = new Intent(DispatchActivity.this, OwnerInfoUploadActivity.class);
                                            startActivity(invokeVenueOwnerActivityIntent);
                                            finish();
                                        } else {
                                            Toast.makeText(DispatchActivity.this, getResources().getString(R.string.OMEPARSEVENUEOWNERWAITINGFORVERIFY), Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    } else if (venueowner.getVerify() == true) {
                                        Intent invokeOwnerMainActivityIntent = new Intent(DispatchActivity.this, OwnerMainActivity.class);
                                        startActivity(invokeOwnerMainActivityIntent);
                                        finish();
                                    }

                                } else {
                                    Toast.makeText(DispatchActivity.this, getResources().getString(R.string.OMEPARSECANNOTGETVENUEINFO), Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });

                    }

                } else {
                    // Start and intent for the logged out activity
                    startActivity(new Intent(this, CnLocalWithoutMapActivity.class));
                    finish();
                }
            }

    }



    // Check google play service
    public boolean checkGooglePlayServicesAvailable() {
        int mgoogleplayStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        // Check google play
        if (mgoogleplayStatus != ConnectionResult.SUCCESS) {

            mavailable = false;
         //   Toast.makeText(this, getResources().getString(R.string.google_play_service_unavailable), Toast.LENGTH_LONG).show();
            return false;
        } else {
            mavailable = true;
            return true;
        }
    }

    public static boolean getGooglePlayServicesState() {
        return mavailable;
    }

}
