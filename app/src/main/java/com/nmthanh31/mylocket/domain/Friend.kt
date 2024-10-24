package com.nmthanh31.mylocket.domain

enum class FriendStatus {
    SENT,
    FRIENDS,
    RECEIVED
}

class Friend(var status: String, id: String, name: String, email: String, photo: String?): User(id, name, email, photo)

