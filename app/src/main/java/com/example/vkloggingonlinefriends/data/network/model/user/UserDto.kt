package com.example.vkloggingonlinefriends.data.network.model.user

import com.google.gson.annotations.SerializedName

data class UserDto(

    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("photo_100")
    val photo: String?
)
