package com.example.zombiechat.src.account.viewModel


import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zombiechat.src.account.data.models.UserModel
import com.example.zombiechat.src.account.data.repo.AccountRepo
import kotlinx.coroutines.launch


class UserViewModel(private val repo: AccountRepo) : ViewModel() {

    val userLiveData: MutableLiveData<UserModel?> = MutableLiveData<UserModel?>()


    fun updateName(name: String) {
        val user = userLiveData.value?.apply { this.name = name }

        viewModelScope.launch {
            repo.updateUser(user!!)
            userLiveData.postValue(user)
        }


    }

    suspend fun updateImage(image: String) {

        viewModelScope.launch {
            val uploadedUrl = repo.uploadUserImage(Uri.parse(image))
            val user = userLiveData.value?.apply { this.image = uploadedUrl }
            repo.updateUser(user!!)
            userLiveData.postValue(user)
        }


    }

    fun updateGender(gender: String) {
        val user = userLiveData.value?.apply { this.gender = gender }
        viewModelScope.launch {
            repo.updateUser(user!!)
            userLiveData.postValue(user)
        }

    }

    fun updateStatus(status: String) {
        val user = userLiveData.value?.apply { this.status = status }
        viewModelScope.launch {
            repo.updateUser(user!!)
            userLiveData.postValue(user)
        }


    }


    suspend fun getUser() {
        viewModelScope.launch {
            try {
                val user = repo.getCurrentUser() ?: throw Exception("Error getting user")

                userLiveData.value = user
            } catch (e: Exception) {
                throw Exception("Error getting user ${e.message}")
            }

        }


    }
}