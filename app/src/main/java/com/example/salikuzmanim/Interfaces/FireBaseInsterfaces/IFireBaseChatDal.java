package com.example.salikuzmanim.Interfaces.FireBaseInsterfaces;

import com.example.salikuzmanim.Interfaces.GetDataListener.IGetDataListener;
import com.example.salikuzmanim.Concrete.Message;

public interface IFireBaseChatDal {
    void insertMessage(Message message);
    void getMessage(Message message, IGetDataListener iGetDataListener);
    void seenMessages(String userID);
    void getMessageForList(String whoSend,IGetDataListener iGetDataListener);
}
