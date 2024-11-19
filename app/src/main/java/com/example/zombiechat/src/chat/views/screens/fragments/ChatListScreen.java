package com.example.zombiechat.src.chat.views.screens.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zombiechat.R;
import com.example.zombiechat.src.chat.data.models.LastChatModel;
import com.example.zombiechat.src.chat.data.repo.FirebaseChatRepo;
import com.example.zombiechat.src.chat.viewModels.ChatListViewModel;
import com.example.zombiechat.src.chat.views.adapters.ChatsListAdapter;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ChatListScreen extends Fragment {

    public static final String TAG = "ChatFragment";
    private RecyclerView mrecyclerview;
    private ChatsListAdapter adapter;
    List<LastChatModel> chatsList = new ArrayList<>();
    ListenerRegistration registration;
    private final ChatListViewModel chatListViewModel = new ChatListViewModel(new FirebaseChatRepo());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mrecyclerview = view.findViewById(R.id.chat_recycler_view);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));


        chatListViewModel.getLastChats().observe(getViewLifecycleOwner(), lastChats -> {
            chatsList.clear();
            chatsList.addAll(lastChats);
            adapter = new ChatsListAdapter(getContext(), chatsList);
            mrecyclerview.setAdapter(adapter);

        });


        try {
            chatListViewModel.fetchLastChats();
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(getContext(), "Error fetching chats", Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    @Override
    public void onStop() {
        super.onStop();
//        registration.remove();
    }
}
