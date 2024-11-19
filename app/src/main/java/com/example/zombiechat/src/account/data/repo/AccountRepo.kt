package com.example.zombiechat.src.account.data.repo

import android.net.Uri
import com.example.zombiechat.src.account.data.models.UserModel
import com.example.zombiechat.util.consts.DbCollection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AccountRepo {

    private var userCollection: CollectionReference =
        FirebaseFirestore.getInstance().collection(DbCollection.userCollection)

    private var currentAuth: String? = FirebaseAuth.getInstance().uid

    private var currentUser: DocumentReference? = null

    private var storageRef = FirebaseStorage.getInstance().reference


    suspend fun getCurrentUser(): UserModel? {


        if (currentAuth != null) {
            currentUser = userCollection.document(currentAuth!!)


            val snapShot = currentUser!!.get().await()

            return snapShot.toObject<UserModel>()

        }
        return null

    }


    suspend fun updateUser(model: UserModel): Boolean {

        if (currentUser != null) {
            currentUser!!.set(model).await();
            return true
        } else {
            return false
        }


    }


    suspend fun uploadUserImage(imagePath: Uri): String? {

        try {
            val future =
                storageRef.child("profileImages/${currentAuth}.jpg").putFile(imagePath).await()
            return future.storage.downloadUrl.await().toString()
        } catch (e: Exception) {
            return null;
        }


    }
}

