package com.example.craftycoders.deadlineapplication;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.craftycoders.deadlineapplication.Models.Deadline;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewDeadlineInfo extends AppCompatActivity {

    private GoogleMap map;

    private static final Map<String, LatLng> predefined_locations = new HashMap<String, LatLng>();
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

    private static final Deadline deadline = new Deadline(
            0,
            "Mobile App",
            "I hate it",
            1462568485978l,
            52.6274318f,
            -1.68335969f,
            false,
            false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_deadline_info);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //  Intent intent = getIntent();
        //Uri data = intent.getData();

        setTitle("View Deadline");

        final LatLng deadlineLatLng = new LatLng(deadline.getLocationLat(), deadline.getLocationLong());

        final TextView deadlineTitle = (TextView) findViewById(R.id.title);
        deadlineTitle.setText("Deadline: " + deadline.getTitle());

        final TextView deadlineLocation = (TextView) findViewById(R.id.location);

        boolean predefinedHandInLocation = false;

        for (Map.Entry<String, LatLng> entry : predefined_locations.entrySet())
        {
            if(distance(deadlineLatLng.latitude, deadlineLatLng.longitude, entry.getValue().latitude, entry.getValue().longitude) < 0.0005)
            {
                deadlineLocation.setText("Location: " + entry.getKey());
                predefinedHandInLocation = true;
                break;
            }
        }

        if(!predefinedHandInLocation)
        {
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(deadlineLatLng.latitude, deadlineLatLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            String thoroughfare = addresses.get(0).getThoroughfare();
            String address = addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            deadlineLocation.setText(thoroughfare + ", " + address);
        }

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        final Marker marker_deadlineLocation = map.addMarker(new MarkerOptions().position(deadlineLatLng));

        // Move the camera instantly to deadlineLocation with a zoom of 13.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(deadlineLatLng, 13));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);

        int dateStyle = DateFormat.LONG;
        int timeStyle = DateFormat.SHORT;
        DateFormat dateFormat;
        dateFormat = DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.UK);

        final TextView deadlineEndDate = (TextView) findViewById(R.id.dueDate);
        Date dueDate = new Date(deadline.getDueDate());
        deadlineEndDate.setText("Due Date: " + dateFormat.format(dueDate));

        final TextView deadlineExtraNotes = (TextView) findViewById(R.id.notes);
        deadlineExtraNotes.setText(deadline.getNotes());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                return true;
            case R.id.action_edit:
                Intent yourIntent = new Intent(ViewDeadlineInfo.this, AddDeadlines.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("edit", true);
                bundle.putString("title", deadline.getTitle());
                bundle.putFloat("latitude", deadline.getLocationLat());
                bundle.putFloat("longitude", deadline.getLocationLong());
                bundle.putLong("date_time", deadline.getDueDate());
                bundle.putString("notes", deadline.getNotes());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // This will need to be cited found on StackOverflow
    // stackoverflow.com/questions/18170131/comparing-two-locations-using-their-longitude-and-latitude
    private double distance(double deadlineLat, double deadlineLong, double predefinedLat, double predefinedLong)
    {
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
