package com.example.craftycoders.deadlineapplication;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.IOException;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
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
import android.view.KeyEvent;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.example.craftycoders.deadlineapplication.Data.DeadlinesContract;
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
    //Edit/resume variables
    boolean editDeadlines, addResume, dateInvalid;
    private String editTitle, editNotes;
    private Long editDateTime;
    private Float editLatitude, editLongitude;
    private Integer editDay, editMonth, editYear, editHour, editMinute;

    //Global variables
    double latitude, longitude;
    Spinner spinner_month, spinner_hour, spinner_minutes;
    String selectedSpinnerMonth, selectedSpinnerHour, selectedSpinnerMinutes;

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

    EditText editTextTitle, editTextDay, editTextYear, editTextNotesText;
    AutoCompleteTextView editTextLocation;
    Toolbar myToolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        dateInvalid = false;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deadlines);

        editTextTitle = (EditText) findViewById(R.id.title);
        editTextLocation = (AutoCompleteTextView) findViewById(R.id.location);
        editTextDay = (EditText) findViewById(R.id.day);
        editTextYear = (EditText) findViewById(R.id.year);
        editTextNotesText = (EditText) findViewById(R.id.notes);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

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

        //if edit, get deadline object by id from URI
        if( editDeadlines ) {
            editTitle = b.getString("title");
            editLatitude = b.getFloat("latitude");
            editLongitude = b.getFloat("longitude");
            editDateTime = b.getLong("date_time");
            editNotes = b.getString("notes");

            setTitle("Edit " + editTitle + " Deadline");
            //setContentView(R.layout.activity_edit_deadlines);
        } else {
            //get bundle from onpause();
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

        //set autocomplete to location field
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, predefined_locations);
        editTextLocation.setAdapter(adapter);

        //setting up map
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        LatLng editMarker = (editDeadlines || addResume) ? new LatLng(editLatitude, editLongitude) : LOUGHBOROUGH;
        String MarkerTitle = (editDeadlines || addResume) ? editTitle : "Loughborough University";

        map.addMarker(new MarkerOptions().position(editMarker).title( MarkerTitle ));

        // Move the camera instantly to Loughborough with a zoom of 10.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(editMarker, 10));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);

        //setting spinner for months
        spinner_month = (Spinner) findViewById(R.id.month);
        final ArrayAdapter<String> adapter_state_month = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, month);
        adapter_state_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(adapter_state_month);
        spinner_month.setOnItemSelectedListener(this);

        //setting spinner for time (hours)
        spinner_hour = (Spinner) findViewById(R.id.time_hours);
        ArrayAdapter<String> adapter_state_hour = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, time_hours);
        adapter_state_hour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_hour.setAdapter(adapter_state_hour);
        spinner_hour.setOnItemSelectedListener(this);

        //setting spinner for time (minutes)
        spinner_minutes = (Spinner) findViewById(R.id.time_minutes);
        ArrayAdapter<String> adapter_state_minutes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, time_minutes);
        adapter_state_minutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_minutes.setAdapter(adapter_state_minutes);
        spinner_minutes.setOnItemSelectedListener(this);

        //set current day, month, year & time
        //if editDeadline, get date in variables
        if (editDeadlines) {
            java.util.Date time = new java.util.Date(editDateTime*1000);
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
        editTextDay.setText(string_current_day, TextView.BufferType.EDITABLE);


        int parsing_month = ( editDeadlines || addResume ) ? editMonth : current_month;
        int parsing_hour = ( editDeadlines || addResume ) ? editHour : current_hour;
        int parsing_minute = ( editDeadlines || addResume ) ? editMinute : current_minute;
        spinner_month.setSelection( parsing_month );
        spinner_hour.setSelection( parsing_hour );
        spinner_minutes.setSelection(parsing_minute);

        String string_current_year = ( editDeadlines || addResume ) ? Integer.toString( editYear ) : Integer.toString( current_year );
        if ( string_current_year.length() == 0) {
            string_current_year = "";
        }
        editTextYear.setText(string_current_year, TextView.BufferType.EDITABLE);

        if ( editDeadlines || addResume ) {
            //if editing deadlines, add extra notes parsed
            editTextNotesText.setText(editNotes);
        }


        // final EditText editLocation = (EditText) findViewById(R.id.location);
        editTextLocation.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //remove all markers
                map.clear();

                //once user has finished editing location, search for lat and long
                if (!hasFocus) {
                    //validateInput(v);
                    //get entered Location
                    Geocoder geocoder = new Geocoder(AddDeadlines.this, Locale.UK);
                    List<Address> addresses = new ArrayList<>();
                    String selectedLocation = editTextLocation.getText().toString();

                    int index = -1;
                    for (int i=0;i<predefined_locations.length;i++) {
                        if (predefined_locations[i].equals(selectedLocation)) {
                            index = i;
                            break;
                        }
                    }

                    try {
                        Log.i("System.out", "Tried");
                        Log.i("System.out", selectedLocation);
                        addresses = geocoder.getFromLocationName(selectedLocation, 1);
                        Log.i("System.out", Integer.toString(addresses.size()));
                    } catch (IOException e) {
                        if("sdk".equals( Build.PRODUCT )) {
                            Log.i("System.out", "Geocoder doesn't work under emulation.");
                        } else
                            e.printStackTrace();
                    }

                    if ( addresses.size() > 0 || index > -1 ) {

                        if ( addresses.size() > 0 ) {
                            latitude = addresses.get(0).getLatitude();
                            longitude = addresses.get(0).getLongitude();
                        } else {
                            latitude = predefined_locations_coords[index][0];
                            longitude = predefined_locations_coords[index][1];
                        }

                        LatLng NEW_LAT = new LatLng(latitude, longitude);
                        map.addMarker(new MarkerOptions().position(NEW_LAT).title(selectedLocation));

                        // Move the camera instantly to new location with a zoom of 18.
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(NEW_LAT, 18));
                    } else {
                        Toast.makeText(AddDeadlines.this,
                                "Can't find this location - please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //day validation
        editTextDay.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //once user has finished editing location, search for lat and long
                if (!hasFocus) {
                    selectedDay = Integer.valueOf(editTextDay.getText().toString());
                    selectedMonth = spinner_month.getSelectedItemPosition();
                    selectedYear = Integer.valueOf(editTextYear.getText().toString());

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
                    }
                }
            }
        });

        //year validation
        editTextYear.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //once user has finished editing location, search for lat and long
                if (!hasFocus) {
                    selectedDay = Integer.valueOf(editTextDay.getText().toString());
                    selectedMonth = spinner_month.getSelectedItemPosition();
                    selectedYear = Integer.valueOf(editTextYear.getText().toString());

                    if (selectedYear < 100) {
                        selectedYear = Integer.valueOf("20" + String.valueOf(selectedYear));
                    } else if (selectedYear < 1000) {
                        selectedYear = Integer.valueOf("2" + String.valueOf(selectedYear));
                    }

                    editTextYear.setText(Integer.toString(selectedYear));

                    String formatted_day = String.format("%02d", selectedDay);
                    String formatted_month = String.format("%02d", selectedMonth);

                    Log.i("date", selectedYear + "-" + formatted_month + "-" + formatted_day);
                    Log.i("date-valid", Boolean.toString(isValidDate(selectedYear + "-" + formatted_month + "-" + formatted_day)));

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
                    }
                }
            }
        });

    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public void addNewDeadline(View view) throws ParseException {
        if (dateInvalid) {
            Toast.makeText(AddDeadlines.this,
                    "Please validate all fields in red", Toast.LENGTH_LONG).show();
        } else {
            editTextTitle = (EditText) findViewById(R.id.title);
            editTextNotesText = (EditText) findViewById(R.id.notes);

            String title = (isEmpty(editTextTitle)) ? "" : editTextTitle.getText().toString();
            String notes = (isEmpty(editTextNotesText)) ? "" : editTextNotesText.getText().toString();

            //convert date/time to seconds
            String day = editTextDay.getText().toString();
            String year = editTextYear.getText().toString();

            Integer i = Integer.parseInt(selectedSpinnerMonth);
            i++;    //increase so months start from 1-12
            String selectedMonthFormatted = String.format("%02d", i);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.UK);
            String dateInString = day + "-" + selectedMonthFormatted + "-" + year + " " + selectedSpinnerHour + ":" + selectedSpinnerMinutes + ":00";
            Date date = sdf.parse(dateInString);
            Long due_date = date.getTime();

            ContentValues values = new ContentValues();
            values.put(DeadlinesContract.KEY_ID, 2);
            values.put(DeadlinesContract.KEY_TITLE, title);
            values.put(DeadlinesContract.KEY_NOTES, notes);
            values.put(DeadlinesContract.KEY_DUE_DATE, due_date);
            values.put(DeadlinesContract.KEY_LOC_LAT, longitude);
            values.put(DeadlinesContract.KEY_LOC_LONG, latitude);
            values.put(DeadlinesContract.KEY_HAND_IN, false);

            /*ContentResolver contentResolver = getContentResolver();

            try{
                Uri uri = contentResolver.insert(DeadlinesContract.CONTENT_URI, values);
                Log.d("ContentProvider", "AddDeadline: Successfully added deadline with uri: " + uri);
            }
            catch(Exception e){
                Log.d("ContentProvider", "AddDeadline: Failed to add" + e.getMessage());
            }

            //return to parent activity
            NavUtils.navigateUpFromSameTask(this);*/
        }
    }

    protected void onPause() {
        super.onPause();

        /*final EditText editTitle = (EditText) findViewById(R.id.title);
        final EditText editNote = (EditText) findViewById(R.id.notes);

        Intent yourIntent = new Intent(AddDeadlines.this, AddDeadlines.class);
        Bundle b = new Bundle();
        b.putBoolean("edit", false);
        b.putBoolean("addResume", true);
        b.putString("title", editTitle.getText().toString() );
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
        dateInvalid = true;
        Log.i("update", "turn red");
        editTextDay = (EditText) findViewById(R.id.day);
        editTextYear = (EditText) findViewById(R.id.year);

        editTextDay.setTextColor(Color.parseColor("#ff0000"));
        TextView selectedText = (TextView) spinner_month.getChildAt(0);
        if (selectedText != null) {
            selectedText.setTextColor(Color.RED);
        }
        editTextYear.setTextColor(Color.parseColor("#ff0000"));
    }

    public void turn_black_date() {
        dateInvalid = false;
        Log.i("update", "turn black");
        editTextDay = (EditText) findViewById(R.id.day);
        editTextYear = (EditText) findViewById(R.id.year);

        editTextDay.setTextColor(Color.parseColor("#000000"));
        TextView selectedText = (TextView) spinner_month.getChildAt(0);
        if (selectedText != null) {
            selectedText.setTextColor(Color.BLACK);
        }
        editTextYear.setTextColor(Color.parseColor("#000000"));
    }

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

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
                selectedSpinnerMonth = Integer.toString(spinner_month.getSelectedItemPosition());
                break;
            case R.id.time_hours:
                spinner_hour.setSelection(position);
                selectedSpinnerHour = (String) spinner_hour.getSelectedItem();
                break;
            case R.id.time_minutes:
                spinner_minutes.setSelection(position);
                selectedSpinnerMinutes = (String) spinner_minutes.getSelectedItem();
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