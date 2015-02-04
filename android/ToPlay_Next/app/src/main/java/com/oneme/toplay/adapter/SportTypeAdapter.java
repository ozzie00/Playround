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
package com.oneme.toplay.adapter;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ImageView;


import com.oneme.toplay.R;


public class SportTypeAdapter extends ArrayAdapter<String>{

    Context mcontext;

    private LayoutInflater mInflater;
    // Add spinner for sport type
    String[] msportarray = {};

    int msporticon[] = {};

    public SportTypeAdapter(Context context, int textViewResourceId, String[] objects, int sporticon[]) {
        super(context, textViewResourceId, objects);
        this.mcontext      = context;
        this.mInflater     = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.msportarray   = objects;
        this.msporticon    = sporticon;

    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        //  LayoutInflater inflater = getLayoutInflater();
        View row                = mInflater.inflate(R.layout.ome_sport_row, parent, false);
        TextView label          = (TextView)row.findViewById(R.id.sport_type_name);
        label.setText(msportarray[position]);

        ImageView icon = (ImageView)row.findViewById(R.id.sport_icon);
        icon.setImageResource(msporticon[position]);

        return row;
    }

}
