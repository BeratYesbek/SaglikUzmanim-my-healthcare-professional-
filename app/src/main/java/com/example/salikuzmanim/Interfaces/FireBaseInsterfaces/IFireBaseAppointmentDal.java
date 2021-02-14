package com.example.salikuzmanim.Interfaces.FireBaseInsterfaces;

import com.example.salikuzmanim.Interfaces.GetDataListener.IGetAppointmentDataListener;
import com.example.salikuzmanim.Interfaces.IEntity;

public interface IFireBaseAppointmentDal<T extends IEntity> {
    void sendAppointment(T entity, IGetAppointmentDataListener iGetAppointmentDataListener);
    void updateAppointment(T entity,IGetAppointmentDataListener iGetAppointmentDataListener);
    void getAppointment(IGetAppointmentDataListener iGetAppointmentDataListener);
}
