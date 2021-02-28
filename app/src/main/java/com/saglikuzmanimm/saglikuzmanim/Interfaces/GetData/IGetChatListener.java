package com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Chat;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IGetListener;

import java.util.ArrayList;

public interface IGetChatListener extends IGetListener {

    void onSuccess(ArrayList<Chat> chatArrayList);

    void onFailed(Exception exception);
}
