package com.example.vkloggingonlinefriends.data.network.mappers

import com.example.vkloggingonlinefriends.data.network.model.friend.FriendDto
import com.example.vkloggingonlinefriends.domain.model.Friend
import com.example.vkloggingonlinefriends.domain.util.DomainMapper
import com.example.vkloggingonlinefriends.utils.EMPTY_ID
import com.example.vkloggingonlinefriends.utils.VK_OFFLINE

class FriendDtoMapper : DomainMapper<FriendDto, Friend> {

    override fun mapToDomainModel(model: FriendDto): Friend =
        with(model) {
            Friend(
                id = id ?: EMPTY_ID,
                firstName = firstName.orEmpty(),
                lastName = lastName.orEmpty(),
                photo = photo.orEmpty(),
                online = online ?: VK_OFFLINE
            )
        }

    override fun mapFromDomainModel(domainModel: Friend): FriendDto =
        with(domainModel) {
            FriendDto(
                id = id,
                firstName = firstName,
                lastName = lastName,
                photo = photo,
                online = online
            )
        }

    fun toDomainList(initial: List<FriendDto>): List<Friend> {
        return initial.map(this::mapToDomainModel)
    }

    fun fromDomainList(initial: List<Friend>): List<FriendDto> {
        return initial.map(this::mapFromDomainModel)
    }

}