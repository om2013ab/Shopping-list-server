package com.omarahmed

import com.omarahmed.di.mainModule
import com.omarahmed.routes.*
import com.omarahmed.services.ShoppingItemService
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
import io.ktor.gson.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.Koin.Feature
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module() {

    install(Koin){
        modules(mainModule)
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }

    install(Authentication) {
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    install(Routing){
        val shoppingItemService: ShoppingItemService by inject()
        addNewItemRoute(shoppingItemService)
        getAllItemsRoute(shoppingItemService)
        updateItemRoute(shoppingItemService)
        deleteItemRoute(shoppingItemService)
        searchForItemRoute(shoppingItemService)
        static {
            resources("static")
        }
    }
}

