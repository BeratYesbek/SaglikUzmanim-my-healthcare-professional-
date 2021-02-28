package com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Comment;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IGetListener;

import java.util.ArrayList;
import java.util.List;

public interface IGetCommentListener extends IGetListener {
    void onSuccess(ArrayList<Comment> comments, List<Float> point);
    void onFailed(Exception exception);
}
