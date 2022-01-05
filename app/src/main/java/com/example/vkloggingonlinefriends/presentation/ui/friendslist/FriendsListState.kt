package com.example.vkloggingonlinefriends.presentation.ui.friendslist


sealed class FriendsListState{
    object LoadingFriendsStarted : FriendsListState()
    object LoadingFriendsSuccess : FriendsListState()
    data class LoadingFriendsFailed(val errorMessage: String?): FriendsListState()
}
