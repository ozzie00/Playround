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

package com.oneme.toplay.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.oneme.toplay.Application;
import com.oneme.toplay.CnMapActivity;
import com.oneme.toplay.R;
import com.oneme.toplay.adapter.SportTypeAdapter;
import com.oneme.toplay.adapter.VenueAdapter;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.CopyVenue;
import com.oneme.toplay.base.VenueToIntentExtra;
import com.oneme.toplay.database.Sport;
import com.oneme.toplay.database.Venue;
import com.oneme.toplay.ui.widget.DrawShadowFrameLayout;
import com.oneme.toplay.venue.DetailInfoActivity;
import com.oneme.toplay.venue.PrimeBusinessActivity;
import com.oneme.toplay.venue.VenueNextActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VenueTypeAndSearchActivity extends BaseActivity {

    private static final String TAG = "VenueTypeAndSearchActivity";

    private static final int MAX_VENUE_SEARCH_RESULTS = 100;

    private ListView msearchresult;

    //public String data;
    public ArrayList<Venue> msuggest;
    public ArrayAdapter<Venue> madapter;

    private static ParseGeoPoint mGeoPoint;

    private MenuItem menuItem;

    private DrawShadowFrameLayout mDrawShadowFrameLayout;

    private TextView mcityText;

    private String mcity;

    private String msporttype      = null;

    // Add spinner for sport type
    String[] msportarray = {

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_navdrawer_venue_type_and_search);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
        //        | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);

        mDrawShadowFrameLayout = (DrawShadowFrameLayout) findViewById(R.id.main_content);

        mGeoPoint = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

        msuggest  = new ArrayList<Venue>();

        new getBdGeocoderCity().execute(AppConstant.OMEPARSENULLSTRING);

        new getVenueRadarAutocomplete().execute(mGeoPoint);

        mcityText = (TextView)findViewById(R.id.search_venue_city_view);

        // Note: msportarray items are correponding to msporticonarray of Sport Class
        msportarray = getResources().getStringArray(R.array.sport_type_array);

        // choose sport
        Spinner msportspinner              = (Spinner)findViewById(R.id.search_sport_spinner);
        SportTypeAdapter msportTypeAdapter = new SportTypeAdapter(VenueTypeAndSearchActivity.this, R.layout.ome_sport_row, msportarray, Sport.msporticonarray);
        msportspinner.setAdapter(msportTypeAdapter);

        msportspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                msporttype = Sport.msportarraylist.get(pos);
                new getVenueSearchAutocomplete().execute(mGeoPoint);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // fetch query result

        //new getVenueNameAutocomplete().execute("beijing");//venuequery);

        // setting search name list view
        msearchresult = (ListView) findViewById(R.id.search_type_venue_list);
        msearchresult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Venue venue = madapter.getItem(position);

                Intent invokeVenueDetailInfoActivityIntent = new Intent(VenueTypeAndSearchActivity.this, DetailInfoActivity.class);
                VenueToIntentExtra.putExtra(invokeVenueDetailInfoActivityIntent, venue);
                startActivity(invokeVenueDetailInfoActivityIntent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ome_search_venue_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_venue_prime:
                menuItem = item;
                //menuItem.setActionView(R.layout.ome_activity_search_venue_progressbar);
                Intent invokePrimeBusinessActivityIntent = new Intent(VenueTypeAndSearchActivity.this, PrimeBusinessActivity.class);
                startActivity(invokePrimeBusinessActivityIntent);
                break;
            case R.id.search_venue_menu:
                menuItem = item;
                //menuItem.setActionView(R.layout.ome_activity_search_venue_progressbar);
                Intent invokeVenueActivityIntent = new Intent(VenueTypeAndSearchActivity.this, VenueNextActivity.class);
                startActivity(invokeVenueActivityIntent);
                break;
           // case R.id.search_venue_map:
           //     menuItem = item;
                //menuItem.setActionView(R.layout.ome_activity_search_venue_progressbar);
                //menuItem.expandActionView();
                //new getVenueNameAutocomplete().execute(venuequery);
           //     Intent invokeVenueMapActivityIntent = new Intent(VenueTypeAndSearchActivity.this, CnMapActivity.class);
           //     startActivity(invokeVenueMapActivityIntent);
           //     break;

            default:
                break;
        }
        return true;
    }


    class getVenueRadarAutocomplete extends AsyncTask<ParseGeoPoint,String,String> {
        private ProgressDialog venueLoadDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(ParseGeoPoint...key) {
            final ParseGeoPoint geoPoint = key[0];
            msuggest = new ArrayList<Venue>();

            runOnUiThread(new Runnable() {
                public void run() {

                    ParseQuery<Venue> query = Venue.getQuery();
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                    query.whereNear(AppConstant.OMETOPLAYVENUELOCATIONKEY, geoPoint);
                    query.setLimit(MAX_VENUE_SEARCH_RESULTS);
                    query.findInBackground(new FindCallback<Venue>() {
                        public void done(List<Venue> venueList, ParseException e) {
                            if (e == null) {
                                for (Venue venue : venueList) {
                                    Venue mvenue = new Venue();
                                    CopyVenue.Done(venue, mvenue);

                                    if (msuggest.size() < MAX_VENUE_SEARCH_RESULTS) {
                                        msuggest.add(mvenue);
                                    }
                                }

                                if (msuggest.size() >= 1) {
                                    madapter = new VenueAdapter(VenueTypeAndSearchActivity.this, msuggest);
                                    msearchresult.setAdapter(madapter);
                                    madapter.notifyDataSetChanged();
                                }


                            } else {

                            }
                        }
                    });

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }

    class getVenueSearchAutocomplete extends AsyncTask<ParseGeoPoint,String,String> {
        private ProgressDialog venueSearchDialog;

        public getVenueSearchAutocomplete() {
            venueSearchDialog = new ProgressDialog(VenueTypeAndSearchActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            venueSearchDialog.show();
        }

        @Override
        protected String doInBackground(ParseGeoPoint...key) {
            final ParseGeoPoint geoPoint = key[0];
            msuggest = new ArrayList<Venue>();

            runOnUiThread(new Runnable() {
                public void run() {

                    ParseQuery<Venue> query = Venue.getQuery();
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                    query.whereNear(AppConstant.OMETOPLAYVENUELOCATIONKEY, geoPoint);
                   // query.whereContains(AppConstant.OMETOPLAYVENUEADDRESSKEY, mcity);
                    query.whereEqualTo(AppConstant.OMETOPLAYVENUETYPEKEY, msporttype);
                   // query.setLimit(MAX_VENUE_SEARCH_RESULTS);
                    query.findInBackground(new FindCallback<Venue>() {
                        public void done(List<Venue> venueList, ParseException e) {
                            if (e == null) {

                                for (Venue venue : venueList) {
                                    Venue mvenue = new Venue();
                                    CopyVenue.Done(venue, mvenue);

                                    msuggest.add(mvenue);
                                }

                                if (msuggest.size() >= 1) {
                                    madapter = new VenueAdapter(VenueTypeAndSearchActivity.this, msuggest);
                                    msearchresult.setAdapter(madapter);
                                    madapter.notifyDataSetChanged();
                                }


                            } else {

                            }
                        }
                    });

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (venueSearchDialog.isShowing()) {
                venueSearchDialog.dismiss();
            }
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_VENUE;
    }

    @Override
    protected void onActionBarAutoShowOrHide(boolean shown) {
        super.onActionBarAutoShowOrHide(shown);
        mDrawShadowFrameLayout.setShadowVisible(shown, shown);
    }

    class getBdGeocoderCity extends AsyncTask<String,String,String> {

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
                mbdstringBuilder.append(AppConstant.BD_GEOCODER_LOCATION + mGeoPoint.getLatitude() + AppConstant.OMEPARSECOMMASTRING + mGeoPoint.getLongitude());
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
                JSONObject jsonObj    = new JSONObject(jsonResults.toString());
                String isSuccessful   = jsonObj.getString(AppConstant.BD_GEOCODER_STATUS);
                JSONObject resultObj  = jsonObj.getJSONObject(AppConstant.BD_GEOCODER_RESULT);
                JSONObject addressObj = resultObj.getJSONObject(AppConstant.BD_GEOCODER_ADDRESS_COMPONENT);

                if (isSuccessful.equals(AppConstant.OMEPARSEZEROSTRING)) {
                    mgeoresult = addressObj.getString(AppConstant.BD_GEOCODER_CITY);
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
                mcityText.setText(result);
                mcity = result;
            }
        }

    }

}
