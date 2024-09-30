package com.example.zombiechat.chat.data.repo;

import com.example.zombiechat.chat.data.models.LastChatModel;
import com.example.zombiechat.chat.data.models.SingleChatModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Flow;
import java.util.concurrent.Future;

public interface ChatRepo {

    CompletableFuture<List<LastChatModel>> getLastChats() throws ExecutionException, InterruptedException;

    Future<String> getChatId(String otherUserId);


}
