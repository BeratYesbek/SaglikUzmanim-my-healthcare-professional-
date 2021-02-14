package com.example.salikuzmanim.Interfaces.GetDataListener;

import com.example.salikuzmanim.Interfaces.IEntity;

public interface IGetUserDataListener<T extends IEntity> {
    void onSuccess(T entity);
    void onFailed(Exception e);
}
