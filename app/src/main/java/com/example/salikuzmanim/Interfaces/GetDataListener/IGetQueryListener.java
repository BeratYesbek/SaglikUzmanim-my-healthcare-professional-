package com.example.salikuzmanim.Interfaces.GetDataListener;

import com.google.firebase.firestore.QuerySnapshot;

public interface IGetQueryListener<T extends QuerySnapshot> {
    void onSuccess(T queryDocument);
    void onFailed(Exception e);
}
