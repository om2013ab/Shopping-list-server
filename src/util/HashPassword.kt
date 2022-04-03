package com.omarahmed.util

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private val hashPasswordSecretKey = System.getenv("PASSWORD_SECRET").toByteArray()
private val algorithm = "HmacSHA1"

private val hmacKey = SecretKeySpec(hashPasswordSecretKey, algorithm)

fun hashPassword(password: String): String {
    val hmac = Mac.getInstance(algorithm)
    hmac.init(hmacKey)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}