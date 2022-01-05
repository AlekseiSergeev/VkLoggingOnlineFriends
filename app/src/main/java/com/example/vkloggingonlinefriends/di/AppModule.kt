package com.example.vkloggingonlinefriends.di

import android.content.Context
import com.example.vkloggingonlinefriends.datastore.AppDataStore
import com.example.vkloggingonlinefriends.presentation.VkApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): VkApplication {
        return app as VkApplication
    }

    @Singleton
    @Provides
    fun provideDataStore(app: VkApplication): AppDataStore {
        return AppDataStore(app)
    }
}