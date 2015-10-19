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

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;

import com.oneme.toplay.R;
import com.oneme.toplay.track.CnMapOverlay.CachedLocation;
import com.oneme.toplay.track.stats.TripStatistics;

import java.util.ArrayList;
import java.util.List;

//import com.google.common.annotations.VisibleForTesting;

/**
 * A path painter that varies the path colors based on fixed speeds or average
 * speed margin depending of the TrackPathDescriptor passed to its constructor.
 * 
 * @author Vangelis S.
 */
public class CnMultiColorTrackPath implements CnTrackPath {
  private final TrackPathDescriptor trackPathDescriptor;
  private final int slowColor;
  private final int normalColor;
  private final int fastColor;

  public CnMultiColorTrackPath(Context context, TrackPathDescriptor trackPathDescriptor) {
    this.trackPathDescriptor = trackPathDescriptor;
    slowColor = context.getResources().getColor(R.color.track_color_slow);
    normalColor = context.getResources().getColor(R.color.track_color_normal);
    fastColor = context.getResources().getColor(R.color.track_color_fast);
  }

  @Override
  public boolean updateState(TripStatistics tripStatistics) {
    return trackPathDescriptor.updateState(tripStatistics);
  }

  @Override
  public void updatePath(BaiduMap baiduMap, ArrayList<Polyline> paths, int startIndex,
      List<CachedLocation> locations) {
    if (baiduMap == null) {
      return;
    }
    if (startIndex >= locations.size()) {
      return;
    }
    
    boolean newSegment = startIndex == 0 || !locations.get(startIndex - 1).isValid();
    LatLng lastLatLng = startIndex != 0 ? locations.get(startIndex -1).getLatLng() : null;
    
    ArrayList<LatLng> lastSegmentPoints = new ArrayList<LatLng>();
    int lastSegmentColor = paths.size() != 0  ? paths.get(paths.size() - 1).getColor() : slowColor;
    boolean useLastPolyline = true;

    for (int i = startIndex; i < locations.size(); ++i) {
      CachedLocation cachedLocation = locations.get(i);

      // If not valid, start a new segment
      if (!cachedLocation.isValid()) {
        newSegment = true;
        lastLatLng = null;
        continue;
      }
      LatLng latLng = cachedLocation.getLatLng();
      int color = getColor(cachedLocation.getSpeed());
      
      // Either update point or draw a line from the last point
      if (newSegment) {
        CnTrackPathUtils.addPath(baiduMap, paths, lastSegmentPoints, lastSegmentColor, useLastPolyline);
        useLastPolyline = false;
        lastSegmentColor = color;
        newSegment = false;
      }
      if (lastSegmentColor == color) {
        lastSegmentPoints.add(latLng);
      } else {
        CnTrackPathUtils.addPath(baiduMap, paths, lastSegmentPoints, lastSegmentColor, useLastPolyline);
        useLastPolyline = false;
        if (lastLatLng != null) {
          lastSegmentPoints.add(lastLatLng);
        }
        lastSegmentPoints.add(latLng);
        lastSegmentColor = color;
      }
      lastLatLng = latLng;
    }
    CnTrackPathUtils.addPath(baiduMap, paths, lastSegmentPoints, lastSegmentColor, useLastPolyline);
  }

  //@VisibleForTesting
  protected int getColor(double speed) {
    if (speed <= trackPathDescriptor.getSlowSpeed()) {
      return slowColor;
    } else if (speed <= trackPathDescriptor.getNormalSpeed()) {
      return normalColor;
    } else {
      return fastColor;
    }
  }
  
  //@VisibleForTesting
  protected int getSlowColor() {
    return slowColor;
  }

  //@VisibleForTesting
  protected int getNormalColor() {
    return normalColor;
  }

  //@VisibleForTesting
  protected int getFastColor() {
    return fastColor;
  }
}
