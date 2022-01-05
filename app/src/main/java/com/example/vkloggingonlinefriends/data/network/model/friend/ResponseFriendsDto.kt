package com.example.vkloggingonlinefriends.data.network.model.friend

import com.example.vkloggingonlinefriends.data.network.model.vk.VkError

data class ResponseFriendsDto(
    val response: FriendsDto,
    val error: VkError?
)
