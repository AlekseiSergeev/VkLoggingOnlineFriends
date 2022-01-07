package com.example.vkloggingonlinefriends.domain.model

data class Friend(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val photo: String,
    val online: Int,
    val logging: Boolean = false
)
