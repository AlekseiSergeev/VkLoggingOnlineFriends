package com.example.vkloggingonlinefriends.data.repository

import com.example.vkloggingonlinefriends.data.cache.model.LoggedFriends
import com.example.vkloggingonlinefriends.data.cache.model.OnlineTimeStatistic
import com.example.vkloggingonlinefriends.domain.model.Friend
import com.example.vkloggingonlinefriends.domain.model.User

interface VkCacheDataSource {

    suspend fun insertUser(user: User)

    suspend fun getUser(): User

    suspend fun insertFriend(friend: Friend)

    suspend fun getAllFriends(): List<Friend>

    suspend fun getFriendById(id: Int): Friend

    suspend fun updateFriendLogging(id: Int, logging: Boolean)

    suspend fun insertLoggedFriend(id: Int, logging: Boolean)

    suspend fun deleteLoggedFriend(id: Int)

    suspend fun updateLoggedFriendPreviousOnline(id: Int, onlineStatus: Int)

    suspend fun getLoggedFriends(): List<LoggedFriends>

    suspend fun insertOnlineTimeStatistic(statistic: OnlineTimeStatistic)

    suspend fun getStatisticForFriend(id: Int): List<OnlineTimeStatistic>

    suspend fun deleteAllFriends()

    suspend fun deleteAllLoggedFriends()

    suspend fun deleteUsers()
}