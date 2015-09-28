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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class Time {

    // time format is "MMM dd yyyy HH:mm" in playround
    public static final String currentTime() {
        DateFormat mdateFormat = new SimpleDateFormat("MMM dd yyyy HH:mm");
        Calendar mcalendar     = Calendar.getInstance();

        String mgmtCurrentTime = mdateFormat.format(mcalendar.getTime());
        return mgmtCurrentTime;
    }

    public static final String currentDay() {
        DateFormat mdateFormat = new SimpleDateFormat("EEE, MMM dd");
        Calendar mcalendar     = Calendar.getInstance();

        String mgmtCurrentTime = mdateFormat.format(mcalendar.getTime());
        return mgmtCurrentTime;
    }

    public static final String currentHour() {
        DateFormat mdateFormat = new SimpleDateFormat("HH:mm");
        Calendar mcalendar     = Calendar.getInstance();

        String mgmtCurrentTime = mdateFormat.format(mcalendar.getTime());
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

    public static final Date stringToTime(String mtime) {

        SimpleDateFormat  mformat = new SimpleDateFormat("MMM dd yyy HH:mm");
        try {
            Date mdate = mformat.parse(mtime);
            return  mdate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static String setFormattedTimeString(final String formatExpr,
                                                     final long timeStampInSeconds) {
        final Date dateFromTimeStamp        = new Date(timeStampInSeconds);
        final SimpleDateFormat simpleformat = new SimpleDateFormat(formatExpr);
        final String formattedDateInString  = simpleformat.format(dateFromTimeStamp);
        return formattedDateInString;
    }

    public static Calendar setCalendar(final int year, final int month,
                                            final int day, final int hour, final int minute) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar;
    }

    public static double timeDifferenceInDays(final Calendar firstDate,
                                                   final Calendar secondDate) {
        final long firstDateMilli  = firstDate.getTimeInMillis();
        final long secondDateMilli = secondDate.getTimeInMillis();
        final long diff            = firstDateMilli - secondDateMilli;

        // 24 * 60 * 60 * 1000 in days. can Change to hour
        final double diffDays = (double) diff / (double) (24 * 60 * 60 * 1000);

        return diffDays;
    }

    public static double timeDifferenceInHours(final Calendar firstDate,
                                              final Calendar secondDate) {
        final long firstDateMilli  = firstDate.getTimeInMillis();
        final long secondDateMilli = secondDate.getTimeInMillis();
        final long diff            = firstDateMilli - secondDateMilli;

        // 24 * 60 * 60 * 1000 in days. can Change to hour
        final double diffHours = (double) diff / (double) (60 * 60 * 1000);

        return diffHours;
    }

    public static double timeDifferenceInMinutes(final Calendar firstDate,
                                               final Calendar secondDate) {
        final long firstDateMilli  = firstDate.getTimeInMillis();
        final long secondDateMilli = secondDate.getTimeInMillis();
        final long diff            = firstDateMilli - secondDateMilli;

        // 24 * 60 * 60 * 1000 in days. can Change to minute
        final double diffMinutes = (double) diff / (double) (60 * 1000);

        return diffMinutes;
    }

    public static double timeDifferenceInSecond(final Calendar firstDate,
                                                 final Calendar secondDate) {
        final long firstDateMilli  = firstDate.getTimeInMillis();
        final long secondDateMilli = secondDate.getTimeInMillis();
        final long diff            = firstDateMilli - secondDateMilli;

        // 24 * 60 * 60 * 1000 in days. can Change to second
        final double diffSeconds = (double) diff / (double) (1000);

        return diffSeconds;
    }


}
