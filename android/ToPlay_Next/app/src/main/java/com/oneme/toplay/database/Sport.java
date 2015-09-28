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

import java.util.ArrayList;
import java.util.Arrays;

public final class Sport {

    // Note: IndexOf is corresponding to getSportTypeValue of Invite class, and also is the same as
    // sport_type_array of strings.xml in values
    // Other exchange with GYM because of history reason
    public static final ArrayList<String> msportarraylist = new ArrayList<>(Arrays.asList(
            "Badminton",
            "Baseball",
            "Basketball",
            "Beach Volleyball",
            "Bowling",
            "Cricket",
            "Football, American",
            "Football, Rugby",
            "Football, Soccer",
            "Golf",
            "Handball",
            "Hockey",
            "Polo",
            "Ice Hockey",
            "Squash",
            "Table Soccer",
            "Table Tennis",
            "tennis",
            "Volleyball",
            "Water Polo",
            "Snooker",
            "Cycling Mountain Bike",
            "Cycling Road",
            "Fitness walking",
            "Running",
            "Walking",
            "Treadmill",
            "Trekking",
            "Canoe",
            "Diving",
            "Kite surfing",
            "Rowing",
            "Sailing",
            "Scuba diving",
            "Surfing",
            "Swimming",
            "Windsurfing",
            "Canyoning",
            "Caving",
            "Climbing",
            "Ice climbing",
            "Orienteering",
            "Skateboarding",
            "Archery",
            "Shooting",
            "Dart",
            "Alpine Skiing",
            "Bobsleigh",
            "Cross country ski",
            "Luge",
            "Roller skiing",
            "Ski Jumping",
            "Snowboarding",
            "Snowshoeing",
            "Speed skating",
            "Short Track Speed Skating",
            "Skating",
            "Skeleton",
            "Curling",
            "Boxing",
            "Dancing",
            "Elliptical training",
            "Fencing",
            "Gymnastics",
            "Judo",
            "Martial arts",
            "Pilates",
            "Poker",
            "Riding",
            "Taekwondo",
            "Trampoline",
            "Weight training",
            "Wheelchair",
            "Wrestling",
            "Yoga",
            "Other",
            "Inline skating",
            "Karting"
    ));

    // Note: IndexOf is corresponding to getSportTypeValue of Invite class, and also is the same as
    // sport_type_array of strings.xml in values
    // Other exchange with GYM because of history reason
    public static final int msporticonarray[] = {
            R.drawable.ome_badminton_icon,
            R.drawable.ome_baseball_icon,
            R.drawable.ome_basketball_icon,
            R.drawable.ome_beach_volleyball_icon,
            R.drawable.ome_bowling_icon,
            R.drawable.ome_cricket_icon,
            R.drawable.ome_football_american_icon,
            R.drawable.ome_football_rugby_icon,
            R.drawable.ome_football_soccer_icon,
            R.drawable.ome_golf_icon,
            R.drawable.ome_handball_icon,
            R.drawable.ome_hockey_icon,
            R.drawable.ome_polo_icon,
            R.drawable.ome_ice_hockey_icon,
            R.drawable.ome_squash_icon,
            R.drawable.ome_table_soccer_icon,
            R.drawable.ome_table_tennis_icon,
            R.drawable.ome_tennis_icon,
            R.drawable.ome_volleyball_icon,
            R.drawable.ome_water_polo_icon,
            R.drawable.ome_snooker_icon,
            R.drawable.ome_mountain_bike_icon,
            R.drawable.ome_cycling_icon,
            R.drawable.ome_fitness_walk_icon,
            R.drawable.ome_running_icon,
            R.drawable.ome_walk_icon,
            R.drawable.ome_treadmill_icon,
            R.drawable.ome_trekking_icon,
            R.drawable.ome_canoe_icon,
            R.drawable.ome_diving_icon,
            R.drawable.ome_kite_surfing_icon,
            R.drawable.ome_rowing_icon,
            R.drawable.ome_sailing_icon,
            R.drawable.ome_scuba_diving_icon,
            R.drawable.ome_surfing_icon,
            R.drawable.ome_swimming_icon,
            R.drawable.ome_wind_surfing_icon,
            R.drawable.ome_canyoning_icon,
            R.drawable.ome_caving_icon,
            R.drawable.ome_climbing_icon,
            R.drawable.ome_ice_climbing_icon,
            R.drawable.ome_orienteering_icon,
            R.drawable.ome_skateboarding_icon,
            R.drawable.ome_archery_icon,
            R.drawable.ome_shooting_icon,
            R.drawable.ome_darts_icon,
            R.drawable.ome_alpine_skiing_icon,
            R.drawable.ome_bobsleigh_icon,
            R.drawable.ome_cross_country_ski_icon,
            R.drawable.ome_luge_icon,
            R.drawable.ome_roller_skiing_icon,
            R.drawable.ome_ski_jumping_icon,
            R.drawable.ome_snowboarding_icon,
            R.drawable.ome_snowshoeing_icon,
            R.drawable.ome_speed_skating_icon,
            R.drawable.ome_short_track_speed_skating_icon,
            R.drawable.ome_skating_icon,
            R.drawable.ome_skeleton_icon,
            R.drawable.ome_curling_icon,
            R.drawable.ome_boxing_icon,
            R.drawable.ome_dancing_icon,
            R.drawable.ome_elliptical_training_icon,
            R.drawable.ome_fencing_icon,
            R.drawable.ome_gymnastics_icon,
            R.drawable.ome_judo_icon,
            R.drawable.ome_martial_arts_icon,
            R.drawable.ome_pilate_icon,
            R.drawable.ome_poker_icon,
            R.drawable.ome_riding_icon,
            R.drawable.ome_taekwondo_icon,
            R.drawable.ome_trampoline_icon,
            R.drawable.ome_weight_training_icon,
            R.drawable.ome_wheelchair_icon,
            R.drawable.ome_wrestling_icon,
            R.drawable.ome_yoga_icon,
            R.drawable.ome_gym_icon,
            R.drawable.ome_inline_skating_icon,
            R.drawable.ome_karting_icon
    };
}
