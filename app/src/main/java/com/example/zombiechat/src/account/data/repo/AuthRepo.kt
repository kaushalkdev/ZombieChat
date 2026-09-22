package com.example.zombiechat.src.account.data.repo

import com.example.zombiechat.src.account.data.models.UserModel
import com.example.zombiechat.util.consts.DbCollection
import com.example.zombiechat.util.service.AuthService
import com.google.firebase.auth.AuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface AuthRepo {

    suspend fun signIn(authCredential: AuthCredential): Boolean

    suspend fun createNewUser(user: UserModel): Boolean

    fun isLoggedIn(): Boolean

    suspend fun getCurrentUser(): UserModel?
}

class AuthRepoImpl(private val authService: AuthService) : AuthRepo {

    private val userCollection =
        FirebaseFirestore.getInstance().collection(DbCollection.userCollection)


    override suspend fun signIn(authCredential: AuthCredential): Boolean {
        try {
            val authResult = authService.signInWith(authCredential).await()
            val firebaseUser = authResult.user ?: return false

            val userDoc = userCollection.document(firebaseUser.uid).get().await()
            if (!userDoc.exists()) {
                val image = firebaseUser.photoUrl?.toString() ?: ""
                val name = firebaseUser.displayName ?: ""
                val gender = "male"
                val status = "Hey there i am using Zombie chat"
                val userid = firebaseUser.uid
                val user = UserModel(image, name, gender, status, userid)
                return createNewUser(user)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    override suspend fun createNewUser(user: UserModel): Boolean {
        try {
            userCollection.document(user.userid!!).set(user).await()
            return true

        } catch (e: Exception) {
            return false
        }

    }


    override fun isLoggedIn(): Boolean {
        return authService.getCurrentUser() != null
    }

    override suspend fun getCurrentUser(): UserModel? {
        val currentUser = authService.getCurrentUser()
        val userSnapshot = currentUser?.let { userCollection.document(it.uid).get().await() }
        return userSnapshot?.toObject(UserModel::class.java)
    }
}