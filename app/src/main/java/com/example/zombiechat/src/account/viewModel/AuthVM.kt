package com.example.zombiechat.account.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zombiechat.src.account.data.models.UserModel
import com.example.zombiechat.src.account.data.repo.AuthRepo
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.launch

class AuthVM(val repo: AuthRepo) : ViewModel() {

    var currentUser: MutableLiveData<UserModel?> = MutableLiveData()
    val authError: MutableLiveData<String?> = MutableLiveData()

    fun signInWith(authCredential: AuthCredential) {
        viewModelScope.launch {
            try {
                val success = repo.signIn(authCredential)
                if (success) {
                    val user = repo.getCurrentUser()
                    currentUser.postValue(user)
                } else {
                    authError.postValue("Sign in failed. Please try again.")
                }
            } catch (e: Exception) {
                authError.postValue(e.message ?: "An unexpected error occurred.")
            }
        }
    }

    fun isLoggedIn(): Boolean {
        return repo.isLoggedIn()
    }
}
