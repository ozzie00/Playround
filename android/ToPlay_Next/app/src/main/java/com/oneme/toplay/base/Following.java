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

import android.content.Context;

import com.oneme.toplay.R;
import com.oneme.toplay.database.FollowingPlayer;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class Following {

    // define submit following to cloud
    public static void followingPlayer(ParseUser followinguser, ParseUser followeruser) {
        final ParseUser mfollowinguser = followinguser;
        final ParseUser mfolloweruser  = followeruser;
        // Check user, then submit invitation
        if (followeruser == null) {
            return;
        } else {
            ParseQuery<FollowingPlayer> followingquery = FollowingPlayer.getQuery();
            followingquery.whereEqualTo(AppConstant.OMEPARSEFOLLOWINGPLAYERUSERNAMEKEY, mfollowinguser.getUsername());
            followingquery.whereEqualTo(AppConstant.OMEPARSEFOLLOWERPLAYERUSERNAMEKEY, mfolloweruser.getUsername());
            followingquery.countInBackground(new CountCallback() {
                @Override
                public void done(int i, ParseException e) {
                    if (e == null) {
                        if (i < 1) {
                            // Create a following if current user has not followed this user before
                            FollowingPlayer mfollowing = new FollowingPlayer();
                            mfollowing.setFollowingUser(mfollowinguser);
                            mfollowing.setFollowingUsername(mfollowinguser.getUsername());
                            mfollowing.setFollowerUser(mfolloweruser);
                            mfollowing.setFollowerUsername(mfolloweruser.getUsername());

                            ParseACL macl = new ParseACL();
                            macl.setPublicReadAccess(true);
                            macl.setWriteAccess(mfolloweruser, true);

                            mfollowing.saveInBackground();
                        }
                    }
                }
            });
        }

        return;

    }

    // define submit unfollow to cloud
    public static void unfollowPlayer(ParseUser followinguser, ParseUser followeruser) {
        // Check user, then submit unfollow
        if (followeruser == null) {

            return;
        } else {
            ParseQuery<FollowingPlayer> removequery = FollowingPlayer.getQuery();
            removequery.whereEqualTo(AppConstant.OMEPARSEFOLLOWINGPLAYERUSERKEY, followinguser);
            removequery.whereEqualTo(AppConstant.OMEPARSEFOLLOWERPLAYERUSERKEY, followeruser);
            removequery.findInBackground(new FindCallback<FollowingPlayer>() {
                @Override
                public void done(List<FollowingPlayer> unfollowList, ParseException e) {
                    if (e == null) {
                        for (FollowingPlayer delete : unfollowList) {
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
