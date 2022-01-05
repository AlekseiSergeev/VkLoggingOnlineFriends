package com.example.vkloggingonlinefriends.data.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vkloggingonlinefriends.data.cache.model.FriendEntity
import com.example.vkloggingonlinefriends.data.cache.model.LoggedFriends
import com.example.vkloggingonlinefriends.data.cache.model.OnlineTimeStatistic
import com.example.vkloggingonlinefriends.data.cache.model.UserEntity

@Dao
interface VkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("SELECT * FROM users WHERE id = 1")
    suspend fun getUser(): UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriends(friendEntity: FriendEntity)

    @Query("SELECT * FROM friends ORDER by logging DESC")
    suspend fun getAllFriends(): List<FriendEntity>

    @Query("SELECT * FROM friends WHERE id = :id")
    suspend fun getFriendById(id: Int): FriendEntity

    @Query("UPDATE friends SET logging = :logging WHERE id = :id")
    suspend fun updateFriendLogging(id: Int, logging: Boolean)

    @Query("UPDATE friends SET previous_online = :previousOnline WHERE id = :id")
    suspend fun updateFriendPreviousOnline(id: Int, previousOnline: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLoggedFriend(loggedFriends: LoggedFriends)

    @Query("DELETE FROM logged_friends WHERE id = :id  ")
    suspend fun deleteLoggedFriend(id: Int)

    @Query("UPDATE logged_friends SET logging = :logging WHERE id = :id")
    suspend fun updateLoggedFriendLogging(id: Int, logging: Boolean)

    @Query("UPDATE logged_friends SET previous_online = :onlineStatus WHERE id = :id")
    suspend fun updateLoggedFriendPreviousOnline(id: Int, onlineStatus: Int)

    @Query("SELECT * FROM logged_friends ")
    suspend fun getLoggedFriends(): List<LoggedFriends>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOnlineTimeStatistic(statistic: OnlineTimeStatistic)

    @Query("SELECT * FROM saved_statistic WHERE friend_id = :id ")
    suspend fun getStatisticForFriend(id: Int): List<OnlineTimeStatistic>

    @Query("DELETE FROM users")
    suspend fun deleteUsers()

    @Query("DELETE FROM friends")
    suspend fun deleteAllFriends()

    @Query("DELETE FROM logged_friends")
    suspend fun deleteAllLoggedFriends()
}