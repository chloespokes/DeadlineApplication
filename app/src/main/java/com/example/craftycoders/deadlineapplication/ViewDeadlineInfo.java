package com.example.craftycoders.deadlineapplication;

import android.content.ContentUris;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.craftycoders.deadlineapplication.Data.DeadlineRepo;
import com.example.craftycoders.deadlineapplication.Data.DeadlinesContract;
import com.example.craftycoders.deadlineapplication.Models.Deadline;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewDeadlineInfo extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private GestureDetectorCompat mDetector;
    View.OnTouchListener gestureListener;
    private int mDeadlineId;
    float latitude, longitude;

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

    private Deadline deadline = new Deadline();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_deadline_info);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        mDeadlineId = intent.getIntExtra("deadlineId", 0);

        setTitle("View Deadline");

        Uri uri = ContentUris.withAppendedId(DeadlinesContract.CONTENT_URI, mDeadlineId);

        deadline = DeadlineRepo.GetDeadline(ViewDeadlineInfo.this.getContentResolver(), uri);
//        deadline = new Deadline(
//                0,
//                "MAD",
//                "Woop",
//                1463076000000L,
//                52.6274318F,
//                -1.6833596999999827F,
//                false,
//                false);

        final LatLng deadlineLatLng = new LatLng(deadline.getLocationLat(), deadline.getLocationLong());
        latitude = deadline.getLocationLat();
        longitude = deadline.getLocationLong();

        final TextView deadlineTitle = (TextView) findViewById(R.id.title);
        deadlineTitle.setText("Deadline: " + deadline.getTitle());

        final TextView deadlineLocation = (TextView) findViewById(R.id.location);

        //setting up map
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        boolean predefinedHandInLocation = false;

        for (Map.Entry<String, LatLng> entry : predefined_locations.entrySet())
        {
            if(distance(deadlineLatLng.latitude, deadlineLatLng.longitude, entry.getValue().latitude, entry.getValue().longitude) < 0.0005)
            {
                deadlineLocation.setText(entry.getKey());
                predefinedHandInLocation = true;
                break;
            }
        }

        if(!predefinedHandInLocation)
        {
            Geocoder geocoder;
            List<Address> addresses = new ArrayList<Address>();
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

        int dateStyle = DateFormat.LONG;
        int timeStyle = DateFormat.SHORT;
        DateFormat dateFormat;
        dateFormat = DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.UK);

        final TextView deadlineEndDate = (TextView) findViewById(R.id.dueDate);
        Date dueDate = new Date(deadline.getDueDate());
        deadlineEndDate.setText(dateFormat.format(dueDate));

        final TextView deadlineTimeRemaining = (TextView) findViewById(R.id.timeRemaining);

        deadlineTimeRemaining.setText(Utils.ConvertDueDateToTimeRemaining(deadline.getDueDate()));

        final TextView deadlineExtraNotes = (TextView) findViewById(R.id.notes);
        deadlineExtraNotes.setText(deadline.getNotes());

        mDetector = new GestureDetectorCompat(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        };

        ViewGroup scrollView = (ViewGroup) findViewById(R.id.scrollView2);
        recursiveLoopChildren(scrollView);
    }

    private void recursiveLoopChildren(ViewGroup parent) {
        for (int childIndex = 0; childIndex < parent.getChildCount(); childIndex++){
            final View child = parent.getChildAt(childIndex);
            if (child instanceof ViewGroup)
            {
                recursiveLoopChildren((ViewGroup)child);
            }
            else {
                if(child !=null)
                {
                    if(child.getId() != R.id.map){
                        child.setOnTouchListener(gestureListener);
                    }
                }
            }

        }
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > 250)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > 120 && Math.abs(velocityX) > 200) {
                    editDeadline();
                } else if (e2.getX() - e1.getX() > 120 && Math.abs(velocityX) > 200) {
                    deleteDeadline();
                }
            } catch (Exception e) {
                Log.d("GestureDetector", e.getMessage());
            }
            return false;
        }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return mDetector.onTouchEvent(ev);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("lat", Float.toString(latitude));
        Log.d("long", Float.toString(longitude));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Loughborough"));
        LatLng NEW_LAT = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(NEW_LAT, 13));
        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
    }

    public void editDeadline() {
        Intent resultIntent = new Intent(ViewDeadlineInfo.this.getApplicationContext(), AddDeadlines.class);
        resultIntent.putExtra("deadlineId", mDeadlineId);
        startActivity(resultIntent);
    }

    // http://www.wolinlabs.com/blog/yes.no.message.box.html
    public void deleteDeadline() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete this Deadline");
        builder.setMessage("Are you sure?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = ContentUris.withAppendedId(DeadlinesContract.CONTENT_URI, mDeadlineId);
                DeadlineRepo.DeleteDeadline(ViewDeadlineInfo.this.getContentResolver(), uri);

                Intent resultIntent = new Intent(ViewDeadlineInfo.this.getApplicationContext(), ViewDeadlines.class);
                startActivity(resultIntent);
            }
        });

        builder.setNegativeButton("No", null);
        builder.show();
    }
    public void onBackPressed() {
        //Go back to ViewDeadlines when back clicked
        Intent intent = new Intent(ViewDeadlineInfo.this, ViewDeadlines.class );
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                return true;
            case R.id.action_edit:
                editDeadline();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // This will need to be cited found on StackOverflow
    // stackoverflow.com/questions/18170131/comparing-two-locations-using-their-longitude-and-latitude
    private double distance(double deadlineLat, double deadlineLong, double predefinedLat, double predefinedLong) {
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
