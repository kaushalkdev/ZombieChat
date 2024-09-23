package com.example.zombiechat.chat.data.repo;

import android.util.Log;

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

public class FirebaseChatRepo implements ChatRepo {

    final String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    final CollectionReference chatRoomCollectiom = FirebaseFirestore.getInstance().collection(Collections.chatRoomIds);
    final CollectionReference chatCollections = FirebaseFirestore.getInstance().collection(Collections.chatCollection);
    final CollectionReference friendsCollection = FirebaseFirestore.getInstance().collection(Collections.friendsCollection);

    @Override
    public Future<List<LastChatModel>> getLastChats() {

//        chatRoomCollectiom.get().addOnSuccessListener(queryDocumentSnapshots -> {
//            List<DocumentSnapshot> chatBoxes = queryDocumentSnapshots.getDocuments();
//            Log.d("ChatRepoImpl", "getLastChats: " + chatBoxes.size());
//
//            for (DocumentSnapshot chatBox : chatBoxes) {
//                String chatId = chatBox.getId();
//                List<DocumentSnapshot> chats = chatCollections.document(chatId).collection(Collections.chatCollection).get().getResult().getDocuments();
//                // TODO add the logic to get the last chat
//
//            }
//        });
//        Log.d("ChatRepoImpl", "getLastChats: " + chatBoxes.size());

//        for (DocumentSnapshot chatBox : chatBoxes) {
//            String chatId = chatBox.getId();
//            List<DocumentSnapshot> chats = chatCollections.document(chatId).collection(Collections.chatCollection).get().getResult().getDocuments();
//            // TODO add the logic to get the last chat
//
//        }
        CompletableFuture<List<LastChatModel>> lastChats = new CompletableFuture<>();
        List<DocumentSnapshot> chatRoomIds = chatRoomCollectiom.document(currentUser).collection(Collections.chatRoomIds).get().getResult().getDocuments();
        chatCollections.whereIn("chatId", chatRoomIds).get()


                .addOnFailureListener(e -> Log.d("FirebaseChatRepo", "getLastChats: " + e.getMessage())).addOnSuccessListener(queryDocumentSnapshots -> {
                    List<LastChatModel> lastChatModels = new ArrayList<>();
                    for (DocumentSnapshot chat : queryDocumentSnapshots.getDocuments()) {
                        String chatId = chat.getId();
                        List<DocumentSnapshot> chats = chatCollections.document(chatId).collection(Collections.chatCollection).get().getResult().getDocuments();
                        String msg = chats.get(chats.size() - 1).getString("msg");
                        String userImage = chats.get(chats.size() - 1).getString("userImage");
                        String userName = chats.get(chats.size() - 1).getString("userName");
                        String msgTime = chats.get(chats.size() - 1).getString("msgTime");
                        lastChatModels.add(new LastChatModel(chatId, msg, userImage, userName, msgTime));
                    }
                    lastChats.complete(lastChatModels);
                });


        return lastChats;


    }

    @Override
    public Future<String> getChatId(String otherUserId) {
        return CompletableFuture.supplyAsync(() -> friendsCollection.document(currentUser).collection(Collections.chatIdCollection).document(otherUserId).get().getResult().getString("chatId"));

    }
}
