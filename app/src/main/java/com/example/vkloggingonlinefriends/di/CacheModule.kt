package com.example.vkloggingonlinefriends.di

import android.content.Context
import androidx.room.Room
import com.example.vkloggingonlinefriends.data.cache.database.VkDao
import com.example.vkloggingonlinefriends.data.cache.database.VkDatabase
import com.example.vkloggingonlinefriends.data.cache.mappers.FriendEntityMapper
import com.example.vkloggingonlinefriends.data.cache.mappers.UserEntityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideVkDatabase(@ApplicationContext context: Context): VkDatabase {
        return Room.databaseBuilder(
            context,
            VkDatabase::class.java,
            VkDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideVkDao(vkDatabase: VkDatabase): VkDao {
        return vkDatabase.vkDao()
    }

    @Singleton
    @Provides
    fun provideFriendMapper(): FriendEntityMapper {
        return FriendEntityMapper()
    }

    @Singleton
    @Provides
    fun provideUserMapper(): UserEntityMapper {
        return UserEntityMapper()
    }
}