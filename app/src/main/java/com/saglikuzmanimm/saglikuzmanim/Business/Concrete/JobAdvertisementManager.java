package com.saglikuzmanimm.saglikuzmanim.Business.Concrete;

import com.saglikuzmanimm.saglikuzmanim.Business.Abstract.IJobAdvertisementManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.JobAdvertisement;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract.IJobAdvertisementDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetJobAdvertisementListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public class JobAdvertisementManager implements IJobAdvertisementManager<JobAdvertisement, IResult, IGetJobAdvertisementListener> {

    private IJobAdvertisementDal _iJobAdvertisementDal;

    public JobAdvertisementManager(IJobAdvertisementDal iJobAdvertisementDal){
        this._iJobAdvertisementDal = iJobAdvertisementDal;
    }


    @Override
    public void addData(JobAdvertisement entity, IResult iResult) {
        _iJobAdvertisementDal.addData(entity,iResult);
    }

    @Override
    public void updateData(JobAdvertisement entity, IResult iResult) {
        _iJobAdvertisementDal.updateData(entity,iResult);

    }

    @Override
    public void delete(JobAdvertisement entity, IResult iResult) {
        _iJobAdvertisementDal.delete(entity,iResult);
    }

    @Override
    public void getData(JobAdvertisement entity, IGetJobAdvertisementListener iGetListener) {
        _iJobAdvertisementDal.getData(null,iGetListener);
    }

    @Override
    public void getJobAdvertisementByLocationAndDepartment(String location, String department, IGetJobAdvertisementListener iGetListener) {
        _iJobAdvertisementDal.getJobAdvertisementByLocationAndDepartment(location,department,iGetListener);
    }
}
