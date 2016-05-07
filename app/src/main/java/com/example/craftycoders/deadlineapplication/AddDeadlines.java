package com.example.craftycoders.deadlineapplication;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.IOException;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.graphics.Color;
import android.widget.Button;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.content.Intent;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Chloe on 27/04/16.
 */
public class AddDeadlines extends AppCompatActivity implements OnItemSelectedListener {
    Spinner spinner_month, spinner_hour, spinner_minutes;
    private boolean editDeadlines, addResume;
    private String editTitle, editNotes;
    private Long editDateTime;
    private Float editLatitude, editLongitude;
    private Integer editDay, editMonth, editYear, editHour, editMinute;

    //get current date using Calendar
    Calendar calendar = Calendar.getInstance();
    int current_day = calendar.get(Calendar.DATE);
    int current_month = calendar.get(Calendar.MONTH);
    int current_year = calendar.get(Calendar.YEAR);
    int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
    int current_minute = calendar.get(Calendar.MINUTE);

    int selectedDay, selectedMonth, selectedYear;

    private String[] month = { "January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October", "November",
            "December" };

    private String[] time_hours = { "00", "01", "02", "03",
            "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17",
            "18", "19", "20", "21", "22", "23" };

    private String[] time_minutes = { "00", "01", "02", "03",
            "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17",
            "18", "19", "20", "21", "22", "23", "24",
            "25", "26", "27", "28", "29", "30", "31",
            "32", "33", "34", "35", "36", "37", "38",
            "39", "40", "41", "42", "43", "44", "45",
            "46", "47", "48", "49", "50", "51", "52",
            "53", "54", "55", "56", "57", "58", "59" };

    private static final String[] predefined_locations = new String[] {
            "Schofield, Loughborough University",
            "Haslegrave, Loughborough University",
            "Edward Herbert, Loughborough University",
            "Business & Economics, Loughborough University",
            "James France, Loughborough University",
            "Brocklington, Loughborough University",
            "Wavy Top, Loughborough University"
    };

    float[][] predefined_locations_coords = new float[][] {
            { 52.766406f, -1.228735f }, //schofieldd
            { 52.766769f, -1.228994f }, //haslegrave
            { 52.765055f, -1.227206f }, //ed hertbert
            { 52.767195f, -1.227838f }, //business
            { 52.765062f, -1.227227f }, //james france
            { 52.765805f, -1.227865f }, //brocklington
            { 52.765351f, -1.228155f } //wavy top
    };

    static final LatLng LOUGHBOROUGH = new LatLng(52.762913, -1.237816);
    private GoogleMap map;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deadlines);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setTitle("Add New Deadline");

        Bundle b = new Bundle();
        try {
            b = getIntent().getExtras();
            editDeadlines = b.getBoolean("edit");
            addResume = b.getBoolean("addResume");
        } catch (NullPointerException e) {
            editDeadlines = false;
            addResume = false;
        }

        if( editDeadlines ) {
            editTitle = b.getString("title");
            editLatitude = b.getFloat("latitude");
            editLongitude = b.getFloat("longitude");
            editDateTime = b.getLong("date_time");
            editNotes = b.getString("notes");

            setTitle("Edit " + editTitle + " Deadline");
            //setContentView(R.layout.activity_edit_deadlines);
        } else {
            if ( addResume ) {
                editTitle = b.getString("title");
                editLatitude = b.getFloat("latitude");
                editLongitude = b.getFloat("longitude");
                editDateTime = b.getLong("date_time");
                editNotes = b.getString("notes");
            }
            setTitle("Add New Deadline");
            //setContentView(R.layout.activity_add_deadlines);
        }

        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.location);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, predefined_locations);
        textView.setAdapter(adapter);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        LatLng editMarker = (editDeadlines || addResume) ? new LatLng(editLatitude, editLongitude) : LOUGHBOROUGH;
        String MarkerTitle = (editDeadlines || addResume) ? editTitle : "Loughborough University";

        final Marker marker_lboro = map.addMarker(new MarkerOptions().position(editMarker)
                .title( MarkerTitle ));

        // Move the camera instantly to Loughborough with a zoom of 10.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(editMarker, 10));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);

        //setting spinner for months
        spinner_month = (Spinner) findViewById(R.id.month);
        final ArrayAdapter<String> adapter_state_month = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, month);
        adapter_state_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(adapter_state_month);
        spinner_month.setOnItemSelectedListener(this);

        //setting spinner for time (hours)
        spinner_hour = (Spinner) findViewById(R.id.time_hours);
        ArrayAdapter<String> adapter_state_hour = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, time_hours);
        adapter_state_hour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_hour.setAdapter(adapter_state_hour);
        spinner_hour.setOnItemSelectedListener(this);

        //setting spinner for time (minutes)
        spinner_minutes = (Spinner) findViewById(R.id.time_minutes);
        ArrayAdapter<String> adapter_state_minutes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, time_minutes);
        adapter_state_minutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_minutes.setAdapter(adapter_state_minutes);
        spinner_minutes.setOnItemSelectedListener(this);

        //set current day, month, year & time
        //if editDeadline, get date in variables
        if (editDeadlines) {
            java.util.Date time = new java.util.Date((long)editDateTime*1000);
            // create a calendar
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);  //use java.util.Date object as arguement
            // get the value of all the calendar date fields.
            editDay = cal.get(Calendar.DATE);
            editMonth = cal.get(Calendar.MONTH);
            editYear = cal.get(Calendar.YEAR);
            editHour = cal.get(Calendar.HOUR_OF_DAY);
            editMinute = cal.get(Calendar.MINUTE);
        }
        String string_current_day = ( editDeadlines || addResume ) ? Integer.toString( editDay ) : Integer.toString( current_day );
        final EditText editDay = (EditText) findViewById(R.id.day);
        editDay.setText(string_current_day, TextView.BufferType.EDITABLE);

        int parsing_month = ( editDeadlines || addResume ) ? editMonth : current_month;
        int parsing_hour = ( editDeadlines || addResume ) ? editHour : current_hour;
        int parsing_minute = ( editDeadlines || addResume ) ? editMinute : current_minute;
        spinner_month.setSelection( parsing_month );
        spinner_hour.setSelection( parsing_hour );
        spinner_minutes.setSelection( parsing_minute );

        String string_current_year = ( editDeadlines || addResume ) ? Integer.toString( editYear ) : Integer.toString( current_year );
        final EditText editYear = (EditText) findViewById(R.id.year);
        editYear.setText(string_current_year, TextView.BufferType.EDITABLE);

        if ( editDeadlines || addResume ) {
            //if editing deadlines, add extra notes parsed
            final EditText editNotesText = (EditText) findViewById(R.id.notes);
            editNotesText.setText(editNotes);
        }

        final EditText editLocation = (EditText) findViewById(R.id.location);
        editLocation.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //remove all markers
                map.clear();

                Geocoder geocoder = new Geocoder(AddDeadlines.this);
                List<Address> addresses = new ArrayList<Address>();
                String selectedLocation = editLocation.getText().toString();

                int index = -1;
                for (int i=0;i<predefined_locations.length;i++) {
                    if (predefined_locations[i].equals(selectedLocation)) {
                        index = i;
                        break;
                    }
                }

                try {
                    addresses = geocoder.getFromLocationName(selectedLocation, 1);
                } catch (IOException e) {
                    if("sdk".equals( Build.PRODUCT )) {
                        Log.i("System.out", "Geocoder doesn't work under emulation.");
                    } else
                        e.printStackTrace();
                }

                if ( addresses.size() > 0 || index > -1 ) {
                    double latitude, longitude;

                    if ( addresses.size() > 0 ) {
                        latitude = addresses.get(0).getLatitude();
                        longitude = addresses.get(0).getLongitude();
                    } else {
                        latitude = predefined_locations_coords[index][0];
                        longitude = predefined_locations_coords[index][1];
                    }

                    LatLng NEW_LAT = new LatLng(latitude, longitude);
                    final Marker marker_lboro = map.addMarker(new MarkerOptions().position(NEW_LAT)
                            .title( selectedLocation ));
                    // Move the camera instantly to new location with a zoom of 18.
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(NEW_LAT, 18));
                } else {
                    Toast.makeText(AddDeadlines.this,
                            "Can't find this location - please try again!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //day validation
        editDay.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                selectedDay = Integer.valueOf(editDay.getText().toString());
                selectedMonth = spinner_month.getSelectedItemPosition();
                selectedYear = Integer.valueOf(editYear.getText().toString());

                String formatted_day = String.format("%02d", selectedDay);
                String formatted_month = String.format("%02d", selectedMonth);

                if (!isValidDate(selectedYear + "-" + formatted_month + "-" + formatted_day)) {
                    Toast.makeText(AddDeadlines.this,
                            "Please enter a valid date", Toast.LENGTH_SHORT).show();
                    turn_red_date();
                } else {
                    turn_black_date();
                }

                if (selectedYear < current_year) {
                    Toast.makeText(AddDeadlines.this,
                            "Please enter a future date", Toast.LENGTH_SHORT).show();
                    turn_red_date();
                } else if (selectedMonth < current_month && selectedYear <= current_year) {
                    Toast.makeText(AddDeadlines.this,
                            "Please enter a future date", Toast.LENGTH_SHORT).show();
                    turn_red_date();
                } else if (selectedDay < current_day && selectedMonth <= current_month && selectedYear <= current_year) {
                    Toast.makeText(AddDeadlines.this,
                            "Please enter a future date", Toast.LENGTH_SHORT).show();
                    turn_red_date();
                } else {
                    turn_black_date();
                }
            }
        });

        //year validation
        editYear.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                selectedDay = Integer.valueOf(editDay.getText().toString());
                selectedMonth = spinner_month.getSelectedItemPosition();
                selectedYear = Integer.valueOf(editYear.getText().toString());

                if (selectedYear < 100) {
                    selectedYear = Integer.valueOf("20" + String.valueOf(selectedYear));
                } else if (selectedYear < 1000) {
                    selectedYear = Integer.valueOf("2" + String.valueOf(selectedYear));
                }
                editYear.setText(Integer.toString(selectedYear));

                String formatted_day = String.format("%02d", selectedDay);
                String formatted_month = String.format("%02d", selectedMonth);

                if (!isValidDate(selectedYear + "-" + formatted_month + "-" + formatted_day)) {
                    Toast.makeText(AddDeadlines.this,
                            "Please enter a valid date", Toast.LENGTH_SHORT).show();
                    turn_red_date();
                } else {
                    turn_black_date();
                }

                if (selectedYear < current_year) {
                    Toast.makeText(AddDeadlines.this,
                            "Please enter a future date", Toast.LENGTH_SHORT).show();
                    turn_red_date();
                } else if (selectedMonth < current_month && selectedYear <= current_year) {
                    Toast.makeText(AddDeadlines.this,
                            "Please enter a future date", Toast.LENGTH_SHORT).show();
                    turn_red_date();
                } else if (selectedDay < current_day && selectedMonth <= current_month && selectedYear <= current_year) {
                    Toast.makeText(AddDeadlines.this,
                            "Please enter a future date", Toast.LENGTH_SHORT).show();
                    turn_red_date();
                } else {
                    turn_black_date();
                }
            }
        });

        //initialise map
    }

    protected void onPause() {
        super.onPause();

        /*final EditText editDay = (EditText) findViewById(R.id.title);
        final EditText editNote = (EditText) findViewById(R.id.notes);

        Intent yourIntent = new Intent(AddDeadlines.this, AddDeadlines.class);
        Bundle b = new Bundle();
        b.putBoolean("edit", false);
        b.putBoolean("addResume", true);
        b.putString("title", editDay.getText().toString() );
        b.putFloat("latitude", );
        b.putFloat("longitude", );
        b.putInt("date_time", );
        b.putString("notes", editNote.getText().toString() );

        yourIntent.putExtras(b);
        startActivity(yourIntent);

       // Log.d(TAG, receiver + " " + message);*/
    }


    protected void onResume() {
        super.onResume();

      //  String receiver = getIntent().getStringExtra(MESSAGE_RECEIVER);
      //  String message = getIntent().getStringExtra(MESSAGE_BODY);
      //  if(receiver != null)
      //      phoneNoField.setText(receiver);
      //  if(message != null)
      //      messageBody.setText(message);

       // Log.d(TAG, receiver + " " + message);
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

    public void turn_red_date() {
        final EditText editDay = (EditText) findViewById(R.id.day);
        final EditText editYear = (EditText) findViewById(R.id.year);

        editDay.setTextColor(Color.parseColor("#ff0000"));
        TextView selectedText = (TextView) spinner_month.getChildAt(0);
        if (selectedText != null) {
            selectedText.setTextColor(Color.RED);
        }
        editYear.setTextColor(Color.parseColor("#ff0000"));
    }

    public void turn_black_date() {
        final EditText editDay = (EditText) findViewById(R.id.day);
        final EditText editYear = (EditText) findViewById(R.id.year);

        editDay.setTextColor(Color.parseColor("#000000"));
        TextView selectedText = (TextView) spinner_month.getChildAt(0);
        if (selectedText != null) {
            selectedText.setTextColor(Color.BLACK);
        }
        editYear.setTextColor(Color.parseColor("#000000"));
    }

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {

        switch(parent.getId()){
            case R.id.month:
                spinner_month.setSelection(position);
                break;
            case R.id.time_hours:
                spinner_hour.setSelection(position);
                break;
            case R.id.time_minutes:
                spinner_minutes.setSelection(position);
                break;
        }
        //spinner.setSelection(position);
        //String selectedMonth = (String) spinnerMonths.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
