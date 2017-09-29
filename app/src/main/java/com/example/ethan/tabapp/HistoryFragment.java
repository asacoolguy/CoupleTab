package com.example.ethan.tabapp;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ethan.tabapp.databinding.HistoryFragmentBinding;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ethan on 9/17/2017.
 */

public class HistoryFragment extends Fragment {
    private HistoryFragmentBinding binding;
    private TabValue tabValue;
    private Cursor cursor;
    private HistoryCursorAdapter cursorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("myTag", "starting on Create View");
        binding = DataBindingUtil.inflate(inflater,
                R.layout.history_fragment, container, false);
        View view = binding.getRoot();

        tabValue = ((MainActivity)getActivity()).GetTabValue();
        binding.setTabValue(tabValue);

        String[] mProjection = {
                TabEntryDBHelper._ID,
                TabEntryDBHelper.COLUMN_NAME_DATETIME,
                TabEntryDBHelper.COLUMN_NAME_AMOUNT,
                TabEntryDBHelper.COLUMN_NAME_COMMENT,
                TabEntryDBHelper.COLUMN_NAME_APAID
        };

        // query database
        //TODO maybe do this in a different thread?
        cursor =  ((MainActivity) getActivity()).GetDBManager().query(
                TabEntryDBHelper.TABLE_NAME,
                mProjection,
                null,
                null,
                null,
                null,
                null);

        /*Log.d("myTag", "test print out of cursor contents");
        while(cursor.moveToNext()) {
            String comment = cursor.getString(
                    cursor.getColumnIndexOrThrow(TabEntryDBHelper.COLUMN_NAME_COMMENT));
            Log.d("myTag", comment);
        }
        Log.d("myTag","finished test print out of cursor contents");
        */
        Log.d("myTag", "finishing on Create View");

        return view;
    }




    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // this is moved here because fragments can only access items inside after view inflation
        ListView listView = (ListView) getActivity().findViewById(R.id.history_listview);
        //entries = ((MainActivity)getActivity()).GetTabEntries();
        //HistoryArrayAdaptor adapter = new HistoryArrayAdaptor(getActivity(), entries);
        //Log.d("myTag",listView.toString());
        //listView.setAdapter(adapter);
        cursorAdapter = new HistoryCursorAdapter(getActivity(), cursor, 0);
        listView.setAdapter(cursorAdapter);
    }


}
