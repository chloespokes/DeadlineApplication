package com.example.craftycoders.deadlineapplication.Models;

import java.sql.Timestamp;

/**
 * Created by simonwalker on 30/04/2016.
 */
public class Deadline {
    private String mTitle;
    private String mNotes;
    private long mDueDate;
    private float mLocationLat;
    private float mLocationLong;
    private boolean mIsHandedIn;

    public Deadline(){

    }

    public Deadline(String title, String notes, long dueDate, float locationLat,
                    float locationLong, boolean isHandedIn){
        this.setDueDate(dueDate);
        this.setIsHandedIn(isHandedIn);
        this.setTitle(title);
        this.setNotes(notes);
        this.setLocationLat(locationLat);
        this.setLocationLong(locationLong);
    }


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


}
