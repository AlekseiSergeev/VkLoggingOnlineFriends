package com.example.vkloggingonlinefriends.data.cache.mappers

import com.example.vkloggingonlinefriends.data.cache.model.UserEntity
import com.example.vkloggingonlinefriends.data.network.model.user.UserDto
import com.example.vkloggingonlinefriends.domain.model.User
import com.example.vkloggingonlinefriends.domain.util.DomainMapper

class UserEntityMapper : DomainMapper<UserEntity, User> {

    override fun mapToDomainModel(model: UserEntity): User  =
        with(model) {
            User(
                firstName = firstName,
                lastName = lastName,
                photo = photo
            )
        }

    override fun mapFromDomainModel(domainModel: User): UserEntity =
        with(domainModel) {
            UserEntity(
                firstName = firstName,
                lastName = lastName,
                photo = photo
            )
        }

}