package com.omarahmed.data.repository.user

import com.omarahmed.data.models.User
import com.omarahmed.data.requests.UpdateUserRequest
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class UserRepositoryImpl(
    db: CoroutineDatabase
): UserRepository {

    private val users = db.getCollection<User>()

    override suspend fun createUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }

    override suspend fun getUserByEmail(email: String): User? {
         return users.findOne(User::email eq email)
    }

    override suspend fun doesPasswordAndEmailMatch(email: String, passwordToCheck: String): Boolean {
        val user = getUserByEmail(email)
        return user?.password == passwordToCheck
    }

//    override suspend fun updateUserToken(id: String, token: String): Boolean {
//        return users.updateOne(
//            User::id eq id,
//            setValue(User::token, token)
//        ).wasAcknowledged()
//    }
}