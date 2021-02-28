package com.saglikuzmanimm.saglikuzmanim.Calculator;

import com.saglikuzmanimm.saglikuzmanim.Time.TimeCalculator;

import java.util.Calendar;
import java.util.Date;

public class AppointmentTimeCalculator extends TimeCalculator {

    public Long remainingFifteenMinutesCalculator(Date date){

        try{
            // added 15 minutes over appointment date by this method
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, 15);

            Date date1 = calendar.getTime();

            Date now = new Date();
            long currentTime = now.getTime();
            long addingTime = date1.getTime();
            long remaining_time = addingTime - currentTime;

            return remaining_time;
        }catch (Exception exception){

        }
        return null;
    }
}
