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
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


//
// Data model for invite comment.
//
@ParseClassName(AppConstant.OMETOPLAYINVITECOMMENTCLASSKEY)
public final class InviteComment extends ParseObject {

    private static final String TAG = "InviteComment";

    public ParseUser getAuthor() {
        return getParseUser(AppConstant.OMEPARSEINVITECOMMENTAUTHORKEY);
    }

    public void setAuthor(ParseUser user) {
        put(AppConstant.OMEPARSEINVITECOMMENTAUTHORKEY, user);
    }

    public String getAuthorUsername() {
        if (getString(AppConstant.OMEPARSEINVITECOMMENTAUTHORNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEINVITECOMMENTAUTHORNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setAuthorUsername(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITECOMMENTAUTHORNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITECOMMENTAUTHORNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getContent() {
        if (getString(AppConstant.OMEPARSEINVITECOMMENTCONTENTKEY) != null) {
            return getString(AppConstant.OMEPARSEINVITECOMMENTCONTENTKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setContent(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITECOMMENTCONTENTKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITECOMMENTCONTENTKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public Boolean getPublic() {
        return getBoolean(AppConstant.OMEPARSEINVITECOMMENTPUBLICKEY);}

    public void setPulic(Boolean value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITECOMMENTPUBLICKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITECOMMENTPUBLICKEY, false);
        }
    }

    public String  getParentObjectId() {return getString(AppConstant.OMEPARSEINVITECOMMENTPARENTIDKEY);}

    public final void setParentObjectId(String value) {put(AppConstant.OMEPARSEINVITECOMMENTPARENTIDKEY, value);}

    public String getSubmitTime() {
        if (getString(AppConstant.OMEPARSEINVITECOMMENTSUBMITTIMEKEY) != null) {
            return getString(AppConstant.OMEPARSEINVITECOMMENTSUBMITTIMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setSubmitTime(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITECOMMENTSUBMITTIMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITECOMMENTSUBMITTIMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public static ParseQuery<InviteComment> getQuery() {
        return ParseQuery.getQuery(InviteComment.class);
    }

}
