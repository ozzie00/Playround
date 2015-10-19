/*
 * Copyright 2011 Google Inc.
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

package com.oneme.toplay.track.maps;

import android.content.Context;

import com.oneme.toplay.R;

/**
 * A factory for {@link TrackPath}.
 * 
 * @author Vangelis S.
 */
public class CnTrackPathFactory {

  private CnTrackPathFactory() {}

  /**
   * Get a new {@link TrackPath}.
   * 
   * @param context the context
   */
  public static CnTrackPath getTrackPath(Context context, String trackColorMode) {
    if (context.getString(R.string.settings_map_track_color_mode_dynamic_value)
        .equals(trackColorMode)) {
      return new CnMultiColorTrackPath(context, new DynamicSpeedTrackPathDescriptor(context));
    } else if (context.getString(R.string.settings_map_track_color_mode_fixed_value)
        .equals(trackColorMode)) {
      return new CnMultiColorTrackPath(context, new FixedSpeedTrackPathDescriptor(context));
    } else {
      return new CnSingleColorTrackPath(context);
    }
  }
}