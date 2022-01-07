package com.example.vkloggingonlinefriends.data.cache.datasource

import com.example.vkloggingonlinefriends.data.cache.database.VkDao
import com.example.vkloggingonlinefriends.data.cache.mappers.FriendEntityMapper
import com.example.vkloggingonlinefriends.data.cache.mappers.UserEntityMapper
import com.example.vkloggingonlinefriends.data.cache.model.LoggedFriends
import com.example.vkloggingonlinefriends.data.cache.model.OnlineTimeStatistic
import com.example.vkloggingonlinefriends.data.repository.VkCacheDataSource
import com.example.vkloggingonlinefriends.domain.model.Friend
import com.example.vkloggingonlinefriends.domain.model.User

class VkCacheDataSourceImpl(
    private val vkDao: VkDao,
    private val userEntityMapper: UserEntityMapper,
    private val friendEntityMapper: FriendEntityMapper
) : VkCacheDataSource {

    override suspend fun insertUser(user: User) {
        vkDao.insertUser(userEntityMapper.mapFromDomainModel(user))
    }

    override suspend fun getUser(): User = userEntityMapper.mapToDomainModel(vkDao.getUser())

    override suspend fun insertFriend(friend: Friend) {
        vkDao.insertFriends(friendEntityMapper.mapFromDomainModel(friend))
    }

    override suspend fun getAllFriends(): List<Friend> =
        friendEntityMapper.toDomainList(vkDao.getAllFriends())

    override suspend fun getFriendById(id: Int): Friend =
        friendEntityMapper.mapToDomainModel(vkDao.getFriendById(id))

    override suspend fun updateFriendLogging(id: Int, logging: Boolean) {
        vkDao.updateFriendLogging(id, logging)
    }

    override suspend fun insertLoggedFriend(id: Int, logging: Boolean) {
        vkDao.insertLoggedFriend(LoggedFriends(id, logging, 0, 0))
    }

    override suspend fun deleteLoggedFriend(id: Int) {
        vkDao.deleteLoggedFriend(id)
    }

    override suspend fun updateLoggedFriendPreviousOnline(id: Int, onlineStatus: Int) {
        vkDao.updateLoggedFriendPreviousOnline(id, onlineStatus)
    }

    override suspend fun getLoggedFriends(): List<LoggedFriends> = vkDao.getLoggedFriends()

    override suspend fun insertOnlineTimeStatistic(statistic: OnlineTimeStatistic) {
        vkDao.insertOnlineTimeStatistic(statistic)
    }

    override suspend fun getStatisticForFriend(id: Int): List<OnlineTimeStatistic> =
        vkDao.getStatisticForFriend(id)

    override suspend fun deleteUsers() {
        vkDao.deleteUsers()
    }

    override suspend fun deleteAllFriends() {
        vkDao.deleteAllFriends()
    }

    override suspend fun deleteAllLoggedFriends() {
        vkDao.deleteAllLoggedFriends()
    }

}