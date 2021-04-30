package com.saglikuzmanimm.saglikuzmanim.Business.Concrete;

import android.content.Context;

import com.saglikuzmanimm.saglikuzmanim.Business.Abstract.ITokenManager;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract.ITokenDal;

public class TokenManager implements ITokenManager {

    private ITokenDal _tokenDal;

    public TokenManager(ITokenDal tokenDal) {
        this._tokenDal = tokenDal;
    }

    @Override
    public void getToken(Context context,String collection) {
        _tokenDal.getToken(context,collection);
    }
}
