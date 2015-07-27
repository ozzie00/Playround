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
// Data model for pay prime
//
@ParseClassName(AppConstant.OMETOPLAYPAYPRIMECLASSKEY)
public final class PayPrime extends ParseObject {

    private static final String TAG = "PayPrime";

    public String getUsername() {
        if (getString(AppConstant.OMEPARSEPRIMEUSERNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEPRIMEUSERNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setUsername(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEPRIMEUSERNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEPRIMEUSERNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getUserphone() {
        if (getString(AppConstant.OMEPARSEPRIMEUSERPHONEKEY) != null) {
            return getString(AppConstant.OMEPARSEPRIMEUSERPHONEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setUserphone(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEPRIMEUSERPHONEKEY, value);
        } else {
            put(AppConstant.OMEPARSEPRIMEUSERPHONEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String get3rd() {
        if (getString(AppConstant.OMEPARSEPRIME3RDKEY) != null) {
            return getString(AppConstant.OMEPARSEPRIME3RDKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void set3rd(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEPRIME3RDKEY, value);
        } else {
            put(AppConstant.OMEPARSEPRIME3RDKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getVenuename() {
        if (getString(AppConstant.OMEPARSEPRIMEVENUENAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEPRIMEVENUENAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setVenuename(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEPRIMEVENUENAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEPRIMEVENUENAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getVenueId() {
        if (getString(AppConstant.OMEPARSEPRIMEVENUEIDKEY) != null) {
            return getString(AppConstant.OMEPARSEPRIMEVENUEIDKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setVenueId(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEPRIMEVENUEIDKEY, value);
        } else {
            put(AppConstant.OMEPARSEPRIMEVENUEIDKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getCardname() {
        if (getString(AppConstant.OMEPARSEPRIMECARDNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSEPRIMECARDNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setCardname(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEPRIMECARDNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEPRIMECARDNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getCardId() {
        if (getString(AppConstant.OMEPARSEPRIMECARDIDKEY) != null) {
            return getString(AppConstant.OMEPARSEPRIMECARDIDKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setCardId(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEPRIMECARDIDKEY, value);
        } else {
            put(AppConstant.OMEPARSEPRIMECARDIDKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getCardPrice() {
        if (getString(AppConstant.OMEPARSEPRIMECARDPRICEKEY) != null) {
            return getString(AppConstant.OMEPARSEPRIMECARDPRICEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setCardPrice(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEPRIMECARDPRICEKEY, value);
        } else {
            put(AppConstant.OMEPARSEPRIMECARDPRICEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getPayTime() {
        if (getString(AppConstant.OMEPARSEPRIMEPAYTIMEKEY) != null) {
            return getString(AppConstant.OMEPARSEPRIMEPAYTIMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setPayTime(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEPRIMEPAYTIMEKEY, value);
        } else {
            put(AppConstant.OMEPARSEPRIMEPAYTIMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getPayNumber() {
        if (getString(AppConstant.OMEPARSEPRIMEPAYNUMBERKEY) != null) {
            return getString(AppConstant.OMEPARSEPRIMEPAYNUMBERKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setPayNumber(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEPRIMEPAYNUMBERKEY, value);
        } else {
            put(AppConstant.OMEPARSEPRIMEPAYNUMBERKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public String getSMSCode() {
        if (getString(AppConstant.OMEPARSEPRIMESMSCODEKEY) != null) {
            return getString(AppConstant.OMEPARSEPRIMESMSCODEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setSMSCode(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEPRIMESMSCODEKEY, value);
        } else {
            put(AppConstant.OMEPARSEPRIMESMSCODEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }



    public String getPayCurrency() {
        if (getString(AppConstant.OMEPARSEPRIMEPAYCURRENCYKEY) != null) {
            return getString(AppConstant.OMEPARSEPRIMEPAYCURRENCYKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setPayCurrency(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEPRIMEPAYCURRENCYKEY, value);
        } else {
            put(AppConstant.OMEPARSEPRIMEPAYCURRENCYKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }


    public String getOther() {
        if (getString(AppConstant.OMEPARSEPRIMEOTHERKEY) != null) {
            return getString(AppConstant.OMEPARSEPRIMEOTHERKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void setOther(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSEPRIMEOTHERKEY, value);
        } else {
            put(AppConstant.OMEPARSEPRIMEOTHERKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public static ParseQuery<PayPrime> getQuery() {
        return ParseQuery.getQuery(PayPrime.class);
    }

}
