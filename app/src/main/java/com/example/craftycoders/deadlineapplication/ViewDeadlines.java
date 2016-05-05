package com.example.craftycoders.deadlineapplication;

import android.content.Intent;
import android.os.Bundle;

<<<<<<< HEAD
import android.util.Log;
=======
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
>>>>>>> master
import android.widget.ListView;

import com.example.craftycoders.deadlineapplication.Data.DbHelper;
import com.example.craftycoders.deadlineapplication.Models.Deadline;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.AppCompatActivity;


public class ViewDeadlines extends AppCompatActivity {

    private DeadlineAdapter mAdapter;
    private DbHelper mDbHelper = DbHelper.getInstance(this);

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

         // TODO: 30/04/2016 call content provider to return list of impending deadlines
        final List<Deadline> finalDeadlines;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.clear();
                // TODO: 02/05/2016 uncomment when successfully retrieving data from db 
//                for (Deadline item : finalDeadlines) {
//                    mAdapter.add(item);
//                }
            }
        });
    }
}
