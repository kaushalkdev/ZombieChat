package com.example.zombiechat.friends.data.repo

import com.example.zombiechat.constants.api.collections.Collections
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FriendsRepo {
    private var userCollection: CollectionReference =
        FirebaseFirestore.getInstance().collection(Collections.userCollection)

    suspend fun getAllUsers() {

    }
}