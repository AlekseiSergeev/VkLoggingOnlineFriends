package com.example.vkloggingonlinefriends.data.network.service

import com.example.vkloggingonlinefriends.data.network.model.friend.OnlineFriendsId
import com.example.vkloggingonlinefriends.data.network.model.friend.ResponseFriendsDto
import com.example.vkloggingonlinefriends.data.network.model.user.ResponseUserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VkService {

    @GET("users.get")
    suspend fun getUserInfo(
        @Query("fields") fields: String,
        @Query("v") apiVersion: String,
        @Query("access_token") accessToken: String
    ): Response<ResponseUserDto>

    @GET("friends.get")
    suspend fun getFriends(
        @Query("fields") fields: String,
        @Query("v") apiVersion: String,
        @Query("access_token") accessToken: String
    ): Response<ResponseFriendsDto>

    @GET("friends.getOnline?")
    suspend fun getFriendsOnlineIds(
        @Query("v") apiVersion: String,
        @Query("access_token") accessToken: String
    ): Response<OnlineFriendsId>
}