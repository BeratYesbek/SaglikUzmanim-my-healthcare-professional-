package com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Notification;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IGetListener;

import java.util.ArrayList;

public interface IGetNotificationListener extends IGetListener {
    void onSuccess(ArrayList<Notification> notificationArrayList);
    void onFailed(Exception exception);
}
