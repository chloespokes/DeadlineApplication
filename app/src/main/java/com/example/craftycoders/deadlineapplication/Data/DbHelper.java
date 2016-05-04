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
    private static final String KEY_LOC_LONG = "location_log";
    private static final String KEY_HAND_IN = "is_handed_in";

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
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        db.execSQL("DROP TABLE IF EXISTS " + DEADLINE_TABLE_NAME);
        // Creating tables again
        onCreate(db);
    }

    //Adds new deadline to database
    public void addDeadline(Deadline deadline){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, deadline.getId());
        values.put(KEY_TITLE, deadline.getTitle());
        values.put(KEY_NOTES, deadline.getNotes());
        values.put(KEY_DUE_DATE, deadline.getDueDate());
        values.put(KEY_LOC_LAT, deadline.getLocationLat());
        values.put(KEY_LOC_LONG, deadline.getLocationLong());;
        values.put(KEY_HAND_IN, deadline.getIsHandedIn());

        //Inserting Row
        db.insert(DEADLINE_TABLE_NAME, null, values);
        db.close();//Closes db connection
    }

    //getDeadline: Takes a deadline name as input and returns a deadline object
    public Deadline getDeadline(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DEADLINE_TABLE_NAME, new String[]{
                KEY_ID,
                KEY_TITLE,
                KEY_NOTES,
                KEY_DUE_DATE,
                KEY_LOC_LAT,
                KEY_LOC_LONG,
                KEY_HAND_IN
        }, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Deadline deadline = new Deadline(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Long.parseLong(cursor.getString(3)),
                Float.parseFloat(cursor.getString(4)),
                Float.parseFloat(cursor.getString(5)),
                Boolean.parseBoolean(cursor.getString(6))
        );
        return deadline;
    }

    //getAllDeadlines: Takes returns a List of all deadline in the database
    public List<Deadline> getAllDeadlines(){
        List<Deadline> deadlineList = new ArrayList<Deadline>();

        //Select All Query
        String selectQuery = "SELECT * FROM " + DEADLINE_TABLE_NAME;
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
                        Boolean.parseBoolean(cursor.getString(6))
                );
                //Add to list
                deadlineList.add(deadline);
            }while(cursor.moveToNext());
        }

        //Return Deadline
        return deadlineList;

    }

    //takes an updated deadline object and return
    public int updateDeadline(Deadline deadline) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, deadline.getTitle());
        values.put(KEY_NOTES, deadline.getNotes());
        values.put(KEY_DUE_DATE, deadline.getDueDate());
        values.put(KEY_LOC_LAT, deadline.getLocationLat());
        values.put(KEY_LOC_LONG, deadline.getLocationLong());
        values.put(KEY_HAND_IN, deadline.getIsHandedIn());
        // updating row
        return db.update(DEADLINE_TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(deadline.getId())});
    }

    //DELETE RECORD
    public void deleteShop(Deadline deadline) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DEADLINE_TABLE_NAME, KEY_ID + " = ?",
                new String[]{String.valueOf(deadline.getId())});
        db.close();
    }
}
