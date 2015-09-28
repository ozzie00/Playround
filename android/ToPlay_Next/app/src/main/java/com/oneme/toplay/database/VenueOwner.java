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

//
// define data model for venue owner
//
@ParseClassName(AppConstant.OMETOPLAYVENUEOWNERCLASSKEY)
public final class VenueOwner extends ParseObject {

    private static final String TAG = "VenueOwner";

    public String getName() {
        if (getString(AppConstant.OMEPARSEVENUEOWNERNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUEOWNERNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setName(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUEOWNERNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUEOWNERNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public ParseUser getUser() {
        return getParseUser(AppConstant.OMEPARSEUSERKEY);
    }

    public void setUser(ParseUser value) {
        put(AppConstant.OMEPARSEUSERKEY, value);
    }

    public String  getOmeID() {
        if (getString(AppConstant.OMEPARSEVENUEOWNEROMEIDKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUEOWNEROMEIDKEY);
        } else {
            return AppConstant.OMEPARSEVENUEOWNEROMEIDNULL;
        }
   }

    public final void setOmeID(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUEOWNEROMEIDKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUEOWNEROMEIDKEY, AppConstant.OMEPARSEVENUEOWNEROMEIDNULL);
        }
    }

    public ParseFile getIcon() {
        return getParseFile(AppConstant.OMEPARSEVENUEOWNERICONKEY);
    }

    public void setIcon(ParseFile file) {
        put(AppConstant.OMEPARSEVENUEOWNERICONKEY, file);
    }



    public String getAddress() {
        if (getString(AppConstant.OMEPARSEVENUEOWNERADDRESSKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUEOWNERADDRESSKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setAddress(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUEOWNERADDRESSKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUEOWNERADDRESSKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(AppConstant.OMEPARSEVENUEOWNERLOCATIONKEY);
    }

    public void setLocation(ParseGeoPoint value) {
        put(AppConstant.OMEPARSEVENUEOWNERLOCATIONKEY, value);
    }

    public String getPhone() {
        if ((getString(AppConstant.OMEPARSEVENUEOWNERPHONEKEY) != null)
                && (!getString(AppConstant.OMEPARSEVENUEOWNERPHONEKEY).equals(AppConstant.OMEPARSENULLSTRING))){
            return getString(AppConstant.OMEPARSEVENUEOWNERPHONEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setPhone(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUEOWNERPHONEKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUEOWNERPHONEKEY, AppConstant.OMEPARSENULLSTRING);
        }

    }

    public String getCourtNumber() {
        if (getString(AppConstant.OMEPARSEVENUEOWNERCOURTNUMBERKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUEOWNERCOURTNUMBERKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setCourtNumber(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUEOWNERCOURTNUMBERKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUEOWNERCOURTNUMBERKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getLighted() {
        if (getString(AppConstant.OMEPARSEVENUEOWNERLIGHTEDKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUEOWNERLIGHTEDKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setLighted(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUEOWNERLIGHTEDKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUEOWNERLIGHTEDKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getIndoor() {
        if (getString(AppConstant.OMEPARSEVENUEOWNERINDOORKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUEOWNERINDOORKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setIndoor(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUEOWNERINDOORKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUEOWNERINDOORKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getPublic() {
        if (getString(AppConstant.OMEPARSEVENUEOWNERPUBLICKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUEOWNERPUBLICKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setPublic(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUEOWNERPUBLICKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUEOWNERPUBLICKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public Boolean getVerify() {
        return getBoolean(AppConstant.OMEPARSEVENUEOWNERVERIFYKEY);
    }

    public void setVerify(Boolean value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUEOWNERVERIFYKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUEOWNERVERIFYKEY, false);
        }
    }

    public String getContactName() {
        if (getString(AppConstant.OMEPARSEVENUEOWNERCONTACTNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUEOWNERCONTACTNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setContactName(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUEOWNERCONTACTNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUEOWNERCONTACTNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getContactPhone() {
        if (getString(AppConstant.OMEPARSEVENUEOWNERCONTACTPHONEKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUEOWNERCONTACTPHONEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setContactPhone(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUEOWNERCONTACTPHONEKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUEOWNERCONTACTPHONEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getContactEmail() {
        if (getString(AppConstant.OMEPARSEVENUEOWNERCONTACTEMAILKEY) != null) {
            return getString(AppConstant.OMEPARSEVENUEOWNERCONTACTEMAILKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setContactEmail(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEVENUEOWNERCONTACTEMAILKEY, value);
        } else {
            put(AppConstant.OMEPARSEVENUEOWNERCONTACTEMAILKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public ParseFile getIdCopyImage() {
        return getParseFile(AppConstant.OMEPARSEVENUEOWNERIDCOPYKEY);
    }

    public void setIdCopyImage(ParseFile file) {
        if (file != null) {
            put(AppConstant.OMEPARSEVENUEOWNERIDCOPYKEY, file);
        }
    }

    public ParseFile getLicenseImage() {
        return getParseFile(AppConstant.OMEPARSEVENUEOWNERLICENSECOPYKEY);
    }

    public void setLicenseImage(ParseFile file) {
        if (file != null) {
            put(AppConstant.OMEPARSEVENUEOWNERLICENSECOPYKEY, file);
        }
    }



    public static ParseQuery<VenueOwner> getQuery() {
        return ParseQuery.getQuery(VenueOwner.class);
    }

}