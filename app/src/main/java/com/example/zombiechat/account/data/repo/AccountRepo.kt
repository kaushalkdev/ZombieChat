package com.example.zombiechat.account.data.repo

import com.example.zombiechat.account.data.models.UserModel

interface AccountRepo {

    fun updateUser(model: UserModel): Boolean
    fun uploadUserImage(imagePath: String): String
}

class AccountRepoImpl : AccountRepo {
    override fun updateUser(model: UserModel): Boolean {
        TODO("Not yet implemented")
    }


    override fun uploadUserImage(imagePath: String): String {
        TODO("Not yet implemented")
    }
}

