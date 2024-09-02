package com.example.zombiechat.account.viewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zombiechat.account.data.models.UserModel


class UserViewModel : ViewModel() {
    private val userLiveData: MutableLiveData<UserModel> = MutableLiveData<UserModel>()


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


}