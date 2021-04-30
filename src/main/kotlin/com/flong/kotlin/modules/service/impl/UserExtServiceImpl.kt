package com.flong.kotlin.modules.service.impl

import com.flong.kotlin.modules.entity.UserExt
import com.flong.kotlin.modules.service.IUserExtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class UserExtServiceImpl : IUserExtService {
    @Autowired
    lateinit var redisTemplate: RedisTemplate<Any, Any>

    override fun getUser(username: String): UserExt {

        var user = redisTemplate.opsForValue().get("user_${username}")

        if (user == null) {

            user = UserExt("default","000000")
        }

        return user as UserExt
    }

    override fun createUser(username: String, password: String) {

        redisTemplate.opsForValue().set("user_${username}", UserExt(username, password))
    }
}