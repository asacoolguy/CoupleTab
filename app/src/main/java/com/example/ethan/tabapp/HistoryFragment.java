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
import android.widget.ArrayAdapter;
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
    private HistoryFragmentBinding binding;
    private TabValue tabValue;
    private Cursor entryCursor;
    private HistoryCursorAdapter cursorAdapter;
    private ArrayList<Integer> allYears;
    private ArrayList<String> allMonths;
    private int currentYear;
    private int currentMonth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("myTag", "starting on Create View");
        binding = DataBindingUtil.inflate(inflater,
                R.layout.history_fragment, container, false);
        View view = binding.getRoot();

        tabValue = ((MainActivity)getActivity()).GetTabValue();
        binding.setTabValue(tabValue);

        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH) + 1;

        // populate the history listview with entries using the current year and month
        // TODO: remember to make sure to account for edgecase where thre is no entry in the current year/month
        String[] entryProjection = {
                TabEntryDBHelper._ID,
                TabEntryDBHelper.COLUMN_NAME_DATETIME,
                TabEntryDBHelper.COLUMN_NAME_AMOUNT,
                TabEntryDBHelper.COLUMN_NAME_COMMENT,
                TabEntryDBHelper.COLUMN_NAME_APAID
        };
        entryCursor =  ((MainActivity) getActivity()).GetDBManager().query(
                TabEntryDBHelper.TABLE_NAME,
                entryProjection,
                null, // TODO: add search conditions to specify current year and month
                null,
                null,
                null,
                null); // TODO: sort them from recent to farback

        // populate spinners with available years and months
        allYears = new ArrayList<Integer>();
        findAllYears();
        allMonths = new ArrayList<String>();
        findAllMonths(currentYear);

        // populate spinners with avaliable months based on the chosen year

        Log.d("myTag", "finishing on Create View");


        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // this is moved here because fragments can only access items inside after view inflation
        ListView listView = (ListView) getActivity().findViewById(R.id.history_listview);
        Spinner spinner_year = (Spinner) getActivity().findViewById(R.id.spinner_year);
        Spinner spinner_month = (Spinner) getActivity().findViewById(R.id.spinner_month);

        cursorAdapter = new HistoryCursorAdapter(getActivity(), entryCursor, 0);
        listView.setAdapter(cursorAdapter);
        ArrayAdapter<Integer> spinnerYearAdapter = new ArrayAdapter<Integer>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, allYears);
        spinner_year.setAdapter(spinnerYearAdapter);
        ArrayAdapter<String> spinnerMonthAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, allMonths);
        spinner_month.setAdapter(spinnerMonthAdapter);
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
                null); // TODO: dont forget to sort the years

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
        Cursor monthCursor =  ((MainActivity) getActivity()).GetDBManager().query(
                TabEntryDBHelper.TABLE_NAME,
                monthProjection,
                null, // TODO: add sql selection code here that specifies which year
                null,
                null,
                null,
                null); // TODO: dont forget to sort the months

        while(monthCursor.moveToNext()) {
            int month = monthCursor.getInt(
                    monthCursor.getColumnIndexOrThrow(TabEntryDBHelper.COLUMN_NAME_MONTH));
            String monthString = MainActivity.convertMonthToString(month);
            if (!allMonths.contains(monthString)){
                allMonths.add(monthString);
            }
        }
        monthCursor.close();
    }
}
