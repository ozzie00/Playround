package com.oneme.toplay.service;

//import im.tox.jtoxcore.JTox;
//import im.tox.jtoxcore.ToxException;
//import im.tox.jtoxcore.ToxFileControl;
//import im.tox.jtoxcore.ToxOptions;
//import im.tox.jtoxcore.ToxUserStatus;
//import im.tox.jtoxcore.callbacks.CallbackHandler;

public class Singleton {

    /*
    private static final String TAG = "com.oneme.toplay.service.Singleton";
    public JTox jTox;
    private ClientFriendList mClientFriendList;
    public CallbackHandler callbackHandler;
    public NotificationManager mNotificationManager;
    public DataFile dataFile;
    public File qrFile;
    public BehaviorSubject<ArrayList<Friend>> friendListSubject;
    public BehaviorSubject<ArrayList<FriendRequest>> friendRequestSubject;
    public BehaviorSubject<HashMap> lastMessagesSubject;
    public BehaviorSubject<HashMap> unreadCountsSubject;
    public BehaviorSubject<Boolean> typingSubject;
    public BehaviorSubject<String> activeKeySubject;
    public BehaviorSubject<Boolean> updatedMessagesSubject;
    public BehaviorSubject<Boolean> updatedProgressSubject;
    public BehaviorSubject<Boolean> rightPaneActiveSubject;
    public PublishSubject<Boolean> doClosePaneSubject;
    public Observable friendInfoListSubject;
    public Observable activeKeyAndIsFriendSubject;
    public Observable friendListAndRequestsSubject;
    public Observable rightPaneActiveAndKeyAndIsFriendSubject;
    public Observable friendInfoListAndActiveSubject;
    public HashMap<Integer, Integer> progressMap            = new HashMap<Integer, Integer>();
    public HashMap<Integer, ArrayList<Tuple<Integer,Long>>> progressHistoryMap = new HashMap<>();
    public HashMap<Integer, FileStatus> fileStatusMap       = new HashMap<Integer, FileStatus>();
    public HashMap<Integer, Integer> fileSizeMap            = new HashMap<>();
    public HashMap<Integer, FileOutputStream> fileStreamMap = new HashMap<>();
    public HashMap<Integer, File> fileMap                   = new HashMap<>();
    public HashSet<Integer> fileIds                         = new HashSet<>();
    public HashMap<String, Boolean> typingMap               = new HashMap<String, Boolean>();
    public boolean isInited                                 = false;

    public String activeKey; //ONLY FOR USE BY CALLBACKS
    public boolean chatActive; //ONLY FOR USE BY CALLBACKS

    public enum FileStatus {REQUESTSENT, CANCELLED, INPROGRESS, FINISHED, PAUSED}

    public ClientFriend getFriend(String key) {
        return mClientFriendList.getById(key);
    }

    public void initSubjects(Context mcontext) {
        friendListSubject      = BehaviorSubject.create(new ArrayList<Friend>());
        friendListSubject.subscribeOn(Schedulers.io());

        friendRequestSubject   = BehaviorSubject.create(new ArrayList<FriendRequest>());
        friendRequestSubject.subscribeOn(Schedulers.io());

        rightPaneActiveSubject = BehaviorSubject.create(false);
        rightPaneActiveSubject.subscribeOn(Schedulers.io());

        lastMessagesSubject    = BehaviorSubject.create(new HashMap());
        lastMessagesSubject.subscribeOn(Schedulers.io());

        unreadCountsSubject    = BehaviorSubject.create(new HashMap());
        unreadCountsSubject.subscribeOn(Schedulers.io());

        activeKeySubject       = BehaviorSubject.create("");
        activeKeySubject.subscribeOn(Schedulers.io());

        doClosePaneSubject     = PublishSubject.create();
        doClosePaneSubject.subscribeOn(Schedulers.io());

        updatedMessagesSubject = BehaviorSubject.create(true);
        updatedMessagesSubject.subscribeOn(Schedulers.io());

        updatedProgressSubject = BehaviorSubject.create(true);
        updatedProgressSubject.subscribeOn(Schedulers.io());

        typingSubject          = BehaviorSubject.create(true);
        typingSubject.subscribeOn(Schedulers.io());

        friendInfoListSubject  = combineLatest(friendListSubject, lastMessagesSubject, unreadCountsSubject, new Func3<ArrayList<Friend>, HashMap, HashMap, ArrayList<FriendInfo>>() {
            @Override
            public ArrayList<FriendInfo> call(ArrayList<Friend> fl, HashMap lm, HashMap uc) {
                ArrayList<FriendInfo> fi = new ArrayList<FriendInfo>();
                for (Friend f : fl) {
                    String lastMessage;
                    Timestamp lastMessageTimestamp;
                    int unreadCount;
                    if (lm.containsKey(f.friendKey)) {
                        lastMessage = (String) ((Tuple<String, Timestamp>) lm.get(f.friendKey)).x;
                        lastMessageTimestamp = (Timestamp) ((Tuple<String, Timestamp>) lm.get(f.friendKey)).y;
                    } else {
                        lastMessage = "";
                        lastMessageTimestamp = new Timestamp(0, 0, 0, 0, 0, 0, 0);
                    }
                    if (uc.containsKey(f.friendKey)) {
                        unreadCount = (Integer) uc.get(f.friendKey);
                    } else {
                        unreadCount = 0;
                    }
                    fi.add(new FriendInfo(f.isOnline, f.friendName, f.friendStatus, f.personalNote, f.friendKey, lastMessage, lastMessageTimestamp, unreadCount, f.alias));
                }
                return fi;
            }
        });
        friendListAndRequestsSubject = combineLatest(friendInfoListSubject, friendRequestSubject, new Func2<ArrayList<FriendInfo>, ArrayList<FriendRequest>, Tuple<ArrayList<FriendInfo>, ArrayList<FriendRequest>>>() {
            @Override
            public Tuple<ArrayList<FriendInfo>, ArrayList<FriendRequest>> call(ArrayList<FriendInfo> fl, ArrayList<FriendRequest> fr) {
                return new Tuple(fl, fr);
            }
        });
        activeKeyAndIsFriendSubject = combineLatest(activeKeySubject, friendListSubject, new Func2<String, ArrayList<Friend>, Tuple<String, Boolean>>() {
            @Override
            public Tuple<String, Boolean> call(String key, ArrayList<Friend> fl) {
                boolean isFriend;
                isFriend = isKeyFriend(key, fl);
                return new Tuple<String, Boolean>(key, isFriend);
            }
        });
        friendInfoListAndActiveSubject = combineLatest(friendInfoListSubject, activeKeyAndIsFriendSubject, new Func2<ArrayList<FriendInfo>, Tuple<String,Boolean>,Tuple<ArrayList<FriendInfo>, Tuple<String,Boolean>>>() {
                    @Override
                    public Tuple<ArrayList<FriendInfo>, Tuple<String,Boolean>> call(ArrayList<FriendInfo> o, Tuple<String,Boolean> o2) {
                        return new Tuple<ArrayList<FriendInfo>, Tuple<String,Boolean>>((ArrayList<FriendInfo>) o, (Tuple<String,Boolean>) o2);
                    }
                });
        rightPaneActiveAndKeyAndIsFriendSubject = combineLatest(rightPaneActiveSubject, activeKeyAndIsFriendSubject, new Func2<Boolean, Tuple<String, Boolean>, Triple<Boolean, String, Boolean>>() {
            @Override
            public Triple<Boolean, String, Boolean> call(Boolean rightPaneActive, Tuple<String, Boolean> activeKeyAndIsFriend) {
                String activeKey = activeKeyAndIsFriend.x;
                Boolean isFriend = activeKeyAndIsFriend.y;
                return new Triple<Boolean, String, Boolean>(rightPaneActive, activeKey, isFriend);
            }
        });
    }


    private boolean isKeyFriend(String key, ArrayList<Friend> fl) {
        for (Friend f : fl) {
            if (f.friendKey.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public void sendFileSendRequest(String path, String key, Context context) {
        File file          = new File(path);
        String[] splitPath = path.split("/");
        String fileName    = splitPath[splitPath.length - 1];
        if (Application.APPDEBUG) {
            Log.d("sendFileSendRequest", "name: " + fileName);
        }
        if (fileName != null) {
            int fileNumber = -1;
            try {
                fileNumber = jTox.newFileSender(getFriend(activeKey).getFriendnumber(), file.length(), fileName);
            } catch (Exception e) {
                if (Application.APPDEBUG) {
                    Log.d("toxNewFileSender error", e.toString());
                }
            }
            if (fileNumber != -1) {
                Database mDatabase = new Database(context);
                long id            = mDatabase.addFileTransfer(key, path, fileNumber, (int) file.length(), true);
                fileIds.add((int) id);
                mDatabase.close();
            }
        }
    }

    public void fileSendRequest(String key, int fileNumber, String fileName, long fileSize, Context context) {
        if (Application.APPDEBUG) {
            Log.d("fileSendRequest, fileNumber: ", Integer.toString(fileNumber));
        }
        String fileN       = fileName;
        String[] fileSplit = fileName.split("\\.");
        String filePre     = "";
        String fileExt     = fileSplit[fileSplit.length-1];
        for (int j=0; j<fileSplit.length-1; j++) {
            filePre = filePre.concat(fileSplit[j]);
            if (j<fileSplit.length-2) {
                filePre = filePre.concat(".");
            }
        }
        File dirfile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), Constants.DOWNLOAD_DIRECTORY);
        if (!dirfile.mkdirs()) {
            Log.e("acceptFile", "Directory not created");
        }
        File file = new File(dirfile.getPath(), fileN);
        if (file.exists()) {
            int i = 1;
            do {
                fileN = filePre + "(" + Integer.toString(i) + ")" + "." + fileExt;
                file  = new File(dirfile.getPath(), fileN);
                i++;
            } while (file.exists());
        }
        Database mDatabase = new Database(context);
        long id            = mDatabase.addFileTransfer(key, fileN, fileNumber, (int) fileSize, false);
        fileIds.add((int) id);
        mDatabase.close();
        updateMessages(context);
    }

    public void changeActiveKey(String key) {
        activeKeySubject.onNext(key);
        doClosePaneSubject.onNext(true);
    }

    public void clearActiveKey() {
        activeKeySubject.onNext("");
        doClosePaneSubject.onNext(false);
    }

    public void acceptFile(String key, int fileNumber, Context context) {
        Database mDatabase = new Database(context);;
        int id = mDatabase.getFileId(key, fileNumber);
        if (id != -1) {
            try {
                jTox.fileSendControl(mClientFriendList.getById(key).getFriendnumber(), false, fileNumber, ToxFileControl.TOX_FILECONTROL_ACCEPT.ordinal(), new byte[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mDatabase.fileTransferStarted(key, fileNumber);
            fileStatusMap.put(id, FileStatus.INPROGRESS);
        }
        mDatabase.close();
        updatedMessagesSubject.onNext(true);
    }

    public void rejectFile(String key, int fileNumber, Context context) {
        Database mDatabase = new Database(context);;
        int id = mDatabase.getFileId(key, fileNumber);
        if (id != -1) {
            try {
                jTox.fileSendControl(mClientFriendList.getById(key).getFriendnumber(), false, fileNumber, ToxFileControl.TOX_FILECONTROL_KILL.ordinal(), new byte[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mDatabase.clearFileNumber(key, fileNumber);
            fileStatusMap.put(id, FileStatus.CANCELLED);
        }
        mDatabase.close();
        updatedMessagesSubject.onNext(true);
    }

    public void receiveFileData(String key, int fileNumber, byte[] data, Context context) {
        Database mDatabase = new Database(context);;
        int id             = mDatabase.getFileId(key, fileNumber);
        String state       = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (!fileStreamMap.containsKey(id)) {
                String fileName = mDatabase.getFilePath(key, fileNumber);
                File dirfile    = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), Constants.DOWNLOAD_DIRECTORY);
                if (!dirfile.mkdirs()) {
                    Log.e("acceptFile", "Directory not created");
                }
                File file               = new File(dirfile.getPath(), fileName);
                FileOutputStream output = null;
                try {
                    output = new FileOutputStream(file, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fileMap.put(id, file);
                fileStreamMap.put(id, output);

            }
            mDatabase.close();
            try {
                fileStreamMap.get(id).write(data);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                incrementProgress(id, data.length);
            }
            if (Application.APPDEBUG) {
                Log.d("ToxSingleton", "file size so far: " + fileMap.get(id).length() + " final file size: " + fileSizeMap.get(id));
            }
            if (fileMap.get(id).length() == fileSizeMap.get(id)) { // file finished
                try {
                    fileStreamMap.get(id).close();
                    jTox.fileSendControl(mClientFriendList.getById(key).getFriendnumber(), false, fileNumber, ToxFileControl.TOX_FILECONTROL_FINISHED.ordinal(), new byte[0]);
                    fileFinished(key, fileNumber, context);
                    if (Application.APPDEBUG) {
                        Log.d("ToxSingleton", "receiveFileData finished receiving file");
                    }
                } catch (Exception e) {
                    if (Application.APPDEBUG) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void incrementProgress(int id, int length) {
        Integer idObject = id;
        if (id != -1) {
            long time = System.currentTimeMillis();
            if (!progressMap.containsKey(idObject)) {
                progressMap.put(idObject, length);
                ArrayList<Tuple<Integer, Long>> a = new ArrayList<Tuple<Integer, Long>>();
                a.add(new Tuple<Integer, Long>(length, time));
                progressHistoryMap.put(idObject, a);
            } else {
                Integer current = progressMap.get(idObject);
                progressMap.put(idObject, current + length);
                ArrayList<Tuple<Integer, Long>> a = progressHistoryMap.get(idObject);
                a.add(new Tuple<Integer, Long>(current + length, time));
                progressHistoryMap.put(idObject, a);
            }
        }
        updatedProgressSubject.onNext(true);
    }

    public Tuple<Integer, Long> getProgressSinceXAgo(int id, int ms) {
    //ms is time to lookback, will find the first time value that is at least ms milliseconds ago, or if there isn't one, the first time value
        if (progressHistoryMap.containsKey(id)) {
            ArrayList<Tuple<Integer, Long>> progressHistory = progressHistoryMap.get(id);
            if (progressHistory.size() <= 1) {
                return null;
            }
            Tuple<Integer, Long> current = progressHistory.get(progressHistory.size() - 1);
            Tuple<Integer, Long> before;
            long timeDifference;
            for (int i = progressHistory.size() - 2; i >= 0; --i) {
                before = progressHistory.get(i);
                timeDifference = current.y - before.y;
                if (timeDifference > ms || i == 0) {
                    return new Tuple<Integer, Long>(current.x - before.x, System.currentTimeMillis() - before.y);
                }
            }
        }
        return null;
    }

    public void setProgress(int id, int progress) {
        Integer idObject = id;
        if (id != -1) {
            long time = System.currentTimeMillis();
            progressMap.put(idObject, progress);
            ArrayList<Tuple<Integer, Long>> a;
            if (!progressHistoryMap.containsKey(idObject)) {
                a = new ArrayList<Tuple<Integer, Long>>();
            } else {
                a = progressHistoryMap.get(idObject);
            }
            a.add(new Tuple<Integer, Long>(progress, time));
            progressHistoryMap.put(idObject, a);
            updatedProgressSubject.onNext(true);
        }
    }
    public void fileFinished(String key, int fileNumber, Context context) {
        if (Application.APPDEBUG) {
            Log.d("ToxSingleton", "fileFinished");
        }
        Database mDatabase = new Database(context);;
        int id             = mDatabase.getFileId(key, fileNumber);
        if (id != -1) {
            fileStatusMap.put(id, FileStatus.FINISHED);
            fileIds.remove(id);
        }
        mDatabase.fileFinished(key, fileNumber);
        mDatabase.close();
        updatedMessagesSubject.onNext(true);
    }

    public void cancelFile(String key, int fileNumber, Context context) {
        if (Application.APPDEBUG) {
            Log.d("ToxSingleton", "cancelFile");
        }
        Database mDatabase = new Database(context);;
        int id             = mDatabase.getFileId(key, fileNumber);
        if (id != -1) {
            fileStatusMap.put(id, FileStatus.CANCELLED);
        }
        mDatabase.clearFileNumber(key, fileNumber);
        mDatabase.close();
        updatedMessagesSubject.onNext(true);
    }

    public int getProgress(int id) {
        if (id != -1 && progressMap.containsKey(id)) {
            return progressMap.get(id);
        } else {
            return 0;
        }
    }

    public void sendFileData(final String key, final int fileNumber, final int startPosition, final Context context) {
        class sendFileTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                boolean result = doSendFileData(key, fileNumber, startPosition, context);
                if (Application.APPDEBUG) {
                    Log.d("doSendFileData finished, result: ", Boolean.toString(result));
                }
                Database mDatabase = new Database(context);;
                mDatabase.clearFileNumber(key, fileNumber);
                mDatabase.close();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
            }
        }
        new sendFileTask().execute();
    }

    public boolean doSendFileData(final String key, final int fileNumber, final int startPosition, final Context context) {
        String path        = "";
        Database mDatabase = new Database(context);;
        path               = mDatabase.getFilePath(key, fileNumber);
        int id             = mDatabase.getFileId(key, fileNumber);
        mDatabase.close();
        if (id != -1) {
            fileStatusMap.put(id, FileStatus.INPROGRESS);
        }
        int result = -1;
        if (!path.equals("")) {
            int chunkSize = 1;
            try {
                chunkSize = jTox.fileDataSize(getFriend(key).getFriendnumber());
            } catch (Exception e) {
                e.printStackTrace();
            }
            File file = new File(path);
            byte[] bytes = new byte[(int) file.length()];
            BufferedInputStream buf = null;
            try {
                buf = new BufferedInputStream(new FileInputStream(file));
            } catch (Exception e) {
                e.printStackTrace();
            }
            int i = startPosition;
            if (buf != null) {
                for (i = startPosition; i < bytes.length; i = i + chunkSize) {
                    byte[] data = new byte[chunkSize];
                    try {
                        buf.mark(chunkSize*2);
                        int read = buf.read(data, 0, chunkSize);
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                    try {
                        result = jTox.fileSendData(getFriend(key).getFriendnumber(), fileNumber, data);
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                    if (!(fileStatusMap.containsKey(id) && fileStatusMap.get(id).equals(FileStatus.INPROGRESS))) {
                        break;
                    }
                    if (result == -1) {
                        if (Application.APPDEBUG) {
                            Log.d("sendFileDataTask", "toxFileSendData failed");
                        }
                        try {
                            jTox.doTox();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SystemClock.sleep(50);
                        i = i - chunkSize;
                        try {
                            buf.reset();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (i > bytes.length) {
                        i = bytes.length;
                    }
                    setProgress(id, i);
                }
                try {
                    buf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (result != -1 && fileStatusMap.get(id).equals(FileStatus.INPROGRESS)) {
                try {
                    if (Application.APPDEBUG) {
                        Log.d("toxFileSendControl", "FINISHED");
                    }
                    jTox.fileSendControl(getFriend(key).getFriendnumber(), true, fileNumber, ToxFileControl.TOX_FILECONTROL_FINISHED.ordinal(), new byte[0]);
                    fileFinished(key, fileNumber, context);
                    return true;
                } catch (Exception e) {
                    if (Application.APPDEBUG) {
                        Log.d("toxFileSendControl error", e.toString());
                    }
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public void updateFriendsList(Context context) {
        try {
            Database mDatabase = new Database(context);

            ArrayList<Friend> friendList = mDatabase.getFriendList();

            mDatabase.close();

            friendListSubject.onNext(friendList);
        } catch (Exception e) {
            friendListSubject.onError(e);
        }
    }

    public void clearUselessNotifications (String key) {
        if (key != null && !key.equals("")) {
            try {
                mNotificationManager.cancel(getFriend(key).getFriendnumber());
            } catch (Exception e) {

            }
        }
    }

    public void sendUnsentMessages(Context context) {
        Database mDatabase = new Database(context);
            ArrayList<Message> unsentMessageList = mDatabase.getUnsentMessageList();
            for (int i = 0; i<unsentMessageList.size(); i++) {
                ClientFriend friend = null;
                int id = unsentMessageList.get(i).message_id;
                boolean sendingSucceeded = true;
                try {
                    friend = getFriend(unsentMessageList.get(i).key);
                } catch (Exception e) {
                    if (Application.APPDEBUG) {
                        Log.d(TAG, e.toString());
                    }
                }
                try {
                    if (friend != null && friend.isOnline() && jTox != null) {
                        // TODO: fix for withid change
                        jTox.sendMessage(friend, unsentMessageList.get(i).message);
                    }
                } catch (ToxException e) {
                    if (Application.APPDEBUG) {
                        Log.d(TAG, e.toString());
                        e.printStackTrace();
                    }
                    sendingSucceeded = false;
                }
                if (sendingSucceeded) {
                    mDatabase.updateUnsentMessage(id);
                }
            }
        mDatabase.close();
            updateMessages(context);
    }

    public void updateFriendRequests(Context context) {
        try {
            Database mDatabase = new Database(context);;
            ArrayList<FriendRequest> friendRequest = mDatabase.getFriendRequestsList();
            mDatabase.close();
            friendRequestSubject.onNext(friendRequest);
        } catch (Exception e) {
            friendRequestSubject.onError(e);
        }
    }

    public void updateMessages(Context ctx) {
        updatedMessagesSubject.onNext(true);
        updateLastMessageMap(ctx);
        updateUnreadCountMap(ctx);
    }

    public void updateLastMessageMap(Context context) {
        try {
            Database mDatabase = new Database(context);;
            HashMap map        = mDatabase.getLastMessages();
            mDatabase.close();

            lastMessagesSubject.onNext(map);
        } catch (Exception e) {
            lastMessagesSubject.onError(e);
        }
    }

    public void updateUnreadCountMap(Context context) {
        try {
            Database mDatabase = new Database(context);;
            HashMap map        = mDatabase.getUnreadCounts();
            mDatabase.close();

            unreadCountsSubject.onNext(map);
        } catch (Exception e) {
            unreadCountsSubject.onError(e);
        }
    }

    private static volatile Singleton instance = null;

    private Singleton() {
    }

    public void init(Context context) {
        mClientFriendList = new ClientFriendList();
        callbackHandler   = new CallbackHandler(mClientFriendList);

        qrFile   = context.getFileStreamPath(AppConstant.OMETOPLAYUSERQRCODEFILENAME);
        dataFile = new DataFile(context);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean udpEnabled            = preferences.getBoolean("enable_udp", false);

        ToxOptions options = new ToxOptions(Options.ipv6Enabled, udpEnabled, Options.proxyEnabled);

        // Choose appropriate constructor depending on if data file exists
        if (!dataFile.doesFileExist()) {
            try {
                jTox = new JTox(mClientFriendList, callbackHandler, options);
                // Save data file
                dataFile.saveFile(jTox.save());
                // Save users public key to settings
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(AppConstant.OMEPARSEUSERIDKEY, jTox.getAddress());

                // Ozzie Zhang 2014-12-29 add save users OMEID to cloud
                if (ParseUser.getCurrentUser() != null) {
                    ParseUser.getCurrentUser().put(AppConstant.OMEPARSEUSEROMEIDKEY, jTox.getAddress());
                }

                editor.commit();
            } catch (ToxException e) {
                if (Application.APPDEBUG) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                jTox = new JTox(dataFile.loadFile(), mClientFriendList, callbackHandler, options);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(AppConstant.OMEPARSEUSERIDKEY, jTox.getAddress());

                // Ozzie Zhang 2014-12-29 add save users OMEID to cloud
                if (ParseUser.getCurrentUser() != null) {
                    ParseUser.getCurrentUser().put(AppConstant.OMEPARSEUSEROMEIDKEY, jTox.getAddress());
                }

                editor.commit();
            } catch (ToxException e) {
                if (Application.APPDEBUG) {
                    e.printStackTrace();
                }
            }
        }

        // If the service wasn't running then we wouldn't have gotten callbacks for a user
        //  going offline so default everyone to offline and just wait for callbacks.
        //
        Database mDatabase = new Database(context);;
        mDatabase.setAllOffline();

        // Populate tox friends list with saved friends in database
        ArrayList<Friend> friends = mDatabase.getFriendList();
        mDatabase.close();

        if (friends.size() > 0) {
            for (int i = 0; i < friends.size(); i++) {
                try {
                    jTox.confirmRequest(friends.get(i).friendKey);
                } catch (Exception e) {
                }
            }
        }

        // Instantiate and register callback classes
        MessageCallback mMessageCallback                   = new MessageCallback(context);
        FriendRequestCallback mFriendRequestCallback       = new FriendRequestCallback(context);
        ActionCallback mActionCallback                     = new ActionCallback(context);
        ConnectionStatusCallback mConnectionStatusCallback = new ConnectionStatusCallback(context);
        NameChangeCallback mNameChangeCallback             = new NameChangeCallback(context);
        ReadReceiptCallback mReadReceiptCallback           = new ReadReceiptCallback(context);
        StatusMessageCallback mStatusMessageCallback       = new StatusMessageCallback(context);
        UserStatusCallback mUserStatusCallback             = new UserStatusCallback(context);
        TypingChangeCallback mTypingChangeCallback         = new TypingChangeCallback(context);
        //FileSendRequestCallback mFileSendRequestCallback   = new FileSendRequestCallback(context);
        //FileControlCallback mFileControlCallback           = new FileControlCallback(context);
        //FileDataCallback mFileDataCallback                 = new FileDataCallback(context);

       // AudioDataCallback mAudioDataCallback               = new AudioDataCallback(context);
       // AvCallbackCallback mAvCallbackCallback             = new AvCallbackCallback(context);
       // VideoDataCallback mVideoDataCallback               = new VideoDataCallback(context);

        callbackHandler.registerOnMessageCallback(mMessageCallback);
        callbackHandler.registerOnFriendRequestCallback(mFriendRequestCallback);
        callbackHandler.registerOnActionCallback(mActionCallback);
        callbackHandler.registerOnConnectionStatusCallback(mConnectionStatusCallback);
        callbackHandler.registerOnNameChangeCallback(mNameChangeCallback);
        callbackHandler.registerOnReadReceiptCallback(mReadReceiptCallback);
        callbackHandler.registerOnStatusMessageCallback(mStatusMessageCallback);
        callbackHandler.registerOnUserStatusCallback(mUserStatusCallback);
        callbackHandler.registerOnTypingChangeCallback(mTypingChangeCallback);
       // callbackHandler.registerOnFileSendRequestCallback(mFileSendRequestCallback);
       // callbackHandler.registerOnFileControlCallback(mFileControlCallback);
       // callbackHandler.registerOnFileDataCallback(mFileDataCallback);
      //  callbackHandler.registerOnAudioDataCallback(mAudioDataCallback);
      //  callbackHandler.registerOnAvCallbackCallback(mAvCallbackCallback);
        //callbackHandler.registerOnVideoDataCallback(mVideoDataCallback);

        // Load user details
        try {
            jTox.setName(preferences.getString(AppConstant.OMEPARSEUSERNICKNAMEKEY, ""));
            jTox.setStatusMessage(preferences.getString(AppConstant.OMEPARSEUSERSTATUALERTSKEY, ""));
            ToxUserStatus newStatus;
            String newStatusString = preferences.getString(AppConstant.OMEPARSEUSERSTATUSKEY, "");
            newStatus = UserStatus.getToxUserStatusFromString(newStatusString);
            jTox.setUserStatus(newStatus);
        } catch (ToxException e) {

        }

        // Check if connected to the Internet
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo     = connMgr.getActiveNetworkInfo();

        // If connected to internet, download nodes
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                if(DHTNodes.ipv4.size() == 0)
                    new DownloadNodes(context).execute().get(); // Make sure finished getting nodes first

                // Attempt to connect to all the nodes
                for(int i = 0; i < DHTNodes.ipv4.size(); i++) {
                    jTox.bootstrap(DHTNodes.ipv4.get(i), Integer.parseInt(DHTNodes.port.get(i)), DHTNodes.key.get(i));
                }

            } catch (Exception e) {
            }
        }

        isInited = true;
    }

    public static Singleton getInstance() {
        // Double-checked locking
        if(instance == null) {
            synchronized (Singleton.class) {
                if(instance == null) {
                    instance = new Singleton();
                }
            }
        }

        return instance;
    }

    */
}
