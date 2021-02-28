package com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Appointment;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IGetListener;

import java.util.ArrayList;

public interface IGetAppointmentListener extends IGetListener {

    void onSuccess(ArrayList<Appointment> appointmentArrayList);

    void onFailed(Exception exception);
}
