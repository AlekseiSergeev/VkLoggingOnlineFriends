package com.example.vkloggingonlinefriends.data.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vkloggingonlinefriends.data.cache.model.FriendEntity
import com.example.vkloggingonlinefriends.data.cache.model.LoggedFriends
import com.example.vkloggingonlinefriends.data.cache.model.OnlineTimeStatistic
import com.example.vkloggingonlinefriends.data.cache.model.UserEntity

@Database(
    entities = [
        FriendEntity::class,
        LoggedFriends::class,
        OnlineTimeStatistic::class,
        UserEntity::class], version = 1
)
abstract class VkDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME: String = "vk_logging_db"
    }

    abstract fun vkDao(): VkDao

}