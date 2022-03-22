package com.omarahmed.data.repository.user

import com.omarahmed.data.models.User
import com.omarahmed.data.requests.UpdateUserRequest

interface UserRepository {

    suspend fun createUser(user: User): Boolean

    suspend fun getUserByEmail(email: String): User?

    suspend fun doesPasswordAndEmailMatch(email: String, passwordToCheck: String): Boolean

    suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean

//    suspend fun updateUserToken(id: String, token: String): Boolean
}