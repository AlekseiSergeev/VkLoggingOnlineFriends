package com.example.vkloggingonlinefriends.data.network.model.friend

import com.google.gson.annotations.SerializedName

data class FriendDto(
    var id: Int?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("photo_100")
    val photo: String?,
    var online: Int?
)
