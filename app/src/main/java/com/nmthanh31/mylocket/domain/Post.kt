package com.nmthanh31.mylocket.domain

import com.google.firebase.Timestamp

data class Post(
    val id: String,
    val content: String,
    val time: Timestamp,
    val photo: String,
    val toWho: List<String>
)
