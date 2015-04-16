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
// Data model for venue comment.
//
@ParseClassName(AppConstant.OMETOPLAYVENUECOMMENTCLASSKEY)
public final class VenueComment extends ParseObject {

    private static final String TAG = "VenueComment";

    public ParseUser getAuthor() {
        return getParseUser(AppConstant.OMEPARSEVENUECOMMENTAUTHORKEY);
    }

    public void setAuthor(ParseUser user) {
        put(AppConstant.OMEPARSEVENUECOMMENTAUTHORKEY, user);
    }

    public String getAuthorUsername() {
        if (getString(AppConstant.OMEPARSEVENUECOMMENTAUTHORNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUECOMMENTAUTHORNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setAuthorUsername(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUECOMMENTAUTHORNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUECOMMENTAUTHORNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getContent() {
        if (getString(AppConstant.OMEPARSEVENUECOMMENTCONTENTKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUECOMMENTCONTENTKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setContent(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUECOMMENTCONTENTKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUECOMMENTCONTENTKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public Boolean getPublic() {
        return getBoolean(AppConstant.OMEPARSEVENUECOMMENTPUBLICKEY);}

    public void setPulic(Boolean value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUECOMMENTPUBLICKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUECOMMENTPUBLICKEY, false);
        }
    }

    public String  getParentObjectId() {return getString(AppConstant.OMEPARSEVENUECOMMENTPARENTIDKEY);}

    public final void setParentObjectId(String value) {put(AppConstant.OMEPARSEVENUECOMMENTPARENTIDKEY, value);}

    public String getSubmitTime() {
        if (getString(AppConstant.OMEPARSEVENUECOMMENTSUBMITTIMEKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUECOMMENTSUBMITTIMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setSubmitTime(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUECOMMENTSUBMITTIMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUECOMMENTSUBMITTIMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public static ParseQuery<VenueComment> getQuery() {
        return ParseQuery.getQuery(VenueComment.class);
    }

}
