package com.example.zombiechat.src.chat.data.repo

import com.example.zombiechat.src.chat.data.models.LastChatModel
import com.example.zombiechat.src.chat.data.models.SingleChatModel
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.Flow


interface ChatRepo {

    fun lastChats(): Flow<List<LastChatModel?>?>?

    fun getActiveChats(chatRoomId: String?): Flow<List<SingleChatModel?>?>?

    fun sendMessage(message: String?, sendTo: String?, chatRoomId: String?)
}
