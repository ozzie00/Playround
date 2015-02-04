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

import com.oneme.toplay.local.LocalWithoutMapActivity;
import com.oneme.toplay.local.CnLocalWithoutMapActivity;

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

        // Check google play service available
        if (checkGooglePlayServicesAvailable()) {

            // Check if there is current user info
            if (ParseUser.getCurrentUser() != null) {
                // Start an intent for the logged in activity
                startActivity(new Intent(this, LocalWithoutMapActivity.class));
            } else {
                // Start and intent for the logged out activity
                startActivity(new Intent(this, LocalWithoutMapActivity.class));
            }
        } else {
         //   Toast.makeText(this, getResources().getString(R.string.google_play_service_unavailable), Toast.LENGTH_LONG).show();
         //   Toast.makeText(this, getResources().getString(R.string.switch_to_china_map), Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, CnLocalWithoutMapActivity.class));
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
