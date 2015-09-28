package com.oneme.toplay.base;

public class ConnectionChangeReceiver { // extends BroadcastReceiver {

    /*
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (DHTNodes.ipv4.size() == 0) {
                new DownloadNodes(context).execute();
            }

            Singleton mSingleton = Singleton.getInstance();
            // Bootstrap again
            for (int i = 0; i < DHTNodes.ipv4.size(); i++) {
                try {
                    mSingleton.jTox.bootstrap(com.oneme.toplay.base.DHTNodes.ipv4.get(i), Integer.parseInt(com.oneme.toplay.base.DHTNodes.port.get(i)), com.oneme.toplay.base.DHTNodes.key.get(i));
                } catch (Exception e) {
                }
            }
        }
    }
    */
}
