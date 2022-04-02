package com.omarahmed

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.omarahmed.di.mainModule
import com.omarahmed.routes.*
import com.omarahmed.services.ShoppingItemService
import com.omarahmed.services.UserService
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.content.*
import io.ktor.http.content.*
import io.ktor.features.*
import org.slf4j.event.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.gson.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.Koin.Feature
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module() {
    val jwtSecret = environment.config.property("jwt.secret").getString()
    val jwtIssuer = environment.config.property("jwt.issuer").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtRealm = environment.config.property("jwt.realm").getString()

    val userService: UserService by inject()


    install(Koin){
        modules(mainModule)
    }

    install(CallLogging)

    install(DefaultHeaders)

    install(Authentication) {
        jwt {
            realm = jwtRealm
            verifier(
                JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtIssuer)
                    .build()
            )
            validate { jwtCredential ->
                val email = jwtCredential.payload.getClaim("email").asString()
                val currentUser = userService.getUserByEmail(email)
                if (currentUser != null){
                    JWTPrincipal(jwtCredential.payload)
                }
                else null
            }
        }
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    install(Routing){
        val shoppingItemService: ShoppingItemService by inject()

        authenticate()
        createUserRoute(userService)
        loginUser(userService,jwtAudience,jwtIssuer, jwtSecret)

        addNewItemRoute(shoppingItemService, userService)
        getAllItemsRoute(shoppingItemService, userService)
        updateItemRoute(shoppingItemService)
        updateAllItemsRoute(shoppingItemService)
        deleteItemRoute(shoppingItemService)
        searchForItemRoute(shoppingItemService)
        static {
            resources("static")
        }
    }
}

val JWTPrincipal.email: String?
    get() = payload.claims["email"]?.asString()