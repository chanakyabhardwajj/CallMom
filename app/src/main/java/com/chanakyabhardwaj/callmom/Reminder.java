package com.chanakyabhardwaj.callmom;

import android.app.DownloadManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by cb on 11/23/14.
 */
public class Reminder {
    //the contact URI chosen from the contacts list.
    //the uri is stored as a string.
    private String contact;

    //the time interval between 2 reminders
    //i.e. the user will be reminded after every "interval" milliseconds
    private long interval;

    public Reminder(String contact, long interval){
        this.contact = contact;
        this.interval = interval;
    }

    public String getContact(){
        return this.contact;
    }

    public long getInterval(){
        return this.interval;
    }
}
