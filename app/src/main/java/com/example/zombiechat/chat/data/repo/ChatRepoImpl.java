package com.example.zombiechat.chat.data.repo;

import com.example.zombiechat.chat.data.models.LastChatModel;
import com.example.zombiechat.constants.api.collections.Collections;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class ChatRepoImpl implements ChatRepo {

    final String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    final CollectionReference chatBoxesCollection = FirebaseFirestore.getInstance().collection(Collections.chatBox);
    final CollectionReference chatCollections = FirebaseFirestore.getInstance().collection(Collections.chatCollection);

    @Override
    public Future<List<LastChatModel>> getLastChats() {
        List<LastChatModel> lastChats = new ArrayList<>();
        List<DocumentSnapshot> chatBoxes = chatBoxesCollection.get().getResult().getDocuments();
        for (DocumentSnapshot chatBox : chatBoxes) {
            String chatId = chatBox.getId();
            List<DocumentSnapshot> chats = chatCollections.document(chatId).collection(Collections.chatCollection).get().getResult().getDocuments();
// TODO add the logic to get the last chat

        }

        return CompletableFuture.supplyAsync(() -> lastChats);

    }
}
