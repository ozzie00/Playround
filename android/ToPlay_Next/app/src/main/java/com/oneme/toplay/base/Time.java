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

package com.oneme.toplay.base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Timestamp;

public final class Time {

    public static final String currentTime() {
        DateFormat mdateFormat = new SimpleDateFormat("MMM dd yyyy HH:mm");
        Calendar mcalendar = Calendar.getInstance();

        String mgmtCurrentTime= mdateFormat.format(mcalendar.getTime());
        return mgmtCurrentTime;
    }

    public static final String currentDay() {
        DateFormat mdateFormat = new SimpleDateFormat("EEE, MMM dd");
        Calendar mcalendar = Calendar.getInstance();

        String mgmtCurrentTime= mdateFormat.format(mcalendar.getTime());
        return mgmtCurrentTime;
    }

    public static final String currentHour() {
        DateFormat mdateFormat = new SimpleDateFormat("HH:mm");
        Calendar mcalendar = Calendar.getInstance();

        String mgmtCurrentTime= mdateFormat.format(mcalendar.getTime());
        return mgmtCurrentTime;
    }

    public static final String tomorrowTime() {
        int onedaysecond        = 24*60*60;

        long today              = System.currentTimeMillis();
        Timestamp currentTime   = new Timestamp(today);

        Calendar mcalendar      = Calendar.getInstance();
        mcalendar.setTimeInMillis(currentTime.getTime());
        mcalendar.add(Calendar.SECOND, onedaysecond);

        DateFormat mdateFormat  = new SimpleDateFormat("MMM dd yyyy HH:mm");

        String mgmtTomorrowTime = mdateFormat.format(mcalendar.getTime().getTime());
        return mgmtTomorrowTime;
    }

}
