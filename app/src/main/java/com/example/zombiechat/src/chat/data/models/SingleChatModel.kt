package com.example.zombiechat.src.chat.data.models

import com.google.firebase.Timestamp

class SingleChatModel {
    @JvmField var message: String? = null
    @JvmField var sentTo: String? = null
    @JvmField var sendBy: String? = null
    @JvmField var time: Timestamp? = null
}
