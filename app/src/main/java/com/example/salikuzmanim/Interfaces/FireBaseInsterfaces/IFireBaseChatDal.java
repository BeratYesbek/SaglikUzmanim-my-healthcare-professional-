package com.example.salikuzmanim.Interfaces.FireBaseInsterfaces;

import com.example.salikuzmanim.Interfaces.GetDataListener.IGetDataListener;
import com.example.salikuzmanim.Concrete.Chat;

public interface IFireBaseChatDal {
    void insertMessage(Chat chat);
    void getMessage(Chat chat, IGetDataListener iGetDataListener);
    void seenMessages(String userID);
    void getMessageForList(String whoSend,IGetDataListener iGetDataListener);
}
