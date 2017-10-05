package com.example.ethan.tabapp;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
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
    private HistoryCursorAdapter cursorAdapter;
    private ArrayList<Integer> allYears;
    private ArrayList<String> allMonths;
    private Spinner spinner_year, spinner_month;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // this is moved here because fragments can only access items inside after view inflation
        ListView history_listview = (ListView) getActivity().findViewById(R.id.history_listview);
        registerForContextMenu(history_listview);

        spinner_year = (Spinner) getActivity().findViewById(R.id.spinner_year);
        spinner_month = (Spinner) getActivity().findViewById(R.id.spinner_month);
        Button button_filter = (Button) getActivity().findViewById(R.id.button_filter);

        int latestYear = 0;
        int latestMonth = 0;
        // find all the avaliable years and months as well as the latest entries
        allYears = new ArrayList<Integer>();
        findAllYears();
        if (allYears.size() > 0) {
            latestYear = allYears.get(0);
        }

        allMonths = new ArrayList<String>();
        findAllMonths(latestYear);
        if (allMonths.size() > 0) {
            latestMonth = convertMonthToInteger(allMonths.get(0));
        }

        // populate the Listview with entries from the current/latest year/month
        cursorAdapter = new HistoryCursorAdapter(
                getActivity(), findEntries(latestYear, latestMonth), 0);
        history_listview.setAdapter(cursorAdapter);

        // populate options for the year spinner
        ArrayAdapter<Integer> spinnerYearAdapter = new ArrayAdapter<Integer>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, allYears);
        spinner_year.setAdapter(spinnerYearAdapter);

        // populate options for the month spinner
        final ArrayAdapter<String> spinnerMonthAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, allMonths);
        spinner_month.setAdapter(spinnerMonthAdapter);
        spinner_month.setSelection(0);

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
        // when filter button is clicked, update the listview with entries from the selected times
        button_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateHistoryListView();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.history_floating_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.floating_menu_delete:
                // re-adjust current tab to reflect the entry being deleted
                double pendingAmount = findPendingAmount((int)info.id);
                ((MainActivity)getActivity()).ChangeTabValue(-1 * pendingAmount);
                ((MainActivity)getActivity()).GetDBManager().delete((int)info.id);
                updateHistoryListView();
                Toast.makeText(getActivity(), "Entry deleted. $" +
                        String.valueOf(Math.abs(pendingAmount)) +
                        " returned to the tab.",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    // helper function that finds all entries in a given year and month
    private Cursor findEntries(int year, int month){
        String[] entryProjection = {
                TabEntryDBHelper._ID,
                TabEntryDBHelper.COLUMN_NAME_DATETIME,
                TabEntryDBHelper.COLUMN_NAME_AMOUNT,
                TabEntryDBHelper.COLUMN_NAME_COMMENT,
                TabEntryDBHelper.COLUMN_NAME_WHOPAID,
                TabEntryDBHelper.COLUMN_NAME_FORBOTH
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

    // helper function that finds the adjusted pending amount of an entry given its id
    private double findPendingAmount(int id){
        String[] projection = {
                TabEntryDBHelper.COLUMN_NAME_AMOUNT,
                TabEntryDBHelper.COLUMN_NAME_WHOPAID,
                TabEntryDBHelper.COLUMN_NAME_FORBOTH
        };
        String selection = TabEntryDBHelper._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor c = ((MainActivity) getActivity()).GetDBManager().query(
                TabEntryDBHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
        c.moveToFirst();
        double pendingAmount = c.getDouble(c.getColumnIndexOrThrow(TabEntryDBHelper.COLUMN_NAME_AMOUNT));
        int pendingDirection = c.getInt(c.getColumnIndexOrThrow(TabEntryDBHelper.COLUMN_NAME_WHOPAID));
        int pendingFactor = c.getInt(c.getColumnIndexOrThrow(TabEntryDBHelper.COLUMN_NAME_FORBOTH));
        return pendingAmount * pendingDirection / pendingFactor;
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

    // helper function that updates the history listview with the latest variables
    private void updateHistoryListView(){
        int year = (int)spinner_year.getSelectedItem();
        int month = convertMonthToInteger(String.valueOf(spinner_month.getSelectedItem()));
        cursorAdapter.changeCursor(findEntries(year, month));
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
