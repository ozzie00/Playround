package com.oneme.toplay.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.oneme.toplay.Application;
import com.oneme.toplay.base.Constants;
import com.oneme.toplay.base.Friend;
import com.oneme.toplay.base.FriendRequest;
import com.oneme.toplay.base.Message;
import com.oneme.toplay.base.Tuple;
import com.oneme.toplay.base.UserStatus;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TimeZone;

//import im.tox.jtoxcore.ToxUserStatus;


public class Database {

    private final String TAG = "Database";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context, String activeDatabase) {
            super(context, activeDatabase, null, Constants.DATABASE_VERSION);
        }
        public String CREATE_TABLE_FRIENDS = "CREATE TABLE IF NOT EXISTS friends" +
                " (ome_key text primary key, " +
                "username text, " +
                "status text, " +
                "note text, " +
                "alias text, " +
                "isonline boolean, " +
                "isblocked boolean);";

        public String CREATE_TABLE_MESSAGES = "CREATE TABLE IF NOT EXISTS messages" +
                " ( _id integer primary key , " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "message_id integer, " +
                "ome_key text, " +
                "message text, " +
                "has_been_received boolean, " +
                "has_been_read boolean, " +
                "successfully_sent boolean, " +
                "size integer, " +
                "type int, " +
                "FOREIGN KEY(ome_key) REFERENCES friends(ome_key))";

        public String CREATE_TABLE_FRIEND_REQUESTS = "CREATE TABLE IF NOT EXISTS friend_requests" +
                " ( _id integer primary key, " +
                "ome_key text, " +
                "message text)";

        public void onCreate(SQLiteDatabase mDb) {
            mDb.execSQL(CREATE_TABLE_FRIENDS);
            mDb.execSQL(CREATE_TABLE_FRIEND_REQUESTS);
            mDb.execSQL(CREATE_TABLE_MESSAGES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase mDb, int oldVersion, int newVersion) {
            mDb.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_FRIENDS);
            mDb.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CHAT_LOGS);
            mDb.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_FRIEND_REQUEST);
            onCreate(mDb);
        }
    }

    private DatabaseHelper mSQLiteOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private Context ctx;
    private String activeDatabase;

    public Database(Context ctx) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        this.ctx                      = ctx;
        this.activeDatabase           = preferences.getString("active_account","");
    }

    public Database open(boolean writeable) throws SQLException {
        mSQLiteOpenHelper = new DatabaseHelper(ctx, activeDatabase);
        if (writeable) {
            mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        } else {
            mSQLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        mSQLiteOpenHelper.close();
    }

    //check if a column is in a table
    private boolean isColumnInTable(SQLiteDatabase mDb, String table, String column) {
        try {
            Cursor cursor = mDb.rawQuery("SELECT * FROM " + table + " LIMIT 0", null);

            //if it is -1 the column does not exists
            if(cursor.getColumnIndex(column) == -1) {
                return false;
            }
            else {
                return true;
            }
        }
        catch (Exception e) {
            return false;
        }
    }

    public void addFriend(String key, String message, String alias, String username) {
        this.open(true);

        if(username.contains("@"))
            username = username.substring(0, username.indexOf("@"));

        if(username == null || username.length() == 0)
            username = key.substring(0,7);

        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME_KEY, key);
        values.put(Constants.COLUMN_NAME_STATUS, "0");
        values.put(Constants.COLUMN_NAME_NOTE, message);
        values.put(Constants.COLUMN_NAME_USERNAME, username);
        values.put(Constants.COLUMN_NAME_ISONLINE, false);
        values.put(Constants.COLUMN_NAME_ALIAS, alias);
        values.put(Constants.COLUMN_NAME_ISBLOCKED, false);
        mSQLiteDatabase.insert(Constants.TABLE_FRIENDS, null, values);
        this.close();
    }

    public long addFileTransfer(String key, String path, int fileNumber, int size, boolean sending) {
        this.open(true);
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME_KEY, key);
        values.put(Constants.COLUMN_NAME_MESSAGE, path);
        values.put(Constants.COLUMN_NAME_MESSAGE_ID, fileNumber);
        values.put(Constants.COLUMN_NAME_HAS_BEEN_RECEIVED, false);
        values.put(Constants.COLUMN_NAME_HAS_BEEN_READ, false);
        values.put(Constants.COLUMN_NAME_SUCCESSFULLY_SENT, false);
        if (sending) {
            values.put("type", Constants.MESSAGE_TYPE_FILE_TRANSFER);
        } else {
            values.put("type", Constants.MESSAGE_TYPE_FILE_TRANSFER_FRIEND);
        }
        values.put("size", size);
        long id = mSQLiteDatabase.insert(Constants.TABLE_CHAT_LOGS, null, values);
        this.close();
        return id;
    }

    public void fileTransferStarted(String key, int fileNumber) {
        this.open(false);
        String query = "UPDATE messages SET " + Constants.COLUMN_NAME_SUCCESSFULLY_SENT + " = 1 WHERE (type == 3 OR type == 4) AND message_id == " + fileNumber + " AND ome_key = '" + key + "'";
        mSQLiteDatabase.execSQL(query);
        this.close();
    }

    public void addFriendRequest(String key, String message) {
        this.open(true);
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME_KEY, key);
        values.put(Constants.COLUMN_NAME_MESSAGE, message);
        mSQLiteDatabase.insert(Constants.TABLE_FRIEND_REQUEST, null, values);
        this.close();
    }

    public void addMessage(int message_id, String key, String message, boolean has_been_received, boolean has_been_read, boolean successfully_sent, int type){
        this.open(true);
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME_MESSAGE_ID, message_id);
        values.put(Constants.COLUMN_NAME_KEY, key);
        values.put(Constants.COLUMN_NAME_MESSAGE, message);
        values.put(Constants.COLUMN_NAME_HAS_BEEN_RECEIVED, has_been_received);
        values.put(Constants.COLUMN_NAME_HAS_BEEN_READ, has_been_read);
        values.put(Constants.COLUMN_NAME_SUCCESSFULLY_SENT, successfully_sent);
        values.put("type", type);

        mSQLiteDatabase.insert(Constants.TABLE_CHAT_LOGS, null, values);
        this.close();
    }

    public HashMap getUnreadCounts() {
        this.open(false);
        HashMap map = new HashMap();
        String selectQuery = "SELECT friends.ome_key, COUNT(messages._id) " +
                "FROM messages " +
                "JOIN friends ON friends.ome_key = messages.ome_key " +
                "WHERE messages.has_been_read == 0 AND (messages.type == 2 OR messages.type == 4)" +
                "GROUP BY friends.ome_key";
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String key = cursor.getString(0);
                int count = cursor.getInt(1);
                map.put(key, count);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.close();
        return map;
    };

    public String getFilePath(String key, int fileNumber) {
        this.open(false);
        String path = "";
        String selectQuery = "SELECT message FROM messages WHERE ome_key = '" + key + "' AND (type == 3 OR type == 4) AND message_id == " +
                fileNumber;
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
        Log.d("getFilePath count: ", Integer.toString(cursor.getCount()) + " filenumber: " + fileNumber);
        if (cursor.moveToFirst()) {
            path = cursor.getString(0);
        }
        cursor.close();
        this.close();
        return path;
    }

    public int getFileId(String key, int fileNumber) {
        this.open(false);
        int id = -1;
        String selectQuery = "SELECT _id FROM messages WHERE ome_key = '" + key + "' AND (type == 3 OR type == 4) AND message_id == " +
                fileNumber;
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        this.close();
        return id;
    }

    public void clearFileNumbers() {
        this.open(false);
        String query = "UPDATE messages SET message_id = -1 WHERE (type == 3 OR type == 4)";
        mSQLiteDatabase.execSQL(query);
        this.close();
    }

    public void clearFileNumber(String key, int fileNumber) {
        this.open(false);
        String query = "UPDATE messages SET message_id = -1 WHERE (type == 3 OR type == 4) AND message_id == " + fileNumber + " AND ome_key = '" + key + "'";
        mSQLiteDatabase.execSQL(query);
        this.close();
    }

    public void fileFinished(String key, int fileNumber) {
        if (Application.APPDEBUG) {
            Log.d(TAG, " fileFinished ");
        }
        this.open(false);
        String query = "UPDATE messages SET " + Constants.COLUMN_NAME_HAS_BEEN_RECEIVED + "=1, message_id = -1 WHERE (type == 3 OR type == 4) AND message_id == " + fileNumber + " AND ome_key = '" + key + "'";
        mSQLiteDatabase.execSQL(query);
        this.close();
    }

    public HashMap getLastMessages() {
        this.open(false);
        HashMap map        = new HashMap();
        String selectQuery = "SELECT ome_key, message, timestamp FROM messages WHERE _id IN (" +
                "SELECT MAX(_id) " +
                "FROM messages WHERE (type == 1 OR type == 2) " +
                "GROUP BY ome_key)";
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String key          = cursor.getString(0);
                String message      = cursor.getString(1);
                Timestamp timestamp = Timestamp.valueOf(cursor.getString(2));
                map.put(key, new Tuple<String,Timestamp>(message,timestamp));
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.close();
        return map;
    }

    public ArrayList<Message> getMessageList(String key, boolean actionMessages) {
        this.open(false);
        ArrayList<Message> messageList = new ArrayList<Message>();
        String selectQuery;
        if (key.equals("")) {
            selectQuery = "SELECT * FROM " + Constants.TABLE_CHAT_LOGS + " ORDER BY " + Constants.COLUMN_NAME_TIMESTAMP + " DESC";
        } else {
            String act;
            if (actionMessages) {
                act = "";
            } else {
                act = "AND (type == 1 OR type == 2 OR type == 3 OR type == 4) ";
            }
            selectQuery = "SELECT * FROM " + Constants.TABLE_CHAT_LOGS + " WHERE " + Constants.COLUMN_NAME_KEY + " = '" + key + "' " + act + "ORDER BY " + Constants.COLUMN_NAME_TIMESTAMP + " ASC";
        }
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                Timestamp time   = Timestamp.valueOf(cursor.getString(1));
                int message_id   = cursor.getInt(2);
                String k         = cursor.getString(3);
                String m         = cursor.getString(4);
                boolean received = cursor.getInt(5)>0;
                boolean read     = cursor.getInt(6)>0;
                boolean sent     = cursor.getInt(7)>0;
                int size         = cursor.getInt(8);
                int type         = cursor.getInt(9);
                messageList.add(new Message(id, message_id, k, m,received, read, sent, time, size, type));
            } while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return messageList;
    }

    public HashSet<Integer> getMessageIds(String key, boolean actionMessages) {
        this.open(false);
        String selectQuery;
        HashSet<Integer> idSet = new HashSet<Integer>();
        if (key == null || key.equals("")) {
            selectQuery = "SELECT * FROM " + Constants.TABLE_CHAT_LOGS + " ORDER BY " + Constants.COLUMN_NAME_TIMESTAMP + " DESC";
        } else {
            String act;
            if (actionMessages) {
                act = "";
            } else {
                act = "AND (type == 1 OR type == 2 OR type == 3 OR type == 4) ";
            }
            selectQuery = "SELECT * FROM " + Constants.TABLE_CHAT_LOGS + " WHERE " + Constants.COLUMN_NAME_KEY + " = '" + key + "' " + act + "ORDER BY " + Constants.COLUMN_NAME_TIMESTAMP + " ASC";
        }
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                idSet.add(id);
            } while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return idSet;
    }
    public Cursor getMessageCursor(String key, boolean actionMessages) {
        this.open(false);
        String selectQuery;
        if (key == null || key.equals("")) {
            selectQuery = "SELECT * FROM " + Constants.TABLE_CHAT_LOGS + " ORDER BY " + Constants.COLUMN_NAME_TIMESTAMP + " DESC";
        } else {
            String act;
            if (actionMessages) {
                act = "";
            } else {
                act = "AND (type == 1 OR type == 2 OR type == 3 OR type == 4) ";
            }
            selectQuery = "SELECT * FROM " + Constants.TABLE_CHAT_LOGS + " WHERE " + Constants.COLUMN_NAME_KEY + " = '" + key + "' " + act + "ORDER BY " + Constants.COLUMN_NAME_TIMESTAMP + " ASC";
        }
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
        return cursor;
    }
    public Cursor getRecentCursor() {
        this.open(false);
        String selectQuery = "SELECT f.ome_key, f.username, f.isonline, f.status, m1.timestamp, m1.message, COUNT(m2.ome_key) as unreadCount, m1._id " +
                "FROM " + Constants.TABLE_FRIENDS + " f " +
                "INNER JOIN " + Constants.TABLE_CHAT_LOGS + " m1 ON (f.ome_key = m1.ome_key) " +
                "LEFT OUTER JOIN (SELECT ome_key FROM " + Constants.TABLE_CHAT_LOGS + " WHERE ((type = 2 OR type = 4) AND has_been_read = 0)) " +
                "m2 ON (f.ome_key = m2.ome_key) " +
                "WHERE m1._id = (SELECT MAX(_id) FROM " + Constants.TABLE_CHAT_LOGS + " WHERE (ome_key = f.ome_key AND NOT type = 5)) " +
                "GROUP BY f.ome_key " +
                "ORDER BY m1._id DESC";
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
        return cursor;
    }
    public ArrayList<FriendRequest> getFriendRequestsList() {
        this.open(false);
        ArrayList<FriendRequest> friendRequests = new ArrayList<FriendRequest>();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Constants.COLUMN_NAME_KEY,
                Constants.COLUMN_NAME_MESSAGE
        };

        Cursor cursor = mSQLiteDatabase.query(
                Constants.TABLE_FRIEND_REQUEST,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        if (cursor.moveToFirst()) {
            do {
                String key = cursor.getString(
                        cursor.getColumnIndexOrThrow(Constants.COLUMN_NAME_KEY)
                );
                String message = cursor.getString(
                        cursor.getColumnIndexOrThrow(Constants.COLUMN_NAME_MESSAGE)
                );
                friendRequests.add(new FriendRequest(key, message));
            } while (cursor.moveToNext()) ;
        }

        cursor.close();
        this.close();

        return friendRequests;
    }

    public ArrayList<Message> getUnsentMessageList() {
        this.open(false);
        ArrayList<Message> messageList = new ArrayList<Message>();
        String selectQuery             = "SELECT * FROM " + Constants.TABLE_CHAT_LOGS + " WHERE " + Constants.COLUMN_NAME_SUCCESSFULLY_SENT + "=0 AND type == 1 ORDER BY " + Constants.COLUMN_NAME_TIMESTAMP + " ASC";
        Cursor cursor                  = mSQLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id         = cursor.getInt(0);
                Timestamp time = Timestamp.valueOf(cursor.getString(1));
                int m_id       = cursor.getInt(2);

                if (Application.APPDEBUG) {
                    Log.d(TAG, "UNSENT MESAGE ID: " + " " + m_id);
                }
                String k         = cursor.getString(3);
                String m         = cursor.getString(4);
                boolean received = cursor.getInt(5)>0;
                boolean read     = cursor.getInt(6)>0;
                boolean sent     = cursor.getInt(7)>0;
                int size         = cursor.getInt(8);
                int type         = cursor.getInt(9);
                messageList.add(new Message(id, m_id, k, m, received, read, sent, time, size, type));
            } while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return messageList;
    }

    public void updateUnsentMessage(int m_id) {
        if (Application.APPDEBUG) {
            Log.d(TAG, "UPDATE UNSENT MESSAGE - ID : " +  " " + m_id);
        }
        String messageId     = m_id + "";
        this.open(true);
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME_SUCCESSFULLY_SENT, "1");
        values.put("type", 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        values.put(Constants.COLUMN_NAME_TIMESTAMP, dateFormat.format(date));
        mSQLiteDatabase.update(Constants.TABLE_CHAT_LOGS, values, Constants.COLUMN_NAME_MESSAGE_ID + "=" + messageId
                + " AND " + Constants.COLUMN_NAME_SUCCESSFULLY_SENT + "=0", null);
        this.close();
    }

    public String setMessageReceived(int receipt) { //returns public key of who the message was sent to
        this.open(true);
        String query = "UPDATE " + Constants.TABLE_CHAT_LOGS + " SET " + Constants.COLUMN_NAME_HAS_BEEN_RECEIVED + "=1 WHERE " + Constants.COLUMN_NAME_MESSAGE_ID + "=" + receipt + " AND " + Constants.COLUMN_NAME_SUCCESSFULLY_SENT + "=1 AND type =1";
        mSQLiteDatabase.execSQL(query);
        String selectQuery = "SELECT * FROM " + Constants.TABLE_CHAT_LOGS + " WHERE " + Constants.COLUMN_NAME_MESSAGE_ID + "=" + receipt + " AND " + Constants.COLUMN_NAME_SUCCESSFULLY_SENT + "=1 AND type=1";
        Cursor cursor      = mSQLiteDatabase.rawQuery(selectQuery, null);
        String k = "";
        if (cursor.moveToFirst()) {
            k = cursor.getString(3);
        }
        cursor.close();
        this.close();
        return k;
    }

    public void markIncomingMessagesRead(String key) {
        this.open(true);
        String query = "UPDATE " + Constants.TABLE_CHAT_LOGS + " SET " + Constants.COLUMN_NAME_HAS_BEEN_READ + "=1 WHERE " + Constants.COLUMN_NAME_KEY + "='" + key +"' AND (type == 2 OR type == 4)";
        mSQLiteDatabase.execSQL(query);
        this.close();
        if (Application.APPDEBUG) {
            Log.d(TAG, " " +  "marked incoming messages as read");
        }
    }

    public void deleteMessage(int id) {
        this.open(true);
        String query = "DELETE FROM " + Constants.TABLE_CHAT_LOGS + " WHERE _id == " + id;
        mSQLiteDatabase.execSQL(query);
        this.close();

        if (Application.APPDEBUG) {
            Log.d(TAG, " " + "Deleted message");
        }
    }

    public ArrayList<Friend> getFriendList() {
        this.open(false);

        ArrayList<Friend> friendList = new ArrayList<Friend>();
        String selectQuery           = "SELECT  * FROM " + Constants.TABLE_FRIENDS;

        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String name       = cursor.getString(1);
                String key        = cursor.getString(0);
                String status     = cursor.getString(2);
                String note       = cursor.getString(3);
                String alias      = cursor.getString(4);
                boolean isOnline  = cursor.getInt(5) != 0 ;
                boolean isBlocked = cursor.getInt(6)>0;

                if(alias == null)
                    alias = "";

                if(!alias.equals(""))
                    name = alias;
                else if(name.equals(""))
                    name = key.substring(0,7);

              //  if(!isBlocked)
              //      friendList.add(new Friend(isOnline, name, status, note, key, alias));

            } while (cursor.moveToNext());
        }

        cursor.close();
        this.close();

        return friendList;
    }

    public boolean doesFriendExist(String key) {
        this.open(false);

        Cursor mCount = mSQLiteDatabase.rawQuery("SELECT count(*) FROM " + Constants.TABLE_FRIENDS
                + " WHERE " + Constants.COLUMN_NAME_KEY + "='" + key + "'", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        if(count > 0) {
            mCount.close();
            this.close();
            return true;
        }
        mCount.close();
        this.close();
        return false;
    }

    public void setAllOffline() {
        this.open(false);
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME_ISONLINE, "0");
        mSQLiteDatabase.update(Constants.TABLE_FRIENDS, values, Constants.COLUMN_NAME_ISONLINE + "='1'", null);
        this.close();
    }

    public void deleteFriend(String key) {
        this.open(false);
        mSQLiteDatabase.delete(Constants.TABLE_FRIENDS, Constants.COLUMN_NAME_KEY + "='" + key + "'", null);
        this.close();
    }

    public void deleteFriendRequest(String key) {
        this.open(false);
        mSQLiteDatabase.delete(Constants.TABLE_FRIEND_REQUEST, Constants.COLUMN_NAME_KEY + "='" + key + "'", null);
        this.close();
    }

    public String getFriendRequestMessage(String key) {
        this.open(false);
        String selectQuery = "SELECT message FROM " + Constants.TABLE_FRIEND_REQUEST + " WHERE ome_key='" + key + "'";

        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
        String message = "";
        if (cursor.moveToFirst()) {
            message = cursor.getString(0);
        }
        cursor.close();
        this.close();

        return message;
    }

    public void deleteChat(String key) {
        this.open(false);
        mSQLiteDatabase.delete(Constants.TABLE_CHAT_LOGS, Constants.COLUMN_NAME_KEY + "='" + key + "'", null);
        this.close();
    }

    public void updateFriendName(String key, String newName) {
        this.open(false);
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME_USERNAME, newName);
        mSQLiteDatabase.update(Constants.TABLE_FRIENDS, values, Constants.COLUMN_NAME_KEY + "='" + key + "'", null);
        this.close();
    }

    public void updateStatusMessage(String key, String newMessage) {
        this.open(false);
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME_NOTE, newMessage);
        mSQLiteDatabase.update(Constants.TABLE_FRIENDS, values, Constants.COLUMN_NAME_KEY + "='" + key + "'", null);
        this.close();
    }

    /*
    public void updateUserStatus(String key, ToxUserStatus status) {
        this.open(false);
        ContentValues values = new ContentValues();
        String tmp = UserStatus.getStringFromToxUserStatus(status);
        values.put(Constants.COLUMN_NAME_STATUS, tmp);
        mSQLiteDatabase.update(Constants.TABLE_FRIENDS, values, Constants.COLUMN_NAME_KEY + "='" + key + "'", null);
        this.close();
    }
    */

    public void updateUserOnline(String key, boolean online) {
        this.open(false);
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME_ISONLINE, online);
        mSQLiteDatabase.update(Constants.TABLE_FRIENDS, values, Constants.COLUMN_NAME_KEY + "='" + key + "'", null);
        this.close();
    }

    public String[] getFriendDetails(String key) {
        String[] details = { null, null, null };

        this.open(false);
        String selectQuery = "SELECT * FROM " + Constants.TABLE_FRIENDS + " WHERE " + Constants.COLUMN_NAME_KEY + "='" + key + "'";
        Cursor cursor      = mSQLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String name  = cursor.getString(1);
                String note  = cursor.getString(3);
                String alias = cursor.getString(4);

                if(name == null)
                    name = "";

                if(name.equals(""))
                    name = key.substring(0, 7);

                details[0] = name;
                details[1] = alias;
                details[2] = note;

            } while (cursor.moveToNext());
        }

        cursor.close();
        this.close();

        return details;
    }

    public void updateAlias(String alias, String key) {
        this.open(true);
        String query = "UPDATE " + Constants.TABLE_FRIENDS + " SET " + Constants.COLUMN_NAME_ALIAS + "='" + alias + "' WHERE " + Constants.COLUMN_NAME_KEY + "='" + key + "'";
        mSQLiteDatabase.execSQL(query);
        this.close();
    }

    public boolean isFriendBlocked(String key) {
        boolean isBlocked  = false;
        this.open(false);
        String selectQuery = "SELECT isBlocked FROM " + Constants.TABLE_FRIENDS + " WHERE " + Constants.COLUMN_NAME_KEY + "='" + key + "'";
        Cursor cursor      = mSQLiteDatabase.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            isBlocked = cursor.getInt(0)>0;
        }
        cursor.close();
        this.close();
        return isBlocked;
    }
}
