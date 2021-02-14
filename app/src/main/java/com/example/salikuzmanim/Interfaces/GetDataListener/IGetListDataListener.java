package com.example.salikuzmanim.Interfaces.GetDataListener;

import com.example.salikuzmanim.Interfaces.IEntity;

import java.util.ArrayList;

public interface IGetListDataListener<T extends ArrayList<IEntity>> {

    void onSuccess(T entity);

    void onFailed(Exception exception);
}
