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
package com.oneme.toplay.database;

import com.oneme.toplay.base.AppConstant;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

//
// define data model for venue
//
@ParseClassName(AppConstant.OMETOPLAYPHOTOLINKCLASSKEY)
public final class PhotoLink extends ParseObject {

    private static final String TAG = "PhotoLink";

    public String getVenueObjectId() {
        return getString(AppConstant.OMETOPLAYPHOTOLINKOBJECTKEY);
    }

    public void setVenueObjectId(String objectid) {
        put(AppConstant.OMETOPLAYPHOTOLINKOBJECTKEY, objectid);
    }

    public String getPhotoObjectId() {
        return getString(AppConstant.OMETOPLAYPHOTOLINKPHOTOTOBJECTKEY);
    }

    public void setPhotoObjectId(String objectid) {
        put(AppConstant.OMETOPLAYPHOTOLINKPHOTOTOBJECTKEY, objectid);
    }

    public String getLinkType() {
        if (getString(AppConstant.OMETOPLAYPHOTOLINKTYPEKEY) != null) {
            return getString(AppConstant.OMETOPLAYPHOTOLINKTYPEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setLinkType(String value) {
        if (value != null) {
            put(AppConstant.OMETOPLAYPHOTOLINKTYPEKEY, value);
        } else {
            put(AppConstant.OMETOPLAYPHOTOLINKTYPEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }


    public static ParseQuery<PhotoLink> getQuery() {
        return ParseQuery.getQuery(PhotoLink.class);
    }

}