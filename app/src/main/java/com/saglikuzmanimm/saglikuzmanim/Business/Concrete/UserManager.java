package com.saglikuzmanimm.saglikuzmanim.Business.Concrete;

import com.saglikuzmanimm.saglikuzmanim.Business.Abstract.IUserManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.User;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract.IUserDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetQueryListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetUserListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IGetListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public class UserManager implements IUserManager<User, IResult, IGetListener> {

    private IUserDal _userDal;

    public UserManager(IUserDal userDal) {
        this._userDal = userDal;
    }


    @Override
    public void getAllUserQuery(IGetQueryListener iGetQueryListener) {
        _userDal.getAllUserQuery(iGetQueryListener);
    }


    @Override
    public void updateUserProfile(User entity, IResult iResult) {
        _userDal.updateUserProfile(entity,iResult);
    }

    @Override
    public void getUserProfileImage(User entity, IGetListener iGetListener) {

    }

    @Override
    public void createUserAccount(User entity, IResult iResult) {
        _userDal.createUserAccount(entity,iResult );

    }

    @Override
    public void addData(User entity, IResult iResult) {
    }

    @Override
    public void updateData(User entity, IResult iResult) {
        _userDal.updateData(entity, iResult);
    }

    @Override
    public void delete(User entity, IResult iResult) {

    }

    @Override
    public void getData(User entity, IGetUserListener iGetListener) {
        _userDal.getData(entity, iGetListener);
    }
}
