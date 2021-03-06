package com.example.craftycoders.deadlineapplication.Data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.craftycoders.deadlineapplication.Models.Deadline;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by simonwalker on 06/05/2016.
 */
public class DeadlineRepo {

    private static final String TAG = "DeadlineRepository";

    public static List<Deadline> GetAllDeadlines(ContentResolver contentResolver){
        List<Deadline> allDeadlines= new ArrayList<>();

        // A "projection" defines the columns that will be returned for each row
        String[] mProjection =
                {
                        DeadlinesContract.KEY_ID,
                        DeadlinesContract.KEY_TITLE,
                        DeadlinesContract.KEY_NOTES,
                        DeadlinesContract.KEY_DUE_DATE,
                        DeadlinesContract.KEY_LOC_LAT,
                        DeadlinesContract.KEY_LOC_LONG,
                        DeadlinesContract.KEY_HAND_IN,
                        DeadlinesContract.KEY_CALENDAR_SYNC
                };

        // Defines a string to contain the selection clause
        String mSelectionClause = null;

        // Initializes an array to contain selection arguments
        String[] mSelectionArgs = {""};

        Cursor mCursor = contentResolver.query(
                DeadlinesContract.CONTENT_URI,
                mProjection,
                mSelectionClause,
                mSelectionArgs,
                null);

        // Some providers return null if an error occurs, others throw an exception
        if (null == mCursor) {
            Log.d(TAG, "GetAllDeadlines: provider returned an error");
            // If the Cursor is empty, the provider found no matches
        } else if (mCursor.getCount() < 1) {
            Log.d(TAG, "GetAllDeadlines: provider returned no results");
        } else {
            while (mCursor.moveToNext()) {

                Deadline deadline = new Deadline();
                deadline.setId(mCursor.getInt(mCursor.getColumnIndex(DeadlinesContract.KEY_ID)));
                deadline.setTitle(mCursor.getString(mCursor.getColumnIndex(DeadlinesContract.KEY_TITLE)));
                deadline.setNotes(mCursor.getString(mCursor.getColumnIndex(DeadlinesContract.KEY_NOTES)));
                deadline.setDueDate(mCursor.getLong(mCursor.getColumnIndex(DeadlinesContract.KEY_DUE_DATE)));
                deadline.setLocationLat(mCursor.getFloat(mCursor.getColumnIndex(DeadlinesContract.KEY_LOC_LAT)));
                deadline.setLocationLong(mCursor.getFloat(mCursor.getColumnIndex(DeadlinesContract.KEY_LOC_LONG)));
                deadline.setIsHandedIn(mCursor.getInt(mCursor.getColumnIndex(DeadlinesContract.KEY_HAND_IN)) > 0);

                allDeadlines.add(deadline);
            }
        }

        return allDeadlines;

    }

    public static Deadline GetDeadline(ContentResolver contentResolver, Uri uri){
        // A "projection" defines the columns that will be returned for each row
        String[] mProjection =
                {
                        DeadlinesContract.KEY_ID,
                        DeadlinesContract.KEY_TITLE,
                        DeadlinesContract.KEY_NOTES,
                        DeadlinesContract.KEY_DUE_DATE,
                        DeadlinesContract.KEY_LOC_LAT,
                        DeadlinesContract.KEY_LOC_LONG,
                        DeadlinesContract.KEY_HAND_IN
                };
        Deadline deadline = new Deadline();
        // Defines a string to contain the selection clause
        String mSelectionClause = null;

        // Initializes an array to contain selection arguments
        String[] mSelectionArgs = {""};

        Cursor mCursor = contentResolver.query(
                uri,
                mProjection,
                mSelectionClause,
                mSelectionArgs,
                null);

        // Some providers return null if an error occurs, others throw an exception
        if (null == mCursor) {
            Log.d(TAG, "GetDeadline: provider returned an error");
            // If the Cursor is empty, the provider found no matches
        } else if (mCursor.getCount() < 1) {
            Log.d(TAG, "GetDeadline: provider returned no results");
        } else {
                deadline.setId(mCursor.getInt(mCursor.getColumnIndex(DeadlinesContract.KEY_ID)));
                deadline.setTitle(mCursor.getString(mCursor.getColumnIndex(DeadlinesContract.KEY_TITLE)));
                deadline.setNotes(mCursor.getString(mCursor.getColumnIndex(DeadlinesContract.KEY_NOTES)));
                deadline.setDueDate(mCursor.getLong(mCursor.getColumnIndex(DeadlinesContract.KEY_DUE_DATE)));
                deadline.setLocationLat(mCursor.getFloat(mCursor.getColumnIndex(DeadlinesContract.KEY_LOC_LAT)));
                deadline.setLocationLong(mCursor.getFloat(mCursor.getColumnIndex(DeadlinesContract.KEY_LOC_LONG)));
                deadline.setIsHandedIn(mCursor.getInt(mCursor.getColumnIndex(DeadlinesContract.KEY_HAND_IN)) > 0);
        }
        return deadline;
    }

    public static void DeleteDeadline(ContentResolver contentResolver, Uri uri){
        // Defines a string to contain the selection clause
        String mSelectionClause = DeadlinesContract.KEY_ID + "=?";

        // Initializes an array to contain selection arguments
        String[] mSelectionArgs = {uri.getPathSegments().get(1)};

        try{
            int numberOfDeadlinesDeleted = contentResolver.delete(uri, mSelectionClause, mSelectionArgs);
            Log.d("ContentProvider", "DeleteDeadline: Number of deadlines deleted " + numberOfDeadlinesDeleted);
        }
        catch(Exception e){
            Log.d("ContentProvider", "DeleteDeadline: Failed to delete" + e.getMessage());
        }
    }

    public static void UpdateDeadline(ContentResolver contentResolver, Uri uri, ContentValues values) {
        // Defines a string to contain the selection clause
        String mSelectionClause = DeadlinesContract.KEY_ID + "=?";

        // Initializes an array to contain selection arguments
        String[] mSelectionArgs = {uri.getPathSegments().get(1)};

        try{
            int numberOfDeadlinesUpdated = contentResolver.update(uri, values, mSelectionClause, mSelectionArgs);
            Log.d("ContentProvider", "UpdateDeadline: Number of deadlines updated " + numberOfDeadlinesUpdated);
        }
        catch(Exception e){
            Log.d("ContentProvider", "UpdateDeadline: Failed to update" + e.getMessage());
        }
    }

    public static void AddDeadline(ContentResolver contentResolver){
        ContentValues values = new ContentValues();
        values.put(DeadlinesContract.KEY_ID, 10);
        values.put(DeadlinesContract.KEY_TITLE, "MAD");
        values.put(DeadlinesContract.KEY_NOTES, "these are notes");
        values.put(DeadlinesContract.KEY_DUE_DATE, 3654378);
        values.put(DeadlinesContract.KEY_LOC_LAT, 2532);
        values.put(DeadlinesContract.KEY_LOC_LONG, 65326);
        values.put(DeadlinesContract.KEY_HAND_IN, false);
        values.put(DeadlinesContract.KEY_CALENDAR_SYNC, false);

        try{
            Uri uri = contentResolver.insert(DeadlinesContract.CONTENT_URI, values);
            Log.d("ContentProvider", "AddDeadline: Successfully added deadline with uri: " + uri);
        }
        catch(Exception e){
            Log.d("ContentProvider", "AddDeadline: Failed to add" + e.getMessage());
        }
    }

}
