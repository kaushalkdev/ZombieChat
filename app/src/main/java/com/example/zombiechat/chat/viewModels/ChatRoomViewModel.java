package com.example.zombiechat.chat.viewModels;

import androidx.lifecycle.ViewModel;

import com.example.zombiechat.chat.data.repo.ChatRepo;

import java.util.concurrent.Future;

public class ChatRoomViewModel extends ViewModel {
    private ChatRepo repo;

    public ChatRoomViewModel(ChatRepo repo) {
        this.repo = repo;
    }

    public Future<String> getChatsFor(String otherUserId) {
        return repo.getChatId(otherUserId);
    }
}
