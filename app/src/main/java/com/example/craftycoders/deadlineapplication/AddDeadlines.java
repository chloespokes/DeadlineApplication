package com.example.craftycoders.deadlineapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import java.util.*;
import android.graphics.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Chloe on 27/04/16.
 */
public class AddDeadlines extends AppCompatActivity implements OnItemSelectedListener {
    Spinner spinner_month, spinner_hour, spinner_minutes;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deadlines);

        setTitle("Add New Deadline");

        //Log.i("System.out", "Hello!");

        //setting spinner for months
        spinner_month = (Spinner) findViewById(R.id.month);
        final ArrayAdapter<String> adapter_state_month = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, month);
        adapter_state_month
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(adapter_state_month);
        spinner_month.setOnItemSelectedListener(this);

        //setting spinner for time (hours)
        spinner_hour = (Spinner) findViewById(R.id.time_hours);
        ArrayAdapter<String> adapter_state_hour = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, time_hours);
        adapter_state_hour
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_hour.setAdapter(adapter_state_hour);
        spinner_hour.setOnItemSelectedListener(this);

        //setting spinner for time (minutes)
        spinner_minutes = (Spinner) findViewById(R.id.time_minutes);
        ArrayAdapter<String> adapter_state_minutes = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, time_minutes);
        adapter_state_minutes
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_minutes.setAdapter(adapter_state_minutes);
        spinner_minutes.setOnItemSelectedListener(this);

        //set current day, month, year & time
        String string_current_day = Integer.toString( current_day );
        final EditText editDay = (EditText) findViewById(R.id.day);
        editDay.setText(string_current_day, TextView.BufferType.EDITABLE);
        spinner_month.setSelection(current_month);
        spinner_hour.setSelection(current_hour);
        spinner_minutes.setSelection(current_minute);
        String string_current_year = Integer.toString( current_year );
        final EditText editYear = (EditText) findViewById(R.id.year);
        editYear.setText(string_current_year, TextView.BufferType.EDITABLE);

        //day validation
        editDay.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                selectedDay = Integer.valueOf(editDay.getText().toString());
                selectedMonth = spinner_month.getSelectedItemPosition();
                selectedYear = Integer.valueOf(editYear.getText().toString());

                if (selectedYear < 100 ) {
                    selectedYear = Integer.valueOf( "20" + String.valueOf( selectedYear ));
                } else if (selectedYear < 1000) {
                    selectedYear = Integer.valueOf( "2" + String.valueOf( selectedYear ));
                }
                editYear.setText(Integer.toString(selectedYear));

                String formatted_day = String.format("%02d", selectedDay);
                String formatted_month = String.format("%02d", selectedMonth);

                if ( !isValidDate( selectedYear + "-" + formatted_month + "-" + formatted_day )) {
                    Toast.makeText(AddDeadlines.this,
                            "Please enter a valid date", Toast.LENGTH_SHORT).show();
                    turn_red_date();
                } else {
                    turn_black_date();
                }

                if(selectedYear < current_year)
                {
                    Toast.makeText(AddDeadlines.this,
                            "Please enter a future date", Toast.LENGTH_SHORT).show();
                    turn_red_date();
                }
                else if(selectedMonth < current_month && selectedYear <= current_year)
                {
                    Toast.makeText(AddDeadlines.this,
                            "Please enter a future date", Toast.LENGTH_SHORT).show();
                    turn_red_date();
                }
                else if(selectedDay < current_day && selectedMonth <= current_month && selectedYear <= current_year)
                {
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

                if (selectedYear < 100 ) {
                    selectedYear = Integer.valueOf( "20" + String.valueOf( selectedYear ));
                } else if (selectedYear < 1000) {
                    selectedYear = Integer.valueOf( "2" + String.valueOf( selectedYear ));
                }
                editYear.setText(Integer.toString( selectedYear) );

                String formatted_day = String.format("%02d", selectedDay);
                String formatted_month = String.format("%02d", selectedMonth);

                if ( !isValidDate( selectedYear + "-" + formatted_month + "-" + formatted_day )) {
                    Toast.makeText(AddDeadlines.this,
                            "Please enter a valid date", Toast.LENGTH_SHORT).show();
                    turn_red_date();
                } else {
                    turn_black_date();
                }

                if(selectedYear < current_year)
                {
                    Toast.makeText(AddDeadlines.this,
                            "Please enter a future date", Toast.LENGTH_SHORT).show();
                    turn_red_date();
                }
                else if(selectedMonth < current_month && selectedYear <= current_year)
                {
                    Toast.makeText(AddDeadlines.this,
                            "Please enter a future date", Toast.LENGTH_SHORT).show();
                    turn_red_date();
                }
                else if(selectedDay < current_day && selectedMonth <= current_month && selectedYear <= current_year)
                {
                    Toast.makeText(AddDeadlines.this,
                            "Please enter a future date", Toast.LENGTH_SHORT).show();
                    turn_red_date();
                } else {
                    turn_black_date();
                }
            }
        });
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
