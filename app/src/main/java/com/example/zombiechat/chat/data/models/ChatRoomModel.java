package com.example.zombiechat.chat.data.models;

public class ChatRoomModel {
    private String chatRoomId;
    private String friendId;

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public String getFriendId() {
        return friendId;
    }

    // Empty constructor for Json parsing reasons.
    public ChatRoomModel() {

    }
}
