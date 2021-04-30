package com.flong.kotlin.utils.dto

class R<T>(val code: Int, val message: String, val data: T, var timestamp: Long) {
    constructor(data: T) : this(200, "success", data, System.currentTimeMillis())
}