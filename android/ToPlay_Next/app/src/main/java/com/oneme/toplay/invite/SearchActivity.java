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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.adapter.VenueAdapter;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.CopyVenue;
import com.oneme.toplay.database.Venue;
import com.oneme.toplay.ui.BaseActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

public class SearchActivity extends BaseActivity {

    class LocationData {
        String mName;
        String mAddress;
    }

    private ParseGeoPoint geoPoint;

    // Places Listview
    ListView msearchresult;
    LocationData mselectlocation;

    //public String data;
    public ArrayList<LocationData> msuggest;
    public ArrayAdapter<LocationData> madapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_search_location);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(DetailInfoActivity.this,
                //        LocalNextActivity.class)));
            }
        });

        final Boolean isAvailable = false;// DispatchActivity.getGooglePlayServicesState();

        msuggest        = new ArrayList<LocationData>();
        mselectlocation = new LocationData();

        // get point according to  current latitude and longitude
        geoPoint = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

        if (isAvailable) {
            // show nearby place for user
            new getNearbyPlace().execute(AppConstant.OMEPARSENULLSTRING);
        } else {
            new getVenueRadarAutocomplete().execute(geoPoint);
        }

        final EditText searchedittext = (EditText) findViewById(R.id.invite_search_content_text_view);
        searchedittext.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // if the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    return true;
                }
                return false;
            }
        });

        ImageView mlogo = (ImageView)findViewById(R.id.invite_search_google_logo);
        if (isAvailable) {
            mlogo.setVisibility(View.VISIBLE);
        }

        // setting search list view
        msearchresult = (ListView) findViewById(R.id.invite_search_location_list);
        msearchresult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mselectlocation = (LocationData) parent.getItemAtPosition(position);

                Intent newIntent = new Intent();
                newIntent.putExtra(Application.INTENT_EXTRA_SEARCHLOCATION, mselectlocation.mName);
                setResult(RESULT_OK, newIntent);
                finish();
            }
        });

        searchedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText  = s.toString();
                if (isAvailable == true) {
                    new getPlaceAutocomplete().execute(newText);
                } else {
                    new getBdPlaceAutocomplete().execute(newText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    class getBdPlaceAutocomplete extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... key) {
            String newText = key[0];
            newText = newText.trim();
            // newText = newText.replace(" ", "+");

            HttpURLConnection mconnection = null;
            StringBuilder jsonResults     = new StringBuilder();
            try {
                StringBuilder mstringBuilder = new StringBuilder(AppConstant.BD_PLACES_API_SUGGESTION);
                mstringBuilder.append(AppConstant.BD_PLACE_QUERY + newText);
                mstringBuilder.append(AppConstant.BD_PLACE_REGION);
                mstringBuilder.append(AppConstant.BD_PLACE_OUT_JSON);
                mstringBuilder.append(AppConstant.BD_PLACE_KEY);
                mstringBuilder.append(AppConstant.BD_PLACE_API_KEY);

                URL murl             = new URL(mstringBuilder.toString());
                mconnection          = (HttpURLConnection) murl.openConnection();

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
                JSONObject mjsonObject     = new JSONObject(jsonResults.toString());
                JSONArray mresultJsonArray = mjsonObject.getJSONArray(AppConstant.BD_PLACE_RESULT);

                // Extract the Place descriptions from the results
                if (mresultJsonArray != null) {
                    // this one for user input string
                    msuggest = new ArrayList<LocationData>(mresultJsonArray.length() + 1);
                }

                LocationData muserinput = new LocationData();
                muserinput.mName        = newText ;
                muserinput.mAddress     = getResources().getString(R.string.OMEPARSEINVITESEARCHLOCATIONCUSTOM);
                msuggest.add(muserinput);

                // parse query result
                for (int i = 0; i < mresultJsonArray.length(); i++) {
                    LocationData mlocationdata = new LocationData();
                    JSONObject mresultObject   = mresultJsonArray.getJSONObject(i);
                    String mname               = mresultObject.getString(AppConstant.BD_PLACE_NAME);
                    String mdistrict           = mresultObject.getString(AppConstant.BD_PLACE_DISTRICT);
                    String mcity               = mresultObject.getString(AppConstant.BD_PLACE_CITY);

                    StringBuilder msb= new StringBuilder();
                    if (mdistrict.length() > 1) {
                        msb.append(mdistrict);
                        if (mcity.length() > 1) {
                            msb.append(", ").append(mcity);
                        }
                    } else {
                        if (mcity.length() > 1) {
                            msb.append(mcity);
                        }
                    }

                    String maddress = msb.toString();
                    if (maddress == null) {
                        maddress = mname;
                    }

                    mlocationdata.mName    = mname;
                    mlocationdata.mAddress = maddress;
                    msuggest.add(mlocationdata);
                }

            } catch (JSONException e) {

            }

            runOnUiThread(new Runnable(){
                public void run(){
                    madapter = new resultListAdapter(SearchActivity.this, msuggest);
                    msearchresult.setAdapter(madapter);
                    madapter.notifyDataSetChanged();
                }
            });

            return null;
        }

    }

    class getPlaceAutocomplete extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... key) {
            String newText = key[0];
            newText = newText.trim();
           // newText = newText.replace(" ", "+");

            HttpURLConnection mconnection = null;
            StringBuilder jsonResults     = new StringBuilder();
            try {
                StringBuilder mstringBuilder = new StringBuilder(AppConstant.PLACE_API_BASE + AppConstant.PLACE_TYPE_AUTOCOMPLETE + AppConstant.PLACE_OUT_JSON);
                mstringBuilder.append(AppConstant.PLACE_KEY + AppConstant.PLACE_API_KEY);
                mstringBuilder.append(AppConstant.PLACE_INPUT + URLEncoder.encode(newText, AppConstant.PLACE_OUT_ENCODE));

                URL murl             = new URL(mstringBuilder.toString());
                mconnection          = (HttpURLConnection) murl.openConnection();
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
                JSONObject mjsonObject   = new JSONObject(jsonResults.toString());
                JSONArray mpredJsonArray = mjsonObject.getJSONArray(AppConstant.PLACE_RESPONSE_PREDICTION);

                // Extract the Place descriptions from the results
                if (mpredJsonArray != null) {
                    // this one for user input string as customize location
                    msuggest = new ArrayList<LocationData>(mpredJsonArray.length() + 1);
                }

                LocationData muserinput = new LocationData();
                muserinput.mName        = newText ;
                muserinput.mAddress     = getResources().getString(R.string.OMEPARSEINVITESEARCHLOCATIONCUSTOM);
                msuggest.add(muserinput);

                for (int i = 0; i < mpredJsonArray.length(); i++) {
                    LocationData mlocationdata = new LocationData();
                    JSONObject predsObject     = mpredJsonArray.getJSONObject(i);
                    JSONArray termJsonArray    = predsObject.getJSONArray(AppConstant.PLACE_PREDICTION_TERM);
                    ArrayList<String> mmerge   = null;

                    if (termJsonArray != null) {
                        mmerge = new ArrayList<String>(termJsonArray.length());
                    }

                    // add each value to string
                    for (int j = 0; j < termJsonArray.length(); j++) {
                        String mvalue   = termJsonArray.getJSONObject(j).getString(AppConstant.PLACE_TERM_VALUE);
                        mmerge.add(mvalue);
                    }

                    String mname;

                    // get the first value, it is name
                    mname = mmerge.get(0);

                    StringBuilder msb= new StringBuilder();

                    // create new string with terms value except the first value, use comma as divider
                    // without the first and last comma
                    if (mmerge.size() > 1) {
                        int j= 1;
                        for (; j < mmerge.size() - 1; j++) {
                            msb.append(mmerge.get(j)).append(", ");
                        }
                        msb.append(mmerge.get(j));
                    }

                    String maddress = msb.toString();
                    if (maddress == null) {
                        maddress = mname;
                    }

                    mlocationdata.mName    = mname;
                    mlocationdata.mAddress = maddress;
                    msuggest.add(mlocationdata);
                }

            } catch (JSONException e) {

            }

            runOnUiThread(new Runnable(){
                public void run(){
                    if (msuggest.size() >= 1) {
                        madapter = new resultListAdapter(SearchActivity.this, msuggest);
                        msearchresult.setAdapter(madapter);
                        madapter.notifyDataSetChanged();
                    }
                }
            });

            return null;
        }

    }

    class resultListAdapter extends ArrayAdapter<LocationData> {

        LayoutInflater mInflater;

        LocationData mlocation;
        ArrayList<LocationData> mdata;

        public resultListAdapter(Context c, ArrayList<LocationData> data){
            super(c, 0);
            this.mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mdata     = data;
        }

        @Override
        public int getCount() {

            if(mdata!=null){
                return mdata.size();
            }else{
                return 0;
            }
        }

        public void setData(ArrayList<LocationData> mPpst) {
            //contains class items data.
            mdata = mPpst;
        }

        @Override
        public LocationData getItem(int arg0) {
            // TODO Auto-generated method stub
            //return arg0;
            return mdata.get(arg0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            ///int type = getItemViewType(arg0);
            if(mdata == null ){
                return null;
            }

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.ome_activity_search_location_item, null);
                convertView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
                        LayoutParams.WRAP_CONTENT));
                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder         = new ViewHolder();
                holder.name    = (TextView) convertView.findViewById(R.id.search_location_name_view);
                holder.address = (TextView) convertView.findViewById(R.id.search_location_address_view);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

           // mlocation = mPostingData.get(position);
            mlocation = getItem(position);

            if (mlocation.mName!=null) {
                  holder.name.setText(mlocation.mName);
            }

            if (mlocation.mAddress!=null){
                  holder.address.setText(mlocation.mAddress);
            }

            return convertView;
        }

        class ViewHolder {
            TextView name;
            TextView address;
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public Filter getFilter() {
            Filter myFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();

                    // actually this constraint is no useful
                    if (constraint != null) {
                        filterResults.values = mdata;
                        filterResults.count  = mdata.size();
                        return filterResults;
                    } else {
                        filterResults.values = mdata;
                        filterResults.count  = mdata.size();
                        return filterResults;
                    }
                }

                @Override
                protected void publishResults(CharSequence contraint, FilterResults results) {
                    ArrayList<LocationData> filteredList = (ArrayList<LocationData>) results.values;
                    if(results != null && results.count > 0) {
                        clear();
                        notifyDataSetChanged();
                        for (LocationData string : filteredList) {
                            add(string);
                        }
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return myFilter;
        }
    }

    class getNearbyPlace extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... key) {
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

                try {
                    // Create a JSON object hierarchy from the results
                    JSONObject mjsonObject = new JSONObject(jsonResults.toString());
                    String isSuccessful    = mjsonObject.getString(AppConstant.PLACE_SEARCH_STATUS);

                    if (isSuccessful.equals(AppConstant.PLACE_SEARH_OK)) {
                        JSONArray mresultJsonArray = mjsonObject.getJSONArray(AppConstant.PLACE_RESPONSE_RESULTS);

                        // Extract the Place descriptions from the results
                        if (mresultJsonArray != null) {
                            msuggest = new ArrayList<LocationData>(mresultJsonArray.length());
                        }

                        for (int i = 0; i < mresultJsonArray.length(); i++) {
                            LocationData mlocationdata = new LocationData();
                            JSONObject mresultObject   = mresultJsonArray.getJSONObject(i);
                            String mname               = mresultObject.getString(AppConstant.PLACE_RESULTS_NAME);
                            String maddress            = mresultObject.getString(AppConstant.PLACE_RESULTS_VICINITY);

                            mlocationdata.mName    = mname;
                            mlocationdata.mAddress = maddress;
                            msuggest.add(mlocationdata);
                        }
                    }

                } catch (JSONException e) {

                }

                runOnUiThread(new Runnable(){
                    public void run(){
                        if (msuggest.size() >= 1) {
                            madapter = new resultListAdapter(SearchActivity.this, msuggest);
                            msearchresult.setAdapter(madapter);
                            madapter.notifyDataSetChanged();
                        }
                    }
                });

                return null;

            } catch (MalformedURLException e) {

                return null;
            } catch (IOException e) {

                return null;
            } finally {
                if (mconnection != null) {
                    mconnection.disconnect();
                }
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }

    class getBdNearbyPlace extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... key) {
            HttpURLConnection mbdconnection = null;
            StringBuilder jsonResults       = new StringBuilder();
            String mlocale                  = Locale.getDefault().getLanguage();

            // if access google place fails, then use baidu place api
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

                try {
                    // Create a JSON object hierarchy from the results
                    JSONObject mjsonObject = new JSONObject(jsonResults.toString());
                    String isSuccessful    = mjsonObject.getString(AppConstant.BD_PLACE_STATUS);

                    if (isSuccessful.equals(AppConstant.OMEPARSEZEROSTRING)) {
                        JSONArray mresultJsonArray = mjsonObject.getJSONArray(AppConstant.BD_PLACE_RESULTS);

                        // Extract the Place descriptions from the results
                        if (mresultJsonArray != null) {
                            msuggest = new ArrayList<LocationData>(mresultJsonArray.length());
                        } else {

                        }

                        for (int i = 0; i < mresultJsonArray.length(); i++) {
                            LocationData mlocationdata = new LocationData();
                            JSONObject mresultObject   = mresultJsonArray.getJSONObject(i);
                            String mname               = mresultObject.getString(AppConstant.BD_PLACE_NAME);
                            String maddress            = mresultObject.getString(AppConstant.BD_PLACE_ADDRESS);

                            mlocationdata.mName    = mname;
                            mlocationdata.mAddress = maddress;
                            msuggest.add(mlocationdata);
                        }
                    }

                } catch (JSONException je) {

                }

                runOnUiThread(new Runnable(){
                    public void run(){
                        if (msuggest.size() >= 1) {
                            madapter = new resultListAdapter(SearchActivity.this, msuggest);
                            msearchresult.setAdapter(madapter);
                            madapter.notifyDataSetChanged();
                        }
                    }
                });

                return null;

            } catch (MalformedURLException me) {


            } catch (IOException ie) {


            } finally {
                if (mbdconnection != null) {
                    mbdconnection.disconnect();
                }
            }

            return null;


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }


    class getVenueRadarAutocomplete extends AsyncTask<ParseGeoPoint,String,String> {
        private ProgressDialog venueLoadDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            venueLoadDialog = new ProgressDialog(SearchActivity.this);
            venueLoadDialog.show();
        }

        @Override
        protected String doInBackground(ParseGeoPoint...key) {
            final ParseGeoPoint geoPoint = key[0];
            msuggest = new ArrayList<LocationData>();

            final int mlimit = 40;

            runOnUiThread(new Runnable() {
                public void run() {

                    ParseQuery<Venue> query = Venue.getQuery();
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                    query.whereNear(AppConstant.OMETOPLAYVENUELOCATIONKEY, geoPoint);
                    query.setLimit(mlimit);
                    query.findInBackground(new FindCallback<Venue>() {
                        public void done(List<Venue> venueList, ParseException e) {
                            if (e == null) {
                                for (Venue venue : venueList) {
                                    LocationData mlocationdata = new LocationData();
                                    String mname               = venue.getName();
                                    String maddress            = venue.getAddress();

                                    mlocationdata.mName    = mname;
                                    mlocationdata.mAddress = maddress;

                                    if (msuggest.size() < mlimit) {
                                        msuggest.add(mlocationdata);
                                    }
                                }

                                if (msuggest.size() >= 1) {
                                    madapter = new resultListAdapter(SearchActivity.this, msuggest);
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
            venueLoadDialog.dismiss();
        }

    }

}


