package com.oneme.toplay.jni;

import android.content.Context;

import com.oneme.toplay.base.ClientFriend;
import com.oneme.toplay.database.Database;
import com.oneme.toplay.service.Singleton;

//import im.tox.jtoxcore.callbacks.OnReadReceiptCallback;

public class ReadReceiptCallback { //implements OnReadReceiptCallback<ClientFriend> {

    /*
    private final static String TAG = "com.oneme.toplay.TAG";
    private Context mcontext;
    Singleton mSingleton = Singleton.getInstance();

    public ReadReceiptCallback(Context ctx) {
        this.mcontext = ctx;
    }

    @Override
    public void execute(ClientFriend friend, int receipt) {
        Database mDatabase = new Database(this.mcontext);
        String key         = mDatabase.setMessageReceived(receipt);
        mDatabase.close();

        // Broadcast
        mSingleton.updateMessages(mcontext);
    }
    */

}
