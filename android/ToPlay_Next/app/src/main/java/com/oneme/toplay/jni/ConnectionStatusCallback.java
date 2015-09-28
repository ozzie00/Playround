package com.oneme.toplay.jni;

//import im.tox.jtoxcore.callbacks.OnConnectionStatusCallback;

public class ConnectionStatusCallback { // implements OnConnectionStatusCallback<ClientFriend> {

    /*
    private final static String TAG = "com.oneme.toplay.TAG";
    private Context mcontext;

    Singleton mSingleton = Singleton.getInstance();

    public ConnectionStatusCallback(Context context) {
        this.mcontext = context;
    }

    @Override
    public void execute(ClientFriend friend, boolean online) {
        Database mDatabase = new Database(mcontext);
        mDatabase.updateUserOnline(friend.getId(), online);

        String[] det = mDatabase.getFriendDetails(friend.getId());
        String tmp;

        // Set tmp to alias if not empty otherwise to their name
        if(!det[1].equals(""))
            tmp = det[1];
        else
            tmp = det[0];

        long epochNow = System.currentTimeMillis()/1000;
        if(epochNow - Constants.epoch > 30) {
            String tmp2 = online ? this.mcontext.getString(R.string.connection_online) : this.mcontext.getString(R.string.connection_offline);
            mDatabase.addMessage(-1, friend.getId(), tmp + " " + this.mcontext.getString(R.string.connection_has) + " " + tmp2, true, true, true, 5);
            mDatabase.close();
        }

        if (online) {
            mSingleton.sendUnsentMessages(mcontext);
        } else {
            mSingleton.typingMap.put(friend.getId(),false);
            mSingleton.typingSubject.onNext(true);
        }
        mSingleton.updateFriendsList(mcontext);
        mSingleton.updateMessages(mcontext);
    }

    */
}
