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

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.adapter.SportTypeAdapter;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.database.Sport;
import com.oneme.toplay.database.Invite;

import com.oneme.toplay.LoginActivity;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.AutoCompleteTextView;
import android.graphics.Color;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.Gravity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Locale;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public final class InviteActivity extends ActionBarActivity implements TextWatcher, OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private static final String TAG           = "InviteActivity";

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    private final Context context = InviteActivity.this;
    private LinearLayout mInviteLinerLayout;
    private Button minviteplayButton;

    private Button msporttypeButton;
    private Button mplayerlevelButton;
    private Button mplayernumberButton;
    private Button minviteplayfeeButton;
    private EditText motherText;
    private Button mdateButton;
    private Button mtimeButton;

    private ParseGeoPoint geoPoint;

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


    private static final String LOG_TAG           = "InviteActivity";

    private static final String PLACES_API_BASE   = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_NEARBY       = "/nearbysearch";


    private static final String OUT_JSON          = "/json";

    private static final String PLACE_API_KEY     = AppConstant.OMETOPLAYGOOGLEPLACEKEY;

    AutoCompleteTextView mcourtAutoComplete;


    // Add spinner for sport type
    String[] msportarray = {

    };

    private int msporticon[] = {
            R.drawable.ome_badminton_icon,
            R.drawable.ome_basketball_icon,
            R.drawable.ome_snooker_icon,
            R.drawable.ome_table_soccer_icon,
            R.drawable.ome_table_tennis_icon,
            R.drawable.ome_tennis_icon,
            R.drawable.ome_trekking_icon,
            R.drawable.ome_running_icon,
            R.drawable.ome_cycling_icon,
            R.drawable.ome_other_icon
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_invite);

        // Note: msportarray items are correponding to msporticonarray of Sport Class
        msportarray = getResources().getStringArray(R.array.sport_type_array);

        //get point according to  current latitude and longitude
        geoPoint = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

        // add LinearLayout
        mInviteLinerLayout =(LinearLayout) findViewById(R.id.invite_linerlayout);

        // add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mInviteLinerLayout.setOrientation(LinearLayout.VERTICAL);

        //add date on button
        mdateButton = (Button) findViewById(R.id.dateButton);
        mdateButton.setText(getResources().getString(R.string.OMEPARSEINVITEPLAYDATE));

        // add hour on button
        mtimeButton = (Button) findViewById(R.id.timeButton);
        mtimeButton.setText(getResources().getString(R.string.OMEPARSEINVITEPLAYHOUR));

        // set for date and time picker
        final Calendar calendar = Calendar.getInstance();

        mdate =  calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH);

        // add zero in minute or hour, for example change 21:5 to 21:05
        if (calendar.get(Calendar.MINUTE) < AppConstant.OMEPARSEINVITETIMETWOBITDIVIDER) {

            mhour = calendar.get(Calendar.HOUR_OF_DAY) + ":0" + calendar.get(Calendar.MINUTE);
        } else {
            mhour = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        }

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                isVibrate());

        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false, false);

        findViewById(R.id.dateButton).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(AppConstant.OMEPARSEINVITEYEARSTART, AppConstant.OMEPARSEINVITEYEAREND);
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        findViewById(R.id.timeButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.setVibrate(isVibrate());
                timePickerDialog.setCloseOnSingleTapMinute(isCloseOnSingleTapMinute());
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });

        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }

            TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeSetListener(this);
            }
        }

        // invite court
        mcourtAutoComplete = (AutoCompleteTextView)findViewById(R.id.invitecourtautocomplete);

        mcourtAutoComplete.setHint(getResources().getString(R.string.OMEPARSEINVITEPLAYCOURTPLACEHOLD));
        mcourtAutoComplete.addTextChangedListener(this);
       // mcourtAutoComplete.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item));

        mcourtAutoComplete.setAdapter(new PlacesNearByAdapter(this, R.layout.ome_activity_invite_court_list_item));

        mcourtAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String clickedCourt = (String) adapterView.getItemAtPosition(position);
                mcourt = mcourtAutoComplete.getEditableText().toString();
            }
        });

        mcourtAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mcourt = s.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                mcourt = s.toString();
            }
        });

        Spinner msportspinner = (Spinner)findViewById(R.id.sport_spinner);
        SportTypeAdapter msportTypeAdapter = new SportTypeAdapter(InviteActivity.this, R.layout.ome_sport_row, msportarray, Sport.msporticonarray);
        msportspinner.setAdapter(msportTypeAdapter);

        msportspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                msporttype      = msportarray[pos];
                msporttypevalue = Integer.toString(pos);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // add sport type button
        //addSportTypeButtonPopup();

        // add player level button
        addPlayerLevelButtonPopup();

        // add player number button
        addPlayerNumberButtonPopup();

        // add fee button
        addInvitePlayFeeButtonPopup();

        // add invite play button
        addInvitePlayButton();

        // add invite other
        addInviteOtherTextField();

        //Ozzie Zhang 2014-11-16 add search
        //handleIntent(getIntent());

    }

    private boolean isVibrate() {
        //return ((CheckBox) findViewById(R.id.checkBoxVibrate)).isChecked();
        return false;
    }

    private boolean isCloseOnSingleTapDay() {
        //  return ((CheckBox) findViewById(R.id.checkBoxCloseOnSingleTapDay)).isChecked();
        return false;
    }

    private boolean isCloseOnSingleTapMinute() {
        // return ((CheckBox) findViewById(R.id.checkBoxCloseOnSingleTapMinute)).isChecked();
        return false;
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
      //  Toast.makeText(InviteActivity.this, "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
        // change date button text
        int mmonth = month + 1;
        mdateButton.setText(day + "/" + mmonth);
        mdate = day + "/" + mmonth;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
       // Toast.makeText(InviteActivity.this, "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_LONG).show();

        // change hour button text
        mtimeButton.setText(hourOfDay + ":" + minute);
        if (minute < AppConstant.OMEPARSEINVITETIMETWOBITDIVIDER) {
            mtimeButton.setText(hourOfDay + ":0" + minute);
            mhour = hourOfDay + ":0" + minute;
        } else {
            mtimeButton.setText(hourOfDay + ":" + minute);
            mhour = hourOfDay + ":" + minute;
        }

    }


    // define player level button popup menu
    private void addPlayerLevelButtonPopup() {

        mplayerlevelButton = (Button) findViewById(R.id.playerlevelButton);
        mplayerlevelButton.setText(getResources().getString(R.string.OMEPARSEINVITEPLAYERLEVELCHOOSE));

        // Set default value for player level
        mplayerlevel = AppConstant.OMEPARSEINVITEPLAYERLEVELDEFAULT;

        mplayerlevelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Creating the instance of PopupMenu
                PopupMenu playerlevelpopup = new PopupMenu(InviteActivity.this, mplayerlevelButton);
                // Inflating the Popup using xml file
                playerlevelpopup.getMenuInflater().inflate(R.menu.ome_playerlevel_popup, playerlevelpopup.getMenu());


                //registering popup with OnMenuItemClickListener
                playerlevelpopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //  Toast.makeText(InviteActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();

                        mplayerlevelButton.setText(item.getTitle());
                        mplayerlevel = item.getTitle().toString();
                        return true;
                    }
                });

                playerlevelpopup.show();//showing popup menu
            }
        });//closing the setOnClickListener method
    }

    // define player number button popup menu
    private void addPlayerNumberButtonPopup() {
        mplayernumberButton = (Button) findViewById(R.id.playernumberButton);
        mplayernumberButton.setText(getResources().getString(R.string.OMEPARSEINVITEPLAYERNUMBERCHOOSE));

        // Set default value for player number
        mplayernumber = AppConstant.OMEPARSEINVITEPLAYERNUMBERDEFAULT;

        mplayernumberButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu playernumberpopup = new PopupMenu(InviteActivity.this, mplayernumberButton);
                //Inflating the Popup using xml file
                playernumberpopup.getMenuInflater().inflate(R.menu.ome_playernumber_popup, playernumberpopup.getMenu());

                //registering popup with OnMenuItemClickListener
                playernumberpopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //  Toast.makeText(InviteActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();

                        mplayernumberButton.setText(item.getTitle());
                        mplayernumber = item.getTitle().toString();

                        return true;
                    }
                });

                playernumberpopup.show();//showing popup menu
            }
        });

    }

    // define invite play fee button popup menus
    private void addInvitePlayFeeButtonPopup() {
        minviteplayfeeButton = (Button) findViewById(R.id.invitefeeButton);
        minviteplayfeeButton.setText(getResources().getString(R.string.OMEPARSEINVITEPLAYFEECHOOSE));

        // Set default value for fee
        minviteplayfee = getResources().getString(R.string.OMEPARSEINVITEFEEDEFAULT);

        minviteplayfeeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu feepopup = new PopupMenu(InviteActivity.this, minviteplayfeeButton);

                // add fee type item to popup menu
                feepopup.getMenu().add(AppConstant.OMEPARSEINVITEPLAYFEEGROUPID,
                        AppConstant.OMEPARSEINVITEFEEDEFAULTID,
                        0,
                        getResources().getString(R.string.OMEPARSEINVITEFEEDEFAULT));

                feepopup.getMenu().add(AppConstant.OMEPARSEINVITEPLAYFEEGROUPID,
                        AppConstant.OMEPARSEINVITEFEEFREEID,
                        0,
                        getResources().getString(R.string.OMEPARSEINVITEFEEFREE));

                //Inflating the Popup using xml file
                //feepopup.getMenuInflater().inflate(R.menu.inviteplayfee_popup, feepopup.getMenu());

                //registering popup with OnMenuItemClickListener
                feepopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        // Toast.makeText(InviteActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();

                        minviteplayfeeButton.setText(item.getTitle());
                        minviteplayfee = item.getTitle().toString();

                        return true;
                    }
                });

                feepopup.show();//showing popup menu
            }
        });
    }


    // define invite court text field
    private void addInviteOtherTextField() {
        motherText = (EditText) findViewById(R.id.inviteotherText);

        motherText.setText(getResources().getString(R.string.OMEPARSEINVITEPLAYOTHERPLACEHOLD));

        // Set default value for other info
        mother = getResources().getString(R.string.OMEPARSEINVITEOTHERDEFAULT);

        mother = motherText.getText().toString().trim();

    }

    //define invite play button
    private void addInvitePlayButton() {

        // set button position and align
        LinearLayout.LayoutParams inviteplayButtonParam = new LinearLayout.LayoutParams(
                900,
                120);

        inviteplayButtonParam.setMargins(8, 60, 8, 8);
        inviteplayButtonParam.gravity = Gravity.CENTER;

        // add "Invite Play" Button
        minviteplayButton = new Button(this);
        minviteplayButton.setBackgroundColor(AppConstant.OMETOPLAYDEFAULTCOLOR);
        minviteplayButton.setTextColor(Color.WHITE);
        minviteplayButton.setLayoutParams(inviteplayButtonParam);
        minviteplayButton.setText(getResources().getString(R.string.OMEPARSEINVITEPLAYBUTTON));
       // minviteplayButton.setId(5);


        // add invite play button listener
        minviteplayButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder minviteplayAlertDialogBuilder = new AlertDialog.Builder(context);

                minviteplayButton.setBackgroundColor(Color.WHITE);
                minviteplayButton.setTextColor(AppConstant.OMETOPLAYDEFAULTCOLOR);

                // set title
                // alertDialogBuilder.setTitle("Your Title");

                // set dialog message
                minviteplayAlertDialogBuilder
                        .setMessage(getResources().getString(R.string.OMEPARSEINVITEREALLYPLAY))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.OMEPARSEYES), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (ParseUser.getCurrentUser() == null) {

                                    //Toast.makeText(InviteActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT),
                                    //        Toast.LENGTH_SHORT).show();

                                    // jump to login activity
                                    Intent invokeLoginActivityIntent = new Intent(InviteActivity.this, LoginActivity.class);
                                    startActivity(invokeLoginActivityIntent);
                                } else {
                                    submitInvitation();

                                    Toast.makeText(InviteActivity.this, getResources().getString(R.string.OMEPARSEINVITEPLAYERSUBMITSUCCESSALERTINFO),
                                            Toast.LENGTH_SHORT).show();
                                    InviteActivity.this.finish();

                                }
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.OMEPARSENO), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                minviteplayButton.setBackgroundColor(AppConstant.OMETOPLAYDEFAULTCOLOR);
                                minviteplayButton.setTextColor(Color.WHITE);
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog minviteplayAlertDialog = minviteplayAlertDialogBuilder.create();

                // show it
                minviteplayAlertDialog.show();
            }
        });

        // add invite play button
        mInviteLinerLayout.addView(minviteplayButton);
    }

    // Ozzie Zhang 10-29-2014 please modify this function
    // define submit invitation to cloud
    private void submitInvitation () {

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
        Invite invite = new Invite();

        // Set the location to the current user's location
        invite.setLocation(geoPoint);

        invite.setUser(ParseUser.getCurrentUser());
        invite.setFromUser(ParseUser.getCurrentUser());
        invite.setFromUsername(ParseUser.getCurrentUser().getUsername());
        // Ozzie Zhang 2014-11-02 please modify this line code
        invite.setText(ParseUser.getCurrentUser().getUsername() + " "+ getResources().getString(R.string.OMEPARSEWANTTOKEY)
                    + " " + getResources().getString(R.string.OMEPARSEINVITETITLEPLAY) + " " + msporttype);
        invite.setSportType(msporttype);
        invite.setSportTypeValue(msporttypevalue);
        invite.setPlayerLevel(mplayerlevel);
        invite.setPlayerNumber(mplayernumber);
        invite.setPlayTime(minviteplaytime);
        invite.setCourt(mcourt);
        invite.setFee(minviteplayfee);
        invite.setOther(mother);
        invite.setSubmitTime(msubmittime);

        ParseACL acl = new ParseACL();

        // Give public read access
        acl.setPublicReadAccess(true);
        invite.setACL(acl);

        // Save the post
        invite.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
            //      dialog.dismiss();
            finish();
           }
            });

        return;

    }



    @Override
    public void afterTextChanged(Editable arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }



    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection mconnection = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder mstringBuilder = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            mstringBuilder.append("?key=" + PLACE_API_KEY);
           // sb.append("&components=country:uk");
            mstringBuilder.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL murl = new URL(mstringBuilder.toString());
            mconnection = (HttpURLConnection) murl.openConnection();
            InputStreamReader in = new InputStreamReader(mconnection.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (mconnection != null) {
                mconnection.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            if (predsJsonArray != null) {
                resultList = new ArrayList<String>(predsJsonArray.length());
            }

            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }


    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }


    private ArrayList<String> NearBy(String input) {
        ArrayList<String> resultList  = null;

        HttpURLConnection mconnection = null;
        StringBuilder jsonResults     = new StringBuilder();

        String mlocale                = Locale.getDefault().getLanguage();

        try {
            StringBuilder mstringBuilder = new StringBuilder(PLACES_API_BASE + TYPE_NEARBY + OUT_JSON);
            mstringBuilder.append("?key=" + PLACE_API_KEY);

            // set search type
            mstringBuilder.append("&types=gym|school|stadium|university|amusement_park");

            mstringBuilder.append("&location=" + geoPoint.getLatitude()+","+geoPoint.getLongitude());

            // order by distance
            //mstringBuilder.append("&rankby=distance");

            // radius meter
            mstringBuilder.append("&radius=20000" );

            mstringBuilder.append("&language=" + mlocale);
           // mstringBuilder.append("&input=" + URLEncoder.encode(input, "utf8"));
           // mstringBuilder.append("&input=" + URLEncoder.encode(input, "utf8"));


            URL murl = new URL(mstringBuilder.toString());
            mconnection = (HttpURLConnection) murl.openConnection();
            InputStreamReader in = new InputStreamReader(mconnection.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (mconnection != null) {
                mconnection.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            // Extract the Place descriptions from the results
            if (predsJsonArray != null) {
                resultList = new ArrayList<String>(predsJsonArray.length());
            }

            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("name"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }


    private class PlacesNearByAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public PlacesNearByAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = NearBy(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }


}
