package com.example.zombiechat.account.data.repo

import com.example.zombiechat.account.data.models.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth

interface AuthRepo {

    fun signIn(): Boolean

    fun createNewUser(user: UserModel): Boolean

    fun logout(): Boolean

    fun isLoggedIn(): Boolean
}


class AuthRepoImpl : AuthRepo {

    val firebaseAuth = FirebaseAuth.getInstance()


    override fun signIn(): Boolean {
        return true
    }

    override fun createNewUser(user: UserModel): Boolean {
        TODO("Not yet implemented")
    }


    override fun logout(): Boolean {
        return true
    }

    override fun isLoggedIn(): Boolean {
        return true
    }
}