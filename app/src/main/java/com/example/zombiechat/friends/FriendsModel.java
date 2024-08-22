package com.example.zombiechat.friends;

public class FriendsModel {
    String friendId;
    String userid;

    public FriendsModel() {
    }

    public FriendsModel(String friendId, String userid) {
        this.friendId = friendId;
        this.userid = userid;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
