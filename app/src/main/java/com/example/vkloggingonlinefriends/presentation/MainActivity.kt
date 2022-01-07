package com.example.vkloggingonlinefriends.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vkloggingonlinefriends.R
import com.example.vkloggingonlinefriends.data.cache.datastore.AppDataStore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var dataStore: AppDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataStore.observeDataStore()
    }
}