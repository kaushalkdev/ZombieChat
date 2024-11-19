package com.example.zombiechat.src.account.data.repo

import com.example.zombiechat.src.account.data.models.UserModel
import com.example.zombiechat.util.consts.DbCollection
import com.example.zombiechat.util.service.AuthService
import com.google.firebase.auth.AuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.koin.java.KoinJavaComponent.inject
import javax.inject.Singleton

interface AuthRepo {

    suspend fun signIn(authCredential: AuthCredential): Boolean

    suspend fun createNewUser(user: UserModel): Boolean

    fun isLoggedIn(): Boolean

    suspend fun getCurrentUser(): UserModel?
}

@Singleton
class AuthRepoImpl : AuthRepo {
    private val authService: AuthService by inject(AuthService::class.java)

    private val userCollection =
        FirebaseFirestore.getInstance().collection(DbCollection.userCollection)


    override suspend fun signIn(authCredential: AuthCredential): Boolean {
        try {
            val authResult = authService.signInWith(authCredential).await()
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


    override fun isLoggedIn(): Boolean {
        return authService.getCurrentUser() != null
    }

    override suspend fun getCurrentUser(): UserModel? {
        val currentUser = authService.getCurrentUser()
        val userSnapshot = currentUser?.let { userCollection.document(it.uid).get().await() }
        return userSnapshot?.toObject(UserModel::class.java)
    }
}