package com.example.salikuzmanim.Interfaces.FireBaseInsterfaces;

import com.example.salikuzmanim.Interfaces.GetDataListener.IGetExpertDataListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetListDataListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetUserDataListener;
import com.example.salikuzmanim.Interfaces.IEntity;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetDataListener;
import com.example.salikuzmanim.Concrete.Order;

public interface IFireBaseExpertDal<T extends IEntity> {
    void insertExpert(T entity, IGetExpertDataListener iGetExpertDataListener);

    void updateExpert(T entity, IGetExpertDataListener iGetExpertDataListener);

    void getExpertData(String expertUid,IGetExpertDataListener IGetExpertDataListener);

    void insertCertificateImage(T entity, IGetExpertDataListener iGetExpertDataListener);

    void createExpertAccount(T entity, IGetExpertDataListener iGetExpertDataListener);

    void updateExpertProfileImage(T entity, IGetExpertDataListener iGetExpertDataListener);

    void getExpertProfileImage(T entity, IGetExpertDataListener iGetExpertDataListener);

    void uploadExpertVideo(T entity, IGetUserDataListener iGetUserDataListener);

    void getExpertVideo(T entity, IGetUserDataListener iGetUserDataListener);

   <E extends Order> void getAllExpert(E entity, IGetListDataListener iGetListDataListener);

   void getExpertList(IGetDataListener iGetDataListener);
}
