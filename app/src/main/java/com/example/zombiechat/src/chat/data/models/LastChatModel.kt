package com.example.zombiechat.src.chat.data.models

import com.google.firebase.Timestamp

class LastChatModel(
    val chatId: String,
    val msg: String,
    val userImage: String,
    val userName: String,
    val userId: String,
    val msgTime: Timestamp
)
