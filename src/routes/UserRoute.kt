package com.omarahmed.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.omarahmed.data.requests.CreateAccountRequest
import com.omarahmed.data.requests.LoginRequest
import com.omarahmed.data.responses.AuthResponse
import com.omarahmed.data.responses.SimpleResponse
import com.omarahmed.services.UserService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get
import java.util.*
import kotlin.math.log


fun Route.createUserRoute(userService: UserService) {

    post("/api/user/create") {
        val userRequest = try {
            call.receive<CreateAccountRequest>()
        } catch (e: ContentTransformationException) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        if (userService.doesUserEmailExist(userRequest.email)) {
            call.respond(HttpStatusCode.OK, SimpleResponse<Unit>(false, "A user with this email is already exist"))
            return@post
        }
        val createUserAcknowledge = userService.createUser(userRequest)
        if (createUserAcknowledge) {
            call.respond(HttpStatusCode.OK, SimpleResponse<Unit>(true, "Successfully created your account"))
        } else {
            print(" issueMessage: ${HttpStatusCode.InternalServerError.description}")
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}

fun Route.loginUser(
    userService: UserService,
    jwtAudience: String,
    jwtIssuer: String,
    jwtSecret: String
) {
    post("/api/user/login") {
        val loginRequest = call.receiveOrNull<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        if (userService.getUserByEmail(loginRequest.email) == null) {
            call.respond(HttpStatusCode.OK, SimpleResponse<Unit>(false, "User not found"))
            return@post
        }
        if (userService.doesPasswordAndEmailMatch(loginRequest)) {
            val expiresAt = 1000L * 60L * 60L * 24L * 365L
            val token = JWT.create()
                .withAudience(jwtAudience)
                .withIssuer(jwtIssuer)
                .withClaim("email", loginRequest.email)
                .withExpiresAt(Date(System.currentTimeMillis() + expiresAt))
                .sign(Algorithm.HMAC256(jwtSecret))
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Successfully logged in", AuthResponse(token)))
//            if (user != null){
//                userService.updateUserToken(user.id,token)
//            }
        } else {
            call.respond(HttpStatusCode.OK, SimpleResponse<Unit>(false, "Email or password is not correct"))
        }

    }
}

fun Route.authenticate() {
    authenticate {
        get("/api/user/authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}