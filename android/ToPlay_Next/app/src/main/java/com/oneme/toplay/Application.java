package com.oneme.toplay;




import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;


import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.database.Message;
import com.oneme.toplay.database.Player;
import com.oneme.toplay.database.Venue;
import com.oneme.toplay.database.VenueOwner;
import com.oneme.toplay.service.Singleton;

import com.parse.Parse;
import com.parse.ParseObject;

import com.baidu.mapapi.SDKInitializer;

public class Application extends android.app.Application {

    private static final String TAG      = "Application";

    // Debugging switch
    public static final boolean APPDEBUG = false;

    // Debugging tag for the application
    public static final String APPTAG    = "Playround";

    // Used to pass location from MainActivity to InviteActivity, LocalActivity
    public static final String INTENT_EXTRA_LOCATION     = AppConstant.OMELOCATIONKEY;

    // Used to pass location from MainActivity to InviteActivity, LocalActivity

   // public static final ParseUser INTENT_EXTRA_USER      = AppConstant.OMEPARSEUSERKEY;

    public static final String INTENT_EXTRA_USEROBJECTID = AppConstant.OMETOPLAYINVITECLASSKEY;

    public static final String INTENT_EXTRA_USERNAME     = AppConstant.OMEPARSEUSERNAMEKEY;

    public static final String INTENT_EXTRA_USEROMEID    = AppConstant.OMEPARSEUSEROMEIDKEY;

    public static final String INTENT_EXTRA_USERLEVEL    = AppConstant.OMEPARSEUSERLEVELKEY;

    public static final String INTENT_EXTRA_USERICONPATH = AppConstant.OMEPARSEUSERICONKEY;

    public static final String INTENT_EXTRA_SPORTTYPE    = AppConstant.OMEPARSEINVITESPORTTYPEKEY;

    public static final String INTENT_EXTRA_SPORTTYPEVALUE= AppConstant.OMEPARSEINVITESPORTTYPEVALUEKEY;

    public static final String INTENT_EXTRA_PLAYERNUMBER = AppConstant.OMEPARSEINVITEPLAYERNUMBERKEY;

    public static final String INTENT_EXTRA_PLAYERLEVEL  = AppConstant.OMEPARSEINVITEPLAYERLEVELKEY;

    public static final String INTENT_EXTRA_TIME         = AppConstant.OMEPARSEINVITETIMEKEY;

    public static final String INTENT_EXTRA_COURT        = AppConstant.OMEPARSEINVITECOURTKEY;

    public static final String INTENT_EXTRA_FEE          = AppConstant.OMEPARSEINVITEFEEKEY;

    public static final String INTENT_EXTRA_OTHER        = AppConstant.OMEPARSEINVITEOTHERINFOKEY;

    public static final String INTENT_EXTRA_FROMUSER     = AppConstant.OMEPARSEINVITEFROMUSERKEY;

    public static final String INTENT_EXTRA_FROMUSERNAME = AppConstant.OMEPARSEINVITEFROMUSERNAMEKEY;

    public static final String INTENT_EXTRA_SUBMITTIME   = AppConstant.OMEPARSEINVITESUBMITTIMEKEY;

    public static final String INTENT_EXTRA_INVITEOBJECTID = AppConstant.OMEPARSEMESSAGEFOROBJECTIDKEY;

    public static final String INTENT_EXTRA_HOMEVENUE      = AppConstant.OMEPARSEUSERHOMEVENUEKEY;

    public static final String INTENT_EXTRA_BACKUPVENUE    = AppConstant.OMEPARSEUSERBACKUPVENUEKEY;

    public static final String INTENT_EXTRA_HOMEVENUEPHONE   = AppConstant.OMEPARSEUSERHOMEVENUEPHONEKEY;

    public static final String INTENT_EXTRA_BACKUPVENUEPHONE = AppConstant.OMEPARSEUSERBACKUPVENUEPHONEKEY;

    public static final String INTENT_EXTRA_VENUESEARCH      = AppConstant.OMETOPLAYVENUESEARCHKEY;






    // Key for saving the search distance preference
    private static final String KEY_SEARCH_DISTANCE    = "searchDistance";

    // Key for saving current location
    private static final String KEY_CURRENT_LONGITUDE  = "currentLongitude";

    // Key for saving current location
    private static final String KEY_CURRENT_LATITUDE   = "currentLatitude";

    //Ozzie Zhang change default search distance from 250.0f to 6560.0f
    private static final float DEFAULT_SEARCH_DISTANCE = 3*6560.0f;

    private static double CURRENT_LONGITUDE            = 42.3598f;

    private static double CURRENT_LATITUDE             = 71.0921f;

    private static SharedPreferences preferences;

    private static ConfigHelper configHelper;

   // private final Singleton mSingleton = Singleton.getInstance();

    public Application() {
    }

    @Override
    public void onCreate() {
        super.onCreate();


        ParseObject.registerSubclass(Invite.class);

        ParseObject.registerSubclass(Message.class);

        ParseObject.registerSubclass(Player.class);

        ParseObject.registerSubclass(Venue.class);

        ParseObject.registerSubclass(VenueOwner.class);

        // init baidu map
        SDKInitializer.initialize(getApplicationContext());

        // Ozzie Zhang 2014-12-31 change this code
        // this --> getApplicationContext()
        // Application ID and Client Key
        Parse.initialize(getApplicationContext(), AppConstant.OMETOPLAYPARSEAPPLICATIONID,
                AppConstant.OMETOPLAYPARSECLIENTKEY);

        preferences = getSharedPreferences(AppConstant.OMETOPLAYPACKAGENAME, Context.MODE_PRIVATE);

        configHelper = new ConfigHelper();
        configHelper.fetchConfigIfNeeded();



        /*

        //Initialize the RxJava Subjects in tox singleton;
        mSingleton.initSubjects(this);

        //Update lists
        mSingleton.updateFriendsList(this);
        mSingleton.updateLastMessageMap(this);
        mSingleton.updateUnreadCountMap(this);

        Database mDatabase = new Database(getApplicationContext());
        mDatabase.clearFileNumbers();
        mDatabase.close();

        updateLeftPane();

        */



    }

    public void updateLeftPane() {
     //   mSingleton.updateFriendRequests(getApplicationContext());
     //   mSingleton.updateFriendsList(getApplicationContext());
     //   mSingleton.updateMessages(getApplicationContext());
    }

    public static float getSearchDistance() {
        return preferences.getFloat(KEY_SEARCH_DISTANCE, DEFAULT_SEARCH_DISTANCE);
    }

    public static String getCurrentLatitude() {
        return preferences.getString(KEY_CURRENT_LATITUDE, Double.toString(CURRENT_LATITUDE));
    }

    public static String getCurrentLongitude() {
        return preferences.getString(KEY_CURRENT_LONGITUDE, Double.toString(CURRENT_LONGITUDE));
    }

    public static ConfigHelper getConfigHelper() {
    return configHelper;
  }

    public static void setSearchDistance(float value) {
        preferences.edit().putFloat(KEY_SEARCH_DISTANCE, value).commit();
    }

    public static void setCurrentLatitude(String value) {
        preferences.edit().putString(KEY_CURRENT_LATITUDE, value).commit();
    }

    public static void setCurrentLongitude(String value) {
        preferences.edit().putString(KEY_CURRENT_LONGITUDE, value).commit();
    }


}
