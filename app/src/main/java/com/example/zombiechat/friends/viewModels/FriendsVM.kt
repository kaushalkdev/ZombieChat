package com.example.zombiechat.friends.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.friends.data.repo.FriendsRepo
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FriendsVM(private val repo: FriendsRepo) : ViewModel() {

    private val liveFriends: MutableLiveData<List<UserModel>> = MutableLiveData()

    private val liveRequests: MutableLiveData<List<UserModel>> = MutableLiveData()


    fun getAllFriends(): MutableLiveData<List<UserModel>> {
        return liveFriends
    }

    fun getAllRequests(): MutableLiveData<List<UserModel>> {
        return liveRequests
    }


    suspend fun fetchAllFriends() {
        runBlocking {
            val friends = repo.getAllFriends()
            liveFriends.postValue(friends)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun fetchAllRequests() {

        // TODO read more about this global scope.

        GlobalScope.launch {
            val requests = repo.getAllRequests()
            liveRequests.postValue(requests)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun acceptRequest(userId: String) {
        GlobalScope.launch {
            repo.acceptRequest(userId)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun rejectRequest(userId: String) {
        GlobalScope.launch {
            repo.rejectRequest(userId)
        }
    }

}