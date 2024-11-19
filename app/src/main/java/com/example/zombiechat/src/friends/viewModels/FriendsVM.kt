package com.example.zombiechat.src.friends.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zombiechat.src.account.data.models.UserModel
import com.example.zombiechat.friends.data.models.NewFriendsModel
import com.example.zombiechat.friends.data.repo.FriendsRepo
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class FriendsVM(private val repo: FriendsRepo) : ViewModel() {

    private val liveFriends: MutableLiveData<List<NewFriendsModel>> = MutableLiveData()

    private val liveRequests: MutableLiveData<List<UserModel>> = MutableLiveData()


    fun getAllFriends(): MutableLiveData<List<NewFriendsModel>> {
        return liveFriends
    }

    fun getAllRequests(): MutableLiveData<List<UserModel>> {
        return liveRequests
    }


    suspend fun fetchAllFriends() {
        viewModelScope.launch {
            val friends = repo.getAllFriends()
            liveFriends.postValue(friends)
        }

    }


    fun fetchAllRequests() {
        viewModelScope.launch {
            val requests = repo.getAllRequests()
            liveRequests.postValue(requests)
        }
    }

    fun acceptRequest(userId: String) {
        viewModelScope.launch { repo.acceptRequest(userId) }

    }


    fun rejectRequest(userId: String) {
        viewModelScope.launch {
            repo.rejectRequest(userId)
        }
    }


}