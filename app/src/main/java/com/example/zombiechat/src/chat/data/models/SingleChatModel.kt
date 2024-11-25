package com.example.zombiechat.src.chat.data.models

import com.google.firebase.Timestamp

class SingleChatModel {
    var message: String? = null
    var sentTo: String? = null
    var sendBy: String? = null
    var time: Timestamp? = null
}
