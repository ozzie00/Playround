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

import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;

/**
 * Various utility functions for track path painting.
 * 
 * @author Vangelis S.
 */
public class CnTrackPathUtils {

  private CnTrackPathUtils() {}

  /**
   * Add a path.
   * 
   * @param baiduMap the baidu map
   * @param paths the existing paths
   * @param points the path points
   * @param color the path color
   * @param append true to append to the last path
   */
  public static void addPath(BaiduMap baiduMap, ArrayList<Polyline> paths,
      ArrayList<LatLng> points, int color, boolean append) {
    if (points.size() == 0) {
      return;
    }
    if (append && paths.size() != 0) {
      Polyline lastPolyline = paths.get(paths.size() - 1);
      ArrayList<LatLng> pathPoints = new ArrayList<LatLng>();
      pathPoints.addAll(lastPolyline.getPoints());
      pathPoints.addAll(points);
      lastPolyline.setPoints(pathPoints);
    } else {

      // according to baidu exception "points count can not less than 2 or more than 10000"
      if (points.size() >= 2 && points.size() < 10000) {
        OverlayOptions polylineOptions = new PolylineOptions().width(5).color(color).points(points);
        //PolylineOptions polylineOptions = new PolylineOptions().width(5).color(color).points(points);

        //Polyline polyline = googleMap.addPolyline(polylineOptions);

        Polyline polyline = (Polyline) baiduMap.addOverlay(polylineOptions);

        //Polyline polyline = new Polyline(points,polylineOptions);


        paths.add(polyline);
      }
    }
    points.clear();
  }
}
