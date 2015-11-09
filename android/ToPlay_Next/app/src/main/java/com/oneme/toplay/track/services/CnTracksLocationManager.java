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

package com.oneme.toplay.track.services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
//import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;

//import com.google.android.gms.location.LocationClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;

import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;



import com.oneme.toplay.track.util.GoogleLocationUtils;

/**
 * My Tracks Location Manager. Applies Google location settings before allowing
 * access to {@link LocationManager}.
 * 
 * @author Jimmy Shih
 */
public class CnTracksLocationManager {

  /**
   * Observer for Google location settings.
   *
   * @author Jimmy Shih
   */
  private class GoogleSettingsObserver extends ContentObserver {

    public GoogleSettingsObserver(Handler handler) {
      super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
      isAllowed = GoogleLocationUtils.isAllowed(context);
    }
  }



  private final Context context;
  private final Handler handler;
  private final LocationClient bdlocationClient;
  private final LocationManager locationManager;
  private final ContentResolver contentResolver;
  private final GoogleSettingsObserver observer;

  private boolean isAllowed;
  private BDLocationListener requestLastLocationListener;
  private BDLocationListener requestLocationUpdatesListener;
  private float requestLocationUpdatesDistance;
  private long requestLocationUpdatesTime;

  boolean isFirstLoc = true;

  public MyLocationListener myListener = new MyLocationListener();

  public CnTracksLocationManager(Context context, Looper looper, boolean enableLocaitonClient) {
    this.context = context;
    this.handler = new Handler(looper);

    if (enableLocaitonClient) {
      bdlocationClient = new LocationClient(context);
      bdlocationClient.registerLocationListener(myListener);
      LocationClientOption option = new LocationClientOption();
      option.setOpenGps(true);
      option.setCoorType("bd09ll");
      option.setScanSpan(1000*60*1);
      bdlocationClient.setLocOption(option);
      bdlocationClient.start();
    } else {
      bdlocationClient = null;
    }

    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    contentResolver = context.getContentResolver();
    observer = new GoogleSettingsObserver(handler);

    isAllowed = GoogleLocationUtils.isAllowed(context);

    contentResolver.registerContentObserver(
        GoogleLocationUtils.USE_LOCATION_FOR_SERVICES_URI, false, observer);
  }

  /**
   * Closes the {@link CnTracksLocationManager}.
   */
  public void close() {
    if (bdlocationClient != null) {
      bdlocationClient.stop();
    }
    contentResolver.unregisterContentObserver(observer);
  }

  /**
   * Returns true if allowed to access the location manager. Returns true if
   * there is no enforcement or the Google location settings allows access to
   * location data.
   */
  public boolean isAllowed() {
    return isAllowed;
  }

  /**
   * Returns true if gps provider is enabled.
   */
  public boolean isGpsProviderEnabled() {
    if (!isAllowed()) {
      return false;
    }
    String provider = LocationManager.GPS_PROVIDER;
    if (locationManager.getProvider(provider) == null) {
      return false;
    }
    return locationManager.isProviderEnabled(provider);
  }

  /**
   * Request last location.
   * 
   * @param bdlocationListener location listener
   */
  public void requestLastLocation(final BDLocationListener bdlocationListener) {
    handler.post(new Runnable() {
        @Override
      public void run() {
        if (!isAllowed()) {
          requestLastLocationListener = null;
          bdlocationListener.onReceiveLocation(null);
        } else {
          requestLastLocationListener = bdlocationListener;
          bdlocationClient.getLastKnownLocation();
        }
      }
    });
  }

  /**
   * Requests location updates. This is an ongoing request, thus the caller
   * needs to check the status of {@link #isAllowed}.
   * 
   * @param minTime the minimal time
   * @param minDistance the minimal distance
   * @param bdlocationListener the location listener
   */
  public void requestLocationUpdates(
      final long minTime, final float minDistance, final BDLocationListener bdlocationListener) {
    handler.post(new Runnable() {
        @Override
      public void run() {
        requestLocationUpdatesTime       = minTime;
        requestLocationUpdatesDistance   = minDistance;
          requestLocationUpdatesListener = bdlocationListener;
          bdlocationClient.requestLocation();
      }
    });
  }

  /**
   * Removes location updates.
   * 
   * @param bdlocationListener the location listener
   */
  public void removeLocationUpdates(final BDLocationListener bdlocationListener) {
    handler.post(new Runnable() {
        @Override
      public void run() {
        requestLocationUpdatesListener = null;
        if (bdlocationClient != null && bdlocationClient.isStarted()) {
          bdlocationClient.unRegisterLocationListener(bdlocationListener);
        }
      }
    });
  }

  /**
   *  locationListener SDK
   */
  public class MyLocationListener implements BDLocationListener {

    @Override
    public void onReceiveLocation(BDLocation bdlocation) {


      if (bdlocation == null)
        return;
      MyLocationData locData = new MyLocationData.Builder()
              .accuracy(bdlocation.getRadius())
                      // get directory 0-360
              .direction(100).latitude(bdlocation.getLatitude())
              .longitude(bdlocation.getLongitude()).build();
      if (isFirstLoc) {
        isFirstLoc = false;
        LatLng ll = new LatLng(bdlocation.getLatitude(),
                bdlocation.getLongitude());
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
      }
    }

    public void onReceivePoi(BDLocation poiLocation) {
    }
  }

}
