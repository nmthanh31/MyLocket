package com.nmthanh31.mylocket.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.nmthanh31.mylocket.domain.Friend
import com.nmthanh31.mylocket.domain.FriendStatus
import com.nmthanh31.mylocket.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class FriendViewModelFactory(private val uid: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendViewModel::class.java)) {
            return FriendViewModel(uid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class FriendViewModel(private val uid: String) : ViewModel() {
    private val db = Firebase.firestore

    private val _friends = MutableStateFlow<List<Friend>>(emptyList())
    val friends: StateFlow<List<Friend>>
        get() {
            return _friends
        }

    private var listenerRegistration: ListenerRegistration? = null

    init {
        listenForFriendUpdates()
    }

    private fun listenForFriendUpdates() {
        listenerRegistration = db.collection("users")
            .document(uid)
            .collection("friends")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("Load Friends", "Listen failed.", e)
                    _friends.value = emptyList()
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val friends = mutableListOf<Friend>()
                    for (document in snapshot.documents){
                        val friendId = document.getString("id")
                        val friendName = document.getString("name")
                        val friendEmail = document.getString("email")
                        val friendPhoto = document.getString("photo")
                        val friendStatus = document.getString("status")
                        val friend = Friend(id = friendId!!, name = friendName!!, email = friendEmail!!, photo = friendPhoto, status = friendStatus!!)
                        friends.add(friend)
                    }
                    _friends.value = friends
                } else {
                    Log.d("Load Friends", "No friends found.")
                    _friends.value = emptyList()
                }
            }
    }

    fun addFriend(friend: Friend, user: User) {
        viewModelScope.launch {
            try {
                val newFriendForMe = hashMapOf(
                    "id" to friend.id,
                    "name" to friend.name,
                    "email" to friend.email,
                    "photo" to friend.photo,
                    "status" to friend.status
                )
                db.collection("users").document(uid).collection("friends").document(friend.id).set(newFriendForMe).await()

                val newFriendForFriend = hashMapOf(
                    "id" to user.id,
                    "name" to user.name,
                    "email" to user.email,
                    "photo" to user.photo,
                    "status" to FriendStatus.RECEIVED.toString()
                )
                db.collection("users").document(friend.id).collection("friends").document(user.id).set(newFriendForFriend).await()
            } catch (e: Exception) {
                Log.e("Add User", "Error adding user", e)
            }
        }
    }

    fun acceptFriend(friend: Friend, user: User){
        viewModelScope.launch {
            try {
                db.collection("users")
                    .document(uid)
                    .collection("friends")
                    .document(friend.id)
                    .update("status", FriendStatus.FRIENDS.toString())
                    .addOnSuccessListener {
                        Log.d("Firestore","Accept Friend Successful")
                    }.await()
                db.collection("users")
                    .document(friend.id)
                    .collection("friends")
                    .document(user.id)
                    .update("status", FriendStatus.FRIENDS.toString())
                    .addOnSuccessListener {
                        Log.d("Firestore","Accept Friend Successful")
                    }.await()
            }catch (e: Exception){
                Log.e("Friend Firestore", e.toString())
            }
        }
    }

    fun deleteFriend(userId: String, friendId: String){
        viewModelScope.launch {
            try {
                db.collection("users").document(uid).collection("friends").document(friendId).delete().await()
                db.collection("users").document(friendId).collection("friends").document(userId).delete().await()
                Log.d("Delete Friend", "Friend deleted successfully")
            } catch (e: Exception) {
                Log.e("Delete Friend", "Error deleting friend", e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }

}