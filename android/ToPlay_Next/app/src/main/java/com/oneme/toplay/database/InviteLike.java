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
// Data model for invite like
//
@ParseClassName(AppConstant.OMETOPLAYINVITELIKECLASSKEY)
public final class InviteLike extends ParseObject {

    private static final String TAG = "InviteLike";

    public ParseUser getAuthor() {
        return getParseUser(AppConstant.OMEPARSEINVITELIKEAUTHORKEY);
    }

    public void setAuthor(ParseUser user) {
        put(AppConstant.OMEPARSEINVITELIKEAUTHORKEY, user);
    }

    public String getAuthorUsername() {
        if (getString(AppConstant.OMEPARSEINVITELIKEAUTHORNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEINVITELIKEAUTHORNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setAuthorUsername(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITELIKEAUTHORNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITELIKEAUTHORNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String  getParentObjectId() {return getString(AppConstant.OMEPARSEINVITELIKEPARENTIDKEY);}

    public final void setParentObjectId(String value) {put(AppConstant.OMEPARSEINVITELIKEPARENTIDKEY, value);}

    public static ParseQuery<InviteLike> getQuery() {
        return ParseQuery.getQuery(InviteLike.class);
    }

}
