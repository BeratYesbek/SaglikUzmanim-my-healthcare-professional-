package com.saglikuzmanimm.saglikuzmanim.DataAccess.Abstract;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Expert;
import com.saglikuzmanimm.saglikuzmanim.Core.Abstract.IFirebaseExpertDal;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetExpertListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public interface IExpertDal extends IFirebaseExpertDal<Expert, IResult, IGetExpertListener> {
}
