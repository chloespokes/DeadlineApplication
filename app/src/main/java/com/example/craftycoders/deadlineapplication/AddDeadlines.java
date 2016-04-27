package com.example.craftycoders.deadlineapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
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
    Spinner spinnerMonths;
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);

    private String[] month = { "January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October", "November",
            "December" };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deadlines);

        setTitle("Add New Deadline");

        System.out.println(month.length);

        EditText editDay = (EditText) findViewById(R.id.day);

        spinnerMonths = (Spinner) findViewById(R.id.month);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, month);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonths.setAdapter(adapter_state);
        spinnerMonths.setOnItemSelectedListener(this);

        EditText editYear = (EditText) findViewById(R.id.year);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        spinnerMonths.setSelection(position);
        String selectedMonth = (String) spinnerMonths.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
