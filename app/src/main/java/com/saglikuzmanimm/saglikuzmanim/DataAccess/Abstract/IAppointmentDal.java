package com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Appointment;
import com.saglikuzmanimm.saglikuzmanim.Core.Abstract.IFirebaseAppointmentDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetAppointmentListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public interface IAppointmentDal extends IFirebaseAppointmentDal<Appointment, IResult, IGetAppointmentListener> {
}
