package com.example.zombiechat.src.chat.data.repo

import android.util.Log
import com.example.zombiechat.src.account.data.models.UserModel
import com.example.zombiechat.src.chat.data.models.ChatRoomModel
import com.example.zombiechat.src.chat.data.models.LastChatModel
import com.example.zombiechat.src.chat.data.models.SingleChatModel
import com.example.zombiechat.util.consts.DbCollection
import com.example.zombiechat.util.consts.Fields
import com.example.zombiechat.util.service.ChatException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import java.util.Objects
import java.util.concurrent.ExecutionException

class FirebaseChatRepo : ChatRepo {
    val currentUser: String = Objects.requireNonNull(FirebaseAuth.getInstance().currentUser).uid
    val chatRoomCollection: CollectionReference =
        FirebaseFirestore.getInstance().collection(DbCollection.chatRoomIds)
    val chatCollections: CollectionReference =
        FirebaseFirestore.getInstance().collection(DbCollection.chatCollection)
    val friendsCollection: CollectionReference =
        FirebaseFirestore.getInstance().collection(DbCollection.friendsCollection)
    val usersCollection: CollectionReference =
        FirebaseFirestore.getInstance().collection(DbCollection.userCollection)

    @get:Throws(
        ExecutionException::class, InterruptedException::class
    )
    override val lastChats: Observable<List<LastChatModel>?>
        get() {
            val lastChatModels: MutableList<LastChatModel> = ArrayList()

            // TODO : fix the order of list view in streams.
            return Observable.create { emitter: ObservableEmitter<List<LastChatModel>?> ->

                // Creating a task which will complete in future with charRoomIds
                val chatRoomTask =
                    chatRoomCollection.document(currentUser).collection(DbCollection.chatRoomIds)
                        .get()


                // Attaching success listener for now.
                chatRoomTask.addOnSuccessListener { charRoomsSnapshot: QuerySnapshot ->
                    for (chatRoom in charRoomsSnapshot.documents) {
                        // converting chatModel

                        val chatRoomModel = Objects.requireNonNull(
                            chatRoom.toObject(
                                ChatRoomModel::class.java
                            )
                        )

                        // task for chats in a chat room
                        val chatsTaskForAChatRoom =
                            chatCollections.document(chatRoomModel.chatRoomId)
                                .collection(DbCollection.chatCollection).orderBy(
                                    Fields.timestamp, Query.Direction.DESCENDING
                                ).limit(1).get()

                        // Attaching a success listener
                        chatsTaskForAChatRoom


                            .addOnSuccessListener { chats: QuerySnapshot ->
                                for (chat in chats.documents) {
                                    // task for user details to show in chat list with last message

                                    val userTask =
                                        usersCollection.document(chatRoomModel.friendId).get()
                                    val singleChatModel = chat.toObject(
                                        SingleChatModel::class.java
                                    )

                                    // Attaching a success listener
                                    userTask.addOnSuccessListener { user: DocumentSnapshot ->
                                        val userModel = user.toObject(
                                            UserModel::class.java
                                        )
                                        val lastChatModel = LastChatModel(
                                            chatRoomModel.chatRoomId,
                                            Objects.requireNonNull(singleChatModel).message,
                                            Objects.requireNonNull(userModel).image,
                                            userModel!!.name,
                                            userModel.userid,
                                            Objects.requireNonNull(singleChatModel).time

                                        )

                                        // Adding last chat model to list
                                        lastChatModels.add(lastChatModel)
                                        lastChatModels.sort(java.util.Comparator { o1: LastChatModel, o2: LastChatModel ->
                                            o2.msgTime.compareTo(
                                                o1.msgTime
                                            )
                                        })
                                        emitter.onNext(lastChatModels)
                                    }
                                }
                            }
                    }
                }
            }
        }

    override fun getActiveChats(chatRoomId: String?): Observable<List<SingleChatModel?>?>? {
        val singleChatModels: MutableList<SingleChatModel?> = ArrayList()
        return Observable.create { emitter: ObservableEmitter<List<SingleChatModel?>?> ->
            chatCollections.document(
                chatRoomId!!
            ).collection(DbCollection.chatCollection)
                .orderBy(Fields.timestamp, Query.Direction.ASCENDING)
                .addSnapshotListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                    if (error != null) {
                        Log.e("ChatRepo", "Error in getting chat messages", error)
                    }
                    if (value != null) {
                        for (chat in value.documents) {
                            val singleChatModel = chat.toObject(
                                SingleChatModel::class.java
                            )
                            singleChatModels.add(singleChatModel)
                            emitter.onNext(singleChatModels)
                        }
                    }
                }
        }
    }


    override fun sendMessage(message: String?, sendTo: String?, chatRoomId: String?) {

        try {
            val singleChatModel = SingleChatModel()
            singleChatModel.message = message
            singleChatModel.sendBy = currentUser
            singleChatModel.sentTo = sendTo
            singleChatModel.time = now.now()
            chatCollections.document(chatRoomId!!).collection(DbCollection.chatCollection)
                .add(singleChatModel)
        } catch (e: Exception) {
            throw  ChatException("Error in sending message", e)

        }

    }
}
