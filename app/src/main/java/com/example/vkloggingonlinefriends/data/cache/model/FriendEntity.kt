package com.example.vkloggingonlinefriends.data.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="friends")
data class FriendEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "photo")
    val photo: String,
    @ColumnInfo(name = "online")
    val online: Int,
    @ColumnInfo(name = "logging")
    val logging: Boolean
)
