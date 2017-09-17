package com.example.ethan.tabapp;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import java.text.DecimalFormat;

/**
 * Created by Ethan on 5/24/2017.
 * helper class that keeps the values of currentTab and the pendingTab
 * handles all changing and displaying the two values
 */

public class TabValue{
    private double currentTab;
    private double pendingTab;
    private DecimalFormat decimalFormat;
    public ObservableField<String> currentTabString;
    public ObservableField<String> pendingTabString;
    public ObservableField<Integer> pendingDirection; // A paying = pos. B paying = neg.
    public ObservableField<Boolean> AOwesMoney;
    public ObservableField<Boolean> BOwesMoney;

    public TabValue(){
        currentTab = 0.0;
        pendingTab = 0.0;
        decimalFormat = new DecimalFormat("#.##");
        pendingDirection = new ObservableField<Integer>();
        pendingDirection.set(1);
        AOwesMoney = new ObservableField<Boolean>();
        AOwesMoney.set(false);
        BOwesMoney = new ObservableField<Boolean>();
        BOwesMoney.set(false);

        currentTabString = new ObservableField<String>();
        currentTabString.set("0");
        pendingTabString = new ObservableField<String>();
        pendingTabString.set("");
    }

    // method to load input into currentTab
    public void loadCurrentTabValue(String s){
        currentTabString.set(s);
        currentTab = Double.parseDouble(s);
        if (currentTab > 0){
            AOwesMoney.set(true);
        }
        else{
            BOwesMoney.set(true);
        }
    }

    // method to handle when a number button gets clicked. updates the pendingValue.
    public void onNumberClick(int i) {
        if (pendingTab == 0.0 && i == 0) {
            return; // nothing needs to be updated
        }

        pendingTabString.set(pendingTabString.get() + String.valueOf(i));
        pendingTab = Double.parseDouble(pendingTabString.get());
    }

    // method to handle when the decimal button gets clicked. enters decimal mode accordingly
    public void onDecimalClick(){
        if (!pendingTabString.get().contains(".")){
            pendingTabString.set(pendingTabString.get() + ".");
        }
    }

    // method to handle when the delete button gets clicked. removes one space
    public void onDeleteClick(){
        int l = pendingTabString.get().length();
        if (l > 1){
            pendingTabString.set(pendingTabString.get().substring(0, l - 1));
            pendingTab = Double.parseDouble(pendingTabString.get());
        }
        else if (l == 1){
            pendingTabString.set("");
            pendingTab = 0;
        }
    }

    // method to handle when the enter button gets clicked.
    // adds the pendingTab to the tab and resets pendingTab
    public void onEnterClick(){
        if (pendingTab != 0){
            currentTab = currentTab + (pendingDirection.get() * pendingTab) / 2.0;
            if (currentTab > 0){
                AOwesMoney.set(true);
                BOwesMoney.set(false);
                currentTabString.set(decimalFormat.format(currentTab));
            }
            else{
                BOwesMoney.set(true);
                AOwesMoney.set(false);
                currentTabString.set(decimalFormat.format(-1 * currentTab));
            }


            pendingTab = 0;
            pendingTabString.set("");
        }
    }


    // helper method. takes in a tab value and converts it into appropirate string to display
    private String valueToString(double value) {
        // if the double value is an int, then display just the interger
        if ((value % 1) == 0) {
            return String.valueOf((int) value);
        }
        // otherwise display only the first 2 decimal places
        else{
            return String.valueOf(decimalFormat.format(value));
        }
    }
}
