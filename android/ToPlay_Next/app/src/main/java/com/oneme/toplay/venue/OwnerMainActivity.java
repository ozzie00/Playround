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

package com.oneme.toplay.venue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.me.UploadVenueActivity;
import com.parse.ParseUser;


public class OwnerMainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ome_activity_venue_owner_main);

        // call update activity
        RelativeLayout update = (RelativeLayout) findViewById(R.id.ownermain_booking_update_block);
        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent invokeUpdateActivityIntent = new Intent(OwnerMainActivity.this, UploadVenueActivity.class);
                startActivity(invokeUpdateActivityIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ParseUser user = ParseUser.getCurrentUser();

        if (user != null && user.getString(AppConstant.OMEPARSEUSERTAGKEY).equalsIgnoreCase(AppConstant.OMEPARSEUSERTAGVENUE)){
            MenuItem logoutItem = menu.add(getResources().getString(R.string.OMEPARSELOGOUT));
            //menu.findItem(R.id.action_login)
            logoutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    // preference logout
                    SharedPreferences preferences   = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("loggedin", false);
                    editor.apply();

                    // Stop the Core Service
                    //Intent stopCoreServiceIntent = new Intent(OwnerMainActivity.this, CoreService.class);
                    //OwnerMainActivity.this.stopService(stopCoreServiceIntent);

                    // Call the Parse log out method
                    ParseUser.logOut();

                    finish();

                    return true;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);

    }
}
