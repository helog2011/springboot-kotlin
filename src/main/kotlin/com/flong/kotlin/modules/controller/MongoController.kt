package com.flong.kotlin.modules.controller

import com.flong.kotlin.modules.entity.UserMongo
import com.flong.kotlin.modules.repository.UserMongoRepository
import com.flong.kotlin.utils.dto.R
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mongo")
class MongoController {
    @Autowired
    lateinit var userMongoRepository: UserMongoRepository

    @GetMapping("/save")
    fun save(@RequestParam("name") userName: String): R<String> {
        userMongoRepository.save(UserMongo(1, "helog", 20))
        userMongoRepository.findByUsername("helog");
        return R("create ${userName} success")
    }
}