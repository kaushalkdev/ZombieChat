package com.example.zombiechat.util.service

import com.google.firebase.crashlytics.FirebaseCrashlytics

abstract class BaseException(message: String?, throwable: Throwable) {

    init {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}


class ChatException(message: String?, throwable: Throwable) : BaseException(message, throwable) {}