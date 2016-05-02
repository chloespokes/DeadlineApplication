package com.example.craftycoders.deadlineapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.craftycoders.deadlineapplication.Models.Deadline;

import java.util.Collection;
import java.util.List;


public class ViewDeadlines extends AppCompatActivity {

    private DeadlineAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_deadlines);

        mAdapter = new DeadlineAdapter(this, R.layout.row_deadline_item);
        ListView listViewDeadline = (ListView) findViewById(R.id.listViewToDo);
        listViewDeadline.setAdapter(mAdapter);

        PopulateImpendingDeadlines();
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
