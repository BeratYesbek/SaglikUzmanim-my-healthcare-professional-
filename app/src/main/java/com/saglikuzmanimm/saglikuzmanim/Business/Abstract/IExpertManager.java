package com.saglikuzmanimm.saglikuzmanim.Business.Abstract;

import com.saglikuzmanimm.saglikuzmanim.Concrete.Expert;
import com.saglikuzmanimm.saglikuzmanim.Concrete.Collection;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetExpertListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.GetData.IGetQueryListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntity;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntityRepository;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IGetListener;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IResult;

public interface IExpertManager<T extends IEntity,R extends IResult,E extends IGetListener> extends IEntityRepository<Expert, IResult, IGetExpertListener> {
    void getExpertById(String expertUid, E iGetListener);


    void createExpertAccount(T entity, R iResult);

    void updateExpertProfileImage(T entity, R iResult);


    void uploadExpertVideo(T entity, R iResult);



    void getExpertQuery(IGetQueryListener iGetQueryListener);

    <C extends Collection> void getAllExpert(C entity, E iGetListener);
}
