package com.saglikuzmanimm.saglikuzmanim.Core.Abstract;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Notification;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetNotificationListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntity;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntityRepository;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IGetListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public interface IFirebaseNotificationDal<T extends IEntity,R extends IResult,E extends IGetListener> extends IEntityRepository<Notification, IResult, IGetNotificationListener> {

}
