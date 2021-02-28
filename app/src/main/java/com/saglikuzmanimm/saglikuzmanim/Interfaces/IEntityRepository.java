package com.saglikuzmanimm.saglikuzmanim.Interfaces;

public interface IEntityRepository<T extends IEntity, R extends IResult, E extends IGetListener> {

    void addData(T entity, R iResult);

    void updateData(T entity, R iResult);

    void delete(T entity, R iResult);

    void getData(T entity, E iGetListener);
}
