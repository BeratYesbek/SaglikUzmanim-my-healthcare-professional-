package com.saglikuzmanimm.saglikuzmanim.Interfaces;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Appointment;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Person;

public interface IVideoMeetingListener {
    void initiateVideoMeeting(Person person, Appointment appointment);

}
