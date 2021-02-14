package com.example.salikuzmanim.Concrete;

public class Token {
    private String token;
    private String userID;

    public  Token(String token,String userID){
        this.setToken(token);
        this.setUserID(userID);
    }
    public  Token(){
        this.setToken(token);
        this.setUserID(userID);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
