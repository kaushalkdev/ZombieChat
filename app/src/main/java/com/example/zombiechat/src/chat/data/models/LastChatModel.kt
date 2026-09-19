package com.example.zombiechat.src.chat.data.models

import com.google.firebase.Timestamp

class LastChatModel(
    @JvmField val chatId: String,
    @JvmField val msg: String,
    @JvmField val userImage: String,
    @JvmField val userName: String,
    @JvmField val userId: String,
    @JvmField val msgTime: Timestamp
)
