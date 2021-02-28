package com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData;

import com.saglikuzmanimm.saglikuzmanim.Concrete.MessageArrayList;

public interface IGetMessageArrayListListener {

    void onSuccess(MessageArrayList messageArrayLists);

    void onFailed(Exception exception);

}
