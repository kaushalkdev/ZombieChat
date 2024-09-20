package com.example.zombiechat.chat.views.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zombiechat.R;
import com.example.zombiechat.chat.data.models.LastChatModel;
import com.example.zombiechat.chat.data.repo.ChatRepoImpl;
import com.example.zombiechat.chat.viewModels.ChatListViewModel;
import com.example.zombiechat.chat.views.adapters.LastChatsAdapter;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ChatFragment extends Fragment {

    public static final String TAG = "ChatFragment";
    private RecyclerView mrecyclerview;
    private LastChatsAdapter adapter;
    List<LastChatModel> chatsList = new ArrayList<>();
    ListenerRegistration registration;
    private final ChatListViewModel chatListViewModel = new ChatListViewModel(new ChatRepoImpl());

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
            adapter = new LastChatsAdapter(getContext(), chatsList);
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
    public void onStart() {
        super.onStart();
//        mAuth = FirebaseAuth.getInstance();

//        registration = db.collection("chatids")
//                .whereEqualTo(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getUid())
//                .addSnapshotListener(requireActivity(), new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                        if (queryDocumentSnapshots != null) {
//                            chatid.clear();
//                            for (final QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                                Log.d(TAG, "chatid: " + documentSnapshot.get("chatid"));
//
//                                chatid.add(documentSnapshot.get("chatid").toString());
//                                db.collection("chatbox")
//                                        .document(documentSnapshot.get("chatid").toString())
//                                        .collection("chats")
//                                        .orderBy("time", Query.Direction.DESCENDING)
//                                        .limit(1)
//                                        .addSnapshotListener(requireActivity(), new EventListener<QuerySnapshot>() {
//                                            @Override
//                                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                                                if (queryDocumentSnapshots != null) {
//                                                    for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {
//
//                                                        Chat chats = new Chat();
//
//                                                        chatsList.clear();
//                                                        chats.setMessage(documentSnapshot1.get("message").toString());
//                                                        chats.setSentTO(documentSnapshot1.get("sentTO").toString());
//                                                        chats.setSendBy(documentSnapshot1.get("sendBy").toString());
//
//                                                        chatsList.add(chats);
//
//
//                                                    }
//                                                }
//
//                                                adapter = new ChatAdapter(getActivity(), chatid);
//                                                adapter.notifyDataSetChanged();
//                                                mrecyclerview.setAdapter(adapter);
//                                            }
//                                        });
//
//                            }
//                        }
//
//
//                    }
//                });

    }

    @Override
    public void onStop() {
        super.onStop();
//        registration.remove();
    }
}
