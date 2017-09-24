package com.example.ethan.tabapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ethan on 9/19/2017.
 */

public class HistoryArrayAdaptor extends ArrayAdapter<TabEntry> {
    private Context context;
    private ArrayList<TabEntry> entries;

    public HistoryArrayAdaptor(Context c, ArrayList<TabEntry> e){
        super(c, -1, e);
        context = c;
        entries = e;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View historyListItem = inflater.inflate(R.layout.history_list_item, parent, false);
        TextView dateTextView = (TextView) historyListItem.findViewById(R.id.history_list_date);
        TextView amountTextView = (TextView) historyListItem.findViewById(R.id.history_list_amount);
        TextView paidbyTextView = (TextView) historyListItem.findViewById(R.id.history_list_paidby);
        TextView commentTextView = (TextView) historyListItem.findViewById(R.id.history_list_comment);

        TabEntry entry = entries.get(position);
        dateTextView.setText(entry.getDateTime());
        amountTextView.setText("$" + String.valueOf(entry.getAmount()));
        if(entry.getAPaid()){
            paidbyTextView.setText("Ethan paid");
        }
        else{
            paidbyTextView.setText("Grace paid");
        }
        commentTextView.setText(entry.getComment());

        return historyListItem;
    }
}
