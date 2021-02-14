package com.example.salikuzmanim.Interfaces.GetDataListener;

import com.example.salikuzmanim.Interfaces.IEntity;

public interface IGetExpertDataListener<T extends IEntity> {
    void onSuccess(T entity);
    void onError(Exception exception);
}
