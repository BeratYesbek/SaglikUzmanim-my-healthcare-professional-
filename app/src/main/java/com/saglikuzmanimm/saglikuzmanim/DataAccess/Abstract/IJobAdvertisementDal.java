package com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract;

import com.saglikuzmanimm.saglikuzmanim.Concrete.JobAdvertisement;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetJobAdvertisementListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntity;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntityRepository;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IGetListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public interface IJobAdvertisementDal<T extends IEntity,R extends IResult,E extends IGetListener> extends IEntityRepository<JobAdvertisement,IResult, IGetJobAdvertisementListener> {

    void getJobAdvertisementByLocationAndDepartment(String location,String department,E iGetListener);

}
