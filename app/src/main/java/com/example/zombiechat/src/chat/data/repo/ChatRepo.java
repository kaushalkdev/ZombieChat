package com.example.zombiechat.src.chat.data.repo;


import com.example.zombiechat.src.chat.data.models.LastChatModel;
import com.example.zombiechat.src.chat.data.models.SingleChatModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.core.Observable;

public interface ChatRepo {

    Observable<List<LastChatModel>> getLastChats() throws ExecutionException, InterruptedException;

    Observable<List<SingleChatModel>> getActiveChats(String chatRoomId);

    void sendMessage(String message, String sendTo, String chatRoomId);


}
