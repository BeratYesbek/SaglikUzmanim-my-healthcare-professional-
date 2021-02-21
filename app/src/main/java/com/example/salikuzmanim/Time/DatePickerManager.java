package com.example.salikuzmanim.Time;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;


public class DatePickerManager implements DatePickerDialog.OnDateSetListener {

    private Context _context;
    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


    public void showDatePickerDialog(Context context){
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,this, Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        _context = context;
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        String date = day + "-" +MONTHS[month] + "-" +year;
        timepickerManager timepickerManager = new timepickerManager(date);
        timepickerManager.getTime(_context);

    }
}
