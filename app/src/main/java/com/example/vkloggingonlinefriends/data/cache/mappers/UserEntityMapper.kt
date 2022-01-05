package com.example.vkloggingonlinefriends.data.cache.mappers

import com.example.vkloggingonlinefriends.data.cache.model.UserEntity
import com.example.vkloggingonlinefriends.domain.model.User
import com.example.vkloggingonlinefriends.domain.util.DomainMapper

class UserEntityMapper : DomainMapper<UserEntity, User> {

    override fun mapToDomainModel(model: UserEntity): User {
        return User(
            firstName = model.firstName,
            lastName = model.lastName,
            photo = model.photo
        )
    }

    override fun mapFromDomainModel(domainModel: User): UserEntity {
        return UserEntity(
            firstName = domainModel.firstName,
            lastName = domainModel.lastName,
            photo = domainModel.photo
        )
    }

}