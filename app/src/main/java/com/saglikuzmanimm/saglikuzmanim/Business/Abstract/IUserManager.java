package com.saglikuzmanimm.saglikuzmanim.Business.Abstract;

import com.saglikuzmanimm.saglikuzmanim.Concrete.User;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetUserListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetQueryListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntity;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntityRepository;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IGetListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public interface IUserManager<T extends IEntity,R extends IResult,E extends IGetListener>  extends IEntityRepository<User, IResult, IGetUserListener> {
    void updateUserProfile(T entity, R iResult);

    void getUserProfileImage(T entity, E iGetListener);

    void createUserAccount(T entity, R iResult);

    <Q extends IGetQueryListener> void getAllUserQuery(Q iGetQueryListener);
}
