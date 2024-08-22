package com.example.zombiechat.account.data.repo

import com.example.zombiechat.account.data.models.UserModel

interface ProfileRepo {
    fun getUser(userId: String): UserModel
    fun userAFriend(currentUserId: String, otherUserId: String): Boolean
    fun activeFriendRequest(currentUserId: String, otherUserId: String): Boolean
    fun sendFriendRequest(currentUserId: String, otherUserId: String): Boolean
}

class ProfileRepoImpl : ProfileRepo {
    override fun getUser(userId: String): UserModel {
        TODO("Not yet implemented")
    }

    override fun userAFriend(currentUserId: String, otherUserId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun activeFriendRequest(currentUserId: String, otherUserId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun sendFriendRequest(currentUserId: String, otherUserId: String): Boolean {
        TODO("Not yet implemented")
    }
}