package com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract;

import com.saglikuzmanimm.saglikuzmanim.Concrete.User;
import com.saglikuzmanimm.saglikuzmanim.Core.Abstract.IFirebaseUserDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetUserListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public interface IUserDal extends IFirebaseUserDal<User, IResult, IGetUserListener> {
}
