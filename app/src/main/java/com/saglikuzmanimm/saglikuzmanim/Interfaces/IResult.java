package com.saglikuzmanimm.saglikuzmanim.Interfaces;

public interface IResult {
    void onSuccess();
    void onFailed(Exception exception);
}
