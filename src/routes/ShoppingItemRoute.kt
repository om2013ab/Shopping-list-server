package com.omarahmed.routes

import com.google.gson.Gson
import com.omarahmed.data.models.ShoppingItem
import com.omarahmed.data.models.User
import com.omarahmed.data.requests.AddItemRequest
import com.omarahmed.data.requests.UpdateItemRequest
import com.omarahmed.data.responses.SimpleResponse
import com.omarahmed.email
import com.omarahmed.services.ShoppingItemService
import com.omarahmed.services.UserService
import com.omarahmed.util.Constants
import com.omarahmed.util.Constants.BASE_URL
import com.omarahmed.util.Constants.ITEMS_PICTURE_PATH
import com.omarahmed.util.QueryParams
import com.omarahmed.util.saveFile
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.NoContent
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import java.io.File
import java.util.*

fun Routing.addNewItemRoute(
    shoppingItemService: ShoppingItemService,
    userService: UserService
) {
    authenticate {
        post("/api/items/new_item") {
            val multiPart = call.receiveMultipart()
            var itemName: String? = null
            var fileName: String? = null

            multiPart.forEachPart { partData ->
                when (partData) {
                    is PartData.FormItem -> {
                        itemName = partData.value
                    }
                    is PartData.FileItem -> {
                        val fileBytes = partData.streamProvider().readBytes()
                        val fileExtension = partData.originalFileName?.takeLastWhile { it != '.' }
                        fileName = "${UUID.randomUUID()}.$fileExtension"
                        val folder = File(ITEMS_PICTURE_PATH)
                        folder.mkdir()
                        File("$ITEMS_PICTURE_PATH$fileName").writeBytes(fileBytes)
                    }
                    else -> Unit
                }
                partData.dispose
            }

            itemName?.let {
                val itemImageUrl = "${BASE_URL}items_pictures/$fileName"
                val email = call.principal<JWTPrincipal>()?.email ?: ""
                val userId = userService.getUserByEmail(email)?.id ?: ""
                if (userService.doesEmailBelongToUserId(email, userId)) {
                    val addedItemAcknowledge = shoppingItemService.addNewItem(
                        userId = userId,
                        itemName = it,
                        itemImageUrl = itemImageUrl
                    )
                    if (addedItemAcknowledge) {
                        call.respond(OK, SimpleResponse<Unit>(true, "Successfully added new item!"))
                    } else {
                        File("$ITEMS_PICTURE_PATH$fileName").delete()
                        call.respond(InternalServerError)
                    }
                } else {
                    call.respond(Unauthorized, "You are not who are")
                }

            } ?: kotlin.run {
                call.respond(BadRequest)
                return@post
            }
        }
    }

}

fun Route.getAllItemsRoute(
    shoppingItemService: ShoppingItemService,
    userService: UserService
) {
    authenticate {
        get("/api/items/get") {
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toInt() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toInt() ?: Constants.DEFAULT_PAGE_SIZE
            val email = call.principal<JWTPrincipal>()?.email ?: ""
            val userId = userService.getUserByEmail(email)?.id ?: ""
            val items = shoppingItemService.getItemsByUserId(userId,page, pageSize)
            call.respond(OK, items)
        }
    }
}

fun Route.updateItemRoute(shoppingItemService: ShoppingItemService) {
    authenticate {
        put("/api/item/update") {
            val itemId = call.parameters[QueryParams.PARAM_ITEM_ID] ?: kotlin.run {
                call.respond(NotFound)
                return@put
            }
            val updatedItem = call.receiveOrNull<UpdateItemRequest>()
            if (updatedItem != null) {
                val isUpdateAcknowledge = shoppingItemService.updateItem(itemId, updatedItem)
                if (isUpdateAcknowledge) {
                    call.respond(OK, SimpleResponse<Unit>(true, "Successfully updated the item"))
                } else {
                    call.respond(InternalServerError)
                }
            }
        }
    }


}

fun Route.deleteItemRoute(shoppingItemService: ShoppingItemService) {
    authenticate {
        delete("/api/item/remove") {
            val itemId = call.parameters[QueryParams.PARAM_ITEM_ID] ?: kotlin.run {
                call.respond(NotFound)
                return@delete
            }
            val deleteAcknowledge = shoppingItemService.deleteItem(itemId)
            if (deleteAcknowledge) {
                call.respond(OK, SimpleResponse<Unit>(true, "Successfully deleted the item: $itemId"))
            } else {
                call.respond(OK, SimpleResponse<Unit>(false, "Something went wrong"))
            }
        }
    }
}

fun Route.searchForItemRoute(shoppingItemService: ShoppingItemService) {
    authenticate {
        get("/api/items/search") {
            val query = call.parameters[QueryParams.PARAM_SEARCH_QUERY]
            query?.let {
                val searchResult = shoppingItemService.searchForItem(it)
                call.respond(OK, searchResult)
            }

        }
    }

}