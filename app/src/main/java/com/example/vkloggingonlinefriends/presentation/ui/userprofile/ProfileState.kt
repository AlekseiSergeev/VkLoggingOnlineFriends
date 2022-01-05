package com.example.vkloggingonlinefriends.presentation.ui.userprofile

import com.example.vkloggingonlinefriends.domain.model.User


sealed class ProfileState{
    object InitialProfileState : ProfileState()
    object LoadingUserStarted : ProfileState()
    data class LoadingUserSuccess (val user: User) : ProfileState()
    data class LoadingUserFailed (val errorMessage: String?): ProfileState()
    object UnloggedUser : ProfileState()
}
