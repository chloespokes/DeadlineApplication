package com.example.craftycoders.deadlineapplication;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by simonwalker on 09/05/2016.
 */
public class Utils {
    public static String ConvertDueDateToTimeRemaining(long dueDateMs){

        Date currentDateTime = new Date(System.currentTimeMillis());
        Date dueDate = new Date(dueDateMs);

        Map<TimeUnit,Long> timeRemaining = computeDiff(currentDateTime, dueDate);

        return timeRemaining.get(TimeUnit.DAYS) + " days "
                + timeRemaining.get(TimeUnit.HOURS) + " hours, "
                + timeRemaining.get(TimeUnit.MINUTES) + " minutes, "
                + timeRemaining.get(TimeUnit.SECONDS) +" seconds remaining ";
    }

    public static Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);
        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
        long milliesRest = diffInMillies;
        for ( TimeUnit unit : units ) {
            long diff = unit.convert(milliesRest, TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit,diff);
        }
        return result;
    }
}
