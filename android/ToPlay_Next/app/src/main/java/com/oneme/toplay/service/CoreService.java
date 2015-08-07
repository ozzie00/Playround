package com.oneme.toplay.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;

import com.oneme.toplay.Application;

public class CoreService { // extends Service {
    /*

    private Singleton mSingleton = Singleton.getInstance();

    public CoreService() {
        super();
    }

    private Thread serviceThread;
    private boolean keepRunning = true;

    @Override
    public void onCreate() {
        if(!mSingleton.isInited) {
            mSingleton.init(getApplicationContext());
            if (Application.APPDEBUG) {
                Log.d("CoreService", "Initting Singleton");
            }
        }

        //Ozzie Zhang 2014-12-04 disable this code to false
        keepRunning = true;

        Runnable start = new Runnable() {
            @Override
            public void run() {
                while(keepRunning) {
                    final SharedPreferences preferences   = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    final ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    final boolean wifiOnly                = preferences.getBoolean("wifi_only", true);
                    final NetworkInfo mWifi               = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    if(wifiOnly && !mWifi.isConnected()) {

                        try {
                            // Sleep for 10 seconds before checking again
                            Thread.sleep(10000);
                        } catch (Exception e) {
                        }

                    } else {

                        try {
                            Thread.sleep(mSingleton.jTox.doToxInterval());
                            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                            WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                                    "MyWakelockTag");
                            wakeLock.acquire();
                            mSingleton.jTox.doTox();
                            wakeLock.release();
                        } catch (Exception e) {

                        }

                    }
                }
            }
        };

        //Ozzie Zhang 2014-12-09 disable these code
        serviceThread = new Thread(start);
        serviceThread.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int id) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        keepRunning        = false;
        serviceThread.interrupt();
        mSingleton.isInited = false;
        mSingleton          = null;
        if (Application.APPDEBUG) {
            Log.d("CoreService", "onDestroy() called");
        }
    }
    */
}
