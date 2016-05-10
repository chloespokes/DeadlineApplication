package com.example.craftycoders.deadlineapplication;

/**
 * Created by simonwalker on 30/04/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.craftycoders.deadlineapplication.Models.Deadline;

/**
 * Adapter to bind a NeglectData List to a view
 */
public class DeadlineAdapter extends ArrayAdapter<Deadline> {

    /** Adapter context */
    Context mContext;

    /** Adapter View layout */
    int mLayoutResourceId;

    public DeadlineAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Returns the view for a specific item on the list
     */
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View row = convertView;

        final Deadline currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentItem);
        final TextView title = (TextView) row.findViewById(R.id.deadlineTitle);
        final TextView timeRemaining = (TextView) row.findViewById(R.id.deadlineTimeRemaining);
        title.setText(currentItem.getTitle());
        timeRemaining.setText(CreateTimeRemainingString(currentItem.getDueDate()));

        row.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent resultIntent = new Intent(mContext, ViewDeadlineInfo.class);
                resultIntent.putExtra("deadlineId", currentItem.getId());
                mContext.startActivity(resultIntent);
            }
        });

        return row;
    }

    private String CreateTimeRemainingString(long deadlineDueDateMs){

        // TODO: 30/04/2016 Convert time remaining to string for displaying maybe extract into utils class

        String timeRemainingAsString = Utils.ConvertDueDateToTimeRemaining(deadlineDueDateMs);

        return timeRemainingAsString;
    }

}