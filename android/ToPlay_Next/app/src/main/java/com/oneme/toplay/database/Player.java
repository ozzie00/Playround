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
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
/*
public class Player {

    public String username;
    public Image  avatar;
    public String level;
    public List following;
    public List followed;
    public List<Invite> invite;
    public List<Join> join;
    public List group;
    public List comments;

}
*/

@ParseClassName(AppConstant.OMETOPLAYPLAYERCLASSKEY)
public class Player extends ParseObject {

    public Player() {
        // A default constructor is required.
    }

    public ParseUser getUser() {
        return getParseUser(AppConstant.OMEPARSEUSERKEY);
    }

    public void setUser(ParseUser value) {
        put(AppConstant.OMEPARSEUSERKEY, value);
    }

    public String getUsername() {
        return getString(AppConstant.OMEPARSEUSERNAMEKEY);
    }

    public void setUsername(String value) {
        put(AppConstant.OMEPARSEUSERNAMEKEY, value);
    }

    public String getRating() {
        return getString(AppConstant.OMEPARSEPLAYERRATEKEY);
    }

    public void setRating(String rating) {
        put(AppConstant.OMEPARSEPLAYERRATEKEY, rating);
    }

    public ParseFile getPhotoFile() {
        return getParseFile(AppConstant.OMEPARSEPLAYERAVATARKEY);
    }

    public void setPhotoFile(ParseFile file) {
        put(AppConstant.OMEPARSEPLAYERAVATARKEY, file);
    }

    public static ParseQuery<Player> getQuery() {
        return ParseQuery.getQuery(Player.class);
    }

}
