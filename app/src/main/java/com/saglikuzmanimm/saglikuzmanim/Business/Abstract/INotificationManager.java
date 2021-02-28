package com.saglikuzmanimm.saglikuzmanim.Business.Abstract;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Notification;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetNotificationListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntityRepository;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public interface INotificationManager extends IEntityRepository<Notification,IResult, IGetNotificationListener> {


}
