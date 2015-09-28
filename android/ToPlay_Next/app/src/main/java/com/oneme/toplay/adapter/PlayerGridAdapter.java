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

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneme.toplay.R;
import com.oneme.toplay.base.third.ImageLoader;
import com.oneme.toplay.database.PlayerIcon;

import java.util.ArrayList;


public class PlayerGridAdapter extends ArrayAdapter<PlayerIcon> {
    Context context;
    int layoutResourceId;
    ArrayList<PlayerIcon> mplayericonlist = new ArrayList<PlayerIcon>();
    ImageLoader mimageLoader;


    public PlayerGridAdapter(Context context, int layoutResourceId, ArrayList<PlayerIcon> playericonlist) {
        super(context, layoutResourceId, playericonlist);
        this.context          = context;
        this.layoutResourceId = layoutResourceId;
        this.mplayericonlist  = playericonlist;

        mimageLoader = new ImageLoader(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view            = convertView;
        RecordHolder holder  = null;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view                    = inflater.inflate(layoutResourceId, parent, false);

            holder            = new RecordHolder();
            holder.mimageview = (ImageView) view.findViewById(R.id.player_avatar_icon);
            holder.mtextview  = (TextView) view.findViewById(R.id.player_name);
            view.setTag(holder);
        } else {
            holder = (RecordHolder) view.getTag();
        }

        //Ozzie Zhang 2014-12-24 use imageLoader
        // Load image into GridView
        mimageLoader.DisplayImage(mplayericonlist.get(position).getTitle(),
                holder.mimageview);

        PlayerIcon mplayericon = mplayericonlist.get(position);
        holder.mtextview.setText(mplayericon.getTitle());
        //holder.mimageview.setImageBitmap(mplayericon.getImage());
        return view;
    }

    @Override
    public int getCount() {
        return mplayericonlist.size();
    }

    @Override
    public PlayerIcon getItem(int position) {
        return mplayericonlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class RecordHolder {
        TextView mtextview;
        ImageView mimageview;
    }
}
