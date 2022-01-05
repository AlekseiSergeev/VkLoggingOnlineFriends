package com.example.vkloggingonlinefriends.presentation.ui.login

data class LoginState(
    val isLoggedIn: Boolean = false,
    val showProgressBar: Boolean = false,
    val loginError: String? = null,
)
