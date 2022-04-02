package com.omarahmed.services

import com.omarahmed.data.models.ShoppingItem
import com.omarahmed.data.repository.shopoingItem.ShoppingItemsRepo
import com.omarahmed.data.requests.UpdateItemRequest

class ShoppingItemService(
    private val shoppingItemsRepo: ShoppingItemsRepo
) {

    suspend fun addNewItem(
        userId: String,
        itemName: String,
        itemIconUrl: String?
    ): Boolean {
        return shoppingItemsRepo.addNewItem(
            ShoppingItem(
                userId = userId,
                name = itemName,
                imageUrl = itemIconUrl
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

    suspend fun updateAllItem(ids: List<String>): Boolean {
        return shoppingItemsRepo.updateAllItems(ids)
    }

    suspend fun deleteItem(itemId: String): Boolean {
        return shoppingItemsRepo.deleteItem(itemId)
    }

    suspend fun searchForItem(userId: String, query: String): List<ShoppingItem>{
        return shoppingItemsRepo.searchForItem(userId,query)
    }
}