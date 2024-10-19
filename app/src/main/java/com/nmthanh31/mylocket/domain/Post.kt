package com.nmthanh31.mylocket.domain

import java.sql.Time

data class Post(
    val uid: String,
    val content: String,
    val time: Time,
    val toWho: List<Friend>,
    val photo: String
)
