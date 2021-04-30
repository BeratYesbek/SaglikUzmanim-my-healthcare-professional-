package com.saglikuzmanimm.saglikuzmanim.Core.Abstract;

import android.content.Context;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Token;

public interface IFirebaseTokenDal {

    void getToken(Context context, String collection);

    void updateToken(Token token, String collection);

}
