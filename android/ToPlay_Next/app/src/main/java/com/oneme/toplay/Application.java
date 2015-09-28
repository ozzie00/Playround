package com.oneme.toplay;

import android.content.Context;
import android.content.SharedPreferences;

import com.baidu.mapapi.SDKInitializer;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.BookingVenue;
import com.oneme.toplay.database.FollowingPlayer;
import com.oneme.toplay.database.Group;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.database.InviteComment;
import com.oneme.toplay.database.InviteLike;
import com.oneme.toplay.database.InviteScore;
import com.oneme.toplay.database.Message;
import com.oneme.toplay.database.PayPrime;
import com.oneme.toplay.database.Photo;
import com.oneme.toplay.database.PhotoLink;
import com.oneme.toplay.database.Player;
import com.oneme.toplay.database.ThirdRequest;
import com.oneme.toplay.database.Venue;
import com.oneme.toplay.database.VenueAsHome;
import com.oneme.toplay.database.VenueComment;
import com.oneme.toplay.database.VenueLike;
import com.oneme.toplay.database.VenueOwner;
import com.parse.Parse;
import com.parse.ParseObject;

public class Application extends android.app.Application {

    private static final String TAG      = "Application";

    // Debugging switch
    public static final boolean APPDEBUG = false;

    // Debugging tag for the application
    public static final String APPTAG    = "Playround";

    // Used to pass location from MainActivity to InviteActivity, LocalActivity
    public static final String INTENT_EXTRA_LOCATION     = AppConstant.OMEPARSELOCATIONKEY;

    // Used to pass location from MainActivity to InviteActivity, LocalActivity

   // public static final ParseUser INTENT_EXTRA_USER      = AppConstant.OMEPARSEUSERKEY;

    public static final String INTENT_EXTRA_USEROBJECTID     = AppConstant.OMETOPLAYINVITECLASSKEY;

    public static final String INTENT_EXTRA_USERNAME         = AppConstant.OMEPARSEUSERNAMEKEY;

    public static final String INTENT_EXTRA_USERPHONE        = AppConstant.OMEPARSEUSERPHONEKEY;

    public static final String INTENT_EXTRA_USEROMEID        = AppConstant.OMEPARSEUSEROMEIDKEY;

    public static final String INTENT_EXTRA_USERLEVEL        = AppConstant.OMEPARSEUSERLEVELKEY;

    public static final String INTENT_EXTRA_USERICONPATH     = AppConstant.OMEPARSEUSERICONKEY;

    public static final String INTENT_EXTRA_SPORTTYPE        = AppConstant.OMEPARSEINVITESPORTTYPEKEY;

    public static final String INTENT_EXTRA_SPORTTYPEVALUE   = AppConstant.OMEPARSEINVITESPORTTYPEVALUEKEY;

    public static final String INTENT_EXTRA_PLAYERNUMBER     = AppConstant.OMEPARSEINVITEPLAYERNUMBERKEY;

    public static final String INTENT_EXTRA_PLAYERLEVEL      = AppConstant.OMEPARSEINVITEPLAYERLEVELKEY;

    public static final String INTENT_EXTRA_TIME             = AppConstant.OMEPARSEINVITETIMEKEY;

    public static final String INTENT_EXTRA_COURT            = AppConstant.OMEPARSEINVITECOURTKEY;

    public static final String INTENT_EXTRA_FEE              = AppConstant.OMEPARSEINVITEFEEKEY;

    public static final String INTENT_EXTRA_OTHER            = AppConstant.OMEPARSEINVITEOTHERINFOKEY;

    public static final String INTENT_EXTRA_FROMUSER         = AppConstant.OMEPARSEINVITEFROMUSERKEY;

    public static final String INTENT_EXTRA_FROMUSERNAME     = AppConstant.OMEPARSEINVITEFROMUSERNAMEKEY;

    public static final String INTENT_EXTRA_SUBMITTIME       = AppConstant.OMEPARSEINVITESUBMITTIMEKEY;

    public static final String INTENT_EXTRA_INVITEOBJECTID   = AppConstant.OMEPARSEMESSAGEFOROBJECTIDKEY;

    public static final String INTENT_EXTRA_VENUESEARCH      = AppConstant.OMETOPLAYVENUESEARCHKEY;

    public static final String INTENT_EXTRA_SEARCHLOCATION   = AppConstant.OMEPARSEINVITECOURTKEY;

    public static final String INTENT_EXTRA_WORKOUTNAME      = AppConstant.OMEPARSEINVITEWORKNAMEKEY;

    public static final String INTENT_EXTRA_VENUE            = AppConstant.OMETOPLAYVENUENAMEKEY;

    public static final String INTENT_EXTRA_VENUELEVEL       = AppConstant.OMETOPLAYVENUELEVELKEY;

    public static final String INTENT_EXTRA_VENUETYPE        = AppConstant.OMETOPLAYVENUETYPEKEY;

    public static final String INTENT_EXTRA_VENUEADDRESS     = AppConstant.OMETOPLAYVENUEADDRESSKEY;

    public static final String INTENT_EXTRA_VENUEPHONE       = AppConstant.OMETOPLAYVENUEPHONEKEY;

    public static final String INTENT_EXTRA_VENUECOURTNUMBER = AppConstant.OMETOPLAYVENUECOURTNUMBERKEY;

    public static final String INTENT_EXTRA_VENUELIGHTED     = AppConstant.OMETOPLAYVENUELIGHTEDKEY;

    public static final String INTENT_EXTRA_VENUEINDOOR      = AppConstant.OMETOPLAYVENUEINDOORKEY;

    public static final String INTENT_EXTRA_VENUEPUBLIC      = AppConstant.OMETOPLAYVENUEPUBLICKEY;

    public static final String INTENT_EXTRA_VENUEPAYNO       = AppConstant.OMEPARSEBOOKINGPAYNUMBERKEY;

    public static final String INTENT_EXTRA_VENUEBUSINESS    = AppConstant.OMETOPLAYVENUEBUSINESSKEY;

    public static final String INTENT_EXTRA_VENUEPRICE       = AppConstant.OMETOPLAYVENUEPRICEKEY;

    public static final String INTENT_EXTRA_VENUEPRIMEINFO   = AppConstant.OMETOPLAYVENUEPRIMEINFOKEY;

    public static final String INTENT_EXTRA_VENUEJSON3RD     = AppConstant.OMETOPLAYVENUEJSON3RD;

    public static final String INTENT_EXTRA_VENUEJSONNAMEID  = AppConstant.OMETOPLAYVENUEJSONNAMEID;

    public static final String INTENT_EXTRA_VENUEJSONNAME    = AppConstant.OMETOPLAYVENUEJSONNAME;

    public static final String INTENT_EXTRA_VENUEJSONCURRENCY= AppConstant.OMETOPLAYVENUEJSONCURRENCY;

    public static final String INTENT_EXTRA_VENUEJSONCARDID  = AppConstant.OMETOPLAYVENUEJSONCARDID;

    public static final String INTENT_EXTRA_VENUEJSONCARDNAME= AppConstant.OMETOPLAYVENUEJSONCARDNAME;

    public static final String INTENT_EXTRA_VENUEJSONCARDPRICE= AppConstant.OMETOPLAYVENUEJSONCARDPRICE;

    public static final String INTENT_EXTRA_PRIMEPAYNO       = AppConstant.OMEPARSEPRIMEPAYNUMBERKEY;

    public static final String INTENT_EXTRA_LATITUDE         = "TOPLAYLATITUDE";

    public static final String INTENT_EXTRA_LONGITUDE        = "TOPLAYLONGITUDE";


    // Key for saving the search distance preference
    private static final String KEY_SEARCH_DISTANCE    = "searchDistance";

    // Key for saving current location
    private static final String KEY_CURRENT_LONGITUDE  = "currentLongitude";

    // Key for saving current location
    private static final String KEY_CURRENT_LATITUDE   = "currentLatitude";

    //Ozzie Zhang change default search distance from 250.0f to 6560.0f
    private static final float DEFAULT_SEARCH_DISTANCE = 30*6560.0f;

    public static double CURRENT_LATITUDE              = 39.989231;

    public static double CURRENT_LONGITUDE             = 116.321337;

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

        ParseObject.registerSubclass(InviteComment.class);

        ParseObject.registerSubclass(InviteLike.class);

        ParseObject.registerSubclass(InviteScore.class);

        ParseObject.registerSubclass(Group.class);

        ParseObject.registerSubclass(FollowingPlayer.class);

        ParseObject.registerSubclass(VenueComment.class);

        ParseObject.registerSubclass(VenueLike.class);

        ParseObject.registerSubclass(VenueAsHome.class);

        ParseObject.registerSubclass(BookingVenue.class);

        ParseObject.registerSubclass(Photo.class);

        ParseObject.registerSubclass(PhotoLink.class);

        ParseObject.registerSubclass(PayPrime.class);

        ParseObject.registerSubclass(ThirdRequest.class);

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
        String mlatitude = preferences.getString(KEY_CURRENT_LATITUDE, Double.toString(CURRENT_LATITUDE));

        if (mlatitude == null) {
            mlatitude = Double.toString(CURRENT_LATITUDE);
        }
        return mlatitude;
    }

    public static String getCurrentLongitude() {
        String mlongitude = preferences.getString(KEY_CURRENT_LONGITUDE, Double.toString(CURRENT_LONGITUDE));

        if (mlongitude == null) {
            mlongitude = Double.toString(CURRENT_LONGITUDE);
        }
        return mlongitude;
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
