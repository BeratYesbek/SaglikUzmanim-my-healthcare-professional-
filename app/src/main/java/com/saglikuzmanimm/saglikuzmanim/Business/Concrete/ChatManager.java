package com.saglikuzmanimm.saglikuzmanim.Business.Concrete;

import com.saglikuzmanimm.saglikuzmanim.Business.Abstract.IChatManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Chat;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract.IChatDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetChatListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetMessageArrayListListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public class ChatManager implements IChatManager<Chat, IResult, IGetChatListener> {

    private IChatDal _chatDal;
    public ChatManager(IChatDal chatDal){
        this._chatDal = chatDal;
    }


    @Override
    public void addData(Chat entity, IResult iResult) {
        _chatDal.addData(entity,iResult);
    }

    @Override
    public void updateData(Chat entity, IResult iResult) {

    }

    @Override
    public void delete(Chat entity, IResult iResult) {

    }

    @Override
    public void getData(Chat entity, IGetChatListener iGetListener) {
        _chatDal.getData(entity,iGetListener);
    }

    @Override
    public void seenMessages(String userID) {
        _chatDal.seenMessages(userID);
    }


    @Override
    public void getMessageForList(String whoSend, IGetMessageArrayListListener iGetListener) {
        _chatDal.getMessageForList(whoSend,iGetListener);
    }


}
