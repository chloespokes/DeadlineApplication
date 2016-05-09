package com.example.craftycoders.deadlineapplication.Models;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.craftycoders.deadlineapplication.Data.DeadlineRepo;
import com.example.craftycoders.deadlineapplication.R;
import com.example.craftycoders.deadlineapplication.Utils;
import com.example.craftycoders.deadlineapplication.ViewDeadlineInfo;

/**
 * Created by simonwalker on 09/05/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    final static long mNotificationIntervalMs = 86400000;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context,
                "Checking whether to create notification ", Toast.LENGTH_LONG).show();

        for (Deadline deadline : DeadlineRepo.GetAllDeadlines(context.getContentResolver()))
        {
            if(!deadline.getIsHandedIn())
            {
                if((System.currentTimeMillis() + mNotificationIntervalMs ) < (deadline.getDueDate() -  mNotificationIntervalMs) ){
                    CreateDeadlineReminder(context, deadline);
                }
            }
        }
    }

    private void CreateDeadlineReminder(Context context, Deadline deadline) {
        NotificationCompat.Builder mBuilder;
        Intent resultIntent = new Intent(context, ViewDeadlineInfo.class);
        resultIntent.putExtra("deadlineId", deadline.getId());

        PendingIntent resultPendingIntent =
            PendingIntent.getActivity(
                    context,
                    0,
                    resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );


        mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.cast_ic_notification_0)
                        .setContentTitle(deadline.getTitle())
                        .setContentText("Deadline has" + Utils.ConvertDueDateToTimeRemaining(deadline.getDueDate()))
                        .setContentIntent(resultPendingIntent);


        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
