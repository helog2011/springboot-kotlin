package com.flong.kotlin.modules.service

import com.flong.kotlin.modules.entity.UserExt

interface IUserExtService {
    fun getUser(username: String): UserExt

    fun createUser(username: String,password: String)

}