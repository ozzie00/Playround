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

package com.oneme.toplay.venue;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.IntentExtraToVenue;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.database.BookingVenue;
import com.oneme.toplay.database.Venue;
import com.oneme.toplay.pay.PayBookingVenueActivity;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;


public class BookingActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {

    private Venue mvenue = null;

    private String minviteObjectID = null;
    private ParseUser muser        = ParseUser.getCurrentUser();
    private String musername       = null;
    private String currencysymbol  = null;

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    private final Context context = BookingActivity.this;

    private TextView mdateText;
    private TextView mtimeText;
    private TextView mremarkText;
    private EditText mremarkedit;

    private String mdate           = null;
    private String mhour           = null;
    private String mremark         = null;
    private String mtime           = null;
    private String mvenuename      = null;
    private String mpaynumber      = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_venue_booking);

       // getActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Locale currentlocale = getResources().getConfiguration().locale;

        currencysymbol       = Currency.getInstance(currentlocale).getSymbol();

        if (muser != null) {
            musername = muser.getUsername();
        }

        // get ready to extra data
        Intent detailIntent = getIntent();
        mvenue = new Venue();

        if (mvenue != null && detailIntent != null) {
            IntentExtraToVenue.getExtra(detailIntent, mvenue);
        }

        // set date and time block
        RelativeLayout startdate = (RelativeLayout)findViewById(R.id.venue_booking_date_block);
        RelativeLayout starttime = (RelativeLayout)findViewById(R.id.venue_booking_time_block);

        mdateText = (TextView)findViewById(R.id.venue_booking_date_detail_text_view);
        mtimeText = (TextView)findViewById(R.id.venue_booking_time_text_view);

        // set for date and time picker
        final Calendar calendar = Calendar.getInstance();

        mdateText.setText(Time.currentDay());
        mtimeText.setText(Time.currentHour());

        // reformat the play time, original format is YYYY-MM-DD HH:mm, add zero string according to if beyond ten
        if (calendar.get(Calendar.MONTH) + 1 < AppConstant.OMEPARSEINVITETIMETWOBITDIVIDER) {
            mdate = Integer.toString(calendar.get(Calendar.YEAR)) + AppConstant.OMEPARSEHYPHENSTRING + AppConstant.OMEPARSEZEROSTRING
                    + Integer.toString(calendar.get(Calendar.MONTH) + 1) + AppConstant.OMEPARSEHYPHENSTRING
                    + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        } else {
            mdate = Integer.toString(calendar.get(Calendar.YEAR)) + AppConstant.OMEPARSEHYPHENSTRING
                    + Integer.toString(calendar.get(Calendar.MONTH) + 1) + AppConstant.OMEPARSEHYPHENSTRING
                    + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

        }

        // add zero in minute or hour, for example change 21:5 to 21:05
        if (calendar.get(Calendar.MINUTE) < AppConstant.OMEPARSEINVITETIMETWOBITDIVIDER) {

            mhour = calendar.get(Calendar.HOUR_OF_DAY) + AppConstant.OMEPARSECOLONZEROSTRING + calendar.get(Calendar.MINUTE);
        } else {
            mhour = calendar.get(Calendar.HOUR_OF_DAY) + AppConstant.OMEPARSECOLONSTRING + calendar.get(Calendar.MINUTE);
        }

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                false);

        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false, false);

        startdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(false);
                datePickerDialog.setYearRange(AppConstant.OMEPARSEINVITEYEARSTART, AppConstant.OMEPARSEINVITEYEAREND);
                // datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.setVibrate(false);
                // timePickerDialog.setCloseOnSingleTapMinute(isCloseOnSingleTapMinute());
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });

        // set remark block
        RelativeLayout mremarkrelative = (RelativeLayout)findViewById(R.id.venue_booking_remark_block);
        mremarkText                 = (TextView)findViewById(R.id.venue_booking_remark_text_view);

        mremarkrelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom descritpion dialog
                final Dialog descriptiondialog = new Dialog(context);
                descriptiondialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                descriptiondialog.setContentView(R.layout.ome_activity_booking_venue_remark_dialog);

                mremarkedit = (EditText)descriptiondialog.findViewById(R.id.booking_venue_remark_dialog_edittext);


                // set the custom dialog components - text, image and button
                TextView descriptiontitle = (TextView) descriptiondialog.findViewById(R.id.booking_venue_remark_dialog_title);
                descriptiontitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        descriptiondialog.dismiss();
                    }
                });

                TextView descriptiondone = (TextView) descriptiondialog.findViewById(R.id.booking_venue_remark_dialog_OK);
                // if TextView is clicked, close the custom dialog
                descriptiondone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mremark = mremarkedit.getText().toString();
                        descriptiondialog.dismiss();
                        mremarkText.setText(mremark);
                    }
                });

                descriptiondialog.show();
            }
        });

        // set fee
        TextView currency  = (TextView) findViewById(R.id.venue_booking_currency);
        currency.setText(currencysymbol);
        currency.setTextColor(getResources().getColor(R.color.playround_default));

        final TextView mfee = (TextView)findViewById(R.id.venue_booking_fee_text);
        if (muser != null) {
            mfee.setVisibility(View.VISIBLE);
            mfee.setTextColor(getResources().getColor(R.color.playround_default));
        }

        // set confirm button
        Button confirm = (Button) findViewById(R.id.venue_booking_confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtime      = mdate + AppConstant.OMEPARSESPACESTRING + mhour;
                mvenuename = mvenue.getName();
                Intent invokePayActivityIntent = new Intent(BookingActivity.this, PayBookingVenueActivity.class);
                invokePayActivityIntent.putExtra(Application.INTENT_EXTRA_TIME, mtime);
                invokePayActivityIntent.putExtra(Application.INTENT_EXTRA_VENUE, mvenuename);
                startActivityForResult(invokePayActivityIntent, AppConstant.OMEPARSEBOOKINGVENUEPAYRESULT);
            }
        });

    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        // change date text
        int mmonth = month;
        // in calendar JANUARY = 0, when show month, need plus 1
        if (mmonth + 1 < AppConstant.OMEPARSEINVITETIMETWOBITDIVIDER) {
            mdateText.setText(Integer.toString(year) + AppConstant.OMEPARSEHYPHENSTRING + AppConstant.OMEPARSEZEROSTRING + Integer.toString(mmonth + 1) + AppConstant.OMEPARSEHYPHENSTRING + Integer.toString(day));
            mdate = Integer.toString(year) + AppConstant.OMEPARSEHYPHENSTRING + AppConstant.OMEPARSEZEROSTRING + Integer.toString(mmonth + 1) + AppConstant.OMEPARSEHYPHENSTRING + Integer.toString(day);
        } else {
            mdateText.setText(Integer.toString(year) + AppConstant.OMEPARSEHYPHENSTRING + Integer.toString(mmonth + 1) + AppConstant.OMEPARSEHYPHENSTRING + Integer.toString(day));
            mdate = Integer.toString(year) + AppConstant.OMEPARSEHYPHENSTRING + Integer.toString(mmonth + 1) + AppConstant.OMEPARSEHYPHENSTRING + Integer.toString(day);
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        // change hour  text
        mtimeText.setText(hourOfDay + AppConstant.OMEPARSECOLONSTRING + minute);
        if (minute < AppConstant.OMEPARSEINVITETIMETWOBITDIVIDER) {
            mtimeText.setText(Integer.toString(hourOfDay) + AppConstant.OMEPARSECOLONZEROSTRING + Integer.toString(minute));
            mhour = Integer.toString(hourOfDay) + AppConstant.OMEPARSECOLONZEROSTRING + Integer.toString(minute);
        } else {
            mtimeText.setText(Integer.toString(hourOfDay) + AppConstant.OMEPARSECOLONSTRING + Integer.toString(minute));
            mhour = Integer.toString(hourOfDay) + AppConstant.OMEPARSECOLONSTRING + Integer.toString(minute);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.OMEPARSEBOOKINGVENUEPAYRESULT && resultCode == ActionBarActivity.RESULT_OK) {
            Toast.makeText(BookingActivity.this, getResources().getString(R.string.OMEPARSEBOOKINGRESULTNOTIFICATION),
                    Toast.LENGTH_LONG).show();
            mpaynumber  = data.getStringExtra(Application.INTENT_EXTRA_VENUEPAYNO);

            savebookinginfo();
        }

    }

    private void savebookinginfo() {
        if (muser != null) {

            BookingVenue morder = new BookingVenue();
            morder.setAuthor(muser);
            morder.setAuthorUsername(muser.getUsername());
            if (mvenue != null) {
                morder.setVenueObjectId(mvenue.getObjectId());
                morder.setVenueName(mvenue.getName());
            }
            mtime = mdate + AppConstant.OMEPARSESPACESTRING + mhour;
            morder.setTime(mtime);
            morder.setSubmitTime(Time.currentTime());
            morder.setPayStatus(AppConstant.OMEPARSEBOOKINGPAYSUCCESS);
            morder.setPayNumber(mpaynumber);
            morder.setRefundStatus(AppConstant.OMEPARSEBOOKINGREFUNDNOTSTART);
            morder.setFinishStatus(AppConstant.OMEPARSEBOOKINGFINISHNOTSTART);

            ParseACL acl = new ParseACL();

            // Give public read access
            acl.setPublicReadAccess(false);
            acl.setWriteAccess(muser, true);
            morder.setACL(acl);

            morder.saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        finish();
                    }
                }
            });
        } else {
            Toast.makeText(BookingActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT), Toast.LENGTH_LONG).show();
        }
    }

}
