package com.saglikuzmanimm.saglikuzmanim.Business.Concrete;


import com.saglikuzmanimm.saglikuzmanim.Business.Abstract.IAppointmentManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Appointment;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract.IAppointmentDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetAppointmentListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public class AppointmentManager implements IAppointmentManager{

    private IAppointmentDal _iAppointmentDal;

    public AppointmentManager(IAppointmentDal iAppointmentDal) {
        this._iAppointmentDal = iAppointmentDal;
    }


    @Override
    public void addData(Appointment entity, IResult iResult) {
        _iAppointmentDal.addData(entity,iResult);
    }

    @Override
    public void updateData(Appointment entity, IResult iResult) {
        _iAppointmentDal.updateData(entity,iResult);

    }

    @Override
    public void delete(Appointment entity, IResult iResult) {

    }

    @Override
    public void getData(Appointment entity, IGetAppointmentListener iGetListener) {
        _iAppointmentDal.getData(entity,iGetListener);

    }

}
