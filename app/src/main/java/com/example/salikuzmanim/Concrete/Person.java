package com.example.salikuzmanim.Concrete;

import android.net.Uri;

import com.example.salikuzmanim.Interfaces.IEntity;

public class Person implements IEntity {
    private String _firstName;
    private String _lastName;
    private String _email;
    private String _password;
    private String _type;
    private Uri _profileImage;
    private String _ID;


    public Person(){}

    public Person(String firstName, String lastName,Uri profileImage){
        this.set_firstName(firstName);
        this.set_lastName(lastName);
        this.set_profileImage(profileImage);
    }

    public Person(String firstName,String lastName,String email,String type,String ID,Uri profileImage) {

        this.set_firstName(firstName);
        this.set_lastName(lastName);
        this.set_email(email);
        this.set_type(type);
        this.set_ID(ID);
        this.set_profileImage(profileImage);

    }

    public Person(String firstName, String lastName, String email, String password,String type) {
        this.set_firstName(firstName);
        this.set_lastName(lastName);
        this.set_email(email);
        this.set_password(password);
        this.set_type(type);
    }

   public Person(String email,String password){
       this.set_email(email);
      this.set_password(password);
   }



    public Person(String firstName, String lastName, String email) {
        this.set_firstName(firstName);
        this.set_lastName(lastName);
        this.set_email(email);
    }


    public String get_firstName() {
        return _firstName;
    }

    public void set_firstName(String _firstName) {
        this._firstName = _firstName;
    }

    public String get_lastName() {
        return _lastName;
    }

    public void set_lastName(String _lastName) {
        this._lastName = _lastName;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public Uri get_profileImage() {
        return _profileImage;
    }

    public void set_profileImage(Uri _profileImage) {
        this._profileImage = _profileImage;
    }

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }
}
