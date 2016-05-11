package com.example.craftycoders.deadlineapplication;

import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Chloe on 10/05/16.
 */
public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm+1, dd);
    }
    public void populateSetDate(int year, int month, int day) {
        String selectedDayFormatted = String.format("%02d", day);
        String selectedMonthFormatted = String.format("%02d", month);

        TextView selectedDate = (TextView) getActivity().findViewById(R.id.selectedDate);
        selectedDate.setText(selectedDayFormatted + "/" + selectedMonthFormatted + "/" + String.valueOf(year));

        EditText editDay = (EditText) getActivity().findViewById(R.id.day);
        EditText editMonth = (EditText) getActivity().findViewById(R.id.month);
        EditText editYear = (EditText) getActivity().findViewById(R.id.year);

        editDay.setText(selectedDayFormatted);
        editMonth.setText(selectedMonthFormatted);
        editYear.setText(String.valueOf(year));
    }
}