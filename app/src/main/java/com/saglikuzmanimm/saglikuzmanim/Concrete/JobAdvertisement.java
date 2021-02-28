package com.saglikuzmanimm.saglikuzmanim.Concrete;

import com.saglikuzmanimm.saglikuzmanim.Interfaces.IEntity;

public class JobAdvertisement implements IEntity {
    private String _job_advertisement_title;
    private String _job_advertisement_explanation;
    private String _job_advertisement_department;
    private String _job_advertisement_location;
    private String _job_advertisement_ID;
    private String _documentID;
    private String _uploaderID;
    private com.google.firebase.Timestamp _timestamp;


    public JobAdvertisement(){}
    public JobAdvertisement(String job_advertisement_title, String job_advertisement_explanation, String job_advertisement_department, String job_advertisement_location, String job_advertisement_ID,String uploaderID,String documentID, com.google.firebase.Timestamp timestamp) {
        this.set_job_advertisement_title(job_advertisement_title);
        this.set_job_advertisement_explanation(job_advertisement_explanation);
        this.set_job_advertisement_department(job_advertisement_department);
        this.set_job_advertisement_location(job_advertisement_location);
        this.set_job_advertisement_ID(job_advertisement_ID);
        this.set_documentID(documentID);
        this._uploaderID = uploaderID;
        this.set_timestamp(timestamp);
    }
    public JobAdvertisement(String job_advertisement_title, String job_advertisement_explanation, String job_advertisement_department, String job_advertisement_location, String job_advertisement_ID){
        this._job_advertisement_title = job_advertisement_title;
        this._job_advertisement_explanation = job_advertisement_explanation;
        this._job_advertisement_department = job_advertisement_department;
        this._job_advertisement_location = job_advertisement_location;
        this._job_advertisement_ID = job_advertisement_ID;
    }

    public String get_job_advertisement_title() {
        return _job_advertisement_title;
    }

    public void set_job_advertisement_title(String _job_advertisement_title) {
        this._job_advertisement_title = _job_advertisement_title;
    }

    public String get_job_advertisement_explanation() {
        return _job_advertisement_explanation;
    }

    public void set_job_advertisement_explanation(String _job_advertisement_explanation) {
        this._job_advertisement_explanation = _job_advertisement_explanation;
    }

    public String get_job_advertisement_department() {
        return _job_advertisement_department;
    }

    public void set_job_advertisement_department(String _job_advertisement_department) {
        this._job_advertisement_department = _job_advertisement_department;
    }

    public String get_job_advertisement_location() {
        return _job_advertisement_location;
    }

    public void set_job_advertisement_location(String _job_advertisement_location) {
        this._job_advertisement_location = _job_advertisement_location;
    }

    public String get_job_advertisement_ID() {
        return _job_advertisement_ID;
    }

    public void set_job_advertisement_ID(String _job_advertisement_ID) {
        this._job_advertisement_ID = _job_advertisement_ID;
    }

    public String get_documentID() {
        return _documentID;
    }

    public void set_documentID(String _documentID) {
        this._documentID = _documentID;
    }



    public String get_uploaderID() {
        return _uploaderID;
    }

    public void set_uploaderID(String _uploaderID) {
        this._uploaderID = _uploaderID;
    }

    public com.google.firebase.Timestamp get_timestamp() {
        return _timestamp;
    }

    public void set_timestamp(com.google.firebase.Timestamp _timestamp) {
        this._timestamp = _timestamp;
    }
}
