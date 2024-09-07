package com.example.zombiechat.friends.view.request;

public class RequestModel {

    String requeststatus;
    String sendBy;
    String sentTo;

    public RequestModel() {
    }

    public RequestModel(String requeststatus, String sendBy, String sentTo) {
        this.requeststatus = requeststatus;
        this.sendBy = sendBy;
        this.sentTo = sentTo;
    }

    public String getRequeststatus() {
        return requeststatus;
    }

    public void setRequeststatus(String requeststatus) {
        this.requeststatus = requeststatus;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }
}
