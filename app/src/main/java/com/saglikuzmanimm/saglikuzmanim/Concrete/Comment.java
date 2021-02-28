package com.saglikuzmanimm.saglikuzmanim.Concrete;

import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntity;
import com.google.firebase.Timestamp;

public class Comment implements IEntity {
    private String _comment;
    private String _receiverID;
    private String _senderID;
    private Float _point;
    private Timestamp _timestamp;

    public String get_comment() {
        return _comment;
    }

    public void set_comment(String _comment) {
        this._comment = _comment;
    }

    public Timestamp get_timestamp() {
        return _timestamp;
    }

    public void set_timestamp(Timestamp _timestamp) {
        this._timestamp = _timestamp;
    }


    public String get_receiverID() {
        return _receiverID;
    }

    public void set_receiverID(String _receiverID) {
        this._receiverID = _receiverID;
    }

    public String get_senderID() {
        return _senderID;
    }

    public void set_senderID(String _senderID) {
        this._senderID = _senderID;
    }

    public Float get_point() {
        return _point;
    }

    public void set_point(Float _point) {
        this._point = _point;
    }
}
