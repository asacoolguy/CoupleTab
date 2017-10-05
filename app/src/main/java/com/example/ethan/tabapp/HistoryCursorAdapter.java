package com.example.ethan.tabapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ethan on 9/28/2017.
 */

public class HistoryCursorAdapter extends CursorAdapter {
    private LayoutInflater inflater;

    public HistoryCursorAdapter(Context context, Cursor cursor, int flags){
        super (context, cursor, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor){
        TextView dateTextView = (TextView) view.findViewById(R.id.history_list_date);
        TextView amountTextView = (TextView) view.findViewById(R.id.history_list_amount);
        TextView paidbyTextView = (TextView) view.findViewById(R.id.history_list_paidby);
        TextView commentTextView = (TextView) view.findViewById(R.id.history_list_comment);

        dateTextView.setText(cursor.getString(
                cursor.getColumnIndex(TabEntryDBHelper.COLUMN_NAME_DATETIME)));
        amountTextView.setText("$" + cursor.getString(
                cursor.getColumnIndex(TabEntryDBHelper.COLUMN_NAME_AMOUNT)));
        String paidbyString = "";
        String paidforTarget = "";
        if (cursor.getInt(cursor.getColumnIndex(TabEntryDBHelper.COLUMN_NAME_WHOPAID)) > 0){
            paidbyString = "Grace paid";
            paidforTarget = "Ethan";
        }
        else{
            paidbyString = "Ethan paid";
            paidforTarget = "Grace";
        }
        if (cursor.getInt(cursor.getColumnIndex(TabEntryDBHelper.COLUMN_NAME_FORBOTH)) > 1){
            paidbyString += " for both";
        }
        else{
            paidbyString += " for " + paidforTarget;
        }
        paidbyTextView.setText(paidbyString);
        commentTextView.setText(cursor.getString(
                cursor.getColumnIndex(TabEntryDBHelper.COLUMN_NAME_COMMENT)));
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent){
        View historyListItem = inflater.inflate(R.layout.history_list_item, parent, false);
        return historyListItem;
    }
}
