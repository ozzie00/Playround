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
package com.oneme.toplay.base;

import com.oneme.toplay.database.Venue;
import com.oneme.toplay.database.VenueAsHome;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class Homeing {

    // define submit venue as home to cloud
    public static void setAsHome(final Venue venue, final ParseUser user) {
        // Check user, then submit invitation
        if (venue == null || user == null) {
            return;
        } else {
            final String musername = user.getUsername();
            final String mobjectid = venue.getObjectId();
            ParseQuery<VenueAsHome> ashomequery = VenueAsHome.getQuery();
            ashomequery.whereEqualTo(AppConstant.OMEPARSEVENUEHOMEAUTHORNAMEKEY, musername);
            ashomequery.whereEqualTo(AppConstant.OMEPARSEVENUEHOMEPARENTIDKEY, mobjectid);
            ashomequery.countInBackground(new CountCallback() {
                @Override
                public void done(int i, ParseException e) {

                    //android.util.Log.d("Homeing ", " my home field  " + i);
                    if (e == null) {
                        if (i < 1) {
                            // Create a venue as home if current user has not followed this user before
                            VenueAsHome ashome = new VenueAsHome();
                            ashome.setAuthor(user);
                            ashome.setAuthorUsername(musername);
                            ashome.setParentObjectId(mobjectid);

                            ParseACL macl = new ParseACL();
                            macl.setPublicReadAccess(true);
                            macl.setWriteAccess(user, true);

                            ashome.setACL(macl);
                            ashome.saveInBackground();
                        }
                    }
                }
            });
        }

        return;

    }

    // define submit cancel as home to cloud
    public static void cancelAsHome(final Venue venue, final ParseUser user) {
        // Check user, then submit unfollow
        if (venue == null || user == null) {

            return;
        } else {
            final String musername = user.getUsername();
            final String mobjectid = venue.getObjectId();
            ParseQuery<VenueAsHome> removequery = VenueAsHome.getQuery();
            removequery.whereEqualTo(AppConstant.OMEPARSEVENUEHOMEAUTHORNAMEKEY, musername);
            removequery.whereEqualTo(AppConstant.OMEPARSEVENUEHOMEPARENTIDKEY, mobjectid);
            removequery.findInBackground(new FindCallback<VenueAsHome>() {
                @Override
                public void done(List<VenueAsHome> notashome, ParseException e) {
                    if (e == null) {
                        for (VenueAsHome delete : notashome) {
                            delete.deleteInBackground();
                        }
                    } else {

                    }
                }
            });

        }

        return;

    }
}
