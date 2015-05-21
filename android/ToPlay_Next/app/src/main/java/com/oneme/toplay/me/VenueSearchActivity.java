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

package com.oneme.toplay.me;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.Sport;
import com.oneme.toplay.database.Venue;

import com.oneme.toplay.ui.BaseActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class VenueSearchActivity extends BaseActivity {

    class VenueData {
        String mType;
        String mName;
        String mAddress;
        String mPhone;
    }

    // Places Listview
    ListView msearchresult;
    VenueData mselectlocation;

    //public String data;
    public ArrayList<VenueData> msuggest;
    public ArrayAdapter<VenueData> madapter;

    private static final int MAX_VENUE_SEARCH_RESULTS = 5;

    private String mnameKey = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_venue_search);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(VenueSearchActivity.this,
                //        MyVenueActivity.class)));
            }
        });

        //msuggest        = new ArrayList<VenueData>();
        mselectlocation = new VenueData();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final EditText searchedittext = (EditText) findViewById(R.id.venue_search_content_text_view);
        searchedittext.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // if the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    //new getVenueNameAutocomplete().execute(searchedittext.toString());
                    //new getVenueAddressAutocomplete().execute(searchedittext.toString());
                    return true;
                }
                return false;
            }
        });

        // setting search name list view
        msearchresult = (ListView) findViewById(R.id.venue_search_name_list);
        msearchresult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mselectlocation = (VenueData) parent.getItemAtPosition(position);
                Intent newIntent = new Intent();
                newIntent.putExtra(Application.INTENT_EXTRA_VENUE, mselectlocation.mName);
                newIntent.putExtra(Application.INTENT_EXTRA_VENUEPHONE, mselectlocation.mPhone);
                newIntent.putExtra(Application.INTENT_EXTRA_VENUETYPE, mselectlocation.mType);
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
                new getVenueNameAutocomplete().execute(newText);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }




    class getVenueNameAutocomplete extends AsyncTask<String,String,String>{
        private ProgressDialog venueLoadDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            venueLoadDialog = new ProgressDialog(VenueSearchActivity.this);
            venueLoadDialog.show();
        }

        @Override
        protected String doInBackground(String... key) {
            mnameKey = key[0];
            mnameKey = mnameKey.trim();
            msuggest = new ArrayList<VenueData>();

            final int mlimit = MAX_VENUE_SEARCH_RESULTS + MAX_VENUE_SEARCH_RESULTS;

            runOnUiThread(new Runnable(){
                public void run(){

                    ParseQuery<Venue> query = Venue.getQuery();
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                    query.whereContains(AppConstant.OMETOPLAYVENUENAMEKEY, mnameKey);
                    query.setLimit(MAX_VENUE_SEARCH_RESULTS);
                    query.findInBackground(new FindCallback<Venue>() {
                        public void done(List<Venue> venueList, ParseException e) {
                            if (e == null) {
                                for (Venue venue : venueList) {
                                    VenueData mvenue = new VenueData();
                                    mvenue.mType     = venue.getType();
                                    mvenue.mName     = venue.getName();
                                    mvenue.mAddress  = venue.getAddress();
                                    mvenue.mPhone    = venue.getPhone();
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
                                    VenueData mvenue = new VenueData();
                                    mvenue.mType     = venue.getType();
                                    mvenue.mName     = venue.getName();
                                    mvenue.mAddress  = venue.getAddress();
                                    mvenue.mPhone    = venue.getPhone();
                                    if (msuggest.size() < mlimit) {
                                        msuggest.add(mvenue);
                                    }
                                }
                                madapter = new resultListAdapter(VenueSearchActivity.this, msuggest);
                                msearchresult.setAdapter(madapter);
                                madapter.notifyDataSetChanged();

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


    class resultListAdapter extends ArrayAdapter<VenueData> {

        LayoutInflater mInflater;

        VenueData mvenue;
        ArrayList<VenueData> mdata;

        public resultListAdapter(Context c, ArrayList<VenueData> data){
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

        public void setData(ArrayList<VenueData> mPpst) {
            //contains class items data.
            mdata = mPpst;
        }

        @Override
        public VenueData getItem(int arg0) {
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
                convertView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
                        LayoutParams.WRAP_CONTENT));
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

            if (mvenue.mType != null) {
                int index = Sport.msportarraylist.indexOf(mvenue.mType);

                // check exception
                if (index < 0) {
                    index = 0;
                }
                holder.type.setImageResource(Sport.msporticonarray[index]);
            }
            if (mvenue.mName!=null) {
                  holder.name.setText(mvenue.mName);
            }

            if (mvenue.mAddress!=null){
                  holder.address.setText(mvenue.mAddress);
            }

            if (mvenue.mPhone!=null && mvenue.mPhone.length() > AppConstant.OMEPHONEMINIMUMLENGTH){
                holder.phone.setVisibility(View.VISIBLE);

                holder.phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent invokePhoneCall = new Intent(Intent.ACTION_CALL);
                        invokePhoneCall.setData(Uri.parse(AppConstant.OMEPARSEINVOKECALLPHONE + mvenue.mPhone));
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
                    ArrayList<VenueData> filteredList = (ArrayList<VenueData>) results.values;
                    if(results != null && results.count > 0) {
                        clear();
                        notifyDataSetChanged();
                        for (VenueData string : filteredList) {
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

}


