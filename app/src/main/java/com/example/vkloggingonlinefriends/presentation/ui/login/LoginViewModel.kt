package com.example.vkloggingonlinefriends.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkloggingonlinefriends.data.cache.datastore.AppDataStore
import com.example.vkloggingonlinefriends.presentation.ui.login.LoginEvent.*
import com.example.vkloggingonlinefriends.utils.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(private val dataStore: AppDataStore) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState(isLoggedIn = false))
    val loginState = _loginState.asStateFlow()

    init {
        observeSavedToken()
    }

    fun onTriggerEvent(event: LoginEvent) {
        when (event) {
            is LoginStarted -> {
                _loginState.value = LoginState(showProgressBar = true)
            }
            is LoginSuccess -> {
                _loginState.value = LoginState(showProgressBar = false, isLoggedIn = true)
            }
            is LoginFailed -> {
                _loginState.value = LoginState(showProgressBar = false, loginError = event.error)
            }
            is ReturnToInitialState -> {
                _loginState.value = LoginState()
            }
        }
    }

    private fun observeSavedToken() {
        viewModelScope.launch {
            dataStore.vkToken.collectLatest {
                if (it != EMPTY_STRING) {
                    onTriggerEvent(LoginSuccess)
                } else onTriggerEvent(ReturnToInitialState)
            }
        }
    }

}