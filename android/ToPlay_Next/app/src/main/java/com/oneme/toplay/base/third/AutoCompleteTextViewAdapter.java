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
package com.oneme.toplay.base.third;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;




public class AutoCompleteTextViewAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> mData;
    private ArrayList<String> itemsAll;
    private ArrayList<String> suggestions;
    private int viewResourceId;

    public AutoCompleteTextViewAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
        super(context, textViewResourceId, items);
        this.mData          = items;
        this.itemsAll       = (ArrayList<String>) items.clone();
        this.suggestions    = new ArrayList<String>();
        this.viewResourceId = textViewResourceId;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        String name = mData.get(position);
        if (name != null) {
            TextView homename    = (TextView)  v.findViewById(android.R.id.text2);
            if (homename != null) {
                homename.setText(name);
            }

        }
        return v;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int index) {
        return mData.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    suggestions.clear();
                    for (String string : itemsAll) {
                        if (string.toLowerCase().contains(constraint.toString().toLowerCase())) { //.startsWith(constraint.toString().toLowerCase())) {
                            suggestions.add(string);
                        }
                    }
                    filterResults.values = suggestions;
                    filterResults.count  = suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                ArrayList<String> filteredList = (ArrayList<String>) results.values;
                if(results != null && results.count > 0) {
                    clear();
                    notifyDataSetChanged();
                    for (String string : filteredList) {
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
