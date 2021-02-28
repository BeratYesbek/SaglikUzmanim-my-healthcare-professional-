package com.saglikuzmanimm.saglikuzmanim.Concrete;

import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntity;
import com.google.firebase.Timestamp;

public class Notification implements IEntity {
    private String _receiverID;
    private String _senderID;
    private String _messageBody;
    private String _messageTitle;
    private Boolean _isSeen;
    private Timestamp _timestamp;

    private String _documentID;

    public Notification(String receiverID, String senderID, String messageBody, String messageTitle, Boolean isSeen) {
        this.set_receiverID(receiverID);
        this.set_senderID(senderID);
        this.set_messageBody(messageBody);
        this.set_messageTitle(messageTitle);
        this.set_isSeen(isSeen);
    }
    public Notification(String receiverID, String senderID, String messageBody, String messageTitle, Boolean isSeen,String documentID,Timestamp timestamp) {
        this.set_receiverID(receiverID);
        this.set_senderID(senderID);
        this.set_messageBody(messageBody);
        this.set_messageTitle(messageTitle);
        this.set_isSeen(isSeen);
        this._documentID = documentID;
        this._timestamp = timestamp;
    }


    public Notification(String documentID) {
        this.set_documentID(documentID);
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

    public String get_messageBody() {
        return _messageBody;
    }

    public void set_messageBody(String _messageBody) {
        this._messageBody = _messageBody;
    }

    public String get_messageTitle() {
        return _messageTitle;
    }

    public void set_messageTitle(String _messageTitle) {
        this._messageTitle = _messageTitle;
    }

    public Boolean get_isSeen() {
        return _isSeen;
    }

    public void set_isSeen(Boolean _isSeen) {
        this._isSeen = _isSeen;
    }

    public Timestamp getTimestamp() {
        return _timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this._timestamp = timestamp;
    }

    public String get_documentID() {
        return _documentID;
    }

    public void set_documentID(String _documentID) {
        this._documentID = _documentID;
    }
}
