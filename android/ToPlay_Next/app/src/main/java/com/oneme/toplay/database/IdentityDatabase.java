package com.oneme.toplay.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;

import java.util.ArrayList;

/**
 * Database for storing playround accounts in local
 * @author Mark Winter
 */
public class IdentityDatabase extends SQLiteOpenHelper {

    private final String TAG          = "IdentityDatabase";

    private String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS users" +
            " ( _id integer primary key , " +
            "username text," +
            "password text," +
            "nickname text," +
            "status text," +
            "status_message text);";

    public IdentityDatabase(Context ctx) {
        // Last int is the database version
        super(ctx, "userdb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * Add user to the user table
     * @param username - username of user
     * @param password - user's password
     */
    public void addUser(String username, String password) {
        SQLiteDatabase db    = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("nickname", username);
        values.put("status", "online");
        values.put("status_message", "Hi! What's up");
        db.insert("users", null, values);
        db.close();
    }

    /**
     * Checks to see if the user details are correct
     * @param username - username of user
     * @return - true if details were correct, false otherwise
     */
    public boolean login(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        if (Application.APPDEBUG) {

            Log.d(TAG, " database " + db.getPath()
                    + " lockedbycurrenThread  " + db.isDbLockedByCurrentThread()
                    + " lockedbyotherThread " + db.isDbLockedByOtherThreads()
                    + " " + db.query("users",
                    null, null, null, null, null, null));

            Log.d(TAG, " database version " + db.getVersion());

        }

        Cursor cursor     = db.rawQuery("SELECT count(*) FROM users WHERE username='" + username + "'", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();

        return count > 0;
    }

    public String[] getUserDetails(String username) {
        String[] details  = new String[3];
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "SELECT * FROM users WHERE username='" + username + "'";
        Cursor cursor     = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            details[0] = cursor.getString(3);
            details[1] = cursor.getString(4);
            details[2] = cursor.getString(5);
        }
        cursor.close();
        db.close();

        return details;
    }

    public void updateUserDetail(String username, String detail, String newDetail) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query      = "UPDATE users SET " + detail + "='" + newDetail + "' WHERE username='" + username + "'";
        db.execSQL(query);
        db.close();
    }

    public boolean doUsersExist() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor     = db.rawQuery("SELECT count(*) FROM users", null);
        cursor.moveToFirst();
        int count         = cursor.getInt(0);

        if (Application.APPDEBUG) {
            Log.d(TAG, " user count \n" + cursor.getCount());
            int i = cursor.getCount();
            while (i-- > 0) {
                Log.d(TAG, "  " + cursor.getString(i)
                        + " " + cursor.getColumnName(i)
                        + " \n" + cursor.getColumnNames());
            }
         }

        cursor.close();
        db.close();

        return count > 0;
    }

    public ArrayList<String> getAllProfiles() {
        ArrayList<String> profiles    = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT username FROM users";

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                profiles.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return profiles;
    }
}
