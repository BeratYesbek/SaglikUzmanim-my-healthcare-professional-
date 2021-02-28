package com.saglikuzmanimm.saglikuzmanim.Business.Concrete;

import com.saglikuzmanimm.saglikuzmanim.Business.Abstract.IExpertManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Expert;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Collection;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract.IExpertDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetExpertListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetQueryListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IGetListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public class ExpertManager implements IExpertManager<Expert,IResult,IGetListener> {


    private IExpertDal _iExpertDal;

    public ExpertManager(IExpertDal iExpertDal) {
        this._iExpertDal = iExpertDal;
    }

    @Override
    public void addData(Expert entity, IResult iResult) {

    }

    @Override
    public void updateData(Expert entity, IResult iResult) {
        _iExpertDal.updateData(entity,iResult);
    }

    @Override
    public void delete(Expert entity, IResult iResult) {

    }

    @Override
    public void getData(Expert entity, IGetExpertListener iGetListener) {
        _iExpertDal.getData(entity,iGetListener);
    }

    @Override
    public void getExpertById(String expertUid, IGetListener iGetListener) {
        _iExpertDal.getExpertById(expertUid, iGetListener);
    }


    @Override
    public void createExpertAccount(Expert entity, IResult iResult) {
        _iExpertDal.createExpertAccount(entity,iResult);
    }

    @Override
    public void updateExpertProfileImage(Expert entity, IResult iResult) {
        System.out.println(15);
        _iExpertDal.updateExpertProfileImage(entity,iResult);
    }


    @Override
    public void uploadExpertVideo(Expert entity, IResult iResult) {
        _iExpertDal.uploadExpertVideo(entity, iResult);
    }


    @Override
    public void getExpertQuery(IGetQueryListener iGetQueryListener) {
        _iExpertDal.getExpertQuery(iGetQueryListener);
    }

    @Override
    public void getAllExpert(Collection entity, IGetListener iGetListener) {
        _iExpertDal.getAllExpert(entity, iGetListener);
    }
}




