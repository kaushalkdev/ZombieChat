package com.example.zombiechat.chat.viewModels;

import androidx.lifecycle.ViewModel;

import com.example.zombiechat.chat.data.repo.ChatRepo;


public class ChatRoomViewModel extends ViewModel {
    private ChatRepo repo;

    public ChatRoomViewModel(ChatRepo repo) {
        this.repo = repo;
    }


    public void sendMessage(String message, String sendTo, String chatRoomId) {
        repo.sendMessage(message, sendTo, chatRoomId);
    }
}
