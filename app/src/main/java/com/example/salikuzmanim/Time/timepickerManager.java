package com.example.salikuzmanim.Time;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TimePicker;

import com.example.salikuzmanim.Adapter.AdapterShowExpertToUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class timepickerManager {
    int t1hour, t1minute;
    private String time;
    private String date;
    private Context _context;

    public timepickerManager(String date) {
        this.date = date;
    }

    public void getTime(Context context) {
        _context = context;
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                t1hour = i;
                t1minute = i1;

                if(t1hour < 10){
                    time = "0"+t1hour + ":" + t1minute;
                    if(t1minute <10){
                        time = "0"+t1hour + ":" + "0"+t1minute;
                    }
                }else{
                    time = +t1hour + ":" + t1minute;
                }

                SimpleDateFormat f24Hours = new SimpleDateFormat(
                        "HH:mm"
                );
                try {
                    Date date = f24Hours.parse(time);
                    sendDateAdapter();

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, 0, 24, true
        );
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.updateTime(t1hour, t1minute);
        timePickerDialog.show();

    }

    public void sendDateAdapter() {
        if (date != null && time != null){
            System.out.println(date + " " + time);
            AdapterShowExpertToUser.getDate(date,time);
        } else {

        }
    }
}
