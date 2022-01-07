package com.example.vkloggingonlinefriends.data.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logged_friends")
data class LoggedFriends(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "logging")
    val logging: Boolean,
    @ColumnInfo(name = "previous_online")
    val previousOnlineStatus: Int,
    @ColumnInfo(name = "current_online")
    val currentOnlineStatus: Int
)
