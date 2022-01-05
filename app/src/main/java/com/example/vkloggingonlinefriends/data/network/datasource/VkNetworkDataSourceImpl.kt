package com.example.vkloggingonlinefriends.data.network.datasource

import com.example.vkloggingonlinefriends.data.network.mappers.FriendDtoMapper
import com.example.vkloggingonlinefriends.data.network.mappers.UserDtoMapper
import com.example.vkloggingonlinefriends.data.network.service.VkService
import com.example.vkloggingonlinefriends.data.repository.VkNetworkDataSource
import com.example.vkloggingonlinefriends.datastore.AppDataStore
import com.example.vkloggingonlinefriends.domain.model.Friend
import com.example.vkloggingonlinefriends.domain.model.User
import com.example.vkloggingonlinefriends.domain.state.DataState
import com.example.vkloggingonlinefriends.utils.API_VERSION
import com.example.vkloggingonlinefriends.utils.FIELDS
import com.example.vkloggingonlinefriends.utils.FIELDS_FRIENDS

class VkNetworkDataSourceImpl(
    private val vkService: VkService,
    private val userDtoMapper: UserDtoMapper,
    private val friendDtoMapper: FriendDtoMapper,
    private val dataStore: AppDataStore,
) : VkNetworkDataSource {

    override suspend fun getUserInfo(): DataState<User> {
        return try {
            val responseVkServer =
                vkService.getUserInfo(FIELDS, API_VERSION, dataStore.vkToken.value)
            val userResponse = responseVkServer.body()
            if (responseVkServer.isSuccessful && userResponse != null) {
                val userDto = userResponse.response?.firstOrNull()
                DataState.Success(userDtoMapper.mapToDomainModel(userDto))
            } else {
                val vkErrorResponse = responseVkServer.body()?.error
                val errorMessage = vkErrorResponse?.errorMessage
                val errorCode = vkErrorResponse?.errorCode
                DataState.Error(Exception("Error code: $errorCode \n Error: $errorMessage"))
            }
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }

    override suspend fun getFriends(): DataState<List<Friend>> {
        return try {
            val responseVkServer =
                vkService.getFriends(FIELDS_FRIENDS, API_VERSION, dataStore.vkToken.value)
            val friendsResponse = responseVkServer.body()
            if (responseVkServer.isSuccessful && friendsResponse != null) {
                val networkFriends = friendsResponse.response.items
                DataState.Success(friendDtoMapper.toDomainList(networkFriends))
            } else {
                val vkErrorResponse = responseVkServer.body()?.error
                val errorMessage = vkErrorResponse?.errorMessage
                val errorCode = vkErrorResponse?.errorCode
                DataState.Error(Exception("Error code: $errorCode \n Error: $errorMessage"))
            }
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }

    override suspend fun getFriendsOnlineIds(): DataState<List<Int>?> {
        return try {
            val responseVkServer =
                vkService.getFriendsOnlineIds(API_VERSION, dataStore.vkToken.value)
            val friendsIdResponse = responseVkServer.body()
            if (responseVkServer.isSuccessful && friendsIdResponse != null) {
                val friendsId = friendsIdResponse.response
                DataState.Success(friendsId)
            } else {
                val vkErrorResponse = responseVkServer.body()?.error
                val errorMessage = vkErrorResponse?.errorMessage
                val errorCode = vkErrorResponse?.errorCode
                DataState.Error(Exception("Error code: $errorCode \n Error: $errorMessage"))
            }
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }

}