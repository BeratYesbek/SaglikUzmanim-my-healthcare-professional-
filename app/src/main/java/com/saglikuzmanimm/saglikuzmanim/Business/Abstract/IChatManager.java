package com.saglikuzmanimm.saglikuzmanim.Business.Abstract;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Chat;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetChatListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetMessageArrayListListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntity;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntityRepository;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IGetListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public interface IChatManager<T extends IEntity, R extends IResult, E extends IGetListener> extends IEntityRepository<Chat, IResult, IGetChatListener> {
    void seenMessages(String userID);



    void getMessageForList(String whoSend, IGetMessageArrayListListener iGetListener);

}
