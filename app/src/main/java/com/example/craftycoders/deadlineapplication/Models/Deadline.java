package com.example.craftycoders.deadlineapplication.Models;

import java.sql.Timestamp;

/**
 * Created by simonwalker on 30/04/2016.
 */
public class Deadline {
    private int mId;
    private String mTitle;
    private String mNotes;
    private int mStartDate;
    private int mEndDate;
    private float mLocationLat;
    private float mLocationLong;
    private boolean mIsHandedIn;

    public Deadline(){

    }

    public Deadline(int id, String title, String notes, int startDate, int endDate, float locationLat,
                    float locationLong, boolean isHandedIn){
        this.setId(id);
        this.setEndDate(endDate);
        this.setStartDate(startDate);
        this.setIsHandedIn(isHandedIn);
        this.setTitle(title);
        this.setNotes(notes);
        this.setLocationLat(locationLat);
        this.setLocationLong(locationLong);
    }

    public int getId(){return this.mId;}
    public void setId(int id){this.mId = id;}

    public String getTitle(){return this.mTitle;}
    public void setTitle(String title){this.mTitle = title;}

    public String getNotes(){return this.mNotes;}
    public void setNotes(String notes){this.mNotes = notes;}

    public int getStartDate(){return this.mStartDate;};
    public void setStartDate(int startTime){this.mStartDate = startTime;}

    public int getEndDate(){return this.mEndDate;};
    public void setEndDate(int endDate){this.mEndDate = endDate;}

    public float getLocationLat(){return this.mLocationLat;};
    public void setLocationLat(float locationLat){this.mLocationLat = locationLat;}

    public float getLocationLong(){return this.mLocationLong;};
    public void setLocationLong(float locationLong){this.mLocationLong = locationLong;}

    public boolean getIsHandedIn(){return this.mIsHandedIn;};
    public void setIsHandedIn(boolean isHandedIn){this.mIsHandedIn = isHandedIn;}


}
