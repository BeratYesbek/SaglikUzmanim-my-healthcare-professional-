package com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Expert;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IGetListener;

import java.util.ArrayList;

public interface IGetExpertListener extends IGetListener {
    void onSuccess(ArrayList<Expert> expertArrayList);
    void onFailed(Exception exception);
}
