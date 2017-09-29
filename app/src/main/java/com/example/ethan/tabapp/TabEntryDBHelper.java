package com.example.ethan.tabapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ethan on 9/26/2017.
 * Helper class that extends SQLiteOpenHelper. Contains basic SQL commands and names of columns.
 */

public class TabEntryDBHelper extends SQLiteOpenHelper {
    // database info
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TabEntries.db";

    // table info
    public static final String TABLE_NAME = "tab_entries";
    public static final String _ID = "_id";
    public static final String COLUMN_NAME_DATETIME = "date_time";
    public static final String COLUMN_NAME_YEAR = "year";
    public static final String COLUMN_NAME_MONTH = "month";
    public static final String COLUMN_NAME_AMOUNT = "amount";
    public static final String COLUMN_NAME_COMMENT = "comment";
    public static final String COLUMN_NAME_APAID = "apaid";

    public TabEntryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_YEAR + " INTEGER," +
                    COLUMN_NAME_MONTH + " INTEGER," +
                    COLUMN_NAME_DATETIME + " TEXT," +
                    COLUMN_NAME_AMOUNT + " FLOAT," +
                    COLUMN_NAME_COMMENT + " TEXT," +
                    COLUMN_NAME_APAID + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

}

