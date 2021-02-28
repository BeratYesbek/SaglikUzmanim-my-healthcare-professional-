package com.saglikuzmanimm.saglikuzmanim.Business.Concrete;

import com.saglikuzmanimm.saglikuzmanim.Business.Abstract.ICommentManager;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Comment;
import com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract.ICommentDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetCommentListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public class CommentManager implements ICommentManager {


    private ICommentDal _iCommentDal;

    public CommentManager(ICommentDal commentDal) {
        this._iCommentDal = commentDal;
    }

    @Override
    public void addData(Comment entity, IResult iResult) {
        _iCommentDal.addData(entity, iResult);
    }

    @Override
    public void updateData(Comment entity, IResult iResult) {

    }

    @Override
    public void delete(Comment entity, IResult iResult) {

    }

    @Override
    public void getData(Comment entity, IGetCommentListener iGetListener) {
        _iCommentDal.getData(entity, iGetListener);
    }
}
