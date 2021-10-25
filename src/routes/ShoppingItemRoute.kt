package com.omarahmed.routes

import com.google.gson.Gson
import com.omarahmed.data.requests.AddItemRequest
import com.omarahmed.data.requests.UpdateItemRequest
import com.omarahmed.data.responses.SimpleResponse
import com.omarahmed.services.ShoppingItemService
import com.omarahmed.util.Constants
import com.omarahmed.util.Constants.BASE_URL
import com.omarahmed.util.Constants.ITEMS_PICTURE_PATH
import com.omarahmed.util.QueryParams
import com.omarahmed.util.saveFile
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import java.io.File
import java.util.*

fun Routing.addNewItemRoute(
    shoppingItemService: ShoppingItemService
) {
    val gson: Gson by inject()
    post("/api/items/new_item") {
        val multiPart = call.receiveMultipart()
        var addItemRequest: AddItemRequest? = null
        var fileName: String? = null

        multiPart.forEachPart { partData ->
            when(partData){
                is PartData.FormItem -> {
                   if (partData.name == "adding_item_data"){
                       addItemRequest = gson.fromJson(
                           partData.value,
                           AddItemRequest::class.java
                       )
                   }
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

        addItemRequest?.let {
            val itemImageUrl = "${BASE_URL}items_pictures/$fileName"
            val addedItemAcknowledge = shoppingItemService.addNewItem(
                request = it,
                itemImageUrl = itemImageUrl
            )
            if (addedItemAcknowledge){
                call.respond(OK,SimpleResponse(true,"Successfully added new item!"))
            } else {
                File("$ITEMS_PICTURE_PATH$fileName").delete()
                call.respond(InternalServerError)
            }
        } ?: kotlin.run {
            call.respond(BadRequest)
            return@post
        }
    }
}

fun Route.getAllItemsRoute(shoppingItemService: ShoppingItemService){
    get("/api/items/get") {
        val page = call.parameters[QueryParams.PARAM_PAGE]?.toInt() ?: 0
        val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toInt() ?: Constants.DEFAULT_PAGE_SIZE
        val items = shoppingItemService.getItems(page,pageSize)
        call.respond(OK,items)
    }
}

fun Route.updateItemRoute(shoppingItemService: ShoppingItemService) {
    val gson: Gson by inject()

    put("/api/item/update") {
        val itemId = call.parameters[QueryParams.PARAM_ITEM_ID] ?: kotlin.run {
            call.respond(NotFound)
            return@put
        }
        val multiPart = call.receiveMultipart()
        var updateItemRequest: UpdateItemRequest? = null
        var fileName: String? = null

        multiPart.forEachPart { partData ->
            when(partData){
                is PartData.FormItem -> {
                    if (partData.name == "updating_item_data"){
                        updateItemRequest = gson.fromJson(
                            partData.value,
                            UpdateItemRequest::class.java
                        )
                    }
                }
                is PartData.FileItem -> {
                    fileName = partData.saveFile(ITEMS_PICTURE_PATH)
                }
                else -> Unit
            }
            partData.dispose
        }

        updateItemRequest?.let {
            val itemImageUrl = "${BASE_URL}items_pictures/$fileName"
            val updateItemAcknowledge = shoppingItemService.updateItem(
                itemId = itemId,
                request = it,
                newImageUrl = itemImageUrl
            )
            if (updateItemAcknowledge){
                call.respond(OK,SimpleResponse(true,"Successfully updated item!"))
            } else {
                File("$ITEMS_PICTURE_PATH$fileName").delete()
                call.respond(InternalServerError)
            }
        } ?: kotlin.run {
            call.respond(BadRequest)
            return@put
        }

    }
}

fun Route.deleteItemRoute(shoppingItemService: ShoppingItemService){
    delete("/api/item/remove") {
        val itemId = call.parameters[QueryParams.PARAM_ITEM_ID] ?: kotlin.run {
            call.respond(NotFound)
            return@delete
        }
        val deleteAcknowledge = shoppingItemService.deleteItem(itemId)
        if (deleteAcknowledge) {
            call.respond(OK,SimpleResponse(true, "Successfully deleted the item: $itemId"))
        } else {
            call.respond(OK, SimpleResponse(false,"Something went wrong"))
        }

    }
}

fun Route.searchForItemRoute(shoppingItemService: ShoppingItemService){
    get("/api/items/search") {
        val query = call.parameters[QueryParams.PARAM_SEARCH_QUERY]
        if (query == null || query.isBlank()){
            call.respond(BadRequest)
            return@get
        }
        val searchResult = shoppingItemService.searchForItem(query)
        call.respond(OK,searchResult)
    }
}