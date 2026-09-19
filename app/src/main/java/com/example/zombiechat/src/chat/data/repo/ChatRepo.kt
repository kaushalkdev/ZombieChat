package com.example.zombiechat.src.chat.data.repo

import com.example.zombiechat.src.chat.data.models.LastChatModel
import com.example.zombiechat.src.chat.data.models.SingleChatModel
import io.reactivex.rxjava3.core.Observable


interface ChatRepo {

    fun lastChats(): Observable<List<LastChatModel>>

    fun getActiveChats(chatRoomId: String?): Observable<List<SingleChatModel>>

    fun sendMessage(message: String?, sendTo: String?, chatRoomId: String?)
}
