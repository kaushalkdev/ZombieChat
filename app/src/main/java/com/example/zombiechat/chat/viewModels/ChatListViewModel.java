package com.example.zombiechat.chat.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zombiechat.chat.data.models.LastChatModel;
import com.example.zombiechat.chat.data.repo.ChatRepo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatListViewModel extends ViewModel {
    final ChatRepo repo;

    public ChatListViewModel(ChatRepo repo) {
        this.repo = repo;
    }

    private final MutableLiveData<List<LastChatModel>> lastChats = new MutableLiveData<>();

    public MutableLiveData<List<LastChatModel>> getLastChats() {
        return lastChats;
    }

    public void fetchLastChats() throws ExecutionException, InterruptedException {

        List<LastChatModel> fetchedLastChats = repo.getLastChats().get();
        lastChats.postValue(fetchedLastChats);
    }
}
