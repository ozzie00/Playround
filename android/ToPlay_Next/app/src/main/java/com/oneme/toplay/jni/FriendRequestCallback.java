package com.oneme.toplay.jni;

//import im.tox.jtoxcore.callbacks.OnFriendRequestCallback;

public class FriendRequestCallback { //implements OnFriendRequestCallback {

   // private static final String TAG           = "im.tox.antox.TAG";
   // public static final String FRIEND_KEY     = "im.tox.antox.FRIEND_KEY";
   // public static final String FRIEND_MESSAGE = "im.tox.antox.FRIEND_MESSAGE";

    /*
    private Context mcontext;

    Singleton mSingleton = Singleton.getInstance();

    public FriendRequestCallback(Context context) {
        this.mcontext = context;
    }

    @Override
    public void execute(String publicKey, String message){

        // Add friend request to database
        Database mDatabase = new Database(this.mcontext);
        if(!mDatabase.isFriendBlocked(publicKey))
            mDatabase.addFriendRequest(publicKey, message);
        mDatabase.close();

        mSingleton.updateFriendRequests(mcontext);
        if (Application.APPDEBUG) {
            Log.d("FriendRequestCallback", "");
        }

        // Notifications for friend requests
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.mcontext);
        // Check user accepts friend request notifcations in their settings
        if(preferences.getBoolean("notifications_enable_notifications", true) != false
                && preferences.getBoolean("notifications_friend_request", true) != false) {

                long[] vibratePattern = {0, 500}; // Start immediately and vibrate for 500ms

                if(preferences.getBoolean("notifications_new_message_vibrate", true) == false) {
                    vibratePattern[1] = 0; // Set vibrate to 0ms
                }

                // Notification
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this.mcontext)
                                .setSmallIcon(R.drawable.ic_actionbar)
                                .setContentTitle(this.mcontext.getString(R.string.friend_request))
                                .setContentText(message)
                                .setVibrate(vibratePattern)
                                .setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true);

                Intent targetIntent = new Intent(this.mcontext, MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(this.mcontext, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(contentIntent);
            mSingleton.mNotificationManager.notify(0, mBuilder.build()); // TODO: number currently points at first in list, should be pointing at the specific friend request in question
        }

    }

    */
}
