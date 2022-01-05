package com.example.vkloggingonlinefriends.data.network.model.friend

import com.example.vkloggingonlinefriends.data.network.model.vk.VkError

data class OnlineFriendsId(
    val response: List<Int>?,
    val error: VkError?
)
