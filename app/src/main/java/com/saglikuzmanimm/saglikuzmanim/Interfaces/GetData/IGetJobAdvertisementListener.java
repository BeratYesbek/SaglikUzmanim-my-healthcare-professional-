package com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData;

import com.saglikuzmanimm.saglikuzmanim.Concrete.JobAdvertisement;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IGetListener;

import java.util.ArrayList;

public interface IGetJobAdvertisementListener extends IGetListener {
    void onSuccess(ArrayList<JobAdvertisement> jobAdvertisementArrayList);
    void onFailed(Exception exception);
}
