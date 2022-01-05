package com.example.vkloggingonlinefriends.presentation.ui.friendslist

sealed class FriendsListEvent{
    data class LoadingFriends (val getAllFriends: Boolean) : FriendsListEvent()
    data class SearchFriends (val query: String, val active: Boolean) : FriendsListEvent()
}
