package com.example.vkloggingonlinefriends.data.repository

import com.example.vkloggingonlinefriends.data.cache.model.LoggedFriends
import com.example.vkloggingonlinefriends.data.cache.model.OnlineTimeStatistic
import com.example.vkloggingonlinefriends.domain.model.Friend
import com.example.vkloggingonlinefriends.domain.model.User
import com.example.vkloggingonlinefriends.domain.repository.VkRepository
import com.example.vkloggingonlinefriends.domain.state.DataState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class VkRepositoryImpl(
    private val networkDataSource: VkNetworkDataSource,
    private val cacheDataSource: VkCacheDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : VkRepository {

    override suspend fun getUserInfo(): Flow<DataState<User>> = flow {
        emit(DataState.Loading)
        try {
            when (val serviceResponse = networkDataSource.getUserInfo()) {
                is DataState.Success -> {
                    cacheDataSource.insertUser(serviceResponse.data)
                    val user = cacheDataSource.getUser()
                    emit(DataState.Success(user))
                }
                is DataState.Error -> {
                    emit(DataState.Error(serviceResponse.exception))
                }
                else -> {}
            }
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }.flowOn(ioDispatcher)

    override suspend fun getFriends(): Flow<DataState<List<Friend>>> = flow {
        emit(DataState.Loading)
        try {
            when (val serviceResponse = networkDataSource.getFriends()) {
                is DataState.Success -> {
                    val friends = serviceResponse.data
                    friends.forEach {
                        cacheDataSource.insertFriend(it)
                    }
                    val loggedFriends = cacheDataSource.getLoggedFriends()
                    loggedFriends.forEach {
                        it.id.let { id -> cacheDataSource.updateFriendLogging(id, it.logging) }
                    }
                    emit(DataState.Success(cacheDataSource.getAllFriends()))
                }
                is DataState.Error -> {
                    emit(DataState.Error(serviceResponse.exception))
                }
                else -> {}
            }
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }.flowOn(ioDispatcher)

    override suspend fun getOnlineFriends(): Flow<DataState<List<Friend>>> = flow {
        emit(DataState.Loading)
        try {
            val friendsList: MutableList<Friend> = mutableListOf()
            when (val serviceResponse = networkDataSource.getFriendsOnlineIds()) {
                is DataState.Success -> {
                    val idList = serviceResponse.data
                    idList?.forEach {
                        val friend = cacheDataSource.getFriendById(it)
                        friendsList.add(friend)
                    }
                    emit(DataState.Success(friendsList))
                }
                is DataState.Error -> {
                    emit(DataState.Error(serviceResponse.exception))
                }
                else -> {}
            }
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }.flowOn(ioDispatcher)

    override suspend fun getFriendById(id: Int): Friend = withContext(ioDispatcher) {
        cacheDataSource.getFriendById(id)
    }

    override suspend fun getOnlineFriendsId(): List<Int>? = withContext(ioDispatcher) {
        try {
            when (val serviceResponse = networkDataSource.getFriendsOnlineIds()) {
                is DataState.Success -> {
                    serviceResponse.data
                }
                else -> {
                    listOf()
                }
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    override suspend fun updateFriendLogging(id: Int, logging: Boolean) {
        withContext(ioDispatcher) {
            cacheDataSource.updateFriendLogging(id, logging)
        }
    }

    override suspend fun getLoggedFriends(): List<LoggedFriends> = withContext(ioDispatcher) {
        cacheDataSource.getLoggedFriends()
    }

    override suspend fun insertLoggedFriend(id: Int, logging: Boolean) {
        withContext(ioDispatcher) {
            cacheDataSource.insertLoggedFriend(id, logging)
        }
    }

    override suspend fun updateLoggedFriendPreviousOnline(id: Int, onlineStatus: Int) {
        withContext(ioDispatcher) {
            cacheDataSource.updateLoggedFriendPreviousOnline(id, onlineStatus)
        }
    }

    override suspend fun deleteLoggedFriend(id: Int) {
        withContext(ioDispatcher) {
            cacheDataSource.deleteLoggedFriend(id)
        }
    }

    override suspend fun insertOnlineTimeStatistic(statistic: OnlineTimeStatistic) {
        withContext(ioDispatcher) {
            cacheDataSource.insertOnlineTimeStatistic(statistic)
        }
    }

    override suspend fun getStatisticForFriend(id: Int): List<OnlineTimeStatistic> =
        withContext(ioDispatcher) {
            cacheDataSource.getStatisticForFriend(id)
        }

    override suspend fun deleteUsers() {
        withContext(ioDispatcher) {
            cacheDataSource.deleteUsers()
        }
    }

    override suspend fun deleteAllFriends() {
        withContext(ioDispatcher) {
            cacheDataSource.deleteAllFriends()
            cacheDataSource.deleteAllLoggedFriends()
        }
    }
}