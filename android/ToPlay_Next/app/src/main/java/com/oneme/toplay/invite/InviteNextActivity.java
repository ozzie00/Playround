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

package com.oneme.toplay.invite;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;



import com.oneme.toplay.Application;
import com.oneme.toplay.LoginActivity;
import com.oneme.toplay.R;
import com.oneme.toplay.adapter.SportTypeAdapter;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.database.InviteScore;
import com.oneme.toplay.database.Group;
import com.oneme.toplay.database.Sport;
import com.oneme.toplay.voice.WitActivity;
import com.oneme.toplay.invite.SearchActivity;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

public final class InviteNextActivity extends ActionBarActivity implements OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG           = "InviteNextActivity";

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    private final Context context = InviteNextActivity.this;
    private LinearLayout mInviteLinerLayout;
    private Button minviteplayButton;

    private Button msporttypeButton;
    private Button mplayerlevelButton;
    private Button mplayernumberButton;
    private Button minviteplayfeeButton;
    private EditText mworkoutnameText;
    private EditText mdescriptionedit;
    private TextView mcourtText;
    private TextView mdateText;
    private TextView mtimeText;
    private TextView mdescriptionText;

    private ParseGeoPoint geoPoint;
    private ParseUser muser        = ParseUser.getCurrentUser();
    private String mworkoutname    = null;
    private String msporttype      = null;
    private String msporttypevalue = null;
    private String mplayerlevel    = null;
    private String mplayernumber   = null;
    private String minviteplaytime = null;
    private String mcourt          = null;
    private String minviteplayfee  = null;
    private String mother          = null;
    private String msubmittime     = null;

    private String mdate           = null;
    private String mhour           = null;

    public MenuItem minviteadd;


    // Add spinner for sport type
    String[] msportarray = {

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_invite_next);

        // Note: msportarray items are correponding to msporticonarray of Sport Class
        msportarray = getResources().getStringArray(R.array.sport_type_array);

        final Boolean isAvailable = false;// DispatchActivity.getGooglePlayServicesState();


        //get point according to  current latitude and longitude
        geoPoint = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

        if (isAvailable) {
            // show nearby place for user
            new getNearbyPlace().execute(AppConstant.OMEPARSENULLSTRING);
        } else {
            new getBdNearbyPlace().execute(AppConstant.OMEPARSENULLSTRING);
        }


        // set workout name
        mworkoutnameText = (EditText)findViewById(R.id.invite_content_text_view);
        mworkoutname     = mworkoutnameText.getText().toString();

        mworkoutnameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // minviteadd.setIcon(R.drawable.ome_invite_add);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mworkoutname = s.toString();
                    minviteadd.setIcon(R.drawable.ome_invite_add_pressed);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mworkoutname = s.toString();
                    minviteadd.setIcon(R.drawable.ome_invite_add_pressed);
                }
            }
        });

        // choose sport
        Spinner msportspinner              = (Spinner)findViewById(R.id.invite_sport_spinner);
        SportTypeAdapter msportTypeAdapter = new SportTypeAdapter(InviteNextActivity.this, R.layout.ome_sport_row, msportarray, Sport.msporticonarray);
        msportspinner.setAdapter(msportTypeAdapter);

        msportspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                msporttype      = msportarray[pos];
                msporttypevalue = Integer.toString(pos);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // set location
        RelativeLayout locationblock = (RelativeLayout)findViewById(R.id.invite_location_block);
        mcourtText                   = (TextView)findViewById(R.id.invite_location_address_view);

        if (muser != null) {
            if (muser.getString(AppConstant.OMEPARSEUSERHOMEVENUEKEY) != null) {
                mcourtText.setText(muser.getString(AppConstant.OMEPARSEUSERHOMEVENUEKEY));
                mcourt = muser.getString(AppConstant.OMEPARSEUSERHOMEVENUEKEY);
            } else if (muser.getString(AppConstant.OMEPARSEUSERBACKUPVENUEKEY) != null) {
                mcourtText.setText(muser.getString(AppConstant.OMEPARSEUSERBACKUPVENUEKEY));
                mcourt = muser.getString(AppConstant.OMEPARSEUSERBACKUPVENUEKEY);
            }
        }

        locationblock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent invokeSearchActivityIntent = new Intent(InviteNextActivity.this, SearchActivity.class);
                 startActivityForResult(invokeSearchActivityIntent, AppConstant.OMEPARSEINVITESEARCHLOCATIONRESULT);

            }
        });


        // set date and time block
        RelativeLayout startdate = (RelativeLayout)findViewById(R.id.invite_date_block);
        RelativeLayout starttime = (RelativeLayout)findViewById(R.id.invite_time_block);

        mdateText = (TextView)findViewById(R.id.invite_date_detail_text_view);
        mtimeText = (TextView)findViewById(R.id.invite_time_text_view);

        // set for date and time picker
        final Calendar calendar = Calendar.getInstance();

        mdateText.setText(Time.currentDay());
        mtimeText.setText(Time.currentHour());

        // reformat the play time, original format is MMM dd yyyy HH:mm
        mdate =  Integer.toString(calendar.get(Calendar.MONTH)) + AppConstant.OMEPARSESPACESTRING
                + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)) + AppConstant.OMEPARSESPACESTRING
                + Integer.toString(calendar.get(Calendar.YEAR));


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

        startdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(false);
                datePickerDialog.setYearRange(AppConstant.OMEPARSEINVITEYEARSTART, AppConstant.OMEPARSEINVITEYEAREND);
               // datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        starttime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.setVibrate(false);
               // timePickerDialog.setCloseOnSingleTapMinute(isCloseOnSingleTapMinute());
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });

        // set description block
        RelativeLayout mdescription = (RelativeLayout)findViewById(R.id.invite_description_block);
        mdescriptionText            = (TextView)findViewById(R.id.invite_descritpion_text_view);

        mdescription.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom descritpion dialog
                final Dialog descriptiondialog = new Dialog(context);
                descriptiondialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                descriptiondialog.setContentView(R.layout.ome_activity_invite_next_description_dialog);

                mdescriptionedit = (EditText)descriptiondialog.findViewById(R.id.invite_description_dialog_edittext);


                // set the custom dialog components - text, image and button
                TextView descriptiontitle = (TextView) descriptiondialog.findViewById(R.id.invite_description_dialog_title);
                descriptiontitle.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        descriptiondialog.dismiss();
                    }
                });

                TextView descriptiondone = (TextView) descriptiondialog.findViewById(R.id.invite_description_dialog_OK);
                // if TextView is clicked, close the custom dialog
                descriptiondone.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mother = mdescriptionedit.getText().toString();
                        descriptiondialog.dismiss();
                        mdescriptionText.setText(mother);
                    }
                });

                descriptiondialog.show();
            }
        });

    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        // change date text
        int mmonth = month;
        // in calendar JANUARY = 0, when show month, need plus 1
        mdateText.setText(Integer.toString(day) + AppConstant.OMEPARSESLASHSTRING + Integer.toString(mmonth + 1));
        mdate = Integer.toString(mmonth) +  AppConstant.OMEPARSESPACESTRING + Integer.toString(day) + AppConstant.OMEPARSESPACESTRING + Integer.toString(year);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ome_invite_next_menu, menu);

        minviteadd = menu.findItem(R.id.action_invite_add);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_invite_share:
                shareInvitation();
                return true;
            case R.id.action_invite_add:
                if (mworkoutname.length() > 0) {
                    submitInvitation();
                } else {
                    Toast.makeText(InviteNextActivity.this, getResources().getString(R.string.OMEPARSEINVITEFILLWORKOUTNAME), Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareInvitation() {

        // create share content
        String sharecontent = null;

        String mtime = mdate + mhour;

        if (mcourt != null) {
            sharecontent = getResources().getString(R.string.OMEPARSEWANTTOKEY)
                    + " " + getResources().getString(R.string.OMETOPLAYATWORD)
                    + " " + mcourt + " " + getResources().getString(R.string.OMEPARSEINVITETITLEPLAY)
                    + " " + msporttype;
        }

        if (mtime != null) {
            sharecontent = getResources().getString(R.string.OMEPARSEWANTTOKEY)
                    + " " + getResources().getString(R.string.OMEPARSEINVITETITLEPLAY)
                    + " " + msporttype + " " + getResources().getString(R.string.OMETOPLAYINWORD)
                    + " " + mtime;
        }

        if (mcourt != null && mtime != null) {
            sharecontent = getResources().getString(R.string.OMEPARSEWANTTOKEY)
                    + " " + getResources().getString(R.string.OMETOPLAYATWORD)
                    + " " + mcourt + " " + getResources().getString(R.string.OMEPARSEINVITETITLEPLAY)
                    + " " + msporttype + " " + getResources().getString(R.string.OMETOPLAYINWORD)
                    + " " + mtime;
        }

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharecontent);
        shareIntent.setType(AppConstant.OMEPARSESHARETEXTFILE);
        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.OMEPARSEADDCOTACTSHAREQRCODEWITH)));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.OMEPARSEINVITESEARCHLOCATIONRESULT && resultCode == Activity.RESULT_OK) {
            mcourtText.setText(data.getStringExtra(Application.INTENT_EXTRA_SEARCHLOCATION));
            mcourt = mcourtText.getText().toString();
        }
    }

    private void submitInvitation () {

        if (muser == null) {
            Toast.makeText(InviteNextActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT), Toast.LENGTH_SHORT).show();
            // jump to login activity
            return;
        }

        // Set up a progress dialog
        //   final ProgressDialog dialog = new ProgressDialog(InviteActivity.this);
        //   dialog.setMessage(getString(R.string.progress_invite));
        //   dialog.show();

        msubmittime = Time.currentTime();

        // set invite play time
        minviteplaytime = mdate + " " + mhour;

        // Set default value for court address
        if (mcourt == null) {
            mcourt = getResources().getString(R.string.OMEPARSEINVITECOURTDEFAULT);
        }

        //Toast.makeText(InviteActivity.this, "mcourt " + mcourt, Toast.LENGTH_LONG).show();


        // Create an invitation.
        final Invite invite = new Invite();

        // Set the location to the current user's location
        invite.setLocation(geoPoint);

        invite.setUser(muser);
        invite.setFromUser(muser);
        invite.setFromUsername(muser.getUsername());
        // Ozzie Zhang 2014-11-02 please modify this line code
        //invite.setText(ParseUser.getCurrentUser().getUsername() + " "+ getResources().getString(R.string.OMEPARSEWANTTOKEY)
        //        + " " + getResources().getString(R.string.OMEPARSEINVITETITLEPLAY) + " " + msporttype);

        // Ozzie Zhang 2015-02-19 use text key to workout name
        if (mworkoutname != null) {
            invite.setWorkoutName(mworkoutname);
        }

        if (msporttype != null) {
            invite.setSportType(msporttype);
        }

        if (msporttypevalue != null) {
            invite.setSportTypeValue(msporttypevalue);
        }

        if (minviteplaytime != null) {
            invite.setPlayTime(minviteplaytime);
        }

       // invite.setPlayerLevel(mplayerlevel);
       // invite.setPlayerNumber(mplayernumber);

        if (mcourt != null) {
            invite.setCourt(mcourt);
        }
       // invite.setFee(minviteplayfee);
       // invite.setOther(mother);
        if (msubmittime != null) {
            invite.setSubmitTime(msubmittime);
        }

        if (mother != null) {
            invite.setOther(mother);
        }

        ParseACL acl = new ParseACL();

        // Give public read access
        acl.setPublicReadAccess(true);
        invite.setACL(acl);

        // Save the post
        invite.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // create join group for this invite
                    String musername = muser.getUsername();
                    Group group      = new Group();
                    group.setGroupAdmin(muser);
                    group.setGroupAdminUsername(musername);
                    group.setParentObjectId(invite.getObjectId());
                    group.setGroupPulic(true);
                    group.setMemberUser(muser);
                    group.setMemberUsername(musername);

                    if (mworkoutname != null) {
                        group.setGroupWorkout(mworkoutname);
                    }

                    if (msporttype != null) {
                        group.setGroupSport(msporttype);
                    }

                    if (msporttypevalue != null) {
                        group.setSportTypeValue(msporttypevalue);
                    }

                    if (msubmittime != null) {
                        group.setMemberJoinTime(msubmittime);
                    }

                    ParseACL gacl = new ParseACL();
                    gacl.setPublicReadAccess(true);
                    gacl.setWriteAccess(muser,true);
                    group.saveInBackground();

                    // create score record for this invite
                    InviteScore score = new InviteScore();

                    score.setAuthor(muser);
                    score.setAuthorUsername(muser.getUsername());
                    score.setParentObjectId(invite.getObjectId());
                    score.setContent(Integer.toString(0));
                    score.setRate(0);

                    if (msporttype != null) {
                        score.setSport(msporttype);
                    }

                    if (msporttypevalue != null) {
                        score.setSportValue(msporttypevalue);
                    }

                    ParseACL macl = new ParseACL();
                    macl.setPublicReadAccess(true);
                    macl.setWriteAccess(muser,true);
                    score.saveInBackground();
                }
                //      dialog.dismiss();
                finish();
            }
        });

        return;

    }


    class getNearbyPlace extends AsyncTask<String,String,String> {

        ArrayList<String> resultList    = null;

        @Override
        protected String doInBackground(String... key) {
           // ArrayList<String> resultList    = null;
            HttpURLConnection mconnection   = null;
            StringBuilder jsonResults       = new StringBuilder();
            String mlocale                  = Locale.getDefault().getLanguage();

            try {
                StringBuilder mstringBuilder = new StringBuilder(AppConstant.PLACE_API_BASE + AppConstant.PLACE_TYPE_NEARBY + AppConstant.PLACE_OUT_JSON);
                mstringBuilder.append(AppConstant.PLACE_KEY + AppConstant.PLACE_API_KEY);
                mstringBuilder.append(AppConstant.PLACE_LOCATION + geoPoint.getLatitude()+ AppConstant.OMEPARSECOMMASTRING +geoPoint.getLongitude());
                mstringBuilder.append(AppConstant.PLACE_RADIUS + AppConstant.OME_RADIUS);
                mstringBuilder.append(AppConstant.PLACE_TYPE_KEY + AppConstant.PLACE_TYPES);
                mstringBuilder.append(AppConstant.PLACE_LANGUAGE + mlocale);


                URL murl    = new URL(mstringBuilder.toString());
                mconnection = (HttpURLConnection) murl.openConnection();
                InputStreamReader in = new InputStreamReader(mconnection.getInputStream());

                // Load the results into a StringBuilder
                int read;
                char[] buff = new char[AppConstant.OMEPARSEBUFFERLENGTH];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }


            } catch (MalformedURLException e) {
                Log.e(TAG, "Error processing Places API URL", e);

            } catch (IOException e) {
                Log.e(TAG, "Error connecting to Places API", e);

            } finally {
                if (mconnection != null) {
                    mconnection.disconnect();
                }
            }

            try {
                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray(AppConstant.PLACE_RESPONSE_RESULTS);

                // Extract the Place descriptions from the results
                if (predsJsonArray != null) {
                    resultList = new ArrayList<String>(predsJsonArray.length());
                }

                for (int i = 0; i < predsJsonArray.length(); i++) {
                    resultList.add(predsJsonArray.getJSONObject(i).getString(AppConstant.PLACE_RESULTS_NAME));
                }

            } catch (JSONException e) {

            }

            // checkt result list, because offline result list will be null
            if (resultList != null && resultList.size() > 0) {
                return resultList.get(0);
            } else {
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                mcourtText.setText(result);
                mcourt = result;
            }
        }

    }

    class getBdNearbyPlace extends AsyncTask<String,String,String> {

        ArrayList<String> resultList    = null;

        @Override
        protected String doInBackground(String... key) {
            // ArrayList<String> resultList    = null;
            HttpURLConnection mbdconnection = null;
            StringBuilder jsonResults       = new StringBuilder();
            String mlocale                  = Locale.getDefault().getLanguage();

            try {
                StringBuilder mbdstringBuilder = new StringBuilder(AppConstant.BD_PLACES_API_SEARCH);
                mbdstringBuilder.append(AppConstant.BD_PLACE_QUERY + AppConstant.BD_PLACE_NEARBY_SEARCH );
                mbdstringBuilder.append(AppConstant.BD_PLACE_LOCATION + geoPoint.getLatitude()+ AppConstant.OMEPARSECOMMASTRING +geoPoint.getLongitude());
                mbdstringBuilder.append(AppConstant.BD_PLACE_RADIUS + AppConstant.OME_RADIUS);
                mbdstringBuilder.append(AppConstant.BD_PLACE_OUT_JSON);
                mbdstringBuilder.append(AppConstant.BD_PLACE_KEY);
                mbdstringBuilder.append(AppConstant.BD_PLACE_API_KEY);

                URL mbdurl             = new URL(mbdstringBuilder.toString());
                mbdconnection          = (HttpURLConnection) mbdurl.openConnection();
                InputStreamReader inbd = new InputStreamReader(mbdconnection.getInputStream());

                // Load the results into a StringBuilder
                int read;
                char[] buff = new char[AppConstant.OMEPARSEBUFFERLENGTH];
                while ((read = inbd.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
            } catch (MalformedURLException me) {

            } catch (IOException ie) {

            } finally {
                if (mbdconnection != null) {
                    mbdconnection.disconnect();
                }
            }

            try {
                // Create a JSON object hierarchy from the results
                JSONObject jsonObj  = new JSONObject(jsonResults.toString());
                String isSuccessful = jsonObj.getString(AppConstant.BD_PLACE_STATUS);

                if (isSuccessful.equals(AppConstant.OMEPARSEZEROSTRING)) {
                    JSONArray predsJsonArray = jsonObj.getJSONArray(AppConstant.BD_PLACE_RESULTS);

                    // Extract the Place descriptions from the results
                    if (predsJsonArray != null) {
                        resultList = new ArrayList<String>(predsJsonArray.length());
                    }

                    for (int i = 0; i < predsJsonArray.length(); i++) {
                        resultList.add(predsJsonArray.getJSONObject(i).getString(AppConstant.BD_PLACE_NAME));
                    }
                }

            } catch (JSONException je) {

            }

            if (resultList != null && resultList.size() > 0) {
                return resultList.get(0);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                mcourtText.setText(result);
                mcourt = result;
            }
        }

    }




}
