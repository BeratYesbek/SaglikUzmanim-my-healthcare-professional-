package com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData;

import com.saglikuzmanimm.saglikuzmanim.Concrete.User;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IGetListener;

import java.util.ArrayList;

public interface IGetUserListener extends IGetListener {
    void onSuccess(ArrayList<User> userArrayList);
    void onFailed(Exception exception);
}
