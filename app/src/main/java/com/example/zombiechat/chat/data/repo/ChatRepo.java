package com.example.zombiechat.chat.data.repo;

import com.example.zombiechat.chat.data.models.LastChatModel;

import java.util.List;
import java.util.concurrent.Future;

public interface ChatRepo {

   Future<List<LastChatModel>> getLastChats();
}
