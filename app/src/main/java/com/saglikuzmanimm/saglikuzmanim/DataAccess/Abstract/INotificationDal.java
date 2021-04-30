package com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Notification;
import com.saglikuzmanimm.saglikuzmanim.Core.Abstract.IFirebaseNotificationDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetNotificationListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public interface INotificationDal extends IFirebaseNotificationDal<Notification, IResult, IGetNotificationListener> {
}
