package com.example.zombiechat.friends.data.repo

import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.constants.api.collections.Collections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FriendsRepo {
    private var userCollection: CollectionReference =
        FirebaseFirestore.getInstance().collection(Collections.userCollection)

    private var currentUserId: String? = FirebaseAuth.getInstance().uid

    suspend fun getAllUsers(): List<UserModel> {
        val users: MutableList<UserModel> = mutableListOf()

        try {
            val usersSnapshot = userCollection.get().await()
            for (user in usersSnapshot) {

                if (user.getString("userid") != currentUserId) {


                    users.add(user.toObject(UserModel::class.java))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return users
    }


}