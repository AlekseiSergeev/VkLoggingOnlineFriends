package com.example.vkloggingonlinefriends.data.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_statistic")
data class OnlineTimeStatistic(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "friend_id")
    val friendId: Int,
    @ColumnInfo(name = "current_date")
    val currentDate: Long,
    @ColumnInfo(name = "description")
    val description: String
)
