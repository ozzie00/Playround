/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.oneme.toplay.track.util;

import android.content.Context;

import com.oneme.toplay.R;
import com.oneme.toplay.track.content.TracksProviderUtils;
import com.oneme.toplay.track.content.Track;
import com.oneme.toplay.track.content.TracksColumns;
import com.oneme.toplay.track.services.TrackRecordingServiceConnection;

/**
 * Utilities for updating track.
 * 
 * @author Jimmy Shih
 */
public class TrackUtils {

  private TrackUtils() {}

  public static String TRACK_SORT_ORDER = "IFNULL(" + TracksColumns.SHAREDWITHME + ",0) ASC, "
      + TracksColumns.STARTTIME + " DESC";

  public static void updateTrack(Context context, Track track, String name, String category,
      String description, TracksProviderUtils myTracksProviderUtils,
      TrackRecordingServiceConnection trackRecordingServiceConnection, boolean newWeight) {
    if (name != null) {
      track.setName(name);
    }
    boolean updateCalorie = false;
    if (category != null) {
      updateCalorie = !category.equals(track.getCategory()) || newWeight;
      track.setCategory(category);
      track.setIcon(TrackIconUtils.getIconValue(context, category));
    }

    if (description != null) {
      track.setDescription(description);
    }
    track.setModifiedTime(System.currentTimeMillis());
    myTracksProviderUtils.updateTrack(track);

    if (updateCalorie) {
      if (track.getId() == PreferencesUtils.getLong(context, R.string.recording_track_id_key)) {
        // Update calorie through track recording service
        TrackRecordingServiceConnectionUtils.updateCalorie(trackRecordingServiceConnection);
      } else {
        CalorieUtils.updateTrackCalorie(context, track);
      }
    }

    boolean driveSync = PreferencesUtils.getBoolean(
        context, R.string.drive_sync_key, PreferencesUtils.DRIVE_SYNC_DEFAULT);
    if (driveSync) {
      PreferencesUtils.addToList(context, R.string.drive_edited_list_key,
          PreferencesUtils.DRIVE_EDITED_LIST_DEFAULT, String.valueOf(track.getId()));
    }
  }
}
