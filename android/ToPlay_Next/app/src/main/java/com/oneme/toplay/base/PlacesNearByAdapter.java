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



import com.parse.ParseGeoPoint;


import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;



public class PlacesNearByAdapter extends ArrayAdapter<String> implements Filterable {

    private ParseGeoPoint geoPoint;

    private static final String LOG_TAG           = "PlacesNearByAdapter";

    private static final String PLACES_API_BASE   = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_NEARBY       = "/nearbysearch";


    private static final String OUT_JSON          = "/json";

    private static final String PLACE_API_KEY     = AppConstant.OMETOPLAYGOOGLEPLACEKEY;

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
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private ArrayList<String> NearBy(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection mconnection = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder mstringBuilder = new StringBuilder(PLACES_API_BASE + TYPE_NEARBY + OUT_JSON);
            mstringBuilder.append("?key=" + PLACE_API_KEY);
            mstringBuilder.append("&types=gym|stadium|university|school|amusement_park");
            // sb.append("&components=country:uk");
            mstringBuilder.append("&location=" + geoPoint.getLatitude() + "," + geoPoint.getLongitude());
            mstringBuilder.append("&radius=20000");
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
}