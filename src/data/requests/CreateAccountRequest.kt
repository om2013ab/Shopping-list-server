package com.omarahmed.data.requests

data class CreateAccountRequest(
    val name: String,
    val email: String,
    val password: String
)
