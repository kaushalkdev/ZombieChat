package com.example.zombiechat.account.data.repo

import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.constants.api.collections.Collections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface ProfileRepo {
    suspend fun getUser(userId: String?): UserModel
    suspend fun userAFriend(otherUserId: String): Boolean
    suspend fun activeFriendRequest(otherUserId: String): Boolean
    suspend fun sendFriendRequest(otherUserId: String): Boolean
}

class ProfileRepoImpl : ProfileRepo {
    private val requestsColl: CollectionReference =
        FirebaseFirestore.getInstance().collection(Collections.requestsCollection)
    private var userCollection: CollectionReference =
        FirebaseFirestore.getInstance().collection(Collections.userCollection)

    private var currentUserId: String? = FirebaseAuth.getInstance().uid
    override suspend fun getUser(userId: String?): UserModel {
        val userSnapshot = userCollection.document(userId ?: currentUserId!!).get().await()
        return userSnapshot.toObject(UserModel::class.java)!!
    }

    override suspend fun userAFriend(otherUserId: String): Boolean {
        val friendsSnapshot =
            requestsColl.whereEqualTo("sendBy", currentUserId).whereEqualTo("sentTo", otherUserId)
                .get().await()

        for (friend in friendsSnapshot) {
            if (friend.toObject(UserModel::class.java).userid == userCollection.document().id) {
                return true
            }
        }
        return false
    }

    override suspend fun activeFriendRequest(otherUserId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun sendFriendRequest(otherUserId: String): Boolean {
        TODO("Not yet implemented")
    }
}