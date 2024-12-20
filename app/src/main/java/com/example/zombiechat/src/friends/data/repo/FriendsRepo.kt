package com.example.zombiechat.friends.data.repo

import com.example.zombiechat.src.account.data.models.RequestModel
import com.example.zombiechat.src.account.data.models.UserModel
import com.example.zombiechat.util.consts.DbCollection
import com.example.zombiechat.friends.data.models.FriendsModel
import com.example.zombiechat.friends.data.models.NewFriendsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FriendsRepo {
    private var userCollection: CollectionReference =
        FirebaseFirestore.getInstance().collection(DbCollection.userCollection)

    private val friendsColl: CollectionReference =
        FirebaseFirestore.getInstance().collection(DbCollection.friendsCollection)

    private val requestsColl: CollectionReference =
        FirebaseFirestore.getInstance().collection(DbCollection.requestsCollection)

    private var currentUserId: String? = FirebaseAuth.getInstance().uid


    suspend fun getAllFriends(): List<NewFriendsModel> {

        val friends: MutableList<NewFriendsModel> = mutableListOf()

        try {

            // get all the friends of the current user.
            val friendsList: List<FriendsModel?> =
                friendsColl.document(currentUserId!!).collection(DbCollection.friendsCollection)
                    .get().await().documents.map { it.toObject(FriendsModel::class.java) }


            // filter out the friends from the user collection.
            val alUsersSnapshot =
                userCollection.whereIn("userid", friendsList.map { it?.friendId }).get()
                    .await().documents
            for (user in alUsersSnapshot) {
                if (friendsList.map { it?.friendId }.contains(user.id)) {
                    user.toObject(UserModel::class.java)?.let {
                        friends.add(
                            NewFriendsModel(
                                it.userid,
                                it.name,
                                it.image,
                                it.status,
                                friendsList.find { it?.friendId == user.id }?.chatRoomId
                            )
                        )
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return friends
    }

    suspend fun getAllRequests(): List<UserModel> {
        val requests: MutableList<UserModel> = mutableListOf()

        try {
            // find all the requests sent to the current user.
            val requestsSnapshots = requestsColl.whereEqualTo("sentTo", currentUserId).get().await()


            if (requestsSnapshots.isEmpty) {
                return requests
            }

            // find all the users who sent the requests.
            val allUsersSnapshot =
                userCollection.whereIn("userid", requestsSnapshots.map { it.getString("sendBy") })
                    .get().await().documents


            for (user in allUsersSnapshot) {
                user.toObject(UserModel::class.java)?.let { requests.add(it) }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return requests
    }

    suspend fun acceptRequest(userId: String): Boolean {
        try {

            // finds if document exists in the requests collection.
            val requestSnapshot = requestsColl.get()
                .await().documents.filter { it.toObject<RequestModel>()?.sendBy == userId }

            // returns if request is not present.
            if (requestSnapshot.isEmpty()) {
                return false
            }

            // adding the other user a friend for current user.
            friendsColl.document(currentUserId!!).collection(DbCollection.friendsCollection)
                .add(FriendsModel(userId)).await()

            // adding the current user a friend for other user.
            friendsColl.document(userId).collection(DbCollection.friendsCollection)
                .add(FriendsModel(currentUserId)).await()

            // deleting the request
            requestsColl.document(requestSnapshot.first().id).delete().await()

            return true

        } catch (e: Exception) {
            e.printStackTrace()

        }
        return false
    }

    suspend fun rejectRequest(userId: String) {
        try {
            // finds if document exists in the requests collection.
            val requestSnapshot = requestsColl.get()
                .await().documents.filter { it.toObject<RequestModel>()?.sendBy == userId }

            // returns if request is not present.
            if (requestSnapshot.isEmpty()) {
                return
            }

            // deleting the request
            requestsColl.document(requestSnapshot.first().id).delete().await()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}