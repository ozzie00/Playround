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

package com.oneme.toplay.join;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.database.Sport;

public class InfoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle          = this.getArguments();
        String mworkout        = bundle.getString(Application.INTENT_EXTRA_WORKOUTNAME);
        String mcourt          = bundle.getString(Application.INTENT_EXTRA_COURT);
        String msporttypevalue = bundle.getString(Application.INTENT_EXTRA_SPORTTYPEVALUE);
        String minviteplaytime = bundle.getString(Application.INTENT_EXTRA_TIME);
        String mother          = bundle.getString(Application.INTENT_EXTRA_OTHER);

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.ome_activity_join_info_fragment, container, false);

        TextView mworkoutname = (TextView)rootView.findViewById(R.id.join_info_workout_name);
        mworkoutname.setText(mworkout);

        ImageView msporttype = (ImageView)rootView.findViewById(R.id.join_info_workout_sport);
        if (msporttypevalue != null) {
            msporttype.setImageDrawable(getResources().getDrawable(Sport.msporticonarray[Integer.parseInt(msporttypevalue)]));
        }


        TextView mworkoutlocation = (TextView)rootView.findViewById(R.id.join_info_workout_location);
        mworkoutlocation.setText(mcourt);

        TextView mworkoutdescription = (TextView)rootView.findViewById(R.id.join_info_workout_description_content);
        mworkoutdescription.setText(mother);




        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(
        Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ome_activity_join_info_fragment_menu, menu);

    }


}