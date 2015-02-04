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


/**
 * Data model for an invitation.
 */
@ParseClassName(AppConstant.OMETOPLAYINVITECLASSKEY)
public final class Invite extends ParseObject {

    private static final String TAG = "Invite";

    public String getText() {
        return getString("text");
    }

    public void setText(String value) {
        put("text", value);
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

    public String getUserLevel() {return getString(AppConstant.OMEPARSEUSERLEVELKEY);}

    public void setUserLevel(String value) {put(AppConstant.OMEPARSEUSERLEVELKEY, value);}

    public String getSportType() {return getString(AppConstant.OMEPARSEINVITESPORTTYPEKEY);}

    public void setSportType(String value) {put(AppConstant.OMEPARSEINVITESPORTTYPEKEY, value);}

    public String getSportTypeValue() {

        //Ozzie Zhang 2014-12-23 only for test
        if (getString(AppConstant.OMEPARSEINVITESPORTTYPEVALUEKEY) != null)  {
            return getString(AppConstant.OMEPARSEINVITESPORTTYPEVALUEKEY);
        } else {
            return "0";
        }
    }

    public void setSportTypeValue(String value) {put(AppConstant.OMEPARSEINVITESPORTTYPEVALUEKEY, value);}

    public String getPlayerLevel() {return getString(AppConstant.OMEPARSEINVITEPLAYERLEVELKEY);}

    public void setPlayerLevel(String value) {put(AppConstant.OMEPARSEINVITEPLAYERLEVELKEY, value);}

    public String getPlayerNumber() {return getString(AppConstant.OMEPARSEINVITEPLAYERNUMBERKEY);}

    public void setPlayerNumber(String value) {put(AppConstant.OMEPARSEINVITEPLAYERNUMBERKEY, value);}

    public String getPlayTime() {return getString(AppConstant.OMEPARSEINVITETIMEKEY);}

    public void setPlayTime(String value) {put(AppConstant.OMEPARSEINVITETIMEKEY, value);}

    public String getCourt() {return getString(AppConstant.OMEPARSEINVITECOURTKEY);}

    public void setCourt(String value) {put(AppConstant.OMEPARSEINVITECOURTKEY, value);}

    public String getFee() {return getString(AppConstant.OMEPARSEINVITEFEEKEY);}

    public void setFee(String value) {put(AppConstant.OMEPARSEINVITEFEEKEY, value);}

    public String getOther() {return getString(AppConstant.OMEPARSEINVITEOTHERINFOKEY);}

    public void setOther(String value) {put(AppConstant.OMEPARSEINVITEOTHERINFOKEY, value);}

    public ParseUser getFromUser() {return getParseUser(AppConstant.OMEPARSEINVITEFROMUSERKEY);}

    public void setFromUser(ParseUser value) {put(AppConstant.OMEPARSEINVITEFROMUSERKEY, value);}

    public String getFromUsername() {return getString(AppConstant.OMEPARSEINVITEFROMUSERNAMEKEY);}

    public void setFromUsername(String value) {put(AppConstant.OMEPARSEINVITEFROMUSERNAMEKEY, value);}

    public String getSubmitTime() {return getString(AppConstant.OMEPARSEINVITESUBMITTIMEKEY);}

    public void setSubmitTime(String value) {put(AppConstant.OMEPARSEINVITESUBMITTIMEKEY, value);}


    public static ParseQuery<Invite> getQuery() {
        return ParseQuery.getQuery(Invite.class);
    }

  //  public static ParseQuery<Invite> getQuery() {
  //      return ParseQuery.getQuery(AppConstant.OMETOPLAYINVITECLASSKEY);
  //  }
}
