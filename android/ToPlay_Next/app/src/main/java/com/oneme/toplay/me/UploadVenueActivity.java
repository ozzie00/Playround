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

package com.oneme.toplay.me;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.CheckGoogleService;
import com.oneme.toplay.database.Venue;
import com.oneme.toplay.ui.BaseActivity;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class UploadVenueActivity extends BaseActivity {

    private final String TAG= "UploadVenueActivity";
    private final Context context = UploadVenueActivity.this;

    private EditText mnameText;
    private TextView maddressText;
    private TextView mphoneText;

    private EditText mphoneEdit;

    private String mname    = null;
    private String maddress = null;
    private String mphone   = null;

    private ParseGeoPoint geoPoint;
    public MenuItem mvenueadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ome_activity_uploadvenue);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(UploadVenueActivity.this,
                //        MyVenueActivity.class)));
            }
        });

        final Boolean isAvailable = CheckGoogleService.access(UploadVenueActivity.this);

        //get point according to  current latitude and longitude
        geoPoint = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

        if (isAvailable) {
            // show nearby place for user
            new getNearbyPlace().execute(AppConstant.OMEPARSENULLSTRING);
        } else {
            new getBdGeocoder().execute(AppConstant.OMEPARSENULLSTRING);
        }

        // add venue name
        mnameText = (EditText) findViewById(R.id.uploadvenue_name_Text);
        mnameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                if (arg0.length() > 0) {
                    mname = arg0.toString();
                    mvenueadd.setIcon(R.drawable.ome_invite_add_pressed);
                } else {
                    mvenueadd.setIcon(R.drawable.ome_invite_add);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    mname = s.toString();
                    mvenueadd.setIcon(R.drawable.ome_invite_add_pressed);
                } else {
                    mvenueadd.setIcon(R.drawable.ome_invite_add);
                }

            }
        });

        // add address
        RelativeLayout locationblock = (RelativeLayout)findViewById(R.id.uploadvenue_location_block);
        maddressText = (TextView) findViewById(R.id.uploadvenue_location_address_view);

        locationblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invokeSearchActivityIntent = new Intent(UploadVenueActivity.this, SearchAddressActivity.class);
                startActivityForResult(invokeSearchActivityIntent, AppConstant.OMEPARSEUPLOADVENUESEARCHLOCATIONRESULT);

            }
        });

        // add mphone
        RelativeLayout mphoneblock = (RelativeLayout)findViewById(R.id.uploadvenue_phone_block);
        mphoneText                 = (TextView)findViewById(R.id.uploadvenue_phone_view);

        mphoneblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom descritpion dialog
                final Dialog descriptiondialog = new Dialog(context);
                descriptiondialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                descriptiondialog.setContentView(R.layout.ome_activity_uploadvenue_mobile_dialog);

                mphoneEdit = (EditText)descriptiondialog.findViewById(R.id.uploadvenue_mobile_dialog_edittext);

                // set the custom dialog components - text, image and button
                TextView phonetitle = (TextView) descriptiondialog.findViewById(R.id.uploadvenue_mobile_dialog_title);
                phonetitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        descriptiondialog.dismiss();
                    }
                });

                TextView phonedone = (TextView) descriptiondialog.findViewById(R.id.uploadvenue_mobile_dialog_OK);
                // if TextView is clicked, close the custom dialog
                phonedone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mphone = mphoneEdit.getText().toString();
                        descriptiondialog.dismiss();
                        mphoneText.setText(mphone);
                    }
                });

                descriptiondialog.show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ome_upload_venue_menu, menu);

        mvenueadd = menu.findItem(R.id.action_upload_venue_add);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_upload_venue_add:
                if ((mname != null && mname.length() > 1)
                        && ((maddress != null) && maddress.length() > 1)
                        && ((mphone != null) && mphone.length() > 1))  {
                    submitVenue();
                    finish();
                } else {
                    Toast.makeText(UploadVenueActivity.this, getResources().getString(R.string.OMEPARSEUPLOADVENUEALERT), Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.OMEPARSEUPLOADVENUESEARCHLOCATIONRESULT && resultCode == Activity.RESULT_OK) {
            maddressText.setText(data.getStringExtra(Application.INTENT_EXTRA_SEARCHADDRESS));
            maddress = maddressText.getText().toString();
        }

    }

    private void submitVenue () {

        // Create an invitation.
        Venue venue = new Venue();

        ParseGeoPoint mGeoPoint = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));;
        // Set the detailed venue information
        venue.setName(mname);
        venue.setAddress(maddress);
        venue.setPhone(mphone);
        venue.setLocation(mGeoPoint);
        venue.setCourtNumber("1");
        venue.setIndoor("1");
        venue.setLighted("1");
        venue.setPublic("Public");

        venue.setUploadedBy(ParseUser.getCurrentUser().getUsername());
        ParseACL acl = new ParseACL();

        // Give public read access
        acl.setPublicReadAccess(true);
        venue.setACL(acl);

        // Save the post
        venue.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                finish();
            }
        });

        return;

    }


    class getBdGeocoder extends AsyncTask<String,String,String> {

        String  mgeoresult = null;

        @Override
        protected String doInBackground(String... key) {
            HttpURLConnection mbdconnection = null;
            StringBuilder jsonResults       = new StringBuilder();

            try {
                StringBuilder mbdstringBuilder = new StringBuilder(AppConstant.BD_GEOCODER_API);
                mbdstringBuilder.append(AppConstant.BD_GEOCODER_KEY + AppConstant.BD_GEOCODER_API_KEY );
                // According to Baidu development document, it should add the following line code for setting callback,
                // but actual test result show that it shoulb be disable
                // mbdstringBuilder.append(AppConstant.BD_GEOCODER_CALLBACK);
                mbdstringBuilder.append(AppConstant.BD_GEOCODER_LOCATION + geoPoint.getLatitude() + AppConstant.OMEPARSECOMMASTRING + geoPoint.getLongitude());
                mbdstringBuilder.append(AppConstant.BD_GEOCODER_OUT_JSON);
                mbdstringBuilder.append(AppConstant.BD_GEOCODER_POIS);

                URL mbdurl             = new URL(mbdstringBuilder.toString());
                mbdconnection          = (HttpURLConnection) mbdurl.openConnection();
                InputStreamReader inbd = new InputStreamReader(mbdconnection.getInputStream());

                // Load the results into a StringBuilder
                int read;
                char[] buff  = new char[AppConstant.OMEPARSEBUFFERLENGTH];
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
                JSONObject jsonObj   = new JSONObject(jsonResults.toString());
                String isSuccessful  = jsonObj.getString(AppConstant.BD_GEOCODER_STATUS);
                JSONObject resultObj = jsonObj.getJSONObject(AppConstant.BD_GEOCODER_RESULT);

                if (isSuccessful.equals(AppConstant.OMEPARSEZEROSTRING)) {
                    mgeoresult = resultObj.getString(AppConstant.BD_GEOCODER_FORMATTED_ADDRESS);
                }

            } catch (JSONException je) {

            }

            if (mgeoresult != null) {
                return mgeoresult;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                maddressText.setText(result);
                maddress = result;
            }
        }

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


            } catch (IOException e) {


            } finally {
                if (mconnection != null) {
                    mconnection.disconnect();
                }
            }

            try {
                // Create a JSON object hierarchy from the results
                JSONObject jsonObj       = new JSONObject(jsonResults.toString());
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
                maddressText.setText(result);
                maddress = result;
            }
        }

    }


}
