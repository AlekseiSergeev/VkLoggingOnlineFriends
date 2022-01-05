package com.example.vkloggingonlinefriends.presentation.ui.frienddetail

sealed class FriendDetailEvent {
    data class SetCurrentFriend(val friendId: Int): FriendDetailEvent()
    object SelectDateButtonClicked: FriendDetailEvent()
    data class SelectedDateChanged(val year: Int, val month: Int, val day: Int): FriendDetailEvent()
    object StartLoggingFriend: FriendDetailEvent()
    object StopLoggingFriend: FriendDetailEvent()
}
