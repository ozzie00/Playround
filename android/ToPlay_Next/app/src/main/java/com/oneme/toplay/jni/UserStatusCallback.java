package com.oneme.toplay.jni;

import android.content.Context;

import com.oneme.toplay.base.ClientFriend;
import com.oneme.toplay.database.Database;
import com.oneme.toplay.service.Singleton;

//import im.tox.jtoxcore.ToxUserStatus;
//import im.tox.jtoxcore.callbacks.OnUserStatusCallback;

public class UserStatusCallback { //implements OnUserStatusCallback<ClientFriend> {

    /*
    private final static String TAG = "com.oneme.toplay.TAG";
    private Context mcontext;

    Singleton mSingleton = Singleton.getInstance();
    public UserStatusCallback(Context context) {
        this.mcontext = context;
    }

    @Override
    public void execute(ClientFriend friend, ToxUserStatus newStatus) {
        Database mDatabase = new Database(mcontext);
        mDatabase.updateUserStatus(friend.getId(), newStatus);
        mDatabase.close();
        mSingleton.updateFriendsList(mcontext);
    }
    */
}
