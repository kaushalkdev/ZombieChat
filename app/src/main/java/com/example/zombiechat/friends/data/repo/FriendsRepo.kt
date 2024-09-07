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
                    if (friend.toObject(UserModel::class.java).userid == user.toObject(UserModel::class.java).userid) {
                        friends.add(user.toObject(UserModel::class.java))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return friends
    }


}