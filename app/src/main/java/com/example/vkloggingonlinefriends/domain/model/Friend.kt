package com.example.vkloggingonlinefriends.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Friend(
    val id: Int?,
    val firstName: String?,
    val lastName: String?,
    val photo: String?,
    val online: Int?,
    var logging: Boolean = false,
    var previousOnlineStatus: Int = 0
) : Parcelable
