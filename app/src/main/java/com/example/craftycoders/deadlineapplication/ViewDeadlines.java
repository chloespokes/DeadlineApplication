package com.example.craftycoders.deadlineapplication;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;

import com.example.craftycoders.deadlineapplication.Data.DbHelper;
import com.example.craftycoders.deadlineapplication.Models.AlarmReceiver;
import com.example.craftycoders.deadlineapplication.Models.Deadline;

import java.util.ArrayList;


public class ViewDeadlines extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Deadline>> {

    private DeadlineAdapter mAdapter;
    private String TAG = "ViewDeadlinesActivity";
    private int mDeadlineReminderIntervalMs = 86400000;
    private boolean mConfiguredReminders = false;

    Button clickAddDeadline;
    ListView listViewDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_deadlines);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setTitle("Impending Deadlines");

        //custom loader to load in db deadlines
        this.getLoaderManager().initLoader(5,null,this);

        mAdapter = new DeadlineAdapter(this, R.layout.row_deadline_item);
        listViewDeadline = (ListView) findViewById(R.id.listViewToDo);
        listViewDeadline.setAdapter(mAdapter);

        if(!mConfiguredReminders){
            ConfigureDeadlineReminders();
        }

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

    public void reloadDeadlines(View v) {
        //inform loader, dataset has been changed
        Intent intent = new Intent();
        intent.setAction(DeadlinesLoader.DeadlinesLoader_RELOAD);
        sendBroadcast(intent);
    }

    @Override
    public Loader<ArrayList<Deadline>> onCreateLoader(int id, Bundle args) {
        DeadlinesLoader loader = new DeadlinesLoader(this);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Deadline>> loader, ArrayList<Deadline> data ) {
        mAdapter.clear();
        for (Deadline item : data) {
            mAdapter.add(item);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Deadline>> arg0) {

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
