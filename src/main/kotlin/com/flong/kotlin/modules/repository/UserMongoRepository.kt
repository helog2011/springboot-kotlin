package com.flong.kotlin.modules.repository

import com.flong.kotlin.modules.entity.UserMongo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface  UserMongoRepository : MongoRepository<UserMongo, Long> {
    fun findByUsername(username: String): UserMongo
}