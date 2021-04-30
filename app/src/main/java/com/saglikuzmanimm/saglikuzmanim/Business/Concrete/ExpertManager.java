package com.saglikuzmanimm.saglikuzmanim.Business.Concrete;

import com.saglikuzmanimm.saglikuzmanim.Business.Abstract.IExpertManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Collection;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Expert;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract.IExpertDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetExpertListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetQueryListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IGetListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public class ExpertManager implements IExpertManager<Expert,IResult,IGetListener> {


    private IExpertDal _expertDal;

    public ExpertManager(IExpertDal expertDal) {
        this._expertDal = expertDal;
    }

    @Override
    public void addData(Expert entity, IResult iResult) {

    }

    @Override
    public void updateData(Expert entity, IResult iResult) {
        _expertDal.updateData(entity,iResult);
    }

    @Override
    public void delete(Expert entity, IResult iResult) {

    }

    @Override
    public void getData(Expert entity, IGetExpertListener iGetListener) {
        _expertDal.getData(entity,iGetListener);
    }

    @Override
    public void getExpertById(String expertUid, IGetListener iGetListener) {
        _expertDal.getExpertById(expertUid, (IGetExpertListener) iGetListener);
    }


    @Override
    public void createExpertAccount(Expert entity, IResult iResult) {
        _expertDal.createExpertAccount(entity,iResult);
    }

    @Override
    public void updateExpertProfileImage(Expert entity, IResult iResult) {
        System.out.println(15);
        _expertDal.updateExpertProfileImage(entity,iResult);
    }


    @Override
    public void uploadExpertVideo(Expert entity, IResult iResult) {
        _expertDal.uploadExpertVideo(entity, iResult);
    }


    @Override
    public void getExpertQuery(IGetQueryListener iGetQueryListener) {
        _expertDal.getExpertQuery(iGetQueryListener);
    }

    @Override
    public void getAllExpert(Collection entity, IGetListener iGetListener) {
        _expertDal.getAllExpert(entity, (IGetExpertListener) iGetListener);
    }
}




