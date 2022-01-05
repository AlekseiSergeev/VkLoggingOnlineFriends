package com.example.vkloggingonlinefriends.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.vkloggingonlinefriends.presentation.VkApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataStore
@Inject constructor(app: VkApplication) {
    private val context = app.applicationContext
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "VkToken")
    private val datastore: DataStore<Preferences> = context.dataStore

    private val scope = CoroutineScope(Dispatchers.Main)
    private val _vkToken = MutableStateFlow("")
    val vkToken = _vkToken.asStateFlow()

    val isServiceRunning: Flow<Boolean> = datastore.data.map {
        it[FRIENDS_SERVICE] ?: false
    }

    init {
        observeDataStore()
    }

    fun observeDataStore() {
        datastore.data.onEach { preferences ->
            _vkToken.value = preferences[VK_TOKEN] ?: ""
        }.launchIn(scope)
    }

    fun setNewToken(token: String) {
        scope.launch {
            datastore.edit { preferences ->
                preferences[VK_TOKEN] = token
                _vkToken.value = preferences[VK_TOKEN] ?: ""
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

    fun getToken(): String {
        return _vkToken.value
    }

    companion object {
        private val VK_TOKEN = stringPreferencesKey("vk_token")
        private val FRIENDS_SERVICE = booleanPreferencesKey("friends_service")
    }
}