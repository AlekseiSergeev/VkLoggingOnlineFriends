package com.example.vkloggingonlinefriends.data.cache.mappers

import com.example.vkloggingonlinefriends.data.cache.model.FriendEntity
import com.example.vkloggingonlinefriends.domain.model.Friend
import com.example.vkloggingonlinefriends.domain.util.DomainMapper

class FriendEntityMapper : DomainMapper<FriendEntity, Friend> {

    override fun mapToDomainModel(model: FriendEntity): Friend =
        with(model) {
        Friend(
            id = id,
            firstName = firstName,
            lastName = lastName,
            photo = photo,
            online = online,
            logging = logging
        )
    }

    override fun mapFromDomainModel(domainModel: Friend): FriendEntity =
        with(domainModel) {
        FriendEntity(
            id = id,
            firstName = firstName,
            lastName = lastName,
            photo = photo,
            online = online,
            logging = logging
        )
    }

    fun toDomainList(initial: List<FriendEntity>): List<Friend> {
        return initial.map(this::mapToDomainModel)
    }

    fun fromDomainList(initial: List<Friend>): List<FriendEntity> {
        return initial.map(this::mapFromDomainModel)
    }

}