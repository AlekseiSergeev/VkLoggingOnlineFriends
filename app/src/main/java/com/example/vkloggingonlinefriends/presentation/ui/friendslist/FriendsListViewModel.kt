package com.example.vkloggingonlinefriends.presentation.ui.friendslist


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkloggingonlinefriends.domain.model.Friend
import com.example.vkloggingonlinefriends.domain.repository.VkRepository
import com.example.vkloggingonlinefriends.domain.state.DataState
import com.example.vkloggingonlinefriends.presentation.ui.friendslist.FriendsListEvent.*
import com.example.vkloggingonlinefriends.presentation.ui.friendslist.FriendsListState.*
import com.example.vkloggingonlinefriends.utils.API_VERSION
import com.example.vkloggingonlinefriends.utils.FIELDS_FRIENDS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class FriendsListViewModel
@Inject constructor(
    private val repository: VkRepository,
): ViewModel() {

    private val _friendsDataState: MutableStateFlow<DataState<List<Friend>>> = MutableStateFlow(DataState.Loading)

    private val _stateFriends: MutableStateFlow<FriendsListState> = MutableStateFlow(LoadingFriendsStarted)
    val stateFriends = _stateFriends.asStateFlow()

    private val _resultFriends: MutableStateFlow<List<Friend>> = MutableStateFlow(listOf())
    private val _filteredFriends: MutableStateFlow<List<Friend>> = MutableStateFlow(listOf())
    val filteredFriends = _filteredFriends.asStateFlow()

    fun onTriggerEvent (event: FriendsListEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when(event) {
                is LoadingFriends -> {
                    if(event.getAllFriends) loadAllFriends()
                    else loadOnlineFriends()
                }
                is SearchFriends -> {
                    if(event.active) {
                        filterFriends(event.query)
                    }
                    else {
                        resetFilter()
                    }
                }
            }
        }
    }

    private fun filterFriends(query: String) {
        val filteredList = mutableListOf<Friend>()
        filteredList.clear()
        viewModelScope.launch {
            _resultFriends.value.forEach {
                if(it.firstName != null && it.lastName != null) {
                    if (it.firstName.contains(query, true) || it.lastName.contains(query, true)) {
                        filteredList.add(it)
                    }
                }
            }
        }
        _filteredFriends.value = filteredList
    }

    private fun resetFilter() {
        _filteredFriends.value = _resultFriends.value
    }

    private suspend fun loadAllFriends() {
        repository.getFriends()
            .onEach { dataState ->
                _friendsDataState.value = dataState
            }
            .launchIn(viewModelScope)
        observeDataState()
    }

    private suspend fun loadOnlineFriends() {
        repository.getOnlineFriends()
            .onEach { dataState ->
                _friendsDataState.value = dataState
            }
            .launchIn(viewModelScope)
        observeDataState()
    }

    private suspend fun observeDataState() {
        _friendsDataState.collectLatest { dataState ->
            when (dataState) {
                is DataState.Success<List<Friend>> -> {
                    _resultFriends.value = dataState.data
                    resetFilter()
                    _stateFriends.value = LoadingFriendsSuccess
                }
                is DataState.Error -> {
                    _stateFriends.value = LoadingFriendsFailed(dataState.exception.message)
                }
                is DataState.Loading -> {
                    _stateFriends.value = LoadingFriendsStarted
                }
            }
        }
    }
}