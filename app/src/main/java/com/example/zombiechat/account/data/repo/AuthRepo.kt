package com.example.zombiechat.account.data.repo

interface AuthRepo {

    fun login(email: String, password: String): Boolean

    fun createNewUser(email: String, password: String): Boolean

    fun logout(): Boolean

    fun isLoggedIn(): Boolean
}


class AuthRepoImpl : AuthRepo {

    override fun login(email: String, password: String): Boolean {
        return true
    }

    override fun createNewUser(email: String, password: String): Boolean {
        return true
    }

    override fun logout(): Boolean {
        return true
    }

    override fun isLoggedIn(): Boolean {
        return true
    }
}