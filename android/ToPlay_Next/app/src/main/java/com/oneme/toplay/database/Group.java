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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


//
// Data model for a group.
//
@ParseClassName(AppConstant.OMETOPLAYGROUPCLASSKEY)
public final class Group extends ParseObject {

    private static final String TAG = "Group";

    public ParseUser getGroupAdmin() {
        return getParseUser(AppConstant.OMEPARSEGROUPADMINKEY);
    }

    public void setGroupAdmin(ParseUser value) {
        put(AppConstant.OMEPARSEGROUPADMINKEY, value);
    }

    public String getGroupAdminUsername() {
        if (getString(AppConstant.OMEPARSEGROUPADMINNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEGROUPADMINNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setGroupAdminUsername(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEGROUPADMINNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEGROUPADMINNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getGroupWorkout() {
        if (getString(AppConstant.OMEPARSEGROUPWORKOUTNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEGROUPWORKOUTNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setGroupWorkout(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEGROUPWORKOUTNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEGROUPWORKOUTNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String  getParentObjectId() {return getString(AppConstant.OMEPARSEGROUPPARENTIDKEY);}

    public final void setParentObjectId(String value) {put(AppConstant.OMEPARSEGROUPPARENTIDKEY, value);}

    public String getGroupSport() {
        if (getString(AppConstant.OMEPARSEGROUPSPORTKEY) != null) {
            return getString(AppConstant.OMEPARSEGROUPSPORTKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setGroupSport(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEGROUPSPORTKEY, value);
        } else {
            put(AppConstant.OMEPARSEGROUPSPORTKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getSportTypeValue() {
        if (getString(AppConstant.OMEPARSEGROUPSPORVALUETKEY) != null)  {
            return getString(AppConstant.OMEPARSEGROUPSPORVALUETKEY);
        } else {
            return AppConstant.OMEPARSESPORTVALUENULL;
        }
    }

    public void setSportTypeValue(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEGROUPSPORVALUETKEY, value);
        } else {
            put(AppConstant.OMEPARSEGROUPSPORVALUETKEY, AppConstant.OMEPARSESPORTVALUENULL);
        }
    }

    public Boolean getGroupPublic() {
        return getBoolean(AppConstant.OMEPARSEGROUPPUBLICKEY);}

    public void setGroupPulic(Boolean value) {
        if (value != null) {
            put(AppConstant.OMEPARSEGROUPPUBLICKEY, value);
        } else {
            put(AppConstant.OMEPARSEGROUPPUBLICKEY, true);
        }
    }

    public ParseFile getGroupIcon() {
        return getParseFile(AppConstant.OMEPARSEGROUPICONKEY);
    }

    public void setGroupIcon(ParseFile file) {
        put(AppConstant.OMEPARSEGROUPICONKEY, file);
    }

    public ParseUser getMemberUser() {
        return getParseUser(AppConstant.OMEPARSEGROUPMEMBERUSERKEY);
    }

    public void setMemberUser(ParseUser value) {
        put(AppConstant.OMEPARSEGROUPMEMBERUSERKEY, value);
    }

    public String getMemberUsername() {
        if (getString(AppConstant.OMEPARSEGROUPMEMBERUSERNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEGROUPMEMBERUSERNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setMemberUsername(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEGROUPMEMBERUSERNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEGROUPMEMBERUSERNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getMemberJoinTime() {
        if (getString(AppConstant.OMEPARSEGROUPMEMBERJOINTIMEKEY) != null) {
            return getString(AppConstant.OMEPARSEGROUPMEMBERJOINTIMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setMemberJoinTime(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEGROUPMEMBERJOINTIMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEGROUPMEMBERJOINTIMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }


    public static ParseQuery<Group> getQuery() {
        return ParseQuery.getQuery(Group.class);
    }

}
