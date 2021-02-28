package com.saglikuzmanimm.saglikuzmanim.Business.Concrete;

import android.content.Context;

import com.saglikuzmanimm.saglikuzmanim.Business.Abstract.ITokenManager;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract.ITokenDal;

public class TokenManager implements ITokenManager {

    private ITokenDal _iTokenDal;

    public TokenManager(ITokenDal iTokenDal ) {
        this._iTokenDal = iTokenDal;
    }

    @Override
    public void getToken(Context context,String collection) {
        _iTokenDal.getToken(context,collection);
    }
}
