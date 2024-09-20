package com.example.zombiechat.account.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zombiechat.account.data.models.RequestModel
import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.account.data.repo.ProfileRepo

import kotlinx.coroutines.runBlocking

class ProfileVM(private val repo: ProfileRepo) : ViewModel() {


    private var userModel = MutableLiveData<UserModel>()

    var friendStatus = MutableLiveData<RequestModel>()

    var isRequestSent = MutableLiveData<Boolean>()


    fun getUserModel(): MutableLiveData<UserModel> {
        return userModel
    }

    suspend fun getUser(userId: String) {
        runBlocking {
            val model = repo.getUser(userId)
            userModel.postValue(model)

        }
    }

    suspend fun checkIfFriend(userId: String) {
        runBlocking {
            val requestModel = repo.userAFriend(userId)

            friendStatus.postValue(requestModel)
        }
    }


    suspend fun sendFriendRequest(userId: String) {
        runBlocking {
            var result = repo.sendFriendRequest(userId)
            isRequestSent.postValue(result)
        }
    }

//        fun updateName(name: String) {
//            repo.updateName(name)
//        }
//
//        fun updateImage(image: String) {
//            repo.updateImage(image)
//        }
}