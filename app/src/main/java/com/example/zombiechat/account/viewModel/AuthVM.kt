package com.example.zombiechat.account.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.account.data.repo.AuthRepo
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.launch

class AuthVM(val repo: AuthRepo) : ViewModel() {


    var currentUser: UserModel? = null

    suspend fun signInWith(authCredential: AuthCredential) {
        viewModelScope.launch {
            repo.signIn(authCredential)
            currentUser = repo.getCurrentUser()
        }
    }

    fun logOut() {
        repo.logout()
    }


    fun isLoggedIn(): Boolean {
        return repo.isLoggedIn()
    }


}
