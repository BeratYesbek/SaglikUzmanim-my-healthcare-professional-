package com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract;

import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntity;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntityRepository;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IGetListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public interface IAppointmentDal<T extends IEntity,R extends IResult,E extends IGetListener> extends IEntityRepository<T, R, E> {

}
