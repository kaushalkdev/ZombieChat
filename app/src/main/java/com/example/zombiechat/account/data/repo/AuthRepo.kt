package com.example.zombiechat.account.data.repo

import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.constants.api.collections.Collections
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

interface AuthRepo {

    suspend fun signIn(authCredential: AuthCredential): Boolean

    suspend fun createNewUser(user: UserModel): Boolean

    fun logout()

    fun isLoggedIn(): Boolean

    suspend fun getCurrentUser(): UserModel?
}

@Singleton
class AuthRepoImpl : AuthRepo {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userCollection =
        FirebaseFirestore.getInstance().collection(Collections.userCollection)


    override suspend fun signIn(authCredential: AuthCredential): Boolean {
        try {
            val authResult = firebaseAuth.signInWithCredential(authCredential).await()
            if (authResult.user == null) return false
            if (!authResult.additionalUserInfo!!.isNewUser) return true
            else {
                val image = authResult.user?.photoUrl
                val name = authResult.user?.displayName
                val gender = "male"
                val status = "Hey there i am using Zombie chat"
                val userid = authResult.user?.uid
                val user = UserModel(image.toString(), name.toString(), gender, status, userid)
                return createNewUser(user)

            }

        } catch (e: Exception) {
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


    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun getCurrentUser(): UserModel? {
        val currentUser = firebaseAuth.currentUser
        val userSnapshot = currentUser?.let { userCollection.document(it.uid).get().await() }
        return userSnapshot?.toObject(UserModel::class.java)
    }
}