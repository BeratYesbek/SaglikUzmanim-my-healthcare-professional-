package com.example.salikuzmanim.Concrete;

public class Chat {

    private String messageID;
    private String message;
    private String reciverID;
    private String senderID;
    private String messageTime;
    private boolean isSeen;
    public Chat(){}
    public Chat(String message, String messageID, String reciverID, String senderID, String messageTimestamp, boolean isSeen) {

        this.message = message;
        this.setMessageID(messageID);
        this.reciverID = reciverID;
        this.senderID = senderID;
        this.messageTime = messageTimestamp;
        this.isSeen = isSeen;

    }
    public Chat(String senderID, String reciverID){
        this.senderID = senderID;
        this.reciverID = reciverID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReciverID() {
        return reciverID;
    }

    public void setReciverID(String reciverID) {
        this.reciverID = reciverID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime (String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
