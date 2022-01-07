package com.example.vkloggingonlinefriends.di

import com.example.vkloggingonlinefriends.data.network.mappers.FriendDtoMapper
import com.example.vkloggingonlinefriends.data.network.mappers.UserDtoMapper
import com.example.vkloggingonlinefriends.data.network.api.VkApi
import com.example.vkloggingonlinefriends.data.cache.datastore.AppDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideVkApi(): VkApi {
        return Retrofit.Builder()
            .baseUrl("https://api.vk.com/method/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VkApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserMapper(): UserDtoMapper {
        return UserDtoMapper()
    }

    @Singleton
    @Provides
    fun provideFriendMapper(): FriendDtoMapper {
        return FriendDtoMapper()
    }

    @Singleton
    @Provides
    @Named("access_token")
    fun provideAccessToken(dataStore: AppDataStore): String {
        return dataStore.vkToken.value
    }
}