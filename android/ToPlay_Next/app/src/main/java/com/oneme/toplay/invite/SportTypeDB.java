/*
* Copyright 2014 OneME
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.oneme.toplay.invite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteDatabase.CursorFactory;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

public class SportTypeDB {

    public static final String KEY_TITLE = "colourName";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "SpinnerDBHelper";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String DATABASE_CREATE =
            "create table colours (_id integer primary key autoincrement, "
                    + "colourName text not null);";

    private static final String DATABASE_NAME = "dbdb";
    private static final String DATABASE_TABLE = "colours";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    public SportTypeDB(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws android.database.SQLException if the database could be neither opened or created
     */

    public SportTypeDB open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param newColourName the title of the note
     * @param body the body of the note
     * @return rowId or -1 if failed
     */
    public long createEntry(String newColourName) {

        if (mDb == null)
        {
            this.open();
        }

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, newColourName);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }


    public boolean deleteEntry(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteAll() {

        if (mDb == null)
        {
            this.open();
        }

        return mDb.delete(DATABASE_TABLE, null, null) > 0;
    }

    public Cursor fetchAllColours() {

        if (mDb == null)
        {
            this.open();
        }

        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE}, null, null, null, null, null);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS colours");
            onCreate(db);
        }

    }
}


