package com.example.craftycoders.deadlineapplication.Models;

import java.sql.Timestamp;

/**
 * Created by simonwalker on 30/04/2016.
 */
public class Deadline {
    private int mId;
    private String mTitle;
    private String mNotes;
    private long mDueDate;
    private float mLocationLat;
    private float mLocationLong;
    private boolean mIsHandedIn;
    private boolean mCalendarSync;

    public Deadline(){

    }

    public Deadline(int id, String title, String notes, long dueDate, float locationLat,
                    float locationLong, boolean isHandedIn, boolean calendarSync){
        this.setId(id);
        this.setDueDate(dueDate);
        this.setIsHandedIn(isHandedIn);
        this.setTitle(title);
        this.setNotes(notes);
        this.setLocationLat(locationLat);
        this.setLocationLong(locationLong);
        this.setCalendarSync(calendarSync);
    }

    public int getId(){return this.mId;}
    public void setId(int id){this.mId = id;}

    public String getTitle(){return this.mTitle;}
    public void setTitle(String title){this.mTitle = title;}

    public String getNotes(){return this.mNotes;}
    public void setNotes(String notes){this.mNotes = notes;}

    public long getDueDate(){return this.mDueDate;};
    public void setDueDate(long dueDate){this.mDueDate = dueDate;}

    public float getLocationLat(){return this.mLocationLat;};
    public void setLocationLat(float locationLat){this.mLocationLat = locationLat;}

    public float getLocationLong(){return this.mLocationLong;};
    public void setLocationLong(float locationLong){this.mLocationLong = locationLong;}

    public boolean getIsHandedIn(){return this.mIsHandedIn;};
    public void setIsHandedIn(boolean isHandedIn){this.mIsHandedIn = isHandedIn;}

    public boolean getCalendarSync(){return this.mCalendarSync;};
    public void setCalendarSync(boolean calendarSync){this.mIsHandedIn = calendarSync;}


}
