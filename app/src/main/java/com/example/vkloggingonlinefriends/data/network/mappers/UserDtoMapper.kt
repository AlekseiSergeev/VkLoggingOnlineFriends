package com.example.vkloggingonlinefriends.data.network.mappers

import com.example.vkloggingonlinefriends.data.network.model.user.UserDto
import com.example.vkloggingonlinefriends.domain.model.User
import com.example.vkloggingonlinefriends.domain.util.DomainMapper

class UserDtoMapper : DomainMapper<UserDto?, User> {
    override fun mapToDomainModel(model: UserDto?): User {
        return User(
            firstName = model?.firstName,
            lastName = model?.lastName,
            photo = model?.photo
        )
    }

    override fun mapFromDomainModel(domainModel: User): UserDto {
        return UserDto(
            firstName = domainModel.firstName,
            lastName = domainModel.lastName,
            photo = domainModel.photo
        )
    }

}