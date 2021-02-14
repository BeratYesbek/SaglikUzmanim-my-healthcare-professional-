package com.example.salikuzmanim.Interfaces.GetDataListener;

import com.example.salikuzmanim.Interfaces.IEntity;

public interface IGetJobAdvertisementDataListener<T extends IEntity> {
    void onSuccess(T entity);
    void onFailed(Exception e);
}
