package com.oneme.toplay.jni;

import android.content.Context;

import com.oneme.toplay.base.ClientFriend;
import com.oneme.toplay.database.Database;
import com.oneme.toplay.service.Singleton;

//import im.tox.jtoxcore.callbacks.OnStatusMessageCallback;

public class StatusMessageCallback { //implements OnStatusMessageCallback<ClientFriend> {

    /*
    private final static String TAG = "com.oneme.toplay.TAG";
    private Context mcontext;

    Singleton mSingleton = Singleton.getInstance();
    public StatusMessageCallback(Context context) {
        this.mcontext = context;
    }

    @Override
    public void execute(ClientFriend friend, String newStatus) {
        Database mDatabase = new Database(mcontext);;
        mDatabase.updateStatusMessage(friend.getId(), newStatus);
        mDatabase.close();
        mSingleton.updateFriendsList(mcontext);
    }
    */
}
