package com.nmthanh31.mylocket.data

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.nmthanh31.mylocket.domain.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostViewModelFactory(private val uid: String): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)){
            return PostViewModel(uid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class PostViewModel(private val uid: String): ViewModel() {
    private val db = Firebase.firestore

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>>
        get() = _posts

    private var listenerRegistration: ListenerRegistration? = null

    init {
        listenForPostUpdates()
    }

    private fun listenForPostUpdates(){
        listenerRegistration = db.collectionGroup("posts")
            .whereArrayContains("toWho", uid)
            .addSnapshotListener{
                snapshot, e->
                if (e != null){
                    Log.e("Load Posts", "Listen Failed", e)
                    _posts.value = emptyList()
                    return@addSnapshotListener
                }
                if (snapshot!=null && !snapshot.isEmpty){
                    val posts = mutableListOf<Post>()
                    for (document in snapshot){
                        val postId = document.getString("id")
                        val postContent = document.getString("content")
                        val postTime = document.getTimestamp("time")
                        val postPhoto = document.getString("photo")
                        val postToWho = document.get("toWho") as List<String>
                        val post = Post(id = postId!!, content = postContent!!, time = postTime!!, photo = postPhoto!!, toWho = postToWho)
                        posts.add(post)
                    }
                    _posts.value = posts
                }else{
                    Log.d("Load Posts", "No posts found.")
                    _posts.value = emptyList()
                }
            }
    }

    fun addPost(content: String, photo:String, toWho: List<String>){
        viewModelScope.launch {
            try {
                val postID = db.collection("users").document(uid).collection("posts").document().id
                val newPost = hashMapOf(
                    "id" to postID,
                    "content" to content,
                    "time" to FieldValue.serverTimestamp(),
                    "photo" to photo,
                    "toWho" to toWho
                )
                db.collection("users")
                    .document(uid)
                    .collection("posts")
                    .document(postID)
                    .set(newPost).await()
            }catch (e: Exception){
                Log.e("Add Post", e.toString())
            }
        }
    }


}

