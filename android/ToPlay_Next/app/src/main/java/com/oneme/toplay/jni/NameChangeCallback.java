package com.oneme.toplay.jni;

import android.content.Context;

import com.oneme.toplay.base.ClientFriend;
import com.oneme.toplay.database.Database;
import com.oneme.toplay.service.Singleton;

//import im.tox.jtoxcore.callbacks.OnNameChangeCallback;

public class NameChangeCallback { // implements OnNameChangeCallback<ClientFriend> {
/*
    private final static String TAG = "com.oneme.toplay.TAG";
    private Context mcontext;

    Singleton mSingleton = Singleton.getInstance();
    public NameChangeCallback(Context context) {
        this.mcontext = context;
    }

    @Override
    public void execute(ClientFriend friend, String newName) {
        Database mDatabase = new Database(mcontext);
        mDatabase.updateFriendName(friend.getId(), newName);
        mDatabase.close();
        mSingleton.updateFriendsList(mcontext);
    }
    */
}
