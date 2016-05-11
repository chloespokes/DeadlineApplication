package com.example.craftycoders.deadlineapplication;


import com.example.craftycoders.deadlineapplication.AddDeadlines;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.craftycoders.deadlineapplication.Data.DbHelper;
import com.example.craftycoders.deadlineapplication.Data.DeadlineProvider;
import com.example.craftycoders.deadlineapplication.Data.DeadlinesContract;
import com.example.craftycoders.deadlineapplication.Models.AlarmReceiver;
import com.example.craftycoders.deadlineapplication.Models.Deadline;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class ViewDeadlines extends AppCompatActivity  {

    private DeadlineAdapter mAdapter;
    private DbHelper mDbHelper = DbHelper.getInstance(this);
    private String TAG = "ViewDeadlinesActivity";
    private int mDeadlineReminderIntervalMs = 86400000;
    private boolean mConfiguredReminders = false;

    Button clickSync, clickAddDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_deadlines);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setTitle("View Deadlines");

        mAdapter = new DeadlineAdapter(this, R.layout.row_deadline_item);
        ListView listViewDeadline = (ListView) findViewById(R.id.listViewToDo);
        listViewDeadline.setAdapter(mAdapter);

        PopulateImpendingDeadlines();

        if(!mConfiguredReminders){
            ConfigureDeadlineReminders();
        }


        clickSync = (Button) findViewById(R.id.syncButton);
        clickSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //refresh list
                PopulateImpendingDeadlines();
            }
        });

        clickAddDeadline = (Button) findViewById(R.id.addDeadlineButton);
        clickAddDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start activity to add new deadline
                Intent intent = new Intent(ViewDeadlines.this, AddDeadlines.class );
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.standard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void PopulateImpendingDeadlines() {
        final List<Deadline> finalDeadlines = GetAllDeadlines();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.clear();
                for (Deadline item : finalDeadlines) {
                    mAdapter.add(item);
                }
            }
        });
    }

    private void AddDeadline(){
        ContentValues values = new ContentValues();
        values.put(DeadlinesContract.KEY_TITLE, "MAD");
        values.put(DeadlinesContract.KEY_NOTES, "these are notes");
        values.put(DeadlinesContract.KEY_DUE_DATE, 1475660700000l);
        values.put(DeadlinesContract.KEY_LOC_LAT, 2532);
        values.put(DeadlinesContract.KEY_LOC_LONG, 65326);
        values.put(DeadlinesContract.KEY_HAND_IN, false);

        try{
            Uri uri = getContentResolver().insert(DeadlinesContract.CONTENT_URI, values);
            Toast.makeText(getBaseContext(),
                    uri.toString(), Toast.LENGTH_LONG).show();
        }
        catch(Exception e){
            Log.d("ContentProvider", "AddDeadline: Failed to add");
        }
    }

    private List<Deadline> GetAllDeadlines(){
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
                        DeadlinesContract.KEY_HAND_IN
                };

        // Defines a string to contain the selection clause
        String mSelectionClause = null;

        // Initializes an array to contain selection arguments
        String[] mSelectionArgs = {""};

        Cursor mCursor = getContentResolver().query(
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

    private void ConfigureDeadlineReminders(){
        AlarmManager alarmManager=(AlarmManager) this.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),mDeadlineReminderIntervalMs,
                pendingIntent);

        mConfiguredReminders = true;
    }


}
