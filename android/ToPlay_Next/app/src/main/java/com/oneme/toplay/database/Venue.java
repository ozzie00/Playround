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
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.ArrayList;

//
// define data model for venue
//
@ParseClassName(AppConstant.OMETOPLAYVENUECLASSKEY)
public final class Venue extends ParseObject {

    private static final String TAG = "Venue";

    public String getName() {
        if (getString(AppConstant.OMETOPLAYVENUENAMEKEY) != null) {
            return getString(AppConstant.OMETOPLAYVENUENAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setName(String value) {
        if (value != null) {
            put(AppConstant.OMETOPLAYVENUENAMEKEY, value);
        } else {
            put(AppConstant.OMETOPLAYVENUENAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public ParseFile getIcon() {
        return getParseFile(AppConstant.OMETOPLAYVENUEICONKEY);
    }

    public void setIcon(ParseFile file) {
        put(AppConstant.OMETOPLAYVENUEICONKEY, file);
    }

    public String getLevel() {
        if (getString(AppConstant.OMETOPLAYVENUELEVELKEY) != null) {
            return getString(AppConstant.OMETOPLAYVENUELEVELKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setLevel(String value) {
        if (value != null) {
            put(AppConstant.OMETOPLAYVENUELEVELKEY, value);
        } else {
            put(AppConstant.OMETOPLAYVENUELEVELKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getType() {
        if (getString(AppConstant.OMETOPLAYVENUETYPEKEY) != null) {
            return getString(AppConstant.OMETOPLAYVENUETYPEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setType(String value) {
        if (value != null) {
            put(AppConstant.OMETOPLAYVENUETYPEKEY, value);
        } else {
            put(AppConstant.OMETOPLAYVENUETYPEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getAddress() {
        if (getString(AppConstant.OMETOPLAYVENUEADDRESSKEY) != null) {
            return getString(AppConstant.OMETOPLAYVENUEADDRESSKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setAddress(String value) {
        if (value != null) {
            put(AppConstant.OMETOPLAYVENUEADDRESSKEY, value);
        } else {
            put(AppConstant.OMETOPLAYVENUEADDRESSKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(AppConstant.OMETOPLAYVENUELOCATIONKEY);
    }

    public void setLocation(ParseGeoPoint value) {
        if (value != null) {
            put(AppConstant.OMETOPLAYVENUELOCATIONKEY, value);
        }
    }

    public String getPhone() {
        if ((getString(AppConstant.OMETOPLAYVENUEPHONEKEY) != null)
                && (!getString(AppConstant.OMETOPLAYVENUEPHONEKEY).equals(AppConstant.OMEPARSENULLSTRING))){
            return getString(AppConstant.OMETOPLAYVENUEPHONEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setPhone(String value) {
        if (value != null) {
            put(AppConstant.OMETOPLAYVENUEPHONEKEY, value);
        } else {
            put(AppConstant.OMETOPLAYVENUEPHONEKEY, AppConstant.OMEPARSENULLSTRING);
        }

    }

    public String getCourtNumber() {
        if (getString(AppConstant.OMETOPLAYVENUECOURTNUMBERKEY) != null) {
            return getString(AppConstant.OMETOPLAYVENUECOURTNUMBERKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setCourtNumber(String value) {
        if (value != null) {
            put(AppConstant.OMETOPLAYVENUECOURTNUMBERKEY, value);
        } else {
            put(AppConstant.OMETOPLAYVENUECOURTNUMBERKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getLighted() {
        if (getString(AppConstant.OMETOPLAYVENUELIGHTEDKEY) != null) {
            return getString(AppConstant.OMETOPLAYVENUELIGHTEDKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setLighted(String value) {
        if (value != null) {
            put(AppConstant.OMETOPLAYVENUELIGHTEDKEY, value);
        } else {
            put(AppConstant.OMETOPLAYVENUELIGHTEDKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getIndoor() {
        if (getString(AppConstant.OMETOPLAYVENUEINDOORKEY) != null) {
            return getString(AppConstant.OMETOPLAYVENUEINDOORKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setIndoor(String value) {
        if (value != null) {
            put(AppConstant.OMETOPLAYVENUEINDOORKEY, value);
        } else {
            put(AppConstant.OMETOPLAYVENUEINDOORKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getPublic() {
        if (getString(AppConstant.OMETOPLAYVENUEPUBLICKEY) != null) {
            return getString(AppConstant.OMETOPLAYVENUEPUBLICKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setPublic(String value) {
        if (value != null) {
            put(AppConstant.OMETOPLAYVENUEPUBLICKEY, value);
        } else {
            put(AppConstant.OMETOPLAYVENUEPUBLICKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getUploadedBy() {
        if (getString(AppConstant.OMETOPLAYVENUEUPLOADEDBYKEY) != null) {
            return getString(AppConstant.OMETOPLAYVENUEUPLOADEDBYKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setUploadedBy(String value) {
        if (value != null) {
            put(AppConstant.OMETOPLAYVENUEUPLOADEDBYKEY, value);
        } else {
            put(AppConstant.OMETOPLAYVENUEUPLOADEDBYKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public void getPlayerAsHome(ArrayList<String> player) {
        if (get(AppConstant.OMETOPLAYVENUEPLAYERASHOMEKEY) != null) {
            player = (ArrayList<String>) get(AppConstant.OMETOPLAYVENUEPLAYERASHOMEKEY);
        }
    }

    public void setPlayerAsHome(String value) {
        if (value != null) {
            addUnique(AppConstant.OMETOPLAYVENUEPLAYERASHOMEKEY, Arrays.asList(value));
        }
    }

    public String getDescription() {
        if (getString(AppConstant.OMETOPLAYVENUEDESCRIPTIONKEY) != null) {
            return getString(AppConstant.OMETOPLAYVENUEDESCRIPTIONKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setDescription(String value) {
        if (value != null) {
            put(AppConstant.OMETOPLAYVENUEDESCRIPTIONKEY, value);
        } else {
            put(AppConstant.OMETOPLAYVENUEDESCRIPTIONKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getBusiness() {
        if (getString(AppConstant.OMETOPLAYVENUEBUSINESSKEY) != null) {
            return getString(AppConstant.OMETOPLAYVENUEBUSINESSKEY);
        } else {
            return AppConstant.OMETOPLAYVENUEBUSSINESSFREE;
        }
    }

    public void setBusiness(String value) {
        if (value != null) {
            put(AppConstant.OMETOPLAYVENUEBUSINESSKEY, value);
        } else {
            put(AppConstant.OMETOPLAYVENUEBUSINESSKEY, AppConstant.OMETOPLAYVENUEBUSSINESSFREE);
        }
    }



    public static ParseQuery<Venue> getQuery() {
        return ParseQuery.getQuery(Venue.class);
    }

}