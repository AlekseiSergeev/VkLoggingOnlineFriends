package com.example.vkloggingonlinefriends.data.network.mappers

import com.example.vkloggingonlinefriends.data.network.model.user.UserDto
import com.example.vkloggingonlinefriends.domain.model.User
import com.example.vkloggingonlinefriends.domain.util.DomainMapper

class UserDtoMapper : DomainMapper<UserDto, User> {

    override fun mapToDomainModel(model: UserDto): User =
        with(model) {
            User(
                firstName = firstName.orEmpty(),
                lastName = lastName.orEmpty(),
                photo = photo.orEmpty()
            )
        }

    override fun mapFromDomainModel(domainModel: User): UserDto =
        with(domainModel) {
            UserDto(
                firstName = firstName,
                lastName = lastName,
                photo = photo
            )
        }

}