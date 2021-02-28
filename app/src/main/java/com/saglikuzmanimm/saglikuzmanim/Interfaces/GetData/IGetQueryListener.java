package com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData;

import com.google.firebase.firestore.QuerySnapshot;

public interface IGetQueryListener<T extends QuerySnapshot> {
    void onSuccess(T queryDocument);
    void onFailed(Exception e);
}
