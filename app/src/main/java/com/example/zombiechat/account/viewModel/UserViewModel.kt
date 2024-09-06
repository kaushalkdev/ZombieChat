package com.example.zombiechat.account.viewModel


import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.account.data.repo.AccountRepo
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class UserViewModel(private val repo: AccountRepo) : ViewModel() {

    val userLiveData: MutableLiveData<UserModel> = MutableLiveData<UserModel>()


    fun updateName(name: String) {
        val user = userLiveData.value?.apply { this.name = name }
        repo.updateUser(user!!)
        userLiveData.postValue(user)
    }

    suspend fun updateImage(image: String) {

        runBlocking {
            launch {


                val uploadedUrl = repo.uploadUserImage(Uri.parse(image))
                val user = userLiveData.value?.apply { this.image = uploadedUrl }
                repo.updateUser(user!!)
                userLiveData.postValue(user)
            }
        }

    }

    fun updateGender(gender: String) {
        val user = userLiveData.value?.apply { this.gender = gender }
        repo.updateUser(user!!)
        userLiveData.postValue(user)
    }

    fun updateStatus(status: String) {
        val user = userLiveData.value?.apply { this.status = status }
        repo.updateUser(user!!)
        userLiveData.postValue(user)
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