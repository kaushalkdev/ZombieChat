package com.example.zombiechat.chat.data.repo;

import android.util.Log;

import com.example.zombiechat.account.data.models.UserModel;
import com.example.zombiechat.chat.data.models.ChatRoomModel;
import com.example.zombiechat.chat.data.models.LastChatModel;
import com.example.zombiechat.chat.data.models.SingleChatModel;
import com.example.zombiechat.constants.api.collections.Collections;
import com.example.zombiechat.constants.fields.Fields;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import io.reactivex.rxjava3.core.Observable;

public class FirebaseChatRepo implements ChatRepo {

    final String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    final CollectionReference chatRoomCollection = FirebaseFirestore.getInstance().collection(Collections.chatRoomIds);
    final CollectionReference chatCollections = FirebaseFirestore.getInstance().collection(Collections.chatCollection);
    final CollectionReference friendsCollection = FirebaseFirestore.getInstance().collection(Collections.friendsCollection);
    final CollectionReference usersCollection = FirebaseFirestore.getInstance().collection(Collections.userCollection);

    @Override
    public Observable<List<LastChatModel>> getLastChats() throws ExecutionException, InterruptedException {
        List<LastChatModel> lastChatModels = new ArrayList<>();

        return Observable.create(emitter -> {

            // Creating a task which will complete in future with charRoomIds
            Task<QuerySnapshot> chatRoomTask = chatRoomCollection.document(currentUser).collection(Collections.chatRoomIds).get();

            // Attaching success listener for now.
            chatRoomTask.addOnSuccessListener(charRoomsSnapshot -> {


                for (DocumentSnapshot chatRoom : charRoomsSnapshot.getDocuments()) {

                    // converting chatModel
                    ChatRoomModel chatRoomModel = Objects.requireNonNull(chatRoom.toObject(ChatRoomModel.class));

                    // task for chats in a chat room
                    Task<QuerySnapshot> chatsTaskForAChatRoom = chatCollections.document(chatRoomModel.getChatRoomId()).collection(Collections.chatCollection).orderBy(Fields.timestamp, Query.Direction.DESCENDING).limit(1).get();

                    // Attaching a success listener
                    chatsTaskForAChatRoom


                            .addOnSuccessListener(chats -> {
                                for (DocumentSnapshot chat : chats.getDocuments()) {

                                    // task for user details to show in chat list with last message
                                    Task<DocumentSnapshot> userTask = usersCollection.document(chatRoomModel.getFriendId()).get();
                                    SingleChatModel singleChatModel = chat.toObject(SingleChatModel.class);

                                    // Attaching a success listener
                                    userTask.addOnSuccessListener(user -> {

                                        UserModel userModel = user.toObject(UserModel.class);
                                        LastChatModel lastChatModel = new LastChatModel(chatRoomModel.getChatRoomId(), Objects.requireNonNull(singleChatModel).getMessage(), Objects.requireNonNull(userModel).getImage(), userModel.getName(), userModel.getUserid(), Objects.requireNonNull(singleChatModel).getTime()

                                        );

                                        // Adding last chat model to list
                                        lastChatModels.add(lastChatModel);
                                        emitter.onNext(lastChatModels);
                                    });


                                }


                            });
                }


            });


        });


    }


    @Override
    public void sendMessage(String message, String sendTo, String chatRoomId) {
        SingleChatModel singleChatModel = new SingleChatModel();
        singleChatModel.setMessage(message);
        singleChatModel.setSendBy(currentUser);
        singleChatModel.setSentTo(sendTo);
        singleChatModel.setTime(Timestamp.now());
        chatCollections.document(chatRoomId).collection(Collections.chatCollection).add(singleChatModel);
    }
}
