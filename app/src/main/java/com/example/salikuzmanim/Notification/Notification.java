package com.example.salikuzmanim.Notification;

public class Notification  {

    private String token;
    private String message;
    private String userName;
    private String userID;
    public Notification(String token,String message,String userName) {
        this.setToken(token);
        this.setMessage(message);
        this.setUserName(userName);
    }

    public Notification() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
