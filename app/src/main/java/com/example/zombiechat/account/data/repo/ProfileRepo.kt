package com.example.zombiechat.account.data.repo

import com.example.zombiechat.account.data.models.RequestModel
import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.constants.api.collections.Collections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

interface ProfileRepo {
    suspend fun getUser(userId: String): UserModel
    suspend fun userAFriend(otherUserId: String): RequestModel?
    suspend fun activeFriendRequest(otherUserId: String): Boolean
    suspend fun sendFriendRequest(otherUserId: String): Boolean
}

class ProfileRepoImpl : ProfileRepo {
    private val requestsColl: CollectionReference =
        FirebaseFirestore.getInstance().collection(Collections.requestsCollection)
    private var userCollection: CollectionReference =
        FirebaseFirestore.getInstance().collection(Collections.userCollection)

    private var currentUserId: String? = FirebaseAuth.getInstance().uid
    override suspend fun getUser(userId: String): UserModel {
        val userSnapshot = userCollection.document(userId).get().await()
        return userSnapshot.toObject<UserModel>()!!
    }

    override suspend fun userAFriend(otherUserId: String): RequestModel? {
        val friendsSnapshot =
            requestsColl.whereEqualTo("sendBy", currentUserId).whereEqualTo("sentTo", otherUserId)
                .get().await()
        if (friendsSnapshot.documents.isEmpty()) return null

        val document = friendsSnapshot.documents.first().toObject<RequestModel>()
         return  document!!


    }

    override suspend fun activeFriendRequest(otherUserId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun sendFriendRequest(otherUserId: String): Boolean {
        val request = hashMapOf(
            "sendBy" to currentUserId,
            "sentTo" to otherUserId,
            "status" to "requestSent"
        )


        val result = requestsColl.add(request).await()
        return result.get().await().exists()
    }
}