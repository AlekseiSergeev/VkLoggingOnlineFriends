package com.example.vkloggingonlinefriends.presentation.ui.frienddetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkloggingonlinefriends.data.cache.model.OnlineTimeStatistic
import com.example.vkloggingonlinefriends.domain.model.Friend
import com.example.vkloggingonlinefriends.domain.repository.VkRepository
import com.example.vkloggingonlinefriends.presentation.ui.frienddetail.FriendDetailEvent.*
import com.example.vkloggingonlinefriends.presentation.ui.frienddetail.FriendDetailState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FriendDetailViewModel
@Inject constructor(
    private val repository: VkRepository
) : ViewModel() {

    private lateinit var currentFriend: Friend

    private val _stateFriendDetail: MutableStateFlow<FriendDetailState> =
        MutableStateFlow(FriendInfoLoading)
    val stateFriendDetail = _stateFriendDetail.asStateFlow()

    private val friendStatistic = mutableListOf<OnlineTimeStatistic>()

    private val friendStatisticByDate = mutableListOf<OnlineTimeStatistic>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun onTriggerEvent(event: FriendDetailEvent) {
        viewModelScope.launch {
            when (event) {
                is SetCurrentFriend -> {
                    setCurrentFriend(event.friendId)
                    loadFriendStatistic(event.friendId)
                    _stateFriendDetail.value = FriendInfoLoadedSuccess(currentFriend, getMinDate())
                }
                SelectDateButtonClicked -> {
                    _stateFriendDetail.value = DateSelection
                }
                is SelectedDateChanged -> {
                    getStatisticByDate(event.year, event.month, event.day)
                    _stateFriendDetail.value = StatisticsReady(getFormattedStatistic())
                }
                StartLoggingFriend -> {
                    startLoggingFriend()
                }
                StopLoggingFriend -> {
                    stopLoggingFriend()
                }
            }
        }
    }

    private suspend fun loadFriendStatistic(id: Int) {
        friendStatistic.addAll(repository.getStatisticForFriend(id))
        getMinDate()
    }

    private fun getMinDate(): Long {
        var minDate = System.currentTimeMillis()
        if (!friendStatistic.isNullOrEmpty())  minDate = friendStatistic[0].currentDate
        return minDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getStatisticByDate(year: Int, month: Int, day: Int) {
        val statistics = mutableListOf<OnlineTimeStatistic>()
        val selectedDate = LocalDate.of(year, month, day)
        friendStatistic.forEach {
            val currentDate =
                Instant.ofEpochMilli(it.currentDate).atZone(ZoneId.systemDefault()).toLocalDate()
            if (currentDate.dayOfMonth == selectedDate.dayOfMonth
                && currentDate.month == selectedDate.month && currentDate.year == selectedDate.year
            ) {
                statistics.add(it)
            }
        }
        friendStatisticByDate.clear()
        friendStatisticByDate.addAll(statistics)
    }

    private fun getFormattedStatistic(): List<String> {
        val formattedStatistic = mutableListOf<String>()
        friendStatisticByDate.forEach { statistic ->
            val sdf = SimpleDateFormat("HH:mm:ss dd/M/yyyy", Locale.ENGLISH)
            val date = sdf.format(Date(statistic.currentDate))
            val string = "${statistic.description} $date \n"
            formattedStatistic.add(string)
        }
        return formattedStatistic
    }

    private suspend fun setCurrentFriend(friendId: Int) {
        currentFriend = repository.getFriendById(friendId)
    }

    private suspend fun startLoggingFriend() {
        insertLoggedFriend()
        updateFriendLogging(true)
        _stateFriendDetail.value = LoggingStarted
    }

    private suspend fun stopLoggingFriend() {
        deleteLoggedFriend()
        updateFriendLogging(false)
        val loggedFriends = repository.getLoggedFriends()
        if (loggedFriends.isNullOrEmpty()) _stateFriendDetail.value = LoggingStopped
    }

    private suspend fun updateFriendLogging(logging: Boolean) {
        repository.updateFriendLogging(currentFriend.id, logging)
    }

    private suspend fun insertLoggedFriend() {
        repository.insertLoggedFriend(currentFriend.id, true)
    }

    private suspend fun deleteLoggedFriend() {
        repository.deleteLoggedFriend(currentFriend.id)
    }

}