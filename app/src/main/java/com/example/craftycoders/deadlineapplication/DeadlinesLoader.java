package com.example.craftycoders.deadlineapplication;

import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.util.Log;

import com.example.craftycoders.deadlineapplication.Data.DeadlinesContract;
import com.example.craftycoders.deadlineapplication.Models.Deadline;

import java.util.ArrayList;

/**
 * Created by Chloe on 12/05/16.
 */
public class DeadlinesLoader extends AsyncTaskLoader<ArrayList<Deadline>> {

    Receiver receiver;

    public static final String DeadlinesLoader_RELOAD = "DeadlinesLoader.RELOAD";
    private String TAG = "DeadlinesLoader";

    public DeadlinesLoader (Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        receiver = new Receiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(DeadlinesLoader_RELOAD);
        getContext().registerReceiver(receiver, filter);

        forceLoad();
        super.onStartLoading();
    }

    @Override
    public ArrayList<Deadline> loadInBackground() {

        ArrayList<Deadline> array = new ArrayList<Deadline>();

        //access db, and retrieve all deadlines
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

        // Defines a string to contain the selection clause
        String mSelectionClause = DeadlinesContract.KEY_HAND_IN + " = ";

        // Initializes an array to contain selection arguments
        String[] mSelectionArgs = {"0"};

        Cursor mCursor = getContext().getContentResolver().query(
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

                array.add(deadline);
            }
        }
        return array;
    }

    @Override
    public void deliverResult (ArrayList<Deadline> data) {
        if (isStarted()) {
            //return results start away
            super.deliverResult(data);
        }
    }

    @Override
    protected void onReset() {
        getContext().unregisterReceiver(receiver);
        this.stopLoading();
        super.onReset();
    }

    public class Receiver extends BroadcastReceiver {

        DeadlinesLoader loader;

        public Receiver(DeadlinesLoader loader) {
            this.loader = loader;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            loader.onContentChanged();
        }
    }

}
