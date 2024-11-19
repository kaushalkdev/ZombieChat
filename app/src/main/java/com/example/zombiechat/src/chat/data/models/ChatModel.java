package com.example.zombiechat.src.chat.data.models;

public class ChatModel {

    //sequencing of this is also important
    String message;
    String sendBy;
    String sentTO;
    String time;

    public ChatModel(String message, String sendBy, String sentTO, String time) {
        this.message = message;
        this.sendBy = sendBy;
        this.sentTO = sentTO;
        this.time = time;
    }

    public ChatModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public String getSentTO() {
        return sentTO;
    }

    public void setSentTO(String sentTO) {
        this.sentTO = sentTO;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
