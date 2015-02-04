package com.oneme.toplay;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.Database;
import com.oneme.toplay.base.BitmapManager;
import com.oneme.toplay.base.ClientFriend;
import com.oneme.toplay.base.Constants;
import com.oneme.toplay.base.Triple;
import com.oneme.toplay.addfriend.AddFriendActivity;
import com.oneme.toplay.service.Singleton;

import java.util.Locale;

//import im.tox.jtoxcore.ToxCallType;
//import im.tox.jtoxcore.ToxCodecSettings;
//import im.tox.jtoxcore.ToxException;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class MainActivity { // extends ActionBarActivity implements DialogID.DialogIDListener {

    /*
    public DrawerLayout pane;
    public View chat;
    public View request;

    private final Singleton mSingleton = Singleton.getInstance();

    Subscription activeKeySubscribe;
    Subscription chatActiveSubscribe;
    Subscription doClosePaneSubscribe;

    SharedPreferences preferences;

    //private static SharedPreferences preferences;

    private static ConfigHelper configHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pressing the volume keys will affect STREAM_MUSIC played from this app
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Check if a language has been set or not
        String language = preferences.getString(AppConstant.OMEPARSELANGUAGEKEY, "-1");
        if (language.equals("-1")) {
            SharedPreferences.Editor editor = preferences.edit();
            String currentLanguage          = getResources().getConfiguration().locale.getCountry().toLowerCase();
            editor.putString(AppConstant.OMEPARSELANGUAGEKEY, currentLanguage);
            editor.commit();
        } else {
            Locale locale        = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale        = locale;
            getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());
        }

        setContentView(R.layout.activity_main);

        //Ozzie Zhang 2014-12-08 disable this code
        getSupportActionBar().hide();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Fix for an android 4.1.x bug
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            );
        }

        // Check if connected to the Internet
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo     = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && !networkInfo.isConnected()) {
                //
            showAlertDialog(MainActivity.this, getString(R.string.main_no_internet),
                    getString(R.string.main_not_connected));
        }

        chat = (View) findViewById(R.id.fragment_chat);
        pane = (DrawerLayout) findViewById(R.id.slidingpane_layout);
        DrawerLayout.DrawerListener paneListener = new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (Application.APPDEBUG) {
                    Log.d("MainActivity", "Drawer listener, drawer open");
                }
                mSingleton.rightPaneActiveSubject.onNext(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (Application.APPDEBUG) {
                    Log.d("MainActivity", "Drawer listener, drawer closed");
                }
                mSingleton.rightPaneActiveSubject.onNext(false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        };
        pane.setDrawerListener(paneListener);

        mSingleton.mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Init Bitmap Manager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new BitmapManager();

        //Get epoch time for online/offline messages
        Constants.epoch = System.currentTimeMillis() / 1000; // Current time in seconds


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


    }

    public void updateLeftPane() {
        mSingleton.updateFriendRequests(getApplicationContext());
        mSingleton.updateFriendsList(getApplicationContext());
        mSingleton.updateMessages(getApplicationContext());
    }

    public void onClickAddFriend(View v) {
        Intent intent = new Intent(this, AddFriendActivity.class);
        startActivityForResult(intent, Constants.ADD_FRIEND_REQUEST_CODE);
    }

    public void onClickVoiceCallFriend(View v) {
        ToxCodecSettings toxCodecSettings = new ToxCodecSettings(ToxCallType.TYPE_AUDIO, 0, 0, 0, 64000, 20, 48000, 1);
        ClientFriend friend               = mSingleton.getFriend(mSingleton.activeKey);
        int userID                        = friend.getFriendnumber();
        try {
            mSingleton.jTox.avCall(userID, toxCodecSettings, 10);
        } catch (ToxException e) {
        }
    }

    public void onClickVideoCallFriend(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constants.ADD_FRIEND_REQUEST_CODE && resultCode==RESULT_OK){
            mSingleton.updateFriendsList(this);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mSingleton.activeKey = "";
    }

    @Override
    public void onResume(){
        super.onResume();
        preferences    = PreferenceManager.getDefaultSharedPreferences(this);
        doClosePaneSubscribe = mSingleton.doClosePaneSubject.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean close) {
                        if (close) {
                            pane.openDrawer(Gravity.RIGHT);
                        } else {
                            pane.closeDrawer(Gravity.RIGHT);
                        }
                    }
                });
        activeKeySubscribe = mSingleton.rightPaneActiveAndKeyAndIsFriendSubject.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Triple<Boolean, String, Boolean>>() {
                    @Override
                    public void call(Triple<Boolean, String, Boolean> rightPaneActiveAndActiveKeyAndIfFriend) {
                        boolean rightPaneActive = rightPaneActiveAndActiveKeyAndIfFriend.x;
                        String activeKey        = rightPaneActiveAndActiveKeyAndIfFriend.y;
                        boolean isFriend        = rightPaneActiveAndActiveKeyAndIfFriend.z;

                        if (Application.APPDEBUG) {
                            Log.d("activeKeySub", "oldkey: " + mSingleton.activeKey + " newkey: " + activeKey + " isfriend: " + isFriend);
                        }
                        if (activeKey.equals("")) {
                            chat.setVisibility(View.GONE);
                        } else {
                            if (!activeKey.equals(mSingleton.activeKey)) {
                                mSingleton.doClosePaneSubject.onNext(true);
                                if (isFriend) {
                                    chat.setVisibility(View.VISIBLE);
                                } else {
                                    chat.setVisibility(View.GONE);
                                }
                            }
                        }
                        mSingleton.activeKey = activeKey;
                        if (!activeKey.equals("") && rightPaneActive && isFriend) {
                            Database mDatabase = new Database(getApplicationContext());
                            mDatabase.markIncomingMessagesRead(activeKey);
                            mSingleton.clearUselessNotifications(activeKey);
                            mSingleton.updateMessages(getApplicationContext());
                            mDatabase.close();
                            mSingleton.chatActive = true;
                        } else {
                            mSingleton.chatActive = false;
                        }
                    }
                });

    }

    @Override
    public void onPause(){
        super.onPause();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getBoolean("beenLoaded", false)) {
            activeKeySubscribe.unsubscribe();
            doClosePaneSubscribe.unsubscribe();
            mSingleton.chatActive = false;
        }
    }

    void showAlertDialog(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if(pane.isDrawerOpen(Gravity.RIGHT))
            pane.closeDrawers();
        else
            finish();
    }

    // Needed for ID dialog in settings fragment
    @Override
    public void onDialogClick(DialogFragment fragment) {

    }

    // Method for copy button in settings fragment
    public void copyToxID(View view) {
        // Copy ID to clipboard
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Context context                     = getApplicationContext();
        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                .getSystemService(context.CLIPBOARD_SERVICE);
        clipboard.setText(sharedPreferences.getString(AppConstant.OMEPARSEUSERIDKEY, ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.ome_message_main_menu, menu);
        //MenuItem settingItem = menu.add(getResources().getString(R.string.meactivity_title));

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.message_main_action_add_person:
                Intent intent = new Intent(this, AddFriendActivity.class);
                startActivityForResult(intent, Constants.ADD_FRIEND_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */
}

