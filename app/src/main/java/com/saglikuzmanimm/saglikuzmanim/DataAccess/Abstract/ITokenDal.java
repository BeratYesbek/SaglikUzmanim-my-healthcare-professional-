package com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract;

import android.content.Context;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Token;

public interface ITokenDal {

    void getToken(Context context, String collection);

    void updateToken(Token token, String collection);

}
