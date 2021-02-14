package com.example.salikuzmanim.Interfaces.FireBaseInsterfaces;

import com.example.salikuzmanim.Interfaces.GetDataListener.IGetJobAdvertisementDataListener;
import com.example.salikuzmanim.Interfaces.GetDataListener.IGetListDataListener;
import com.example.salikuzmanim.Interfaces.IEntity;

public interface IFireBaseJobAdvertisementDal<T extends IEntity> {

    void addJobAdvertisement(T entity, IGetJobAdvertisementDataListener iGetJobAdvertisementDataListener);

    void updateJobAdvertisement(T entity, IGetJobAdvertisementDataListener iGetJobAdvertisementDataListener);

    void getJobAdvertisement(IGetListDataListener iGetListDataListener);

    void deleteJobAdvertisement(T entity, IGetJobAdvertisementDataListener iGetJobAdvertisementDataListener);

    void getAllJobAdvertisement(String location,String department, IGetListDataListener iGetListDataListener);

}
