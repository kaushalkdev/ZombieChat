package com.example.zombiechat.account.data.repo

import android.net.Uri
import android.os.Build
import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.constants.api.collections.Collections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

class AccountRepo {

    private var userCollection: CollectionReference =
        FirebaseFirestore.getInstance().collection(Collections.userCollection)

    private var currentAuth: String? = FirebaseAuth.getInstance().uid

    private var currentUser: DocumentReference? = null

    private var storageRef = FirebaseStorage.getInstance().reference


    suspend fun getCurrentUser(): UserModel? {


        if (currentAuth != null) {
            currentUser = userCollection.document(currentAuth!!)
            val model = UserModel

            val snapShot = currentUser!!.get().await()



            model.image = snapShot.getString("image");
            model.name = snapShot.getString("name");
            model.status = snapShot.getString("status");
            model.userid = snapShot.getString("userid");
            model.gender = snapShot.getString("gender");

            return model

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

