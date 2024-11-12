package com.example.zombiechat.service

import com.example.zombiechat.account.data.models.UserModel
import com.google.firebase.auth.FirebaseAuth


object AuthService {

    val firebaseAuth = FirebaseAuth.getInstance()
    suspend fun signIn() {}
    suspend fun signOut() {}
    suspend fun getCurrentUser(): UserModel? {
        firebaseAuth.currentUser
    }
}