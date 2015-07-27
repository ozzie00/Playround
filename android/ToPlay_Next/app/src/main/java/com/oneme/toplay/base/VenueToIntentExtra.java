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

import com.oneme.toplay.Application;
import com.oneme.toplay.database.Venue;

public class VenueToIntentExtra {

    public static void putExtra(Intent intent, Venue venue) {
        intent.putExtra(Application.INTENT_EXTRA_VENUE, venue.getName());
        intent.putExtra(Application.INTENT_EXTRA_VENUELEVEL, venue.getLevel());
        intent.putExtra(Application.INTENT_EXTRA_VENUETYPE, venue.getType());
        intent.putExtra(Application.INTENT_EXTRA_VENUEADDRESS, venue.getAddress());
        intent.putExtra(Application.INTENT_EXTRA_VENUEPHONE, venue.getPhone());
        intent.putExtra(Application.INTENT_EXTRA_VENUECOURTNUMBER, venue.getCourtNumber());
        intent.putExtra(Application.INTENT_EXTRA_VENUELIGHTED, venue.getLighted());
        intent.putExtra(Application.INTENT_EXTRA_VENUEINDOOR, venue.getIndoor());
        intent.putExtra(Application.INTENT_EXTRA_VENUEPUBLIC, venue.getPublic());
        intent.putExtra(Application.INTENT_EXTRA_INVITEOBJECTID, venue.getObjectId());
        intent.putExtra(Application.INTENT_EXTRA_VENUEBUSINESS, venue.getBusiness());
        intent.putExtra(Application.INTENT_EXTRA_VENUEPRICE, venue.getPrice());
        intent.putExtra(Application.INTENT_EXTRA_VENUEPRIMEINFO, venue.getPrimeInfo());
    }
}
