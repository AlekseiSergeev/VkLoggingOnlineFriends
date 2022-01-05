package com.example.vkloggingonlinefriends.presentation.ui.frienddetail

import com.example.vkloggingonlinefriends.data.cache.model.OnlineTimeStatistic
import com.example.vkloggingonlinefriends.domain.model.Friend

sealed class FriendDetailState{
    object FriendInfoLoading: FriendDetailState()
    data class FriendInfoLoadedSuccess (val friend: Friend, val minDate: Long): FriendDetailState()
    object DateSelection: FriendDetailState()
    data class StatisticsReady (val statistics: List<OnlineTimeStatistic>): FriendDetailState()
    object LoggingStarted: FriendDetailState()
    object LoggingStopped: FriendDetailState()
}
