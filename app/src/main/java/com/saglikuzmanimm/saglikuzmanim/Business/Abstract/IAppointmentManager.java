package com.saglikuzmanimm.saglikuzmanim.Business.Abstract;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Appointment;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetAppointmentListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntityRepository;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public interface IAppointmentManager extends IEntityRepository<Appointment, IResult, IGetAppointmentListener> {


}
