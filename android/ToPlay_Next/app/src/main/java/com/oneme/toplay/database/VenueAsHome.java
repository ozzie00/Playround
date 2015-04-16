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
@ParseClassName(AppConstant.OMETOPLAYVENUEASHOMECLASSKEY)
public final class VenueAsHome extends ParseObject {

    private static final String TAG = "VenueAsHome";

    public ParseUser getAuthor() {
        return getParseUser(AppConstant.OMEPARSEVENUEHOMEAUTHORKEY);
    }

    public void setAuthor(ParseUser user) {
        put(AppConstant.OMEPARSEVENUEHOMEAUTHORKEY, user);
    }

    public String getAuthorUsername() {
        if (getString(AppConstant.OMEPARSEVENUEHOMEAUTHORNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUEHOMEAUTHORNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setAuthorUsername(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUEHOMEAUTHORNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUEHOMEAUTHORNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }



    public String  getParentObjectId() {return getString(AppConstant.OMEPARSEVENUEHOMEPARENTIDKEY);}

    public final void setParentObjectId(String value) {put(AppConstant.OMEPARSEVENUEHOMEPARENTIDKEY, value);}

    public String getOther() {
        if (getString(AppConstant.OMEPARSEVENUEHOMEOTHERKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUEHOMEOTHERKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setOther(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUEHOMEOTHERKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUEHOMEOTHERKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public static ParseQuery<VenueAsHome> getQuery() {
        return ParseQuery.getQuery(VenueAsHome.class);
    }

}
