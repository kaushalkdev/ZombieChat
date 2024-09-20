package com.example.zombiechat.chat.data.models;

public class LastChatModel {
    final String chatId;
    final String msg;
    final String userImage;
    final String userName;
    final String msgTime;

    public LastChatModel(String chatId, String msg, String userImage, String userName, String msgTime) {
        this.chatId = chatId;
        this.msg = msg;
        this.userImage = userImage;
        this.userName = userName;
        this.msgTime = msgTime;
    }


    public String getChatId() {
        return chatId;
    }

    public String getMsg() {
        return msg;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getMsgTime() {
        return msgTime;
    }


}
