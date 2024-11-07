package com.example.zombiechat.account.viewModel

import androidx.lifecycle.ViewModel
import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.account.data.repo.AuthRepo

class AuthVM(val repo: AuthRepo) : ViewModel() {
    var currentUser: UserModel? = null


    fun signIn() {
    }

    fun signOut() {
    }

    fun createAccount(user: UserModel?) {
    }
}
