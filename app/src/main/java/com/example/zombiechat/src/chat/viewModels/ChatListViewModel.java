package com.example.zombiechat.src.chat.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zombiechat.src.chat.data.models.LastChatModel;
import com.example.zombiechat.src.chat.data.repo.ChatRepo;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
        try {
            // TODO dispose the subscriber when viewmodel is destroyed
            Disposable subscribe = repo.getLastChats().observeOn(Schedulers.io()).subscribe(lastChats::postValue);

        } catch (Exception e) {
            Log.d("ChatRepo", "fetchLastChats: " + e);
        }

    }
}
