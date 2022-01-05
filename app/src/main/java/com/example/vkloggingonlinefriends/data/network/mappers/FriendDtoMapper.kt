package com.example.vkloggingonlinefriends.data.network.mappers

import com.example.vkloggingonlinefriends.data.network.model.friend.FriendDto
import com.example.vkloggingonlinefriends.domain.model.Friend
import com.example.vkloggingonlinefriends.domain.util.DomainMapper

class FriendDtoMapper : DomainMapper<FriendDto, Friend> {

    override fun mapToDomainModel(model: FriendDto): Friend {
        return Friend(
            id = model.id,
            firstName = model.firstName,
            lastName = model.lastName,
            photo = model.photo,
            online = model.online
        )
    }

    override fun mapFromDomainModel(domainModel: Friend): FriendDto {
        return FriendDto(
            id = domainModel.id,
            firstName = domainModel.firstName,
            lastName = domainModel.lastName,
            photo = domainModel.photo,
            online = domainModel.online
        )
    }

    fun toDomainList(initial: List<FriendDto>): List<Friend> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Friend>): List<FriendDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}