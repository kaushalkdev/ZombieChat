package com.example.zombiechat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;


public class ChatFragment extends Fragment {

    public static final String TAG = "ChatFragment";
    private RecyclerView mrecyclerview;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ChatAdapter adapter;
    List<String> chatid = new ArrayList<>();
    List<Chat> chatsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mrecyclerview = view.findViewById(R.id.chat_recycler_view);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

        db.collection("chatids")
                .whereEqualTo(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (final QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {


                            db.collection("chatbox")
                                    .document(documentSnapshot.get("chatid").toString())
                                    .collection("chats")
                                    .orderBy("time", Query.Direction.DESCENDING)
                                    .limit(1)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                            for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {

                                                Chat chats = new Chat();

                                                chatsList.clear();
                                                chats.setMessage(documentSnapshot1.get("message").toString());
                                                chats.setSentTO(documentSnapshot1.get("sentTO").toString());
                                                chats.setSendBy(documentSnapshot1.get("sendBy").toString());

                                                chatsList.add(chats);


                                            }
                                            adapter = new ChatAdapter(chatsList);
                                            adapter.notifyDataSetChanged();
                                            mrecyclerview.setAdapter(adapter);
                                        }
                                    });

                        }
                    }
                });

    }

    @Override
    public void onStop() {
        super.onStop();

        chatsList.clear();
    }
}
