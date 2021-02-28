package com.saglikuzmanimm.saglikuzmanim.Time;

import android.net.ParseException;

import java.util.Date;

public abstract class TimeCalculator {

   public Long calculateRemainingTime(Date date){
        try {

            Date now = new Date();
            long currentTime = now.getTime();
            long appointmentDate = date.getTime();
            long remaining_time = appointmentDate - currentTime;

            return remaining_time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
