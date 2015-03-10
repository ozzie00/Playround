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
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseFile;


//
// Data model for an invitation.
//
@ParseClassName(AppConstant.OMETOPLAYINVITECLASSKEY)
public final class Invite extends ParseObject {

    private static final String TAG = "Invite";

    // text for workout name
    public String getText() {
        if (getString(AppConstant.OMEPARSETEXTKEY) != null) {
            return getString(AppConstant.OMEPARSETEXTKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setText(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSETEXTKEY, value);
        } else {
            put(AppConstant.OMEPARSETEXTKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public ParseUser getUser() {
        return getParseUser(AppConstant.OMEPARSEUSERKEY);
    }

    public void setUser(ParseUser value) {
        put(AppConstant.OMEPARSEUSERKEY, value);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(AppConstant.OMEPARSELOCATIONKEY);
    }

    public void setLocation(ParseGeoPoint value) {
        put(AppConstant.OMEPARSELOCATIONKEY, value);
    }

    public ParseFile getUserIcon() {
        return getParseFile(AppConstant.OMEPARSEUSERICONKEY);
    }

    public void setUserIcon(ParseFile file) {
        put(AppConstant.OMEPARSEUSERICONKEY, file);
    }

    public String getUserLevel() {
        if (getString(AppConstant.OMEPARSEUSERLEVELKEY) != null) {
            return getString(AppConstant.OMEPARSEUSERLEVELKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setUserLevel(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEUSERLEVELKEY, value);
        } else {
            put(AppConstant.OMEPARSEUSERLEVELKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getSportType() {
        if (getString(AppConstant.OMEPARSEINVITESPORTTYPEKEY) != null) {
            return getString(AppConstant.OMEPARSEINVITESPORTTYPEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setSportType(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITESPORTTYPEKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITESPORTTYPEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getSportTypeValue() {

        //Ozzie Zhang 2014-12-23 only for test
        if (getString(AppConstant.OMEPARSEINVITESPORTTYPEVALUEKEY) != null)  {
            return getString(AppConstant.OMEPARSEINVITESPORTTYPEVALUEKEY);
        } else {
            return AppConstant.OMEPARSESPORTVALUENULL;
        }
    }

    public void setSportTypeValue(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITESPORTTYPEVALUEKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITESPORTTYPEVALUEKEY, AppConstant.OMEPARSESPORTVALUENULL);
        }
    }

    public String getPlayerLevel() {
        if (getString(AppConstant.OMEPARSEINVITEPLAYERLEVELKEY) != null) {
            return getString(AppConstant.OMEPARSEINVITEPLAYERLEVELKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setPlayerLevel(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITEPLAYERLEVELKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITEPLAYERLEVELKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getPlayerNumber() {
        if (getString(AppConstant.OMEPARSEINVITEPLAYERNUMBERKEY) != null) {
            return getString(AppConstant.OMEPARSEINVITEPLAYERNUMBERKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setPlayerNumber(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITEPLAYERNUMBERKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITEPLAYERNUMBERKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getPlayTime() {
        if (getString(AppConstant.OMEPARSEINVITETIMEKEY) != null) {
            return getString(AppConstant.OMEPARSEINVITETIMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setPlayTime(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITETIMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITETIMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getCourt() {
        if (getString(AppConstant.OMEPARSEINVITECOURTKEY) != null) {
            return getString(AppConstant.OMEPARSEINVITECOURTKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setCourt(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITECOURTKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITECOURTKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getFee() {
        if (getString(AppConstant.OMEPARSEINVITEFEEKEY) != null) {
            return getString(AppConstant.OMEPARSEINVITEFEEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setFee(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITEFEEKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITEFEEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getOther() {
        if (getString(AppConstant.OMEPARSEINVITEOTHERINFOKEY) != null) {
            return getString(AppConstant.OMEPARSEINVITEOTHERINFOKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setOther(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITEOTHERINFOKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITEOTHERINFOKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public ParseUser getFromUser() {return getParseUser(AppConstant.OMEPARSEINVITEFROMUSERKEY);}

    public void setFromUser(ParseUser value) {put(AppConstant.OMEPARSEINVITEFROMUSERKEY, value);}

    public String getFromUsername() {
        if (getString(AppConstant.OMEPARSEINVITEFROMUSERNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEINVITEFROMUSERNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setFromUsername(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITEFROMUSERNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITEFROMUSERNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getSubmitTime() {
        if (getString(AppConstant.OMEPARSEINVITESUBMITTIMEKEY) != null) {
            return getString(AppConstant.OMEPARSEINVITESUBMITTIMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setSubmitTime(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITESUBMITTIMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITESUBMITTIMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public Boolean getPublic() {
        return getBoolean(AppConstant.OMEPARSEINVITEPUBLICKEY);}

    public void setPulic(Boolean value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITEPUBLICKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITEPUBLICKEY, false);
        }
    }

    public String getWorkoutName() {
        if (getString(AppConstant.OMEPARSEINVITEWORKNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEINVITEWORKNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setWorkoutName(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEINVITEWORKNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEINVITEWORKNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public static ParseQuery<Invite> getQuery() {
        return ParseQuery.getQuery(Invite.class);
    }

}
