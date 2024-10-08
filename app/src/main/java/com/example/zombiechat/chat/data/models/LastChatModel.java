package com.example.zombiechat.chat.data.models;

import com.google.firebase.Timestamp;

public class LastChatModel {
    final String chatId;
    final String msg;
    final String userImage;
    final String userName;
    final String userId;
    final Timestamp msgTime;


    public LastChatModel(String chatId, String msg, String userImage, String userName, String userId, Timestamp msgTime) {
        this.chatId = chatId;
        this.msg = msg;
        this.userImage = userImage;
        this.userName = userName;
        this.userId = userId;
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

    public Timestamp getMsgTime() {
        return msgTime;
    }


    public String getUserId() {
        return userId;
    }


}
