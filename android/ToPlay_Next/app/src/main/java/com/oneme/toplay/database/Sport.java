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

package com.oneme.toplay.database;

import com.oneme.toplay.R;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public final class Sport {

    // Note: IndexOf is corresponding to getSportTypeValue of Invite class
    public static final ArrayList<String> msportarraylist = new ArrayList<>(Arrays.asList(
            "Badminton",
            "Basketball",
            "Snooker",
            "Table Soccer",
            "Table Tennis",
            "tennis",
            "Trekking",
            "Running",
            "Cycling",
            "Other"));


    public static final int msporticonarray[] = {
            R.drawable.ome_badminton_icon,
            R.drawable.ome_basketball_icon,
            R.drawable.ome_snooker_icon,
            R.drawable.ome_table_soccer_icon,
            R.drawable.ome_table_tennis_icon,
            R.drawable.ome_tennis_icon,
            R.drawable.ome_trekking_icon,
            R.drawable.ome_running_icon,
            R.drawable.ome_cycling_icon,
            R.drawable.ome_other_icon
    };
}
