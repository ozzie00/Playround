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
// Data model for venue like
//
@ParseClassName(AppConstant.OMETOPLAYVENUELIKECLASSKEY)
public final class VenueLike extends ParseObject {

    private static final String TAG = "VenueLike";

    public ParseUser getAuthor() {
        return getParseUser(AppConstant.OMEPARSEVENUELIKEAUTHORKEY);
    }

    public void setAuthor(ParseUser user) {
        put(AppConstant.OMEPARSEVENUELIKEAUTHORKEY, user);
    }

    public String getAuthorUsername() {
        if (getString(AppConstant.OMEPARSEVENUELIKEAUTHORNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUELIKEAUTHORNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setAuthorUsername(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUELIKEAUTHORNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUELIKEAUTHORNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }



    public String  getParentObjectId() {return getString(AppConstant.OMEPARSEVENUELIKEPARENTIDKEY);}

    public final void setParentObjectId(String value) {put(AppConstant.OMEPARSEVENUELIKEPARENTIDKEY, value);}

    public String getOther() {
        if (getString(AppConstant.OMEPARSEVENUELIKEOTHERKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUELIKEOTHERKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setOther(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUELIKEOTHERKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUELIKEOTHERKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public static ParseQuery<VenueLike> getQuery() {
        return ParseQuery.getQuery(VenueLike.class);
    }

}
