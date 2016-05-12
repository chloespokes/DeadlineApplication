package com.example.craftycoders.deadlineapplication;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by simonwalker on 09/05/2016.
 */
public class Utils {
    public static final Map<String, LatLng> predefined_locations = new HashMap<String, LatLng>();
    static
    {
        predefined_locations.put("Schofield, Loughborough University", new LatLng( 52.766406, -1.228735 ));
        predefined_locations.put("Haslegrave, Loughborough University", new LatLng( 52.766769, -1.228994 ));
        predefined_locations.put("Edward Herbert, Loughborough University", new LatLng( 52.765055, -1.227206 ));
        predefined_locations.put("Business & Economics, Loughborough University", new LatLng(52.767195, -1.227838 ));
        predefined_locations.put("James France, Loughborough University", new LatLng( 52.765062, -1.227227 ));
        predefined_locations.put("Brocklington, Loughborough University", new LatLng( 52.765805, -1.227865 ));
        predefined_locations.put("Wavy Top, Loughborough University", new LatLng( 52.765351, -1.228155 ));
    }

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

    public static boolean isPredefinedLocation(LatLng deadlineLatLng, Map.Entry<String, LatLng> entry){
        if(distance(deadlineLatLng.latitude, deadlineLatLng.longitude, entry.getValue().latitude, entry.getValue().longitude) < 0.0005)
        {
            return true;
        }
        return false;
    }

    public static String getLocationFromLatLng(Context context, LatLng deadlineLatLng){
        Geocoder geocoder;
        List<Address> addresses = new ArrayList<Address>();
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(deadlineLatLng.latitude, deadlineLatLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String thoroughfare = addresses.get(0).getThoroughfare();
        String address = addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        return thoroughfare + "," + address;
    }

    // This will need to be cited found on StackOverflow
    // stackoverflow.com/questions/18170131/comparing-two-locations-using-their-longitude-and-latitude
    private static double distance(double deadlineLat, double deadlineLong, double predefinedLat, double predefinedLong) {
        double earthRadius = 3958.75;

        double dLat = Math.toRadians(predefinedLat - deadlineLat);
        double dLong = Math.toRadians(predefinedLong - deadlineLong);

        double sindLat = Math.sin(dLat / 2);
        double sindLong = Math.sin(dLong / 2);

        double a = (Math.pow(sindLat, 2) + Math.pow(sindLong, 2))
                * Math.cos(Math.toRadians(deadlineLat))
                * Math.cos(Math.toRadians(predefinedLat));

        double b = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * b;

        return dist;
    }
}
