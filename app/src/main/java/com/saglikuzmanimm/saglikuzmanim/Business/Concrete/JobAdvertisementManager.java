package com.saglikuzmanimm.saglikuzmanim.Business.Concrete;

import com.saglikuzmanimm.saglikuzmanim.Business.Abstract.IJobAdvertisementManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.JobAdvertisement;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Concrete.JobAdvertisementDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetJobAdvertisementListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public class JobAdvertisementManager implements IJobAdvertisementManager<JobAdvertisement, IResult, IGetJobAdvertisementListener> {

    private JobAdvertisementDal _jobAdvertisementDal;

    public JobAdvertisementManager(JobAdvertisementDal jobAdvertisementDal){
        this._jobAdvertisementDal = jobAdvertisementDal;
    }


    @Override
    public void addData(JobAdvertisement entity, IResult iResult) {
        _jobAdvertisementDal.addData(entity,iResult);
    }

    @Override
    public void updateData(JobAdvertisement entity, IResult iResult) {
        _jobAdvertisementDal.updateData(entity,iResult);

    }

    @Override
    public void delete(JobAdvertisement entity, IResult iResult) {
        _jobAdvertisementDal.delete(entity,iResult);
    }

    @Override
    public void getData(JobAdvertisement entity, IGetJobAdvertisementListener iGetListener) {
        _jobAdvertisementDal.getData(entity,iGetListener);
    }

    @Override
    public void getJobAdvertisementByLocationAndDepartment(String location, String department, IGetJobAdvertisementListener iGetListener) {
        _jobAdvertisementDal.getJobAdvertisementByLocationAndDepartment(location,department,iGetListener);
    }
}
