package com.example.vkloggingonlinefriends.presentation.ui.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkloggingonlinefriends.data.cache.datastore.AppDataStore
import com.example.vkloggingonlinefriends.domain.model.User
import com.example.vkloggingonlinefriends.domain.repository.VkRepository
import com.example.vkloggingonlinefriends.domain.state.DataState
import com.example.vkloggingonlinefriends.presentation.ui.userprofile.ProfileEvent.*
import com.example.vkloggingonlinefriends.presentation.ui.userprofile.ProfileState.*
import com.example.vkloggingonlinefriends.utils.EMPTY_STRING
import com.vk.api.sdk.VK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val repository: VkRepository,
    private val dataStore: AppDataStore
) : ViewModel() {

    private val _userDataState: MutableStateFlow<DataState<User>> =
        MutableStateFlow(DataState.Loading)

    private val _profileState: MutableStateFlow<ProfileState> =
        MutableStateFlow(InitialProfileState)
    val profileState = _profileState.asStateFlow()


    fun onTriggerEvent(event: ProfileEvent) {
        viewModelScope.launch {
            when (event) {
                is LoadingUser -> {
                    loadingUserInfo()
                    observeDataState()
                }
                is Logout -> {
                    logout()
                }
                is ReturnToInitialProfileState -> {
                    _profileState.update { InitialProfileState }
                }
            }
        }
    }

    private suspend fun loadingUserInfo() {
        repository.getUserInfo()
            .onEach { dataState ->
                _userDataState.value = dataState
            }
            .launchIn(viewModelScope)
    }

    private suspend fun observeDataState() {
        _userDataState.collectLatest { dataState ->
            when (dataState) {
                is DataState.Success<User> -> {
                    _profileState.update { LoadingUserSuccess(dataState.data) }
                }
                is DataState.Error -> {
                    _profileState.update { LoadingUserFailed(dataState.exception.message) }
                }
                DataState.Loading -> {
                    _profileState.update { LoadingUserStarted }
                }
            }
        }
    }

    private suspend fun logout() {
        dataStore.setNewToken(EMPTY_STRING)
        clearCache()
        VK.logout()
        _profileState.update { UnloggedUser }
    }

    private suspend fun clearCache() {
        repository.deleteUsers()
        repository.deleteAllFriends()
    }

}