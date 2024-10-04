package com.example.zombiechat.chat.data.repo;

import com.example.zombiechat.chat.data.models.LastChatModel;
import com.example.zombiechat.chat.data.models.SingleChatModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Flow;
import java.util.concurrent.Future;

import io.reactivex.rxjava3.core.Observable;

public interface ChatRepo {

    Observable<List<LastChatModel>> getLastChats() throws ExecutionException, InterruptedException;

    Observable<List<SingleChatModel>> getActiveChats(String chatRoomId);
    void sendMessage(String message, String sendTo, String chatRoomId);


}
