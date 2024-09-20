package com.example.zombiechat.friends.data.repo

import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.constants.api.collections.Collections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FriendsRepo {
    private var userCollection: CollectionReference =
        FirebaseFirestore.getInstance().collection(Collections.userCollection)

    private val friendsColl: CollectionReference =
        FirebaseFirestore.getInstance().collection(Collections.friendsCollection)

    private val requestsColl: CollectionReference =
        FirebaseFirestore.getInstance().collection(Collections.requestsCollection)

    private var currentUserId: String? = FirebaseAuth.getInstance().uid


    suspend fun getAllFriends(): List<UserModel> {

        val friends: MutableList<UserModel> = mutableListOf()

        try {
            val friendsSnapshot =
                friendsColl.document(currentUserId!!).collection(Collections.friendsCollection)
                    .get().await()

            val allUsersSnapshot = userCollection.get().await()

            for (friend in friendsSnapshot) {
                for (user in allUsersSnapshot) {
                    if (friend.getString("userId") == user.toObject(UserModel::class.java).userid) {
                        friends.add(user.toObject(UserModel::class.java))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return friends
    }

    suspend fun getAllRequests(): List<UserModel> {
        val requests: MutableList<UserModel> = mutableListOf()

        try {
            val requestsSnapshot = requestsColl.get().await()

            val allUsersSnapshot = userCollection.get().await()

            for (request in requestsSnapshot) {
                for (user in allUsersSnapshot) {
//   TODO               using sendTo id to find whom we have requested for now ; To be changes later.
                    if (request.getString("sentTo") == user.toObject(UserModel::class.java).userid) {
                        requests.add(user.toObject(UserModel::class.java))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return requests
    }


}