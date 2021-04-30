package com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract;

import com.saglikuzmanimm.saglikuzmanim.Concrete.JobAdvertisement;
import com.saglikuzmanimm.saglikuzmanim.Core.Abstract.IFirebaseJobAdvertisementDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetJobAdvertisementListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public interface IJobAdvertisementDal extends IFirebaseJobAdvertisementDal<JobAdvertisement, IResult, IGetJobAdvertisementListener> {
}
