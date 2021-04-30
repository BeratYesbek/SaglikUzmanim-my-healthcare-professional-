package com.saglikuzmanimm.saglikuzmanim.Core.Abstract;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Comment;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetCommentListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntityRepository;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public interface IFirebaseCommentDal extends IEntityRepository<Comment, IResult, IGetCommentListener> {
}
