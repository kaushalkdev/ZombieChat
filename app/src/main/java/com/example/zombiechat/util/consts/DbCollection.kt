package com.example.zombiechat.util.consts


/**
 * This class contains constants for various Firestore collections used in the application.
 */
class DbCollection {

    companion object {
        /**
         * Firestore collection name for users.
         */
        const val userCollection = "users"

        /**
         * Firestore collection name for user chats.
         */
        const val chatCollection = "chatCollection"

        /**
         * Firestore collection name for chat rooms.
         */
        const val chatRoomIds = "chatRoomIds"

        /**
         * Firestore collection name for friends.
         */
        const val friendsCollection = "friends"

        /**
         * Firestore collection name for friend requests.
         */
        const val requestsCollection = "friendRequests"

        /**
         * Firestore collection name for chat ids.
         */
        const val chatIdCollection = "chatIds"
    }
}
