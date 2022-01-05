package com.example.vkloggingonlinefriends.data.repository

import com.example.vkloggingonlinefriends.domain.model.Friend
import com.example.vkloggingonlinefriends.domain.model.User
import com.example.vkloggingonlinefriends.domain.state.DataState

interface VkNetworkDataSource {

    suspend fun getUserInfo(): DataState<User>

    suspend fun getFriends(): DataState<List<Friend>>

    suspend fun getFriendsOnlineIds(): DataState<List<Int>?>

}