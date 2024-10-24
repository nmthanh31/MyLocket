package com.nmthanh31.mylocket.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.nmthanh31.mylocket.domain.Friend
import com.nmthanh31.mylocket.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel : ViewModel(){
    private val db = Firebase.firestore

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users:StateFlow<List<User>>
        get() {
            return _users
        }

    private var listenerRegistration: ListenerRegistration? = null

    init {
        listenForUserUpdates()
    }

    private fun listenForUserUpdates() {
        listenerRegistration = db.collection("users")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("UserViewModel", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val users = mutableListOf<User>()
                    for (document in snapshot.documents){
                        val userId = document.getString("id")
                        val userName = document.getString("name")
                        val userEmail = document.getString("email")
                        val userPhoto = document.getString("photo")
                        val user = User(id = userId!!, name = userName!!, email = userEmail!!, photo = userPhoto)
                        users.add(user)
                    }
                    _users.value = users
                } else {
                    Log.d("UserViewModel", "No users found.")
                }
            }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            try {
                val userDoc = db.collection("users")
                    .whereEqualTo("id", user.id)
                    .get()
                    .await()
                if (userDoc.isEmpty){
                    val newUser = hashMapOf(
                        "id" to user.id,
                        "name" to user.name,
                        "email" to user.email,
                        "photo" to user.photo
                    )
                    db.collection("users").document(user.id).set(newUser).await()
                    Log.d("Add User", "User added successfully")
                }else{
                    Log.d("Add User", "User with id ${user.id} already exists")
                }
            } catch (e: Exception) {
                Log.e("Add User", "Error adding user", e)
            }
        }
    }


    fun removeUserListener(){
        listenerRegistration?.remove()
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}