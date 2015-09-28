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
package com.oneme.toplay.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.Photo;
import com.oneme.toplay.database.PhotoLink;
import com.oneme.toplay.database.Sport;
import com.oneme.toplay.database.Venue;
import com.parse.ParseQuery;

import java.util.ArrayList;

public class VenueAdapter extends ArrayAdapter<Venue> {

    LayoutInflater mInflater;
    Context context;

    Venue mvenue;
    ArrayList<Venue> mdata;
    ParseQuery<PhotoLink> mlinkquery;
    ParseQuery<Photo> mphotoquery;

    public VenueAdapter(Context c, ArrayList<Venue> data){
        super(c, 0);
        this.mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mdata     = data;
        this.context   = c;
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
        final ViewHolder holder;
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
            holder.type    = (ImageView)convertView.findViewById(R.id.venue_sport_type_button);
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

        String mvenueobjectId = mvenue.getObjectId();

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
                    context.startActivity(invokePhoneCall);
                }
            });

        }


        return convertView;
    }

    class ViewHolder {
        ImageView type;
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