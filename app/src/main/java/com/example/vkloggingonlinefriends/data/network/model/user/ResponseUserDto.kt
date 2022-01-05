package com.example.vkloggingonlinefriends.data.network.model.user

import com.example.vkloggingonlinefriends.data.network.model.vk.VkError

data class ResponseUserDto(
    val response: List<UserDto>?,
    val error: VkError?
)
