package com.example.vkloggingonlinefriends.data.cache.mappers

import com.example.vkloggingonlinefriends.data.cache.model.FriendEntity
import com.example.vkloggingonlinefriends.domain.model.Friend
import com.example.vkloggingonlinefriends.domain.util.DomainMapper

class FriendEntityMapper : DomainMapper<FriendEntity, Friend> {

    override fun mapToDomainModel(model: FriendEntity): Friend {
        return Friend(
            id = model.id,
            firstName = model.firstName,
            lastName = model.lastName,
            photo = model.photo,
            online = model.online,
            logging = model.logging,
            previousOnlineStatus = model.previousOnlineStatus
        )
    }

    override fun mapFromDomainModel(domainModel: Friend): FriendEntity {
        return FriendEntity(
            id = domainModel.id,
            firstName = domainModel.firstName,
            lastName = domainModel.lastName,
            photo = domainModel.photo,
            online = domainModel.online,
            logging = domainModel.logging,
            previousOnlineStatus = domainModel.previousOnlineStatus
        )
    }

    fun toDomainList(initial: List<FriendEntity>): List<Friend> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Friend>): List<FriendEntity> {
        return initial.map { mapFromDomainModel(it) }
    }
}