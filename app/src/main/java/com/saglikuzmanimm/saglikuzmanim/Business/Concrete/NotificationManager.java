package com.saglikuzmanimm.saglikuzmanim.Business.Concrete;

import com.saglikuzmanimm.saglikuzmanim.Business.Abstract.INotificationManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Notification;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract.INotificationDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetNotificationListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public class NotificationManager implements INotificationManager {

    private INotificationDal _iNotificationDal;

    public NotificationManager(INotificationDal iNotificationDal) {

        _iNotificationDal = iNotificationDal;
    }


    @Override
    public void addData(Notification entity, IResult iResult) {
        _iNotificationDal.addData(entity,iResult);
    }

    @Override
    public void updateData(Notification entity, IResult iResult) {
        _iNotificationDal.updateData(entity,iResult);
    }

    @Override
    public void delete(Notification entity, IResult iResult) {

    }

    @Override
    public void getData(Notification entity, IGetNotificationListener notificationListener) {
        _iNotificationDal.getData(entity,notificationListener);
    }
}
