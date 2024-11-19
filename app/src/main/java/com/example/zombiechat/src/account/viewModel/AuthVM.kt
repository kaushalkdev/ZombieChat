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

    suspend fun signInWith(authCredential: AuthCredential) {
        viewModelScope.launch {
            repo.signIn(authCredential)
            currentUser.postValue(repo.getCurrentUser())
        }
    }



    fun isLoggedIn(): Boolean {
        return repo.isLoggedIn()
    }


}
