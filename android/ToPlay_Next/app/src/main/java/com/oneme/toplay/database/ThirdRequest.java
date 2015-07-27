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

import java.util.ArrayList;
import java.util.Arrays;

//
// define data model for third http request
//
@ParseClassName(AppConstant.OMETOPLAYTHIRDAPIREQUESTCLASSKEY)
public final class ThirdRequest extends ParseObject {

    private static final String TAG = "ThirdRequest";


    public String get3rdName() {
        if (getString(AppConstant.OMEPARSETHIRDNAMEKEY) != null) {
            return getString(AppConstant.OMEPARSETHIRDNAMEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    public void set3rdName(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSETHIRDNAMEKEY, value);
        } else {
            put(AppConstant.OMEPARSETHIRDNAMEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    // Service type include prime, map place, map suggestion, search suggestion etc
    // Please refer to the part Parse API key corresponding to third request service type in AppConstant.java
    public String getServiceType() {
        if (getString(AppConstant.OMEPARSETHIRDSERVICEKEY) != null) {
            return getString(AppConstant.OMEPARSETHIRDSERVICEKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    // Service type include prime, map place, map suggestion, search suggestion etc
    // Please refer to the part Parse API key corresponding to third request service type in AppConstant.java
    public void setSerivceType(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSETHIRDSERVICEKEY, value);
        } else {
            put(AppConstant.OMEPARSETHIRDSERVICEKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    // http request is json format string, the json format:
    // {"api base":"xxxxx","api mobile":"xxxxx",
    // "api cardid":"xxxxx","api cardnumber":"xxxxx","api paynumber":"xxxxx"}
    public String getHttp() {
        if (getString(AppConstant.OMEPARSETHIRDREQUESTHTTPKEY) != null) {
            return getString(AppConstant.OMEPARSETHIRDREQUESTHTTPKEY);
        } else {
            return AppConstant.OMEPARSENULLSTRING;
        }
    }

    // {"api base":"xxxxx","api mobile":"xxxxx",
    // "api cardid":"xxxxx","api cardnumber":"xxxxx","api paynumber":"xxxxx",
    public void setHttp(String value) {
        if (value != null) {
            put(AppConstant.OMEPARSETHIRDREQUESTHTTPKEY, value);
        } else {
            put(AppConstant.OMEPARSETHIRDREQUESTHTTPKEY, AppConstant.OMEPARSENULLSTRING);
        }
    }

    public static ParseQuery<ThirdRequest> getQuery() {
        return ParseQuery.getQuery(ThirdRequest.class);
    }

}