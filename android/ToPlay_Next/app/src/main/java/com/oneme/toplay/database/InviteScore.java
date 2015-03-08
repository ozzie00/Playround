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
// Data model for invite score
//
@ParseClassName(AppConstant.OMETOPLAYINVITESCORECLASSKEY)
public final class InviteScore extends ParseObject {

    private static final String TAG = "InviteScore";

    public ParseUser getAuthor() {
        return getParseUser(AppConstant.OMEPARSEINVITESCOREAUTHORKEY);
    }

    public void setAuthor(ParseUser user) {
        put(AppConstant.OMEPARSEINVITESCOREAUTHORKEY, user);
    }

    public String getAuthorUsername() {
        if (getString(AppConstant.OMEPARSEINVITESCOREAUTHORNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEINVITESCOREAUTHORNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setAuthorUsername(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITESCOREAUTHORNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITESCOREAUTHORNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getContent() {
        if (getString(AppConstant.OMEPARSEINVITESCORECONTENTKEY) != null) {
            return getString(AppConstant.OMEPARSEINVITESCORECONTENTKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setContent(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITESCORECONTENTKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITESCORECONTENTKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String  getParentObjectId() {return getString(AppConstant.OMEPARSEINVITESCOREPARENTIDKEY);}

    public final void setParentObjectId(String value) {put(AppConstant.OMEPARSEINVITESCOREPARENTIDKEY, value);}


    public static ParseQuery<InviteScore> getQuery() {
        return ParseQuery.getQuery(InviteScore.class);
    }

}
