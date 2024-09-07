package com.example.zombiechat.friends.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.friends.data.repo.FriendsRepo
import kotlinx.coroutines.runBlocking

class FriendsVM(private val repo: FriendsRepo) : ViewModel() {

    private val liveFriends: MutableLiveData<List<UserModel>> = MutableLiveData()


    fun getAllFriends(): MutableLiveData<List<UserModel>> {
        return liveFriends
    }

    suspend fun fetchAllFriends() {
        runBlocking {
            val friends = repo.getAllFriends()
            liveFriends.postValue(friends)
        }
    }

}