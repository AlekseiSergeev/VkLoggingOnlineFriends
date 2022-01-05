package com.example.vkloggingonlinefriends.presentation.ui.login

sealed class LoginEvent {
    object LoginStarted : LoginEvent()
    object LoginSuccess : LoginEvent()
    data class LoginFailed(val error: String?): LoginEvent()
    object ReturnToInitialState : LoginEvent()
}
