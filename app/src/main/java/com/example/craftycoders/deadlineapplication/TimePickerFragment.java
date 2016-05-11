package com.example.craftycoders.deadlineapplication;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Chloe on 10/05/16.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        //Do something with the user chosen time
        String time_period;
        if (hourOfDay < 12) {
            time_period = "AM";
        } else {
            time_period = "PM";
        }

        String selectedHourFormatted = String.format("%02d", hourOfDay);
        String selectedMinuteFormatted = String.format("%02d", minute);

        TextView selectedTime = (TextView) getActivity().findViewById(R.id.selectedTime);
        selectedTime.setText(selectedHourFormatted + ":" + selectedMinuteFormatted + time_period );

        EditText editHours = (EditText) getActivity().findViewById(R.id.time_hours);
        EditText editMinutes = (EditText) getActivity().findViewById(R.id.time_minutes);

        editHours.setText(selectedHourFormatted);
        editMinutes.setText(selectedMinuteFormatted);
    }
}