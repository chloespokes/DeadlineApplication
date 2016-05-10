package com.example.craftycoders.deadlineapplication;

import java.util.*;
import java.text.ParseException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.content.Intent;
import android.app.DialogFragment;
import android.view.Menu;

import com.example.craftycoders.deadlineapplication.Data.DeadlinesContract;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Chloe on 27/04/16.
 */
public class AddDeadlines extends AppCompatActivity {
    private String TAG = "AddDeadlinesActivity";

    //Edit/resume variables
    boolean editDeadlines, addResume, dateInvalid, formInvalid;
    private String editTitle, editNotes;
    private Long editDateTime;
    private Float editLatitude, editLongitude;
    private Integer editDay, editMonth, editYear, editHour, editMinute;

    //Global variables
    double latitude, longitude;

    //get current date using Calendar
    Calendar calendar = Calendar.getInstance();
    int current_day = calendar.get(Calendar.DATE);
    int current_month = calendar.get(Calendar.MONTH);
    int current_year = calendar.get(Calendar.YEAR);
    int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
    int current_minute = calendar.get(Calendar.MINUTE);

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

    EditText editTextTitle, editTextHours, editTextMinutes, editTextDay, editTextMonth, editTextYear, editTextNotesText;
    AutoCompleteTextView editTextLocation;
    Toolbar myToolbar;
    Button dueDate, dueTime;
    TextView selectedDate, selectedTime;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        dateInvalid = false;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deadlines);

        editTextLocation = (AutoCompleteTextView) findViewById(R.id.location);
        selectedDate = (TextView) findViewById(R.id.selectedDate);
        selectedTime = (TextView) findViewById(R.id.selectedTime);
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

        //set date picker
        dueDate = (Button) findViewById(R.id.dueDate);
        dueDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");

            }
        });

        //set time picker
        dueTime = (Button) findViewById(R.id.dueTime);
        dueTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"TimePicker");

            }
        });


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
        current_day = ( editDeadlines || addResume ) ? editDay : current_day;
        current_month = ( editDeadlines || addResume ) ? editMonth : current_month;
        current_year = ( editDeadlines || addResume ) ? editYear : current_year;
        current_hour = ( editDeadlines || addResume ) ? editHour : current_hour;
        current_minute = ( editDeadlines || addResume ) ? editMinute : current_minute;

        String time_period;
        if (current_hour < 12) {
            time_period = "AM";
        } else {
            time_period = "PM";
        }

        selectedDate.setText( current_day + "/" + current_month + "/" + current_year );
        selectedTime.setText( current_hour + ":" + current_minute + time_period );

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
                longitude = 0;
                latitude = 0;

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
                        longitude = 0;
                        latitude = 0;
                    }
                }
            }
        });


    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public void addNewDeadline(View view) throws ParseException {
        formInvalid = false;

        editTextTitle = (EditText) findViewById(R.id.title);
        editTextNotesText = (EditText) findViewById(R.id.notes);

        if (isEmpty(editTextTitle) || isEmpty(editTextNotesText) || latitude == 0 || longitude == 0) {
            formInvalid = true;
        }


        if (formInvalid) {
            Toast.makeText(AddDeadlines.this,
                    "Please enter all fields", Toast.LENGTH_LONG).show();
        } else {
            String title = (isEmpty(editTextTitle)) ? "" : editTextTitle.getText().toString();
            String notes = (isEmpty(editTextNotesText)) ? "" : editTextNotesText.getText().toString();

            //convert date/time to seconds
            String day = editTextDay.getText().toString();
            String month = editTextMonth.getText().toString();
            String year = editTextYear.getText().toString();
            String hour = editTextHours.getText().toString();
            String minutes = editTextMinutes.getText().toString();

            Integer i = Integer.parseInt(month);
            String selectedMonthFormatted = String.format("%02d", i);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.UK);
            String dateInString = day + "-" + selectedMonthFormatted + "-" + year + " " + hour + ":" + minutes + ":00";
            Date date = sdf.parse(dateInString);
            Long due_date = date.getTime();

            ContentValues values = new ContentValues();
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

            //take activity back to view deadline
            Intent intent = new Intent(AddDeadlines.this, ViewDeadlines.class );
            startActivity(intent);
            finish();
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

    public void onBackPressed() {
        //Go back to ViewDeadlines when back clicked
        Intent intent = new Intent(AddDeadlines.this, ViewDeadlines.class );
        startActivity(intent);
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

}