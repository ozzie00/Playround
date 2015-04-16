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

package com.oneme.toplay.base;

import android.content.Intent;
import android.os.Bundle;

import com.oneme.toplay.Application;
import com.oneme.toplay.database.Venue;

public class IntentExtraToVenue {

    public static void getExtra(Intent intent, Venue venue) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            venue.setName(extras.getString(Application.INTENT_EXTRA_VENUE));
            venue.setLevel(extras.getString(Application.INTENT_EXTRA_VENUELEVEL));
            venue.setType(extras.getString(Application.INTENT_EXTRA_VENUETYPE));
            venue.setAddress(extras.getString(Application.INTENT_EXTRA_VENUEADDRESS));
            venue.setPhone(extras.getString(Application.INTENT_EXTRA_VENUEPHONE));
            venue.setCourtNumber(extras.getString(Application.INTENT_EXTRA_VENUECOURTNUMBER));
            venue.setLighted(extras.getString(Application.INTENT_EXTRA_VENUELIGHTED));
            venue.setIndoor(extras.getString(Application.INTENT_EXTRA_VENUEINDOOR));
            venue.setPublic(extras.getString(Application.INTENT_EXTRA_VENUEPUBLIC));
            venue.setObjectId(extras.getString(Application.INTENT_EXTRA_INVITEOBJECTID));
        }
    }
}
