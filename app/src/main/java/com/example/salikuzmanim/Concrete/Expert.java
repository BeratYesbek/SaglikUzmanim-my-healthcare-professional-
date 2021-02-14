package com.example.salikuzmanim.Concrete;

import android.net.Uri;

import com.example.salikuzmanim.Interfaces.IEntity;

public class Expert extends Person implements IEntity {

    private Boolean _check_expert;
    private Uri _diplomaImage;
    private Uri _idCardImage;
    private Uri _expertVideo;

    private Float _point;
    private Float _appointmentPrice;
    private String _expertUid;
    private String TcNumber;
    private String _department;
    private String _about;



    public Expert() {
    }

    public Expert(Uri expertVideo) {
        this._expertVideo = expertVideo;
    }

    public Expert(String firstName, String lastName, String email, String password, String type, String TcNumber, String departmant, Uri diplomaImage, Uri idCardImage) {
        super(firstName, lastName, email, password, type);
        this.setTcNumber(TcNumber);
        this.set_department(departmant);
        this.set_diplomaImage(diplomaImage);
        this.set_idCardImage(idCardImage);

    }

    public Expert(String firstName, String lastName, String email, String department,
                  String type, String about, Float appointmentPrice, Float point,
                  Boolean check_expert, String expertUid, Uri profileImage, Uri expertVideo) {

        super(firstName, lastName, email, type, expertUid, profileImage);
        this.set_department(department);
        this.set_about(about);
        this.set_check_expert(check_expert);
        this.set_appointmentPrice(appointmentPrice);
        this._expertUid = expertUid;
        this.set_point(point);
        this.set_expertVideo(expertVideo);

    }

    public Expert(String firstName, String lastName, String email, String department, String about, Float point, Boolean check_circle, String expertUid) {
        super(firstName, lastName, email);
        this.set_department(department);
        this.set_about(about);
        this.set_point(point);
        this.set_check_expert(check_circle);
        this.set_expertUid(expertUid);

    }

    public Boolean get_check_expert() {
        return _check_expert;
    }

    public void set_check_expert(Boolean _check_expert) {
        this._check_expert = _check_expert;
    }

    public Uri get_diplomaImage() {
        return _diplomaImage;
    }

    public void set_diplomaImage(Uri _diplomaImage) {
        this._diplomaImage = _diplomaImage;
    }

    public Uri get_idCardImage() {
        return _idCardImage;
    }

    public void set_idCardImage(Uri _idCardImage) {
        this._idCardImage = _idCardImage;
    }

    public Uri get_expertVideo() {
        return _expertVideo;
    }

    public void set_expertVideo(Uri _expertVideo) {
        this._expertVideo = _expertVideo;
    }

    public Float get_point() {
        return _point;
    }

    public void set_point(Float _point) {
        this._point = _point;
    }

    public Float get_appointmentPrice() {
        return _appointmentPrice;
    }

    public void set_appointmentPrice(Float _appointmentPrice) {
        this._appointmentPrice = _appointmentPrice;
    }

    public String get_expertUid() {
        return _expertUid;
    }

    public void set_expertUid(String _expertUid) {
        this._expertUid = _expertUid;
    }

    public String getTcNumber() {
        return TcNumber;
    }

    public void setTcNumber(String tcNumber) {
        TcNumber = tcNumber;
    }

    public String get_department() {
        return _department;
    }

    public void set_department(String _department) {
        this._department = _department;
    }

    public String get_about() {
        return _about;
    }

    public void set_about(String _about) {
        this._about = _about;
    }
}





