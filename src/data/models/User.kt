package com.omarahmed.data.models

import io.ktor.auth.*
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val name: String,
    val email: String,
    val password: String,
    @BsonId
    val id: String = ObjectId().toString()
): Principal
