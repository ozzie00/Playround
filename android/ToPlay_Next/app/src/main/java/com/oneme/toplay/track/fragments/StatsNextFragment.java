/*
 * Copyright 2008 Google Inc.
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

package com.oneme.toplay.track.fragments;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.ZoomControls;

import com.oneme.toplay.R;
import com.oneme.toplay.track.ChartView;
import com.oneme.toplay.track.TrackDetailNextActivity;
import com.oneme.toplay.track.content.Track;
import com.oneme.toplay.track.content.TrackDataHub;
import com.oneme.toplay.track.content.TrackDataListener;
import com.oneme.toplay.track.content.TrackDataType;
import com.oneme.toplay.track.content.Waypoint;
import com.oneme.toplay.track.stats.TripStatistics;
import com.oneme.toplay.track.stats.TripStatisticsUpdater;
import com.oneme.toplay.track.util.CalorieUtils;
import com.oneme.toplay.track.util.CalorieUtils.ActivityType;
import com.oneme.toplay.track.util.LocationUtils;
import com.oneme.toplay.track.util.PreferencesUtils;
import com.oneme.toplay.track.util.StatsUtils;
import com.oneme.toplay.track.util.TrackIconUtils;
import com.oneme.toplay.track.util.UnitConversions;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * A fragment to display track statistics to the user.
 * 
 * @author Sandor Dornbush
 * @author Rodrigo Damazio
 */
public class StatsNextFragment extends Fragment implements TrackDataListener {

  public static final String STATS_FRAGMENT_TAG = "statsFragment";

  // 1 second in milliseconds
  private static final long ONE_SECOND = (long) UnitConversions.S_TO_MS;

  private TrackDataHub trackDataHub;
  private Handler handler;

  private Location lastLocation = null;
  private TripStatistics lastTripStatistics = null;
  private String category = "";
  private int recordingGpsAccuracy = PreferencesUtils.RECORDING_GPS_ACCURACY_DEFAULT;

  public static final String CHART_FRAGMENT_TAG = "chartFragment";

  private final ArrayList<double[]> pendingPoints = new ArrayList<double[]>();

  //private TrackDataHub trackDataHub;

  // Stats gathered from the received data
  private TripStatisticsUpdater tripStatisticsUpdater;
  private long startTime;

  private boolean metricUnits = true;
  private boolean reportSpeed = true;
  private int recordingDistanceInterval = PreferencesUtils.RECORDING_DISTANCE_INTERVAL_DEFAULT;

  // Modes of operation
  private boolean chartByDistance = true;
  private boolean[] chartShow = new boolean[] { true, true, true, true, true, true };

  // UI elements
  private ChartView chartView;
 // private ZoomControls zoomControls;


  // A runnable to update the total time field.
  private final Runnable updateTotalTime = new Runnable() {
    public void run() {
      if (isResumed() && isSelectedTrackRecording()) {
        if (!isSelectedTrackPaused() && lastTripStatistics != null) {
          StatsUtils.setTotalTimeValue(getActivity(), System.currentTimeMillis()
              - lastTripStatistics.getStopTime() + lastTripStatistics.getTotalTime());
        }
        handler.postDelayed(this, ONE_SECOND);
      }
    }
  };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    /*
     * Create a chartView here to store data thus won't need to reload all the
     * data on every onStart or onResume.
     */
    chartView = new ChartView(getActivity());
  };


  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.ome_track_stats, container, false);
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    handler = new Handler();
   
    Spinner activityTypeIcon = (Spinner) getView().findViewById(R.id.stats_activity_type_icon);
    activityTypeIcon.setAdapter(TrackIconUtils.getIconSpinnerAdapter(getActivity(), ""));
    activityTypeIcon.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
          ((TrackDetailNextActivity) getActivity()).chooseActivityType(category);
        }
        return true;
      }
    });
    activityTypeIcon.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
          ((TrackDetailNextActivity) getActivity()).chooseActivityType(category);
        }
        return true;
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    resumeTrackDataHub();
    checkChartSettings();
    getActivity().runOnUiThread(updateChart);
    updateUi(getActivity());
    if (isSelectedTrackRecording()) {
      handler.post(updateTotalTime);
    }
  }



  @Override
  public void onPause() {
    super.onPause();
    pauseTrackDataHub();
    handler.removeCallbacks(updateTotalTime);
  }

  @Override
  public void onTrackUpdated(final Track track) {

    if (isResumed()) {
      if (track == null || track.getTripStatistics() == null) {
        startTime = -1L;
        return;
      }
      startTime = track.getTripStatistics().getStartTime();
    }

    if (isResumed()) {
      getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          if (isResumed()) {
            lastTripStatistics = track != null ? track.getTripStatistics() : null;
            category = track != null ? track.getCategory() : "";
            updateUi(getActivity());
          }
        }
      });
    }
  }

  @Override
  public void clearTrackPoints() {
    lastLocation = null;
    if (isResumed()) {
      tripStatisticsUpdater = startTime != -1L ? new TripStatisticsUpdater(startTime) : null;
      pendingPoints.clear();
      chartView.reset();
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          if (isResumed()) {
            chartView.resetScroll();
          }
        }
      });
    }
  }

  @Override
  public void onSampledInTrackPoint(Location location) {
    lastLocation = location;
    if (isResumed()) {
      double[] data = new double[ChartView.NUM_SERIES + 1];
      fillDataPoint(location, data);
      pendingPoints.add(data);
    }
  }

  @Override
  public void onSampledOutTrackPoint(Location location) {
    lastLocation = location;
    if (isResumed()) {
      fillDataPoint(location, null);
    }
  }

  @Override
  public void onSegmentSplit(Location location) {
    // We don't care.
    if (isResumed()) {
      fillDataPoint(location, null);
    }
  }

  @Override
  public void onNewTrackPointsDone() {

    if (isResumed()) {
      chartView.addDataPoints(pendingPoints);
      pendingPoints.clear();
      runOnUiThread(updateChart);
    }

    if (isResumed()) {
      getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          if (isResumed()) {
            if (!isSelectedTrackRecording() || isSelectedTrackPaused()) {
              lastLocation = null;
            }

            if (lastLocation != null) {
              boolean hasFix = !LocationUtils.isLocationOld(lastLocation);
              boolean hasGoodFix = lastLocation.hasAccuracy()
                      && lastLocation.getAccuracy() < recordingGpsAccuracy;

              if (!hasFix || !hasGoodFix) {
                lastLocation = null;
              }
            }
            StatsUtils.setLocationValues(
                    getActivity(), getActivity(), null, lastLocation, isSelectedTrackRecording());
          }
        }
      });
    }
  }

  @Override
  public void clearWaypoints() {
    // We don't care.
    if (isResumed()) {
      chartView.clearWaypoints();
    }
  }

  @Override
  public void onNewWaypoint(Waypoint waypoint) {
    // We don't care.
    if (isResumed() && waypoint != null && LocationUtils.isValidLocation(waypoint.getLocation())) {
      chartView.addWaypoint(waypoint);
    }
  }

  @Override
  public void onNewWaypointsDone() {
    // We don't care.
    if (isResumed()) {
      runOnUiThread(updateChart);
    }
  }

  @Override
  public boolean onMetricUnitsChanged(final boolean metric) {
    if (isResumed()) {
      if (metricUnits == metric) {
        return false;
      }
      metricUnits = metric;
      chartView.setMetricUnits(metricUnits);
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          if (isResumed()) {
            chartView.requestLayout();
            updateUi(getActivity());
          }
        }
      });
      return true;
    }
    return false;

    //if (isResumed()) {
    //  getActivity().runOnUiThread(new Runnable() {
    //    @Override
    //    public void run() {
    //      if (isResumed()) {
    //        updateUi(getActivity());
    //      }
    //    }
    //  });
    //}
    //return true;
  }

  @Override
  public boolean onReportSpeedChanged(final boolean speed) {
    if (isResumed()) {
      if (reportSpeed == speed) {
        return false;
      }
      reportSpeed = speed;
      chartView.setReportSpeed(reportSpeed);
      boolean chartShowSpeed = PreferencesUtils.getBoolean(
              getActivity(), R.string.chart_show_speed_key, PreferencesUtils.CHART_SHOW_SPEED_DEFAULT);
      setSeriesEnabled(ChartView.SPEED_SERIES, chartShowSpeed && reportSpeed);
      setSeriesEnabled(ChartView.PACE_SERIES, chartShowSpeed && !reportSpeed);
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          if (isResumed()) {
            chartView.requestLayout();
            updateUi(getActivity());
          }
        }
      });
      return true;
    }
    return false;

   // if (isResumed()) {
   //   getActivity().runOnUiThread(new Runnable() {
   //     @Override
   //     public void run() {
   //       if (isResumed()) {
   //         updateUi(getActivity());
   //       }
   //     }
   //   });
   // }
   // return true;
  }

  @Override
  public boolean onRecordingGpsAccuracy(int newValue) {
    recordingGpsAccuracy = newValue;
    return false;
  }

  @Override
  public boolean onRecordingDistanceIntervalChanged(int value) {
    // We don't care.
    if (isResumed()) {
      if (recordingDistanceInterval == value) {
        return false;
      }
      recordingDistanceInterval = value;
      return true;
    }
    return false;
  }

  @Override
  public boolean onMapTypeChanged(int mapType) {
    // We don't care
    return false;
  }

  /**
   * Resumes the trackDataHub. Needs to be synchronized because trackDataHub can
   * be accessed by multiple threads.
   */
  private synchronized void resumeTrackDataHub() {
    trackDataHub = ((TrackDetailNextActivity) getActivity()).getTrackDataHub();
    trackDataHub.registerTrackDataListener(this, EnumSet.of(TrackDataType.TRACKS_TABLE,
        TrackDataType.SAMPLED_IN_TRACK_POINTS_TABLE, TrackDataType.SAMPLED_OUT_TRACK_POINTS_TABLE,
        TrackDataType.PREFERENCE));
  }



  /**
   * Pauses the trackDataHub. Needs to be synchronized because trackDataHub can
   * be accessed by multiple threads.
   */
  private synchronized void pauseTrackDataHub() {
    trackDataHub.unregisterTrackDataListener(this);
    trackDataHub = null;
  }

  /**
   * Returns true if the selected track is recording. Needs to be synchronized
   * because trackDataHub can be accessed by multiple threads.
   */
  private synchronized boolean isSelectedTrackRecording() {
    return trackDataHub != null && trackDataHub.isSelectedTrackRecording();
  }


  /**
   * Returns true if the selected track is paused. Needs to be synchronized
   * because trackDataHub can be accessed by multiple threads.
   */
  private synchronized boolean isSelectedTrackPaused() {
    return trackDataHub != null && trackDataHub.isSelectedTrackPaused();
  }

  /**
   * Updates the UI.
   */
  private void updateUi(FragmentActivity activity) {
    ActivityType activityType = CalorieUtils.getActivityType(activity, category);
    String trackIconValue = TrackIconUtils.getIconValue(activity, category);
    StatsUtils.setTripStatisticsValues(
            activity, activity, null, lastTripStatistics, activityType, trackIconValue);
    StatsUtils.setLocationValues(
            activity, activity, null, lastLocation, isSelectedTrackRecording());
  }





  /**
   * A runnable that will enable/disable zoom controls and orange pointer as
   * appropriate and redraw.
   */
  private final Runnable updateChart = new Runnable() {
    @Override
    public void run() {
      if (!isResumed() || trackDataHub == null) {
        return;
      }

    //  zoomControls.setIsZoomInEnabled(chartView.canZoomIn());
    //  zoomControls.setIsZoomOutEnabled(chartView.canZoomOut());
      chartView.setShowPointer(isSelectedTrackRecording());
      chartView.invalidate();
    }
  };




  @Override
  public void onStart() {
    super.onStart();
    ViewGroup layout = (ViewGroup) getActivity().findViewById(R.id.chart_next_view_layout);
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    layout.addView(chartView, layoutParams);
  }





  @Override
  public void onStop() {
    super.onStop();
    ViewGroup layout = (ViewGroup) getActivity().findViewById(R.id.chart_next_view_layout);
    layout.removeView(chartView);
  }


  /**
   * Checks the chart settings.
   */
  private void checkChartSettings() {
    boolean needUpdate = false;
    if (chartByDistance != PreferencesUtils.isChartByDistance(getActivity())) {
      chartByDistance = !chartByDistance;
      chartView.setChartByDistance(chartByDistance);
      reloadTrackDataHub();
      needUpdate = true;
    }
    if (setSeriesEnabled(ChartView.ELEVATION_SERIES, PreferencesUtils.getBoolean(getActivity(),
            R.string.chart_show_elevation_key, PreferencesUtils.CHART_SHOW_ELEVATION_DEFAULT))) {
      needUpdate = true;
    }

    boolean chartShowSpeed = PreferencesUtils.getBoolean(
            getActivity(), R.string.chart_show_speed_key, PreferencesUtils.CHART_SHOW_SPEED_DEFAULT);
    if (setSeriesEnabled(ChartView.SPEED_SERIES, chartShowSpeed && reportSpeed)) {
      needUpdate = true;
    }
    if (setSeriesEnabled(ChartView.PACE_SERIES, chartShowSpeed && !reportSpeed)) {
      needUpdate = true;
    }
    if (setSeriesEnabled(ChartView.POWER_SERIES, PreferencesUtils.getBoolean(
            getActivity(), R.string.chart_show_power_key, PreferencesUtils.CHART_SHOW_POWER_DEFAULT))) {
      needUpdate = true;
    }
    if (setSeriesEnabled(ChartView.CADENCE_SERIES, PreferencesUtils.getBoolean(getActivity(),
            R.string.chart_show_cadence_key, PreferencesUtils.CHART_SHOW_CADENCE_DEFAULT))) {
      needUpdate = true;
    }
    if (setSeriesEnabled(ChartView.HEART_RATE_SERIES, PreferencesUtils.getBoolean(getActivity(),
            R.string.chart_show_heart_rate_key, PreferencesUtils.CHART_SHOW_HEART_RATE_DEFAULT))) {
      needUpdate = true;
    }
    if (needUpdate) {
      chartView.postInvalidate();
    }
  }

  /**
   * Sets the series enabled value.
   *
   * @param index the series index
   * @param value the value
   * @return true if changed
   */
  private boolean setSeriesEnabled(int index, boolean value) {
    if (chartShow[index] != value) {
      chartShow[index] = value;
      chartView.setChartValueSeriesEnabled(index, value);
      return true;
    } else {
      return false;
    }
  }




  /**
   * Reloads the trackDataHub. Needs to be synchronized because trackDataHub can
   * be accessed by multiple threads.
   */
  private synchronized void reloadTrackDataHub() {
    if (trackDataHub != null) {
      trackDataHub.reloadDataForListener(this);
    }
  }

  /**
   * To zoom in.
   */
  private void zoomIn() {
    //chartView.zoomIn();
    //zoomControls.setIsZoomInEnabled(chartView.canZoomIn());
    //zoomControls.setIsZoomOutEnabled(chartView.canZoomOut());
  }

  /**
   * To zoom out.
   */
  private void zoomOut() {
   // chartView.zoomOut();
   // zoomControls.setIsZoomInEnabled(chartView.canZoomIn());
   // zoomControls.setIsZoomOutEnabled(chartView.canZoomOut());
  }

  /**
   * Runs a runnable on the UI thread if possible.
   *
   * @param runnable the runnable
   */
  private void runOnUiThread(Runnable runnable) {
    FragmentActivity fragmentActivity = getActivity();
    if (fragmentActivity != null) {
      fragmentActivity.runOnUiThread(runnable);
    }
  }

  /**
   * Given a location, fill in a data point, an array of double[]. <br>
   * data[0] = time/distance <br>
   * data[1] = elevation <br>
   * data[2] = speed <br>
   * data[3] = pace <br>
   * data[4] = heart rate <br>
   * data[5] = cadence <br>
   * data[6] = power <br>
   *
   * @param location the location
   * @param data the data point to fill in, can be null
   */
  //@VisibleForTesting
  void fillDataPoint(Location location, double data[]) {
    double timeOrDistance = Double.NaN;
    double elevation = Double.NaN;
    double speed = Double.NaN;
    double pace = Double.NaN;
    double heartRate = Double.NaN;
    double cadence = Double.NaN;
    double power = Double.NaN;

    if (tripStatisticsUpdater != null) {
      tripStatisticsUpdater.addLocation(
              location, recordingDistanceInterval, false, ActivityType.INVALID, 0.0);
      TripStatistics tripStatistics = tripStatisticsUpdater.getTripStatistics();
      if (chartByDistance) {
        double distance = tripStatistics.getTotalDistance() * UnitConversions.M_TO_KM;
        if (!metricUnits) {
          distance *= UnitConversions.KM_TO_MI;
        }
        timeOrDistance = distance;
      } else {
        timeOrDistance = tripStatistics.getTotalTime();
      }

      elevation = tripStatisticsUpdater.getSmoothedElevation();
      if (!metricUnits) {
        elevation *= UnitConversions.M_TO_FT;
      }

      speed = tripStatisticsUpdater.getSmoothedSpeed() * UnitConversions.MS_TO_KMH;
      if (!metricUnits) {
        speed *= UnitConversions.KM_TO_MI;
      }
      pace = speed == 0 ? 0.0 : 60.0 / speed;
    }

    if (data != null) {
      data[0] = timeOrDistance;
      data[1] = elevation;
      data[2] = speed;
      data[3] = pace;
      data[4] = heartRate;
      data[5] = cadence;
      data[6] = power;
    }
  }

  //@VisibleForTesting
  ChartView getChartView() {
    return chartView;
  }

  //@VisibleForTesting
  void setTripStatisticsUpdater(long time) {
    tripStatisticsUpdater = new TripStatisticsUpdater(time);
  }

  //@VisibleForTesting
  void setChartView(ChartView view) {
    chartView = view;
  }

  //@VisibleForTesting
  void setMetricUnits(boolean value) {
    metricUnits = value;
  }

  //@VisibleForTesting
  void setReportSpeed(boolean value) {
    reportSpeed = value;
  }

  //@VisibleForTesting
  void setChartByDistance(boolean value) {
    chartByDistance = value;
  }
}
