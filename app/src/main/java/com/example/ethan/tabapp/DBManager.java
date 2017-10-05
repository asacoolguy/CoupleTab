package com.example.ethan.tabapp;

import android.arch.persistence.room.Update;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Ethan on 9/26/2017.
 * Helper class to manage all database related actions. Has the only instance of the database.
 * All database interactions must be done through this class.
 */

public class DBManager {
    private TabEntryDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    private boolean databaseOpened;

    public DBManager(Context c){
        context = c;
        databaseOpened = false;
    }

    public DBManager open() throws SQLException{
        dbHelper = new TabEntryDBHelper(context);
        database = dbHelper.getWritableDatabase();
        //dbHelper.onUpgrade(database,0,1);
        databaseOpened = true;
        return this;
    }

    public void close(){
        dbHelper.close();
        databaseOpened = false;
    }

    public void insert(int year, int month, String dateTime, String comment, double amount,
                       int whoPaid, int forBoth){
        if (databaseOpened){
            ContentValues value = new ContentValues();
            value.put(dbHelper.COLUMN_NAME_YEAR, year);
            value.put(dbHelper.COLUMN_NAME_MONTH, month);
            value.put(dbHelper.COLUMN_NAME_DATETIME, dateTime);
            value.put(dbHelper.COLUMN_NAME_COMMENT, comment);
            value.put(dbHelper.COLUMN_NAME_AMOUNT, amount);
            value.put(dbHelper.COLUMN_NAME_WHOPAID, whoPaid);
            value.put(dbHelper.COLUMN_NAME_FORBOTH, forBoth);
            database.insert(dbHelper.TABLE_NAME, null, value);
        }
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs,
                        String groupBy, String having, String orderBy){
        if (databaseOpened){
            return database.query(table, columns, selection,
                    selectionArgs, groupBy, having, orderBy);
        }
        else{
            return null;
        }
    }

    public void delete(int id){
        if (databaseOpened){
            // Define 'where' part of query.
            String selection = TabEntryDBHelper._ID + " = ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = {String.valueOf(id)};
            // Issue SQL statement.
            database.delete(TabEntryDBHelper.TABLE_NAME, selection, selectionArgs);
        }

    }

}
