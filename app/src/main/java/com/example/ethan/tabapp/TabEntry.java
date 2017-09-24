package com.example.ethan.tabapp;

/**
 * Created by Ethan on 9/19/2017.
 * simple data class that stores info from entries
 */

public class TabEntry {
    private String dateTime;
    private String comment;
    private String amount;
    private boolean Apaid; // true if this transaction is paid by A, otherwise it's B

    public TabEntry(String dt, String c, String a, boolean p){
        dateTime = dt;
        comment = c;
        amount = a;
        Apaid = p;
    }

    public String getDateTime(){
        return dateTime;
    }

    public String getComment(){
        return comment;
    }

    public String getAmount(){
        return amount;
    }

    public boolean getAPaid(){
        return Apaid;
    }
}
