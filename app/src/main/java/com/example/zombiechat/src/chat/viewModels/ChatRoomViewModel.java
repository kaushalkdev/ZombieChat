package com.example.zombiechat.src.chat.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.zombiechat.src.chat.data.models.SingleChatModel;
import com.example.zombiechat.src.chat.data.repo.ChatRepo;

import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;


public class ChatRoomViewModel extends ViewModel {
    private final ChatRepo repo;
    private Disposable disposable;

    public ChatRoomViewModel(ChatRepo repo) {
        this.repo = repo;
    }


    private final MutableLiveData<List<SingleChatModel>> activeChats = new MutableLiveData<>();

    public MutableLiveData<List<SingleChatModel>> getActivesChats() {
        return activeChats;
    }


    public void sendMessage(String message, String sendTo, String chatRoomId) {
        repo.sendMessage(message, sendTo, chatRoomId);
    }


    public void fetchActiveChats(String chatRoomId) {
        disposable = repo.getActiveChats(chatRoomId).subscribe(activeChats::postValue);

    }

    public void dispose() {
        disposable.dispose();
    }


}
