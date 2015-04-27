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
// Data model for booking venue
//
@ParseClassName(AppConstant.OMETOPLAYBOOKINGVENUECLASSKEY)
public final class BookingVenue extends ParseObject {

    private static final String TAG = "BookingVenue";

    public static final String OMEPARSEBOOKINGFINISHSTATUSKEY  = "bookingFinishStatus";

    public ParseUser getAuthor() {
        return getParseUser(AppConstant.OMEPARSEBOOKINGAUTHORKEY);
    }

    public void setAuthor(ParseUser user) {
        put(AppConstant.OMEPARSEBOOKINGAUTHORKEY, user);
    }

    public String getAuthorUsername() {
        if (getString(AppConstant.OMEPARSEBOOKINGAUTHORNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEBOOKINGAUTHORNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setAuthorUsername(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEBOOKINGAUTHORNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEBOOKINGAUTHORNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }


    public String  getVenueObjectId() {return getString(AppConstant.OMEPARSEBOOKINGVENUEOBJECTIDKEY);}

    public final void setVenueObjectId(String value) {put(AppConstant.OMEPARSEBOOKINGVENUEOBJECTIDKEY, value);}

    public String getVenueName() {
        if (getString(AppConstant.OMEPARSEBOOKINGVENUENAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEBOOKINGVENUENAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setVenueName(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEBOOKINGVENUENAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEBOOKINGVENUENAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getTime() {
        if (getString(AppConstant.OMEPARSEBOOKINGTIMEKEY) != null) {
            return getString(AppConstant.OMEPARSEBOOKINGTIMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setTime(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEBOOKINGTIMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEBOOKINGTIMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getSubmitTime() {
        if (getString(AppConstant.OMEPARSEBOOKINGSUBMITTIMEKEY) != null) {
            return getString(AppConstant.OMEPARSEBOOKINGSUBMITTIMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setSubmitTime(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEBOOKINGSUBMITTIMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEBOOKINGSUBMITTIMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getPayStatus() {
        if (getString(AppConstant.OMEPARSEBOOKINGPAYSTATUSKEY) != null) {
            return getString(AppConstant.OMEPARSEBOOKINGPAYSTATUSKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setPayStatus(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEBOOKINGPAYSTATUSKEY, value);
        } else {
            put(AppConstant.OMEPARSEBOOKINGPAYSTATUSKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getPayNumber() {
        if (getString(AppConstant.OMEPARSEBOOKINGPAYNUMBERKEY) != null) {
            return getString(AppConstant.OMEPARSEBOOKINGPAYNUMBERKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setPayNumber(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEBOOKINGPAYNUMBERKEY, value);
        } else {
            put(AppConstant.OMEPARSEBOOKINGPAYNUMBERKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }


    public String getRefundStatus() {
        if (getString(AppConstant.OMEPARSEBOOKINGREFUNDSTATUSKEY) != null) {
            return getString(AppConstant.OMEPARSEBOOKINGREFUNDSTATUSKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setRefundStatus(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEBOOKINGREFUNDSTATUSKEY, value);
        } else {
            put(AppConstant.OMEPARSEBOOKINGREFUNDSTATUSKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getFinishStatus() {
        if (getString(AppConstant.OMEPARSEBOOKINGFINISHSTATUSKEY) != null) {
            return getString(AppConstant.OMEPARSEBOOKINGFINISHSTATUSKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setFinishStatus(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEBOOKINGFINISHSTATUSKEY, value);
        } else {
            put(AppConstant.OMEPARSEBOOKINGFINISHSTATUSKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getOther() {
        if (getString(AppConstant.OMEPARSEBOOKINGOTHERKEY) != null) {
            return getString(AppConstant.OMEPARSEBOOKINGOTHERKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setOther(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEBOOKINGOTHERKEY, value);
        } else {
            put(AppConstant.OMEPARSEBOOKINGOTHERKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public static ParseQuery<BookingVenue> getQuery() {
        return ParseQuery.getQuery(BookingVenue.class);
    }

}
