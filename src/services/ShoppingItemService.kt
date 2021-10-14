package com.omarahmed.services

import com.omarahmed.data.models.ShoppingItem
import com.omarahmed.data.repository.ShoppingItemsRepo
import com.omarahmed.data.requests.AddItemRequest
import com.omarahmed.data.requests.UpdateItemRequest

class ShoppingItemService(
    private val shoppingItemsRepo: ShoppingItemsRepo
) {

    suspend fun addNewItem(request: AddItemRequest, itemImageUrl: String): Boolean {
        return shoppingItemsRepo.addNewItem(
            ShoppingItem(
                name = request.itemName,
                imageUrl = itemImageUrl
            )
        )
    }

    suspend fun getItems(page: Int, pageSize: Int): List<ShoppingItem> {
        return shoppingItemsRepo.getItems(page, pageSize)
    }

    suspend fun updateItem(
        itemId: String,
        request: UpdateItemRequest,
        newImageUrl: String
    ): Boolean{
        return shoppingItemsRepo.updateItem(itemId,request,newImageUrl)
    }

    suspend fun deleteItem(itemId: String): Boolean {
        return shoppingItemsRepo.deleteItem(itemId)
    }
}