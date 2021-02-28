package com.saglikuzmanimm.saglikuzmanim.Concrete;
import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntity;
import com.google.firebase.Timestamp;

public class Appointment implements IEntity {
    private String _senderID;
    private String _receiverID;
    private String _documentID;
    private String _appointmentID;
    private String _whoCanceled;
    private Boolean _situation;
    private Boolean _abort;
    private Boolean _payment;
    private Boolean _completed;

    private Timestamp _timestamp_appointment_date;
    private Timestamp _timestamp_sendTo_time;
    private Float _appointmentPrice;
    public Appointment(){}
    public Appointment(String senderID,String receiverID,String documentID,String appointmentID,String whoCanceled,Boolean situation,Boolean abort,Boolean payment,Timestamp timestamp_appointment_date,Timestamp timestamp_sendTo_time,Float appointmentPrice){
        this.set_senderID(senderID);
        this.set_receiverID(receiverID);
        this.set_documentID(documentID);
        this.set_appointmentID(appointmentID);
        this.set_whoCanceled(whoCanceled);
        this.set_situation(situation);
        this.set_abort(abort);
        this.set_payment(payment);
        this.set_timestamp_appointment_date(timestamp_appointment_date);
        this.set_timestamp_sendTo_time(timestamp_sendTo_time);
        this.set_appointmentPrice(appointmentPrice);
    }

    public Appointment(String documentID,String whoCanceled,Boolean abort,Boolean payment,Boolean situation){
        this._documentID = documentID;
        this._whoCanceled = whoCanceled;
        this._abort = abort;
        this._payment = payment;
        this._situation = situation;
    }


    public String get_senderID() {
        return _senderID;
    }

    public void set_senderID(String _senderID) {
        this._senderID = _senderID;
    }

    public String get_receiverID() {
        return _receiverID;
    }

    public void set_receiverID(String _receiverID) {
        this._receiverID = _receiverID;
    }

    public String get_documentID() {
        return _documentID;
    }

    public void set_documentID(String _documentID) {
        this._documentID = _documentID;
    }

    public String get_appointmentID() {
        return _appointmentID;
    }

    public void set_appointmentID(String _appointmentID) {
        this._appointmentID = _appointmentID;
    }

    public String get_whoCanceled() {
        return _whoCanceled;
    }

    public void set_whoCanceled(String _whoCanceled) {
        this._whoCanceled = _whoCanceled;
    }

    public Boolean get_situation() {
        return _situation;
    }

    public void set_situation(Boolean _situation) {
        this._situation = _situation;
    }

    public Boolean get_abort() {
        return _abort;
    }

    public void set_abort(Boolean _abort) {
        this._abort = _abort;
    }

    public Boolean get_payment() {
        return _payment;
    }

    public void set_payment(Boolean _payment) {
        this._payment = _payment;
    }

    public Timestamp get_timestamp_appointment_date() {
        return _timestamp_appointment_date;
    }

    public void set_timestamp_appointment_date(Timestamp _timestamp_appointment_date) {
        this._timestamp_appointment_date = _timestamp_appointment_date;
    }

    public Timestamp get_timestamp_sendTo_time() {
        return _timestamp_sendTo_time;
    }

    public void set_timestamp_sendTo_time(Timestamp _timestamp_sendTo_time) {
        this._timestamp_sendTo_time = _timestamp_sendTo_time;
    }

    public Float get_appointmentPrice() {
        return _appointmentPrice;
    }

    public void set_appointmentPrice(Float _appointmentPrice) {
        this._appointmentPrice = _appointmentPrice;
    }

    public Boolean get_completed() {
        return _completed;
    }

    public void set_completed(Boolean _completed) {
        this._completed = _completed;
    }
}
