package com.example.zombiechat.account.viewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.account.data.repo.AccountRepo
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class UserViewModel(private val repo: AccountRepo) : ViewModel() {

    val userLiveData: MutableLiveData<UserModel> = MutableLiveData<UserModel>()


    fun updateName(name: String) {
        userLiveData.value?.name = name
    }

    fun updateImage(image: String) {
        userLiveData.value?.image = image
    }

    fun updateGender(gender: String) {
        userLiveData.value?.gender = gender
    }

    fun updateStatus(status: String) {
        userLiveData.value?.status = status
    }

    fun updateUserId(userId: String) {
        userLiveData.value?.userid = userId
    }

    suspend fun getUser() {
        runBlocking {
            launch {

                try {
                    val user = repo.getCurrentUser() ?: throw Exception("Error getting user")

                    userLiveData.value = user
                } catch (e: Exception) {
                    throw Exception("Error getting user ${e.message}")
                }

            }
        }


    }
}