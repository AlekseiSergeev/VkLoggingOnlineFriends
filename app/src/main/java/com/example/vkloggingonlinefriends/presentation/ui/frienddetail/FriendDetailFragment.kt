package com.example.vkloggingonlinefriends.presentation.ui.frienddetail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vkloggingonlinefriends.R
import com.example.vkloggingonlinefriends.databinding.FragmentFriendDetailBinding
import com.example.vkloggingonlinefriends.data.cache.datastore.AppDataStore
import com.example.vkloggingonlinefriends.domain.model.Friend
import com.example.vkloggingonlinefriends.domain.service.FriendsLoggingService
import com.example.vkloggingonlinefriends.presentation.ui.frienddetail.FriendDetailEvent.*
import com.example.vkloggingonlinefriends.presentation.ui.frienddetail.FriendDetailState.*
import com.example.vkloggingonlinefriends.utils.EMPTY_STRING
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class FriendDetailFragment : Fragment() {

    companion object {
        private const val MONDAY = 2
    }

    @Inject
    lateinit var dataStore: AppDataStore
    private lateinit var binding: FragmentFriendDetailBinding
    private val viewModel: FriendDetailViewModel by viewModels()
    private var serviceIsRunning = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        observeStateFriendDetail()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: FriendDetailFragmentArgs by navArgs()
        val friendId = args.friendId
        viewModel.onTriggerEvent(SetCurrentFriend(friendId))

        observeServiceState()

        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.onTriggerEvent(StartLoggingFriend)
            } else {
                viewModel.onTriggerEvent(StopLoggingFriend)
            }
        }

        binding.selectDateBtn.setOnClickListener {
            viewModel.onTriggerEvent(SelectDateButtonClicked)
        }

        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            setButtonText("$day.${month + 1}.$year")
            viewModel.onTriggerEvent(SelectedDateChanged(year, month + 1, day))
        }
    }

    private fun observeStateFriendDetail() {
        lifecycleScope.launchWhenResumed {
            viewModel.stateFriendDetail.collectLatest { state ->
                when (state) {
                    FriendInfoLoading -> {
                        binding.calendarView.isVisible = false
                        binding.savedStatisticTv.isVisible = false
                    }
                    is FriendInfoLoadedSuccess -> {
                        binding.calendarView.isVisible = false
                        binding.savedStatisticTv.isVisible = false
                        showFriendInfo(state.friend)
                        calendarViewSetting(state.minDate)
                    }
                    DateSelection -> {
                        binding.calendarView.isVisible = true
                        binding.savedStatisticTv.isVisible = false
                    }
                    is StatisticsReady -> {
                        binding.calendarView.isVisible = false
                        binding.savedStatisticTv.isVisible = true
                        showSavedStatistic(state.statistics)
                    }
                    LoggingStarted -> {
                        startService()
                    }
                    LoggingStopped -> {
                        stopService()
                    }
                }
            }
        }
    }

    private fun calendarViewSetting(minDate: Long) {
        binding.calendarView.firstDayOfWeek = MONDAY
        binding.calendarView.minDate = minDate
        binding.calendarView.maxDate = System.currentTimeMillis()
    }

    private fun observeServiceState() {
        lifecycleScope.launchWhenResumed {
            dataStore.isServiceRunning.collect { isRunning ->
                serviceIsRunning = isRunning
            }
        }
    }

    private fun startService() {
        if (!serviceIsRunning) {
            val serviceIntent = Intent(activity, FriendsLoggingService::class.java)
            context?.let { ContextCompat.startForegroundService(it, serviceIntent) }
        } else Toast.makeText(context, getString(R.string.service_is_running), Toast.LENGTH_SHORT)
            .show()
    }

    private fun stopService() {
        if (serviceIsRunning) {
            val serviceIntent = Intent(activity, FriendsLoggingService::class.java)
            activity?.stopService(serviceIntent)
            Toast.makeText(context, getString(R.string.service_stopped), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showFriendInfo(friend: Friend) {
        binding.firstNameTv.text = friend.firstName
        binding.lastNameTv.text = friend.lastName
        binding.switch1.isChecked = friend.logging
        Glide.with(this)
            .load(friend.photo)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.friendPhoto)
    }

    private fun showSavedStatistic(statistics: List<String>) {
        binding.savedStatisticTv.text =
            if (statistics.isEmpty()) getString(R.string.dont_saved) else EMPTY_STRING
        statistics.forEach {
            binding.savedStatisticTv.append(it)
        }
    }

    private fun setButtonText(text: String) {
        binding.selectDateBtn.text = text
    }

}