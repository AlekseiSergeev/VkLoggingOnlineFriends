package com.example.vkloggingonlinefriends.data.cache.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.vkloggingonlinefriends.presentation.VkApplication
import com.example.vkloggingonlinefriends.utils.EMPTY_STRING
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppDataStore
@Inject constructor(app: VkApplication) {

    companion object {
        private const val DATASTORE_NAME = "VkToken"
        private val VK_TOKEN = stringPreferencesKey("vk_token")
        private val FRIENDS_SERVICE = booleanPreferencesKey("friends_service")
    }

    private val context = app.applicationContext
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)
    private val datastore: DataStore<Preferences> = context.dataStore

    private val scope = CoroutineScope(Dispatchers.Main)
    private val _vkToken = MutableStateFlow(EMPTY_STRING)
    val vkToken = _vkToken.asStateFlow()

    val isServiceRunning: Flow<Boolean> = datastore.data.map {
        it[FRIENDS_SERVICE] ?: false
    }

    init {
        observeDataStore()
    }

    fun observeDataStore() {
        datastore.data.onEach { preferences ->
            _vkToken.value = preferences[VK_TOKEN] ?: EMPTY_STRING
        }.launchIn(scope)
    }

    suspend fun setNewToken(token: String) {
        withContext(Dispatchers.Main) {
            datastore.edit { preferences ->
                preferences[VK_TOKEN] = token
                _vkToken.value = preferences[VK_TOKEN] ?: EMPTY_STRING
            }
        }
    }

    fun setServiceRunning(value: Boolean) {
        scope.launch {
            datastore.edit { preferences ->
                preferences[FRIENDS_SERVICE] = value
            }
        }
    }

}