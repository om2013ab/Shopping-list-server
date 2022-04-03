package com.omarahmed.services

import com.omarahmed.data.models.User
import com.omarahmed.data.repository.user.UserRepository
import com.omarahmed.data.requests.CreateAccountRequest
import com.omarahmed.data.requests.LoginRequest
import com.omarahmed.data.requests.UpdateUserRequest
import com.omarahmed.util.hashPassword

class UserService(
    private val userRepository: UserRepository
) {
    suspend fun createUser(request: CreateAccountRequest): Boolean {
        return userRepository.createUser(
            User(
                name = request.name,
                email = request.email,
                password = hashPassword(request.password)
            )
        )
    }

    suspend fun doesUserEmailExist(userEmail: String): Boolean {
        return userRepository.getUserByEmail(userEmail) != null
    }

    suspend fun doesPasswordAndEmailMatch(loginRequest: LoginRequest): Boolean {
        return userRepository.doesPasswordAndEmailMatch(loginRequest.email, loginRequest.password)
    }

    suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean {
        return userRepository.doesEmailBelongToUserId(email, userId)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userRepository.getUserByEmail(email)
    }

//    suspend fun updateUserToken(id: String, token: String): Boolean{
//        return userRepository.updateUserToken(id,token)
//    }
}