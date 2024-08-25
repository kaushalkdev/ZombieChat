package com.example.zombiechat.account.data.repo

import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.constants.api.collections.Collections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

interface AccountRepo {

    fun updateUser(model: UserModel): Boolean
    fun uploadUserImage(imagePath: String): String
}

class AccountRepoImpl : AccountRepo {

    private var userCollection: CollectionReference =
        FirebaseFirestore.getInstance().collection(Collections.userCollection)

    private var currentAuth: String? = FirebaseAuth.getInstance().uid

    private var currentUser: DocumentReference? = null

    private var storage: FirebaseStorage = FirebaseStorage.getInstance()


    fun getCurrentUser() {
        if (currentAuth != null) {
            currentUser = userCollection.document(currentAuth!!)
        }
    }


    override fun updateUser(model: UserModel): Boolean {

        if (currentUser != null) {
            currentUser!!.set(model)
            return true
        } else {
            return false
        }


    }


    override fun uploadUserImage(imagePath: String): String {
        TODO("Not yet implemented")
    }
}

