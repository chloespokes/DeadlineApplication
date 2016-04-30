package com.example.craftycoders.deadlineapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;
import android.text.TextWatcher;
import android.widget.Toast;
import java.util.*;


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
        //Log.i("System.out", Integer.toString(current_hour));
        //Log.i("System.out", Integer.toString(current_minute));

        //setting spinner for months
        spinner_month = (Spinner) findViewById(R.id.month);
        ArrayAdapter<String> adapter_state_month = new ArrayAdapter<String>(this,
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

        //set current day, month, year
        String string_current_day = Integer.toString( current_day );
        EditText editDay = (EditText) findViewById(R.id.day);
        editDay.setText(string_current_day, TextView.BufferType.EDITABLE);

        //spinner.setAdapter(adapter_state);
        spinner_month.setSelection(current_month);
        // spinner.setAdapter(adapter_state_2);
        spinner_hour.setSelection(current_hour);
        // spinner.setAdapter(adapter_state_3);
        spinner_minutes.setSelection(current_minute);


        String string_current_year = Integer.toString( current_year );
        EditText editYear = (EditText) findViewById(R.id.year);
        editYear.setText( string_current_year, TextView.BufferType.EDITABLE );
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
