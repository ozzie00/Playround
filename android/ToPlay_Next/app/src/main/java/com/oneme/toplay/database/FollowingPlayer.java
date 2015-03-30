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

package com.oneme.toplay.database;


import com.oneme.toplay.base.AppConstant;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


//
// Data model for following player
//
@ParseClassName(AppConstant.OMETOPLAYFOLLOWINGCLASSKEY)
public final class FollowingPlayer extends ParseObject {

    private static final String TAG = "FollowingPlayer";

    public ParseUser getFollowingUser() {
        return getParseUser(AppConstant.OMEPARSEFOLLOWINGPLAYERUSERKEY);
    }

    public void setFollowingUser(ParseUser user) {
        put(AppConstant.OMEPARSEFOLLOWINGPLAYERUSERKEY, user);
    }

    public String getFollowingUsername() {
        if (getString(AppConstant.OMEPARSEFOLLOWINGPLAYERUSERNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEFOLLOWINGPLAYERUSERNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setFollowingUsername(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEFOLLOWINGPLAYERUSERNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEFOLLOWINGPLAYERUSERNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public ParseUser getFollowerUser() {
        return getParseUser(AppConstant.OMEPARSEFOLLOWERPLAYERUSERKEY);
    }

    public void setFollowerUser(ParseUser user) {
        put(AppConstant.OMEPARSEFOLLOWERPLAYERUSERKEY, user);
    }

    public String getFollowerUsername() {
        if (getString(AppConstant.OMEPARSEFOLLOWERPLAYERUSERNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEFOLLOWERPLAYERUSERNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setFollowerUsername(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEFOLLOWERPLAYERUSERNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEFOLLOWERPLAYERUSERNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getFollowingOther() {
        if (getString(AppConstant.OMEPARSEFOLLOWINGPLAYEROTHERKEY) != null) {
            return getString(AppConstant.OMEPARSEFOLLOWINGPLAYEROTHERKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setFolloweringOther(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEFOLLOWINGPLAYEROTHERKEY, value);
        } else {
            put(AppConstant.OMEPARSEFOLLOWINGPLAYEROTHERKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }



    public static ParseQuery<FollowingPlayer> getQuery() {
        return ParseQuery.getQuery(FollowingPlayer.class);
    }

}
