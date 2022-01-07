package com.example.vkloggingonlinefriends.domain.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.vkloggingonlinefriends.R
import com.example.vkloggingonlinefriends.data.cache.model.LoggedFriends
import com.example.vkloggingonlinefriends.data.cache.model.OnlineTimeStatistic
import com.example.vkloggingonlinefriends.data.cache.datastore.AppDataStore
import com.example.vkloggingonlinefriends.domain.repository.VkRepository
import com.example.vkloggingonlinefriends.presentation.MainActivity
import com.example.vkloggingonlinefriends.utils.CHANNEL_ID
import com.example.vkloggingonlinefriends.utils.VK_OFFLINE
import com.example.vkloggingonlinefriends.utils.VK_ONLINE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class FriendsLoggingService : Service() {

    companion object {
        const val SERVICE_ID = 111
        const val REQUEST_CODE = 0
        const val ONE_MINUTE = 60000L
    }

    private val job = Job()
    private val startDescription: String by lazy { getString(R.string.start_description) }
    private val endDescription: String by lazy { getString(R.string.end_description) }
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
            this, REQUEST_CODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(SERVICE_ID, notification)

        CoroutineScope(Dispatchers.IO + job).launch {
            var currentDate = System.currentTimeMillis()
            while (job.isActive) {
                loggedFriends.addAll(repository.getLoggedFriends())
                repository.getOnlineFriendsId()
                    ?.let(onlineFriendsId::addAll)

                loggedFriends.forEach { loggedFriend ->
                    var updateFriend = loggedFriend.copy(currentOnlineStatus = VK_OFFLINE)

                    for (id in onlineFriendsId) {
                        if (id == updateFriend.id) updateFriend =
                            updateFriend.copy(currentOnlineStatus = VK_ONLINE)
                    }

                    currentDate = System.currentTimeMillis()
                    if (updateFriend.currentOnlineStatus == VK_ONLINE &&
                        updateFriend.previousOnlineStatus == VK_OFFLINE
                    ) {
                        repository.insertOnlineTimeStatistic(
                            OnlineTimeStatistic(
                                friendId = updateFriend.id,
                                currentDate = currentDate,
                                description = startDescription
                            )
                        )
                    }

                    if (updateFriend.currentOnlineStatus == VK_OFFLINE &&
                        updateFriend.previousOnlineStatus == VK_ONLINE
                    ) {
                        repository.insertOnlineTimeStatistic(
                            OnlineTimeStatistic(
                                friendId = updateFriend.id,
                                currentDate = currentDate,
                                description = endDescription
                            )
                        )
                    }
                    repository.updateLoggedFriendPreviousOnline(
                        updateFriend.id,
                        updateFriend.currentOnlineStatus
                    )
                }

                loggedFriends.clear()
                onlineFriendsId.clear()
                delay(ONE_MINUTE)
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