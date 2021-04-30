package com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Chat;
import com.saglikuzmanimm.saglikuzmanim.Core.Abstract.IFirebaseChatDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetChatListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public interface IChatDal extends IFirebaseChatDal<Chat, IResult, IGetChatListener> {
}
