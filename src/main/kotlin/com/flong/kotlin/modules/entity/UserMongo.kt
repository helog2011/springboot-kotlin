package com.flong.kotlin.modules.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class UserMongo (@Id var id: Long? = -1, var username: String = "", val age: Int? = 0)