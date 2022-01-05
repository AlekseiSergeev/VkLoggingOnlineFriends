package com.example.vkloggingonlinefriends.presentation.ui.userprofile

sealed class ProfileEvent{
    object LoadingUser : ProfileEvent()
    object Logout : ProfileEvent()
    object ReturnToInitialProfileState : ProfileEvent()
}
