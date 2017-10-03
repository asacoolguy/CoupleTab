package com.example.ethan.tabapp;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ethan.tabapp.databinding.HistoryFragmentBinding;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Ethan on 9/17/2017.
 */

public class HistoryFragment extends Fragment {
    private Cursor entryCursor;
    private HistoryCursorAdapter cursorAdapter;
    private ArrayList<Integer> allYears;
    private ArrayList<String> allMonths;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("myTag", "starting on Create View");
        View view = inflater.inflate(R.layout.history_fragment, container, false);

        int latestYear = 0;
        int latestMonth = 0;

        // populate spinners with available years and months
        allYears = new ArrayList<Integer>();
        findAllYears();
        latestYear = allYears.get(0);

        allMonths = new ArrayList<String>();
        findAllMonths(latestYear);
        latestMonth = convertMonthToInteger(allMonths.get(0));

        // populate the history listview with entries using the current year and month
        entryCursor = findEntries(latestYear, latestMonth);

        // populate spinners with avaliable months based on the chosen year

        Log.d("myTag", "finishing on Create View");


        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // this is moved here because fragments can only access items inside after view inflation
        ListView listView = (ListView) getActivity().findViewById(R.id.history_listview);
        final Spinner spinner_year = (Spinner) getActivity().findViewById(R.id.spinner_year);
        final Spinner spinner_month = (Spinner) getActivity().findViewById(R.id.spinner_month);
        Button button_filter = (Button) getActivity().findViewById(R.id.button_filter);

        cursorAdapter = new HistoryCursorAdapter(getActivity(), entryCursor, 0);
        listView.setAdapter(cursorAdapter);
        ArrayAdapter<Integer> spinnerYearAdapter = new ArrayAdapter<Integer>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, allYears);

        spinner_year.setAdapter(spinnerYearAdapter);
        final ArrayAdapter<String> spinnerMonthAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, allMonths);
        // update the items in spinner_month based on what's selected in spinner_year
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
            {
                findAllMonths((int)parent.getItemAtPosition(position));
                spinnerMonthAdapter.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> parent)
            {
                // do nothing
            }
        });

        spinner_month.setAdapter(spinnerMonthAdapter);
        spinner_month.setSelection(0);

        button_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = (int)spinner_year.getSelectedItem();
                int month = convertMonthToInteger(String.valueOf(spinner_month.getSelectedItem()));
                cursorAdapter.changeCursor(findEntries(year, month));
            }
        });

    }

    // helper function that finds all entries in a given year and month
    private Cursor findEntries(int year, int month){
        String[] entryProjection = {
                TabEntryDBHelper._ID,
                TabEntryDBHelper.COLUMN_NAME_DATETIME,
                TabEntryDBHelper.COLUMN_NAME_AMOUNT,
                TabEntryDBHelper.COLUMN_NAME_COMMENT,
                TabEntryDBHelper.COLUMN_NAME_APAID
        };
        String selection = TabEntryDBHelper.COLUMN_NAME_YEAR + " = ? AND " +
                TabEntryDBHelper.COLUMN_NAME_MONTH + " = ?";
        String[] selectionArgs = {String.valueOf(year), String.valueOf(month)};
        Cursor c = ((MainActivity) getActivity()).GetDBManager().query(
                TabEntryDBHelper.TABLE_NAME,
                entryProjection,
                selection,
                selectionArgs,
                null,
                null,
                TabEntryDBHelper._ID + " DESC");
        return c;
    }


    // helper function that finds all years with entries
    private void findAllYears(){
        allYears.clear();

        String[] yearProjection = {TabEntryDBHelper.COLUMN_NAME_YEAR};
        Cursor yearCursor =  ((MainActivity) getActivity()).GetDBManager().query(
                TabEntryDBHelper.TABLE_NAME,
                yearProjection,
                null,
                null,
                null,
                null,
                TabEntryDBHelper.COLUMN_NAME_YEAR + " DESC");

        while(yearCursor.moveToNext()) {
            int year = yearCursor.getInt(
                    yearCursor.getColumnIndexOrThrow(TabEntryDBHelper.COLUMN_NAME_YEAR));
            if (!allYears.contains(year)){
                allYears.add(year);
            }
        }
        yearCursor.close();
    }

    // helper function that finds all months with entries in a given year
    private void findAllMonths(int year){
        allMonths.clear();

        String[] monthProjection = {TabEntryDBHelper.COLUMN_NAME_MONTH};
        String selection = TabEntryDBHelper.COLUMN_NAME_YEAR + " = ?";
        String[] selectionArgs = {String.valueOf(year)};

        Cursor monthCursor =  ((MainActivity) getActivity()).GetDBManager().query(
                TabEntryDBHelper.TABLE_NAME,
                monthProjection,
                selection,
                selectionArgs,
                null,
                null,
                TabEntryDBHelper.COLUMN_NAME_MONTH + " DESC");

        while(monthCursor.moveToNext()) {
            int month =  monthCursor.getInt(
                    monthCursor.getColumnIndexOrThrow(TabEntryDBHelper.COLUMN_NAME_MONTH));
            String monthString = convertMonthToString(month);
            if (!allMonths.contains(monthString)){
                allMonths.add(monthString);
            }
        }
        monthCursor.close();
    }

    static public String convertMonthToString(int month){
        String monthString = "";
        switch (month){
            default:
            case 0:
                monthString = "January";
                break;
            case 1:
                monthString = "February";
                break;
            case 2:
                monthString = "March";
                break;
            case 3:
                monthString = "April";
                break;
            case 4:
                monthString = "May";
                break;
            case 5:
                monthString = "June";
                break;
            case 6:
                monthString = "July";
                break;
            case 7:
                monthString = "August";
                break;
            case 8:
                monthString = "September";
                break;
            case 9:
                monthString = "October";
                break;
            case 10:
                monthString = "November";
                break;
            case 11:
                monthString = "December";
                break;

        }
        return monthString;
    }

    static public int convertMonthToInteger(String monthString){
        int month = 0;
        switch (monthString){
            default:
            case "January":
                month = 0;
                break;
            case "February":
                month = 1;
                break;
            case "March":
                month = 2;
                break;
            case "April":
                month = 3;
                break;
            case "May":
                month = 4;
                break;
            case "June":
                month = 5;
                break;
            case "July":
                month = 6;
                break;
            case "August":
                month = 7;
                break;
            case "September":
                month = 8;
                break;
            case "October":
                month = 9;
                break;
            case "November":
                month = 10;
                break;
            case "December":
                month = 11;
                break;

        }
        return month;
    }
}
