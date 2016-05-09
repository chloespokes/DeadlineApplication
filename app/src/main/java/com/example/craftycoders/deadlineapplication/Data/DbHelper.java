package com.example.craftycoders.deadlineapplication.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.craftycoders.deadlineapplication.Models.Deadline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cols3 on 02/05/2016.
 */

public class DbHelper extends SQLiteOpenHelper{

    private static DbHelper mInstance = null;

    // Database Name
    public static final String DATABASE_NAME = "deadlineData.db";
    // Database Version
    public static final int DATABASE_VERSION = 2;
    // Deadline table name
    private static final String DEADLINE_TABLE_NAME = "deadlines";

    // Deadlines Table Columns names
    private static final String KEY_ID = "_ID";
    private static final String KEY_TITLE = "title";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_DUE_DATE = "due_date";
    private static final String KEY_LOC_LAT = "location_lat";
    private static final String KEY_LOC_LONG = "location_long";
    private static final String KEY_HAND_IN = "is_handed_in";
    private static final String KEY_CALENDAR_SYNC = "calendar_sync";

    //CREATE TABLE deadline (_ID TEXT, title TEXT, notes TEXT, etc.);
    private static final String DEADLINE_TABLE_CREATE =
            "CREATE TABLE " + DEADLINE_TABLE_NAME + "("
                    + KEY_ID + " INTEGER PRIMARY KEY ASC,"
                    + KEY_TITLE + " TEXT,"
                    + KEY_NOTES + " TEXT,"
                    + KEY_DUE_DATE + " LONG,"
                    + KEY_LOC_LAT + " FLOAT,"
                    + KEY_LOC_LONG + " FLOAT,"
                    + KEY_HAND_IN + " BOOLEAN"
                    + KEY_CALENDAR_SYNC + " BOOLEAN"
                    +")";

    private Context mCxt;

    public static DbHelper getInstance(Context context) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new DbHelper(context);
        }
        return mInstance;
    }


    //Constructor
    public DbHelper(Context context) {
        super(context, DeadlinesContract.DATABASE_NAME, null, DeadlinesContract.DATABASE_VERSION);
        this.mCxt = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try
        {
            db.execSQL(DEADLINE_TABLE_CREATE);
            Log.d("Success: ", "Database created..");
        }
        catch(Exception e)
        {
            Log.d("Failed to create db", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DeadlinesContract.DEADLINE_TABLE_NAME);
        // Creating tables again
        onCreate(db);
    }

    //Adds new deadline to database
    public long addDeadline(ContentValues deadlineValues){
        SQLiteDatabase db = this.getWritableDatabase();

//        ContentValues values = new ContentValues();
//        values.put(KEY_ID, deadline.getId());
//        values.put(KEY_TITLE, deadline.getTitle());
//        values.put(KEY_NOTES, deadline.getNotes());
//        values.put(KEY_DUE_DATE, deadline.getDueDate());
//        values.put(KEY_LOC_LAT, deadline.getLocationLat());
//        values.put(KEY_LOC_LONG, deadline.getLocationLong());;
//        values.put(KEY_HAND_IN, deadline.getIsHandedIn());
//        values.put(KEY_CALENDAR_SYNC, deadline.getIsCalendarSync());

        //Inserting Row
        long rowID = db.insert(DeadlinesContract.DEADLINE_TABLE_NAME, null, deadlineValues);
        db.close();//Closes db connection
        Log.d("DBHELPER: ", "deadline successfully added");

        return rowID;
    }

    //getDeadline: Takes a deadline name as input and returns a deadline object
    public Deadline getDeadline(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DeadlinesContract.DEADLINE_TABLE_NAME, new String[]{
                DeadlinesContract.KEY_ID,
                DeadlinesContract.KEY_TITLE,
                DeadlinesContract.KEY_NOTES,
                DeadlinesContract.KEY_DUE_DATE,
                DeadlinesContract.KEY_LOC_LAT,
                DeadlinesContract.KEY_LOC_LONG,
                DeadlinesContract.KEY_HAND_IN,
                DeadlinesContract.KEY_CALENDAR_SYNC
        }, DeadlinesContract.KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Deadline deadline = new Deadline(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Long.parseLong(cursor.getString(3)),
                Float.parseFloat(cursor.getString(4)),
                Float.parseFloat(cursor.getString(5)),
                Boolean.parseBoolean(cursor.getString(6)),
                Boolean.parseBoolean(cursor.getString(6))
        );
        return deadline;
    }

    //getAllDeadlines:  returns a List of all deadline in the database
    public List<Deadline> getAllDeadlines(){
        List<Deadline> deadlineList = new ArrayList<Deadline>();

        //Select All Query
        String selectQuery = "SELECT * FROM " + DeadlinesContract.DEADLINE_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Loop through rows and add to list
        if(cursor.moveToFirst()){
            do{
                Deadline deadline = new Deadline(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        Long.parseLong(cursor.getString(3)),
                        Float.parseFloat(cursor.getString(4)),
                        Float.parseFloat(cursor.getString(5)),
                        Boolean.parseBoolean(cursor.getString(6)),
                        Boolean.parseBoolean(cursor.getString(6))
                );
                //Add to list
                deadlineList.add(deadline);
            }while(cursor.moveToNext());
        }

        //Return Deadline
        return deadlineList;

    }

    //getAllDeadlines:  returns a List of all deadline in the database
    public Cursor getAllDeadlinesProvider(){

        //Select All Query
        String selectQuery = "SELECT * FROM " + DeadlinesContract.DEADLINE_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(selectQuery, null);

    }

    //getAllDeadlines:  returns a List of all deadline in the database
    public Cursor getDeadline(String deadlineId){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DeadlinesContract.DEADLINE_TABLE_NAME, new String[]{
                DeadlinesContract.KEY_ID,
                DeadlinesContract.KEY_TITLE,
                DeadlinesContract.KEY_NOTES,
                DeadlinesContract.KEY_DUE_DATE,
                DeadlinesContract.KEY_LOC_LAT,
                DeadlinesContract.KEY_LOC_LONG,
                DeadlinesContract.KEY_HAND_IN,
                DeadlinesContract.KEY_CALENDAR_SYNC
        }, DeadlinesContract.KEY_ID + "=?", new String[]{deadlineId}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }


    //takes an updated deadline object and return
    public int updateDeadline(ContentValues deadlineValues, String deadlineId) {
        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(KEY_TITLE, deadline.getTitle());
//        values.put(KEY_NOTES, deadline.getNotes());
//        values.put(KEY_DUE_DATE, deadline.getDueDate());
//        values.put(KEY_LOC_LAT, deadline.getLocationLat());
//        values.put(KEY_LOC_LONG, deadline.getLocationLong());
//        values.put(KEY_HAND_IN, deadline.getIsHandedIn());
//        values.put(KEY_CALENDAR_SYNC, deadline.getIsCalendarSync());
        // updating row
        return db.update(DeadlinesContract.DEADLINE_TABLE_NAME, deadlineValues, DeadlinesContract.KEY_ID + " = ?",
                new String[]{deadlineId});
    }

    //DELETE RECORD
    public int deleteDeadline(String deadlineId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = db.delete(DeadlinesContract.DEADLINE_TABLE_NAME, DeadlinesContract.KEY_ID + " = ?",
                new String[]{deadlineId});
        db.close();

        return count;
    }

    //DELETE RECORD
    public int deleteAllDeadlines(String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = db.delete(DeadlinesContract.DEADLINE_TABLE_NAME, selection, selectionArgs);

        db.close();

        return count;
    }
}
