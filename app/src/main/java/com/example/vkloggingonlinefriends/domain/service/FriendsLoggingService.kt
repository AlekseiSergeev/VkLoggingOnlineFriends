package com.example.vkloggingonlinefriends.domain.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.vkloggingonlinefriends.R
import com.example.vkloggingonlinefriends.data.cache.model.LoggedFriends
import com.example.vkloggingonlinefriends.data.cache.model.OnlineTimeStatistic
import com.example.vkloggingonlinefriends.datastore.AppDataStore
import com.example.vkloggingonlinefriends.domain.repository.VkRepository
import com.example.vkloggingonlinefriends.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class FriendsLoggingService : Service() {

    val CHANNEL_ID = "friendsLoggingServiceChannel"
    private val job = Job()
    private var serviceIsRunning = false

    private val loggedFriends: MutableList<LoggedFriends> = mutableListOf()
    private val onlineFriendsId: MutableList<Int> = mutableListOf()

    @Inject
    lateinit var dataStore: AppDataStore

    @Inject
    lateinit var repository: VkRepository

    override fun onCreate() {
        super.onCreate()
        serviceIsRunning = true
        dataStore.setServiceRunning(serviceIsRunning)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("VK friends service")
            .setContentText("logging active")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)

        CoroutineScope(Dispatchers.IO + job).launch {
            var currentDate = System.currentTimeMillis()
            while (job.isActive) {
                loggedFriends.addAll(repository.getLoggedFriends())
                repository.getOnlineFriendsId()
                    ?.let { onlineFriendsId.addAll(it) }
                loggedFriends.forEach { it ->
                    it.currentOnlineStatus = 0

                    for (id in onlineFriendsId) {
                        if (id == it.id) it.currentOnlineStatus = 1
                    }

                    currentDate = System.currentTimeMillis()
                    if (it.currentOnlineStatus == 1 && it.previousOnlineStatus == 0) {
                        repository.insertOnlineTimeStatistic(
                            OnlineTimeStatistic(
                                friendId = it.id,
                                currentDate = currentDate,
                                description = "Начало сеанса:"
                            )
                        )
                    }
                    if (it.currentOnlineStatus == 0 && it.previousOnlineStatus == 1) {
                        repository.insertOnlineTimeStatistic(
                            OnlineTimeStatistic(
                                friendId = it.id,
                                currentDate = currentDate,
                                description = "Конец сеанса:"
                            )
                        )
                    }
                    repository.updateLoggedFriendPreviousOnline(it.id, it.currentOnlineStatus)
                }

                loggedFriends.clear()
                onlineFriendsId.clear()
                delay(60000)
            }
        }

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        serviceIsRunning = false
        dataStore.setServiceRunning(serviceIsRunning)
    }
}