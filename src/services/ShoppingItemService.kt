package com.omarahmed.services

import com.omarahmed.data.models.ShoppingItem
import com.omarahmed.data.repository.shopoingItem.ShoppingItemsRepo
import com.omarahmed.data.repository.user.UserRepository
import com.omarahmed.data.requests.AddItemRequest
import com.omarahmed.data.requests.UpdateItemRequest

class ShoppingItemService(
    private val shoppingItemsRepo: ShoppingItemsRepo
) {

    suspend fun addNewItem(
        userId: String,
        itemName: String,
        itemImageUrl: String?
    ): Boolean {
        return shoppingItemsRepo.addNewItem(
            ShoppingItem(
                userId = userId,
                name = itemName,
                imageUrl = itemImageUrl
            )
        )
    }

    suspend fun getItemsByUserId(
        userId: String,
        page: Int,
        pageSize: Int
    ): List<ShoppingItem> {
        return shoppingItemsRepo.getItemsByUserId(userId,page, pageSize)
    }

    suspend fun updateItem(
        itemId: String,
        request: UpdateItemRequest,
    ): Boolean{
        return shoppingItemsRepo.updateItem(itemId,request)
    }

    suspend fun deleteItem(itemId: String): Boolean {
        return shoppingItemsRepo.deleteItem(itemId)
    }

    suspend fun searchForItem(query: String): List<ShoppingItem>{
        return shoppingItemsRepo.searchForItem(query)
    }
}