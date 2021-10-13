package com.omarahmed.services

import com.omarahmed.data.models.ShoppingItem
import com.omarahmed.data.repository.ShoppingItemsRepo
import com.omarahmed.data.requests.AddItemRequest

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

    suspend fun getItems(): List<ShoppingItem> {
        return shoppingItemsRepo.getItems()
    }
}