package com.example.zombiechat;

public class SingleChatModel {
    String message;
    String sentTo;
    String sendBy;
    String time;

    public SingleChatModel() {
    }

    public SingleChatModel(String message, String sentTo, String sendBy, String time) {
        this.message = message;
        this.sentTo = sentTo;
        this.sendBy = sendBy;
        this.time = time;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
