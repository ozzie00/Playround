package com.oneme.toplay.jni;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.oneme.toplay.MainActivity;
import com.oneme.toplay.R;
import com.oneme.toplay.base.ClientFriend;
import com.oneme.toplay.base.Constants;
import com.oneme.toplay.database.Database;
import com.oneme.toplay.service.Singleton;

//import im.tox.jtoxcore.callbacks.OnMessageCallback;

public class MessageCallback {//implements OnMessageCallback<ClientFriend> {
    /*
	public static final String TAG = "MessageCallback";

	private Context mcontext;

    Singleton mSingleton = Singleton.getInstance();

	public MessageCallback(Context context) {
		this.mcontext = context;
	}

	@Override
	public void execute(ClientFriend friend, String message) {
        // Add message to database
        Database mDatabase = new Database(this.mcontext);
        if(!mDatabase.isFriendBlocked(friend.getId())) {
            if (!(mSingleton.chatActive && (mSingleton.activeKey.equals(friend.getId())))) {
                mDatabase.addMessage(-1, friend.getId(), message, true, false, true, 2);
            } else {
                mDatabase.addMessage(-1, friend.getId(), message, true, true, true, 2);
            }
        }
        mDatabase.close();

        // Broadcast to main activity to tell it to refresh
        mSingleton.updateMessages(mcontext);

        // Notifications for messages
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.mcontext);

        // Check user accepts notifications in their settings
        if(preferences.getBoolean("notifications_enable_notifications", true)
                && preferences.getBoolean("notifications_new_message", true)) {

                if (!(mSingleton.chatActive && (mSingleton.activeKey.equals(friend.getId())))) {

                    String name = mSingleton.getFriend(friend.getId()).getName();

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this.mcontext)
                                    .setSmallIcon(R.drawable.ic_actionbar)
                                    .setContentTitle(name)
                                    .setContentText(message)
                                    .setDefaults(Notification.DEFAULT_ALL);

                    // Creates an explicit intent for an Activity in your app
                    Intent resultIntent = new Intent(this.mcontext, MainActivity.class);
                    resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    resultIntent.setAction(Constants.SWITCH_TO_FRIEND);
                    resultIntent.putExtra("key", friend.getId());
                    resultIntent.putExtra("name", name);

                    // The stack builder object will contain an artificial back stack for the
                    // started Activity.
                    // This ensures that navigating backward from the Activity leads out of
                    // your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.mcontext);
                    // Adds the back stack for the Intent (but not the Intent itself)
                    stackBuilder.addParentStack(MainActivity.class);
                    // Adds the Intent that starts the Activity to the top of the stack
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);
                    mSingleton.mNotificationManager.notify(friend.getFriendnumber(), mBuilder.build());
                }
        }
	}
	*/
}
