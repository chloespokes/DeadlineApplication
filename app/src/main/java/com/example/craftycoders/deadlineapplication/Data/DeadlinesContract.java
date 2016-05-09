package com.example.craftycoders.deadlineapplication.Data;

import android.net.Uri;

/**
 * Created by simonwalker on 06/05/2016.
 */
public class DeadlinesContract {

    // Database Name
    public static final String DATABASE_NAME = "deadlineData.db";
    // Database Version
    public static final int DATABASE_VERSION = 3;
    // Deadline Table name
    public static final String DEADLINE_TABLE_NAME = "deadlines";

    // Deadlines Table Columns names
    public static final String KEY_ID = "_ID";
    public static final String KEY_TITLE = "title";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_DUE_DATE = "due_date";
    public static final String KEY_LOC_LAT = "location_lat";
    public static final String KEY_LOC_LONG = "location_long";
    public static final String KEY_HAND_IN = "is_handed_in";
    public static final String KEY_CALENDAR_SYNC = "calendar_sync";

    //URIs
    public static final String PROVIDER_NAME = "com.example.provider.CraftyCoders";
    public static final String URL = "content://" + PROVIDER_NAME + "/deadlines";
    public static final Uri CONTENT_URI = Uri.parse(URL);



}

