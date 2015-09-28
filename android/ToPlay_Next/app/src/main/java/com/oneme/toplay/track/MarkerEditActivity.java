/*
 * Copyright 2012 Google Inc.
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

package com.oneme.toplay.track;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.oneme.toplay.R;
import com.oneme.toplay.track.content.TracksProviderUtils;
import com.oneme.toplay.track.content.Track;
import com.oneme.toplay.track.content.Waypoint;
import com.oneme.toplay.track.content.Waypoint.WaypointType;
import com.oneme.toplay.track.content.WaypointCreationRequest;
import com.oneme.toplay.track.services.TrackRecordingServiceConnection;
import com.oneme.toplay.track.util.PreferencesUtils;
import com.oneme.toplay.track.util.TrackRecordingServiceConnectionUtils;
import com.oneme.toplay.ui.BaseActivity;

/**
 * An activity to add/edit a marker.
 *
 * @author Jimmy Shih
 */
public class MarkerEditActivity extends BaseActivity {

  private static final String TAG = MarkerEditActivity.class.getSimpleName();

  public static final String EXTRA_TRACK_ID = "track_id";
  public static final String EXTRA_MARKER_ID = "marker_id";

  private long trackId;
  private long markerId;
  private TrackRecordingServiceConnection trackRecordingServiceConnection;
  private Waypoint waypoint;

  // UI elements
  private View statisticsSection;
  private EditText statisticsName;
  private View waypointSection;
  private EditText waypointName;
  private AutoCompleteTextView waypointMarkerType;
  private EditText waypointDescription;
  private Button done;

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);

    setContentView(R.layout.marker_edit);

    // getActionBar().show();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Toolbar toolbar = getActionBarToolbar();
    toolbar.setNavigationIcon(R.drawable.ic_up);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
        //navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(JoinNextActivity.this,
        //        LocalNextActivity.class)));
      }
    });

    trackId = getIntent().getLongExtra(EXTRA_TRACK_ID, -1L);
    markerId = getIntent().getLongExtra(EXTRA_MARKER_ID, -1L);
    trackRecordingServiceConnection = new TrackRecordingServiceConnection(this, null);
        
    // Setup UI elements
    statisticsSection = findViewById(R.id.marker_edit_statistics_section);
    statisticsName = (EditText) findViewById(R.id.marker_edit_statistics_name);
    
    waypointSection = findViewById(R.id.marker_edit_waypoint_section);
    waypointName = (EditText) findViewById(R.id.marker_edit_waypoint_name);   
    waypointMarkerType = (AutoCompleteTextView) findViewById(R.id.marker_edit_waypoint_marker_type);
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        this, R.array.waypoint_types, android.R.layout.simple_dropdown_item_1line);
    waypointMarkerType.setAdapter(adapter);
    waypointDescription = (EditText) findViewById(R.id.marker_edit_waypoint_description);

    Button cancel = (Button) findViewById(R.id.marker_edit_cancel);
    cancel.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    done = (Button) findViewById(R.id.marker_edit_done);
    updateUiByMarkerId();
  }

  @Override
  protected void onStart() {
    super.onStart();
    TrackRecordingServiceConnectionUtils.startConnection(this, trackRecordingServiceConnection);
  }
  
  @Override
  protected void onStop() {
    super.onStop();
    trackRecordingServiceConnection.unbind();
  }

  //@Override
  //protected int getLayoutResId() {
  //  return R.layout.marker_edit;
  //}

  /**
   * Updates the UI based on the marker id.
   */
  private void updateUiByMarkerId() {
    final boolean newMarker = markerId == -1L;
  
    setTitle(newMarker ? R.string.menu_insert_marker : R.string.menu_edit);
    done.setText(newMarker ? R.string.generic_add : R.string.generic_save);
    done.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (newMarker) {
          addMarker();
        } else {
          saveMarker();
        }
        boolean driveSync = PreferencesUtils.getBoolean(
            MarkerEditActivity.this, R.string.drive_sync_key, PreferencesUtils.DRIVE_SYNC_DEFAULT);
        if (driveSync) {
          TracksProviderUtils myTracksProviderUtils = TracksProviderUtils.Factory.get(
              MarkerEditActivity.this);
          Track track = myTracksProviderUtils.getTrack(newMarker ? trackId : waypoint.getTrackId());
          track.setModifiedTime(System.currentTimeMillis());
          myTracksProviderUtils.updateTrack(track);
          PreferencesUtils.addToList(MarkerEditActivity.this, R.string.drive_edited_list_key,
              PreferencesUtils.DRIVE_EDITED_LIST_DEFAULT, String.valueOf(track.getId()));
        }      
        finish();
      }
    });
  
    if (newMarker) {
      statisticsSection.setVisibility(View.GONE);
      waypointSection.setVisibility(View.VISIBLE);
      int nextWaypointNumber = trackId == -1L ? -1
          : TracksProviderUtils.Factory.get(this).getNextWaypointNumber(trackId, WaypointType.WAYPOINT);
      if (nextWaypointNumber == -1) {
        nextWaypointNumber = 0;
      }
      waypointName.setText(getString(R.string.marker_name_format, nextWaypointNumber));
      waypointName.selectAll();
      waypointMarkerType.setText("");
      waypointDescription.setText("");
    } else {
      waypoint = TracksProviderUtils.Factory.get(this).getWaypoint(markerId);
      if (waypoint == null) {
        //Log.d(TAG, "waypoint is null");
        finish();
        return;
      }
      boolean statistics = waypoint.getType() == WaypointType.STATISTICS;
      statisticsSection.setVisibility(statistics ? View.VISIBLE : View.GONE);
      waypointSection.setVisibility(statistics ? View.GONE : View.VISIBLE);
      if (statistics) {
        statisticsName.setText(waypoint.getName());
      } else {
        waypointName.setText(waypoint.getName());
        waypointMarkerType.setText(waypoint.getCategory());
        waypointDescription.setText(waypoint.getDescription());
      }  
    }
  }

  /**
   * Adds a marker.
   */
  private void addMarker() {
    WaypointCreationRequest waypointCreationRequest = new WaypointCreationRequest(
        WaypointType.WAYPOINT,
        false,
        waypointName.getText().toString(),
        waypointMarkerType.getText().toString(),
        waypointDescription.getText().toString(),
        null,
        null);
    TrackRecordingServiceConnectionUtils.addMarker(
        this, trackRecordingServiceConnection, waypointCreationRequest);
  }

  /**
   * Saves a marker.
   */
  private void saveMarker() {
    boolean statistics = waypoint.getType() == WaypointType.STATISTICS;
    if (statistics) {
      waypoint.setName(statisticsName.getText().toString());
    } else {
      waypoint.setName(waypointName.getText().toString());
      waypoint.setCategory(waypointMarkerType.getText().toString());
      waypoint.setDescription(waypointDescription.getText().toString());
    }
    TracksProviderUtils.Factory.get(this).updateWaypoint(waypoint);
  }
}
