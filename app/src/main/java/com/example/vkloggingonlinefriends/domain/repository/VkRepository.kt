package com.example.vkloggingonlinefriends.domain.repository

import com.example.vkloggingonlinefriends.data.cache.model.LoggedFriends
import com.example.vkloggingonlinefriends.data.cache.model.OnlineTimeStatistic
import com.example.vkloggingonlinefriends.domain.model.Friend
import com.example.vkloggingonlinefriends.domain.model.User
import com.example.vkloggingonlinefriends.domain.state.DataState
import kotlinx.coroutines.flow.Flow

interface VkRepository {

    suspend fun getUserInfo(): Flow<DataState<User>>

    suspend fun getFriends(): Flow<DataState<List<Friend>>>

    suspend fun getOnlineFriends(): Flow<DataState<List<Friend>>>

    suspend fun getFriendById(id: Int): Friend

    suspend fun updateFriendLogging(id: Int, logging: Boolean)

    suspend fun getLoggedFriends(): List<LoggedFriends>

    suspend fun insertLoggedFriend(id: Int, logging: Boolean)

    suspend fun deleteLoggedFriend(id: Int)

    suspend fun updateLoggedFriendPreviousOnline(id: Int, onlineStatus: Int)

    suspend fun insertOnlineTimeStatistic(statistic: OnlineTimeStatistic)

    suspend fun getStatisticForFriend(id: Int): List<OnlineTimeStatistic>

    suspend fun getOnlineFriendsId(): List<Int>?

    suspend fun deleteAllFriends()

    suspend fun deleteUsers()
}
