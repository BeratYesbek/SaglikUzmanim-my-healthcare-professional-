package com.example.salikuzmanim.Interfaces.FireBaseInsterfaces;

import com.example.salikuzmanim.Concrete.User;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetExpertDataListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetQueryListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetUserDataListener;
import com.example.salikuzmanim.Interfaces.IEntity;

public interface IFireBaseUserDal<T extends IEntity> {
    void insertUser(T entity, IGetUserDataListener iGetUserDataListener);

    void getUserData(IGetUserDataListener iGetUserDataListener);

    void updateUser(T entity, IGetUserDataListener iGetUserDataListener);

    void updateUserProfile(T entity,IGetUserDataListener iGetUserDataListener);

    void createUserAccount(T entity,IGetUserDataListener iGetUserDataListener);

    void getAllUser(IGetQueryListener iGetQueryListener);

    void getUserProfileImage(User entity, IGetExpertDataListener iGetExpertDataListener);

}
