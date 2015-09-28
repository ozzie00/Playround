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

import com.oneme.toplay.database.Venue;

public class CopyVenue {

    public static void Done(Venue from, Venue to) {
        if (from != null && to != null) {
            to.setName(from.getName());
            to.setLevel(from.getLevel());
            to.setType(from.getType());
            to.setAddress(from.getAddress());
            // mvenue.setLocation(venue.getLocation());
            to.setPhone(from.getPhone());
            to.setCourtNumber(from.getCourtNumber());
            to.setLighted(from.getLighted());
            to.setIndoor(from.getIndoor());
            to.setPublic(from.getPublic());
            to.setObjectId(from.getObjectId());
            to.setBusiness(from.getBusiness());
            to.setPrice(from.getPrice());
            to.setPrimeInfo(from.getPrimeInfo());
       }
    }
}
