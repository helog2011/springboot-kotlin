package com.flong.kotlin.modules.controller

import com.flong.kotlin.modules.entity.UserExt
import com.flong.kotlin.modules.service.IUserExtService
import com.flong.kotlin.utils.dto.R
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.net.http.HttpResponse
@Api(value = "学生信息相关接口", tags = ["学生"])
@RestController
@RequestMapping("/user")
class UserExtController {
    @Autowired
    lateinit var userService: IUserExtService

    @ApiOperation(value = "获取学生列表")
    @GetMapping("/getUser")
    fun getUser(@RequestParam("name") userName: String): R<UserExt> {
        return R(userService.getUser(userName))
    }

    @GetMapping("/createUser")
    fun createUser(@RequestParam("name") userName: String,
                   @RequestParam("password") password: String): R<String> {
        userService.createUser(userName, password)
        return R("create ${userName} success")
    }

    @PostMapping("/createUser")
    @ApiImplicitParam(name = "userExt", value = "学生详细实体student", required = true, dataType = "UserExt")
    fun createUser(@RequestBody(required = true) userExt: UserExt): R<String> {
        userService.createUser(userExt.userName!!, userExt.password!!)
        return R("create ${userExt.userName} success")
    }
}