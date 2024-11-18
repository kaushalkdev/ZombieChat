package com.example.zombiechat.service

import com.example.zombiechat.account.data.models.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AuthService {

    val firebaseAuth = FirebaseAuth.getInstance()


    suspend fun signInWith(authCredential: AuthCredential): Task<AuthResult> {
        return firebaseAuth.signInWithCredential(authCredential)
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}