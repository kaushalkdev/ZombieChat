package com.example.zombiechat.account.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.account.data.repo.ProfileRepo
import kotlinx.coroutines.runBlocking

class ProfileVM(private val repo: ProfileRepo) : ViewModel() {


    private var userModel = MutableLiveData<UserModel>()


    fun getUserModel(): MutableLiveData<UserModel> {
        return userModel
    }

    suspend fun getUser() {
        val model = repo.getUser(null)
        userModel.postValue(model)
    }

//        fun updateName(name: String) {
//            repo.updateName(name)
//        }
//
//        fun updateImage(image: String) {
//            repo.updateImage(image)
//        }
}