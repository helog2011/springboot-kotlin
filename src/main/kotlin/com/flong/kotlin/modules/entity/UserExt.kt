package com.flong.kotlin.modules.entity

import java.io.Serializable

class UserExt : Serializable {
    var userName: String? = null;
    var password: String? = null;

    constructor()
    constructor(userName: String?, password: String?) {
        this.userName = userName
        this.password = password
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserExt) return false

        if (userName != other.userName) return false
        if (password != other.password) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userName?.hashCode() ?: 0
        result = 31 * result + (password?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "UserExt(userName=$userName, password=$password)"
    }


}
