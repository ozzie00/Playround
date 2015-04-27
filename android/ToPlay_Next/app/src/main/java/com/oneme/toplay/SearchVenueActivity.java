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

package com.oneme.toplay;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.oneme.toplay.adapter.VenueAdapter;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.VenueToIntentExtra;
import com.oneme.toplay.database.Venue;
import com.oneme.toplay.venue.DetailInfoActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchVenueActivity extends ActionBarActivity {

    private static final String TAG = "SearchVenueActivity";

    private static final int MAX_VENUE_SEARCH_RESULTS = 5;

    private Bundle appData;

    private String venuequery;

    private ListView msearchresult;

    //public String data;
    public ArrayList<Venue> msuggest;
    public ArrayAdapter<Venue> madapter;

    private String mnameKey = null;

    private MenuItem menuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_search_venue);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
                | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);

        msuggest   = new ArrayList<Venue>();

        appData = getIntent().getBundleExtra(SearchManager.APP_DATA);
        if (appData != null) {
            boolean jargon = appData.getBoolean(Application.INTENT_EXTRA_VENUESEARCH);

        }

        // fetch query result
        venuequery = getIntent().getStringExtra(SearchManager.QUERY);

        new getVenueNameAutocomplete().execute(venuequery);

        // setting search name list view
        msearchresult = (ListView) findViewById(R.id.search_venue_list);
        msearchresult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Venue venue = madapter.getItem(position);

                Intent invokeVenueDetailInfoActivityIntent = new Intent(SearchVenueActivity.this, DetailInfoActivity.class);
                VenueToIntentExtra.putExtra(invokeVenueDetailInfoActivityIntent, venue);
                startActivity(invokeVenueDetailInfoActivityIntent);
            }
        });

        final EditText searchedittext = (EditText) findViewById(R.id.search_venue_content_text_view);
        searchedittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // if the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    return true;
                }
                return false;
            }
        });

        searchedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText  = s.toString();
                new getVenueNameAutocomplete().execute(newText);
            }

            @Override
            public void afterTextChanged(Editable s) {

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
            case R.id.menu_load:
                menuItem = item;
                menuItem.setActionView(R.layout.ome_activity_search_venue_progressbar);
                //menuItem.expandActionView();
                //new getVenueNameAutocomplete().execute(venuequery);
                break;
            default:
                break;
        }
        return true;
    }


    class getVenueNameAutocomplete extends AsyncTask<String,String,String> {
        private ProgressDialog venueLoadDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            venueLoadDialog = new ProgressDialog(SearchVenueActivity.this);
            venueLoadDialog.show();
        }

        @Override
        protected String doInBackground(String... key) {
            mnameKey = key[0];
            mnameKey = mnameKey.trim();
            msuggest = new ArrayList<Venue>();

            final int mlimit = MAX_VENUE_SEARCH_RESULTS + MAX_VENUE_SEARCH_RESULTS;

            runOnUiThread(new Runnable() {
                public void run() {

                    ParseQuery<Venue> query = Venue.getQuery();
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                    query.whereContains(AppConstant.OMETOPLAYVENUENAMEKEY, mnameKey);
                    query.setLimit(MAX_VENUE_SEARCH_RESULTS);
                    query.findInBackground(new FindCallback<Venue>() {
                        public void done(List<Venue> venueList, ParseException e) {
                            if (e == null) {
                                for (Venue venue : venueList) {
                                    Venue mvenue = new Venue();
                                    mvenue.setName(venue.getName());
                                    mvenue.setLevel(venue.getLevel());
                                    mvenue.setType(venue.getType());
                                    mvenue.setAddress(venue.getAddress());
                                    // mvenue.setLocation(venue.getLocation());
                                    mvenue.setPhone(venue.getPhone());
                                    mvenue.setCourtNumber(venue.getCourtNumber());
                                    mvenue.setLighted(venue.getLighted());
                                    mvenue.setIndoor(venue.getIndoor());
                                    mvenue.setPublic(venue.getPublic());
                                    mvenue.setObjectId(venue.getObjectId());

                                    if (msuggest.size() < mlimit) {
                                        msuggest.add(mvenue);
                                    }
                                }

                            } else {

                            }
                        }
                    });

                    ParseQuery<Venue> addressquery = Venue.getQuery();
                    addressquery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                    addressquery.whereContains(AppConstant.OMETOPLAYVENUEADDRESSKEY, mnameKey);
                    addressquery.setLimit(MAX_VENUE_SEARCH_RESULTS);
                    addressquery.findInBackground(new FindCallback<Venue>() {
                        public void done(List<Venue> venueList, ParseException e) {
                            if (e == null) {
                                for (Venue venue : venueList) {
                                    Venue mvenue = new Venue();
                                    mvenue.setName(venue.getName());
                                    mvenue.setLevel(venue.getLevel());
                                    mvenue.setType(venue.getType());
                                    mvenue.setAddress(venue.getAddress());
                                   // mvenue.setLocation(venue.getLocation());
                                    mvenue.setPhone(venue.getPhone());
                                    mvenue.setCourtNumber(venue.getCourtNumber());
                                    mvenue.setLighted(venue.getLighted());
                                    mvenue.setIndoor(venue.getIndoor());
                                    mvenue.setPublic(venue.getPublic());
                                    mvenue.setObjectId(venue.getObjectId());

                                    if (msuggest.size() < mlimit) {
                                        msuggest.add(mvenue);
                                    }
                                }

                                if (msuggest.size() >= 1) {
                                    madapter = new VenueAdapter(SearchVenueActivity.this, msuggest);
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


    /*
    class resultListAdapter extends ArrayAdapter<Venue> {

        LayoutInflater mInflater;

        Venue mvenue;
        ArrayList<Venue> mdata;

        public resultListAdapter(Context c, ArrayList<Venue> data){
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

        public void setData(ArrayList<Venue> mPpst) {
            //contains class items data.
            mdata = mPpst;
        }

        @Override
        public Venue getItem(int arg0) {
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
                convertView = mInflater.inflate(R.layout.ome_activity_venue_search_item, null);
                convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder         = new ViewHolder();
                holder.type    = (ImageButton)convertView.findViewById(R.id.venue_sport_type_button);
                holder.name    = (TextView) convertView.findViewById(R.id.venue_name_view);
                holder.address = (TextView) convertView.findViewById(R.id.venue_address_view);
                holder.phone   = (ImageButton)convertView.findViewById(R.id.venue_phone_button);

                // need set imagebutton focusable is false, then onItemClick can work on list view
                holder.type.setFocusable(false);
                //holder.phone.setFocusable(false);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // mlocation = mPostingData.get(position);
            mvenue = getItem(position);

            if (mvenue.getType() != null) {
                int index = Sport.msportarraylist.indexOf(mvenue.getType());
                if (index >= 0) {
                    holder.type.setImageResource(Sport.msporticonarray[index]);
                }
            }
            if (mvenue.getName()!=null) {
                holder.name.setText(mvenue.getName());
            }

            if (mvenue.getAddress()!=null){
                holder.address.setText(mvenue.getAddress());
            }

            if (mvenue.getPhone()!=null && mvenue.getPhone().length() > AppConstant.OMEPHONEMINIMUMLENGTH){
                holder.phone.setVisibility(View.VISIBLE);

                holder.phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent invokePhoneCall = new Intent(Intent.ACTION_CALL);
                        invokePhoneCall.setData(Uri.parse(AppConstant.OMEPARSEINVOKECALLPHONE + mvenue.getPhone()));
                        startActivity(invokePhoneCall);
                    }
                });

            }


            return convertView;
        }

        class ViewHolder {
            ImageButton type;
            TextView name;
            TextView address;
            ImageButton phone;
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
                    ArrayList<Venue> filteredList = (ArrayList<Venue>) results.values;
                    if(results != null && results.count > 0) {
                        clear();
                        notifyDataSetChanged();
                        for (Venue string : filteredList) {
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
    */


}
