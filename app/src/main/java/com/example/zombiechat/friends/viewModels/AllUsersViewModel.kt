package com.example.zombiechat.friends.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.friends.data.repo.AllUsersRepo
import kotlinx.coroutines.runBlocking

class AllUsersViewModel(private val repo: AllUsersRepo) : ViewModel() {

    private val usersLiveData: MutableLiveData<List<UserModel>> = MutableLiveData()


    fun getLiveUsers(): MutableLiveData<List<UserModel>> {
        return usersLiveData
    }


    suspend fun getAllUsers() {
        runBlocking {
            val users = repo.getAllUsers()
            usersLiveData.postValue(users)
        }
    }

}