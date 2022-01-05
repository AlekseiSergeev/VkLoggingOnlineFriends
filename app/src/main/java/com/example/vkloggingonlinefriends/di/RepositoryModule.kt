package com.example.vkloggingonlinefriends.di

import com.example.vkloggingonlinefriends.data.cache.database.VkDao
import com.example.vkloggingonlinefriends.data.cache.datasource.VkCacheDataSourceImpl
import com.example.vkloggingonlinefriends.data.cache.mappers.FriendEntityMapper
import com.example.vkloggingonlinefriends.data.cache.mappers.UserEntityMapper
import com.example.vkloggingonlinefriends.data.network.datasource.VkNetworkDataSourceImpl
import com.example.vkloggingonlinefriends.data.network.mappers.FriendDtoMapper
import com.example.vkloggingonlinefriends.data.network.mappers.UserDtoMapper
import com.example.vkloggingonlinefriends.data.network.service.VkService
import com.example.vkloggingonlinefriends.data.repository.VkCacheDataSource
import com.example.vkloggingonlinefriends.data.repository.VkNetworkDataSource
import com.example.vkloggingonlinefriends.data.repository.VkRepositoryImpl
import com.example.vkloggingonlinefriends.datastore.AppDataStore
import com.example.vkloggingonlinefriends.domain.repository.VkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideVkNetworkDataSource(
        vkService: VkService,
        userDtoMapper: UserDtoMapper,
        friendDtoMapper: FriendDtoMapper,
        dataStore: AppDataStore
    ): VkNetworkDataSource {
        return VkNetworkDataSourceImpl(vkService, userDtoMapper, friendDtoMapper, dataStore)
    }

    @Singleton
    @Provides
    fun provideVkCacheDataSource(
        vkDao: VkDao,
        userEntityMapper: UserEntityMapper,
        friendEntityMapper: FriendEntityMapper
    ): VkCacheDataSource {
        return VkCacheDataSourceImpl(vkDao, userEntityMapper, friendEntityMapper)
    }

    @Singleton
    @Provides
    fun provideVkRepository(
        networkDataSource: VkNetworkDataSource,
        cacheDataSource: VkCacheDataSource
    ): VkRepository {
        return VkRepositoryImpl(networkDataSource, cacheDataSource)
    }
}