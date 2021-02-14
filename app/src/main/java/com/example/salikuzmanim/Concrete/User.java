package com.example.salikuzmanim.Concrete;
import android.net.Uri;

import com.example.salikuzmanim.Interfaces.IEntity;
public class User extends Person implements IEntity {
    private String TcNumber;

    public User (){}
    public User(String firstName, String lastName, Uri imageProfile){
        super(firstName,lastName,imageProfile);
    }
    public User(String firstName, String lastName, String email,String type,String userUid, Uri imageProfile){
        super(firstName,lastName,email,type,userUid,imageProfile);
    }
    public User(String firstName,String lastName,String email){
        super(firstName,lastName,email);

    }

    public User(String firstName, String lastName, String email, String password,String type,String TcNumber) {
        super(firstName, lastName, email, password,type);
        this.setTcNumber(TcNumber);
    }

    public User(String email,String password){
        super(email,password);
    }


    public String getTcNumber() {
        return TcNumber;
    }

    public void setTcNumber(String tcNumber) {
        TcNumber = tcNumber;
    }
}
