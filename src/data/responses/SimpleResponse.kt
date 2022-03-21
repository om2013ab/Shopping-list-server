package com.omarahmed.data.responses

data class SimpleResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)
