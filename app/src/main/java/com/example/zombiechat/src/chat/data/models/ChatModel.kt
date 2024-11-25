package com.example.zombiechat.src.chat.data.models

class ChatModel {
    //sequencing of this is also important
    var message: String? = null
    var sendBy: String? = null
    var sentTO: String? = null
    var time: String? = null

    constructor(message: String?, sendBy: String?, sentTO: String?, time: String?) {
        this.message = message
        this.sendBy = sendBy
        this.sentTO = sentTO
        this.time = time
    }

    constructor()
}
