package com.example.salikuzmanim.Interfaces.GetDataListener;

import com.example.salikuzmanim.Concrete.Appointment;
import com.example.salikuzmanim.Concrete.Person;

public interface IVideoMeetingListener {
    void initiateVideoMeeting(Person person, Appointment appointment);

}
