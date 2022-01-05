package com.example.vkloggingonlinefriends.data.network.model.vk

import com.google.gson.annotations.SerializedName

data class VkError(
    @SerializedName("error_code")
    val errorCode: Int?,
    @SerializedName("error_msg")
    val errorMessage: String?
)
