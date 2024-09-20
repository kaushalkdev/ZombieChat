package com.example.zombiechat.friends.data.repo

import android.widget.Toast
import com.example.zombiechat.account.data.models.RequestModel
import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.constants.api.collections.Collections
import com.example.zombiechat.friends.data.models.FriendsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.tasks.await

class FriendsRepo {
    private var userCollection: CollectionReference =
        FirebaseFirestore.getInstance().collection(Collections.userCollection)

    private val friendsColl: CollectionReference =
        FirebaseFirestore.getInstance().collection(Collections.friendsCollection)

    private val requestsColl: CollectionReference =
        FirebaseFirestore.getInstance().collection(Collections.requestsCollection)

    private var currentUserId: String? = FirebaseAuth.getInstance().uid


    suspend fun getAllFriends(): List<UserModel> {

        val friends: MutableList<UserModel> = mutableListOf()

        try {

            // get all the friends of the current user.
            val friendsList: List<FriendsModel?> =
                friendsColl.document(currentUserId!!).collection(Collections.friendsCollection)
                    .get().await().documents.map { it.toObject(FriendsModel::class.java) }



            // filter out the friends from the user collection.
            val alUsersSnapshot =
                userCollection.whereIn("userid", friendsList.map { it?.friendId }).get()
                    .await().documents
            for (user in alUsersSnapshot) {
                if (friendsList.map { it?.friendId }.contains(user.id)) {
                    user.toObject(UserModel::class.java)?.let { friends.add(it) }
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
            friendsColl.document(currentUserId!!).collection(Collections.friendsCollection)
                .add(FriendsModel(userId)).await()

            // adding the current user a friend for other user.
            friendsColl.document(userId).collection(Collections.friendsCollection)
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